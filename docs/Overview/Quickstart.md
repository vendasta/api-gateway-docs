---
tags: [02, overview, getting-started, quickstart]
---

# Quickstart: Your First API Call

This guide walks you through making your first Vendasta API call in under 10 minutes.

## Prerequisites

- A Vendasta Partner Account with admin access
- A tool for making HTTP requests (curl, Postman, or the built-in "Try It" feature in these docs)

## Step 1: Create a Service Account

1. Log into [Partner Center](https://partners.vendasta.com/login/)
2. Navigate to **Administration** > **Service Accounts**
3. Click **Create Service Account** and give it a name
4. On the Manage Keys screen, click **RS256** to generate a key pair
5. Download the credential JSON file and store it securely

The file will look like this:

```json
{
  "type": "service_account",
  "private_key_id": "c0273fce-79b7-4104-8a8c-ea489abb3979",
  "private_key": "-----BEGIN RSA PRIVATE KEY-----<private-key>-----END RSA PRIVATE KEY-----\n",
  "client_email": "my-service@partner-service-account.apigateway.co",
  "token_uri": "https://sso-api-prod.apigateway.co/oauth2/token",
  "assertionHeaderData": {
    "alg": "RS256",
    "kid": "c0273fce-79b7-4104-8a8c-ea489abb3979"
  },
  "assertionPayloadData": {
    "aud": "https://iam-prod.apigateway.co",
    "iss": "my-service@partner-service-account.apigateway.co",
    "sub": "my-service@partner-service-account.apigateway.co"
  }
}
```

For detailed instructions with screenshots, see [Creating a Service Account](../Authorization/2-legged-oauth/CreatingAServiceAccount.md).

## Step 2: Get an Access Token

Use the credential file to request an access token. Here is a Python example:

```python
import json
import time
import jwt
import requests

# Load the credential file
with open("service-account-credentials.json") as f:
    credentials = json.load(f)

# Build the JWT assertion
now = int(time.time())
payload = {
    "aud": credentials["assertionPayloadData"]["aud"],
    "iss": credentials["assertionPayloadData"]["iss"],
    "sub": credentials["assertionPayloadData"]["sub"],
    "iat": now,
    "exp": now + 3600,
    "scope": "profile email business"
}
headers = {
    "alg": credentials["assertionHeaderData"]["alg"],
    "kid": credentials["assertionHeaderData"]["kid"]
}

assertion = jwt.encode(payload, credentials["private_key"], algorithm="RS256", headers=headers)

# Exchange assertion for an access token
response = requests.post(credentials["token_uri"], data={
    "grant_type": "urn:ietf:params:oauth:grant-type:jwt-bearer",
    "assertion": assertion
})
token = response.json()["access_token"]
print(f"Access Token: {token}")
```

<!-- theme: info -->
> **Tip:** For quick tests you can also use the **Generate Token** button at the top of the Developer Center.

For examples in other languages see [Obtaining an Access Token](../Authorization/2-legged-oauth/UsingAServiceAccount.md).

## Step 3: Make Your First API Call

With your access token, call the user-info endpoint to verify everything is working:

```sh
curl -X GET "https://sso-api-prod.apigateway.co/oauth2/user-info" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

You should receive a response like:

```json
{
  "sub": "U-d6f69389-350c-465e-ad8a-3c68447fb63a",
  "email": "my-service@partner-service-account.apigateway.co",
  "updated_at": 1591049766,
  "roles": ["partner_service_account"],
  "created_at": 1591049766
}
```

## Step 4: Fetch Real Data

Now try listing business accounts. Replace the partner ID with your own:

```sh
curl -X GET "https://prod.apigateway.co/platform/salesAccounts?page[limit]=3" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/vnd.api+json"
```

The response follows the [JSON:API](https://jsonapi.org/examples/) format:

```json
{
  "links": {
    "first": "https://prod.apigateway.co/platform/salesAccounts?page[cursor]=abc&page[limit]=3",
    "next": "https://prod.apigateway.co/platform/salesAccounts?page[cursor]=def&page[limit]=3"
  },
  "data": [
    {
      "type": "salesAccounts",
      "id": "AG-1234567",
      "attributes": {
        "name": "Acme Corp",
        "customerIdentifier": "CUST-001"
      }
    }
  ]
}
```

## Environments

| Environment | Base URL |
|---|---|
| Production | `https://prod.apigateway.co` |
| Demo/Sandbox | `https://demo.apigateway.co` |

To get a sandbox environment for testing, contact support@vendasta.com.

## What's Next?

- **[Authorization deep dive](../Authorization/Authorization.md)** — Learn about 3-legged OAuth and scopes
- **[Create an account](../Guides/Accounts.md)** — Create and manage business accounts
- **[Sell products](../Guides/Sell/Overview.md)** — Activate products for your accounts via API
- **[CRM API](../Guides/CRM/crm.md)** — Manage contacts, companies, and activities
- **[Set up SSO](../SSO/Overview.md)** — Let your users sign in to Vendasta through your platform
- **[API Reference](../../openapi/platform/platform.yaml)** — Full Platform API reference
