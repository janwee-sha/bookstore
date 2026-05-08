# 代码结构

`order` 模块采用围绕订单业务能力组织的六边形架构的 DDD 设计风格。新增或调整功能时，请优先遵循以下约定：

- `northbound.remote` 承载北向入口适配器逻辑，例如 REST 资源和消息订阅者，不应直接承载复杂业务规则。
- `northbound.local` 承载北向入口端口逻辑，例如本地应用服务。
- `northbound.message` 承载北向入口消息，例如 REST 资源的请求、响应对象。
- `domain` 承载领域模型，应避免依赖 Spring Web、Feign、数据库实体或消息中间件实现。
- `southbound.port` 承载南向出口端口逻辑，例如领域模型的仓储接口。
- `southbound.adapter` 承载南向出口的适配器逻辑，例如依赖 RabbitMQ 的领域事件发布器实现。
- `southbound.message` 承载南向出口消息，例如向下游服务发布的领域事件消息。

推荐包结构如下：

```
order
└─core                  ——> 核心子域
    ├─domain                ——> 领域层
    ├─northbound            ——> 入站侧
    │  ├─local              ——> 本地应用服务
    │  ├─message            ——> 入站消息对象
    │  └─remote             ——> 远程入口适配
    │      ├─resource       ——> REST 资源
    │      └─subscriber     ——> 消息订阅者
    └─southbound            ——> 出站侧
        ├─adapter           ——> 外部系统/技术适配
        ├─message           ——> 出站消息对象
        └─port              ——> 出站端口抽象
```
