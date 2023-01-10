package com.example.vendasta.ShoeStore.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class ParserUtils {

    public static String getAccountIdFromRequest(HttpServletRequest request) {
        String[] parts = request.getServletPath().split("/");
        for (String part : parts) {
            Pattern pattern = Pattern.compile("AG-[a-zA-Z\\d]{10}");
            Matcher matcher = pattern.matcher(part.trim());
            if(matcher.find()) {
                return part;
            }
        }
        return "";
    }
  
    public static boolean isValidAccount(String part) {
        Pattern pattern = Pattern.compile("AG-[a-zA-Z\\d]{10}");
        Matcher matcher = pattern.matcher(part.trim());
        if(matcher.find()) {
            return true;
        }
        return false;
    }

}
