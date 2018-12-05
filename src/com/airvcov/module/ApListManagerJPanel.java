package com.airvcov.module;

import com.airvcov.util.*;
import com.airvcov.util.socketNormal.SocketTCPServer;
import com.airvcov.util.socketNormal.SocketUDPClient;
import com.airvcov.util.socketNormal.SocketUDPServer;
import com.alibaba.fastjson.JSONObject;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.plaf.PanelUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

public class ApListManagerJPanel implements ActionListener, MouseListener {


    //定义全局组件
    JPanel backgroundPanel, topPanel, toolPanel, searchPanel, tablePanel;
    //    DefaultTableModel model ;
    JTable jTable1;
    BaseTableModule baseTableModule;
    JTable table;
    JLabel jLabel;
    JScrollPane jScrollPane;
    JButton tool_refresh, tool_sendconfig;
    Vector vData ;
    Vector vName;

    DefaultTableModel model; // 新建一个默认数据模型

    //构造器
    public ApListManagerJPanel() {
        backgroundPanel = new JPanel(new BorderLayout());
        initTopPanel();
//        initTablePanel();
//        initTestTable();


        initApListThread();
    }

    //初始化顶部面板
    public void initTopPanel() {
        topPanel = new JPanel(new BorderLayout());
        initToolPanel();
        backgroundPanel.add(topPanel, "North");

    }

