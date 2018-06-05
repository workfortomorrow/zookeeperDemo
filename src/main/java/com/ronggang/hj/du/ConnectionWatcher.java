package com.ronggang.hj.du;


import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: duhongjiang
 * @Date: Created in 2018/3/4
 */
public class ConnectionWatcher implements Watcher {

    private static final int SESSION_TIMEOUT = 5000;

    public ZooKeeper zk;

    private CountDownLatch connectedSignal = new CountDownLatch(1);

    public void connect(String host) throws IOException {
        try {
            zk=new ZooKeeper(host,SESSION_TIMEOUT,this);
            connectedSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void closeZk() throws InterruptedException {
        zk.close();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if(watchedEvent.getState()==Watcher.Event.KeeperState.SyncConnected)
            connectedSignal.countDown();
    }
}
