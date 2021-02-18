package com.liuyao.demo.test.el;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//2：需被注入的Bean
@Service
public class DemoService {

    @Value("其他累的属性")
    private String another; //此处为注入普通字符串

    public String getAnother() {
        return another;
    }

    public void setAnother(String another) {
        this.another = another;
    }
}
