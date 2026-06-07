# 为 frontend 补充测试覆盖

## 元信息

| 字段 | 内容 |
| --- | --- |
| 状态 | backlog |
| 范围 | frontend |
| 优先级 | medium |
| 类型 | test |
| 相关模块 | `frontend`, `authorization`, `gateway` |

## 背景

`frontend` 目前约 300 行代码（`main.jsx`、`oauth.js`、`api.js`、`config.js`），无测试框架、`package.json` 无 `test` 脚本，也未接入根目录 Gradle `check`。作为 Demo 项目不必追求高覆盖率，但 OAuth + PKCE 属于容易写错且与安全相关的逻辑，值得用少量、有针对性的测试保护。

## 目标

建立与后端“优先单元测试业务逻辑”思路一致的前端测试体系，重点覆盖认证流程与 API 调用，而非全量 UI 或 E2E。

可验证结果：

- `frontend` 有可运行的测试脚本。
- OAuth + PKCE 关键分支有单元测试保护。
- API 请求封装有基础单元测试。
- 关键 React 状态分支有少量组件测试。

## 非目标

- 不在本任务中强制建立完整端到端 OAuth 登录测试。
- 不追求高覆盖率指标。
- 不重复验证 `authorization` 模块已经覆盖的 OAuth2 客户端注册细节。

## 现状

| 维度 | 现状 |
| --- | --- |
| 规模 | 单页 React 应用，逻辑集中在 `oauth.js`、`api.js` 与 `main.jsx` |
| 测试 | 无 Vitest / Jest / Playwright 等依赖与脚本 |
| CI | 根 `build.gradle` 未纳入 frontend 测试 |
| 后端覆盖 | `authorization` 模块的 `OAuth2ClientRegistrationIntegrationTest` 已验证 `bookstore-frontend` 客户端注册（PKCE 必填、redirect URI 等），但不覆盖浏览器侧 PKCE 实现与 token 交换 |

## 建议方案

使用 Vitest 建立轻量前端测试体系。优先覆盖纯 JS 认证和 API 模块，再补少量 React Testing Library 组件测试。E2E 暂缓，避免把全栈服务启动顺序和外部运行环境引入当前任务。

## 测试分层（按优先级）

### 1. 单元测试 — 优先（Vitest）

逻辑集中在纯 JS 模块，投入产出比最高。

**`oauth.js`（核心）**

- `decodeJwtPayload`：合法 JWT、空 token、格式错误
- `saveTokens` / `loadTokens` / `clearTokens`：sessionStorage 读写与清理
- `completeLogin`（mock `fetch` + `sessionStorage`）：
  - 授权服务器返回 `error`
  - 缺少 `code` / `state`
  - `state` 与 sessionStorage 不一致
  - token 交换失败（非 2xx）
  - 成功路径：清理 PKCE、保存 token
- PKCE 辅助逻辑：`code_challenge` 使用 S256、`base64url` 编码正确（可通过 `completeLogin` 间接验证，或导出后单测）

**`api.js`**

- `fetchCurrentUser`：请求 URL 正确、携带 `Authorization: Bearer`、成功解析 JSON、失败时抛错

### 2. 组件测试 — 适量（Vitest + React Testing Library）

UI 偏展示，不必全测，只覆盖关键状态分支：

| 组件 / 场景 | 验证内容 |
| --- | --- |
| `GatewayResult` | `idle` / `loading` / `error` / `success` 四种展示 |
| `LoginPage` | 点击登录触发回调；`status.state === "error"` 时显示错误文案 |
| `App` 路由 | mock `window.location.pathname` 与 token：无 token → 登录页；`/authorized` → 回调页；有 token → 主页 |

`CallbackPage` 的 `useEffect` 依赖 OAuth 回调，适合 mock `completeLogin` 做轻量测试，不必优先上 E2E。

### 3. E2E — 可暂缓（Playwright 等）

完整 OAuth 流程需 config、eureka、gateway、authorization、frontend 全栈联调，成本高、易 flaky。除非要把「登录 → 调 gateway → 读用户列表」固化进 CI，否则 Demo 阶段可不做。

## 建议落地结构

```text
frontend/
├── src/
│   ├── oauth.js
│   ├── oauth.test.js
│   ├── api.test.js
│   └── main.test.jsx
└── vitest.config.js   # 或合并进 vite.config.js
```

## 实施步骤

1. 引入 **Vitest** + **@testing-library/react**（与 Vite 7 配套）
2. 在 `package.json` 增加 `"test": "vitest run"`（可选 `"test:watch": "vitest"`）
3. 先写 `oauth.test.js`、`api.test.js`（约 10–15 个用例，覆盖主要分支）
4. 再补 3–5 个组件测试（`GatewayResult`、`LoginPage`、`App` 路由）
5. （可选）在根 `build.gradle` 增加调用 `npm test` 的 task，与后端 CI 统一

## 与后端测试的分工

- **后端**：OAuth 服务端配置、API 契约、业务逻辑（`OAuth2ClientRegistrationIntegrationTest`、`contractTest` 等）
- **前端**：浏览器侧 PKCE 生成与校验、token 存储、错误处理、带 Bearer 的请求封装

两者互补，前端测试不应重复验证已在 `authorization` 集成测试中覆盖的服务端客户端注册细节。

## 验证方式

建议验证范围：

- `frontend` 测试脚本，例如 `npm test` 或等价 Vitest 命令。
- 若将前端测试接入 Gradle，再运行根 README 中对应的 Gradle Wrapper 验证命令。
- 若改动 OAuth 客户端配置或 gateway 调用契约，补充运行受影响后端模块的集成测试或契约测试。

## 状态记录

- 2026-06-07：创建 backlog 任务，明确前端测试覆盖建议与后端测试分工。
