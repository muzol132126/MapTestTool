package main.java.tool;

import main.java.dao.RecordDao;
import main.java.util.DBUtil;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TableSortFilter extends JPanel {

    private Vector<String> titles;

    private Vector<Vector> tabledata = new Vector<>();

    DBUtil dbUtil = new DBUtil();
    RecordDao recordDao = new RecordDao();

    public TableSortFilter(String uid) throws Exception {
        String[] ts = {"编号", "文件名称", "错误位置", "错误信息"};
        titles = new Vector<>();
        titles.addAll(Arrays.asList(ts));
        //ReadFile rf = new ReadFile();

        Connection con = null;
        con = DBUtil.getCon();


        tabledata = recordDao.get(con, uid);
        DefaultTableModel model = new DefaultTableModel(tabledata, titles);
        JTable jTable = new JTable(model);

        final TableRowSorter<TableModel> rowSorter
                = new TableRowSorter<>(jTable.getModel());

        final JTextField jtfFilter = new JTextField();
        JButton jbtFilter = new JButton("查询");
        jTable.setRowSorter(rowSorter);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("请输入关键字来匹配"),
                BorderLayout.WEST);
        panel.add(jtfFilter, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.SOUTH);
        add(new JScrollPane(jTable), BorderLayout.CENTER);

        jtfFilter.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = jtfFilter.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = jtfFilter.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });
    }

/*    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                JFrame frame = new JFrame("Row Filter");
                try {
                    frame.add(new TableSortFilter());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }

        });
    }*/
}