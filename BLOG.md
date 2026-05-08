# 使用 Jenkins、Gradle、Docker 和 GitHub 为 bookstore 搭建一个 CI/CD 管道

## 1. 什么是 CI/CD

CI/CD 是持续集成与持续交付/部署的合称。

持续集成，Continuous Integration，强调在代码提交后自动完成拉取源码、依赖解析、编译、测试、打包等动作，尽早发现集成问题。

持续交付或持续部署，Continuous Delivery / Continuous Deployment，则进一步将构建产物自动交付到目标环境中。对于容器化应用来说，一个常见流程是：构建 Docker 镜像、推送到镜像仓库，然后由目标环境拉取镜像并启动服务。

本文将使用：

* GitHub 作为源码仓库；
* Jenkins 作为 CI/CD 调度工具；
* Gradle 作为 Java 多模块项目的构建工具；
* Docker 和 Docker Compose 作为镜像构建与单机多服务部署工具。

需要说明的是，本文演示的是一个**单机 CI/CD Demo**：Jenkins、Docker、PostgreSQL、RabbitMQ 和 `bookstore` 应用容器都运行在同一台 Linux 主机上。它适合学习 CI/CD 基本流程，但不等同于生产级方案。生产环境通常还需要独立的 Jenkins Agent、镜像仓库、部署环境隔离、健康检查、灰度发布、回滚策略和更严格的凭据管理。

Jenkins 官方也建议不要让 Jenkins Controller 直接执行构建任务，而应将构建任务交给 Agent 执行；如果暂时没有独立 Agent，也至少应理解这种单机 Demo 的安全边界。([Jenkins][1])

---

## 2. 准备 bookstore 项目结构

本文使用的示例项目是 `bookstore`，它是一个 Spring Cloud + Gradle 的多模块服务。仓库结构如下：

```text
bookstore/
├── authorization/             # 认证授权服务，容器端口 7030
│   └── Dockerfile
├── book/                      # 图书服务，容器端口 7100
│   └── Dockerfile
├── config/                    # Spring Cloud Config Server，容器端口 7000
│   └── Dockerfile
├── eureka/                    # Eureka 服务注册中心，容器端口 7010
│   └── Dockerfile
├── foundation/                # 共享基础库，不单独构建镜像
├── gateway/                   # API 网关，容器端口 8080
│   └── Dockerfile
├── order/                     # 订单服务，容器端口 7120
│   └── Dockerfile
├── docker/postgres/initdb/    # PostgreSQL 初始化脚本
├── docker-compose.yml
├── build.gradle
├── settings.gradle
├── Jenkinsfile
└── .dockerignore
```

`settings.gradle` 中包含这些模块：

```groovy
rootProject.name = "bookstore"

include("foundation")
include("config")
include("eureka")
include("gateway")
include("book")
include("order")
include("authorization")
```

其中 `foundation` 是共享库模块，不需要独立运行，也不需要单独的 Docker 镜像。其余 6 个模块都是可以启动的 Spring Boot 服务。

本项目使用 Java 17 和 Gradle Wrapper。Jenkins 中可以直接执行：

```bash
./gradlew clean test
```

---

## 3. 编写各服务 Dockerfile

原文中手动将 Dockerfile 拷贝到 Jenkins workspace 的方式并不推荐。Dockerfile 应该作为项目的一部分提交到 Git 仓库中，这样 Jenkins 每次拉取代码时都会得到一致的构建定义。

这里使用 Docker 多阶段构建：第一阶段使用 Gradle 构建指定模块，第二阶段只保留运行应用所需的 JRE 和 jar 包。Docker 官方也建议使用多阶段构建来分离构建环境和运行环境，从而减小最终镜像体积并降低攻击面。([Docker Documentation][2])

因为 `bookstore` 是 Gradle 多模块项目，构建镜像时应该把仓库根目录作为 Docker build context，并通过 `-f <module>/Dockerfile` 指定具体模块的 Dockerfile。例如：

```bash
docker build -f book/Dockerfile -t bookstore/book:local .
```

`book/Dockerfile` 的定义如下：

