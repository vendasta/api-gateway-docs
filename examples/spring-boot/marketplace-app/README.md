
# Marketplace App Demo

Repository with sample code for building marketplace-app. It will help partners to kickstart their own app using Vendasta platform.

## Getting Started

- [Installation](#installation)
- [API Endpoints](#api-endpoints)
- [Used Tech Pluggins](#used-tech-pluggins)
- [Running Tests](#running-tests)
- [Related Links](#related-links)


### Installation

Clone the project

```bash
  git clone https://github.com/vendasta/api-gateway-docs
```

Go to the project directory

```bash
  cd examples/spring-boot/marketplace-app
```

* Inside the demo code, [ngrok](https://ngrok.com) is used to make a temporary domain, but you are free to use `localhost` or replace it with your own server.

To run ngrok server,

```bash
  ngrok http 8080
```

* This command will produce a result like the following,

![](https://user-images.githubusercontent.com/109953973/203941968-7c940b66-1519-4506-bc26-80e9978c3335.png)
    
Open your product page in the [vendor center](https://vendors.vendasta.com). If you don't have one, you'll need to create one. Under integration tab, enable SSO and follow this [steps](https://developers.vendasta.com/vendor/d191b96068b71-sso-o-auth2-3-legged-flow#step-2-initial-configuration) to configure OAuth2. Update the client-id, client-secret and redirect-uri in `main/resources/application.yml` file.


![](https://user-images.githubusercontent.com/109953973/212901702-eed0615f-db75-41c5-9cff-537935cc1d2b.png)

* Update [env variables](#environment-variables) in `src/main/resources/application.yml` file with your own app ID and client details.

* You need public and private keys to generate [JWT](https://jwt.io) (JSON Web Token) in order to use the Vendasta marketplace APIs. This [document](https://developers.vendasta.com/vendor/ZG9jOjIxNzM0NjA4-api-authentication) will assist you in creating and comprehending token management for marketplace APIs.

* And finally, you need a service account credentials to perform 2-legged OAuth api calls. You can get that file by following this [document](https://developers.vendasta.com/platform/ZG9jOjEwMTkzMDg4-overview).

* To start spring-boot application pick one of the following:

1. Local/Default
   ```bash
     ./mvnw spring-boot:run
   ```
   then open a web browser to http://localhost:8080/ or your configured ngrok domain.


2. Demo
   ```bash
     SPRING_PROFILES_ACTIVE=demo ./mvnw spring-boot:run
   ```

3. Production
   ```bash
     SPRING_PROFILES_ACTIVE=prod ./mvnw spring-boot:run
   ```

### Environment Variables

To run this project, you will need to change the following environment variables in your `src/main/resources/application.yml` file.

`environment` - Default is 'demo'. Change this to 'prod' for a production environment, 

`app-id` - The App Id can be found on the product page of the Vendor Center.

`partner-id` - Partner ID can be found in Partner Center.

`service-account-json-path` - The location of the service-account-json file.

`marketplace-private-key` - Path to the private-key generated for JWT. [Reference](https://developers.vendasta.com/vendor/ZG9jOjIxNzM0NjA4-api-authentication#step-1---public-key).


### API Endpoints

| Endpoint  | Includes  |
| --------- | ---------- | 
| `/<accountId>` | Entry URL for the SSO. Used to identify and store accountId of the logged in user. |
| `/businesses` | List of business details retrieved from vendasta via Platform API. On the app's home page, there is an **API Demo Page** button that can be used to activate this API.|
| `/businesses/account/{accountId}` | Account details page. Describes the account information for the chosen account on the businesses page.|

  To add new endpoints, create dedicated methods to map the urls in `controller/WebController.java`.
> [Vendasta NavBar](https://developers.vendasta.com/vendor/8c35dfd4efc89-session-transfer-introduction#navigation-bar) is added in every pages.

 > **Warning:**
 > Don't change the logics in `/<accountId>` endpoint until you want to change the default redirection path.

### Used Tech Pluggins

| Tech  | Version  | Usage |
| --------- | ---------- | -------- |
| [Spring Boot Framework](https://spring.io) | v2.7.3 | Used to create stand-alone applications. |
| Java | v11 | Spring Boot requires Java |
| [Maven](https://maven.apache.org/run.html) | v3.8.6 | Modern server-side Java template engine |


### Running Tests

The Test files are in  `src/test/java/com/example/vendasta/ShoeStore`. To run tests, run the following command.

```bash
  mvn test
```


### Related Links

Here are some useful links for the deep dive.

- Running Maven Application - [Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.running-your-application)
- Running Tests - [Docs](https://baeldung.com/maven-run-single-test)
- Implementation of 3-legged OAuth with Spring-boot - [Video](https://drive.google.com/file/d/15taDril9zlGkI1aGMxrW7C2g0JLYJQXA/view)
- 2-legged OAuth and platform APIs - [Docs](https://developers.vendasta.com/platform/ZG9jOjEwMTkzMDg0-overview)
- Vendasta Marketplace APIs - [Docs](https://developers.vendasta.com/vendor/ZG9jOjIxNzM0NjA4-api-authentication)