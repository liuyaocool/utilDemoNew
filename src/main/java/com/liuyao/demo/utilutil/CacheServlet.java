package com.liuyao.demo.utilutil;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
* @ClassName: CacheServlet  
* @Description: TODO(字典缓存)  
* @author yangrongze  
* @date 2018年4月26日  
*
 */
public class CacheServlet extends HttpServlet {

    public CacheServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() throws ServletException {
    	ServletContext context = this.getServletContext();
		if(context.getAttribute("dicCode")!=null) {
			return;
		}
		Map<String,Object> map = new HashMap<>();
		map.put("ver", 0.1);
		context.setAttribute("cache", map);
    	System.out.println("字典缓存创建成功");
    }
    
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.setContentType("text/x-javascript;charset=utf-8");
		resp.setHeader("cache-control", "no-cache");
		JSONObject json = new JSONObject();
		json.put("dicCode", this.getServletContext().getAttribute("dicCode"));
		System.out.println(json.toString());
		resp.getWriter().write(json.toString());
		resp.getWriter().flush();
	}

}