```dockerfile
FROM gradle:9.0.0-jdk17 AS build

WORKDIR /workspace

COPY --chown=gradle:gradle . .

RUN gradle :book:bootJar --no-daemon


FROM eclipse-temurin:17-jre

LABEL maintainer="Janwee <janwee_sha@outlook.com>"

WORKDIR /app

RUN groupadd --system app && useradd --system --gid app --home-dir /app --shell /usr/sbin/nologin app

COPY --from=build /workspace/book/build/libs/*.jar app.jar

USER app:app

EXPOSE 7100

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

其他服务的 Dockerfile 与它保持同样结构，只需要替换 Gradle 任务、jar 所在目录和端口：

| 模块 | Gradle 任务 | jar 路径 | 容器端口 |
| --- | --- | --- | --- |
| `config` | `:config:bootJar` | `/workspace/config/build/libs/*.jar` | `7000` |
| `eureka` | `:eureka:bootJar` | `/workspace/eureka/build/libs/*.jar` | `7010` |
| `authorization` | `:authorization:bootJar` | `/workspace/authorization/build/libs/*.jar` | `7030` |
| `book` | `:book:bootJar` | `/workspace/book/build/libs/*.jar` | `7100` |
| `order` | `:order:bootJar` | `/workspace/order/build/libs/*.jar` | `7120` |
| `gateway` | `:gateway:bootJar` | `/workspace/gateway/build/libs/*.jar` | `8080` |

这里有几个细节：

1. 使用 Java 17，与项目 Gradle toolchain 保持一致；
2. 使用多阶段构建，最终镜像中不包含 Gradle、源码和构建缓存；
3. 使用 `COPY`，而不是 `ADD`；
4. 从 `build/libs` 复制 Gradle 生成的 Spring Boot jar，而不是 Maven 常见的 `target` 目录；
5. 使用非 root 用户运行应用；
6. 每个服务暴露自己的实际监听端口。

再创建 `.dockerignore`，减少 Docker build context：

```dockerignore
.git
.gradle
**/build
out
*.iml
.idea
.vscode
.codex
BLOG.md
README.md
```

---

## 4. 添加 Docker Compose 单机部署文件

`bookstore` 不是单个 Web 应用，而是一组相互依赖的微服务。单独使用一条 `docker run` 已经不够直观，因此这里使用 Docker Compose 启动：

* PostgreSQL；
* RabbitMQ；
* Config Server；
* Eureka；
* Authorization；
* Book；
* Order；
* Gateway。

`docker-compose.yml` 的核心结构如下：

```yaml
services:
  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: bookstore
      POSTGRES_USER: dev
      POSTGRES_PASSWORD: dev
    volumes:
      - bookstore-postgres-data:/var/lib/postgresql/data
      - ./docker/postgres/initdb:/docker-entrypoint-initdb.d:ro

  rabbitmq:
    image: rabbitmq:3.13-management-alpine
    ports:
      - "5672:5672"
      - "15672:15672"

  config:
    image: bookstore/config:local
    build:
      context: .
      dockerfile: config/Dockerfile
    environment:
      SPRING_PROFILES_ACTIVE: native
    ports:
      - "7000:7000"

  eureka:
    image: bookstore/eureka:local
    build:
      context: .
      dockerfile: eureka/Dockerfile
    depends_on:
      - config
    environment:
      SPRING_CONFIG_IMPORT: "configserver:http://config:7000"
      EUREKA_INSTANCE_HOSTNAME: eureka
      EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: "http://eureka:7010/eureka/"
    ports:
      - "7010:7010"
```

业务服务同样通过环境变量覆盖容器网络中的地址，例如 `book` 服务：

```yaml
book:
  image: bookstore/book:local
  build:
    context: .
    dockerfile: book/Dockerfile
  depends_on:
    - config
    - eureka
    - postgres
    - rabbitmq
  environment:
    SPRING_CONFIG_IMPORT: "configserver:http://config:7000"
    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: "http://eureka:7010/eureka/"
    SPRING_DATASOURCE_URL: "jdbc:postgresql://postgres:5432/bookstore?currentSchema=book&useUnicode=true&characterEncoding=UTF-8&rewriteBatchedStatements=true&serverTimezone=Asia/Shanghai"
    SPRING_RABBITMQ_ADDRESSES: "rabbitmq:5672"
  ports:
    - "7100:7100"
