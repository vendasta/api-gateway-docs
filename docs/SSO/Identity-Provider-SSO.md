# Identity Provider (IDP) SSO

You can replace Vendasta's login screens with your own by configuring Identity Provider (IDP) SSO. It is a great option if your customers already have a username & password (or other credentials) stored in your system. 

Vendasta currently supports OpenIDConnect, an industry standard protocol built to extend OAuth2.0 with a user identification layer that enables secure authentication and user identity verification in addition to the authorization flow provided by OAuth2.0.

## Step 1 - User Management
SSO only identifies who is sitting at the computer. Creating their user record and setting permissions need to be done as a separate step.

Vendasta provides several options for Managing Users

**1) User pre-syncing using REST APIs**

This is an option you may want to consider if you are going with option 2 from Step 2, and don't currently utilize a service or library that supports SCIM out of the box. Follow the [User Synchronization for Partners](IDP-User-Sync.md) guide to:
- [Create, Update, and Delete Users](https://developers.vendasta.com/platform/9ae19216ae422-create-user)
- [Associate Users with Accounts](https://developers.vendasta.com/platform/7b68c25e54818-associate-user-with-location)

**2) User pre-syncing using SCIM**

SCIM is a good choice if:

a) You plan to access multiple applications such as Vendasta's that also support SCIM, allowing you to standardize your implementation. 

b) Your IDaaS provider, like Okta(not Auth0), or Azure AD, have **outbound** SCIM support, and the apps you use already have **inbound** SCIM capabilities. This way you get most of the benefits "out of the box" with minimal configuration.

Follow the [Guide to use Vendasta SCIM APIs to sync Users](../Guides/Scim/Guide-to-sync-users-in-vendasta.md) to:
- [Create, Update, and Delete Users](https://developers.vendasta.com/platform/8a121c92d9d8b-create-user)
- [Group Management (User Account Association)](https://developers.vendasta.com/platform/e0c9197db556a-update-group)

**3) Just In Time(JIT) User Creation**

This is only recommended in certain situations. Please contact Vendasta Support if you would like to discuss leveraging this option with your integration. User Updates & Deletion should still be managed using options 1 or 2. See [Why we don't automatically create users when they SSO](IDP-User-Sync.md#why-dont-we-automatically-create-users-when-they-sso) for details.


## Step 2 - SSO Implementation

**Implementation Option 1 - IDaaS Provider**

If you are using a commercial Identity as a service(IDaaS) provider to manage your users, odds are good they have a way to add a new OAuth2 client. It may be labeled as "app/application", or "integration". You may need to ask their support team to enable it for your instance, or determine if the plan that your on has the proper support.

Once an OAuth2 client has been created you will enter its details at [Partner Center -> Administration -> Single Sign On](https://partners.vendasta.com/integrations/sso).

>Vendasta supports OpenIdConnect Discovery - this is the default option on the Integration Type dropdown

**Implementation Option 2 - Build it yourself**

If you are building your own client you will need to implement the Authorization, Token, and UserInfo endpoints from the [OpenIdConnect specification](https://openid.net/specs/openid-connect-core-1_0.html). You may be able to make use of one of [these open source libraries](https://oauth.net/code/) to assist with your implementation - Find an option from the Server list.

Vendasta's IDP SSO utilizes the authorization code flow. The IDP in OpenID Connect is referred to officially as the *"Authorization Server"*, but you will see the terms *"Identity Provider"*, *"OpenID Provider"*, and *"Authorization Server"* used interchangeably in many cases by industry. To better understand what the Server Library is facilitating, a great intro to OAuth2+OIDC can be found in [Okta's illustrated guide](https://developer.okta.com/blog/2019/10/21/illustrated-guide-to-oauth-and-oidc)

Once you have your client configured, enter its details at [Partner Center -> Administration -> Single Sign On](https://partners.vendasta.com/integrations/sso). Switch the Integration Type from OpenID Connect Discovery to OAuth2+OIDC if you have not implemented a well-known endpoint.

---
If you need to build it yourself manually, we have [a guide for that too](Implementing-Oauth2-IDP.md)..

## Step 3 - SSO Initiation
Please review the [Navigation Links Guide](Navigation-Links.md) for details on how to link your clients' users to the desired resource(Vendasta Business App, or Marketplace Product). It is best to not use direct links to avoid code changes being required when custom domains, or url structures change.

## Step 4 - Single Logout(optional)

Once you have completed setting up Single Sign On from your system, you may want to implement [Single Logout](Single-Logout.md).
