import { gatewayConfig } from "./config";

export async function fetchCurrentUser(accessToken) {
  const response = await fetch(`${gatewayConfig.baseUrl}/authorization/users`, {
    headers: {
      Authorization: `Bearer ${accessToken}`
    }
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(`用户信息请求失败：${response.status} ${message}`);
  }

  return response.json();
}
