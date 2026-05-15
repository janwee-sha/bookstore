import React, { useEffect, useMemo, useState } from "react";
import { createRoot } from "react-dom/client";
import { authConfig } from "./config";
import { fetchCurrentUser } from "./api";
import {
  clearTokens,
  completeLogin,
  decodeJwtPayload,
  loadTokens,
  startLogin
} from "./oauth";
import "./styles.css";

function App() {
  const [tokens, setTokens] = useState(loadTokens);
  const [status, setStatus] = useState({ state: "idle" });
  const path = window.location.pathname;

  if (path === "/authorized") {
    return <CallbackPage onTokens={setTokens} />;
  }

  if (!tokens?.access_token) {
    return <LoginPage status={status} onLogin={() => startLoginFlow(setStatus)} />;
  }

  return <HomePage tokens={tokens} onLogout={() => logout(setTokens)} />;
}

function LoginPage({ status, onLogin }) {
  return (
    <main className="auth-shell">
      <section className="login-panel">
        <p className="eyebrow">Bookstore Console</p>
        <h1>OAuth 2.0 Login</h1>
        <p className="lead">
          React 前端通过 Authorization Code + PKCE 登录，登录后以 Bearer Token 访问 gateway。
        </p>
        <button className="primary-action" type="button" onClick={onLogin}>
          使用 Bookstore 账号登录
        </button>
        {status.state === "error" && <p className="error-text">{status.message}</p>}
      </section>
    </main>
  );
}

function CallbackPage({ onTokens }) {
  const [message, setMessage] = useState("正在完成登录...");

  useEffect(() => {
    completeLogin(authConfig, window.location.href)
      .then((nextTokens) => {
        onTokens(nextTokens);
        window.location.replace("/");
      })
      .catch((error) => {
        setMessage(error.message);
      });
  }, [onTokens]);

  return (
    <main className="auth-shell">
      <section className="login-panel">
        <p className="eyebrow">Authorization Callback</p>
        <h1>{message}</h1>
      </section>
    </main>
  );
}

function HomePage({ tokens, onLogout }) {
  const [profile, setProfile] = useState({ state: "idle" });
  const claims = useMemo(() => decodeJwtPayload(tokens.access_token), [tokens.access_token]);
  const issuedAt = claims?.iat ? new Date(claims.iat * 1000).toLocaleString() : "-";
  const expiresAt = claims?.exp ? new Date(claims.exp * 1000).toLocaleString() : "-";

  return (
    <main className="app-shell">
      <header className="topbar">
        <div>
          <p className="eyebrow">Bookstore Console</p>
          <h1>主页</h1>
        </div>
        <button className="ghost-action" type="button" onClick={onLogout}>
          退出登录
        </button>
      </header>

      <section className="dashboard-grid">
        <article className="panel">
          <h2>登录信息</h2>
          <dl className="facts">
            <div>
              <dt>User</dt>
              <dd>{claims?.sub || "-"}</dd>
            </div>
            <div>
              <dt>Scopes</dt>
              <dd>{Array.isArray(claims?.scope) ? claims.scope.join(", ") : claims?.scope || "-"}</dd>
            </div>
            <div>
              <dt>Issued</dt>
              <dd>{issuedAt}</dd>
            </div>
            <div>
              <dt>Expires</dt>
              <dd>{expiresAt}</dd>
            </div>
          </dl>
        </article>

        <article className="panel">
          <h2>Gateway 调用</h2>
          <p className="muted">
            请求会携带当前 access token，gateway 只负责验证 JWT 并转发到后端服务。
          </p>
          <button className="secondary-action" type="button" onClick={() => loadProfile(tokens.access_token, setProfile)}>
            读取用户列表
          </button>
          <GatewayResult profile={profile} />
        </article>
      </section>
    </main>
  );
}

function GatewayResult({ profile }) {
  if (profile.state === "idle") {
    return <pre className="result-box">等待调用...</pre>;
  }
  if (profile.state === "loading") {
    return <pre className="result-box">请求中...</pre>;
  }
  if (profile.state === "error") {
    return <pre className="result-box error-box">{profile.message}</pre>;
  }
  return <pre className="result-box">{JSON.stringify(profile.data, null, 2)}</pre>;
}

async function startLoginFlow(setStatus) {
  try {
    setStatus({ state: "loading" });
    await startLogin(authConfig);
  } catch (error) {
    setStatus({ state: "error", message: error.message });
  }
}

async function loadProfile(accessToken, setProfile) {
  try {
    setProfile({ state: "loading" });
    const data = await fetchCurrentUser(accessToken);
    setProfile({ state: "success", data });
  } catch (error) {
    setProfile({ state: "error", message: error.message });
  }
}

function logout(setTokens) {
  clearTokens();
  setTokens(null);
}

createRoot(document.getElementById("root")).render(<App />);
