# 集成测试类继承体系重构

重构 authorization 模块 `com.janwee.bookstore.authorization.core.northbound.remote.rest.SigningUpResourceIntegrationTest` 集成测试类。要求：
1. 参照 `com.janwee.bookstore.authorization.core.northbound.remote.rest.UserResourceCoreIntegrationTest` 集成测试类继承 `com.janwee.bookstore.authorization.core.northbound.remote.rest.RestApiIntegrationTestSupport`。

## 完成记录

- `SigningUpResourceIntegrationTest` 已继承 `RestApiIntegrationTestSupport`。
- 删除该测试类中重复的 Spring Boot/MockMvc 配置、仓储注入和清库逻辑，复用公共集成测试基类。

## 验证记录

- 2026-06-07：执行 `./gradlew :authorization:integrationTest --info --stacktrace --console=plain`，通过。
