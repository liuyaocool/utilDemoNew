# 公钥私钥免密登录

- 1 客户机执行命令 ssh-keygen -t rsa -P "",  一路回车
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

