# zookeeperDemo

#### zookeeper节点操作
1. zookeeper节点操作
-  节点创建，删除，遍历
-  节点数据读写
2.  节点类型

> 节点类型

```
持久节点   PERSISTENT
持久顺序节点 PERSISTENT_SEQUENTIAL
临时节点  EPHEMERAL
临时顺序节点    EPHEMERAL_SEQUENTIAL
```
2. Curator zookeeper 实现分布式锁

- 原理
- 临时顺序节点
- 实例代码
- 关于版本问题





#### Curator zookeeper 实现分布式锁

> 原理

```
每个JVM使用同一个客户端实例，客户端实例对某个方法加锁的时候，
zookeeper对应节点下生成一个唯一的临时顺序节点，如果是序号最小的，则获得锁，释放锁的时候删除节点。

怠机的时候临时节点会自动删除，不会有死锁。

```

> 临时顺序节点

如图是调试过程中 在zookeeper中生成用于判断锁的临时顺序节点

![mark](http://ox0rewbep.bkt.clouddn.com/blog/180304/gAdkjge5Al.png?imageslim)

> 关于版本问题

```
Curator 2.x.x兼容Zookeeper的3.4.x和3.5.x。
而Curator 3.x.x只兼容Zookeeper 3.5.x，并且提供了一些诸如动态重新配置、watch删除等新特性。
```

