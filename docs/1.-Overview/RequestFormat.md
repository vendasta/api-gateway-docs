# Request Format

The body of most requests and responses are JSON objects that are formated according to [JSON:API](https://jsonapi.org/examples/). 

Each representation has a `type` field that can be mapped to a single path with the gateway. With the addition of the resource ID it is possible to map any JSON object to its source. 
