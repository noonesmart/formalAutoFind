package com.airvcov.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *读取配置文件的工具类
 *
 */
public  class PropertiesRead {
    private static Properties properties = new Properties();
    static {
        try {
            InputStream inStream = new FileInputStream(new File("conf/conf.properties"));
            //1 加载conf.properties配置文件
            properties.load(inStream);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //读取字符串
    public static String readString(String string){
        String str = properties.getProperty(string);
        return str;
    }
    //读取端口
    public static int readInt(String string){
        int port = Integer.parseInt(properties.getProperty(string));

        return port;
    }

    public static void main(String[] args){
        //2 读取配置文件中的属性值
        String address = properties.getProperty("address");
        System.out.println(address);
    }
}
