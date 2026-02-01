# CRM Overview

The CRM provides a single source of truth for all of your prospect and customer data. Here are a few object types you should be familiar with.

## Companies
A company represents business you sell to. When using a location based product you will need a seperate company record per location. They can be associated in a parent-child relationship. 

## Contacts
Contacts represent a single person. They can be associated with companies in a many-to-many relationship.

## Activities
An activity is used to track things that happen to a contact and/or company. There are many sub-types such as notes, meetings, calls, and tasks.

## Accounts
Once a company moves from being a lead to a solid oppertunity or sale an account record will be created. It is used to grant access to business app, track payments and activate products.

Some data is automatically kept in sync between the company and account record. The account has additional fields that are customer manageble which are used to keep online listing profiles (like Google Maps) up to date.

In our older APIs you may see this refered to as a Sales Account, Account Group, or Business Location.

## Users
Contacts can be turned into Users to grant them access to their Account in Business App. Their name and address will be kept in sync. This allows customers to help keep your CRM data upto date.

