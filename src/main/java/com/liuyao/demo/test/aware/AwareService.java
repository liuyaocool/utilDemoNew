package com.liuyao.demo.test.aware;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

//2:spring aware 演示bean
@Service
public class AwareService implements BeanNameAware,ResourceLoaderAware { // 实现接口 获得Be名称和资源加载的服务

    private String beanName;
    private ResourceLoader loader;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.loader = resourceLoader;
    }

    public void outputResult() {
        System.out.println("Bean的名称为：" + beanName);

        Resource resource = loader.getResource("classpath:com/wisely/highlight_spring4/ch3/aware/test.txt");

        try {

            System.out.println("ResourceLoader加载的文件内容为：" + IOUtils.toString(resource.getInputStream()));
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
