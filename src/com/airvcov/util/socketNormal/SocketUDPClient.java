package com.airvcov.util.socketNormal;

import java.io.IOException;
import java.net.*;

/**
 * 发送数据给UDP
 */
public class SocketUDPClient {
    private int port= 60010;
    private String apaddress = "192.168.23.122";

    public void sendMsgToUDP(String msg){



        //建立udp的服务
        DatagramSocket datagramSocket = null;
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        //准备数据，把数据封装到数据包中。
        String data = "这个是我第一个udp的例子..";
        //创建了一个数据包
        DatagramPacket packet = null;
        try {
            packet = new DatagramPacket(data.getBytes(), data.getBytes().length, InetAddress.getByName(apaddress) , port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //调用udp的服务发送数据包
        try {
            datagramSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //关闭资源 ---实际上就是释放占用的端口号
        datagramSocket.close();

        //1.定义服务器的地址、端口号、数据
   /*     InetAddress address= null;
        try {
            address = InetAddress.getByName(apaddress);

//        int port=60010;
            DatagramSocket socket=new DatagramSocket(60010);
        byte[] data=msg.getBytes();
        //2.创建数据报，包含发送的数据信息
        DatagramPacket packet=new DatagramPacket(data, data.length, address, port);
        //3.创建DatagramSocket对象
//        DatagramSocket socket= null;
//            socket = new DatagramSocket();
        //4.向服务器端发送数据报
            socket.send(packet);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }*/
    }


//    @Override
//    public void run() {
//        // 创建数据包对象，封装要发送的数据，接收端IP，端口
//        byte[] date = "你好UDP".getBytes();
//        //创建InetAdress对象，封装自己的IP地址
//        InetAddress inet = null;
//        try {
//            inet = InetAddress.getByName(apaddress);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        DatagramPacket dp = new DatagramPacket(date, date.length, inet,port);
//        //创建DatagramSocket对象，数据包的发送和接收对象
//        DatagramSocket ds = null;
//        try {
//            ds = new DatagramSocket();
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//        //调用ds对象的方法send，发送数据包
////        while (true){
//            try {
//                ds.send(dp);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        ds.close();
////        }
//    }
}
