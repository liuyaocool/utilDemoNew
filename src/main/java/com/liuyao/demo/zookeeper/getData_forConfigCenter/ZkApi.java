package com.liuyao.demo.zookeeper.getData_forConfigCenter;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZkApi {

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        CountDownLatch latch = new CountDownLatch(1);
        // watch 是session级别
        SimpleZk zk = new SimpleZk(
                "192.168.1.161:2181,192.168.1.162:2181,192.168.1.163:2181",
                null, 3000);

        String pathName = zk.getZk().create("/n01/aaa", "old".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(pathName);

        // 元数据
        Stat stat = new Stat();
        // 同步
        byte[] data = zk.getZk().getData(pathName, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("getdata watch sync, line 28");
                System.out.println(event.getPath());
                // 若想继续watch 需要再次注册
                try {
                    // 注册default watch，是new ZooKeeper时的watch
//                    zk.getZk().getData(event.getPath(), true, stat);
                    zk.getZk().getData(event.getPath(), this, stat);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, stat);
        System.out.println(new String(data));
        // 出发watch 事件
        Stat stat1 = zk.getZk().setData(pathName, "new".getBytes(), 0);
        // 不会触发watch 因为是一次性的
        Stat stat2 = zk.getZk().setData(pathName, "new01".getBytes(), stat1.getVersion());

        // 异步 watch是一次性的
        zk.getZk().getData(pathName, new Watcher() {
            @Override
            public void process(WatchedEvent event) {

            }
        }, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                System.out.println("callback: " + new String(data));
            }
        }, "ctx");


        Thread.sleep(Long.MAX_VALUE);
    }
}
