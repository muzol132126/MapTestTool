package main.java.component;

import main.java.dao.RecordDao;
import main.java.listener.ActionDoneListener;

import main.java.util.ScreenUtils;
import main.java.util.DBUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Map;

public class UpdateDialog extends JDialog {
    final int WIDTH = 400;
    final int HEIGHT = 300;
    private String id;
    private final ActionDoneListener listener;


    private JTextField nameField;
    private JTextField LocationField;
    private JTextArea InfoArea;

    DBUtil dbUtil = new DBUtil();
    RecordDao recordDao = new RecordDao();


    public UpdateDialog(final JFrame jf, String title, boolean isModel, final ActionDoneListener listener, String id){
        super(jf,title,isModel);
        this.listener  = listener;
        this.id=id;
        //组装视图
        this.setBounds((ScreenUtils.getScreenWidth()-WIDTH)/2,(ScreenUtils.getScreenHeight()-HEIGHT)/2,WIDTH,HEIGHT);

        Box vBox = Box.createVerticalBox();

        //组装文件名称
        Box nameBox = Box.createHorizontalBox();
        JLabel nameLable = new JLabel("文件名称：");
        nameField = new JTextField(15);

        nameBox.add(nameLable);
        nameBox.add(Box.createHorizontalStrut(20));
        nameBox.add(nameField);

        //组装错误位置
        Box stockBox = Box.createHorizontalBox();
        JLabel stockLable = new JLabel("错误位置：");
        LocationField = new JTextField(15);

        stockBox.add(stockLable);
        stockBox.add(Box.createHorizontalStrut(20));
        stockBox.add(LocationField);


        //组装错误描述
        Box descBox = Box.createHorizontalBox();
        JLabel descLable = new JLabel("错误描述：");
        InfoArea = new JTextArea(3,15);

        descBox.add(descLable);
        descBox.add(Box.createHorizontalStrut(20));
        descBox.add(new JScrollPane(InfoArea));

        //组装按钮
        Box btnBox = Box.createHorizontalBox();
        JButton updateBtn = new JButton("修改");
        updateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取用户修改后在输入框中输入的内容
                String name = nameField.getText().trim();
                String Location = LocationField.getText().trim();
                String Info = InfoArea.getText().trim();
                if(name.isEmpty()){
                    return;
                }

                Connection con = null;

                int confirmDialog = JOptionPane.showConfirmDialog(jf, "确认要修改选中的条目吗？", "确认修改", JOptionPane.YES_NO_OPTION);
                if (confirmDialog != JOptionPane.YES_OPTION) {
                    return;
                }else{
                try {
                    con=dbUtil.getCon();
                    recordDao.update(con, id, name, Location, Info);
                    JOptionPane.showMessageDialog(jf,"修改成功");

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                }


            }
        });
        //TODO 处理修改的行为
        btnBox.add(updateBtn);

        vBox.add(Box.createVerticalStrut(20));
        vBox.add(nameBox);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(stockBox);
        vBox.add(Box.createVerticalStrut(15));
        vBox.add(descBox);
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
