# 代码结构

`authorization` 模块采用围绕授权业务能力组织的六边形架构的 DDD 设计风格。`core` 与 `security` 不再作为独立子域存在，模块代码统一收敛到以下六边形包结构中。新增或调整功能时，请优先遵循以下约定：

- `domain` 承载领域模型，应避免依赖技术实现。
- `northbound.remote` 承载北向入口适配器逻辑，例如 REST 资源和消息订阅者，不应直接承载复杂业务规则。
- `northbound.remote.security` 承载北向远程入口的安全配置，例如授权服务器过滤链、资源服务器过滤链和方法安全配置。
- `northbound.local` 承载北向入口端口逻辑，例如本地应用服务。
- `northbound.message` 承载北向入口消息，例如 REST 资源的请求、响应对象、从上游服务订阅的消息模型等。
- `southbound.port` 承载南向出口端口逻辑，例如领域模型的仓储接口。
- `southbound.adapter` 承载南向出口的适配器逻辑，例如依赖 Spring Security、JPA、密码编码器等基础设施的实现。
- `southbound.message` 承载南向出口消息，例如向下游服务发布的异步消息模型、依赖 JPA 的持久化模型实现等。

# OAuth 2 知识

## 1. React 前端使用的 Authorization Code + PKCE

`frontend` 目录中的 React 应用使用 `bookstore-frontend` public client。该客户端没有 `client_secret`，必须携带 PKCE 的 `code_challenge` 和 `code_verifier`，登录成功后回调到：

```
http://127.0.0.1:8088/authorized
```

前端用授权码通过 gateway 代理调用：

```
POST http://localhost:18082/authorization/oauth2/token
```

gateway 不再作为 OAuth 2 Client，不保存登录会话，只校验前端请求携带的 Bearer Token 并转发到后端服务。

## 2. Authorization Code(授权码)模式：

    a. 用户代理向授权服务器发起获取授权码并在成功后重定向到客户端应用的请求：
    ```
    http://localhost:18083/oauth2/authorize?response_type=code&client_id=bookstore&redirect_uri=http://127.0.0.1:18082/authorized&scope=user:read user:write
    ```
    
    b. 用户在授权服务器通过用户名/密码登录并确认授权范围提交许可
    
    c. 用户代理收到授权码并被重定向到客户端应用，如下所示：
    ```
    http://127.0.0.1:18082/authorized?code=vo4XemaDO_4piA7Zkc3NDJQwJXweczmV0Mxt7547cj_xAhUVMApd6VREhfs0zm6voaEuMIIgHQbCxSA9r3oxTMkUmsPstsRNjngEQWNvR7FEllOTCs7tzpoKhr4vQRiU
    ```
    
    d. 使用授权码获取访问令牌
    
    ```
    curl -X POST \
    http://localhost:18083/oauth2/token \
      -H "Content-Type: application/x-www-form-urlencoded" \
      -H "Authorization: Basic $(echo -n 'bookstore:secret' | base64)" \
      -d "grant_type=authorization_code" \
      -d "code=vo4XemaDO_4piA7Zkc3NDJQwJXweczmV0Mxt7547cj_xAhUVMApd6VREhfs0zm6voaEuMIIgHQbCxSA9r3oxTMkUmsPstsRNjngEQWNvR7FEllOTCs7tzpoKhr4vQRiU" \
      -d "redirect_uri=http://127.0.0.1:18082/authorized"
    ```
## 3. Client Credential（客户端授权）模式

    客户端发起如下请求：
    ```
    curl -X POST bookstore:secret@localhost:18083/oauth2/token -d "grant_type=client_credentials" -d "scope=user:read"
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
## 4. 密码模式

    客户端发起如下请求：
    ```
    curl -X POST \
    http://localhost:18083/oauth2/token \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -H "Authorization: Basic Ym9va3N0b3JlOnNlY3JldA== \
    -d "grant_type=password" \
    -d "username=usr0@bookstore.com" \
    -d "password=pass@bookstore.com" \
    -d "scope=user:read user:write"
    ```
