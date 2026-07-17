# 代码结构

`book` 模块采用围绕图书业务能力组织的分层架构的 DDD 设计风格。新增或调整功能时，请优先遵循以下约定：

- `interfaces`：展现层，负责对外暴露 REST 接口、监听外部服务消息等，不承载业务规则。
- `application`：应用层，负责编排用例流程，协调领域对象、领域服务和仓储接口完成业务动作。
- `domain`：领域层，承载领域模型，应避免依赖技术实现。
- `infrastructure`：基础设施层，负责适配外部技术设施，例如持久化、消息发布与消费、安全配置等，并实现领域层定义的接口。
- 调用方向应是自顶向下的，如 `interfaces -> application -> domain -> infrastructure`。
- 依赖方向应尽量遵循依赖倒置原则（Dependency Inversion，DI）：高层模块不应该依赖于低层模块，二者都应该依赖于抽象。
- 请求、响应、持久化记录与领域模型之间应尽量显式转换，避免跨层复用同一个对象导致职责混淆。

示例树形结构如下：
```text
book
├── application                        # 应用层
│   ├── assembler                      # 视图/命令组装器
│   ├── command                        # 命令对象
│   ├── event                          # 集成事件
│   ├── service                        # 应用服务
│   └── view                           # 视图对象
│
├── domain                             # 领域层
│   ├── exception                      # 领域异常
│   ├── model                          # 领域模型
│   ├── repository                     # 资源库接口
│   └── service                        # 领域服务
│
├── infrastructure                     # 基础设施层
│   ├── messaging                      # 消息适配器
│   ├── persistence                    # 持久化适配器
│   │   ├── assembler                  # PO 组装器
│   │   ├── entity                     # 持久化对象
│   │   ├── jpa                        # JPA Repository
│   │   └── *RepositoryJpaAdapter      # 资源库适配器
│   └── security                       # 安全配置
│
└── interfaces                         # 接口层
	  ├── rest                           # REST 资源/控制器
	  └── subscriber                     # 消息订阅者
```
