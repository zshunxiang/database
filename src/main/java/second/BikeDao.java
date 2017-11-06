package second;

import com.mysql.jdbc.PreparedStatement;
import common.*;

import java.sql.SQLException;

public class BikeDao {

    private MyDB myDB=new MyDB();

    public int insert(Bike bike){
        int i=0;
        try{
            String sql="insert into bike(id) values (?)";
            myDB.pstmt = (PreparedStatement) myDB.connection.prepareStatement(sql);
            myDB.pstmt.setLong(1,bike.getId());
            i=myDB.pstmt.executeUpdate();
            System.out.println("成功插入一条共享单车数据");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return i;
    }
}
