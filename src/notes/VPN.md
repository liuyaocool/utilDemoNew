- 本机执行命令 建立通道, 使用ssh通道加密
  - ssh -p 27085 root@144.34.187.149 -L 5901:127.0.0.1:5901

# pptp

可在移动设备使用 不安全,  mac已不支持此协议

或可使用ssh通道实现加密传输(见远程桌面-连接) --不成功

- 检查内核是否支持MPPE补丁

  - modprobe ppp-compress-18 &&echo success
  - 不支持安装 yum install kernel-devel ?

- 检查tun/tap支持

  - cat /dev/net/tun
  - 显示 File descriptor in bad state  则为支持
  - 若没显示 则需VPS服务商开通

- 依赖软件安装

  - yum install -y ppp iptables
  - yum install -y pptpd-1.4.0
    - 或
      - cd /usr/local/src
      - wget http://poptop.sourceforge.net/yum/stable/packages/pptpd-1.3.4-2.rhel5.i386.rpm #下载
      - rpm -ivh pptpd-1.3.4-2.rhel5.i386.rpm #安装
    - 或 
      - rpm -ivh http://rpmfind.net/linux/epel/6/x86_64/pptpd-1.4.0-3.el6.x86_64.rpm
      - 这里注意 el6 版本
  - yum install -y perl    --?

- 修改配置文件 注意先备份

  - vi /etc/pptpd.conf 放开以下两行

    - localip 192.168.2.1  \#设置VPN服务器IP地址
    - remoteip 192.168.2.100-192.168.2.199  \#为拨入VPN的用户动态分配192.168.1.100～192.168.1.199之间的IP

  - vi /etc/ppp/options.pptpd 放开以下两行

    - ms-dns 8.8.8.8
    - ms-dns 8.8.4.4
    - option /etc/ppp/options.pptpd    --?

  - vi /etc/ppp/chap-secrets    --设置账号密码

    - ```
      # 用户名 服务	 密码			   IP地址
      lyvpn   pptpd   "LiuYao1994!"   *
      ```

  - vi /etc/sysctl.conf

    - net.ipv4.ip_forward = 1 #开启路由模式

  - /sbin/sysctl -p  #使设置立刻生效

- 添加iptables转发规则

  - ```
    iptables -t nat -A POSTROUTING -s 192.168.2.0/24  -o eth0  -j MASQUERADE
    /etc/init.d/iptables save #保存防火墙设置
    /etc/init.d/iptables restart #重启防火墙
    chkconfig iptables on #设置开机启动
    
    # iptables -t nat -A POSTROUTING -s 192.168.0.0/24 -j SNAT –to-source ***.***.***.***    # ??
    # iptables -t nat -I POSTROUTING -j MASQUERADE  # ??
    ```

- 设置pptp开机启动

  - /sbin/service pptpd start #启动
  - /sbin/service pptpd restart
  - chkconfig pptpd on #设置开机启动

- mac不支持pptp协议 为l2tp协议https://www.cnblogs.com/4a8a08f09d37b73795649038408b5f33/p/12012766.html

- 其他 ??

