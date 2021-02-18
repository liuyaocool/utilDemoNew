package com.liuyao.demo.test;

import com.liuyao.demo.entity.Hero;
import com.liuyao.demo.utils.LogUtil;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class ReflectTest {

    public static void main(String[] args){

        try {

            //反射机制获得类
            Class clazz = Class.forName("com.liuyao.demo.entity.Hero");//自动调用无参公有构造

            System.out.println("======================获得公有构造============================");
            //获得所有公有构造
            Constructor[] constructors = clazz.getConstructors();
            for (Constructor c: constructors) {
//                Object obj = c.newInstance();//调用构造 报错
                System.out.println(c);
            }

            System.out.println("===================获得所有构造================================");
            //获得所有构造方法
            Constructor[] constructors1 = clazz.getDeclaredConstructors();
            for (Constructor c: constructors1) {
                c.setAccessible(true);//暴力访问 忽略访问限制
                System.out.println(c);
            }

            System.out.println("===================获得字段=====================================");
            //获得所有公有字段
            Field[] fieldArray = clazz.getFields();
            //获得所有字段
            Field[] fields = clazz.getDeclaredFields();
            for (Field f :fields) {
                System.out.println(f);
            }

            System.out.println("===================获得公有字段并调用===========================");
            //获得公有字段并调用
            Field f = clazz.getField("name1");
            Object obj = clazz.getConstructor().newInstance();
            f.set(obj, "超人");
            Hero hero1 = (Hero)obj;
            System.out.println(hero1.name1);

            System.out.println("=================获得私有字段并调用=============================");
            //获得私有字段并调用
            Field field = clazz.getDeclaredField("name");
            field.setAccessible(true);
            Object obj2 = clazz.getConstructor().newInstance();
            Hero hero2 = (Hero)obj2;
            field.set(obj2, "钢铁侠");
            System.out.println(hero2.getName());

            System.out.println("====================获得所有公有方法===========================");
            Method[] methods = clazz.getMethods();
            for (Method me : methods) {
                System.out.println(me);
            }
            System.out.println("====================获得所有方法===========================");
            Method[] methodArray = clazz.getDeclaredMethods();
            for(Method m : methodArray){
                System.out.println(m);
            }

            System.out.println("====================获得公有的show1()方法===========================");
            Method m = clazz.getMethod("show1", String.class);
            Object obj4 = clazz.getConstructor().newInstance();
            m.invoke(obj4, "刘德华");

            System.out.println("====================获得私有的show4()方法===========================");
            Method m2 = clazz.getDeclaredMethod("show4", int.class);
            m2.setAccessible(true);//解除私有限定
            Object result = m2.invoke(obj, 20);//需要两个参数，一个是要调用的对象（获取有反射），一个是实参
            System.out.println("返回值：" + result);

            System.out.println("============================结束+++++++++++====================================================");

            Hero hero = new Hero();
            hero.setAge(11);
            System.out.println(hero.getClass() == clazz);


        }catch (ReflectiveOperationException e){

            e.printStackTrace();
        }

    }

    //获得实例中的某个属性值
    private static Object getResult(Object fieldName, Object goodsVO) {
        try {
            Class<?> aClass = goodsVO.getClass();
            Field declaredField = aClass.getDeclaredField(fieldName.toString());
            declaredField.setAccessible(true);
            PropertyDescriptor pd = new PropertyDescriptor(declaredField.getName(), aClass);
            Method readMethod = pd.getReadMethod();

            return readMethod.invoke(goodsVO);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void aaa(int i){

        System.out.println(i);
    }


    /**
     * map 转实体类
     * @param clazz
     * @param map
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Class<T> clazz, Map map) {
        if (null == map || null == clazz) {
            return null;
        }
        T obj = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            // 给 JavaBean 对象的属性赋值
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {
                    Object value = map.get(propertyName);
                    if ("".equals(value)) {
                        value = null;
                    }
                    Object[] args = new Object[1];
                    args[0] = value;
                    try {
                        //如果obj为空则此处空指针
                        descriptor.getWriteMethod().invoke(obj, args);
                    } catch (IllegalAccessException e) {
                        LogUtil.info("实例化JavaBean失败 Error{}");
                    } catch (InvocationTargetException e) {
                        LogUtil.info("字段映射失败 Error{}");
                    }
                }
            }
        } catch (IntrospectionException e) {
            LogUtil.info("分析类属性失败 Error{}");
        }
        return obj;
    }
    public static <T> T mapToBeanNew(Class<T> clazz, Map map) {
        if (null == map || null == clazz) {
            return null;
        }
        try {
            T obj = clazz.newInstance();
            for (Object key : map.keySet()) {
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(String.valueOf(key), clazz);
                    Method writeMethod = pd.getWriteMethod();
                    String name = writeMethod.getParameterTypes()[0].getName();
                    Object[] args = {map.get(key)};
                    if("java.util.Date".equals(name)){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        args[0] = sdf.parse(args[0].toString());
                    }else if (!name.equals(args[0].getClass().getName())){
                        //如果传入的参数与需要的参数不匹配 转换类型
                        args[0] = Class.forName(name).getConstructor(String.class)
                                .newInstance(String.valueOf(args[0]));
                    }
                    writeMethod.invoke(obj, args);
                } catch (ReflectiveOperationException e) {
//                    log.info("字段写入失败："+e.getMessage());
                } catch (IntrospectionException e) {
//                    log.info("字段映射失败："+e.getMessage());
                } catch (ParseException e) {
//                    log.info("字段类型转换失败："+e.getMessage());
                }
            }
            return obj;
        } catch (ReflectiveOperationException e) {
//            log.info("反射实例画失败："+e.getMessage());
        }
        return null;
    }

    //通过class 转换类型
    public static Object castVal(Object value, Class claz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return claz.getConstructor(value.getClass()).newInstance(value);
    }

    //向实体写入值
    private boolean writeValue(Object bean, String attrName, Object value) {
        try {
            Class claz = bean.getClass();
            PropertyDescriptor pd = new PropertyDescriptor(attrName, claz);
            Method writeMethod = pd.getWriteMethod();
            String name = writeMethod.getParameterTypes()[0].getName();
            if("java.util.Date".equals(name)){
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                value=sdf.parse(value.toString());
            }else if (!name.equals(value.getClass().getName())){
                //如果传入的参数与需要的参数不匹配 转换类型
                value = Class.forName(name).getConstructor(String.class)
                        .newInstance(String.valueOf(value));
            }
            Object[] args = {value};
            writeMethod.invoke(bean, args);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
