package com.airvcov.util.socketNormal;

import com.airvcov.util.PropertiesRead;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

//接受广播包
/*
 * 接收客户端发送的数据
 */
public class SocketUDPServer implements Runnable {
//    Logger logger = new Logger(SocketTCPServer.class);
//    new PropertiesRead();
    Logger logger = Logger.getLogger(SocketUDPClient.class);
//    Logger

    //ap数据
    public static String[] apData = null;

    public String[] getApData() {
        return apData;
    }

    public void setApData(String[] apData) {
        this.apData = apData;
    }

    //msg  ac地址
//    private String acAdd = "acAdd=";

    //监听端口
//    private int port = 60011;


    @Override
    public void run() {
        BasicConfigurator.configure();

        PropertiesRead read = new PropertiesRead();
        int udpport = read.readInt("udpport");
        String backudpadd = read.readString("backudpadd");
        logger.debug(backudpadd);

        /*
         * 接收客户端发送的数据
         */
        //1.创建服务器端DatagramSocket，指定端口
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(udpport);
        } catch (SocketException e) {
            //弹窗提示
            JOptionPane.showMessageDialog(null,e.getMessage());
            e.printStackTrace();
        }
        //2.创建数据报，用于接收客户端发送的数据
        byte[] data = new byte[1024];//创建字节数组，指定接收的数据包的大小
        DatagramPacket packet = new DatagramPacket(data, data.length);
        //3.接收客户端发送的数据
        System.out.println("****服务器端已经启动，等待客户端发送数据");
        while (true) {
            try {
                socket.receive(packet);//此方法在接收到数据报之前会一直阻塞

                try {
                    Thread.sleep(100);
                    System.out.println("休眠一下");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //4.读取数据
                String info = new String(data, 0, packet.getLength());
                System.out.println("我是服务器，客户端说：" + info);
                boolean contains = info.trim().contains("=");
                if (contains) {//说明是广播包
                    System.out.println("收到广播包:" + info);
                    //把变量保存起来
                    apData = info.trim().split("=");

                } else {
                    System.out.println("没有收到广播包");
                }

                /*
                 * 向客户端响应数据
                 */
                //1.定义客户端的地址、端口号、数据
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                byte[] data2 = backudpadd.getBytes();
                //2.创建数据报，包含响应的数据信息
                DatagramPacket packet2 = new DatagramPacket(data2, data2.length, address, port);
                //3.响应客户端
                socket.send(packet2);
                //4.关闭资源
                // socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
