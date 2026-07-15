# 运行配置指南

本指南细化项目宪章中的“配置与环境一致性”原则。

## 配置来源

服务的有效配置由模块本地 `application.yml`、Config Server 配置以及启动时选择的环境共同决定。
分析运行问题时，至少检查：

- 服务模块的 `src/main/resources/application.yml`；
- `config/src/main/resources/application.yml`；
- `config/src/main/resources/config/<service>-local.yml`；
- `config/src/main/resources/config/<service>-docker.yml`。

数据源、RabbitMQ、Eureka、Gateway 路由、端口和安全配置通常跨越这些来源，不能根据单个文件推断
最终运行行为。

## 环境一致性

- `local` 与 `docker` 可以使用不同主机名、端口或凭据来源，但必须保持服务拓扑和业务语义一致。
- 新增、删除或替换基础设施依赖时，必须同步检查两套环境以及依赖该设施的服务。
- 运行时迁移必须修改实际配置；只替换测试 fixture 或测试 profile 不构成迁移完成。
- 有意保留的环境差异必须记录原因、适用范围和验证方式。

## 验证

配置变更至少需要解析或装配层面的静态检查；影响 Spring 上下文、路由、安全或外部连接时，按
`docs/engineering/testing.md` 增加并运行相应集成测试。完整的本地启动顺序以根目录 `README.md`
为准。
