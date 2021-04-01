# 书

高性能mysql 鸟

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

# 关于MySQL编码

使用utf8mb4 代替utf8

原因：utf8只支持每个字符3个字节，二真正的utf-8每个字符最多4个字节

# 使用

## 启动

- service mysqld start
  - 5.7启动报错 
    - 临时密码 证数过期 需要更新
    - 执行 yum update -y
    - 删除 rm -rf /var/lib/mysql/*
- 临时密码： 
  - cat /var/log/mysqld.log | grep password

## 登录

-  mysql -uroot -p → 输入密码
-  mysql -uroot -p123456    --123456为密码

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

## 开放端口

- centos8：firewall-cmd --zone=public --add-port=3306/tcp --permanent

# 架构

![](MYSQL\架构图.png)

1. 连接器
   1. 用户 权限验证
   2. show processlist    查看当前有多少连接
   3. wait_timeout 默认 没动静 8小时后 断开连接
   4. 连接分类
      1. 长连接：推荐，但要周期性断开
      2. 短连接
2. 查询缓存 8.0版本之后移除
   1. 之前执行过的sql及其结果 可能以k，v性质存储再缓存中
   2. sql语句中开启
   3. 不推荐
      1. 表更新，缓存清空
      2. 缓存对应新更新的数据命中率低，两次结果同样的情况效率低
3. 分析器
   1. 词法分析
   2. 语法分析
4. 优化器
   1. 决定用哪个索引
   2. 决定表连接顺序
   3. 执行方式
      1. RBO：基于规则的优化
      2. CBO：基于成本的优化
5. 执行器

# 日志

启动日志 默认 /var/log/mysqld.log

## binlog

mysql server端日志，所有引擎都有，默认关闭 

可做数据恢复

查看： show variables like 'log_bin'; --8.0默认开启

开启：

![](MYSQL\binlog write.png)

## redolog

innodb存储引擎日志

WAL： write ahead log 预写日志 

![](MYSQL\innoDB Engine.png)

![](MYSQL\innoDB write.png)

固定大小，循环写的过程。下图checkpoint--Wtire pos之间是需要写到磁盘的数据

![](MYSQL\innoDB redolog.png)



## undolog 

innoDB引擎日志 回滚日志 保证原子性 MVCC 

# 存储引擎

|              |           MyISAM           |           InnoDB            |
| :----------: | :------------------------: | :-------------------------: |
|   索引类型   | 非聚簇索引（索引数据分离） |  聚簇索引（索引数据合并）   |
|   支持事务   |             ×              |              √              |
|   支持表锁   |             √              |              √              |
|   支持行锁   |             ×              |              √              |
|   支持外键   |             ×              |              √              |
| 支持全文索引 |             √              |          √（5.6+）          |
|   适合操作   |         大量select         | 大量 insert、delete、update |

# 索引

## 基础

- 局部性原理
- 磁盘预读 一般为页（page 多数为4k）的整数倍

## 分类

**不同的存储引擎，数据文件和素引文件存放的位置是不同的，因此有了分类：**

- 聚簇素引：数据和文件放在一起  innodb
  - .frm 存放表结构
  - .ibd 存放数据文件和索引文件
  - 注意：mysq的 innodb存储引擎默认情况下会把所有的数据文件放到表空间中,不会为每一个单独的表保存一份数据文件,如果需要将每一个表单独使用文件保存,设置如下属性
    - set global innodb_file_per_table=on      (show variables like '%per_table%')
- 非局促素引：数据和索引单独一个文件 MiSAM
  - .frm：存放表结构
  - .MYI：存放素引数据
  - .MYD：存放实际数据

## 存储结构 

hash表 → 多叉树 → 二叉树 → AVL树 → 红黑树 → B树 → B+树

在树的结构中，根节点左边数据小与根节点，右边数据大于根节点

磁盘分块4k 数据和索引是在一个一个磁盘块上存储的

1. hash表：

   - 适合场景：等值查询
   - 范围查询浪费时间，数据是无序的，需挨个遍历（企业中大部分查询是范围查询）
   - 使用的时候，全部数据加载到内存，消耗内存空间

2. 二叉树：二分查找。容易一边过长，变成链表，增加磁盘IO次数

   1. AVL树：插入过慢，因为数据量大则会有过多次数树的旋转
   2. 红黑树：AVL树变化出来的，左右最低子树相差2倍之内；牺牲部分查询性能，提高插入性能，

   - 二叉树及其变种都不适合 数的深度无法控制 插入性能低

3. 多叉树：从左到右有序

   1. B树：

      - 枝节点存数据(4k)，则树节点存很少的key，行数据会占用较大的节点空间，树的深度会很大，会加大磁盘的IO次数

   2. B+树

      - 数据只放在叶子节点

      - 叶子节点之间有一个顺序的双向链表

### 引擎区别

1. innoDB    --B+ tree
   1. innoDB是通过B+Tree对主键创建索引，然后叶子节点中存储记录
      - 索引创建优先级：主键 → 唯一键 → 6位row id
   2. 如果创建索引的键为其他字段，则叶子节点中存储该记录的主键，再通过主键索引找到对应的记录，叫做回表
2. myisam
   1. 索引 数据分离 
   2. 索引文件中 叶节点存数据节点地址

## 如何建

- 数据读取流程
  - 如果建立了索引 会先读索引 再读数据 
- 数据量少不适用 因为会读2次IO
- 建议主键自增

## 分类

1. 主键索引
   1. 唯一性索引,但它必须指定为 PRIMARY KEY,每个表只能有一个主键
   2. 自增：自增锁
2. 唯一索引  不需要回表
   1. 索引列的所有值都只能现一次,即必需唯一， 值可以为空。
3. 普通索引  回表
   1. 基本的素引类型，值可以为空，没有唯一性的限制。
   2. 覆盖索引：不需要回表 
      1. 如 id-主键索引 name-普通索引
      2. name B+tree中data存的是id
      3. 所以 select * from tb where name = 'li';
         - 对比 select id from tb where name = 'li';   此sql不需要回表 查询效率更快
   3. 索引下推：回表之前数据进行筛选，删除
4. 全文索引 myisam支持 innoDB5.6之后支持
   1. ES
   2. 全文索引的索引类型为 FULLTEXT.全文索引可以在 varchar、char、text类型的列上创建
5. 组合索引 
   1. 多列值组成一个索引,有用于组合搜素(最左匹配原则)
   2. (age，name) ：可同时支持单独搜索age 和 组合搜索name+age
   3. 例 需求 name、age、name+age
      - 建两个索引 (name,age), age 。原因 age 空间小， 所以单独的索引使用age不用name

谓词下推？

## 维护

索引在插入新的值的时候,为了维护索引的有序性,必须要维护,在维护索引的时候需要需要分以下集中情况：

1. 如果插入一个比较大的值,直接插入即可,几乎没有成本
2. 如果插入的是中间的某一个值,需要逻辑上移动后续的元素,空出位置
3. 如果需要插入的数据页满了,就需要单独申请一个新的数据页,然后移动部分数据过去,叫分裂,此时性能会受影响同时空间的使用率也会降低,除了页分裂之外还包含页合并

**尽量使用自增主键作为索引**

# 执行计划

语法：explain sql;  https://dev.mysql.com/doc/refman/8.0/en/explain-output.html

- id：越大越先执行
- select_type：
- table
- partitions
- type：
  - 效率排序 system> const > ea_ref > ref > fulltext > ref_or_null > index_merge > unique_subquery > index_ subquery > range > index > ALL
  - 一般 至少达到range级别 最好到ref
- possible_keys：可能使用的索引 但实际不一定用到
- key：实际用到的索引
- key_len：索引的 长度
- ref：
- rows：估算sql查出的行数 
- filtered：
- Extra：

# 事务

## 自动提交

- 查看自动提交：select @@autocommit;  1：开启  	0：关闭
- 关闭自动提交
  - 当前会话：set autocommit=0;   
  - 全局：set global autocommit=0;   

## 隔离级别

- 设置隔离级别语句：
  - set session transaction isolation level read committed;
  - set global transaction isolation level read committed;  -- 一般使用这句

级别越高 数据越安全

不同隔离级别产生的问题

| 隔离级别 | 脏读 | 不可重复度 | 幻读 | 设置             |
| -------- | ---- | ---------- | ---- | ---------------- |
| 读未提交 | √    | √          | √    | read uncommitted |
| 读已提交 | ×    | √          | √    | reaad commited   |
| 可重复读 | ×    | ×          | √    | repeatable read  |
| 序列化   | ×    | ×          | ×    | Serializable     |

- **脏读**：修改的数据未提交就被读出来了
- **不可重复读**：同一事务 两次查询（之间有另一个事务提交了数据）结果不一致
- **幻读**：插入的数据未提交就被读出来了

## 传播特性



# 锁

**协调多进程或多线程并发访问某一资源的机制**



不同存储引擎支持不同的锁机制。

- 表锁：大量查询适合用，不会死锁。myisam、memory、innoDB支持。
- 行锁：会死锁。innoDB支持。

- OLTP：在线事务，实时要求高
- OLAP：历史分析，未来决策产生影响。

## MyISAM表锁

```sql
-- 创建表时需要指定引擎
CREATE TABLE 'mylock'(***) ENGINE=MyISAM DEFAULT CHARSET=utf8
```

读阻塞写，不阻塞读

写阻塞读

实际操作不需要显示枷锁，MyISAM会自动加锁。

加锁 lock table 表名 read locak; 则read lock后，加上local字段，其他线程可以插入，但更新会阻塞。此时 当前session concurrent_insert=AUTO。

 可以通过检查**table_locks_waited**和**table_locks_immediate**状态变量来分析系统上的表锁定争夺： 

```sql
show status like 'table_locks%';
```



## innoDB行锁

可以通过检查InnoDB_row_lock状态变量来分析系统上的行锁的争夺情况： 

```sql
show status like 'innodb_row_lock%';
```

- 排他锁
  - select ... for update
- 共享锁
  - select ... lock in  share mode
- 行锁必须通过**索引条件**检索数据，否则还是使用表锁，因为行锁是通过给**索引值加锁**来实现的

# 集群

## 延时问题

5.7+，MySQL MST并行复制技术，永久解决复制延时问题。

## 一主一从 复制

1. 主服务器配置

```sh
#修改配置文件，执行以下命令打开mysql配置文件
vi /etc/my.cnf
#在mysqld模块中添加如下配置信息
log-bin=master-bin #二进制文件名称
binlog-format=ROW  #二进制日志格式，有row、statement、mixed三种格式，row指的是把改变的内容复制过去，而不是把命令在从服务器上执行一遍，statement指的是在主服务器上执行的SQL语句，在从服务器上执行同样的语句。MySQL默认采用基于语句的复制，效率比较高。mixed指的是默认采用基于语句的复制，一旦发现基于语句的无法精确的复制时，就会采用基于行的复制。
server-id=1		   #要求各个服务器的id必须不一样
binlog-do-db=msb   #同步的数据库名称
```

```sql
-- 登录mysql 

-- 授权
-- 5.7
set global validate_password_policy=0;
set global validate_password_length=1;
grant replication slave on *.* to 'root'@'%' identified by '123456';
-- 8.0
set global validate_password.policy=0;
set global validate_password.length=1;
grant replication slave on *.* to 'root'@'%'; 

-- 刷新权限
flush privileges;

-- 重启mysql
service mysqld restart
```

2. 从服务器配置

```shell
#修改配置文件，执行以下命令打开mysql配置文件
vi /etc/my.cnf
#在mysqld模块中添加如下配置信息
log-bin=master-bin	#二进制文件的名称
binlog-format=ROW	#二进制文件的格式
server-id=2			#服务器的id

## 重启mysql
service mysqld restart
```

```sql
-- 登录主服务器 --

-- 主服务器执行命令 查看一些参数
show master status;

-- 登录从服务器 --

-- 从服务器执行命令
-- 连接主服务器 相关参数为主服务器show master status命令的结果
-- 156表示初始化占用的字节数 要从这个位置开始读
change master to master_host='192.168.1.161',master_user='root',master_password='123456',master_port=3306,master_log_file='master-bin.000001',master_log_pos=156;
-- 启动slave
start slave
-- 查看slave的状态 (注意没有分号) G标识按照某些格式输出
show slave status\G
    -- Slave_IO_Running: Yes -- 只有这两个都是Yes才表示主从复制成功
    -- Slave_SQL_Running: Yes

```

## 读写分离

### Mysql proxy

不推荐使用

1. 配置过程

   ```
   #1、下载mysql-proxy
   https://downloads.mysql.com/archives/proxy/#downloads
   #2、上传软件到proxy的机器
   直接通过xftp进行上传
   #3、解压安装包
   tar -zxvf mysql-proxy-0.8.5-linux-glibc2.3-x86-64bit.tar.gz
   #4、修改解压后的目录
   mv mysql-proxy-0.8.5-linux-glibc2.3-x86-64bit mysql-proxy
   #5、进入mysql-proxy的目录
   cd mysql-proxy
   #6、创建目录
   mkdir conf
   mkdir logs
   #7、添加环境变量
   #打开/etc/profile文件
   vi /etc/profile
   #在文件的最后面添加一下命令
   export PATH=$PATH:/root/mysql-proxy/bin
   #8、执行命令让环境变量生效
   source /etc/profile
   #9、进入conf目录，创建文件并添加一下内容
   vi mysql-proxy.conf
   添加内容
   [mysql-proxy]
   user=root
   proxy-address=192.168.85.14:4040
   proxy-backend-addresses=192.168.85.11:3306
   proxy-read-only-backend-addresses=192.168.85.12:3306
   proxy-lua-script=/root/mysql-proxy/share/doc/mysql-proxy/rw-splitting.lua
   log-file=/root/mysql-proxy/logs/mysql-proxy.log
   log-level=debug
   daemon=true
   #10、开启mysql-proxy
   mysql-proxy --defaults-file=/root/mysql-proxy/conf/mysql-proxy.conf
   #11、查看是否安装成功，打开日志文件
   cd /root/mysql-proxy/logs
   tail -100 mysql-proxy.log
   #内容如下：表示安装成功
   2019-10-11 21:49:41: (debug) max open file-descriptors = 1024
   2019-10-11 21:49:41: (message) proxy listening on port 192.168.85.14:4040
   2019-10-11 21:49:41: (message) added read/write backend: 192.168.85.11:3306
   2019-10-11 21:49:41: (message) added read-only backend: 192.168.85.12:3306
   2019-10-11 21:49:41: (debug) now running as user: root (0/0)
   ```

2. 进行连接

   ```
   #mysql的命令行会出现无法连接的情况，所以建议使用客户端
   mysql -uroot -p123 -h192.168.85.14 -P 4040
   ```

3. 查看

   ```
   show databases;
   ```

### amoeba

阿里开源框架

### mycat 

生产中使用



# 进度

基础完成

优化 

0.0 

00:00:00   



