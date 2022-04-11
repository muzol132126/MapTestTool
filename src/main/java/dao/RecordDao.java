package main.java.dao;

import main.java.model.Record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class RecordDao {

    //错误记录添加
    public int add(Connection con, Record record, String umid) throws SQLException {
        String sql = "insert into mistake_record values(null,?,?,?,?)";
        PreparedStatement preparedStatement = con.prepareStatement(sql);

        preparedStatement.setString(1, record.getFilename());
        preparedStatement.setString(2, record.getLocation());
        preparedStatement.setString(3, record.getDescription());
        preparedStatement.setString(4, umid);

        return preparedStatement.executeUpdate();
    }

    //根据用户id查询错误记录(应为管理员权限)
    public Vector<Vector> get(Connection con, String uid) throws SQLException {
        Vector<Vector> resultRecord = new Vector<>();
        String sql = "SELECT mistake_record.id,filename,location,description\n" +
                "FROM mistake_record\n" +
                "INNER JOIN uname_passwd ON mistake_record.u_m_id=uname_passwd.id\n" +
                "WHERE uname_passwd.id="+uid+"";

        PreparedStatement preparedStatement = con.prepareStatement(sql);
        ResultSet rs =preparedStatement.executeQuery();

        while (rs.next()){
            Vector curRecord = new Vector<>();
            curRecord.add(rs.getInt(1));
            curRecord.add(rs.getString(2));
            curRecord.add(rs.getString(3));
            curRecord.add(rs.getString(4));

            resultRecord.add(curRecord);
        }
        return resultRecord;
    }
    //删除记录
    public void delete(Connection con, String id) throws SQLException {
        String sql = "delete from mistake_record where id = "+id+"";

        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.executeUpdate();
    }

    //错误信息修改
    public void update(Connection con, String id, String filename, String location, String description) throws SQLException {
        String sql = "UPDATE mistake_record SET filename=?,location=?,description=? WHERE id="+id+"";

        PreparedStatement preparedStatement = con.prepareStatement(sql);
        preparedStatement.setString(1,filename);
        preparedStatement.setString(2,location);
        preparedStatement.setString(3,description);
        preparedStatement.executeUpdate();
    }


/*    public static void main(String[] args) throws Exception {
        Connection con = DBUtil.getCon();
        RecordDao recordDao = new RecordDao();

        System.out.println(recordDao.get(con, "1"));
    }*/

}
