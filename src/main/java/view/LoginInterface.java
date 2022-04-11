package main.java.view;

import main.java.component.BackGroundPanel;
import main.java.dao.UserDao;
import main.java.model.User;
import main.java.util.DBUtil;
import main.java.util.ScreenUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.sql.Connection;
import java.util.Objects;


public class LoginInterface {

    JFrame jf = new JFrame("登录界面");
    DBUtil dbUtil = new DBUtil();
    UserDao userDao = new UserDao();
    User currentUser = new User(null,null);

    final int WIDTH = 800;
    final int HEIGHT = 600;


    //组装视图
    public void  init() throws Exception {
        //设置窗口相关属性
        jf.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);
        jf.setResizable(false);


        //设置窗口的内容
        BackGroundPanel bgPanel = new BackGroundPanel(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().
                getResourceAsStream("back.jpg"))));
        //这里需要使用getResourceAsStream,否则jar包无法加载出图片


        //组装登录的相关元素
        Box vBox = Box.createVerticalBox();

        //组装用户名
        Box uBox = Box.createHorizontalBox();
        JLabel uLabel = new JLabel("用户名");
        final JTextField uField = new JTextField(15);

        uBox.add(uLabel);
        uBox.add(Box.createHorizontalStrut(20));
        uBox.add(uField);

        Box pBox = Box.createHorizontalBox();
        JLabel pLabel = new JLabel("密  码");
        JPasswordField pField = new JPasswordField(15);

        pBox.add(pLabel);
        pBox.add(Box.createHorizontalStrut(27));
        pBox.add(pField);

        //组装按钮
        Box btnBox = Box.createHorizontalBox();
        JButton loginBtn = new JButton("登录");
        JButton registBtn = new JButton("注册");

        //登录
        loginBtn.addActionListener(e -> {
            //获取用户输入信息
            String username = uField.getText().trim();
            String password = pField.getText().trim();
            if(username.isEmpty()){
                JOptionPane.showMessageDialog(jf, "用户名不能为空");
                return;
            }
            if(password.isEmpty()){
                JOptionPane.showMessageDialog(jf, "密码不能为空");
                return;
            }

            User user = new User(username, password);
            Connection con = null;
            try {
                con = DBUtil.getCon();
                currentUser = userDao.login(con, user);

                if(currentUser != null){
                    JOptionPane.showMessageDialog(jf,"登录成功");
                    new MainInterface().init(currentUser);
                    jf.dispose();

                }else{
                    JOptionPane.showMessageDialog(jf, "用户名或密码错误");
                }
            }catch (Exception e1){
                e1.printStackTrace();
            }finally {
                try {
                    dbUtil.closeCon(con);
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            }

        });

        //注册
        registBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(jf, "即将跳转到注册页面!");
            try {
                new RegisterInterface().init();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            jf.dispose();

        });

        btnBox.add(loginBtn);
        btnBox.add(Box.createHorizontalStrut(100));
        btnBox.add(registBtn);

        vBox.add(Box.createVerticalStrut(200));
        vBox.add(uBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(pBox);
        vBox.add(Box.createVerticalStrut(50));
        vBox.add(btnBox);

        bgPanel.add(vBox);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.add(bgPanel);
        jf.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new LoginInterface().init();

    }
}
