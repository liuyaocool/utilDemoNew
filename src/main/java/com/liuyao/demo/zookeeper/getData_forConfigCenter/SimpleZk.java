package com.liuyao.demo.zookeeper.getData_forConfigCenter;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class SimpleZk {


    private ZooKeeper zk;
    private DefaultWatch watch;
    private CountDownLatch latch;

    /**
     * 获得客户端
     * @param addr 连接地址
     * @param rootPath 客户端工作的父目录
     * @param sessionTimeout 客户端断开session保留多少ms
     * @return
     */
    public SimpleZk(String addr, String rootPath, int sessionTimeout) {
        latch = new CountDownLatch(1);
        if (null != rootPath && !rootPath.isEmpty()) addr += rootPath;
        try {
            this.zk = new ZooKeeper(addr, sessionTimeout, (e) -> {
                Watcher.Event.KeeperState state = e.getState();
                Watcher.Event.EventType type = e.getType();
                String path = e.getPath();
                switch (state) {
                    case Unknown:
                        break;
                    case Disconnected:
                        break;
                    case NoSyncConnected:
                        break;
                    case SyncConnected:
                        latch.countDown();
                        break;
                    case AuthFailed:
                        break;
                    case ConnectedReadOnly:
                        break;
                    case SaslAuthenticated:
                        break;
                    case Expired:
                        break;
                }
                switch (type) {
                    case None:
                        break;
                    case NodeCreated:
                        break;
                    case NodeDeleted:
                        break;
                    case NodeDataChanged:
                        break;
                    case NodeChildrenChanged:
                        break;
                }
            });
            latch.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (null == this.zk) throw new RuntimeException("zookeeper connect failed");
        ZooKeeper.States zkState = this.zk.getState();
        switch (zkState){
            case CONNECTING:
                break;
            case ASSOCIATING:
                break;
            case CONNECTED:
                break;
            case CONNECTEDREADONLY:
                break;
            case CLOSED:
                break;
            case AUTH_FAILED:
                break;
            case NOT_CONNECTED:
                break;
        }
    }

    public boolean exist(String path) {
        WatchCallback wcb = new WatchCallback(zk);
        zk.exists(path, wcb, wcb, "ctx");

        return false;
    }


    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }
}
