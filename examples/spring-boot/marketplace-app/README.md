
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

* Inside the demo code, [ngrok](https://ngrok.com) is used to make a temporary domain, but you are free to replace it with your own.

To run ngrok server,

```bash
ngrok http 8080
```

* This command will produce a result like the following,

![](https://user-images.githubusercontent.com/109953973/203941968-7c940b66-1519-4506-bc26-80e9978c3335.png)
    
Open your product page in the [vendor center](https://vendors.vendasta.com). If you don't have one, you'll need to create one. Under integration tab, enable SSO and follow this [steps](https://developers.vendasta.com/vendor/d191b96068b71-sso-o-auth2-3-legged-flow#step-2-initial-configuration) to configure OAuth2. Update the client-id, client-secret and redirect-uri in `main/resources/application.yml` file.


![](https://user-images.githubusercontent.com/109953973/207820555-d7d69699-7d5a-4aad-8796-60e96c96be48.png)

* Update [constant variables](#constant-variables) in `utils/Constants.java` file with your own app ID.

* And finally, you need a service account credentials to perform 2-legged OAuth api calls. You can get that file by following this [document](https://developers.vendasta.com/platform/ZG9jOjEwMTkzMDg4-overview).

* To start spring-boot application,

```bash
  mvn spring-boot:run
```

### Constant Variables

To run this project, you will need to change the following constant variable in your `utils/Constants.java` file.

`APPID (String)` - The App Id can be found on the product page of the Vendor Center.

`PARTNERID (String)` - Partner ID can be found in Partner Center.

`SERVICE_ACCOUNT_JSON_PATH (String)` - The location of the JSON file.



### API Endpoints

| Endpoint  | Includes  |
| --------- | ---------- | 
| `/entry/<accountId>` | Entry URL for the SSO. Used to identify and store accountId of the logged in user. |
| `/<accountId>` | URL redirected automatically from entry URL after logged in. |
| `/business/list/<accountId>` | List of business details retrieved from vendasta via Platform API.On the app's home page, there is an **API Demo Page** button that can be used to activate this API.|
 
 > **Warning**
 > Don't change the logics in `/entry/<accountId>` and `/<accountId>` endpoints until you want to change the default redirection path.

  To add new endpoints, create dedicated methods to map the urls in `controller/WebController.java`.
> [Vendasta NavBar](https://developers.vendasta.com/vendor/8c35dfd4efc89-session-transfer-introduction#navigation-bar) is added in every pages.

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