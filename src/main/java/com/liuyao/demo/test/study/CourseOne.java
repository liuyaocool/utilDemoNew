package com.liuyao.demo.test.study;

//class类的使用
public class CourseOne {
	
	//万事万物皆对象,除了基本数据类型与java静态东西
	//任何一个类都是对象,是java.lang.Class类的实例对象
	public static void main(String[] args) {
		//Foo的实例对象
		Foo foo1 = new Foo();
		
		//Foo这个类是Class类的实例对象,有三种表示方式
		//1: 告诉我们每个类都有一个隐含的静态成员变量class
		Class c1 = Foo.class;
		
		//2: 
		Class c2 = foo1.getClass();
		
		//官方说明:c1 c2 表示了Foo类的类类型(class type)
		
		System.out.println(c1 == c2);//true
		
		//3:
		Class c3 = null;
		try {
			c3 = Class.forName("com.imooc.study.test_class_reflect.Foo");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(c2 == c3);//true
		
		//可通过类的类类型创建该类的实例对象--->即通c1,c2,c3创建Foo的实例对象
		Foo foo = null;
		try {
			foo = (Foo) c1.newInstance();//前提:需要无参构造
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class Foo{
	
	void print() {
		System.out.println("foo");
	}
}