```

这里最容易踩坑的是 `localhost`。在容器内部，`localhost` 指的是容器自己，而不是宿主机，也不是其他服务容器。所以 Config Server、Eureka、PostgreSQL 和 RabbitMQ 都要使用 Compose service name：

```text
config:7000
eureka:7010
postgres:5432
rabbitmq:5672
```

PostgreSQL 初始化脚本用于创建业务 schema：

```sql
CREATE SCHEMA IF NOT EXISTS book;
CREATE SCHEMA IF NOT EXISTS "order";
```

本地验证可以直接执行：

```bash
docker compose up -d --build
```

---

## 5. 安装 Jenkins

Jenkins 官方提供了 `jenkins/jenkins` Docker 镜像，官方文档建议使用该镜像运行 Jenkins LTS 版本；同时，Docker Hub 上的 Jenkins 镜像也建议避免直接依赖 `latest`，而是固定具体版本或 LTS 标签。([Jenkins][3]) ([Docker Hub][4])

先创建数据目录：

```bash
mkdir -p ~/docker/jenkins
```

将宿主机数据目录的写权限赋予容器内的 jenkins 用户：

```shell
chown -R 1000:1000 ~/docker/jenkins
```

然后启动 Jenkins：

```bash
docker run -d \
  --name jenkins \
  --restart=unless-stopped \
  -p 8080:8080 \
  -p 50000:50000 \
  -v ~/docker/jenkins:/var/jenkins_home \
  jenkins/jenkins:lts-jdk21
```

查看容器状态：

```bash
docker ps
```

查看初始化管理员密码：

```bash
docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

然后在浏览器访问：

```text
http://<your_server_ip>:8080
```

在初始化页面粘贴刚才输出的管理员密码，进入插件安装页面。

为了让这个教程保持最小化，不需要安装所有推荐插件。选择：

```text
Select plugins to install
```

保留或勾选下面这些最少必要插件：

| 插件 | 用途 |
| --- | --- |
| `Pipeline` | 支持 `Jenkinsfile` 和 Pipeline 任务 |
| `Git` | 从 GitHub 拉取源码 |
| `SSH Credentials` | 使用 SSH Deploy Key 访问 GitHub 私有仓库 |
| `Credentials Binding` | 在 Pipeline 中安全读取 Jenkins Credentials |
| `Docker Pipeline` | 提供 Docker 相关 Pipeline 能力；本教程主要使用 Docker CLI，但保留该插件便于后续扩展 |

插件安装完成后，Jenkins 会要求创建第一个管理员用户。建议创建一个正式管理员账号，而不是长期使用初始化密码登录，例如：

```text
Username: admin
Password: <your_strong_password>
Full name: Bookstore Admin
E-mail address: <your_email>
```

然后进入实例配置页面。`Jenkins URL` 在内网 WSL Demo 中可以填写当前浏览器访问 Jenkins 的地址，例如：

```text
http://localhost:8080/
```

如果 Jenkins 是部署在另一台内网机器上，则填写该机器在内网中的访问地址：

```text
http://<your_server_ip>:8080/
```

保存后即可进入 Jenkins 首页。

---

## 6. 配置 Jenkins 安全边界

为了简化演示，本文使用单机部署。但仍然建议将 Jenkins Controller 的 executor 数量设置为 0，避免构建任务直接运行在 Controller 上。

进入：

```text
Dashboard -> Manage Jenkins -> Nodes
```

选择：

```text
Built-In Node -> Configure
```

将：

```text
Number of executors
```

设置为：

```text
0
```

Jenkins 官方文档说明，直接在 built-in node 上运行构建任务不利于安全性、性能和可扩展性，长期使用时应将构建任务交给 agent 执行。([Jenkins][5])

对于本文这种单机 Demo，你可以创建一个本机 Agent，或者在学习阶段临时使用 built-in node。为了更贴近真实 CI/CD 流程，推荐使用独立 Agent。

---

## 7. 让 Jenkins 可以构建 Docker 镜像

Jenkins 官方文档提供了一种 Docker-in-Docker 的安装方式：让 Jenkins 连接到一个独立的 Docker daemon，而不是直接把宿主机的 `/var/run/docker.sock` 暴露给 Jenkins。官方示例中也明确说明，Docker-in-Docker 需要 privileged 权限，并建议通过 TLS 连接 Docker daemon。([Jenkins][3])

创建 Jenkins 专用网络：

```bash
docker network create jenkins
```

