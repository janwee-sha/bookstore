# 代码结构

`authorization` 模块采用采用围绕图书业务能力组织的分层架构的 DDD 设计风格。新增或调整功能时，请优先遵循以下约定：

- `presentation` 负责对外暴露 REST 接口、接收请求参数并返回响应对象，不承载业务规则。
- `application` 负责编排用例流程，协调领域对象、领域服务和仓储接口完成业务动作。
- `domain` 承载核心业务模型、领域规则、领域事件、领域异常和仓储接口，应避免依赖 Spring Web、数据库实体或消息中间件实现。
- `infrastructure` 负责适配外部技术设施，例如持久化、消息发布与消费等，并实现领域层定义的接口。
- 调用方向应是自顶向下的，如 `presentation -> application -> domain -> infrastructure`。
- 依赖方向应尽量遵循依赖倒置原则（Dependency Inversion，DI）：高层模块不应该依赖于低层模块，二者都应该依赖于抽象。
- 请求、响应、持久化记录与领域模型之间应尽量显式转换，避免跨层复用同一个对象导致职责混淆。

示例包结构如下：

```
authorization
├─core               --> 核心子域
│  ├─domain           --> 领域层
│  ├─infrastructure   --> 基础设施曾
│  │  ├─persistence    --> 持久化基础设施
│  │  │  └─jpa          --> JPA 仓储
│  │  └─service         --> 外部服务基础设施
│  └─presentation     --> 展现层
│      ├─message
│      └─resource
└─security          --> 安全子域
```

# OAuth 2 知识

## 1. Authorization Code(授权码)模式：

    a. 用户代理向授权服务器发起获取授权码并在成功后重定向到客户端应用的请求：
    ```
    http://localhost:7030/oauth2/authorize?response_type=code&client_id=bookstore&redirect_uri=http://127.0.0.1:7001/authorized&scope=user:read user:write
    ```
    
    b. 用户在授权服务器通过用户名/密码登录并确认授权范围提交许可
    
    c. 用户代理收到授权码并被重定向到客户端应用，如下所示：
    ```
    http://127.0.0.1:7001/authorized?code=vo4XemaDO_4piA7Zkc3NDJQwJXweczmV0Mxt7547cj_xAhUVMApd6VREhfs0zm6voaEuMIIgHQbCxSA9r3oxTMkUmsPstsRNjngEQWNvR7FEllOTCs7tzpoKhr4vQRiU
    ```
    
    d. 使用授权码获取访问令牌
    
    ```
    curl -X POST \
    http://localhost:7030/oauth2/token \
      -H "Content-Type: application/x-www-form-urlencoded" \
      -H "Authorization: Basic $(echo -n 'bookstore:secret' | base64)" \
      -d "grant_type=authorization_code" \
      -d "code=vo4XemaDO_4piA7Zkc3NDJQwJXweczmV0Mxt7547cj_xAhUVMApd6VREhfs0zm6voaEuMIIgHQbCxSA9r3oxTMkUmsPstsRNjngEQWNvR7FEllOTCs7tzpoKhr4vQRiU" \
      -d "redirect_uri=http://127.0.0.1:7001/authorized"
    ```
## 2. Client Credential（客户端授权）模式

    客户端发起如下请求：
    ```
    curl -X POST bookstore:secret@localhost:7030/oauth2/token -d "grant_type=client_credentials" -d "scope=user:read"
    ```
    
    返回类似下面的输出：
    
    ```
    {
      "access_token":"eyJraWQiOiJmODgzMDU2MC1kYWFjLTQ5MTEtYmJiYy1kMjY2YTc1NTc0NDgiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJib29rc3RvcmUiLCJhdWQiOiJib29rc3RvcmUiLCJuYmYiOjE3NDEzMzY0NDcsInNjb3BlIjpbInVzZXI6cmVhZCJdLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjcwMzAiLCJleHAiOjE3NDEzMzY3NDcsImlhdCI6MTc0MTMzNjQ0NywianRpIjoiYWIwMjQ3N2QtYWVhMi00MzNiLWJhMDUtY2QwZGZlYWU3NTUzIn0.hv-cf5eQvbStSO0cVT-d7TWFayGoLuS0KlsBeFqJp3mYlZqvKwf_Zm_fUwJkr9N1bv0sVn9FMCDcprKbOEbR8vr9m1hyBAtl1PQk3acmDpvjaqLTlIYclp0wF3ZaKWOF1wWoURHS8D_cfFNA5qh4R74q09T18CyBceeExJZgVUQ5NRNNUeHMzWUF5YOmQYwe_RKRDkrScKAx5iYG0SKKkxLuDjimrZ8kp1z5gvDJhOglvd-tG0izo7u2EeDhiwep1LC5V0-VegkyyEAw0jr113OZDj3pO9PInjPTcdJZZg2YB7sk77Xc56xHJrWbDAEkz-2Q6ghbEOYhgUvrtPTvLQ",
      "scope":"user:read",
      "token_type":"Bearer",
      "expires_in":300
    }
    ```
## 3. 密码模式

    客户端发起如下请求：
    ```
    curl -X POST \
    http://localhost:7030/oauth2/token \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -H "Authorization: Basic Ym9va3N0b3JlOnNlY3JldA== \
    -d "grant_type=password" \
    -d "username=usr0@bookstore.com" \
    -d "password=pass@bookstore.com" \
    -d "scope=user:read user:write"
    ```