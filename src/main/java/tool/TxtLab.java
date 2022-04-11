package main.java.tool;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class TxtLab {
    Toolkit kit = Toolkit.getDefaultToolkit() ;

    Dimension screenSize = kit.getScreenSize() ;

    int screenWidth = screenSize.width ;
    int screenHeight = screenSize.height ;
    int width = screenWidth/2 ;
    int height = screenHeight/2 ;
    int x = screenWidth/2-width/2 ;
    int y = screenHeight/2-height/2 ;

    JFrame jf = new JFrame("文本文件");
    File file;

    JMenuBar jmb = new JMenuBar();
    JMenu fileMenu = new JMenu("文件");

    JMenuItem save = new JMenuItem("保存文件");
    JMenuItem open = new JMenuItem("打开文件");

    JTextArea ta = new JTextArea(8,20);
    FileDialog openDia = new FileDialog(jf,"我的打开",FileDialog.LOAD);
    FileDialog saveDia = new FileDialog(jf,"我的保存",FileDialog.SAVE);

    public void init(){

        fileMenu.add(save);
        fileMenu.addSeparator();
        fileMenu.add(open);
        jmb.add(fileMenu);

        open.addActionListener(e -> {
            openDia.setVisible(true);
            String DirPath = openDia.getDirectory();
            String FileName = openDia.getFile();

            if(DirPath == null || FileName ==null)
                return;

            ta.setText("");

            file = new File(DirPath, FileName);

            try{
                BufferedReader bufr = new BufferedReader(new FileReader(file));
                String line;

                while((line = bufr.readLine())!=null)
                {
                    ta.append(line + "\n");
                }
                bufr.close();
            }
            catch (IOException ex)
            {
                throw new RuntimeException("文件读取失败！");
            }
        });

        //设置保存文件功能
        save.addActionListener(e -> {
            if(file == null)
            {
                saveDia.setVisible(true);
                String DirPath = saveDia.getDirectory();
                String FileName = saveDia.getFile();

                if (DirPath == null || FileName == null)
                    return;
                file = new File(DirPath,FileName);
            }

            try
            {
                BufferedWriter bufw = new BufferedWriter(new FileWriter(file));

                String text = ta.getText();

                bufw.write(text);

                bufw.close();
            }
            catch (IOException ex)
            {
                throw new RuntimeException("文件保存失败！");
            }
        });

        jf.setJMenuBar(jmb);
        jf.add(ta);

        jf.setSize(width, height);
        jf.setBounds(x,y,width,height);
        jf.setDefaultCloseOperation(jf.DISPOSE_ON_CLOSE);
        jf.setVisible(true);
    }

    /*public static void main(String[] args) {
        new TxtLab().init();
    }*/
}
