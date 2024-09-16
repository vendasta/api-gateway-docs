# Identity Provider (IDP) SSO

You can replace Vendasta's login screens with your own by configuring Identity Provider (IDP) SSO. It is a great option if your customers already have a username & password (or other credentials) stored in your system. 

Vendasta currently supports OpenIDConnect, an industry standard protocol built to extend OAuth2.0 with a user identification layer that enables secure authentication and user identity verification in addition to the authorization flow provided by OAuth2.0.

## Step 1 - User Management
SSO only identifies who is sitting at the computer. Creating their user record and setting permissions need to be done as a separate step.

Vendasta provides several options for Managing Users

**1) User pre-syncing using REST APIs**
- [Create, Update, and Delete](https://developers.vendasta.com/platform/9ae19216ae422-create-user) 
- [User Account Association](https://developers.vendasta.com/platform/7b68c25e54818-associate-user-with-location)

**2) User pre-syncing using SCIM APIs**
- [Create, Update, and Delete ](https://developers.vendasta.com/platform/8a121c92d9d8b-create-user)
- [Group Management (User Account Association)](https://developers.vendasta.com/platform/e0c9197db556a-update-group)

**3) Just In Time(JIT) User Creation**
- Needs to be manually turned on by Vendasta 
- User Updates & Deletion should still be managed using options 1 or 2 ^

>For more information on flows see [User Synchronization for Partners](IDP-User-Sync.md)

## Step 2 - SSO Implementation

**Implementation Option 1 - IDaaS Provider**

If you are using a commercial Identity as a service(IDaaS) provider to manage your users odds are good they have a way to add a new OAuth2 client. It may be labeled as "app/application", or "integration". You may need to ask their support team to enable it for your instance - in some cases this is only available on enterprise level subscriptions.

Once an OAuth2 client has been created you will enter its details at [Partner Center -> Administration -> Single Sign On](https://partners.vendasta.com/integrations/sso).

>You can utilize OpenIdConnect Discovery 

**Implementation Option 2 - Build it yourself**

If you are building your own client you will need to implement the Authorization, Token, and UserInfo endpoints from the [OpenIdConnect specification](https://openid.net/specs/openid-connect-core-1_0.html). You may be able to make use of one of [these open source libraries](https://oauth.net/code/) to assist with your implementation - Find an option from the Server list.

Vendasta's IDP SSO utilizes the authorization code flow. The IDP in OpenID Connect is referred to officially as the *"Authorization Server"*, but you will see the terms *"Identity Provider"*, *"OpenID Provider"*, and *"Authorization Server"* used interchangeably in many cases by industry. To better understand what the Server Library is facilitating, a great intro to OAuth2+OIDC can be found in [Okta's illustrated guide](https://developer.okta.com/blog/2019/10/21/illustrated-guide-to-oauth-and-oidc)

---
If you need to build it yourself we have [a guide for that too](Implementing-Oauth2-IDP.md)..

## Step 3 - SSO Initiation
Please review the [Navigation Links Guide](Navigation-Links.md) for details on how to link your clients' users to the desired resource(Vendasta Business App, or Marketplace Product). It is best to not use direct links to avoid code changes being required when custom domains, or url structures change.

## Step 4 - Single Logout(optional)

Once you have completed setting up Single Sign On from your system, you may want to implement [Single Logout](Single-Logout.md).
