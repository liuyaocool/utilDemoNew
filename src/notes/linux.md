# 内核

- XX.YY.ZZ --版本 如 2.5.7
  - XX: 主版本号
  - YY: 奇数表示开发板,偶数表示稳定版
  - ZZ: 升级次数

# 公钥私钥免密登录

- 1 客户机执行命令 ssh-keygen -t rsa -P "",  一路回车
  - 会在 ~/.ssh 中生成一对密钥
- 2 客户机执行命令ssh-copy-id 用户名@ip, 输入密码
- 3 ssh免密登录即可

#  常见发行版本

- 红帽体系 RedHat  --学linux常用
  - RHEL-RedHat Enterprise (所谓的RedHat Advance Server, 收费)
  - Fedora Core (由原来RedHat桌面版本发展而来, 免费)
  - CentOS (试验场, REHL(第一条)的社区克隆版本, 一些新的 不稳定的程序都在里边, 免费)
- Debian Linux (唯一 非商业组织维护 功能强大)
- Ubuntu (基于Debian的unstable版本加强而来)    --常用与日常办公 图形化强大
  - kai
  - deepin  --国产
  - 麒麟  --国产
- SuSe Linux (Novell公司产品, 欧洲常用)

# 语言包

yum makcache 出现 Failed to set locale, defaulting to C.UTF-8 错误

- locale -a    --查看安装的语言

  ​	locale: Cannot set LC_CTYPE to default locale: No such file or directory
  ​	locale: Cannot set LC_MESSAGES to default locale: No such file or directory
  ​	locale: Cannot set LC_ALL to default locale: No such file or directory

  ​	。。。

  ​	可知没有安装en_US.UTF-8语言

- 语言包安装

  - dnf install glibc-langpack-en -y
  - 或 dnf install langpacks-en glibc-all-langpacks -y
  - 再查看可知语言安装成功

- 若已安装但还报错

  - echo "export LC_ALL=en_US.UTF-8"  >>  /etc/profile 
  - echo "export LC_CTYPE=en_US.UTF-8"  >>  /etc/profile 
  - source /etc/profile

- 安装中文语言包

  - yum install glibc-langpack-zh

# 配置镜像 yum

yum install 报错 failed to download metadata for repo 'AppStream'

## CentOS

- 1 配置阿里镜像：https://developer.aliyun.com/mirror/
- 2 cd /etc/yum.repos.d
  - 此路径包含很多镜像 
  - 如果用到哪一个，可以vi *.repo，把baseurl放开
- 3 *.repo 说明
  - name=CentOS Stream $releasever - Extras
    mirrorlist=http://mirrorlist.centos.org/?release=$stream&arch=$basearch&repo=extras&infra=$infra
    baseurl=http://mirror.centos.org/$contentdir/$stream/extras/$basearch/os/
    gpgcheck=1
    enabled=1
    gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-centosofficial
- 如何使用tuna的epel镜像
  - 启用Extras源， 见2
  -  Extras源（[tuna](https://mirrors.tuna.tsinghua.edu.cn/help/centos)也有镜像）里安装epel-release：yum install epel-release
  - 其他方式见 https://mirrors.tuna.tsinghua.edu.cn/help/epel/

# 网络

## 无线

- wireless
- 802.11

## 有线

- GbE
- 

# 四大组成

( 程序或服务... 	(系统调用接口...	(内核...	(硬件...))))

- 内核
  - 
- shell
  - shell 提供用户与内核交互的接口
- 文件系统
  - **文件在存储设备上(如磁盘)的组织方式**
  - 支持多种异构文件系统, 如ext2 ext3 fat vfat(fat32) ISO9660 nft ...
    - 上下排列 左右排列
  - 格式化 就是把格式转了一遍, 让看不懂原来的排序
- 应用系统

# shell

可直接调用内核

## 查看支持shell

- cat /etc/shells  --解析器 解析哪些shell
- echo $SHELL  --查看当前正使用

## 与终端的区别

终端输入 → shell → 调用内核

# 常用软件

- make：yum -y install gcc automake autoconf libtool make
- gcc： yun install gcc
- wget：yum install wget    --可下载网络资源

# 常用操作

## 配置环境变量

- vi /etc/profile
- 末尾追加
  - export REDIS_HOME=/opt/soft/redis6
  - export PATH=$PATH:$REDIS_HOME/bin
- source /etc/profile    --使配置生效
- echo $PATH    --查看path

## 编译 安装

- 编译：make all  （ 一般源码根目录下执行）
- 安装：make install prefix=/opt/xxx  （源码根路径下执行）

## 压缩

- 解压：tar xf xxx.tar.gz
- 压缩：tar -zcvf ***.tar.gz /folder

## 查看系统版本

- 系统：
  - redhat系列：cat /etc/redhat-release
- 内核：
  - cat /proc/version
  - uname -r    --显示内核版本
    - -a    --显示主机名 内核版本 硬件平台等

# 正则

真实字符以md文档显示为主

- 匹配操作符

  ```
   \		转义
   .		匹配任意单个字符
   [1236a]	匹配[]设定的任意单个字符
   [^12]		匹配任意非[^]设定单个字符
   [a-k]		匹配任意a-k单个字符
   ^		行首
   $		行尾
   \<,\>		单词首尾便捷
   \<***\>	规定死"***"这个单词
   |		连接操作符
   (,)	选择操作符
   \n		反向引用
  ```

- 重复操作符

  ```
  ？      匹配0-1次
  *       匹配0-多次
  +       匹配1~多次
  {n}     匹配n次
  {n,}    匹配n~多次
  {n,m}   匹配n~m次
  ```

## 例子

```
^ 行开头匹配
cat 6379.conf | grep ^#  # 查看#开头的行
cat 6379.conf | grep ^[^#]  # 查看非#开头的行

```


# 脚本示例

## 示例1

- 自动发布重启.sh 本地机

```sh
#!/bin/sh
# 停止
ssh -p 27085 root@144.34.187.149 "sh /home/**/stop.sh"
# 上传
scp -P 27085 `dirname $0`/target/***.jar root@144.34.187.149:/home/**.jar
# 启动
ssh -p 27085 root@144.34.187.149 "sh /home/&&&/start.sh"
```

- stop.sh 服务器

```sh
a=(`/opt/java/jdk/jdk-11_0_10/bin/jps | grep BrowserInWeb.jar`)
echo kill ${a[0]}
kill ${a[0]}
```

- start.sh 服务器

```sh
a=$(cd `dirname $0`;pwd)
echo start $a/BrowserInWeb.jar
# 注意: 若要第一个脚本启动命令执行成功, java程序必须全路径
# 否则无法得到服务器PATH, 直接java -jar会报错找不到java
/opt/java/jdk/jdk-11_0_10/bin/java -jar $a/BrowserInWeb.jar &
```

