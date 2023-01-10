package com.example.vendasta.ShoeStore.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController  {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request,Model model) {
        int httpErrorCode = getErrorCode(request);
        String errorMessage = null;
        if(request.getSession().getAttribute("error_description") != null) {
            errorMessage = "⛔ \n401\n" + request.getSession().getAttribute("error_description").toString();
        }
        if(errorMessage == null) {
            switch (httpErrorCode) {
                case 400: {
                    errorMessage = "⚠ \n 400 \n Bad Request";
                    break;
                }
                case 401: {
                    errorMessage = "⛔ \n401\nAuthorization Required";
                    break;
                }
                case 403: {
                    errorMessage = "⛔ \n403\n User does not have permission to the account.";
                    break;
                }
                case 404: {
                    errorMessage = " ⚠ \n404\n Look like you're lost.";
                    break;
                }
                case 500: {
                    errorMessage = " 🔌 \n500\n Internal server error.";
                    break;
                }
                default:
                    errorMessage = "⚠ \n Something Went Wrong";
                    break;
            }
        }
        model.addAttribute("message",  errorMessage);
        return "error";
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        if(httpRequest.getSession() != null && httpRequest.getSession().getAttribute("error_description") != null) {
            return 401;
        } else {
            if(httpRequest.getAttribute("javax.servlet.error.status_code") != null) {
                return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
            } else {
                return 403;
            }
        }
    }
}