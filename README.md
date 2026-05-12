# Bookstore Demo项目

一个基于 Spring Cloud，使用 Gradle Wrapper 构建的多模块微服务书店 Demo 项目。

## 项目结构

```
bookstore/
├── authorization/    # 认证授权服务
├── book/             # 图书服务
├── config/           # 配置中心
├── eureka/           # 服务注册与发现中心
├── foundation/       # 基础库模块（共享组件）
├── gateway/          # API 网关
└── order/            # 订单服务
```

各服务模块一般具有如下的目录结构：
- `README.md`：模块介绍文档。
- `src/main` 目录：存放实际业务代码。
- `src/test` 目录：存放单元测试代码。
- `src/integrationTest` 目录：存放集成测试代码。

## 环境要求

- JDK 17 或更高版本
- RabbitMQ

## 构建命令

### 构建并验证整个项目

```bash
./gradlew clean build --info --stacktrace --console=plain
```

### 构建并验证特定模块（以 book 模块为例）

```bash
./gradlew :book:clean :book:build --info --stacktrace --console=plain
```

### 仅构建项目（不运行验证）
```bash
./gradlew build -x check --info --stacktrace --console=plain
```

### 并行构建项目（不运行验证）

```bash
./gradlew build -x check --parallel --info --stacktrace --console=plain
```

### 仅构建特定模块（以 book 模块为例，不运行验证）
```bash
./gradlew :book:build -x check --info --stacktrace --console=plain
```

### 运行验证
```bash
./gradlew check --info --stacktrace --console=plain
```
### 运行特定模块的验证
```bash
./gradlew :book:check --info --stacktrace --console=plain
```

### 查看项目依赖树
```bash
./gradlew dependencies
```

### 查看特定模块（如book）的依赖树
```bash
./gradlew :book:dependencies
```

### 运行 Spring Boot 应用（按建议启动顺序）

1. **配置中心**: `./gradlew :config:bootRun`
2. **服务注册中心**: `./gradlew :eureka:bootRun`
3. **业务服务** : 任意顺序执行`./gradlew :book:bootRun`, `./gradlew :order:bootRun`和 `./gradlew :authorization:bootRun`
4. **API网关**: `./gradlew :gateway:bootRun`

### 构建可执行的JAR文件
```bash
./gradlew :config:bootJar
./gradlew :eureka:bootJar
./gradlew :book:bootJar
./gradlew :order:bootJar
./gradlew :authorization:bootJar
./gradlew :gateway:bootJar
```
