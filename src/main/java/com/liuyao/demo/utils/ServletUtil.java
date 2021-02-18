package com.liuyao.demo.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ServletUtil extends RequestContextHolder{


    private static final String[] IP_HEADERS = {
            "x-forwarded-for",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };
    private static final String[][] AJAX_HEADERS = {
            {"accept", "application/json"},
            {"X-Requested-With", "XMLHttpRequest"},
    };

    /**
     * 获取IP地址
     *
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，
     * X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getRemoteIps() {
        HttpServletRequest request = getRequest();
        String ip = null;
        for (int i = 0; i < IP_HEADERS.length; i++) {
            if(isIp(ip)) break;
            ip = request.getHeader(IP_HEADERS[i]);
        }
        ip = isIp(ip) ? ip : request.getRemoteAddr();
        return getFirstIp(ip);
    }

    /**
     * 获取当前网络ip
     * @return
     */
    public static String getAddr(){
        String ip = getRemoteIps();
        if(ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")){
            //根据网卡取本机配置的IP
            try {
                InetAddress inet = InetAddress.getLocalHost();
                ip= inet.getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return getFirstIp(ip);
    }

    //使用代理，则获取第一个IP地址
    private static String getFirstIp(String ip){
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(null != ip && ip.length() > 15 && ip.indexOf(",") > 0){
            //"***.***.***.***".length() = 15
            ip = ip.split(",")[0];
        }
        return ip;
    }

    private static boolean isNotIp(String ip){
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

    private static boolean isIp(String ip){
        return !ObjectUtil.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip);
    }


    public static HttpServletRequest getRequest() {
        return getServlet().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return getServlet().getResponse();
    }

    public static ServletRequestAttributes getServlet(){
        return (ServletRequestAttributes) currentRequestAttributes();
    }

    public static void redirectUrl(HttpServletRequest request, HttpServletResponse response, String url) {
        try {
            if (isAjaxRequest(request)) {
                request.getRequestDispatcher(url).forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + url);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static boolean isAjaxRequest(HttpServletRequest request) {

        for (int i = 0; i < AJAX_HEADERS.length; i++) {
            String header = request.getHeader(AJAX_HEADERS[i][0]);
            if (null != header && header.indexOf(AJAX_HEADERS[i][1]) != -1) {
                return true;
            }
        }
        if (ObjectUtil.equalsIgnoreCase(request.getRequestURI(), new String[]{".json", ".xml"})) {
            return true;
        } else {
            return ObjectUtil.equalsIgnoreCase(request.getParameter("__ajax"), new String[]{"json", "xml"});
        }
    }

    public static String getParameter(String name) {
        HttpServletRequest request = getRequest();
        return request == null ? null : request.getParameter(name);
    }

    public static Map<String, Object> getParameters() {
        return getParameters(getRequest());
    }

    public static Map<String, Object> getParameters(ServletRequest request) {
        return request == null ? new HashMap<>() : getPreParam(request, "");
    }

    public static Map<String, Object> getPreParam(ServletRequest request, String pre) {
        Validate.notNull(request, "Request must not be null", new Object[0]);
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap();
        pre = null == pre ? "" : pre;

        while(paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (null != paramName && paramName.startsWith(pre)) {
                String unprefixed = paramName.substring(pre.length());
                String[] values = request.getParameterValues(paramName);
                if (values != null && values.length != 0) {
                    params.put(unprefixed, values.length > 1 ? values : values[0]);
                }
            }
        }

        return params;
    }

    public String getHeader(String headerName){
        return getRequest().getHeader(headerName);
    }















    public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
        response.setDateHeader("Expires", System.currentTimeMillis() + expiresSeconds * 1000L);
        response.setHeader("Cache-Control", "private, max-age=" + expiresSeconds);
    }

    public static void setNoCacheHeader(HttpServletResponse response) {
        response.setDateHeader("Expires", 1L);
        response.addHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
    }

    public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
        response.setDateHeader("Last-Modified", lastModifiedDate);
    }

    public static void setEtag(HttpServletResponse response, String etag) {
        response.setHeader("ETag", etag);
    }

    public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response, long lastModified) {
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        if (ifModifiedSince != -1L && lastModified < ifModifiedSince + 1000L) {
            response.setStatus(304);
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
        String headerValue = request.getHeader("If-None-Match");
        if (headerValue != null) {
            boolean conditionSatisfied = false;
            if (!"*".equals(headerValue)) {
                StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

                while(!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(etag)) {
                        conditionSatisfied = true;
                    }
                }
            } else {
                conditionSatisfied = true;
            }

            if (conditionSatisfied) {
                response.setStatus(304);
                response.setHeader("ETag", etag);
                return false;
            }
        }

        return true;
    }

}
