package com.ronggang.hj.du;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;

import java.io.IOException;
import java.util.List;

/**
 * @Author: duhongjiang
 * @Date: Created in 2018/3/4
 * 在永久节点下面创建临时子节点
 */
public class ZookeeperTest extends ConnectionWatcher{


    @Override

    public  void connect(String host) throws IOException {
        super.connect(host);
    }
    @Override
    public void closeZk() throws InterruptedException{
        super.closeZk();
    }
    @Override
    public void process(WatchedEvent watchedEvent) {
        super.process(watchedEvent);
    }

    /**
     * 在给定的节点下创建临时节点
     * @param groupName
     * @param numberName
     * @throws Exception
     */
    public void join(String groupName,String numberName)throws Exception{
        String path="/"+groupName+"/"+numberName;
        String createPath=zk.create(path,null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println(createPath);
    }

    public static void main(String[] args) throws Exception{
        ZookeeperTest zookeeperTest = new ZookeeperTest();
        zookeeperTest.connect("192.168.31.201:2181");
        String fatherNode="testZookeeper";
//        zookeeperTest.createZNode("testZookeeper");

        zookeeperTest.join(fatherNode, "twoPath");
//        Thread.sleep(30000);
        zookeeperTest.listGroup(fatherNode);

        zookeeperTest.deleteNode(fatherNode);
    }

    /**
     * 获取 指定节点下的子节点
     * @param groupName
     * @throws KeeperException
     * @throws InterruptedException
     */
    public  void listGroup(String groupName) throws KeeperException, InterruptedException {
        String patchGroup="/"+groupName;
        List<String>listNode=zk.getChildren(patchGroup,false);
        if(listNode.isEmpty()){
            System.out.print("this is no child Node");
        }else{
            System.out.print(listNode.toString());
        }
    }

    /**
     * 删除指定 节点 以及子节点
     * 不支持递归删除，只能从子节点往上删除
     * @param groupName
     * @throws KeeperException
     * @throws InterruptedException
     */
    public void deleteNode(String groupName) throws KeeperException, InterruptedException {
        String patchGroup="/"+groupName;
        List<String> childrenNodes=zk.getChildren(patchGroup,false);
        for(String node:childrenNodes){
            zk.delete(patchGroup+"/"+node,-1 );
        }
        zk.delete(patchGroup,-1);
        System.out.println("delete OK");
    }

    /**
     * 创建指定永久节点
     * @param zNode
     * @throws Exception
     */
    public void createZNode(String zNode) throws Exception{
        String path="/"+zNode;
        String createPath = zk.create(path,null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(createPath);
    }

}
