package com.example.demo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * <b>描述：Apache Curator</b>
 *
 * @author larkersos
 * @version Created by on 2018/10/29.
 */
public class CuratorTest {
    private static String zkIp = "116.196.87.204";
    private static String zkPort = "2181";

    public static void main(String[] args) throws Exception{
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkIp +":"+ zkPort
                , new RetryNTimes(10, 5000));
        client.start();// 连接

        // 获取子节点，顺便监控子节点
        List<String> children = getChildren(client,"/");
        System.out.println(children);


        // 创建节点
        Stat stat = client.checkExists().forPath("/test");
        if (stat==null) {
            String result = client.create().withMode(CreateMode.PERSISTENT)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath("/test", "Data".getBytes());
            System.out.println(result);
        }

        Stat stat2 = client.checkExists().forPath("/test/1");
        if (stat2==null) {
            String result = client.create().withMode(CreateMode.PERSISTENT)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath("/test/1", "Data".getBytes());
            System.out.println(result);
        }
        client.setData().forPath("/test/1", String.valueOf(Math.random()*999).getBytes());

        stat2 = client.checkExists().forPath("/test/2");
        if (stat2==null) {
            String result = client.create().withMode(CreateMode.PERSISTENT)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath("/test/2", "Data".getBytes());
            System.out.println(result);
        }
        stat2 = client.checkExists().forPath("/test/3");
        if (stat2==null) {
            String result = client.create().withMode(CreateMode.PERSISTENT)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath("/test/3", "Data".getBytes());
            System.out.println(result);
        }


        // 设置节点数据
        client.setData().forPath("/test/2", "222".getBytes());
        client.setData().forPath("/test/3", "333".getBytes());

        // 删除节点
        stat2 = client.checkExists().forPath("/test/2");
        if (stat2==null) {
            String result2 = client.create().withMode(CreateMode.PERSISTENT)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath("/test/2", "Data".getBytes());
        }
        System.out.println(client.checkExists().forPath("/test/2"));
        client.delete().withVersion(-1).forPath("/test/2");
        System.out.println(client.checkExists().forPath("/test/2"));
        client.close();

        System.out.println("OK！");


    }

    public static List<String> getChildren(CuratorFramework client,String path) throws Exception {
        // 获取子节点，顺便监控子节点
        List<String> children = client.getChildren().usingWatcher(new CuratorWatcher() {
            public void process(WatchedEvent event) throws Exception {
//                System.out.println("监控： " + event);
            } }).forPath(path);
//        System.out.println(children);
        if (children != null &&  children.size() >0) {
            for (String child : children) {
                if (!path.endsWith("/")){
                    path = path + "/";
                }
                String subPath = path  + child;
                List<String> sunChildren = getChildren(client,subPath);
                if (sunChildren != null && sunChildren.size()>0) {
//                    for (String sunChild : sunChildren) {
//                        String dataStr = new String(client.getData().forPath(subPath +"/"+ sunChild));
//                        System.out.println(subPath +"/"+ sunChild + " = " + dataStr);
//                    }
                }else{
                    String dataStr = new String(client.getData().forPath(subPath));
                    System.out.println(subPath + " = " + dataStr);
                }
            }
        }

        return children;
    }



}
