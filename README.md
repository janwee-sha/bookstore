# Bookstore Demo项目

一个基于 Spring Cloud、使用 Gradle Wrapper 构建的多模块微服务书店 Demo 项目。

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

各后端服务模块一般具有如下的目录结构：
- `README.md`：模块介绍文档。
- `src/main` 目录：存放实际业务代码。
- `src/test` 目录：存放单元测试代码。
- `src/integrationTest` 目录：存放集成测试代码。
- `src/contractTest` 目录：存放跨服务 HTTP、消息等边界契约测试代码。

## 环境要求

- JDK 17 或更高版本
- Node.js 20 或更高版本
- RabbitMQ

## 构建命令

### 后端

- macOS/Linux 首次使用 Gradle Wrapper 先在仓库根目录授予执行权限：
    
    ```bash
    chmod +x gradlew
    ```
- 验证特定模块

    ```bash
    ./gradlew :authorization:check --info --stacktrace --console=plain
    ```

- 修改代码后验证整个项目

    ```bash
    ./gradlew check --info --stacktrace --console=plain
    ```

- 仅运行特定模块的单元测试

    ```bash
    ./gradlew :authorization:test --info --stacktrace --console=plain
    ```

- 仅运行特定模块的集成测试

    ```bash
    ./gradlew :authorization:integrationTest --info --stacktrace --console=plain
    ```

- 仅运行特定模块的契约测试

    ```bash
    ./gradlew :order:contractTest --info --stacktrace --console=plain
    ```

- 仅编译项目

    ```bash
    ./gradlew compileJava --info --stacktrace --console=plain
    ```

- 仅编译特定模块（以 authorization 模块为例）

    ```bash
    ./gradlew :authorization:compileJava --info --stacktrace --console=plain
    ```

### 启动服务（按建议启动顺序）

1. **配置中心**: `./gradlew :config:bootRun`
2. **服务注册中心**: `./gradlew :eureka:bootRun`
3. **业务服务** : 任意顺序执行`./gradlew :book:bootRun`, `./gradlew :order:bootRun`和 `./gradlew :authorization:bootRun`
4. **API网关**: `./gradlew :gateway:bootRun`
5. **React 前端**: 依次执行 `cd frontend`、 `npm install` 和 `npm run dev`
