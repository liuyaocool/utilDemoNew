package com.liuyao.demo.mashibing.entity.impl;

import com.liuyao.demo.mashibing.entity.Human;

public class Girl implements Human {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void eat(){
        System.out.println(name + " eating");
    }

    public void bath(){
        System.out.println(name + " bathing");
    }
}
