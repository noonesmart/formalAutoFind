package com.airvcov.util.socketNormal;

import com.airvcov.util.AcUtil;
import com.airvcov.util.PropertiesRead;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * TCP socket 线程
 * 1 连接tcp
 * 2 监听tcp
 * 3 保存tcp传过来的数据
 */
public class SocketTCPServer {
    Logger logger = Logger.getLogger(SocketTCPServer.class);


    private static final ThreadLocal<Socket> threadConnect = new ThreadLocal<Socket>();



    //        private static final String HOST = "192.168.23.122";
//    private static final String HOST = "192.168.23.32";
    //    private static final String HOST = "192.168.23.57";
//    private static final int PORT = 9505;
//    private static final int PORT = 60008;
//    private static final int PORT = 9999;
    private static Socket client;

    private static OutputStream outStr = null;

    private static InputStream inStr = null;

    public static Thread tRecv = new Thread(new RecvThread());

    public static Thread tKeep = new Thread(new KeepThread());

    public static JSONObject jsonObject = null;//ac返回的jsonobj

    public static String apmac = null;//apmac  改变页面状态用的

    public static String getUdpApMacStep() {
        return udpApMacStep;
    }

    public static void setUdpApMacStep(String udpApMacStep) {
        SocketTCPServer.udpApMacStep = udpApMacStep;
    }

    public static String udpApMacStep = null;//udp 发消息控制变量

    public static String getApmac() {
        return apmac;
    }

    public static void setApmac(String apmac) {
        SocketTCPServer.apmac = apmac;
    }

    public static void setJsonObject(JSONObject jsonObject) {
        SocketTCPServer.jsonObject = jsonObject;
    }

    public static JSONObject getJsonObject() {
        return jsonObject;
    }

    public static void connect() throws UnknownHostException, IOException {
        BasicConfigurator.configure();//使用log4j缺省环境
        PropertiesRead read = new PropertiesRead();
        String tcpaddress = read.readString("tcpaddress");
        int tcpport = read.readInt("tcpport");
        Logger.getLogger(tcpaddress);
        Logger.getLogger(tcpport+"");
        client = threadConnect.get();
        if (client == null) {
            client = new Socket(tcpaddress, tcpport);
            threadConnect.set(client);
            tKeep.start();
            System.out.println("========链接开始！========");
        }
        outStr = client.getOutputStream();
        inStr = client.getInputStream();
    }

    public static void disconnect() {
        try {
            outStr.close();
            inStr.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //可以不用向tcp发消息

    private static class KeepThread implements Runnable {
        public void run() {
            try {
                System.out.println("=====================开始发送心跳包==============");
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    //下面可以注释掉，不会影响
//                    System.out.println("发送心跳数据包");
//                    outStr.write("send heart beat data package !".getBytes());
//                    outStr.write("abc".getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    //接收数据的线程
    private static class RecvThread implements Runnable {
        public void run() {
            try {
//                synchronized (lock){

                System.out.println("==============开始接收数据===============");
                jsonObject = null;
                while (true) {
                    byte[] b = new byte[1024];
                    int r = inStr.read(b);
                    if (r > -1) {
                        String str = new String(b).trim();
                        //判断是否为json字符串
                        boolean jsonValid = AcUtil.isJsonObject(str);
                        if (jsonValid) {

                            JSONObject parse = (JSONObject) JSONObject.parse(str);

                            if (parse.containsKey("apmac")) {//判断是否存在apmac,返回布尔值
                                System.out.println(parse.getString("apmac"));

                                SocketTCPServer.setApmac(parse.getString("apmac").trim());
                                SocketTCPServer.setUdpApMacStep(parse.getString("apmac").trim());
                            }
                            SocketTCPServer.setJsonObject(parse);
                            String msg = parse.getString("msg");
                            String code = parse.getString("code");
                            System.out.println(msg);
                            System.out.println(code);
                        }
                        System.out.println(str);
                    }
                }
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //启动方式
    public static void main(String[] args) {

        try {
            SocketTCPServer.connect();
            tRecv.start();
            tRecv.sleep(100);
            //取到数据后的操作
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e3) {
            e3.printStackTrace();
        }
    }
}