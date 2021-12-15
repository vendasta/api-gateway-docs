# Changelog

The platform is continuously evolving. This page lists the significant changes when they are announced. For more info on the statuses and release process see [Versioning](./Versioning.md)

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
As well the `stateCode` has been replaced with `regionCode` which follows the [ISO 3166-2](https://en.wikipedia.org/wiki/ISO_3166-2) format.
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
