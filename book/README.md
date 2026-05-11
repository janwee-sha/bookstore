# 代码结构

`book` 模块采用围绕图书业务能力组织的分层架构的 DDD 设计风格。新增或调整功能时，请优先遵循以下约定：

- `core`：核心子域。
- `core.presentation` 负责对外暴露 REST 接口、接收请求参数并返回响应对象，不承载业务规则。
- `core.application` 负责编排用例流程，协调领域对象、领域服务和仓储接口完成业务动作。
- `core.domain` 承载核心子域领域模型，应避免依赖技术实现。
- `core.infrastructure` 负责适配外部技术设施，例如持久化、消息发布与消费等，并实现领域层定义的接口。
- 调用方向应是自顶向下的，如 `presentation -> application -> domain -> infrastructure`。
- 依赖方向应尽量遵循依赖倒置原则（Dependency Inversion，DI）：高层模块不应该依赖于低层模块，二者都应该依赖于抽象。
- 请求、响应、持久化记录与领域模型之间应尽量显式转换，避免跨层复用同一个对象导致职责混淆。
