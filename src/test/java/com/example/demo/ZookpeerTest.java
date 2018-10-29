package com.example.demo;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * <b>描述：ZooKeeper自带客户端（原生zookeeper）</b>
 * ZooKeeper自带客户端的主要类是ZooKeeper类,ZooKeeper类对象除了需要ZooKeeper服务端连接字符串(IP地址：端口)，还必须提供一个Watcher对象。Watcher是一个接口，当服务器节点花发生变化就会以事件的形式通知Watcher对象。所以Watcher常用来监听节点，当节点发生变化时客户端就会知道。
 *
 * @author larkersos
 * @version Created by on 2018/10/29.
 */
public class ZookpeerTest {
    private static String zkIp = "116.196.87.204";
    private static String zkPort = "2181";
    private static String rootPath = "/ZookpeerTest";



    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        ZooKeeper zk = new ZooKeeper(zkIp+":"+zkPort, 3000, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent.toString());
            } });

        System.out.println("OK!");

        // 删除整个子目录 -1代表version版本号，-1是删除所有版本
        if (zk.exists(rootPath + "/country/view",true) !=null){
            zk.delete(rootPath + "/country/view", -1);
        }
        if (zk.exists(rootPath + "/country/city",true) !=null){
            zk.delete(rootPath + "/country/city", -1);
        }
        if (zk.exists(rootPath + "/country",true) !=null){
            zk.delete(rootPath + "/country", -1);
        }
        if (zk.exists(rootPath + "",true) !=null){
            System.out.println(new String(zk.getData(rootPath , true,null)));
        }else{
            // 创建一个目录节点
            zk.create( rootPath + "", "ZookpeerTest".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        /**
         * CreateMode:
         *       PERSISTENT (持续的，相对于EPHEMERAL，不会随着client的断开而消失)
         *       PERSISTENT_SEQUENTIAL（持久的且带顺序的）
         *       EPHEMERAL (短暂的，生命周期依赖于client session)
         *       EPHEMERAL_SEQUENTIAL  (短暂的，带顺序的)
         */
        zk.create( rootPath + "/country", "China".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        // 创建一个子目录节点
        zk.create(rootPath + "/country/city", "China/Hangzhou".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(new String(zk.getData(rootPath + "/country", false, null)));

        // 取出子目录节点列表
        System.out.println(zk.getChildren(rootPath + "/country", true));

        // 创建另外一个子目录节点
        zk.create(rootPath + "/country/view", "China/WestLake".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(zk.getChildren(rootPath + "/country", true));

        // 修改子目录节点数据
        zk.setData(rootPath + "/country/city", "China/Shanghai".getBytes(), -1);
        byte[] datas = zk.getData(rootPath + "/country/city", true, null);
        String str = new String(datas, "utf-8");
        System.out.println(str);

        zk.delete(rootPath + "/country/view", -1);
        zk.delete(rootPath + "/country/city", -1);
        zk.delete(rootPath + "/country", -1);
        zk.delete(rootPath + "", -1);

        System.out.println(str);
        Thread.sleep(15000);
        zk.close();
        System.out.println("OK");
    }
}
