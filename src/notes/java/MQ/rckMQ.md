# 安装 启动

## 安装

1. github 下载源码
2.  源码根目录执行： mvn -Prelease-all -DskipTests clean install -U
3. cd distribution/target/rocketmq-4.6.1/*/ ，即发布版
4. 将程序包移动到合适位置

## 启动

1. ./bin/mqnamesrv 前台启动nameserver
   1. vi runserver.sh，-Xms4g -Xmx4g -Xmn2g
2. ./bin/mqbroker -n localhost:9876 前台启动broker
   1. -n ip:port  --指定nameserver
   2. No address associated with hostname
      1. 添加hosts 配置
   3. vi runbroker.sh ，修改 -Xms512M -Xmx512m -Xmn=128M

## 测试用例

1. ./bin/tools.sh org.apache.rocketmq.example.quickstart.Producer
2. 报错
   1. connect to null failed：
      1. tools.sh 添加 export NAMESRV_ADDR=localhost:9876
      2. 注意不是加到最后 应该是指定位置
   2. broker 启动需要指定nameserver

## console

1. 下载
   1. git clone https://github.com/apache/rocketmq-externals.git
2. 安装
   1. cd rocketmq-console
   2. mvn clean package -Dmaven.test.skip=true
   3. mv ./target rocketmq-console-ng-1.0.1.jar /***
3. 启动
   1. java -jar rocketmq-console-ng-1.0.1.jar



## 其他扩展

https://github.com/apache/rocketmq-externals



