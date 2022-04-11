package main.java.component;


import main.java.dao.RecordDao;
import main.java.tool.TableSortFilter;
import main.java.util.DBUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Vector;


public class ManageComponent extends Box {
    final int WIDTH = 1000;
    final int HEIGHT = 900;

    JFrame jf ;
    private JTable table;
    private Vector<String> titles;
    private Vector<Vector> tableData;
    private DefaultTableModel tableModel;
    DBUtil dbUtil = new DBUtil();
    RecordDao recordDao = new RecordDao();


    public ManageComponent(final JFrame jf, String uid) throws Exception {
        //垂直布局
        super(BoxLayout.Y_AXIS);
        //组装视图
        this.jf = jf;
        JPanel btnPanel = new JPanel();
        Color color = new Color(203, 220, 217);
        btnPanel.setBackground(color);
        btnPanel.setMaximumSize(new Dimension(WIDTH, 80));
        btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton addBtn = new JButton("添加");
        JButton searchBtn = new JButton("查找");
        JButton updateBtn = new JButton("修改");
        JButton deleteBtn = new JButton("删除");


        addBtn.addActionListener(e -> {
            //弹出一个对话框，让用户输入错误的信息
            new AddMistakeDialog(jf, "添加错误", true, result -> {
                try {
                    requestData(uid);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }, uid).setVisible(true);
            try {
                requestData(uid);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        searchBtn.addActionListener(e -> {
           //新建立一个窗口以进行错误查询
            JFrame frame = new JFrame("错误查询");

            try {
                TableSortFilter tsj = new TableSortFilter(uid);
                frame.add(tsj);
            }catch (Exception e1){
                e1.printStackTrace();
            }
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        updateBtn.addActionListener(e -> {
            //获取当前表格选中的id
            int selectedRow = table.getSelectedRow();//如果有选中的条目，则返回条目的行号，如果没有选中，那么返回-1

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(jf, "请选择要修改的条目！");
                return;
            }

            String id = tableModel.getValueAt(selectedRow, 0).toString();

            //弹出一个对话框，让用户修改
            new UpdateDialog(jf, "修改错误", true, result -> {
                try {
                    requestData(uid);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }, id).setVisible(true);
            //JOptionPane.showMessageDialog(jf, "修改成功");
            try {
                requestData(uid);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        deleteBtn.addActionListener(e -> {
            //获取选中的条目
            int selectedRow = table.getSelectedRow();//如果有选中的条目，则返回条目的行号，如果没有选中，那么返回-1

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(jf, "请选择要删除的条目！");
                return;
            }

            //防止误操作
            int result = JOptionPane.showConfirmDialog(jf, "确认要删除选中的条目吗？", "确认删除", JOptionPane.YES_NO_OPTION);
            if (result != JOptionPane.YES_OPTION) {
                return;
            }

            String id = tableModel.getValueAt(selectedRow, 0).toString();
            try {
                Connection con = DBUtil.getCon();
                recordDao.delete(con, id);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                requestData(uid);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnPanel.add(addBtn);
        btnPanel.add(searchBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);

        this.add(btnPanel);

        //组装表格
        String[] ts = {"编号", "文件名称", "错误位置", "错误信息"};
        titles = new Vector<>();
        titles.addAll(Arrays.asList(ts));

        tableData = new Vector<>();

        tableModel = new DefaultTableModel(tableData, titles);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        //设置只能选中一行
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.add(table);

        JScrollPane scrollPane = new JScrollPane(table);
        this.add(scrollPane);


        requestData(uid);
    }

        public void requestData(String umid) throws Exception {

            //访问存储错误数据的文件
            DBUtil dbUtil = new DBUtil();
            RecordDao recordDao = new RecordDao();

            Vector<Vector> vectors;
            Connection con = null;
            con = dbUtil.getCon();
            vectors = recordDao.get(con, umid);
            //清空tableData的数据
            tableData.clear();
            tableData.addAll(vectors);

            //刷新表格
            tableModel.fireTableDataChanged();
            con.close();
    }

}

