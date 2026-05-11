# 代码结构

`order` 模块采用围绕订单业务能力组织的六边形架构的 DDD 设计风格。新增或调整功能时，请优先遵循以下约定：

- `core`：核心子域。
- `core.domain` 承载核心子域领域模型，应避免依赖技术实现。
- `core.northbound.remote` 承载北向入口适配器逻辑，例如 REST 资源和消息订阅者，不应直接承载复杂业务规则。
- `core.northbound.local` 承载北向入口端口逻辑，例如本地应用服务。
- `core.northbound.message` 承载北向入口消息，例如 REST 资源的请求、响应对象、从上游服务订阅的消息模型等。
- `core.southbound.port` 承载南向出口端口逻辑，例如领域模型的仓储接口。
- `core.southbound.adapter` 承载南向出口的适配器逻辑，例如依赖 RabbitMQ 的领域事件发布器实现。
- `core.southbound.message` 承载南向出口消息，例如向下游服务发布的异步消息模型、依赖 JPA 的持久化模型实现等。