- ```
  如果连接的时候出现619错误，请执行下面命令，重新建立ppp设备节点：
  
  rm -r /dev/ppp
  
  mknod /dev/ppp c 108 0
  
  -A INPUT -p gre -j ACCEPT #防火墙打开gre协议
  
  \# Generated by iptables-save v1.4.7 on Tue Jun 28 21:06:12 2016
  
  *filter
  
  :INPUT ACCEPT [4:304]
  
  :FORWARD ACCEPT [0:0]
  
  :OUTPUT ACCEPT [4:512]
  
  -A INPUT -p gre -j ACCEPT
  
  -A INPUT -m state --state RELATED,ESTABLISHED -j ACCEPT
  
  -A INPUT -p icmp -j ACCEPT
  
  -A INPUT -i lo -j ACCEPT
  
  -A INPUT -p tcp -m state --state NEW -m tcp --dport 22 -j ACCEPT
  
  -A INPUT -p tcp -m state --state NEW -m tcp --dport 1723 -j ACCEPT
  
  -A INPUT -s 192.168.99.0/255.255.255.0 -j ACCEPT
  
  -A INPUT -j REJECT --reject-with icmp-host-prohibite
  
  -A FORWARD -i ppp+ -p tcp -m tcp --tcp-flags FIN,SYN,RST,ACK SYN -j TCPMSS --set-mss 1356
  
  COMMIT
  
  \# Completed on Tue Jun 28 21:06:12 2016
  
  \# Generated by iptables-save v1.4.7 on Tue Jun 28 21:06:12 2016
  
  *nat
  
  : PREROUTING ACCEPT [2:136]
  
  : POSTROUTING ACCEPT [2:212]
  
  :OUTPUT ACCEPT [2:212]
  
  -A POSTROUTING -s 192.168.99.0/24 -j MASQUERADE
  
  COMMIT
  
  \# Completed on Tue Jun 28 21:06:12 2016
  
  备注：VPN默认使用的端口是1723，由于VPS服务器上默认没有对防火墙端口进行设置，所以全部端口都是开放的。
  
  http://www.jiadingqiang.com/2143.html
  
  linux pptp服务端：
  我们在Linux下建立的pptpd端口号默认是1723，有时候这个端口并不是那么的好用，不是麽?
  所以服务端修改端口号比较简单
  
  修改 /etc/services 文件
  查找 1723,然后将其修改为你想修改的数值,重启 pptpd即可.
  
   
  
  Windows PPTP客户端：
  
  1、找到 C:\WINDOWS\system32\drivers\etc，修改[services](https://www.baidu.com/s?wd=services&tn=24004469_oem_dg&rsv_dl=gh_pl_sl_csd)文件，修改里面的VPN（PPTP）端口1723为你设定的端口
  
  2、系统自带的东西，修改配置参数当然就得进注册表了。进入`HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\Class\{4D36E972-E325-11CE- BFC1-08002bE10318}`项，其中有类似0000、0001、0002……这样的子项，每个子项都对应一个网[适配器](https://www.baidu.com/s?wd=适配器&tn=24004469_oem_dg&rsv_dl=gh_pl_sl_csd)的配置。逐一打开这些子项，找到其中字段`DriverDesc`值为`WAN Miniport (PPTP)`的子项，例如我找到的是`0003`。在这个子项里的`TcpPortNumber`的值就是pptp vpn所使用的端口，双击修改其值，选择基数为十进制，修改成所需要的值确认即可。重启机器后，修改生效。
  
  - L2TP 是点对点隧道协议的扩展，互联网服务提供商使用它来启用互联网上的 VPN。
  - IPSec（互联网安全协议）是一组安全协议。
  - IKEv2 是一种在 IPSec 中设置安全关联的协议。
  ```

# openvpn

安全

