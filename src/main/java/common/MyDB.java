package common;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;


public class MyDB {

    public Connection connection;
    public PreparedStatement pstmt;
    public ResultSet rs;

    public MyDB(){
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/zsx?characterEncoding=utf8&rewriteBatchedStatements=true";
        String username = "root";
        String password = "root";
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.connection=conn;
    }

    public void Close(){
        try {
            pstmt.close();
            connection.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
