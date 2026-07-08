# Bookstore DDD 架构分析

本文基于当前 `bookstore` 项目的只读代码分析，重点讨论 DDD 设计中的薄弱点。判断标准不是“有没有 DDD 包名”，而是这些模型、服务、仓储和事件是否真的保护了业务边界、不变量和统一语言。

## 总体判断

当前项目更像“微服务 + 分层/六边形包结构 + 少量领域对象”，还没有真正把订单、库存、目录、授权这些模型边界讲清楚。

作为学习 Demo 可以接受，但如果拿来学习 DDD，最容易误导的地方是：项目里已经出现了聚合、领域服务、仓储、事件等 DDD 名词，实际很多地方仍然是事务脚本和技术适配包装。

## 1. `Book` 同时承担目录和库存语义

`book/src/main/java/com/janwee/bookstore/book/domain/model/Book.java` 中，`name`、`price`、`publisher`、`authorId` 是目录信息，`amount` 和 `sell()` 是库存信息。

这两个模型通常有不同的生命周期、不变量和并发压力：

- 目录：书名、作者、出版社、定价、出版状态。
- 库存：可售数量、锁定数量、扣减、补货、释放。
- 订单：下单、等待库存确认、确认、拒绝、履约。

当前 `BookApplicationService.order()` 直接根据 `Book.amount()` 判断库存并扣减，然后发布 `BookOrdered` 或 `BookSoldOut`。这让 `book` 上下文既像目录服务，又像库存确认服务，还承担订单审批流程的一部分。

更合适的 Demo 取舍：

- 如果不拆服务，至少在模型语言上拆成 `Book` 与 `InventoryItem`，或者 `CatalogBook` 与 `StockItem`。
- 如果想体现微服务 DDD，`book` 更适合作为 Catalog 上下文，库存确认应由 Inventory/Stock 上下文承担。

## 2. 聚合没有守住关键状态转换

`order/src/main/java/com/janwee/bookstore/order/domain/Order.java` 中的 `approve()` 和 `reject()` 可以无条件调用。

这意味着：

- 已拒绝订单可以再次 approve。
- 已批准订单可以再次 reject。
- 重复消息或乱序消息没有领域级保护。

领域对象没有表达订单状态机的不变量。当前审批逻辑主要写在 `OrderApplicationService.approve()` 和 `OrderApplicationService.reject()` 中，应用服务直接修改订单状态并创建 `Ticket`。

如果 `Ticket` 是“订单批准后的履约凭证”，它至少应由领域行为表达，例如：

- `order.approve()`
- `order.approveWithTicket()`
- 或由领域服务返回审批结果和待创建的履约凭证。

建议先把订单状态转换规则放回 `Order`，至少只允许：

- `APPROVAL_PENDING -> APPROVED`
- `APPROVAL_PENDING -> REJECTED`

重复事件要么明确幂等处理，要么抛出业务异常。

## 3. 领域事件和集成事件混在一起

`book` 模块中的 `BookOrdered` 和 `BookSoldOut` 放在 `domain/event` 下，但它们被 Rabbit 消费者跨服务使用，本质上已经是集成事件。

领域事件和集成事件的关注点不同：

- 领域事件：模型内部已经发生的业务事实。
- 集成事件：对外发布的服务契约，需要稳定、可版本化，并避免泄漏内部模型。

此外，事件发布器使用内存队列和 `Supplier::poll` 实现。例如 `order/southbound/adapter/event/RabbitEventPublisher.java` 中使用 `LinkedBlockingQueue` 暂存事件。这和数据库事务不是一个可靠边界，真实系统中会出现：

- 订单保存成功但事件没有发出。
- 事件发出但事务回滚。
- 进程重启导致队列中事件丢失。

Demo 可以保留简单实现，但应该明确这是教学简化。若要体现分布式 DDD，建议引入 outbox，或至少在命名和文档中区分 `domain.event` 与 `integration.message`。

## 4. 应用服务太像事务脚本，领域模型偏贫血

当前多个应用服务承载了过多业务决策：

