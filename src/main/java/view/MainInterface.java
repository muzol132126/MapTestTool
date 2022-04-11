package main.java.view;


import main.java.component.BackGroundPanel;
import main.java.component.ManageComponent;
import main.java.tool.GeotiffLab;
import main.java.tool.ImageLab;
import main.java.tool.TxtLab;
import main.java.util.ScreenUtils;
import main.java.model.User;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Objects;


public class MainInterface {
    JFrame jf = new JFrame("地图质检工具");

    final int WIDTH = 1200;
    final int HEIGHT = 900;

    JMenuBar jmb = new JMenuBar();
    JPopupMenu jPopupMenu = new JPopupMenu();



    public void init(User curUser) {
        //设置窗口属性
        jf.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);
        jf.setResizable(false);

        //窗口风格类型
        JRadioButtonMenuItem metalItem = new JRadioButtonMenuItem("Metal 风格");
        JRadioButtonMenuItem windowsItem = new JRadioButtonMenuItem("Windows 风格");
        JRadioButtonMenuItem windowsClassicItem = new JRadioButtonMenuItem("WindowsClassic 风格");
        JRadioButtonMenuItem motifItem = new JRadioButtonMenuItem("Motif 风格");

        ButtonGroup popupButtonBg = new ButtonGroup();

        //组装右键菜单
        popupButtonBg.add(metalItem);
        popupButtonBg.add(windowsItem);
        popupButtonBg.add(windowsClassicItem);
        popupButtonBg.add(motifItem);

        ActionListener listener = e -> {
            String actionCommand = e.getActionCommand();
            try {
                changeFlavor(actionCommand);
            }catch(Exception ex) {
                ex.printStackTrace();
            }
        };
        metalItem.addActionListener(listener);
        windowsItem.addActionListener(listener);
        windowsClassicItem.addActionListener(listener);
        motifItem.addActionListener(listener);


        jPopupMenu.add(metalItem);
        jPopupMenu.add(windowsItem);
        jPopupMenu.add(windowsClassicItem);
        jPopupMenu.add(motifItem);

        //设置菜单栏
        JMenu jMenu1 = new JMenu("文件");

        JMenu jMenu2 = new JMenu("设置");
        JMenu jMenu3 = new JMenu("关于");

        jmb.add(jMenu1);

        jmb.add(jMenu2);
        jmb.add(jMenu3);


        // menu1：文件
        final JMenuItem item1 = new JMenuItem("打开数据文件");
        final JMenuItem item2 = new JMenuItem("查看矢量数据");
        final JMenuItem item3 = new JMenuItem("查看栅格数据");
        final JMenuItem item4 = new JMenuItem("查看文本数据");

        // menu3: 设置
        final JMenuItem item5 = new JMenuItem("切换账号");
        final JMenuItem item6 = new JMenuItem("退出程序");

        // menu4:关于
        final JMenuItem item7 = new JMenuItem("帮助文档");


        // 组装菜单项
        jMenu1.add(item1);
        jMenu1.addSeparator();
        jMenu1.add(item2);
        jMenu1.addSeparator();
        jMenu1.add(item3);
        jMenu1.addSeparator();
        jMenu1.add(item4);

        jMenu2.add(item5);
        jMenu2.add(item6);

        jMenu3.add(item7);


