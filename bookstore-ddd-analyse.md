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

## 2. 应用服务太像事务脚本，领域模型偏贫血

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

## 3. 授权上下文的领域语言被 Spring Security 牵引

`authorization/src/main/java/com/janwee/bookstore/authorization/domain/User.java` 中同时包含 email、password、role、authorities。

它既像领域用户，又像认证账号，还像 Spring Security principal 的源模型。`username()` 返回 email，`authorities()` 直接表达安全权限。

`SpringSecurityUserRegistrationService` 作为 `UserRegistrationService` 的实现，负责密码编码和保存用户。密码编码是基础设施细节，但这里通过“领域服务”入口修改了领域对象。

更清晰的划分可以是：

- `Account` 或 `UserAccount`：注册身份、邮箱唯一性、账号状态。
- `Credential`：密码哈希，不暴露明文密码修改。
- `Role`、`Permission`：授权领域语言。
- Spring Security `UserDetails`：纯适配器模型。

当前坏味道是：领域模型更像是为了适配 Spring Security 而设计，而不是先建模“账户注册、凭证、角色、授权策略”。