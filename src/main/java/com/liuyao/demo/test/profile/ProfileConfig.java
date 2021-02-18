package com.liuyao.demo.test.profile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

//2：profile配置
@Configuration
public class ProfileConfig {

    @Bean
    @Profile("dev") //Profile 为dev时实例化devDemoBean
    public DemoBean devDemoBean() {
        return new DemoBean("from development profile");
    }

    @Bean
    @Profile("prod") //Profile 为prod时实例化prodDemoBean
    public DemoBean prodDemoBean() {
        return new DemoBean("from production profile");
    }
}
