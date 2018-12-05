package com.airvcov.main;

import javax.swing.UIManager;

import com.airvcov.module.FirsrtJFrame;
import com.airvcov.util.socketNormal.SocketUDPServer;
import org.apache.log4j.Logger;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import java.io.IOException;

public class Entrance {
    Logger logger =Logger.getLogger(Entrance.class);

    public static void main(String[] args) {
        try {
            // 设置窗口边框样式
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
        } catch (Exception e) {

        }
		System.setProperty("sun.java2d.noddraw", "true"); // 防止激活输入法时白屏
        // 初始化登陆窗口
//		new LoginJFrame();

//        Thread abc = new Thread(new SocketUDPServer());
//        abc.start();
		new FirsrtJFrame();
//		new Thread(new SocketUDPClient()).start();
//		UdpServer.abc();
//		new UdpUtil().run();
//		SocketServerTCP socketServerTCP = new SocketServerTCP();
//		socketServerTCP.service();

//		new ServerSockerTCP();
//		try {
//			new ServerSockerTCP();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    }
}
