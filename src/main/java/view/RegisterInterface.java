package main.java.view;

import main.java.component.BackGroundPanel;
import main.java.util.ScreenUtils;
import main.java.dao.UserDao;
import main.java.model.User;
import main.java.util.DBUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class RegisterInterface {
    JFrame jf = new JFrame("注册界面");
    DBUtil dbUtil = new DBUtil();
    UserDao userDao = new UserDao();

    final int WIDTH = 800;
    final int HEIGHT = 600;

    //组装视图
    public void  init() throws Exception {
        //设置窗口相关属性
        jf.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);
        jf.setResizable(false);


        //设置窗口的内容
        BackGroundPanel bgPanel = new BackGroundPanel(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream("regist.jpg"))));

        //组装登录的相关元素
        Box vBox = Box.createVerticalBox();

        //组装用户名
        Box uBox = Box.createHorizontalBox();
        JLabel uLabel = new JLabel("用户名");
        final JTextField uField = new JTextField(15);

        uBox.add(uLabel);
        uBox.add(Box.createHorizontalStrut(25));
        uBox.add(uField);

        //组装密码
        Box pBox = Box.createHorizontalBox();
        JLabel pLabel = new JLabel("密  码");
        JPasswordField pField = new JPasswordField(15);

        pBox.add(pLabel);
        pBox.add(Box.createHorizontalStrut(30));
        pBox.add(pField);

        //组装确认密码
        Box cBox = Box.createHorizontalBox();
        JLabel cLabel = new JLabel("确认密码");
        JPasswordField cField = new JPasswordField(15);

        cBox.add(cLabel);
        cBox.add(Box.createHorizontalStrut(10));
        cBox.add(cField);

        //组装按钮
        Box btnBox = Box.createHorizontalBox();
        JButton registBtn = new JButton("注册");
        JButton backBtn = new JButton("返回登录界面");


        //注册按钮的响应
        registBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取用户录入的信息
                String username = uField.getText().trim();
                String password = pField.getText().trim();
                String vpassword = cField.getText().trim();
                Connection con = null;
                try {
                    con = DBUtil.getCon();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if(username.isEmpty()){
                    JOptionPane.showMessageDialog(jf, "用户名不能为空!");
                    return;
                }
                try {
                    if(userDao.isUserExist(con, username)){
                        JOptionPane.showMessageDialog(jf, "用户名已存在, 请更换用户名!");
                        return;
                    }
                    if(password.isEmpty()){
                        JOptionPane.showMessageDialog(jf, "密码不能为空!");
                        return;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                if(vpassword.isEmpty()){
                    JOptionPane.showMessageDialog(jf, "确认密码不能为空!");
                    return;
                }
                if(password.equals(vpassword)){

                    User user = new User(username, password);

                    try {

                        int addnum = userDao.addUser(con,user);
                        if(addnum == 1){
                            JOptionPane.showMessageDialog(jf,"注册成功!");
                            JOptionPane.showMessageDialog(jf, "即将跳转到登录界面");
                            new LoginInterface().init();
                            jf.dispose();
                        }else{
                            JOptionPane.showMessageDialog(jf, "注册失败！");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }finally {
                        try {
                            dbUtil.closeCon(con);
                        }catch (Exception e1){
                            e1.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(jf, "两次密码不一致！");
                }

                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        //返回登录界面按钮的响应
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new LoginInterface().init();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                jf.dispose();
            }
        });

        btnBox.add(registBtn);
        btnBox.add(Box.createHorizontalStrut(100));
        btnBox.add(backBtn);

        vBox.add(Box.createVerticalStrut(170));
        vBox.add(uBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(pBox);
        vBox.add(Box.createVerticalStrut(40));
        vBox.add(cBox);
        vBox.add(Box.createVerticalStrut(50));
        vBox.add(btnBox);

        bgPanel.add(vBox);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.add(bgPanel);
        jf.setVisible(true);
    }

   /* public static void main(String[] args) throws Exception {
        new RegisterInterface().init();
    }*/
}
