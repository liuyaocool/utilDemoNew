package com.liuyao.demo.config;

import com.liuyao.demo.interceptor.MacHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * implements WebMvcConfigurer
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MacHandlerInterceptor())
//                .addPathPatterns("/**")
                /**
                 * 系统可访问内静态资源 见：addResourceHandlers
                 * 默认静态资源是映射到 “/**” 下的
                 * 静态文件不经过拦截器
                 * 配置此处 需搭配spring.mvc.static-path-pattern: /sta/**
                 * 页面引用 也须加前缀 sta/
                 */
                .excludePathPatterns(new String[]{"/sta/**"})
        ;
    }

    //重写此方法 可访问到静态文件
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                //配置静态资源路径 则不会出现静态资源no mapping现象
                .addResourceLocations("classpath:/static/")
        ;
    }


//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(baseInterceptor)
//                //需要配置2：----------- 告知拦截器：/static/admin/** 与 /static/user/** 不需要拦截 （配置的是 路径）
//                .excludePathPatterns("/static/admin/**", "/static/user/**");
//    }
//
//    /**
//     * 添加静态资源文件，外部可以直接访问地址
//     * @param registry
//     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        //其他静态资源，与本文关系不大
//        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+ TaleUtils.getUplodFilePath()+"upload/");
//
//        //需要配置1：----------- 需要告知系统，这是要被当成静态文件的！
//        //第一个方法设置访问路径前缀，第二个方法设置资源路径
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//    }
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        //registry.addViewController("/error/404").setViewName("/admin/page_error/error_404.html");
//    }
}
