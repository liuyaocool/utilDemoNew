package com.liuyao.demo.interceptor;

import com.liuyao.demo.config.SysConfig;
import com.liuyao.demo.utils.SpringUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MacHandlerInterceptor implements HandlerInterceptor {

    private final List<String> MAC_LIST = new ArrayList<>();

    private SysConfig sysConfig = SpringUtil.getBean(SysConfig.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        System.out.println(request.getRequestURI());
        if (checkMac(request)){
            return true;
        }

//        response.sendRedirect(request.getContextPath() + "/error/nopower");
//        response.setStatus(404);
        response.setContentType("text/html;charset=UTF-8;");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("<h1 style='text-align:center;margn-top: 100px;'>越权访问<h1>");
        response.getWriter().close();
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    public boolean checkMac(HttpServletRequest request) {

        String macAddress = null;
        String ip = request.getRemoteAddr();
        if ("127.0.0.1".equals(ip)) {
            return true;
        } else {
            InputStreamReader isr = null;
            BufferedReader reader = null;
            try {
                //  |findstr + ip 不可用
                Process p = Runtime.getRuntime().exec("arp -a");
                isr = new InputStreamReader(p.getInputStream());
                reader = new BufferedReader(isr);
                String line;
                while (null != (line = reader.readLine())){
                    if ((line = line.trim()).startsWith(ip)){
                        String mac = line.replace(ip, "").trim().split(" ")[0];
                        if (MAC_LIST.contains(mac)){
                            return true;
                        } else {
                            String[] macs = readFile(sysConfig.getMaclistfilepath(), "utf-8").split(",");
                            for (int i = 0; i < macs.length; i++) {
                                if (!MAC_LIST.contains(macs[i])){
                                    MAC_LIST.add(macs[i]);
                                }
                            }
                            if (MAC_LIST.contains(mac)) return true;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close(isr);
                close(reader);
            }

            return false;
        }
    }


    public static String readFile(String path, String encoding){
        BufferedReader reader = null;
        FileInputStream fis = null;
        InputStreamReader isr = null;
        encoding = null == encoding ? "UTF-8" : encoding;
        StringBuilder res = new StringBuilder();
        try{
            fis = new FileInputStream(path);
            isr = new InputStreamReader(fis, encoding);
            reader = new BufferedReader(isr);
            String line;
            while((line = reader.readLine()) != null){
                res.append(line).append("\n");
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            close(fis);
            close(isr);
            close(reader);
        }
        return res.toString();
    }

    public static void close(AutoCloseable c){
        try {
            if (null != c) c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
