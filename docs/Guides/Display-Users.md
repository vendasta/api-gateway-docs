# Displaying Users

## Getting a user
If you know the ID of a user you may request some details about them. 
The fields available to you will change based on your permissions. 
If you lack permission that particular field will be omitted in the response. 

```json http
{
  "method": "get",
  "url": "https://prod.apigateway.co/platform/users/U-1234",
  "query": {
    "fields": "user[displayName]"
  },
  "headers": {
    "Authorization": "Bearer <Access Token with 'user.admin' or `user.profile` scope>"
  }
}
```
