# 代码结构

`order` 模块采用围绕订单业务能力组织的六边形架构的 DDD 设计风格。`core` 与 `security` 不再作为独立子域存在，模块代码统一收敛到以下六边形包结构中。新增或调整功能时，请优先遵循以下约定：

- `domain` 承载领域模型，应避免依赖技术实现。
- `northbound.remote` 承载北向入口适配器逻辑，例如 REST 资源和消息订阅者，不应直接承载复杂业务规则。
- `northbound.remote.security` 承载北向远程入口的安全配置，例如资源服务器过滤链和方法安全配置。
- `northbound.local` 承载北向入口端口逻辑，例如本地应用服务。
- `northbound.message` 承载北向入口消息，例如 REST 资源的请求、响应对象、从上游服务订阅的消息模型等。
- `southbound.port` 承载南向出口端口逻辑，例如领域模型的仓储接口。
- `southbound.adapter` 承载南向出口的适配器逻辑，例如依赖 RabbitMQ 的领域事件发布器、依赖 OpenFeign 的下游客户端、OAuth2 客户端配置等。
- `southbound.adapter.persistence` 承载依赖 JPA 的持久化模型、Spring Data 仓储和领域仓储适配器实现。
- `southbound.message` 承载南向出口消息，例如向下游服务发布的异步消息模型等。
