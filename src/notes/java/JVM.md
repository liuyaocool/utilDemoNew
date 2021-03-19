

### meta space

# 类加载-初始化

## class loading

1. 双亲委派
   - 打破双亲委派
     - 重写loadClass（）
   - 打破双亲委派情况
     - JDK1.2之前，自定义ClassLoader都必须重写loadClass()
     - ThreadContextClassLoader可以实现基础类调用实现类代码，通过thread.setContextClassLoader指定
     - 热启动，热部署
       - osgi tomcat 都有自己的模块指定classloader（可以加载同一类库的不同版本）
2. LazyLoading 5种情况
   - new getstatic putstatic invokestatic指令，访问final变量除外
   - java.lang.reflect对类进行反射调用时
   - 初始化子类的时候，父类首先初始化
   - 虚拟机启动时，被执行的主类必须初始化
   - 动态语言支持java.lang.invoke.MethodHandle解析的结果为REF_getstatic REF_putstatic REF_invokestatic的方法句柄时，该类必须初始化
3. ClassLoader的源码
   - findInCache -> parent.loadClass -> findClass()
4. 自定义类加载器
   1. extends ClassLoader
   2. overwrite findClass() -> defineClass(byte[] -> Class clazz)
   3. 加密

## Linking



### jvm执行模式

hot spot = 热点  ？

- 解释器
  - bytecode intepreter
  - jvm参数
    - -Xint 解释模式，启动快，执行稍慢
- JIT
  - jv参数
    - -Xcomp 纯编译模式，启动很慢，执行很快
  - just in time compiler
  - 将热代码保存为本地代码(exe文件)
  - 效率高于解释器模式
- 混合模式 
  - jvm参数
    - -Xmixed 默认混合
    - 开始解释执行，启动速度较快
    - 对热点代码实行检测和编译
  - 解释器+热点代码编译
  - 起始阶段采用解释执行
  - 热点代码检测 -XX:Compile hreshold = 10000
    - 多次被调用的方法（方法计数器：检测方法执行频率）
    - 多次被调用的循环（循环计数器：检测循环执行频率）
    - 进行编译









# 进度

 第三节（93）  00:44:00

