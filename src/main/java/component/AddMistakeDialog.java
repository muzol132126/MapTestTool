package main.java.component;

import main.java.dao.RecordDao;
import main.java.listener.ActionDoneListener;

import main.java.model.Record;
import main.java.util.ScreenUtils;
import main.java.util.DBUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class AddMistakeDialog extends JDialog {
    final int WIDTH = 400;
    final int HEIGHT = 300;

    private ActionDoneListener listener;
    DBUtil dbUtil = new DBUtil();
    RecordDao recordDao = new RecordDao();


    public AddMistakeDialog(final JFrame jf, String title, boolean isModel, final ActionDoneListener listener, String uid){
        super(jf,title,isModel);
        this.listener  = listener;
        //组装视图
        this.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);

        final Box vBox = Box.createVerticalBox();

        //组装名称
        Box nameBox = Box.createHorizontalBox();
        JLabel nameLabel = new JLabel("文件名称：");
        final JTextField nameField = new JTextField(15);

        nameBox.add(nameLabel);
        nameBox.add(Box.createHorizontalStrut(20));
        nameBox.add(nameField);

        //组装错误位置组件
        Box locationBox = Box.createHorizontalBox() ;
        JLabel locationLabel = new JLabel("错误位置：");
        final JTextField locationField = new JTextField(15);

        locationBox.add(locationLabel);
        locationBox.add(Box.createHorizontalStrut(20));
        locationBox.add(locationField);

        //组装错误信息组件
        Box infoBox = Box.createHorizontalBox();
        JLabel infoLabel = new JLabel("错误信息：");
        final JTextArea infoArea = new JTextArea(3,15);

        infoBox.add(infoLabel);
        infoBox.add(Box.createHorizontalStrut(20));
        infoBox.add(infoArea);

        //组装按钮
        Box btnBox = Box.createHorizontalBox();
        JButton addBtn = new JButton("添加");
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取用户的录入
                String name = nameField.getText().trim();
                String location = locationField.getText().trim();
                String info = infoArea.getText().trim();


                if(name.isEmpty()){
                    JOptionPane.showMessageDialog(jf, "文件名不能为空");
                    return;
                }
                if(location.isEmpty()){
                    JOptionPane.showMessageDialog(jf, "错误位置不能为空");
                    return;
                }

                Record record = new Record(name, location, info);
                Connection con = null;
                try {
                    con = DBUtil.getCon();
                    int addnum = recordDao.add(con, record, uid);

                    if(addnum == 1){
                        JOptionPane.showMessageDialog(jf,"添加成功");

                    }else{
                        JOptionPane.showMessageDialog(jf, "添加失败");
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

            }
        });


        btnBox.add(addBtn);

        vBox.add(Box.createVerticalStrut(20));
        vBox.add(nameBox);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(locationBox);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(infoBox);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(btnBox);

        //为了左右有间距，在vBox外层封装一个水平的Box，添加间隔
        Box hBox = Box.createHorizontalBox();
        hBox.add(Box.createHorizontalStrut(20));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalStrut(20));

        this.add(hBox);
    }

}
