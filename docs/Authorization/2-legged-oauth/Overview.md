# Overview
This guide's goal is to assist the reader in the process of authenticating with
Vendasta's standardized OAuth2 integration, specifically the **2-legged OAuth2 flow**, also known as the client credential flow, in which a [client credential](https://tools.ietf.org/html/rfc6749#section-4.4) is used to obtain an access token on behalf of a service. Vendasta strives to be in compliance with the [OAuth2 spec](https://tools.ietf.org/html/rfc6749) as much possible. If an inconsistency from the spec is discovered, please notify Vendasta so we can resolve the issue. Vendasta supports the use of a [jwt token](https://tools.ietf.org/html/rfc7523#section-4) as a valid client assertion.

The **client credentials** do not themselves provide any access to API endpoints,
they serve only as a means to acquire **access tokens** which may be used to access API endpoints.

Vendasta provides **client credentials** to the **caller** in the form of a
**public/private key-pair** which is associated with a **service account**.

A **service account** is a Vendasta user account which is associated with a
specific **service** or **automation**. **Service accounts** provide a
means to limit the permissions of a service and understand and audit the actions
of a service within the system. 

You may think of generating an **access token** as logging into the system using the **private key** as your password. After some time that session will expire and you have to authenticate again.

In order to authenticate against a Vendasta API you will need to accomplish each of the following steps:

1. Create a **service account** for your **automation** or **service**.
1. Generate a **public/private key-pair** for the **service account**.
1. Write code which performs the following steps to acquire and use an **access token**, repeating the steps upon expiry of the acquired access token.
   1. Use the **public/private key-pair** to generate a **client assertion**, which is a proof of ownership of the **private key**.
   1. Exchange the **client assertion** for an **access token** by calling Vendasta's **token endpoint**.
   1. Use this **access token** as a **bearer token** in the **Authorization** header when calling Vendasta endpoints.

Next: [Create a Service Account](CreatingAServiceAccount.md)
