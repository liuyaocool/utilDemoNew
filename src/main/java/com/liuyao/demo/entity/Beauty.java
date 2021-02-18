package com.liuyao.demo.entity;

import com.liuyao.demo.anno.MyAnno;

import java.io.Serializable;

/**
 * 实现序列化的作用
 * 两个java程序之间通信,文字可以但不可以传对象
 * 当序列化之后,对象被序列化成一种类似文字流的东西,传过去之后,再用相同的序列化规则解析成对象,则是先传对象
 * (类似于调制 解调)
 */
public class Beauty implements Serializable {

    private String id;
    private String name;
    public String name1;
    private int age;
    public String eye;
    public String nouse;
    public String mouse;

    public Beauty(String name) {
        this.name = name;
    }
    public Beauty() {
        System.out.println("公开无参构造方法");
    }

    private Beauty(String id, String name) {
        System.out.println("私有无参构造方法");
    }

    public String getId() {
        return id;
    }

    @MyAnno(true)
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @MyAnno(false)
    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void show1(String s){
        System.out.println("调用了：公有的，String参数的show1(): 参数 = " + s);
    }
    protected void show2(){
        System.out.println("调用了：受保护的，无参的show2()");
    }
    void show3(){
        System.out.println("调用了：默认的，无参的show3()");
    }
    private String show4(int age){
        System.out.println("调用了，私有的，并且有返回值的，int参数的show4(): age = " + age);
        return "abcd";
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getEye() {
        return eye;
    }

    @MyAnno(false)
    public void setEye(String eye) {
        this.eye = eye;
    }

    @MyAnno(true)
    public String getNouse() {
        return nouse;
    }

    public void setNouse(String nouse) {
        this.nouse = nouse;
    }

    @MyAnno(false)
    public String getMouse() {
        return mouse;
    }

    public void setMouse(String mouse) {
        this.mouse = mouse;
    }
}