    /**
     * 初始化ap列表
     * 1 tcp连接线程开启
     * 2 tcp监听线程开启
     * 3 udp监听线程开启
     * 4 udp反馈信息线程开启
     */
    public void initApListThread() {
        //1 tcp连接线程开启
        try {
            SocketTCPServer tcpServer = new SocketTCPServer();
            tcpServer.connect();
            tcpServer.tRecv.start();
            try {
                tcpServer.tRecv.sleep(100);//休眠100ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            //弹窗提示
            JOptionPane.showMessageDialog(null,e.getMessage());
            e.printStackTrace();
        }

        //2 udp监听线程开启
        try {
            SocketUDPServer udpServer = new SocketUDPServer();
            Thread udpThread = new Thread(udpServer);
            udpThread.start();
        } catch (Exception e) {
            //弹窗提示
            JOptionPane.showMessageDialog(null,e.getMessage());
            e.printStackTrace();
        }


        vData = new Vector();
        vName = new Vector();
        vName.add("IP地址");
        vName.add("mac地址");
        vName.add("状态");

        model = new DefaultTableModel(null, vName);
        jTable1 = new JTable();
        jTable1.setModel(model);

        JScrollPane scroll = new JScrollPane(jTable1);
        scroll.setSize(300, 200);
        scroll.setLocation(650, 300);
        backgroundPanel.add(scroll, "Center");


    }


    //初始化工具面板 刷新，寻找ap
    public void initToolPanel() {
        toolPanel = new JPanel();

        tool_refresh = new JButton();
        tool_refresh.setFont(MyFont.Static);
        tool_refresh.setText("刷新列表");
        tool_refresh.addMouseListener(this);

        tool_sendconfig = new JButton();
        tool_sendconfig.setFont(MyFont.Static);
        tool_sendconfig.setText("点击监测");
        //绑定事件
        tool_sendconfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == tool_sendconfig){
                    tool_sendconfig.setEnabled(false);
                    tool_sendconfig.setText("监测中");
                    tool_sendconfig.setBackground(Color.red);
                }
                searchAp(e);
            }
        });

        toolPanel.add(tool_refresh);
        toolPanel.add(tool_sendconfig);


        topPanel.add(toolPanel, "East");
    }

    //寻找AP 触发事件
    //有bug

    private void searchAp(ActionEvent evt) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    //从共享内存中找到变量
                    //检测AP
                    String[] apData = SocketUDPServer.apData;
                    if (apData != null && apData.length != 0 && apData.length == 2) {
                        boolean step = false;//校验重复的变量
                        //需要去重
                        if (vData.size() == 0) {
                            System.out.println("ap数据初始化 =：" + apData.toString());
                            Vector vRow1 = new Vector();
//                            vRow1.setSize(14);
                            vRow1.add(0, apData[0].trim());
                            vRow1.add(1, apData[1].trim());
                            vRow1.add(2, "<html><font color='green'>未上线</font></html>");
                            vData.add(vRow1);

                            model = new DefaultTableModel(vData, vName);
                            jTable1.setModel(model);
                        } else {
                            System.out.println("已存在ap数据 =：" + apData.toString());
                            for (int i = 0; i < vData.size(); i++) {
                                Vector vo = (Vector) vData.get(i);
                                String str = ((String) vo.get(1)).trim();//需要校验的字段
                                if (str.equals(apData[1].trim())) {
                                    step = true;
                                    break;
                                }
                            }

                            if (!step) {

                                Vector vRow1 = new Vector();
                                vRow1.add(0, apData[0].trim());
                                vRow1.add(1, apData[1].trim());
                                vRow1.add(2, "<html><font color='green'><b>未上线</b></font></html>");
                                vData.add(vRow1);
                                model = new DefaultTableModel(vData, vName);
                                jTable1.setModel(model);
                            }


                        }

                    }


                    //重置为null
                    SocketUDPServer.apData = null;

                    String apmac = SocketTCPServer.getApmac();
                    if (apmac != null) {
                        //1 改变页面状态
                        changeApStatus(apmac);
                        //重置为null
                        SocketTCPServer.setApmac(null);
                    }


                }
            }
        }).start();
    }

    //传入mac地址 改变AP状态
    public void changeApStatus(String apmac) {
        //需要遍历 vdata 查找
        System.out.println("enter here");
        for (int i = 0; i < vData.size(); i++) {
            System.out.println("vdata 的size=" + vData.size());
            Vector vo = (Vector) vData.get(i);
//            System.out.println("子项为size="+vo.size()+"分别为"+vo.get(0)+vo.get(1)+vo.get(2));
            String str = (String) vo.get(1);//mac地址
            if (str.trim().equals(apmac.trim())) {
                System.out.println("find");
                vo.set(2, "<html><font color='red'><b>已注册</b></font></html>");
                model = new DefaultTableModel(vData, vName);
                jTable1.setModel(model);
                break;
            }
        }
    }

    //鼠标点击事件
    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource() == tool_refresh) {
            System.out.println("enter here");
            vData = new Vector();//初始化 默认一下
            model = new DefaultTableModel(null, vName);
            jTable1.setModel(model);
//            tool_sendconfig.setEnabled(true);
//            tool_sendconfig.setBackground(Color.white);
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


    /***********************************以下方法不使用************************************/

    public void initTestTable() {
        //监听TCP的线程
        try {
            SocketTCPServer socketTCPServer = new SocketTCPServer();
            SocketTCPServer.connect();
            SocketTCPServer.tRecv.start();
            try {
                socketTCPServer.tRecv.sleep(100);//延迟100，不然返回为null
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = socketTCPServer.getJsonObject();
            String apmac = socketTCPServer.getApmac();
            System.out.println("");
            if (apmac != null) {
                //渲染该mac地址的行变色
                changgeApColor(apmac);
                System.out.println("apmac 为空");
                //给AP下发配置

            }

            System.out.println(jsonObject + "here");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        vData = new Vector();
        vName = new Vector();
        vName.add("IP地址");
        vName.add("mac地址");
        vName.add("状态");
        Vector vRow = new Vector();
        vRow.add("cell 0 0");
        vRow.add("cell 0 1");
        vRow.add("未上线");
        //        vRow.
        vData.add(vRow.clone());
        vData.add(vRow.clone());
        model = new DefaultTableModel(vData, vName);
        jTable1 = new JTable();
        jTable1.setModel(model);

        JScrollPane scroll = new JScrollPane(jTable1);
        scroll.setSize(300, 200);
        scroll.setLocation(650, 300);
        backgroundPanel.add(scroll, "Center");
    }


    //传入mac地址 改变面板上的数据颜色

    public void changgeApColor(String apmac) {

        Vector vRow1 = new Vector();
        vRow1.add("cell 2 0");
        vRow1.add("cell 2 1");
        vData.add(vRow1);
        model = new DefaultTableModel(vData, vName);
        jTable1.setModel(model);
//
//        Vector<Vector> vector = new Vector<Vector>();
//        Vector step = new Vector();
//        step.add("ma22ck");
//        step.add("11.11.11");
//        vector.add(step);
//        Vector step2 = new Vector();
//        step2.add("ma33ck2");
//        step2.add("11.11.11.22");
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        String columnName = baseTableModule.getColumnName(1);
//        int rowCount = baseTableModule.getRowCount();
//
//
//        baseTableModule.setValueAt("123234324",0,0);
//        table = new JTable(baseTableModule);
//        jScrollPane = new JScrollPane(table);
//        tablePanel = new JPanel(new BorderLayout());
//        tablePanel.setOpaque(false);
//        tablePanel.add(jScrollPane);
//        backgroundPanel.add(tablePanel,"Center");

        System.out.println("在本页面展现,apmac=" + apmac);
    }

    //测试线程
    private class testThread implements Runnable {

        @Override
        public void run() {
            tool_refresh.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    while (true) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }

                        String apmac = SocketTCPServer.getApmac();
                        //                System.out.println("1231232");
                        if (apmac != null) {
                            System.out.println("apmac=" + apmac);
                            //在这里操作
                            //把参数传过去

//                                new ApListManagerJPanel().changgeApColor(apmac);


                            SocketTCPServer.setApmac(null);
                        }
                    }

                }
            });
        }
    }


    //初始化数据表格面板,用可以 插入的 那种昂
    public void initTablePanel() {
        String conditionParams[] = {"全部", "全部"};
        String params[] = {"mac地址", "apIp地址"};
        //监听UDP的线程
//        new Thread(new SocketUDPServer()).start();
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //实时监听TCP 获取返回数据，改变对应AP的颜色（上线）
        //监听TCP的线程
        try {
            SocketTCPServer socketTCPServer = new SocketTCPServer();
            SocketTCPServer.connect();
            SocketTCPServer.tRecv.start();
            try {
                socketTCPServer.tRecv.sleep(100);//延迟100，不然返回为null
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = socketTCPServer.getJsonObject();
            String apmac = socketTCPServer.getApmac();
            System.out.println("");
            if (apmac != null) {
                //渲染该mac地址的行变色
                changgeApColor(apmac);
                System.out.println("apmac 为空");
                //给AP下发配置

            }

            System.out.println(jsonObject + "here");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //监听apmac的线程
//        Thread testThread = new Thread(new testThread());
//        testThread.start();

        //开启实时监听数据包的线程数据
//        UdpUtil udpUtil = new UdpUtil();
//        udpUtil.start();
//        String[] data = udpUtil.getData();
//        System.out.println("能否获取到"+data[0]+data[1]);
//      udpUtil.
        Vector<Vector> vector = new Vector<Vector>();
        Vector step = new Vector();
        step.add("mack");
        step.add("11.11.11");
        vector.add(step);
        Vector step2 = new Vector();
        step2.add("mack2");
        step2.add("11.11.11.22");
        vector.add(step2);
        baseTableModule = new BaseTableModule(params, vector);
//        baseTableModule.setValueAt();
        table = new JTable(baseTableModule);
        jScrollPane = new JScrollPane(table);
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.add(jScrollPane);
        backgroundPanel.add(tablePanel, "Center");


//        List<Object[]> list = new ArrayList<>();
        //需要把从Ap获取到的数据转换成vector，动态数组

//        String s = "{'mcAdd':'aa','ipAdd':'10.12'},{'mcAdd':'aa','ipAdd':'10.12'}";
//        JSONArray jsonArray = JSON.parseArray(s);
//        list.add(1,"wqr");


    }

    //更新数据表格
//    public void refreshTablePanel() {
//        backgroundPanel.remove(tablePanel);
//        String params[] = {"mac地址", "apIp地址"};
//
//        Vector<Vector> vector = new Vector<Vector>();
//        Vector step = new Vector();
//        step.add("mack");
//        step.add("xx.xx.xx");
//        vector.add(step);
//        Vector step2 = new Vector();
//        step2.add("mack2");
//        step2.add("11.11.yy.yy");
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//        vector.add(step2);
//
//
//        baseTableModule = new BaseTableModule(params, vector);
//
//        table = new JTable(baseTableModule);
//        jScrollPane = new JScrollPane(table);
//
//        tablePanel = new JPanel(new BorderLayout());
//        tablePanel.setOpaque(false);
//        tablePanel.add(jScrollPane);
//
//
//        backgroundPanel.add(tablePanel, "Center");
//
//        backgroundPanel.validate();
//    }


    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        String apmac = SocketTCPServer.getApmac();
                        if (apmac != null) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    String params[] = {"mac地址", "apIp地址"};
                                    Vector<Vector> vector = new Vector<Vector>();
                                    Vector aa = new Vector();
                                    aa.add("123");
                                    aa.add(apmac);
                                    vector.add(aa);
                                    baseTableModule = new BaseTableModule(params, vector);

                                    table = new JTable(baseTableModule);
                                    jScrollPane = new JScrollPane(table);

                                    tablePanel = new JPanel(new BorderLayout());
                                    tablePanel.setOpaque(false);
                                    tablePanel.add(jScrollPane);


                                    backgroundPanel.add(tablePanel, "Center");

                                    backgroundPanel.validate();


                                }
                            });
                        }
                        System.out.println("123213");
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }).start();

    }




}
