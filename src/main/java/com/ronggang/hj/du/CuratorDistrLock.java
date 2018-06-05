package com.ronggang.hj.du;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author: duhongjiang
 * @Date: Created in 2018/3/4
 */
public class CuratorDistrLock {
    private static final String ZK_ADDRESS = "192.168.31.201:2181";//地址
    private static final String ZK_LOCK_PATH = "/zktest";//path
    static SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void main(String[] args) throws Exception {


//      CuratorFramework client= CuratorFrameworkFactory.newClient(ZK_ADDRESS,new RetryNTimes(10,5000));
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client= CuratorFrameworkFactory.newClient(ZK_ADDRESS,3000, 1000, retryPolicy);
        client.start();

         InterProcessMutex lock =new InterProcessMutex(client,ZK_LOCK_PATH);

        Thread thread1 =new Thread(()->{
            doWithLock(client,lock);
        },"t1");
        Thread thread2 =new Thread(()->{
            doWithLock(client,lock);
        },"t2");
        Thread thread3 =new Thread(()->{
            doWithLock(client,lock);
        },"t3");
        thread1.start();
        thread2.start();
        thread3.start();
    }

    private static void doWithLock(CuratorFramework client, InterProcessMutex lock) {
        Boolean isLock=false;

        System.out.println("进入线程"+Thread.currentThread().getName()+":"+time.format(new Date()));

        try {
            if(lock.acquire(1, TimeUnit.SECONDS)){//1秒的时间等待锁
                isLock=true;
                System.out.println(Thread.currentThread().getName()+"获取锁成功！执行加锁任务"+time.format(new Date()));
                Thread.sleep(2000L);
            }else{
                System.out.println("获取锁超时！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println(Thread.currentThread().getName()+"释放锁！"+time.format(new Date()));
            try {
                if(isLock){
                    lock.release();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


}