启动 Docker-in-Docker 容器：

```bash
docker run \
  --name jenkins-docker \
  --rm \
  --detach \
  --privileged \
  --network jenkins \
  --network-alias docker \
  --env DOCKER_TLS_CERTDIR=/certs \
  --volume jenkins-docker-certs:/certs/client \
  --volume jenkins-data:/var/jenkins_home \
  docker:dind \
  --storage-driver overlay2
```

接下来创建一个自定义 Jenkins 镜像，让 Jenkins 容器内包含 Docker CLI 和 Docker Pipeline 插件。

新建 `Dockerfile.jenkins`：

```dockerfile
FROM jenkins/jenkins:lts-jdk21

USER root

RUN apt-get update && \
    apt-get install -y lsb-release ca-certificates curl && \
    install -m 0755 -d /etc/apt/keyrings && \
    curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc && \
    chmod a+r /etc/apt/keyrings/docker.asc && \
    echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/debian $(. /etc/os-release && echo "$VERSION_CODENAME") stable" \
      > /etc/apt/sources.list.d/docker.list && \
    apt-get update && \
    apt-get install -y docker-ce-cli docker-compose-plugin && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

USER jenkins

RUN jenkins-plugin-cli --plugins "docker-workflow git workflow-aggregator ssh-credentials credentials-binding"
```

构建 Jenkins 镜像：

```bash
docker build -f Dockerfile.jenkins -t my-jenkins:lts-jdk21 .
```

停止并删除前面启动的 Jenkins 容器：

```bash
docker stop jenkins
docker rm jenkins
```

使用自定义镜像重新启动 Jenkins：

```bash
docker run -d \
  --name jenkins \
  --restart=unless-stopped \
  --network jenkins \
  -p 8080:8080 \
  -p 50000:50000 \
  -v jenkins-data:/var/jenkins_home \
  -v jenkins-docker-certs:/certs/client:ro \
  -e DOCKER_HOST=tcp://docker:2376 \
  -e DOCKER_CERT_PATH=/certs/client \
  -e DOCKER_TLS_VERIFY=1 \
  my-jenkins:lts-jdk21
```

这种方式比直接挂载宿主机的 `/var/run/docker.sock` 更适合教学演示，也避免了把宿主机的 `/etc/passwd`、`/etc/group`、`/etc/shadow` 等敏感文件挂载进 Jenkins 容器。

---

## 8. 配置 GitHub 凭据

进入 Jenkins：

```text
Dashboard -> Manage Jenkins -> Credentials
```

添加一组 GitHub 凭据。

如果使用 SSH 拉取仓库，建议使用 GitHub Deploy Key，而不是把 Jenkins 生成的 SSH key 添加到账户级 SSH Keys 中。Deploy Key 可以限制到单个仓库，权限边界更清晰。

推荐流程如下：

1. 在本地或安全环境中生成 SSH key；
2. 将公钥添加到 GitHub 仓库的 Deploy keys；
3. 将私钥添加到 Jenkins Credentials；
4. Jenkins Pipeline 中通过凭据 ID 使用该私钥。

凭据类型选择：

```text
SSH Username with private key
```

假设凭据 ID 设置为：

```text
github-deploy-key
```

后续创建 Pipeline 时会用到它。

---

## 9. 创建 Jenkinsfile

Jenkins Pipeline 可以把构建流程声明为代码，并和项目源码一起提交到 Git 仓库中。Jenkins 官方文档也说明，Pipeline 可以在 `Jenkinsfile` 中直接使用 Docker 镜像作为构建环境，并支持构建容器镜像、使用 Docker registry 等能力。([Jenkins][6])

在项目根目录创建 `Jenkinsfile`：

```groovy
pipeline {
    agent any

    triggers {
        pollSCM('H/5 * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Gradle Test') {
            steps {
                sh './gradlew clean test'
            }
        }

        stage('Build Docker Images') {
            steps {
                sh 'docker compose build --pull'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    docker compose down --remove-orphans
                    docker compose up -d
                '''
            }
        }
    }

    post {
        always {
            sh 'docker image prune -f || true'
        }

        success {
            echo 'Bookstore deployed successfully: http://<your_server_ip>:8080'
        }

        failure {
            echo 'Pipeline failed. Please check the Jenkins console output.'
        }
    }
}
```

