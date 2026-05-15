const PKCE_STORAGE_KEY = "bookstore.oauth.pkce";
const TOKEN_STORAGE_KEY = "bookstore.oauth.tokens";

export async function startLogin(config) {
  const state = randomBase64Url(32);
  const codeVerifier = randomBase64Url(64);
  const codeChallenge = await sha256Base64Url(codeVerifier);

  sessionStorage.setItem(PKCE_STORAGE_KEY, JSON.stringify({ state, codeVerifier }));

  const params = new URLSearchParams({
    response_type: "code",
    client_id: config.clientId,
    redirect_uri: config.redirectUri,
    scope: config.scope,
    state,
    code_challenge: codeChallenge,
    code_challenge_method: "S256"
  });

  window.location.assign(`${config.authorizationEndpoint}?${params.toString()}`);
}

export async function completeLogin(config, callbackUrl) {
  const url = new URL(callbackUrl);
  const code = url.searchParams.get("code");
  const state = url.searchParams.get("state");
  const error = url.searchParams.get("error");

  if (error) {
    throw new Error(url.searchParams.get("error_description") || error);
  }
  if (!code || !state) {
    throw new Error("授权服务器回调缺少 code 或 state。");
  }

  const stored = JSON.parse(sessionStorage.getItem(PKCE_STORAGE_KEY) || "null");
  if (!stored || stored.state !== state) {
    throw new Error("OAuth2 state 校验失败，请重新登录。");
  }

  const body = new URLSearchParams({
    grant_type: "authorization_code",
    client_id: config.clientId,
    code,
    redirect_uri: config.redirectUri,
    code_verifier: stored.codeVerifier
  });

  const response = await fetch(config.tokenEndpoint, {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    },
    body
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(`令牌交换失败：${response.status} ${message}`);
  }

  const tokens = await response.json();
  sessionStorage.removeItem(PKCE_STORAGE_KEY);
  saveTokens(tokens);
  return tokens;
}

export function saveTokens(tokens) {
  sessionStorage.setItem(TOKEN_STORAGE_KEY, JSON.stringify({
    ...tokens,
    savedAt: Date.now()
  }));
}

export function loadTokens() {
  return JSON.parse(sessionStorage.getItem(TOKEN_STORAGE_KEY) || "null");
}

export function clearTokens() {
  sessionStorage.removeItem(TOKEN_STORAGE_KEY);
  sessionStorage.removeItem(PKCE_STORAGE_KEY);
}

export function decodeJwtPayload(token) {
  if (!token) {
    return null;
  }
  const [, payload] = token.split(".");
  if (!payload) {
    return null;
  }
  return JSON.parse(new TextDecoder().decode(base64UrlToBytes(payload)));
}

async function sha256Base64Url(value) {
  const bytes = new TextEncoder().encode(value);
  const digest = await crypto.subtle.digest("SHA-256", bytes);
  return bytesToBase64Url(new Uint8Array(digest));
}

function randomBase64Url(length) {
  const bytes = new Uint8Array(length);
  crypto.getRandomValues(bytes);
  return bytesToBase64Url(bytes);
}

function bytesToBase64Url(bytes) {
  let binary = "";
  bytes.forEach((byte) => {
    binary += String.fromCharCode(byte);
  });
  return btoa(binary)
    .replace(/\+/g, "-")
    .replace(/\//g, "_")
    .replace(/=+$/g, "");
}

function base64UrlToBytes(value) {
  const padded = value.replace(/-/g, "+").replace(/_/g, "/")
    .padEnd(Math.ceil(value.length / 4) * 4, "=");
  const binary = atob(padded);
  return Uint8Array.from(binary, (char) => char.charCodeAt(0));
}
