package com.liuyao.demo.zookeeper.getData_forConfigCenter;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;

/**
 * getData exist 通用
 */
public class WatchCallback implements AsyncCallback.StatCallback,
        Watcher, AsyncCallback.DataCallback{

    private ZooKeeper zk;
    private byte[] data;
    private CountDownLatch latch = new CountDownLatch(1);

    public WatchCallback(ZooKeeper zk) {
        this.zk = zk;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    // 因为此类是reactive模型 是异步的 所以需要阻塞 等待数据查询完成
    public void await() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    // exist Callback
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        if (null != stat) {
            zk.getData(path, this, this, "ctx");
        }
    }

    // getData Callback
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
        if (null != data){
            setData(data);
            latch.countDown();
        }
    }

    // getData Watch 只需写getData即可 exist不需要设置监控
    @Override
    public void process(WatchedEvent event) {

        switch (event.getType()) {
            case None:
                break;
            case NodeCreated: //
                zk.getData(event.getPath(),this,this,"sdfs");
                break;
            case NodeDeleted:
                //容忍性
                this.data = null;
                // 数据删除了 需要阻塞
                latch = new CountDownLatch(1);
                break;
            case NodeDataChanged: // 节点变更 获取数据流程重新走一遍 会更新data
                zk.getData(event.getPath(),this,this,"sdfs");
                break;
            case NodeChildrenChanged:
                break;
        }
    }
}