默认配置文件位置: /etc/openvpn/*.conf

## 源 ??

- 默认的Centos软件源里面没有OpenVPN的软件包，我们可以添加rpmforge的repo，从而实现yum安装openvpn

  针对CentOS 5:  rpm -ivh http://apt.sw.be/redhat/el5/en/x86_64/rpmforge/RPMS/rpmforge-release-0.5.2-2.el5.rf.x86_64.rpm

  针对CentOS 6:  rpm -ivh http://apt.sw.be/redhat/el6/en/x86_64/rpmforge/RPMS/rpmforge-release-0.5.2-2.el6.rf.x86_64.rpm

## 安装

- ```
  yum install -y openvpn
  ```

## 生成证书

- 下载easy-rsa

  -  https://github.com/OpenVpn/easy-rsa

- 拷贝证书程序: ./eayrsa3 (带有easyrsa执行文件的文件夹)

  - 拷贝到 /etc/openvpn/server    --服务端证书目录

  - 拷贝到 /etc/openvpn/client   --客户端证书目录

  - cp vars.example vars

    - 将两个目录下的vars.example 改名为vars, 然后放开如下

    - vars.example 找不到去doc目录找

    - ```
      #set_var EASYRSA_REQ_COUNTRY    "xxx"
      #set_var EASYRSA_REQ_PROVINCE   "xxx"
      #set_var EASYRSA_REQ_CITY       "xxx"
      #set_var EASYRSA_REQ_ORG        "xxx"
      #set_var EASYRSA_REQ_EMAIL      "xxx@xxx.xxx"
      #set_var EASYRSA_REQ_OU         "xxx"
      ```

    - 

- server 生成证书

  - ./easyrsa init-pki   #建立一个空的pki结构，生成一系列的文件和目录
  - ./easyrsa build-ca   #创建 ca密码 和 cn **需要记住**
  - ./easyrsa gen-req server nopass  #创建服务端证书  common name 最好不要跟前面的cn那么一样
  - ./easyrsa sign server server   #签约服务端证书
  - ./easyrsa gen-dh  #创建Diffie-Hellman 过程较长

- client 生成证书
  - cd /etc/openvpn/client
  - ./easyrsa init-pki
  - ./easyrsa gen-req xxx 
    - xxx 自定义名字
    - 需要创建一个密码  和 cn name，**自己用的 需要记住**
    - ./pki/reqs/xxx.req    ./pki/private/xxx.key  --生成的两个文件
- 客户端签约服务
  - cd /etc/openvpn/server
  - ./easyrsa import-req /etc/openvpn/client/pki/reqs/xxx.req xxx  #导入req
  - ./easyrsa sign client xxx 
    - 用户签约
    - 输入yes
    - 根据提示输入服务端的ca密码
- 最终文件
  - server
    - ./server/pki/ca.crt
    - ./server/pki/private/server.key 
    - ./server/pki/issued/server.crt 
    - ./server/pki/dh.pem
  - client
    - ./server/pki/ca.crt
    - ./server/pki/issued/xxx.crt  
    - ./client/pki/private/xxx.key 

## server.conf

- 编辑/etc/openvpn/server.conf文件，内容如下：

```
port        1194
proto       udp		# 协议 udp tcp

# "dev tun"将会创建一个路由IP隧道，
# "dev tap"将会创建一个以太网隧道。
dev         tun
ca          /etc/openvpn/keys/ca.crt
cert        /etc/openvpn/keys/server.crt
key         /etc/openvpn/keys/server.key
dh          /etc/openvpn/keys/dh1024.pem

# 设置服务器端模式，并提供一个VPN子网，以便于从中为客户端分配IP地址。
# 此示例，服务器端自身将占用10.8.0.1，其他的将提供客户端使用。
# 如果使用的是以太网桥接模式，注释掉该行。
server      10.1.1.0 255.255.255.0
push        "redirect-gateway def1 bypass-dhcp"
push        "dhcp-option DNS 8.8.8.8"
log         /var/log/openvpn.log
keepalive   10 120
verb        3
client-to-client
comp-lzo
persist-key
persist-tun
```

## 启动

- chkconfig openvpn on
- /etc/init.d/openvpn start

## MAC 连接

- 某些mac /dev下没有tun/tap虚拟网络设备 需要安装

- 下载 Tunnelblick

  - https://tunnelblick.net/downloads.html
  - stable 稳定版

- 安装完成 选择 "我没有配置文件"

- 选择 "新建配置样本,并且进行编辑"

- 桌面会生成一个"Tunnelblick VPN 配置样本" 文件夹, 将以下三个文件拷贝进去

  - ca.crt
  - client_mac.crt
  - client_mac.key

- config.ovpn 配置文件

  - ```
    client
    dev tap
    proto tcp
    remote 115.239.210.27 444
    resolv-retry infinite
    nobind
    persist-key
    persist-tun
    ca ca.crt
    cert client_mac.crt
    key client_mac.key
    ns-cert-type server
    comp-lzo
    verb 3
    ```

- "Tunnelblick VPN 配置样本" 文件夹重命名为 "xxx.tblk"

- 双击 "xxx.tblk"

- 选择 "只是我"

# openvpn.conf

```
#################################################
# 针对多客户端的OpenVPN 2.0 的服务器端配置文件示例
#
# 本文件用于多客户端<->单服务器端的OpenVPN服务器端配置
#
# OpenVPN也支持单机<->单机的配置(更多信息请查看网站上的示例页面)
#
# 该配置支持Windows或者Linux/BSD系统。此外，在Windows上，记得将路径加上双引号，
# 并且使用两个反斜杠，例如："C:\\Program Files\\OpenVPN\\config\\foo.key"
#
# '#' or ';'开头的均为注释内容
#################################################

#OpenVPN应该监听本机的哪些IP地址？
#该命令是可选的，如果不设置，则默认监听本机的所有IP地址。
;local a.b.c.d

# OpenVPN应该监听哪个TCP/UDP端口？
# 如果你想在同一台计算机上运行多个OpenVPN实例，你可以使用不同的端口号来区分它们。
# 此外，你需要在防火墙上开放这些端口。
port 1194

#OpenVPN使用TCP还是UDP协议?
;proto tcp
proto udp

# 指定OpenVPN创建的通信隧道类型。
# "dev tun"将会创建一个路由IP隧道，
# "dev tap"将会创建一个以太网隧道。
#
# 如果你是以太网桥接模式，并且提前创建了一个名为"tap0"的与以太网接口进行桥接的虚拟接口，则你可以使用"dev tap0"
#
# 如果你想控制VPN的访问策略，你必须为TUN/TAP接口创建防火墙规则。
#
# 在非Windows系统中，你可以给出明确的单位编号(unit number)，例如"tun0"。
# 在Windows中，你也可以使用"dev-node"。
# 在多数系统中，除非你部分禁用或者完全禁用了TUN/TAP接口的防火墙，否则VPN将不起作用。
;dev tap
dev tun

# 如果你想配置多个隧道，你需要用到网络连接面板中TAP-Win32适配器的名称(例如"MyTap")。
# 在XP SP2或更高版本的系统中，你可能需要有选择地禁用掉针对TAP适配器的防火墙
# 通常情况下，非Windows系统则不需要该指令。
;dev-node MyTap

# 设置SSL/TLS根证书(ca)、证书(cert)和私钥(key)。
# 每个客户端和服务器端都需要它们各自的证书和私钥文件。
# 服务器端和所有的客户端都将使用相同的CA证书文件。
#
# 通过easy-rsa目录下的一系列脚本可以生成所需的证书和私钥。
# 记住，服务器端和每个客户端的证书必须使用唯一的Common Name。
#
# 你也可以使用遵循X509标准的任何密钥管理系统来生成证书和私钥。
# OpenVPN 也支持使用一个PKCS #12格式的密钥文件(详情查看站点手册页面的"pkcs12"指令)
ca ca.crt
cert server.crt
key server.key  # 该文件应该保密

# 指定迪菲·赫尔曼参数。
# 你可以使用如下名称命令生成你的参数：
#   openssl dhparam -out dh1024.pem 1024
# 如果你使用的是2048位密钥，使用2048替换其中的1024。
dh dh1024.pem

# 设置服务器端模式，并提供一个VPN子网，以便于从中为客户端分配IP地址。
# 在此处的示例中，服务器端自身将占用10.8.0.1，其他的将提供客户端使用。
# 如果你使用的是以太网桥接模式，请注释掉该行。更多信息请查看官方手册页面。
server 10.8.0.0 255.255.255.0

# 指定用于记录客户端和虚拟IP地址的关联关系的文件。
# 当重启OpenVPN时，再次连接的客户端将分配到与上一次分配相同的虚拟IP地址
ifconfig-pool-persist ipp.txt

# 该指令仅针对以太网桥接模式。
# 首先，你必须使用操作系统的桥接能力将以太网网卡接口和TAP接口进行桥接。
# 然后，你需要手动设置桥接接口的IP地址、子网掩码；
# 在这里，我们假设为10.8.0.4和255.255.255.0。
# 最后，我们必须指定子网的一个IP范围(例如从10.8.0.50开始，到10.8.0.100结束)，以便于分配给连接的客户端。
# 如果你不是以太网桥接模式，直接注释掉这行指令即可。
;server-bridge 10.8.0.4 255.255.255.0 10.8.0.50 10.8.0.100

# 该指令仅针对使用DHCP代理的以太网桥接模式，
# 此时客户端将请求服务器端的DHCP服务器，从而获得分配给它的IP地址和DNS服务器地址。
#
# 在此之前，你也需要先将以太网网卡接口和TAP接口进行桥接。
# 注意：该指令仅用于OpenVPN客户端，并且该客户端的TAP适配器需要绑定到一个DHCP客户端上。
;server-bridge

# 推送路由信息到客户端，以允许客户端能够连接到服务器背后的其他私有子网。
# (简而言之，就是允许客户端访问VPN服务器自身所在的其他局域网)
# 记住，这些私有子网也要将OpenVPN客户端的地址池(10.8.0.0/255.255.255.0)反馈回OpenVPN服务器。
;push "route 192.168.10.0 255.255.255.0"
;push "route 192.168.20.0 255.255.255.0"

# 为指定的客户端分配指定的IP地址，或者客户端背后也有一个私有子网想要访问VPN，
# 那么你可以针对该客户端的配置文件使用ccd子目录。
# (简而言之，就是允许客户端所在的局域网成员也能够访问VPN)

# 举个例子：假设有个Common Name为"Thelonious"的客户端背后也有一个小型子网想要连接到VPN，该子网为192.168.40.128/255.255.255.248。
# 首先，你需要去掉下面两行指令的注释：
;client-config-dir ccd
;route 192.168.40.128 255.255.255.248
# 然后创建一个文件ccd/Thelonious，该文件的内容为：
#     iroute 192.168.40.128 255.255.255.248
#这样客户端所在的局域网就可以访问VPN了。
# 注意，这个指令只能在你是基于路由、而不是基于桥接的模式下才能生效。
# 比如，你使用了"dev tun"和"server"指令。

# 再举个例子：假设你想给Thelonious分配一个固定的IP地址10.9.0.1。
# 首先，你需要去掉下面两行指令的注释：
;client-config-dir ccd
;route 10.9.0.0 255.255.255.252
# 然后在文件ccd/Thelonious中添加如下指令：
#   ifconfig-push 10.9.0.1 10.9.0.2

# 如果你想要为不同群组的客户端启用不同的防火墙访问策略，你可以使用如下两种方法：
# (1)运行多个OpenVPN守护进程，每个进程对应一个群组，并为每个进程(群组)启用适当的防火墙规则。
# (2) (进阶)创建一个脚本来动态地修改响应于来自不同客户的防火墙规则。
# 关于learn-address脚本的更多信息请参考官方手册页面。
;learn-address ./script

# 如果启用该指令，所有客户端的默认网关都将重定向到VPN，这将导致诸如web浏览器、DNS查询等所有客户端流量都经过VPN。
# (为确保能正常工作，OpenVPN服务器所在计算机可能需要在TUN/TAP接口与以太网之间使用NAT或桥接技术进行连接)
;push "redirect-gateway def1 bypass-dhcp"

# 某些具体的Windows网络设置可以被推送到客户端，例如DNS或WINS服务器地址。
# 下列地址来自opendns.com提供的Public DNS 服务器。
;push "dhcp-option DNS 208.67.222.222"
;push "dhcp-option DNS 208.67.220.220"

# 去掉该指令的注释将允许不同的客户端之间相互"可见"(允许客户端之间互相访问)。
# 默认情况下，客户端只能"看见"服务器。为了确保客户端只能看见服务器，你还可以在服务器端的TUN/TAP接口上设置适当的防火墙规则。
;client-to-client

# 如果多个客户端可能使用相同的证书/私钥文件或Common Name进行连接，那么你可以取消该指令的注释。
# 建议该指令仅用于测试目的。对于生产使用环境而言，每个客户端都应该拥有自己的证书和私钥。
# 如果你没有为每个客户端分别生成Common Name唯一的证书/私钥，你可以取消该行的注释(但不推荐这样做)。
;duplicate-cn

# keepalive指令将导致类似于ping命令的消息被来回发送，以便于服务器端和客户端知道对方何时被关闭。
# 每10秒钟ping一次，如果120秒内都没有收到对方的回复，则表示远程连接已经关闭。
keepalive 10 120

# 出于SSL/TLS之外更多的安全考虑，创建一个"HMAC 防火墙"可以帮助抵御DoS攻击和UDP端口淹没攻击。
# 你可以使用以下命令来生成：
#   openvpn --genkey --secret ta.key
#
# 服务器和每个客户端都需要拥有该密钥的一个拷贝。
# 第二个参数在服务器端应该为'0'，在客户端应该为'1'。
;tls-auth ta.key 0 # 该文件应该保密

# 选择一个密码加密算法。
# 该配置项也必须复制到每个客户端配置文件中。
;cipher BF-CBC        # Blowfish (默认)
;cipher AES-128-CBC   # AES
;cipher DES-EDE3-CBC  # Triple-DES

# 在VPN连接上启用压缩。
# 如果你在此处启用了该指令，那么也应该在每个客户端配置文件中启用它。
comp-lzo

# 允许并发连接的客户端的最大数量
;max-clients 100

# 在完成初始化工作之后，降低OpenVPN守护进程的权限是个不错的主意。
# 该指令仅限于非Windows系统中使用。
;user nobody
;group nobody

# 持久化选项可以尽量避免访问那些在重启之后由于用户权限降低而无法访问的某些资源。
persist-key
persist-tun

# 输出一个简短的状态文件，用于显示当前的连接状态，该文件每分钟都会清空并重写一次。
status openvpn-status.log

# 默认情况下，日志消息将写入syslog(在Windows系统中，如果以服务方式运行，日志消息将写入OpenVPN安装目录的log文件夹中)。
# 你可以使用log或者log-append来改变这种默认情况。
# "log"方式在每次启动时都会清空之前的日志文件。
# "log-append"这是在之前的日志内容后进行追加。
# 你可以使用两种方式之一(但不要同时使用)。
;log         openvpn.log
;log-append  openvpn.log

# 为日志文件设置适当的冗余级别(0~9)。冗余级别越高，输出的信息越详细。
#
# 0 表示静默运行，只记录致命错误。
# 4 表示合理的常规用法。
# 5 和 6 可以帮助调试连接错误。
# 9 表示极度冗余，输出非常详细的日志信息。
verb 3

# 重复信息的沉默度。
# 相同类别的信息只有前20条会输出到日志文件中。
;mute 20
# 当服务器重启时通知客户端
#可以自动重新连接。必须和udp一起启动
# explicit-exit-notify 1
```

- Tunnelblick config.ovpn

```
##############################################
# Sample client-side OpenVPN 2.0 config file #
# for connecting to multi-client server.     #
#                                            #
# This configuration can be used by multiple #
# clients, however each client should have   #
# its own cert and key files.                #
#                                            #
# On Windows, you might want to rename this  #
# file so it has a .ovpn extension           #
##############################################

# Specify that we are a client and that we
# will be pulling certain config file directives
# from the server.
client

# Use the same setting as you are using on
# the server.
# On most systems, the VPN will not function
# unless you partially or fully disable
# the firewall for the TUN/TAP interface.
;dev tap
dev tun

# Windows needs the TAP-Win32 adapter name
# from the Network Connections panel
# if you have more than one.  On XP SP2,
# you may need to disable the firewall
# for the TAP adapter.
;dev-node MyTap

# Are we connecting to a TCP or
# UDP server?  Use the same setting as
# on the server.
;proto tcp
proto udp

# The hostname/IP and port of the server.
# You can have multiple remote entries
# to load balance between the servers.
remote 144.34.187.149 1161
;remote my-server-2 1161

# Choose a random host from the remote
# list for load-balancing.  Otherwise
# try hosts in the order specified.
;remote-random

# Keep trying indefinitely to resolve the
# host name of the OpenVPN server.  Very useful
# on machines which are not permanently connected
# to the internet such as laptops.
resolv-retry infinite

# Most clients don't need to bind to
# a specific local port number.
nobind

# Downgrade privileges after initialization (non-Windows only)
;user nobody
;group nobody

# Try to preserve some state across restarts.
persist-key
persist-tun

# If you are connecting through an
# HTTP proxy to reach the actual OpenVPN
# server, put the proxy server/IP and
# port number here.  See the man page
# if your proxy server requires
# authentication.
;http-proxy-retry # retry on connection failures
;http-proxy [proxy server] [proxy port #]

# Wireless networks often produce a lot
# of duplicate packets.  Set this flag
# to silence duplicate packet warnings.
;mute-replay-warnings

# SSL/TLS parms.
# See the server config file for more
# description.  It's best to use
# a separate .crt/.key file pair
# for each client.  A single ca
# file can be used for all clients.
ca ca.crt
cert lvopenvpnclient.crt
key lvopenvpnclient.key

# Verify server certificate by checking
# that the certicate has the nsCertType
# field set to "server".  This is an
# important precaution to protect against
# a potential attack discussed here:
#  http://openvpn.net/howto.html#mitm
#
# To use this feature, you will need to generate
# your server certificates with the nsCertType
# field set to "server".  The build-key-server
# script in the easy-rsa folder will do this.
ns-cert-type server

# If a tls-auth key is used on the server
# then every client must also have the key.
;tls-auth ta.key 1

# Select a cryptographic cipher.
# If the cipher option is used on the server
# then you must also specify it here.
;cipher x

# Enable compression on the VPN link.
# Don't enable this unless it is also
# enabled in the server config file.
comp-lzo

# Set log file verbosity.
verb 3

# Silence repeating messages
;mute 20
```




