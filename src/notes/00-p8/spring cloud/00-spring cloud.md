# a

provider 只提供服务 轻量级 不主动 不拒绝

# 前置

- spring boot 快速启动器 
  - 看架构师前置知识 前16节课
  - 或 连老师 spring boot 源码 等
- ssm

# 组件

## netflix

## alibaba

## apache

# 结构

## 流量接入层

- cdn 二级域名 xxoo.aaa.com www.aaa.com 不懂??
  - 亿级流量 多级缓存

restful  /Person/1 = /实体/id 不懂??

# 服务注册中心

## Netflix Eureka

Spring cloud 2020.x 版本 移除了netflix的支持

- <spring-cloud.version>Hoxton.SR3</spring-cloud.version>
- spring boot 版本 2.2.6.RELEASE

### 单节点

### 集群

**两种方式**

- 多服务端之间相互调用 拉取，客户端注册一个服务就行
  - eureka.client.service-url.defaultZone=addr1,addr2,...
- 多服务端之间不通信，相互独立，客户端需要注册每一个服务
  - 客户端请求数据的时候会从多个服务上获取



# 服务调用

## feign

## 熔断 降级 

### 原理

### feign+hystrix

- @FeignClient(name="xxx", **fallback** = xxx.class)
- @FeignClient(name="xxx", **fallbackFactory** = xxx.class)
- feign.hystrix.enabled:=true

## 隔离

原理：第7课 1:10:00

### 线程池隔离 官方推荐

### 信号量隔离

# 网关

## zuul

http://localhost:7500/user-consumer/aliveRestTimeout

### 原理



# 链路追踪

配置到所有服务

## sleuth

```
sleuth:
  sampler:
    rate: 1
```

## zipkin

- 下载
  - https://zipkin.io
  - 点击quickstart
  - 找到java 点击latest release
- 启动

```
zipkin:
  base-url: http://localhost:9411
```



# admin

把 acuator 做成ui

```
spring:
  boot:
    admin:
      client:
        url: http://localhost:8090
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always
```



## 邮件通知

password：不是单纯的password 而是邮箱设置有个pop3/smtp服务 需要开启 有个token

## 钉钉群通知

关键字： 发送内容有个关键字 需要在钉钉机器人呢中设置

## 微信通知



# config

- 重启
- 重新加载
- 单服务重新加载
- 通过配置中心一次性 所有微服务 配置重载

## git服务器

github 示例

- 新建一个public 仓库

- http://localhost:9999/consumer-dev.properties

## 坑

```
spring.application.name=user-cosumer
git 的 文件名 则必须为user-consumer-xxx.xxx
```

```
属性值注入失败 则 配置文件改名为： bootstrap.xxx
```

# 会话管理

## Spring Security









# 问题

- eureka 多服务端不互通的集群方式，client拉取到两个服务列表，若不一样 哪一个列表是最新的 服务列表有时间戳吗？
- Spring cloud 在生产环境运行过程中 都会存在什么问题
  - 网络超时
- 信号量隔离 线程隔离
- tomcat work线程是单线程吗

# 需学习

- Spring 事务
- @Component 注解是什么
- Dubbo
- 第五节 前一小时 为什么 sprigcloud， 为什么restful 协议
- rpc
- 上下文

# 进度 第11节 0:14:00

