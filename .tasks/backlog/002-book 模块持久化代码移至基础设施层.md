# book 模块持久化代码移至基础设施层

继续当前工作区的工作，将 book 模块领域层的持久化技术相关代码移至 `infrastructure.persistence` 包。
以 Book 实体为例：
- 在 com.janwee.bookstore.book.infrastructure.persistence.BookRepositoryJpaImpl 中调用 com.janwee.bookstore.book.infrastructure.persistence.jpa.BookPOJpaRepository
- 在 com.janwee.bookstore.book.infrastructure.persistence.BookPOAssembler 装配器中完成 Book 与 BookPO 对象之间的转换
- 修改对应下游调用和测试用例
