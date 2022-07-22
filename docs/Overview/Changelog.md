# Changelog

The platform is continuously evolving. This page lists the significant changes when they are announced. For more info on the statuses and release process see [Versioning](./Versioning.md)

## 2022-07-22

### Customer Voice Template Management

Create, update, get, list and delete operations are now available for the [templates](../../openapi/customervoice/customervoice.yaml/components/schemas/reviewRequestTemplates) used in Customer Voice. This will allow you to customize the content of email and SMB messages used when sending requests for reviews using the Customer Voice product. 

### Delete from Customer List

The ability to [delete a contact](../../openapi/business/business.yaml/paths/~1customers~1{id}/delete) from Customer List has been added. 

## 2022-07-21

### Country and region lists

You may now get a list of the [country](../../openapi/platform/platform.yaml/paths/~1countries/get) and [region](../../openapi/platform/platform.yaml/paths/~1countryRegions/get) codes that are permitted when we validate addresses. The lists include their names allowing you to provide your users with dropdown lists. 

## 2022-07-18

### Customer List Management

List, update and get operations are now available for the [business customers resourse](../../openapi/business/business.yaml/components/schemas/customers). This allows for full management of the contacts added in Business App's customer list.


## 2022-06-23

### Create Sales Orders with Status

It is now possible to [create sales orders](../../openapi/platform/platform.yaml/paths/~1orders/post) that are not immediately sent for activation. You may set the `statusCode` to `submitted` to send the order to your partner admins for manual approval. Setting the `statusCode` to `draft` will create the order without sending it to anyone. 

### List Subscription Assignments

Using the new [list subscription assignments operation](../../openapi/platform/platform.yaml/paths/~1subscriptionAssignments/get) you can now determine what products have been activated for a business location. 

This allows you to check to see if a product is already active before submitting an order. 

"Why is it called a subscription assignment?" you might ask. Well, we are working on the ability for businesses to purchase multiple product subscriptions and then assign them to one or more locations. For seat-based products the subscriptions will be assignable to users. We still have a large amount of work to do to support this but we didn't want to make you switch APIs.

## 2022-05-18

Proposed new resources for managing subscription assignments.

## 2022-04-12

### Automations name

It is now possible to get the name of an automation if you already know it's name. More info can be found [here](/platform/d2d52201b76ca-get-automation).

### Standard hours of operation

The [businessLocations](/platform/fc461812db5c6-business-location) and [salesAccounts](/platform/af388f5a836f8-sales-account) resources now support setting and retrieving the general weekly standard hours for your locations. A format for holiday and department specific hours has been proposed as something we would like to add in the future. 

## 2022-04-05

### Custom Fields now support external Ids

The `salesAccountCustomFields`, `userCustomFields`, `productCustomFields`, and `orderCustomFields` resources have all been updated to have an `externalId` field.

You can customize the `externalId` in Partner Center. It can be used instead of the system generated `fieldId` when reading or setting the value for a custom field.

## 2022-03-16

### More attributes for salesAccounts

The following new fields have been added to the salesAccounts resource:

- commonNames
- seoKeywords
- website
- servicesOffered
- descriptionShort
- descriptionLong

## 2022-03-03

### List business location reviews

Trusted testers now have access to list the reviews that were collected by the Reputation Management product.

## 2022-03-02

### Order Custom Fields REST API added

Trusted testers now have access to Update and Get Order Custom Fields.

## 2022-02-23

### Inflate order products

Orders can contain packages or products. Now the order line items will contain the products that are part of the packages that are in the order. They have been marked up accordingly so you can differentiate between them.

## 2022-02-02

### Proposed new API operations for Listing Builder added

Added the Listing Sync Listings and Listing Scores API operations.
This will provide Listing Builder users with the ability to use Listing Builder's data to power their own UIs.

## 2022-01-25

### User Custom Fields REST API added

Trusted testers now have access to Update and Get User Custom Fields.

### Create Customer and Send Review Request REST APIs added

Trusted testers now have access to API endpoints for syncing contact info onto Business App and sending a Customer Voice email or SMS review request.

## 2022-01-19

### Sales Account and Sales Account Custom Field REST API added

Trusted testers now have access to Create, Update, Get and List Sales Accounts as well as
Get and Update Custom Fields on the sales accounts.

## 2021-12-14

### Social REST API added

Trusted testers now have access to `GET` endpoint for the following Social Resources:

- Social Profiles

## 2021-12-09

### Advertising Intelligence REST API added

Trusted testers now have access to `GET` endpoint for the following Advertising Resources:

- Connected Accounts
- Account Stats
- Campaign Info
- Campaign Stats

## 2021-11-05

Proposed adding support for triggering a manual automation based on an order.

## 2021-10-06

### Versioning Status Rename

The names and definitions of the maturity statuses used to define the stability level of API operations in our
[versioning policy](Versioning.md) have been clarified and aligned with those used for other Vendasta platform features.
There is no functional impact to your code.

### Address Fields Reconciled

The address related fields for the `businessLocations` resource have been reconciled with the new `users` resource
fields so that they use consistent field names and values.

For `businessLocations` the `zip` has been renamed `postalCode` to match international best practices.
As well the `stateCode` has been replaced with `regionCode` which follows the [ISO 3166-2](https://en.wikipedia.org/wiki/ISO\_3166-2) format.
To migrate you can simply generate the value as `{countryCode}-{stateCode}`.

For `users` the `streetAddress` has been renamed `line1` and `additionalAddress` has been renamed `line2`.

The old fields will continue to work while the trusted tester partners using them are given a chance to migrate. (Targeting 30 days)

## 2021-04-19

Added `orders` resource along with create, get and list operations. This will allow you to activate products for a business location.

## 2021-04-18

Implemented proposed get and list `salesContacts` operations.

## 2021-04-16

Refactored some proposed and unused experimental features for consistency.
Renamed experimental `invoices` resource to `purchases` to clear up confusion regarding rollup invoices.

## 2021-02-16

Added support for setting and retrieving the lat/long on businessLocations

## 2021-02-05

Added support for setting the businessCategories relationship on businessLocations

## 2021-01-19

Update Business Locations has now reached an `Experimental` status.

## 2021-01-15

Further refine proposed contacts model. For clarity it has been scoped to just the sales team as `salesContacts`. It has now reached the `Experimental` status.

## 2020-12-18

List Invoices (`GET /platform/invoices`) hit the experimental status. You can now retrieve detailed info for the wholesale costs we bill you.

## 2020-12-15

Create Business Locations has now reached an `Experimental` status.

## 2020-12-2

Updated proposed `accountLocations` and `accountContacts` resources based on feedback. They have been renamed and
the scope changed slightly.
