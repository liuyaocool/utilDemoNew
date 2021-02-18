package com.liuyao.demo.utils;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;

public class DBUtil {

    public <E> E getMapper(Class<E> clazz){
        String driver = "org.postgresql.Driver";
        String url = "jdbc:postgresql://192.168.1.235:5432/ywsydb";
        String username="ywsy";
        String password="ywsy123";
        /**
         * 创建使用缓存池的数据源
         * <dataSource type="POOLED">
         <property name="driver" value="${jdbc.driverClassName}"/>
         <property name="url" value="${jdbc.url}"/>
         <property name="username" value="${jdbc.username}"/>
         <property name="password" value="${jdbc.password}"/>
         </dataSource>
         */
        DataSource dataSource =new PooledDataSource(driver,url,username,password);
        /**
         *  创建事务
         * <transactionManager type="JDBC" />
         */
        TransactionFactory transactionFactory =  new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        /**
         * 加入资源
         * <mapper resource="ssm/BlogMapper.xml"/>
         */
        configuration.addMapper(clazz);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        SqlSession ss =  sqlSessionFactory.openSession(true);//自动提交

        System.out.println(sqlSessionFactory);

        return ss.getMapper(clazz);
    }
}
