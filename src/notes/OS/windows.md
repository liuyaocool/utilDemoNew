# 通用

- 注册表: regedit

# XP

## 开启445端口

- 打开注册表
- HKEY_LOCAL_MACHINE\System\CurrentControlSet\Services\NetBT\Parameters
- SMBDeviceEnabled DWORD 键值 改为1
- 重启
- 执行命令 netstat -an 即可看到

## 无法访问文件共享问题

xp 使用SMBv1协议, 而比较新的系统都是SMBv2 SMBv3等高版本, 协议不对应

# DOS

## bat

- 修改编码：chcp 65001 改为utf-8，解决中文乱码

## 文件拖到bat

```
echo %1 --全路径
echo %~n1 --文件名（无后缀）
echo %~x1 --.后缀名
echo %~n1%~x1 --文件名.后缀名
```