- `BookApplicationService` 负责发布、修改、撤回、订购、库存判断、事件发布。
- `OrderApplicationService` 负责下单、查书、保存订单、发布事件、审批、创建 `Ticket`。

应用服务做编排是合理的，但当前很多业务含义没有进入领域对象。

典型例子：

- `Book` 的构造器和 Lombok builder 可以绕过 `changeAmountTo()`、`changePriceTo()` 的校验。
- `Book.sell()` 只做库存扣减，不表达“预留”“确认扣减”“释放库存”等业务语义。
- `Order` 不表达“等待库存确认”之外的业务前置条件。
- `Author`、`Ticket` 基本是数据容器。

DDD 不是要求每个类都有复杂行为，但当前项目的核心业务规则确实主要散落在应用层。

## 5. 领域层依赖 Spring 和技术类型

`book/src/main/java/com/janwee/bookstore/book/domain/service/BookPublicationPolicy.java` 位于 `domain/service`，但直接使用了 `@Service` 和 `@Autowired`。

`book/src/main/java/com/janwee/bookstore/book/domain/repository/BookRepository.java` 中使用了 Spring Data 的 `Page` 和 `Pageable`。

这会让领域层不再是纯领域模型，而是 Spring 应用的一部分。对 Demo 不致命，但从 DDD 和六边形架构角度看，领域层最好不知道 Spring、JPA、分页框架。

更好的边界是：

- 领域仓储只保留按业务身份加载、保存聚合的能力。
- 分页查询、列表视图、后台检索放到应用层查询服务或 read model repository。
- Spring 注解留在配置层或适配器层。

## 6. 授权上下文的领域语言被 Spring Security 牵引

`authorization/src/main/java/com/janwee/bookstore/authorization/domain/User.java` 中同时包含 email、password、role、authorities。

它既像领域用户，又像认证账号，还像 Spring Security principal 的源模型。`username()` 返回 email，`authorities()` 直接表达安全权限。

`SpringSecurityUserRegistrationService` 作为 `UserRegistrationService` 的实现，负责密码编码和保存用户。密码编码是基础设施细节，但这里通过“领域服务”入口修改了领域对象。

更清晰的划分可以是：

- `Account` 或 `UserAccount`：注册身份、邮箱唯一性、账号状态。
- `Credential`：密码哈希，不暴露明文密码修改。
- `Role`、`Permission`：授权领域语言。
- Spring Security `UserDetails`：纯适配器模型。

当前坏味道是：领域模型更像是为了适配 Spring Security 而设计，而不是先建模“账户注册、凭证、角色、授权策略”。

## 7. 上下文映射不清晰，事件命名容易误导

当前流程大致是：

1. 订单上下文发布 `OrderCreated`。
2. 图书上下文收到后扣库存。
3. 图书上下文发布 `BookOrdered` 或 `BookSoldOut`。
4. 订单上下文收到后 approve 或 reject。

问题在于 `BookOrdered` 这个名字在 `book` 上下文里像“书被订购”，在 `order` 上下文里实际表示“库存确认成功”。这说明上下文之间的语言没有翻译清楚。

更贴近业务事实的事件名可能是：

- `StockReservationConfirmed`
- `StockReservationRejected`
- `InventoryShortageDetected`

这样订单上下文处理的是库存确认结果，而不是含义模糊的“图书已订购”。

## 优先改进建议

1. 先明确战略边界：Catalog、Inventory、Ordering、Authorization 哪些是独立上下文，哪些只是 Demo 模块。
2. 补强订单状态机：状态转换必须由 `Order` 控制，并处理重复或乱序事件。
3. 从 `Book.amount` 中拆出库存语义，至少在命名和模型上区分目录与库存。
4. 区分领域事件和集成事件，不要把对外消息直接当领域事件。
5. 清理领域层技术依赖：Spring 注解、`Pageable`、Jackson 注解尽量移出 domain。
6. 授权模块重新命名模型，避免 `User` 同时承担领域用户、认证账号、Spring Security principal。

如果只选一个最值得先改的点，建议先改“订单-库存协作模型”。它最能体现 DDD 的上下文边界、聚合不变量、领域事件和最终一致性，也最能暴露当前设计的问题。