这条 Pipeline 完成了以下步骤：

1. Jenkins 每 5 分钟轮询一次 GitHub 仓库；
2. 检测到目标分支有新提交后拉取源码；
3. 执行 Gradle 测试；
4. 按模块 Dockerfile 构建镜像，每个 Dockerfile 在构建阶段执行对应模块的 `bootJar`；
5. 使用 Docker Compose 重新启动整组服务；
6. 清理悬空镜像。

在这个 Demo 中，`bookstore` 的最终入口是 API 网关：

```text
http://<your_server_ip>:8080
```

各服务端口如下：

| 服务 | 宿主机端口 | 容器端口 |
| --- | --- | --- |
| Config Server | `7000` | `7000` |
| Eureka | `7010` | `7010` |
| Authorization | `7030` | `7030` |
| Book | `7100` | `7100` |
| Order | `7120` | `7120` |
| Gateway | `8080` | `8080` |
| RabbitMQ Management | `15672` | `15672` |

---

## 10. 创建 Jenkins Pipeline 任务

进入 Jenkins 首页，点击：

```text
New Item
```

输入任务名称，例如：

```text
bookstore-pipeline
```

任务类型选择：

```text
Pipeline
```

在 Pipeline 配置中选择：

```text
Pipeline script from SCM
```

SCM 选择：

```text
Git
```

填写 GitHub 仓库地址，例如：

```text
git@github.com:yourname/bookstore.git
```

选择前面配置的 Jenkins 凭据：

```text
github-deploy-key
```

指定分支，例如：

```text
*/main
```

Script Path 保持：

```text
Jenkinsfile
```

保存任务。

如果这是第一次创建任务，可以先手动点击一次：

```text
Build Now
```

这样 Jenkins 会先拉取仓库并读取 `Jenkinsfile`，随后 `pollSCM('H/5 * * * *')` 才会作为 Pipeline 触发器生效。

---

## 11. 配置 Jenkins 轮询 GitHub

本文假设 Jenkins 和 `bookstore` 应用服务部署在内网机器上，例如本地 Windows 的 WSL 环境中。此时 GitHub 无法直接访问内网 Jenkins，因此不使用 GitHub Webhook，而改用 Jenkins 主动轮询 GitHub 仓库。

上面 `Jenkinsfile` 中的触发器是：

```groovy
triggers {
    pollSCM('H/5 * * * *')
}
```

它表示 Jenkins 按哈希分散后的时间点，大约每 5 分钟检查一次 SCM。相比固定写成 `*/5 * * * *`，`H/5 * * * *` 可以避免多个 Jenkins Job 在同一秒同时发起轮询。

轮询模式的触发流程如下：

```text
Jenkins 定时轮询 GitHub
  -> 发现 main 分支有新提交
  -> 触发 Pipeline
  -> Checkout
  -> Gradle Test
  -> Docker Compose Build
  -> Docker Compose Deploy
```

如果不想把触发器写在 `Jenkinsfile` 中，也可以在 Jenkins Job 页面配置：

```text
Configure -> Build Triggers -> Poll SCM
```

Schedule 填写：

```text
H/5 * * * *
```

两种方式选择一种即可。本文推荐写在 `Jenkinsfile` 中，因为触发策略也会进入 Git 版本管理。

需要注意，轮询不是实时触发。代码 push 到 GitHub 后，Jenkins 会在下一次轮询时发现变更，所以可能有几分钟延迟。对于内网 WSL Demo，这个延迟通常可以接受。

如果想立即验证整个流程，也可以手动点击：

```text
Build Now
```

验证整个流程是否能跑通。

---

## 12. 验证部署结果

点击 Jenkins 任务中的：

```text
Build Now
```

等待 Pipeline 执行完成。也可以先在服务器上手动执行一次：

```bash
docker compose up -d --build
```

查看应用容器：

```bash
docker compose ps
```

应该能看到类似结果：

```text
NAME                      IMAGE                         STATUS
bookstore-config          bookstore/config:local         Up
bookstore-eureka          bookstore/eureka:local         Up
bookstore-authorization   bookstore/authorization:local  Up
bookstore-book            bookstore/book:local           Up
bookstore-order           bookstore/order:local          Up
bookstore-gateway         bookstore/gateway:local        Up
```

