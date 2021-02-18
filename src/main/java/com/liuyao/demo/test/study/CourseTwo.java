package com.liuyao.demo.test.study;

;

//动态加载类Class.forName("类的全称")
//编译时加载的类是静态加载类(new 对象),运行时动态...
public class CourseTwo {

	public static void main(String[] args) {
		try {
			//动态加载类,在运行时刻加载
			Class c = Class.forName(args[0]);
			//创建对象,通过类类型
			/*会出现错误,如果
			 * Word word = (Word)c.newInstance();
			 * 而创建的对象是Excel就会出现错误
			 * 可以通过接口解决,只需实现OfficeAble接口即可
			 * */
			OfficeAble ob = (OfficeAble)c.newInstance();
			ob.start();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
