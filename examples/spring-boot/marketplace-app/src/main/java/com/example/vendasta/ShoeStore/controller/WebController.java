package com.example.vendasta.ShoeStore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.vendasta.ShoeStore.entity.business.BusinessLocations;
import com.example.vendasta.ShoeStore.service.ServiceAccount;
import com.example.vendasta.ShoeStore.utils.CommonUtils;
import com.example.vendasta.ShoeStore.utils.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@Controller
public class WebController {

    @Autowired
    ServiceAccount accessTokenService;

    @Value("${environment}")
    protected String environment;

    @GetMapping("/{accountId}")
    public String home(Model model,HttpServletRequest request, HttpServletResponse response,@AuthenticationPrincipal OAuth2User principal,@PathVariable("accountId") String accountId, HttpSession session) throws IOException{
        if(!CommonUtils.isValidAccount(accountId)) {
            response.sendRedirect("/error");
            return null;
        }
        session.setAttribute("accountId",accountId);
        model.addAttribute("product_navbar_data_url", principal.getAttribute("product_navbar_data_url"));
        model.addAttribute("account_id",  accountId);
        model.addAttribute("app_id", Constants.APPID);
        return "index";
    }

    @GetMapping("/entry/{accountId}")
    public void entry(HttpServletRequest request , HttpServletResponse response, @PathVariable("accountId") String accountId) throws IOException {
        response.sendRedirect("/" + accountId);
        response.setStatus(HttpStatus.SEE_OTHER.value());
    }
    
    @GetMapping("/business/list/{accountId}")
    public String businessList(Model model,@AuthenticationPrincipal OAuth2User principal,HttpSession session,HttpServletRequest request, @PathVariable("accountId") String accountId){
        final String uri = String.format(Constants.BUSINESS_LOCATIONS_API_URL, environment, Constants.PARTNERID);
        BusinessLocations businessLists = accessTokenService.fetchBusinessLocations(uri);
        model.addAttribute("businessLists", businessLists.getData());
        model.addAttribute("product_navbar_data_url", principal.getAttribute("product_navbar_data_url"));
        model.addAttribute("account_id",  accountId.toString());
        model.addAttribute("app_id", Constants.APPID);
        return "business/list";
    }
}
