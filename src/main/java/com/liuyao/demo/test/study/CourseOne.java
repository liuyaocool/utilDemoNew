package com.liuyao.demo.test.study;

//class���ʹ��
public class CourseOne {
	
	//��������Զ���,���˻�������������java��̬����
	//�κ�һ���඼�Ƕ���,��java.lang.Class���ʵ������
	public static void main(String[] args) {
		//Foo��ʵ������
		Foo foo1 = new Foo();
		
		//Foo�������Class���ʵ������,�����ֱ�ʾ��ʽ
		//1: ��������ÿ���඼��һ�������ľ�̬��Ա����class
		Class c1 = Foo.class;
		
		//2: 
		Class c2 = foo1.getClass();
		
		//�ٷ�˵��:c1 c2 ��ʾ��Foo���������(class type)
		
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
		
		//��ͨ����������ʹ��������ʵ������--->��ͨc1,c2,c3����Foo��ʵ������
		Foo foo = null;
		try {
			foo = (Foo) c1.newInstance();//ǰ��:��Ҫ�޲ι���
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
