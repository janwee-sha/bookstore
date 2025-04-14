1. 
2. Authorization Code(授权码)模式：

    a. 用户代理向授权服务器发起获取授权码并在成功后重定向到客户端应用的请求：
    ```
    http://localhost:7030/oauth2/authorize?response_type=code&client_id=bookstore&redirect_uri=http://127.0.0.1:8080/authorized&scope=user:read user:write
    ```
    
    b. 用户在授权服务器通过用户名/密码登录并确认授权范围提交许可
    
    c. 用户代理收到授权码并被重定向到客户端应用，如下所示：
    ```
    http://127.0.0.1:8080/authorized?code=vo4XemaDO_4piA7Zkc3NDJQwJXweczmV0Mxt7547cj_xAhUVMApd6VREhfs0zm6voaEuMIIgHQbCxSA9r3oxTMkUmsPstsRNjngEQWNvR7FEllOTCs7tzpoKhr4vQRiU
    ```
    
    d. 使用授权码获取访问令牌
    
    ```
    curl -X POST \
    http://localhost:7030/oauth2/token \
      -H "Content-Type: application/x-www-form-urlencoded" \
      -H "Authorization: Basic $(echo -n 'bookstore:secret' | base64)" \
      -d "grant_type=authorization_code" \
      -d "code=vo4XemaDO_4piA7Zkc3NDJQwJXweczmV0Mxt7547cj_xAhUVMApd6VREhfs0zm6voaEuMIIgHQbCxSA9r3oxTMkUmsPstsRNjngEQWNvR7FEllOTCs7tzpoKhr4vQRiU" \
      -d "redirect_uri=http://127.0.0.1:8080/authorized"
    ```
2. Client Credential（客户端授权）模式

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
3. 密码模式

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