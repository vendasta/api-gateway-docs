---
stoplight-id: b0opggqmzakx5
---

# Single Logout

Similar to other platforms, Vendasta maintains its own sessions for users that have logged in. This session remains active until the logout button is clicked in Vendasta or we otherwise determine that it is time to log the user out. 

To log a user out of the Vendasta platform when they click the logout button in your system there are two options:

a) Navigate the user's web browser to the following URL. 
```
https://sso-api-prod.apigateway.co/your-partner-id/oauth2/logout?post_logout_redirect_uri=https://example.com/logout-complete
```

b) Add the following code snippet to your website. This approach can be used to log the user out of multiple sites at the same time. 

```html
<iframe src="https://sso-api-prod.apigateway.co/your-partner-id/oauth2/logout" style="width:0;height:0;border:0; border:none;"></iframe>
```

You will need to replace `your-partner-id` and `example.com/logout-complete` with appropriate values.
