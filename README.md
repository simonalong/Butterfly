# Butterfly 简介

Butterfly（蝴蝶）是一个超高性能的发号器框架。框架通过引入多种新的方案不仅解决了雪花算法存在的所有问题，而且还能够提供比雪花算法更高的性能。在单机版QPS理论值为51.2(w/s)这种情况下，新的方案在一些机器上可达 1200(w/s) 甚至更高。
起名Butterfly是用世界上没有完全相同的蝴蝶翅膀来表示该算法的唯一性。
<br /><br />
雪花算法是twitter提出的分布式id生成器方案，但是有三个问题，其中前两个问题在业内很常见：

- 时间回拨问题
- 机器id的分配和回收问题
- 机器id的上限问题

其中业内针对前两个问题都有个自己的解决方式，但是都不是很完美，或者说没有完全解决。我们这里从新的思路出发，通过改造雪花算法以及其他相关方式彻底解决了以上的三个问题。<br />该方案算是对雪花算法比较完美的一种实现方式。方案请见[方案介绍](https://www.yuque.com/simonalong/butterfly)

## 新的方案
对于以上三个问题，我们这里简述下我们的方案。
1. 时间回拨问题<br/>
这里采用新的方案：大概思路是：启动时间戳采用的是“历史时间”，每次请求只增序列值，序列值增满，然后“历史之间”增1，序列值重新计算。
2. 机器id分配和回收<br/>
这里机器id分配和回收具体有两种方案：zookeeper和db。理论上分配方案zk是通过哈希和扩容机器，而db是通过查找机制。回收方案，zk采用的是永久节点，节点中存储下次过期时间，客户端定时上报（设置心跳），db是添加过期时间字段，查找时候判断过期字段。
3. 机器id上限<br/>
这个采用将改造版雪花+zookeeper分配id方案作为服务端的节点，客户端采用双Buffer+异步获取提高性能，服务端采用每次请求时间戳增1。

## 框架指标
全局唯一：最基本的要求，目前是对一个业务而言是唯一性<br/>
超高性能：纯内存化操作，性能特别高。采用时间预留，解决时钟回拨问题，同时可以使得qps达到更高，理论上最高可到 51.2w/s ~ more（不同的机器上限不同，自己的笔记本可达 1200(w/s)）<br/>
趋势递增：整体递增，对用Mysql这种用b+树作为索引的结构可以提高性能<br/>
信息安全：自增位放在高位，id不是完全连续的，防止外部恶意的数据爬取<br/>
易用性：开发接入非常简单<br/>

## 快速入门
对于使用根据机器id的分配方式不同，这里有三种方式：
- （单机版）zookeeper分配workerId
- （单机版）db分配workerId
- （分布式版）distribute分配workerId

目前还未发布到maven中央仓库，还在测试阶段，如果要使用，请自行打包到私人仓库
### zookeeper分配workerId
```xml
<dependency>
  <groupId>com.github.simonalong</groupId>
  <artifactId>butterfly-zookeeper-allocator</artifactId>
  <!--替换为具体版本号-->
  <version>${last.version.release}</version>
</dependency>
```
#### 使用示例
```java
@Test
public void test(){
    ZkButterflyConfig config = new ZkButterflyConfig();
    config.setHost("localhost:2181");

    ButterflyIdGenerator generator = ButterflyIdGenerator.getInstance(config);
    // 设置起始时间，如果不设置，则默认从2020年2月22日开始
    generator.setStartTime(2020, 5, 1, 0, 0, 0);
            
    // 添加业务空间，如果业务空间不存在，则会注册
    generator.addNamespaces("test1", "test2");
    Long uuid = generator.getUUid("test1");
    System.out.println(uuid);
}
```
### db分配workerId
```xml
<dependency>
  <groupId>com.github.simonalong</groupId>
  <artifactId>butterfly-allocator-db</artifactId>
  <!--替换为具体版本号-->
  <version>${last.version.release}</version>
</dependency>
```
#### 使用示例
在对应的公共库中创建表
```sql
CREATE TABLE `butterfly_uuid_generator` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `namespace` varchar(128) DEFAULT '' COMMENT '命名空间',
  `work_id` int(16) COMMENT '工作id',
  `last_expire_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下次失效时间',
  `uid` varchar(128) DEFAULT '0' COMMENT '本次启动唯一id',
  `ip` varchar(20) NOT NULL DEFAULT '0' COMMENT 'ip',
  `process_id` varchar(128) NOT NULL DEFAULT '0' COMMENT '进程id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name_work` (`namespace`,`work_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发号器表';
```
编写db方式的测试
```java
@Test
public void test(){
    DbButterflyConfig config = new DbButterflyConfig();
    config.setUrl("jdbc:mysql://127.0.0.1:3306/neo?useUnicode=true&characterEncoding=UTF-8&useSSL=false&&allowPublicKeyRetrieval=true");
    config.setUserName("neo_test");
    config.setPassword("neo@Test123");

    ButterflyIdGenerator generator = ButterflyIdGenerator.getInstance(config);
    // 设置起始时间，如果不设置，则默认从2020年2月22日开始
    generator.setStartTime(2020, 5, 1, 0, 0, 0);
            
    // 添加业务空间，如果业务空间不存在，则会注册
    generator.addNamespaces("test1", "test2");
    Long uuid = generator.getUUid("test1");
    System.out.println(uuid);
}
```

### 分布式模式
分布式模式客户端一个，但是对应的服务端（代码很简单可以也自己写服务端）这边有两个：
- dubbo方式的：服务端采用的是butterfly-allocator-zk方式获取workerId和time字段，然后将对应字段给客户端
- restful方式的：服务端采用的是butterfly-allocator-db方式获取workerId和time字段，然后将对应字段给客户端
```xml
<dependency>
  <groupId>com.github.simonalong</groupId>
  <artifactId>butterfly-allocator-distribute</artifactId>
  <!--替换为具体版本号-->
  <version>${last.version.release}</version>
</dependency>
```
#### 使用示例 - dubbo方式获取
首先启动服务端butterfly-server模块，然后客户端这边使用如下即可
```java
@Test
public void test(){
    DistributeDubboButterflyConfig config = new DistributeDubboButterflyConfig();
    config.setZkHoseAndPort("localhost:2181");

    ButterflyIdGenerator generator = ButterflyIdGenerator.getInstance(config);
    // 设置起始时间，如果不设置，则默认从2020年2月22日开始
    generator.setStartTime(2020, 5, 1, 0, 0, 0);
            
    // 添加业务空间，如果业务空间不存在，则会注册
    generator.addNamespaces("test1", "test2");
    Long uuid = generator.getUUid("test1");
    System.out.println(uuid);
}
```

#### 使用示例 - restful方式获取
首先启动服务端butterfly-server模块，然后客户端这边使用如下即可
```java
@Test
public void test(){
    DistributeRestfulButterflyConfig config = new DistributeRestfulButterflyConfig();
    config.setHostAndPort("localhost:8800");

    ButterflyIdGenerator generator = ButterflyIdGenerator.getInstance(config);
    // 设置起始时间，如果不设置，则默认从2020年2月22日开始
    generator.setStartTime(2020, 5, 1, 0, 0, 0);
            
    // 添加业务空间，如果业务空间不存在，则会注册
    generator.addNamespaces("test1", "test2");
    Long uuid = generator.getUUid("test1");
    System.out.println(uuid);
}
```

## 更多内容
对于详细内容介绍，请见文档[Butterfly说明文档](https://www.yuque.com/simonalong/butterfly)
