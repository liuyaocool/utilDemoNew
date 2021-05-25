# Fusion

## 修改nat模式网段

sudo su
1
修改虚拟网卡编辑器配置文件
备份配置文件

cd /Library/Preferences/VMware\ Fusion/
cp networking networking.bak
1
2
修改配置文件

answer VNET_8_HOSTONLY_SUBNET 10.0.0.0

修改网卡配置文件
cd /Library/Preferences/VMware\ Fusion/vmnet8/
cp nat.conf nat.conf.bak
vim nat.conf
1
2
3
修改# NAT gateway address下ip和netmask参数

# NAT gateway address
ip = 10.0.0.254
netmask = 255.255.255.0