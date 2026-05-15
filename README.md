# Bookstore Demo项目

一个基于 Spring Cloud，使用 Gradle Wrapper 构建的多模块微服务书店 Demo 项目。

## 项目结构

```
bookstore/
├── authorization/    # 认证授权服务
├── book/             # 图书服务
├── config/           # 配置中心
├── eureka/           # 服务注册与发现中心
├── frontend/         # React 前端应用
├── foundation/       # 基础库模块（共享组件）
├── gateway/          # API 网关
└── order/            # 订单服务
```

各服务模块一般具有如下的目录结构：
- `README.md`：模块介绍文档。
- `src/main` 目录：存放实际业务代码。
- `src/test` 目录：存放单元测试代码。
- `src/integrationTest` 目录：存放集成测试代码。
- `src/contractTest` 目录：存放跨服务 HTTP、消息等边界契约测试代码。

## 环境要求

- JDK 17 或更高版本
- Node.js 20 或更高版本（运行 `frontend`）
- RabbitMQ

## 构建命令

### 修改代码后验证特定模块（以 authorization 模块为例）

```bash
./gradlew :authorization:check --info --stacktrace --console=plain
```

### 修改代码后验证整个项目

```bash
./gradlew check --info --stacktrace --console=plain
```

### 清理、构建并验证整个项目

```bash
./gradlew clean build --info --stacktrace --console=plain
```

### 清理、构建并验证特定模块（以 authorization 模块为例）

```bash
./gradlew :authorization:clean :authorization:build --info --stacktrace --console=plain
```

### 仅运行特定模块的单元测试

```bash
./gradlew :authorization:test --info --stacktrace --console=plain
```

### 仅运行特定模块的集成测试

```bash
./gradlew :authorization:integrationTest --info --stacktrace --console=plain
```

### 仅运行特定模块的契约测试

```bash
./gradlew :order:contractTest --info --stacktrace --console=plain
```

### 仅编译项目（不运行测试）

```bash
./gradlew compileJava --info --stacktrace --console=plain
```

### 仅编译特定模块（以 authorization 模块为例）

```bash
./gradlew :authorization:compileJava --info --stacktrace --console=plain
```

### 仅生成构建产物（不运行验证）

```bash
./gradlew assemble --info --stacktrace --console=plain
```

### 仅生成特定模块的构建产物（以 authorization 模块为例，不运行验证）

```bash
./gradlew :authorization:assemble --info --stacktrace --console=plain
```

### 并行生成构建产物（不运行验证）

```bash
./gradlew assemble --parallel --info --stacktrace --console=plain
```

### 跳过所有验证执行 build

```bash
./gradlew build -x check --info --stacktrace --console=plain
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
5. **React 前端**: 在 `frontend` 目录执行 `npm install` 后运行 `npm run dev`

前端默认运行在 `http://127.0.0.1:8088`，通过 OAuth 2.0 Authorization Code + PKCE 登录 `authorization` 服务，并以 Bearer Token 调用 `gateway`。

### 构建可执行的JAR文件
```bash
./gradlew :config:bootJar
./gradlew :eureka:bootJar
./gradlew :book:bootJar
./gradlew :order:bootJar
./gradlew :authorization:bootJar
./gradlew :gateway:bootJar
```
