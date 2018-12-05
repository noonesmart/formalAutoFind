package com.airvcov.module;

import com.airvcov.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

public class SecondJFrame extends JFrame implements MouseListener, ActionListener {
    //获得屏幕的大小
    final static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    final static int height = Toolkit.getDefaultToolkit().getScreenSize().height;


    //定义全局组件
    JPanel backgroundPanel,topPanel,topMenu,topPrompt,centerPanl;
    JTabbedPane jTabbedPane;

    JLabel apList;
    public SecondJFrame(){
        //窗口淡入淡出
        new WindowOpacity(this);

        //设置tab面板缩进
        UIManager.put("TabbedPane.tabAreaInsets",new javax.swing.plaf.InsetsUIResource(0,0,0,0));

        try {
            Image image = ImageIO.read(new File("image/logo.png"));
            this.setIconImage(image);
        }catch (IOException e){
            e.printStackTrace();
        }

        initBackgroundPanel();

        this.setTitle("AP自动发现系统");
        this.setSize((int)(width*0.8f),(int)(height*0.8f));
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    //初始化背景面板
    public void initBackgroundPanel(){
        backgroundPanel = new JPanel(new BorderLayout());
        //预留
        initTop();

        initCenterPanel();

        backgroundPanel.add(topPanel,"North");
        backgroundPanel.add(centerPanl,"Center");

        this.add(backgroundPanel);

    }
    //初始化顶部面板
    public void initTop(){
        initTopMenu();

        topPanel = new JPanel(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(width,40));

        topPanel.add(topMenu,"West");

    }
    //初始化顶部菜单
    public void initTopMenu(){
        topMenu = new JPanel();
        topMenu.setPreferredSize(new Dimension(500,40));
        topMenu.setOpaque(false);

        String[] nameStrings = {"AP自动发现系统","预留2","预留3"};

        apList = CreateMenuLabel(apList,nameStrings[0],"apList",topMenu);
        apList.setName("apList");




    }
    //创建顶部菜单Label
    public JLabel CreateMenuLabel(JLabel jlb,String text,String name,JPanel who){
        JLabel line = new JLabel("<html>&nbsp;<font color='#D2D2D2'>|</font>&nbsp;</html>");
        Icon icon = new ImageIcon("image/"+name+".png");
        jlb = new JLabel(icon);
        jlb.setText("<html><font color='black'>"+text+"</font>&nbsp;</html>");
        jlb.addMouseListener(this);
        jlb.setFont(MyFont.Static);
        who.add(jlb);
        return  jlb;
    }

    //初始化中心面板
    public void initCenterPanel(){
        centerPanl = new JPanel(new BorderLayout());
        apList.setText("<html><font color='#336699' style='font-weight:bold'>" + "AP列表" + "</font>&nbsp;</html>");
        createApList();
        centerPanl.setOpaque(false);
    }



    //创建apList 面板
    public void  createApList(){
        centerPanl.removeAll();
        //设置tab标题位置
        jTabbedPane = new JTabbedPane(jTabbedPane.TOP);
        //设置tab布局
        jTabbedPane.setTabLayoutPolicy(jTabbedPane.WRAP_TAB_LAYOUT);
        jTabbedPane.setFont(MyFont.Static);

        //调用接口查找aplist
//        jTabbedPane.addTab("AP列表管理",new GoodsManagerJPanel().backgroundPanel );
        jTabbedPane.addTab("AP列表管理",new ApListManagerJPanel().backgroundPanel );
        centerPanl.add(jTabbedPane,"Center");

    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    //鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    //鼠标划入事件
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    //鼠标划出事件
    @Override
    public void mouseExited(MouseEvent e) {

    }
}
