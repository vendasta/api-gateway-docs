package com.example.vendasta.ShoeStore.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.vendasta.ShoeStore.entity.business.BusinessLocationData;
import com.example.vendasta.ShoeStore.entity.business.BusinessLocations;
import com.example.vendasta.ShoeStore.service.ApiService;
import com.example.vendasta.ShoeStore.utils.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;

@Controller
public class WebController {

    @Autowired
    ApiService apiService;

    @Value("${apigateway.environment}")
    protected String environment;
   
    @Value("${apigateway.app-id}")
    protected String appId;
    
    @Value("${apigateway.partner-id}")
    protected String partnerId;

    @GetMapping("/{accountId}")
    public String home(Model model,HttpServletRequest request, HttpServletResponse response,@AuthenticationPrincipal OAuth2User principal,@PathVariable("accountId") String accountId, HttpSession session) throws IOException{
        if(!canAccessAccount(principal, accountId)) {
            response.sendRedirect("/error");
            return null;
        }
        session.setAttribute("accountId",accountId);
        model.addAttribute("product_navbar_data_url", principal.getAttribute("product_navbar_data_url"));
        model.addAttribute("account_id",  accountId);
        model.addAttribute("app_id", appId);
        return "index";
    }

    @GetMapping("/businesses")
    public String businessList(Model model,HttpServletResponse response,@AuthenticationPrincipal OAuth2User principal,HttpSession session,HttpServletRequest request) throws IOException{
        if(!canAccessAdminPage(principal)) {
            response.sendRedirect("/error");
            return null;
        }
        final String uri = String.format(Constants.BUSINESS_LOCATIONS_API_URL, environment, partnerId);
        BusinessLocations businessLists = apiService.fetchBusinessLocations(uri);
        model.addAttribute("businessLists", businessLists.getData());
        model.addAttribute("product_navbar_data_url", principal.getAttribute("product_navbar_data_url"));
        model.addAttribute("account_id",  request.getSession().getAttribute("accountId"));
        model.addAttribute("app_id", appId);
        return "business/list";
    }
    
    @GetMapping("/businesses/account/{accountId}")
    public String businessAccount(Model model,HttpServletResponse response,@AuthenticationPrincipal OAuth2User principal,@PathVariable("accountId") String accountId,HttpSession session,HttpServletRequest request) throws IOException{
        if(!canAccessAdminPage(principal)) {
            response.sendRedirect("/error");
            return null;
        }
        final String uri = String.format(Constants.GET_BUSINESS_LOCATION, environment, accountId);
        BusinessLocationData business = apiService.getBusinessLocation(uri);
        model.addAttribute("business", business.getData());
        model.addAttribute("product_navbar_data_url", principal.getAttribute("product_navbar_data_url"));
        model.addAttribute("account_id",  request.getSession().getAttribute("accountId"));
        model.addAttribute("app_id", appId);
        return "business/business";
    }

    private boolean canAccessAccount(OAuth2User principal, String accountId){
        if (canAccessAdminPage(principal)){
            return true;
        }
            // if(apiService.checkAccountAccess(accountId,principal.getAttribute("sub"))) {
            //     return true;
            // } else {
            //     return false;
            // }

            //TODO Decide what we want to do for an access check. The current method is to
        // call https://developers.vendasta.com/api/v1/user/{user_id}/permissions/{account_id}
        // using an app access token
        // https://developers.vendasta.com/vendor/df4894447fee6-check-user-access-to-an-account

        return true;
    }

    private boolean canAccessAdminPage(OAuth2User principal){
        String namespace = principal.getAttributes().get("namespace").toString();
        if(!namespace.equals(partnerId)) {
            return false;
        }

        ArrayList<String> roles = (ArrayList<String>) principal.getAttributes().get("roles");
        if (roles.contains("partner") || roles.contains("sales_person")|| roles.contains("digital_agent")){
            return true;
        }

        return false;
    }
}
