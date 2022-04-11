package main.java.dao;

import main.java.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    //用户登录
    public User login(Connection con, User user)throws SQLException {
        User resultUser=null;
        String sql = "select*from uname_passwd where username=? and password=?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, user.getUserName());
        //直接获取用户密码安全性较低，之后考虑对密码加密
        preparedStatement.setString(2, user.getPassword());
        ResultSet rs = preparedStatement.executeQuery();

        if(rs.next()){
            resultUser = new User(null, null);
            resultUser.setId((rs.getInt("id")));
            resultUser.setUserName(rs.getString("username"));
            resultUser.setPassword(rs.getString("password"));

        }
        return resultUser;
    }

    //用户注册
    public int addUser(Connection con, User user)throws Exception{
        String sql = "insert into uname_passwd values(null,?,?)";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, user.getUserName());
        preparedStatement.setString(2, user.getPassword());

        return preparedStatement.executeUpdate();
    }

    //判断用户名是否存在
    public Boolean isUserExist(Connection con, String username) throws SQLException {
        String sql = "select count(*) from uname_passwd where username=?";
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1, username);

        ResultSet rs = preparedStatement.executeQuery();

        if(rs.next()){
            if(rs.getInt(1)>0){
                return true;
            }
            else
            {return false;}
        }else{
            return false;
        }
    }

}
