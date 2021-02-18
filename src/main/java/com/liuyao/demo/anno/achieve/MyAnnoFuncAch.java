package com.liuyao.demo.anno.achieve;

import com.liuyao.demo.anno.MyAnno;
import com.liuyao.demo.utilutil.MyClassUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

public class MyAnnoFuncAch {

	@SuppressWarnings("deprecation")
	public static void achieveAnno(String pkgName) throws ClassNotFoundException {

		System.out.println(new Date().getDate());

		List<Class<?>> clazs = MyClassUtil.getClasses(pkgName);

		System.out.println("++++++++++++++_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_+_");
		for (int i = 0; i < clazs.size(); i++) {

//			Class clazz = Class.forName("com.liuyao.demo.entity.Hero");

			Class clazz = clazs.get(i);
			//判断class是否有这个注解
			boolean hasAnno = clazz.isAnnotationPresent(MyAnno.class);

			if (hasAnno) {
				MyAnno myAnno = (MyAnno) clazz.getAnnotation(MyAnno.class);

				System.out.println(myAnno.value());
			}

			//获得方法上的注解
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				boolean methodHasAnno = method.isAnnotationPresent(MyAnno.class);
				if (methodHasAnno) {

					MyAnno myAnno = method.getAnnotation(MyAnno.class);
					System.out.println(method + "/" + myAnno.value());
				}
			}

			//获得方法注解值
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				boolean fieldHasAnno = field.isAnnotationPresent(MyAnno.class);
				if (fieldHasAnno) {

					MyAnno myAnno = field.getAnnotation(MyAnno.class);
					System.out.println(field + "/" + myAnno.value());
				}
			}

		}

	}
}
