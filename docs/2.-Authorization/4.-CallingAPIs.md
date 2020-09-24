# Calling APIs using an Access Token
<!-- theme: info -->
> These directions assume you have already obtained an Access Token using 2-legged or 3-legged OAuth.

After obtaining an Access Token you may now use this token to call Vendasta APIs.

Vendasta APIs expect an Access Token provided as a **Bearer Token** in the `Authorization` HTTP header. E.g. you would provide the HTTP Header `Authorization: Bearer <access_token>` with your request.

If you obtained an access token during the previous step of this guide you can test it by calling Vendasta's User Info endpoint with the following curl command. Replace `<access_token>` with your access token.


**NOTE:** the following call requires that you attached either the `profile` or `email` scope when requesting an access token in the previous step.

```sh
curl --location --request GET 'https://sso-api-prod.apigateway.co/oauth2/user-info' \
--header 'Authorization: Bearer <access_token>'
```

If successful, you should receive a response similar to the following. Fields may differ according to which scopes the token has access to.

```json
{
  "sub": "U-d6f69389-350c-465e-ad8a-3c68447fb63a",
  "email": "automated-account-creation@partner-service-account.apigateway.co",
  "updated_at": 1591049766,
  "roles": [
    "partner_service_account"
  ],
  "created_at": 1591049766
}
```