        //打开数据文件事件监听器
        item1.addActionListener(e -> {
            ImageLab ILab = new ImageLab();
            try {
                ILab.getLayersAndDisplay();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //查看矢量数据事件监听器
        item2.addActionListener(e -> {
            File file = JFileDataStoreChooser.showOpenFile("shp", null);
            if (file == null) {
                return;
            }

            FileDataStore store = null;
            try {
                store = FileDataStoreFinder.getDataStore(file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            SimpleFeatureSource featureSource = null;
            try {
                assert store != null;
                featureSource = store.getFeatureSource();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // 创建一个容器并添加shp数据
            MapContent map = new MapContent();
            map.setTitle("查看矢量数据");

            assert featureSource != null;
            Style style = SLD.createSimpleStyle(featureSource.getSchema());
            Layer layer = new FeatureLayer(featureSource, style);
            map.addLayer(layer);

            // 显示地图
            JMapFrame.showMap(map);

        });

        //查看栅格数据事件监听器
        item3.addActionListener(e -> {
            GeotiffLab GTLab = new GeotiffLab();
            try {
                GTLab.getTiffLayersAndDisplay();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //查看文本数据事件监听器
        item4.addActionListener(e -> {
            TxtLab TLab = new TxtLab();
            TLab.init();
        });


        //切换账号事件监听器
        item5.addActionListener(e -> {
            try{
                new LoginInterface().init();
                jf.dispose();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        });

        //退出程序事件监听器m
        item6.addActionListener(e -> System.exit(0));

        //查看帮助文档事件监听器
        item7.addActionListener(e -> {
            //在图片上绘制文字
          /*  DisplayHelp dh = new DisplayHelp();
            BufferedImage bi = dh.loadImageLocal(Objects.requireNonNull(this.getClass().
                    getResource("/back-panel.jpg")).getPath());

            BufferedReader br = null;

            try {
                br = new BufferedReader(new FileReader(Objects.requireNonNull(this.getClass().
                        getResource("/Readme.txt")).getPath()));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            String str = null;
            int i=0;

            while (true) {
                try {
                    assert br != null;
                    if ((str = br.readLine()) == null) break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                dh.writeImageLocal(Objects.requireNonNull(this.getClass().getResource("/newhelp.jpg")).getPath(),
                        dh.modifyImage(bi, str, 50, 30+20*i));
                i++;
            }
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }*/


            JFrame jf = new JFrame();
            BackGroundPanel bgPanel;
            try {
                bgPanel = new BackGroundPanel(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader()
                        .getResourceAsStream("newhelp.jpg"))));
                jf.add(bgPanel);

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            jf.setTitle("这是帮助文档！");
            jf.setBounds(600,600,900,600);
            jf.setVisible(true);

        });

        //设置分割面板
        final JSplitPane sp = new JSplitPane();

        //支持连续布局
        sp.setContinuousLayout(true);
        sp.setDividerLocation(200);
        sp.setDividerSize(7);

        //设置左侧内容
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("工具管理");
        final DefaultMutableTreeNode fileManage = new DefaultMutableTreeNode("文件管理");
        final DefaultMutableTreeNode searchManage = new DefaultMutableTreeNode("错误管理");

        DefaultMutableTreeNode textFile = new DefaultMutableTreeNode("文本文件");
        DefaultMutableTreeNode vectorFile = new DefaultMutableTreeNode("矢量文件");
        DefaultMutableTreeNode rasterFile = new DefaultMutableTreeNode("栅格文件");

        fileManage.add(textFile);
        fileManage.add(vectorFile);
        fileManage.add(rasterFile);

        root.add(fileManage);
        root.add(searchManage);


        Color color = new Color(203,237,217);
        JTree tree = new JTree(root);
        MyRenderer myRenderer = new MyRenderer();
        myRenderer.setBackgroundNonSelectionColor(color);
        myRenderer.setBackgroundSelectionColor(new Color(140,140,140));
        tree.setCellRenderer(myRenderer);

        tree.setBackground(color);

        String uid = String.valueOf(curUser.getId());
        //设置当前tree默认选中错误管理
        tree.setSelectionRow(2);

        //当条目选中变化后，这个方法会执行
        tree.addTreeSelectionListener(e -> {
            //得到当前选中的结点对象
            Object lastPathComponent = e.getNewLeadSelectionPath().getLastPathComponent();

            if (fileManage.equals(lastPathComponent)){
                sp.setRightComponent(new JLabel("这里进行文件管理..."));
                sp.setDividerLocation(150);
            }else if (searchManage.equals(lastPathComponent)){
               try {

                   sp.setRightComponent(new ManageComponent(jf, uid));
                   sp.setDividerLocation(150);
               }catch (Exception e1){
                   e1.printStackTrace();
               }
            }
        });

        jf.add(sp);

        try {
            sp.setRightComponent(new ManageComponent(jf, uid));
            sp.setLeftComponent(tree);
        }catch (Exception e){
            e.printStackTrace();
        }


        //菜单栏可改变窗口风格
        jmb.setComponentPopupMenu(jPopupMenu);

        jf.setJMenuBar(jmb);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);

    }

    public static void main(String[] args) {

        //对主界面进行测试
        User testUser = new User("test","123");
        testUser.setId(100);//暂时不存在的用户id(无法对数据进行增删改查）

        try{
            new MainInterface().init(testUser);
        } catch (Exception e){
            e.printStackTrace();
        }

    }


    private class MyRenderer extends DefaultTreeCellRenderer {
        private final ImageIcon rootIcon;
        private final ImageIcon fileManageIcon;
        private ImageIcon searchManageIcon;


        public MyRenderer() {
            rootIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/file.png")));
            fileManageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/file.png")));
            searchManageIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/error.png")));
        }

        //当绘制树的每个结点时，都会调用这个方法
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {
            //使用默认绘制
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            ImageIcon image = null;
            switch (row) {
                case 0:
                    image = rootIcon;
                    break;
                case 1:
                    image = fileManageIcon;
                    break;
                case 2:
                    image = searchManageIcon;
                    break;
            }

            this.setIcon(image);
            return this;
        }
    }

    //更改窗口风格的函数
    private void changeFlavor(String command) throws Exception {
        switch(command) {
            case "Metal 风格":
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                break;
            case "Windows 风格":
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                break;
            case "WindowsClassic 风格":
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
                break;
            case "Motif 风格":
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                break;

        }

        SwingUtilities.updateComponentTreeUI(jf.getContentPane());
        SwingUtilities.updateComponentTreeUI(jmb);
        SwingUtilities.updateComponentTreeUI(jPopupMenu);

    }
}
