# Identity Provider (IDP) SSO

You can replace Vendasta's login screens with your own by configuring Identity Provider (IDP) SSO. It is a great option if your customers already have a username & password (or other credentials) stored in your system. 

Vendasta currently supports OpenIDConnect, an industry standard protocol built to extend OAuth2.0 with a user identification layer that enables secure authentication and user identity verification in addition to the authorization flow provided by OAuth2.0.
>You can utilize openIdConnect discovery 

If you are using a commercial Identity as a service(IDaaS) system to manage your users odds are good they have a way to add a new OAuth2 client. It may be labeled as "app", "integration", or "service provider". You may need to ask their support team to enable it for your instance.

Once an OAuth2 client has been created you will enter its details at [Partner Center -> Adminstration -> Single Sign On](https://partners.vendasta.com/integrations/sso).


If you built your own site you will need to implement the Authorization, Token and UserInfo endpoints from the OAuth2 specification. Only the authorization code flow with [PKCE](https://www.oauth.com/oauth2-servers/pkce/) is required.

For a great into to OAuth2 see [Okta's illustrated guide](https://developer.okta.com/blog/2019/10/21/illustrated-guide-to-oauth-and-oidc).

You may be able to make use of one of [these open source libraries](https://oauth.net/code/) to assist with your implementation. If you need to build it yourself we have [a guide for that too](Implementing-Oauth2-IDP.md).


> SSO only identifies who is sitting at the computer. 
> Creating their user record and setting permissions should be done as a separate step.
>
> You can [manage users by API](IDP-User-Sync.md) to keep them in sync with your system.
>
> Once you have completed setting up Single Sign On from your system, you may want to implement [Single Logout](Single-Logout.md).
