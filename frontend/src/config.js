export const authConfig = {
  clientId: "bookstore-frontend",
  authorizationEndpoint: "http://localhost:18083/oauth2/authorize",
  tokenEndpoint: "http://localhost:18083/oauth2/token",
  redirectUri: "http://127.0.0.1:8088/authorized",
  scope: "openid user:read user:write"
};

export const gatewayConfig = {
  baseUrl: "http://localhost:18082"
};
