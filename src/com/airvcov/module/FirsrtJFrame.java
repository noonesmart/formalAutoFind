package com.airvcov.module;

//import com.airvcov.util.ImagePanel;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;
import org.omg.PortableInterceptor.SUCCESSFUL;
import com.airvcov.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * 登陆页面
 */
public class FirsrtJFrame extends JFrame implements MouseListener, FocusListener {
    //全局的位置变量，用于表示鼠标在窗口上的位置
    static Point origin = new Point();

    JTextField acAddress = new JTextField(20);

//    JTextField abc = new JtextField
    //背景图片
    ImagePanel backgroundPanel = null;

    //测试连接和确定连接按钮
    JButton button_test,button_sure;


    public FirsrtJFrame(){
        //窗口淡入淡出
        new WindowOpacity(this);

        Image backgrounImage = null;
        try {
            backgrounImage = ImageIO.read(new File("image/loginbackground.png"));
            Image imgae = ImageIO.read(new File("image/logo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //窗口背景面板
        backgroundPanel = new ImagePanel(backgrounImage);
        backgroundPanel.setLayout(null);


        acAddress.setBounds(250,202,173,30);
        acAddress.setFont(MyFont.Static);
//        acAddress.revalidate();
        acAddress.addFocusListener(this);//焦点监听
        acAddress.setText("请输入AC地址");

        button_test = new JButton("测试连接");
        button_test.setBounds(200,280,140,27);
        button_test.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.lightBlue));
        button_test.setForeground(Color.white);
        button_test.setFont(MyFont.Static);
        button_test.addMouseListener(this);


        button_sure = new JButton("确定连接");
        button_sure.setBounds(350,280,140,27);
        button_sure.setUI(new BEButtonUI().setNormalColor(BEButtonUI.NormalColor.red));
        button_sure.setForeground(Color.white);
        button_sure.setFont(MyFont.Static);
        button_sure.addMouseListener(this);

        backgroundPanel.add(acAddress);
        backgroundPanel.add(button_test);
        backgroundPanel.add(button_sure);

        this.add(backgroundPanel);
        this.setTitle("AP自动发现");
        this.setSize(830,530);
        this.setVisible(true);
        this.requestFocus();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);


    }
    //鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {

        if(e.getSource()==button_test){
            System.out.println("点击测试按钮");


            if("请输入AC地址".equals(acAddress.getText())){
                JOptionPane.showMessageDialog(null,"AC地址不能为空");
            }else {
                //检测连接ac
                String acAddressText = acAddress.getText().trim();
                System.out.println("用户填写的ac地址："+acAddressText);

                try {
                    //正式
                    boolean step = AcUtil.checkAcAddress(acAddressText);
                    //测试
//                    boolean step = AcUtil.testCheckAcAddress(acAddressText);

                    if(step){//true

                        JOptionPane.showMessageDialog(null,"测试连接成功");
                    }else{//false
                        JOptionPane.showMessageDialog(null,"AC地址输入有误");
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }


            }
        }else if(e.getSource()==button_sure){

            System.out.println("点击确定按钮");
            if("请输入AC地址".equals(acAddress.getText())){
                JOptionPane.showMessageDialog(null,"AC地址不能为空");
            }else {
                //检测连接ac
                String acAddressText = acAddress.getText().trim();
                System.out.println("用户填写的ac地址：" + acAddressText);

                try {
                    //正式
                    boolean step = AcUtil.checkAcAddress(acAddressText);
                    //测试
//                    boolean step = AcUtil.testCheckAcAddress(acAddressText);
                    if (step) {//true
                        this.setVisible(false);//关闭当前页面，打开新页面
                        new SecondJFrame();
                    } else {//false
                        JOptionPane.showMessageDialog(null, "AC地址输入有误");
                    }
                } catch (Exception e1) {

                    JOptionPane.showMessageDialog(null, e1.getMessage());
                    e1.printStackTrace();
                }
            }


        }

    }

    //聚焦事件
    @Override
    public void focusGained(FocusEvent e) {
        if(e.getSource()==acAddress){
            if(acAddress.getText().equals("请输入AC地址"));
            System.out.println("here");
            acAddress.setText("");
        }

    }

    //失焦事件
    @Override
    public void focusLost(FocusEvent e) {
        if(e.getSource()==acAddress){
            if(acAddress.getText().equals("")){
                acAddress.setText("请输入AC地址");
            }
        }
    }



    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
