# 好用网站

- 数据库排名: https://db-engines.com/en/
- redis-spring: spring官网 找到projects redis属于spring-data 有两个页面小标签 主要是learn Reference Doc.
- proxy: github 搜 twitter/twemproxy readme.md

# 常识

- 磁盘 寻址-ms 带宽-G/M
- 内存 寻址-ns 带宽-很大
      内存比磁盘快10W倍
- i/o buffer：成本问题
      磁盘与磁道
      扇区,一扇区512byte 成本变大:索引
      4k 操作系统,无论读多少,最少4k从磁盘拿
- 数据库：data page-4k

# 安装

## 下载

- https://redis.io/
- 右键复制链接地址 是一个压缩包的资源链接地址
- linux 可以使用 wget程序下载

## 安装

通读readme.md,一般安装方法都在这里

- 解压缩: tar xf file
- 安装编译器  yum install gcc 如果安装过跳过
- 编译: 执行命令 make    -- makefile 文件
  - make distclean    --清除错误的编译文件 再重新make
- 安装: make install PREFIX=/opt/soft/redis6 可执行文件
  - 实际执行 src/Makfile 文件进行安装
- 启动
  - cd src 目录 → 执行 ./redis-server
  - 安装路径/bin/redis-server    --前台启动
  - 安装路径/bin/redis-server /***/6379.conf    --后台启动
- 命令行客户端：./redis-cli
- 停止
  - kill pid    --不推荐
  - ./redis-cli shutdown
  - 或 在命令行客户端 执行shutdown命令

## 做成服务

- 先编译

- 安装路径添加到path: /opt/soft/redis6/bin

- cd utils → 执行 ./install_server.sh  --重复此步骤可安装多个服务 并多启

- 根据提示输入配置信息或直接回车选择默认值

- 会在/etc/init.d文件夹生成 redis_号码(端口号) 启动脚本
      /etc/redis/9001.conf
      /var/log/redis_9001.log
      /var/lib/redis/9001
      /opt/soft/redis6/bin/redis-server
      /opt/soft/redis6/bin/redis-cli

- 错误: This systems seems to use systemd.  

  - ./install_server.sh中注释以下代码

  - ```sh
    #bail if this system is managed by systemd
    #_pid_1_exe="$(readlink -f /proc/1/exe)"
    #if [ "${_pid_1_exe##*/}" = systemd ]
    #then
    #       echo "This systems seems to use systemd."
    #       echo "Please take a look at the provided example service unit files in this directory, and adapt and install them. Sorry!"
    #       exit 1
    #fi
    ```

- 启动服务: service redis_号码 start/stop/status

- 

## 开放端口 

见linux 防火墙

- 问题1:开放端口后外机仍无法访问
  - 原因: lsof -i: 可查看到name 为localhos:redis,为只允许本机访问
  - 解决: vi /etc/redis/0000.conf 中找到bind，改为 0.0.0.0，则lsof中name由localhost:redis→*:redis

# 基本命令

- info    --查看节点信息（集群、）