访问网关：

```text
http://<your_server_ip>:8080
```

访问 Eureka 控制台：

```text
http://<your_server_ip>:7010
```

如果服务能在 Eureka 中完成注册，且网关能够转发到业务服务，说明这条单机 CI/CD Demo 已经搭建完成。

注意：`depends_on` 只保证容器启动顺序，不保证服务已经就绪。生产环境应增加健康检查、启动重试、部署失败回滚等机制。

---

## 13. 这条管道还有哪些可以继续优化的地方

本文演示的是最小可运行流程。如果要进一步接近生产环境，可以继续优化：

1. **使用独立 Jenkins Agent**

   不让 Jenkins Controller 执行构建任务。Controller 只负责任务调度、权限管理和 UI，构建任务交给 Agent 执行。

2. **推送镜像到 Registry**

   当前示例中镜像只存在于本机 Docker daemon 中。生产环境通常会推送到 Docker Hub、Harbor、AWS ECR、GitHub Container Registry 等镜像仓库。

3. **使用 Git Commit 作为镜像标签**

   例如：

   ```groovy
   IMAGE_TAG = "${env.GIT_COMMIT}"
   ```

   镜像可以命名为：

   ```text
   registry.example.com/bookstore/book:${env.GIT_COMMIT}
   registry.example.com/bookstore/order:${env.GIT_COMMIT}
   ```

   这样可以明确知道每个环境运行的是哪一次代码提交，也方便回滚。

4. **增加健康检查**

   部署后可以用 `curl` 检查关键服务健康接口：

   ```bash
   curl -f http://localhost:7000/actuator/health
   curl -f http://localhost:7010/actuator/health
   curl -f http://localhost:8080/actuator/health
   ```

   如果健康检查失败，应立即终止部署或回滚旧版本。

5. **拆分构建与部署环境**

   本文为了演示方便，让 Jenkins 直接部署到同一台主机。生产环境通常会把构建、镜像仓库和运行环境分离。

6. **引入 Kubernetes**

   Docker Compose 适合单机 Demo 和小型内部环境。多节点部署、弹性伸缩、滚动发布和服务治理通常会迁移到 Kubernetes。

7. **增加镜像扫描**

   可以在 Pipeline 中加入 Trivy、Grype 等工具扫描镜像漏洞。

8. **将凭据全部放入 Jenkins Credentials**

   不要把 GitHub Token、Registry 密码、SSH 私钥、OAuth Client Secret 写死在 Jenkinsfile、Compose 文件或 Shell 脚本中。

---

## 14. 总结

本文使用 Jenkins、Gradle、Docker、Docker Compose 和 GitHub，为 `bookstore` 多模块微服务项目搭建了一条单机 CI/CD Demo。

完整流程如下：

```text
Jenkins Poll SCM
  -> GitHub Repository
    -> Detect New Commit
    -> Jenkins Pipeline
      -> Checkout
      -> Gradle Test
      -> Build Service Images
      -> Docker Compose Deploy
      -> Verify Gateway / Eureka / Services
```

相比手工登录服务器构建和部署，这条流程已经实现了基本自动化。更重要的是，构建定义 `Jenkinsfile`、镜像定义 `Dockerfile`、多服务编排定义 `docker-compose.yml` 和应用源码都被纳入 Git 版本管理，后续修改、回滚和协作都会更加清晰。

不过，这仍然只是学习版方案。真正的生产级 CI/CD 还需要独立构建节点、镜像仓库、环境隔离、权限控制、健康检查、回滚机制和审计能力。对于个人项目或内部 Demo，本文方案已经足够帮助你理解 Jenkins、Gradle、Docker、Docker Compose 与 GitHub 如何协同完成一次自动化交付。

[1]: https://www.jenkins.io/doc/book/security/controller-isolation/ "Controller Isolation"
[2]: https://docs.docker.com/build/building/best-practices/ "Building best practices | Docker Docs"
[3]: https://www.jenkins.io/doc/book/installing/docker/ "Docker"
[4]: https://hub.docker.com/r/jenkins/jenkins "jenkins/jenkins - Docker Image"
[5]: https://www.jenkins.io/doc/book/managing/nodes/ "Managing Nodes"
[6]: https://www.jenkins.io/doc/book/pipeline/docker/ "Using Docker with Pipeline"
