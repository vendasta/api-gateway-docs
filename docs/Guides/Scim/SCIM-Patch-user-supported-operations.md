---
stoplight-id: fdxg63veh82v5
---

# SCIM Patch User supported operations 
SCIM Patch is very versatile and supports a higly flexible request syntax. Below guide provides list of attributes, operation and request format for SCIM Patch user operations supported by this endpoint.

**Attribute: name.familyName, name.givenName**: 

Operation: replace/add

```json
{
  "op": "replace",
  "path": "name.familyName",
  "value": "newFamilyName"    
}

```
Operation: remove 

```json
{
  "op": "remove",
  "path": "name.familyName"   
}
```
**Attribute: "nickName", "locale", "userType", "preferredLanguage", "timezone"**

Operation: replace/add
```json
{
  "op": "replace",
  "path": "nickName",
  "value": "newNickName"    
}
```

Operation: remove 

```json
{
  "op": "remove",
  "path": "nickName"
}
```


**Attribute: "Addresses"**

Operation: replace/add
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

Operation: remove 

```json
{
  "op": "remove",
  "path": "addresses[type eq \"work\"]"
}

{
  "op": "remove",
  "path": "addresses.locality"   
}
```


**Attribute: "phoneNumbers"**

For Replace or Add
```json
{
	"op":    "replace",
	"path":  "phoneNumbers[type eq \"mobile\"].value",
	"value": "tel:+91-9100000023"
}
```

For remove 

```json
{
	"op":    "remove",
	"path":  "phoneNumbers[type eq \"mobile\"].value"	
}
```