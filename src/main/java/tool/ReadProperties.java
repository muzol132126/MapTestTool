package main.java.tool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {

    private static ReadProperties rp;

    public String dbUrl = null;
    public String dbUsername = null;
    public String dbPassword = null;

    private ReadProperties(){
        LoadProperties();
    }

    public static ReadProperties initial(){
        if(rp == null)
            rp = new ReadProperties();
        return rp;
    }

    public void LoadProperties(){

        InputStream ips = getClass().getResourceAsStream("/my.properties");
        Properties properties = new Properties();
        try{
            properties.load(ips);
            this.dbUrl = properties.getProperty("url");
            this.dbUsername = properties.getProperty("username");
            this.dbPassword = properties.getProperty("password");

            System.out.println(this.dbUrl+"  "+ this.dbPassword);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args){
        ReadProperties rp = new ReadProperties();
        rp.LoadProperties();
    }
}
