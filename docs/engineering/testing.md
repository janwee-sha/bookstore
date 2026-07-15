# 测试与验证指南

本指南细化项目宪章中的“按风险分层验证”原则。构建和运行命令以根目录 `README.md` 为准。

## 测试层级

- 单元测试验证不依赖真实技术设施的领域规则、应用编排、转换逻辑和适配器边界行为，位于
  `<module>/src/test`。
- 集成测试验证持久化、REST、安全过滤链、Spring 装配、服务启动和运行配置，位于
  `<module>/src/integrationTest`。
- 契约测试验证跨服务 HTTP、消息以及由提供方和消费方共同依赖的边界模型，位于
  `<module>/src/contractTest`。

后端的 `integrationTest` 和 `contractTest` source set 由根目录 `build.gradle` 统一定义，并包含在
`check` 生命周期中。前端变更使用 `frontend/package.json` 中与改动相符的脚本验证。

## 选择验证范围

1. 从能够证明改动行为的最低测试层级开始。
2. 修改持久化、REST、安全、启动或配置装配时，增加或更新集成测试。
3. 修改接口、DTO、消息模型、仓储端口、REST 行为或跨模块夹具时，使用 `rg` 搜索所有
   `src/contractTest` 引用，识别受影响的提供方和消费方，并运行对应契约测试。
4. 先运行受影响模块的验证；跨模块或共享构建逻辑发生变化时，再运行根项目 `check`。

例如，`book` 的 provider 行为变化可能同时要求运行 `:book:contractTest` 与
`:order:contractTest`，不能只验证 `book` 模块。

## 常用命令

```bash
./gradlew :<module>:test --info --stacktrace --console=plain
./gradlew :<module>:integrationTest --info --stacktrace --console=plain
./gradlew :<module>:contractTest --info --stacktrace --console=plain
./gradlew :<module>:check --info --stacktrace --console=plain
./gradlew check --info --stacktrace --console=plain
```

验证结果必须说明实际运行的范围。因环境、时间或用户限制未执行的测试必须连同剩余风险一起记录。
