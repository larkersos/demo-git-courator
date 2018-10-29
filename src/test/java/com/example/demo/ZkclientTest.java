package com.example.demo;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.Date;

/**
 * <b>描述：</b>
 *
 * @author larkersos
 * @version Created by on 2018/10/29.
 */
public class ZkclientTest {
    private static String zkIp = "116.196.87.204";
    private static String zkPort = "2181";
    private static String rootPath = "/ZookpeerTest";


    public static void main(String[] args) throws Exception{
        // 建立连接
        ZkClient zkClient = new ZkClient(zkIp+":"+zkPort);

        // 创建目录并写入数据
        boolean stat = zkClient.exists("/Zkclient");
        if (stat==false) {
            zkClient.create("/Zkclient", "mydata-" + new Date().getTime(), CreateMode.PERSISTENT);
        }
        String data=zkClient.readData("/Zkclient");
        System.out.println(data);

        //删除目录
        zkClient.delete("/Zkclient");
        stat = zkClient.exists("/Zkclient");
        if (stat==false) {
            data = zkClient.readData("/Zkclient");
            System.out.println(data);
        }else{
            System.out.println("data is null");
        }

        // 递归删除节目录
        zkClient.deleteRecursive("/Zkclient");
        data=zkClient.readData("/Zkclient");
        System.out.println(data);
    }
}