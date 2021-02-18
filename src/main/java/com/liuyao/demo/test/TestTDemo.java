package com.liuyao.demo.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestTDemo{
	
	public static void main(String[] args) {
		Test_T_fanXing<Integer> inte = new Test_T_fanXing<Integer>(77);
		inte.showType();
		System.out.println("=============================");
		
		Test_T_fanXing<String> stringT = new Test_T_fanXing<String>("stri");
		stringT.showType();
		System.out.println("=========================");
		
		Test_T_Collection<ArrayList, Object, Object> collectionT = new Test_T_Collection<>(new ArrayList());
		collectionT.showType();
		System.out.println("---------------------------------");
		//错误写法<>中内容应该保持一致
//		Test_T_Collection<Collection> collectionColl = new Test_T_Collection<ArrayList>(new ArrayList());

		/** =====================================通配符测试==================================================== */
//		testTomgpei(new Test_T_fanXing<Integer>());//错误使用
		testTomgpei1(new Test_T_fanXing<Integer>());//使用通配符则可解决上述问题
		testTomgpei3(new Test_T_fanXing<Number>());//泛型上边界
		testTomgpei3(new Test_T_fanXing<Double>());
		testTomgpei3(new Test_T_fanXing<Integer>());
//		testTomgpei3(new Test_T_fanXing<String>());//因为String不是Number的子类,错误
//		testTomgpei2(new Test_T_fanXing<Integer>());//错误使用
		testTomgpei(new Test_T_fanXing<>());

		/** =====================================泛型数组==================================================== */
		//java 不能创建一个确切的泛型类型的数组
//		List<String>[] ls = new ArrayList<String>[10];// 错误
		List<?>[] ls1 = new ArrayList<?>[10];
		List<String>[] ls2 = new ArrayList[10];

//		List<String>[] lsa = new List<String>[10]; // Not really allowed.
//		Object o = lsa;
//		Object[] oa = (Object[]) o;
//		List<Integer> li = new ArrayList<Integer>();
//		li.add(new Integer(3));
//		oa[1] = li; // Unsound, but passes run time store check
//		String s = lsa[1].get(0); // Run-time error: ClassCastException.

		List<?>[] lsa = new List<?>[10]; // OK, array of unbounded wildcard type.
		Object o = lsa;
		Object[] oa = (Object[]) o;
		List<Integer> li = new ArrayList<Integer>();
		li.add(new Integer(3));
		oa[1] = li; // Correct.
		Integer i = (Integer) lsa[1].get(0); // OK

	}

	//通配符:?
	public static void testTomgpei(Test_T_fanXing<Number> obj){ }
	/**
	 *  这不是一个泛型方法 只是一个普通方法使用了一个泛型参数而已
	 * 	注:此处?是类型实参 非形参
	 * 	通俗说:就是?与NumberInteger都是一种实际类型
	 * 	可以吧?看成所有类型的父类
	 * 	可以解决具体类型不确定的情况
	 */
	public static void testTomgpei1(Test_T_fanXing<?> obj){ }

	/**
	 * 泛型 进行上下边界限制  泛型类同理
	 * @param obj
	 */
	public static void testTomgpei3(Test_T_fanXing<? extends Number> obj){ }
	public static void testTomgpei2(Test_T_fanXing<Object> obj){ }

	/**
	 * 泛型方法
	 * <T>: 表明这是一个泛型方法
	 * 泛型数量可以是任意多个
	 */
	public static <T,K> T testTmethod(Test_T_fanXing<T> obj){
		return obj.getOb();
	}
	/**
	 * 泛型方法 添加上下边界限制
	 */
//	public static <T> T testTmethod2(Test_T_fanXing<T extends Number> obj){ //会报错
	public static <T extends Number> T testTmethod2(Test_T_fanXing<T> obj){
		return obj.getOb();
	}

}

class Test_T_fanXing<T> {

	private T ob;//泛型成员变量
	
	public Test_T_fanXing(){ }
	public Test_T_fanXing(T ob){
		this.ob = ob;
	}
	
	public T getOb() {
		return ob;
	}

	public void setOb(T ob) {
		this.ob = ob;
	}

	void showType() {

		System.out.println("T的实际类型是:" + ob.getClass().getName());
	}

	public <E> void showE(E e){ }
	//此处T可以与本类中的泛型不同,为一种全新的类型
	public <T> T showT(T t){
		return t;
	}

	public static <T> void show(T t){}
//	一个static的方法，无法访问泛型类型的参数。如果static方法要使用泛型能力，就必须使其成为泛型方法。
//	public static void show(T t){} //即使静态方法要使用泛型类中已经声明过的泛型也不可以

}

//泛型可以继承一个类多个接口,但类必须放在第一个
//泛型继承是为了限制泛型的类型 如下: T继承Collection后,只能传入继承Collection的类型
//泛型可以定义多个  ','分隔
class Test_T_Collection<T extends Collection & Serializable, T1 ,T2>{
	
	private T ob;//泛型成员变量
	private T2 oo;
	private T1 bb;
	
	public Test_T_Collection(T ob){
		this.ob = ob;
	}
	
	public T getOb() {
		return ob;
	}

	public void setOb(T ob) {
		this.ob = ob;
	}

	void showType() {

		System.out.println("T的实际类型是:" + ob.getClass().getName());
	}
	
	<T> T showVoid(T t) {
		
		System.out.println("这是void泛型方法,输出参数:" + t);
		return t;
	}
	
	T2 showFanXing(T2 t) {
		return t;
	}
	
}
