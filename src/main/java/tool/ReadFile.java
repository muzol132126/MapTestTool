package main.java.tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class ReadFile {
    Vector<Vector> vec = new Vector<>();
    public Vector<Vector> readFile(String filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String str;
            while ((str = br.readLine()) != null) {

                String newStr = str;
                String[] arrayStr = newStr.split(" ");
                Vector<String> vect = new Vector<>();
                for (int i = 0; i < arrayStr.length; i++) {

                    vect.add(arrayStr[i]);
                }

                vec.add(vect);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vec;
    }
}