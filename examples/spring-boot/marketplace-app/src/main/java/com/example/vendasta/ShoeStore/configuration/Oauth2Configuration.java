package com.example.vendasta.ShoeStore.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.vendasta.ShoeStore.utils.CommonUtils;
import com.example.vendasta.ShoeStore.utils.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

@Configuration
@EnableWebSecurity
public class Oauth2Configuration {

    @Autowired
    ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain configure(HttpSecurity security) throws Exception {
        security.authorizeRequests() .antMatchers("/error").permitAll()
        .anyRequest().authenticated().and().oauth2Login()
        .authorizationEndpoint()
        .authorizationRequestResolver(new CustomRequestResolver(clientRegistrationRepository,"/oauth2/authorization"))
        .and().redirectionEndpoint().baseUri(Constants.AUTH_CALLBACK_URL)
        .and().successHandler(successHandler()).failureHandler(failureHandler());
        return security.build();
    }

    @Bean
    public CustomSuccessHandler successHandler() {
        return new CustomSuccessHandler();
    }

    @Bean
    public CustomAuthenticationFailureHandler failureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

}

class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String baseUrl = "/";
        if(request.getRequestURI().toString().equals(Constants.AUTH_CALLBACK_URL)) {
            baseUrl = request.getSession().getAttribute("baseUrl").toString();
            request.getSession().setAttribute("accountId", request.getParameter("state"));
        }
        this.setDefaultTargetUrl(baseUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }

}

class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
 
    @Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
        this.setDefaultFailureUrl("/error");
        request.getSession().setAttribute("error_description", request.getParameter("error_description"));
        super.onAuthenticationFailure(request, response, exception);
	}
}

class CustomRequestResolver implements OAuth2AuthorizationRequestResolver {
    private final OAuth2AuthorizationRequestResolver defaultRequestResolver;

    public CustomRequestResolver(ClientRegistrationRepository clientRepo, String authorizationUrl){
        this.defaultRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRepo,authorizationUrl);
    }


    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        return createCustomEntry(request);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        return createCustomEntry(request);
    }


    private OAuth2AuthorizationRequest createCustomEntry(HttpServletRequest request){
        OAuth2AuthorizationRequest authorizationRequest = this.defaultRequestResolver.resolve(request,Constants.CLIENT_REGISTRATION_ID);
        var accountId = CommonUtils.getAccountIdFromRequest(request);
        if(!accountId.equals("") && request.getSession().getAttribute("baseUrl") == null) {
            authorizationRequest = OAuth2AuthorizationRequest.from(authorizationRequest).state(accountId).build();
            request.getSession().setAttribute("baseUrl", request.getRequestURI());
        } else {
            authorizationRequest = this.defaultRequestResolver.resolve(request);
        }
        if(!Objects.isNull(authorizationRequest)){
            String customAuthUrl = UriComponentsBuilder
                    .fromUriString(authorizationRequest.getAuthorizationRequestUri())
                    .queryParam("account_id",accountId)
                    .build(true)
                    .toUriString();
            authorizationRequest = OAuth2AuthorizationRequest.from(authorizationRequest)
                    .authorizationRequestUri(customAuthUrl)
                    .build();
        }
        return authorizationRequest;
    }

}