# Users

Users within your platform will generally fall into one of the following categories. 

**Partner Users:** Are generally your employees. They have access to apps that were provided for the partner's use. You can manually manage them in the [My Team](https://partners.vendasta.com/my-team) area of Partner Center.

**Business Users:** Are generally the managers of the small businesses that your team sells to.  

**Guest Users:** Are people who are part of a different platform instance from yourself. You are most likely to come across them if you are using the fulfilment services of another partner or communicating with a vendor.

**Bot Users:** To allow auditing of background tasks completed by your service accounts they have users accounts as well. 


## Quick start guides
- [Get the profile data of a user](./Display-Users.md)
- [Create a new business user](./Create-Business-User.md)
- [Create a new partner user](./Create-Partner-User.md)


## Personal Data Protection
User records contain sensitive data that we need to work together to protect, such as the name and contact info of a person. There are a few things we can do to achieve that.  

1. You can use the `fields` and `filter*` query parameters to request only the info that you need. 
2. We will apply security checks to individual fields. If you or the app does not have sufficient access the fields will be left blank, nil or omitted in responses. 
3. Request the smallest possible scope of access when creating an access token for your bot. 


## OAuth2 Scopes

Note: Scopes are normally defined for much larger categories of data which include multiple resource types. Due to the sensitive nature of data provided in the `users` resource it has been given scopes for categories of fields and filters.


### Full access scopes

`user.admin` - Allows full access to manage users in the platform.

`user.list` - Allows searching for users based on a set of filters. (ex: email, name, category, organization). Without this scope an exact user id is required. 

`user.profile:read` - Allows readonly access to semi-public fields used for info cards of all platform users

`user.contact:read` - Allows readonly access to the contact info (email, phone, address) of all your users.

`user.permission:read` - Allows reading the list of permissions for the user.

`user.permission` - Allows managing the permissions for users. The permissions that can be set will be limited based on access rules.



### Self access scopes

Info for the user associated with the access token can also be granted.

`self.user` - Allows editing the profile, contact info and profile image for the current user.

`self.user.contact:read` - Allows readonly access to the contact info (email, phone, address) of the current user.


`openid` - Allows getting the user record for the currently logged-in user by using a user id of me. 

`profile` - Readonly access to the user profile, including name, locale, and language preferences.

`email` - Allows readonly access to the email of the current user.

`phone` - Allows readonly access to the phone numbers of the current user.

`address` - Allows readonly access to the address of the current user.
