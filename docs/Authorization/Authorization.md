---
tags: [authorization]
---

# Overview

We support two similar ways to authenticate API requests.

**Service Accounts** - AKA 2-legged OAuth2 - Effectively allows you to create a hidden user in the platform with the access level of an administrator that your app can use. If you have multiple apps it is recommended that you create multiple accounts just like you would for a team of administrators.

**Service Providers** - AKA 3-legged OAuth2 - Allows your app to perform actions on behalf of other users based on their level of access. We support the OpenIDConnect standard. The flow is the same one that is used when connecting an app to your Google or Facebook account. Learn more in [this illustrated guide](https://developer.okta.com/blog/2019/10/21/illustrated-guide-to-oauth-and-oidc).


Using either of these options follows the same basic flow

1. **Create the service** - Tell us a little about your service
2. **Create a secret for the service** - Like setting a password, the key pair allows us confirm your identity
3. **Obtain an Access Token** - Just like logging in, you will do this at the start of each session
4. **Include Access Token in header of API calls** - `Authorization: Bearer <access token>`

Access Tokens do expire so you may need to refresh them from time to time. You will also need to obtain a new one if the scope (set of things you wish to access) changes.


Next: [Setup service account](2-legged-oauth/Overview.md)
