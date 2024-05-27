# Navigation Links

This guide covers building links that you can embed in your website or emails
that will send users to pages in the Vendasta platform and marketplace products. 

<!-- theme: info -->
> ### Common Links
> These links just need the AG-1234567 replaced with the correct business id
> 
> Business App -> `https://sso-api-prod.apigateway.co/service-gateway?serviceProviderId=VBC&account_id=AG-1234567`
>
> Business App edit business profile-> `https://sso-api-prod.apigateway.co/service-gateway?serviceProviderId=VBC&path=%2Faccount%2Flocation%2F%7B%7BaccountId%7D%7D%2Fsettings%2Fprofile&account_id=AG-1234567`
>
> Reputation Management -> `https://sso-api-prod.apigateway.co/service-gateway?serviceProviderId=RM&&account_id=AG-1234567`
>
> Local SEO -> `https://sso-api-prod.apigateway.co/service-gateway?serviceProviderId=MS&account_id=AG-1234567`
>
> Advertising Intelligence  -> `https://sso-api-prod.apigateway.co/service-gateway?serviceProviderId=MP-94072e44d5364872b672d7ab4fc7a7e8&account_id=AG-1234567`
>
> Social Marketing -> `https://sso-api-prod.apigateway.co/service-gateway?serviceProviderId=SM&account_id=AG-1234567`
>
> Customer Voice -> `https://sso-api-prod.apigateway.co/service-gateway?serviceProviderId=MP-c4974d390a044c28aec31e421aa662b2&account_id=AG-1234567`
>
> Website -> `https://sso-api-prod.apigateway.co/service-gateway?serviceProviderId=MP-ee4ea04e553a4b1780caf7aad7be07cd&account_id=AG-1234567`

Many sites allow you to simply copy the URL from your web brower when you want to add a link 
to your website. That works for basic usecases within the Vendasta platform however does not work
for apps that support whitelable domains, which can change between partner markets, or some forms of SSO. 

You should let the Vendasta platform handel these complexities by linking to the SSO service-gateway instead.

## Service Gateway URL

The base URL for links is `https://sso-api-prod.apigateway.co/service-gateway`. You will then need to add the appropriate query parameters to it.

### Query Parameters

#### Service Provider 
`serviceProviderId` should be set to the ID of the app/product/center that you want to send the user to.

It is typically the same as the marketplace app id. See [Find a SKU](../Guides/Sell/FindSKU.md) for a list.

### Service Context
In order to select the correct login screen and branding info we need to know who is going to be using the service. One of the following parameters is required:
- `account_id` - This is what you want most of the time. It is the ID of the Business Sales Account. For example `AG-1234567`
- `group_path` - Used for multi location business groups
- `serviceContext` - An opaque string provided by Vendasta

### Deep Link

You may optionally deep link users to a specific page in services that support it. After logging in (if needed) users will be sent to the location specified in the `path` query parameter.

The `path` parameter should be a URL encoded string containing everything after the domain. It supports placeholders of `{{accountId}}`, `{{partnerId}}`, `{{groupId}}` with the values being looked up from the service context.

Example: To send the user to `whitelabelDomain.com/account/location/{{accountId}}/posts?after=2022-23-12&otherFilter=something,cool` 

use
`&path=%2Faccount%2Flocation%2F%7B%7BaccountId%7D%7D%2Fposts%3Fafter%3D2022-23-12%26otherFilter%3Dsomething%2Ccool`


### Skip Auth

By default the SSO system will ensure the user has logged in before redirecting them to the service. This saves a set of redirects making SSO faster.

Setting `&skipAuth=true` is helpful for building links to public pages and pages you don't want to require a login for. E.g. unsubscribe links. Note that the service provider may still initiate an SSO if it determines that it requires authentication.


<!-- theme: warning -->
> The service gateway does not currently validate that the destination product has been 
> purchased or that the user who clicked on the link has access. To avoid users seeing 
> permission denied or not found errors you should validate that before displaying the link in your system.
> 
