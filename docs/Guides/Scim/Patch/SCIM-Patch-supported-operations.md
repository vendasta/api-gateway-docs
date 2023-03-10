---
stoplight-id: fdxg63veh82v5
---

# SCIM Patch supported operations 


Patch for email is not supported. Email is primary attribute, is unique and is immutable for a given user.

**Name**: Require absolute path and operation structure as below

For Replace or Add
```json
{
    "op": "replace",
    "path": 'name.familyName',
    "value": "newFamilyName"
    
}

```
For remove 

```json
{
    "op": "remove",
    "path": 'name.familyName'    
}
```


**First Level attribute of basic type :** 

attribute - "nickName", "locale", "userType", "preferredLanguage", "timezone"

For Replace or Add
```json
{
    "op": "replace",
    "path": attribute,
    "value": "newFamilyName"
    
}
```

For remove 

```json
{
    "op": "remove",
    "path": attribute  
}
```


**Address** : 
Support for a single address  of type work. Valid Patch request for address.


For Replace or Add
```json
{
    "op": "replace",
    "path": "addresses",
    "value": {
      "type": "work",
      "streetAddress": "100 Universal City Plaza",
      "locality": "Hollywood",
      "region": "US-IA",
      "postalCode": "91608",
      "country": "US",
      "formatted": "100 Universal City Plaza Hollywood, CA 91608 USA",
      "primary": true
    }
    
}

{
    "op": "replace",
    "path": "addresses.locality",
    "value":  "Hollywood"
}
```

For remove 

```json
{
    "op": "remove",
    path: "addresses" 
}

{
    "op": "remove",
    "path": "addresses.locality",
   
}
```


**Phone Numbers :**

For Replace or Add
```json
{
	"op":    "replace",
	"path":  "phoneNumbers[type eq \"mobile\"].value",
	"value": "tel:+91-9100000023",
}
```

For remove 

```json
{
	"op":    "remove",
	"path":  "phoneNumbers[type eq \"mobile\"].value",
	
}




```