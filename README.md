# Bookstore Demo项目

一个基于 Spring Cloud 的微服务书店Demo项目，使用 Gradle 构建的多模块项目。

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

## 环境要求

- JDK 17 或更高版本
- Gradle Wrapper (已包含在项目中)
- PostgreSQL 数据库
- RabbitMQ

## 构建命令

### 清理并构建整个项目（包含测试）

```bash
./gradlew clean build
```

### 仅构建项目（不运行测试）
```bash
./gradlew build -x test
```

### 并行构建项目（不运行测试）

```bash
./gradlew build -x test --parallel
```

### 运行所有测试
```bash
./gradlew test
```
### 运行特定模块（如book）的测试
```bash
./gradlew :book:test
```

### 查看项目依赖树
```bash
./gradlew dependencies
```

### 查看特定模块（如book）的依赖树
```bash
./gradlew :book:dependencies
```

### 运行Spring Boot应用(按建议启动顺序)

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

### 开发辅助命令

```bash
# 刷新依赖
./gradlew --refresh-dependencies

# 查看可用的Gradle任务
./gradlew tasks

# 查看构建版本信息
./gradlew properties

# 生成IDE项目文件（IntelliJ IDEA）
./gradlew idea

# 生成IDE项目文件（Eclipse）
./gradlew eclipse
```

## 问题排查

### 查看详细的构建日志
```bash
./gradlew build --info
```
### 查看堆栈跟踪
```bash
./gradlew build --stacktrace
```

### 使用Gradle扫描诊断构建问题
```bash
./gradlew build --scan
```

## 注意事项

1. 首次构建可能需要较长时间下载依赖
2. 确保所有服务的配置正确指向配置中心
3. 服务启动前确保 Eureka 和 Config 服务已正常运行
