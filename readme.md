# API Gateway Design

This repository is used to design the API Gateway. It is also tied into the preview documentation at https://vendasta.stoplight.io

## Naming Conventions

- Member names SHOULD be camel-cased (i.e., wordWordWord)
- Member names SHOULD start and end with a character “a-z” (U+0061 to U+007A)
- Member names SHOULD contain only ASCII alphanumeric characters (i.e., “a-z”, “A-Z”, and “0-9”)

The resource slug, resource model code, resource type code should all be plural and nearly identical. The context ID should be prefixed to the model and type codes.

### Contexts
IDs


### Paths

### Attribute Names
Fields containing enums should end with `Code`. A second readonly field ending with `Name` should be added containing the translated display text.

Date fields should end with `Date`.

Date-time fields should end with `At`.

Since JSON does not have a native time format this will allow identifying them 
without needing to look at the documentation. (They should follow RFC-3339/ISO-8601)

### Operations

**ID**

{method}-{slug1}-{slug2/var}-{slug3}


**Name**

Method | Collection | Single
-------|------------|-------
GET|list {type}|get {type}
POST|create {type}|-
PATCH|~~update multi {type}~~|update {type}
DELETE|~~delete multi {type}~~|delete {type}
~~PUT~~|~~replace multi {type}~~|~~replace {type}~~
OPTIONS|{type} options|{type} item options


### Models
Name
Code


### Tags
Should be dash seperated and include the context.
Each resource type that can be included on an endpoint should be taged on the endpoint.

## Standard Codes
Countries should use the 2 letter [ISO 3166](https://www.iso.org/iso-3166-country-codes.html) country code
