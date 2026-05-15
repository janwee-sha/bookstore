# Bookstore Frontend

这是 bookstore 的 React 前端应用，用于学习前端技术并替代 gateway 模块中原来的 Thymeleaf 页面。

## 技术边界

- React 负责登录页、OAuth2 回调页和主页。
- OAuth2 登录使用 Authorization Code + PKCE，客户端 ID 为 `bookstore-frontend`。
- token 交换请求走 gateway：`POST http://localhost:7001/authorization/oauth2/token`。
- 业务接口请求走 gateway，并在请求头携带 `Authorization: Bearer <access_token>`。
- gateway 不再作为 OAuth2 Client，不渲染页面，也不保存浏览器登录会话。

## 本地运行

```bash
npm install
npm run dev
```

打开：

```text
http://127.0.0.1:8088
```

后端建议启动顺序：

1. `./gradlew :config:bootRun`
2. `./gradlew :eureka:bootRun`
3. `./gradlew :authorization:bootRun`
4. `./gradlew :book:bootRun`
5. `./gradlew :order:bootRun`
6. `./gradlew :gateway:bootRun`
