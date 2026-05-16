# Bookstore Frontend

这是 bookstore 的 React 前端应用，用于与服务端交互并展示 bookstore 的 UI 用例，如演示 OAuth2 认证授权流程、展示图书信息等。

## 服务网关

frontend 只与作为对外开放的服务端网关的 gateway 服务交互，gateway 再将请求路由至 authorization、book 和 order 等业务服务。

## 认证授权

- React 负责登录页、OAuth2 回调页和主页。
- OAuth2 登录使用 Authorization Code + PKCE，客户端 ID 为 `bookstore-frontend`。
- token 交换请求走 gateway：`POST http://localhost:7001/authorization/oauth2/token`。
- 业务接口请求走 gateway，并在请求头携带 `Authorization: Bearer <access_token>`。

## 本地运行

```bash
npm install
npm run dev
```

打开：

```text
http://127.0.0.1:8088
```
