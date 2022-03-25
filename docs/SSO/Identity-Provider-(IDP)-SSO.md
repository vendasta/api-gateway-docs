# Identity-Provider-(IDP)-SSO

You must be signed into the Partner Center to you configure your Identity Provider for the Vendasta Platform. This will allow your users to log into their apps and services using your custom login screen.

Here is the page for managing and configuring SSO: https://partners.vendasta.com/integrations/sso

We currently support either of the OAuth2 or OpenIDConnect standardized authentication flows. If you have a valid OAuth2 or OpenIDConnect Identity Provider, you can use it to log in your users to Vendasta.
- Create a new OAuth2 Client in your identity provider to represent the Vendasta Platform. Take note of the Client ID and Secret. When prompted for a Redirect URL, enter the value: https://sso-api-prod.apigateway.co/oauth2/callback
- Enter the Client ID and Client Secret from step 1 into the form below.
- Choose either the OpenIDConnect or OAuth2 integration type depending on which standard your identity provider supports.
- Enter your identity provider's Discovery Endpoint or Authorization, Token, and User Info endpoints respectively.
- Click "Save" to enable your integration.

Here are the following areas that we currently support SSO for:
- Business App
- Reputation Management
- Social Marketing
- Customer Voice
- Website Pro
- Advertising Intelligence
- Most other marketplace apps
