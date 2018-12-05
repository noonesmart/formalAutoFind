package com.airvcov.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class AcUtil {



    /**
     * 判断是否为JSON字符串
     * @param test
     * @return
     */
    public final static boolean isJSONValid(String test) {
        try {
            JSONObject.parseObject(test);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否可以转化为json对象
     * @param content
     * @return
     */
    public static boolean isJsonObject(String content) {


        // 此处应该注意，不要使用StringUtils.isEmpty(),因为当content为"  "空格字符串时，JSONObject.parseObject可以解析成功，
        // 实际上，这是没有什么意义的。所以content应该是非空白字符串且不为空，判断是否是JSON数组也是相同的情况。
        if(StringUtils.isBlank(content))
            return false;
        try {
            JSONObject jsonStr = JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }








    //正式
    public static boolean checkAcAddress(String acAddress){


        if(acAddress!=null && !acAddress.equals("")&&!acAddress.equals("请输入AC地址")){
            PropertiesRead read = new PropertiesRead();
            int tcpport = read.readInt("tcpport");
            System.out.println(tcpport+"zheli");

            try {
                //1.建立TCP连接
                Socket sck = new Socket(acAddress,tcpport );
                //2.传输内容
                String content="will connect";
                byte[] bstream=content.getBytes("GBK");  //转化为字节流
                OutputStream os=sck.getOutputStream();   //输出流
                os.write(bstream);

                //3.关闭连接
                sck.close();
                return true;

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }


        return  false;
    }

    //测试用
    public static boolean testCheckAcAddress(String acAddress){
        if(acAddress!=null && !acAddress.equals("")&&!acAddress.equals("请输入AC地址")){
            return true;
        }
        return false;
    }


}
