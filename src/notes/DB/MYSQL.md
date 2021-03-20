# 安装

## linux-yum 

没成功 centos8stream版本 需配置国内镜像 否则很慢

- 检查已安装
  - yum list installed | grep mysql
- 已安装 检查版本 版本不是所需 删除
  - yum -y remove mysql-libs.x86_64
- 安装rpm源
  - wget dev.mysql.com/get/mysql-community-release-el6-5.noarch.rpm
- 安装下好的rpm文件
  - yum install mysql-community-release-el6-5.noarch.rpm -y
  - 会在/etc/yum.repos.d 生成两个 mysql-***.repo 文件
    - vi mysql-community.repo
    - 默认5.6 的enable=1，将5.7的改为1，5.6改为0
  - 更换版本 可通过1 2步骤删除
- 安装mysql
  - yum install mysql-community-server -y

## linux-安装包

- 下载
  1. 官网找到下载页面
  2. 找到 [MySQL Community (GPL) Downloads »](https://dev.mysql.com/downloads/)
  3. 点击 MySql Community Server
  4. 选择系统 找到最大的两个包 根据cpu架构进行下载
  5. 点击 **[No thanks, just start my download.](https://dev.mysql.com/get/Downloads/MySQL-8.0/mysql-8.0.23-1.el8.x86_64.rpm-bundle.tar)**
  6. 迅雷下载更快哦
- 安装
  1. 解压到文件夹 tar -xf *.tar -C /folder
  2. 到解压目录下 执行 
     - yum install -y /folder/mysql-community-*
  3. Complete 完成

# 使用

## 启动

- service mysqld start
  - 5.7启动报错 
    - 临时密码 证数过期 需要更新
    - 执行 yum update -y
    - 删除 rm -rf /var/lib/mysql/*
- 临时密码： 
  - cat /var/log/mysqld.log | grep password

## 设置密码

```
5.7
set global validate_password_policy=0;
set global validate_password_length=1;
ALTER USER 'root'@'localhost' IDENTIFIED BY '123456';
```

SHOW VARIABLES LIKE 'validate_password%'; 可知 8.0版本参数变了

```
8.0
set global validate_password.policy=0;
set global validate_password.length=1;
ALTER USER 'root'@'localhost' IDENTIFIED BY '123456';
```

## 访问权限

```
5.7
grant all privileges on *.* to 'root'@'%' identified by '123456' with grant option;
flush privileges;
```

```
8.0

# MySQL8.0之前的版本密码加密规则：mysql_native_password，
# MySQL8.0密码加密规则：caching_sha2_password
# 若不修改加密规则 则连接报错 caching_sha2_password

# 修改加密规则
ALTER USER 'root'@'localhost' IDENTIFIED BY '捏密码' PASSWORD EXPIRE NEVER;
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '捏密码';
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '新的密码'

## 设置远程访问
use mysql
update user set host ='%' where user = 'root';
flush privileges;
GRANT ALL ON *.* TO 'root'@'%';
flush privileges;
```



## 登录

-  mysql -uroot -p → 输入密码
-  mysql -uroot -p123456    --123456为密码















# 日志

## 默认 /var/log/mysqld.log

