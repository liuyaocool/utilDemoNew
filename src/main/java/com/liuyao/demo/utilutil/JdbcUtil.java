package com.liuyao.demo.utilutil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JdbcUtil {
	private static Properties ps = new Properties();
	private static  ThreadLocal<Connection> t = new ThreadLocal<Connection>();
	static{
		try{
			InputStream in = JdbcUtil.class.getResourceAsStream("/com/config/properties.properties");
			ps.load(in);
			in.close();
		}catch(Exception e){
			e.printStackTrace();
			throw new ExceptionInInitializerError();
		}
	}
	public static Connection getConnection() throws Exception{
		Connection conn = t.get();
		if(conn==null){ 
			String driver = ps.getProperty("driver");
			String url = ps.getProperty("url");
			String username = ps.getProperty("username");
			String psw = ps.getProperty("password");
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username,psw);
		}
		return conn;
	}
	public static void close(Connection conn,Statement statement,ResultSet rs){
		if(rs!=null) try{rs.close();}catch(Exception e){}
		if(statement!=null) try{statement.close();}catch(Exception e){}
		if(conn!=null) try{conn.close();}catch(Exception e){}
		
	}
}
