package com.liuyao.demo.test.conditional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

//3
@Configuration
public class ConditionConfig {

    @Bean
    @Conditional(WindowsCondition.class) // 符合windows则实例化windowsListService
    public ListService windowsListService() {
        return new WindowsListService();
    }

    @Bean
    @Conditional(LinuxCondition.class) // 符合linux则实例化linuxListService
    public ListService linuxListService() {
        return new LinuxListService();
    }
}
