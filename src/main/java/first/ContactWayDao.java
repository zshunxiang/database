package first;

import com.mysql.jdbc.PreparedStatement;

import java.sql.SQLException;
import common.*;
public class ContactWayDao {

    private MyDB myDB=new MyDB();

    public int insertContactWay(ContactWay contactWay){
        int i=0;
        try {
            String sql = "insert into contact (dormitory,tel) values(?,?)";
            myDB.pstmt = (PreparedStatement) myDB.connection.prepareStatement(sql);
            myDB.pstmt.setString(1, contactWay.getDormitory());
            myDB.pstmt.setString(2, contactWay.getTel());
            i = myDB.pstmt.executeUpdate();
            System.out.println("成功插入一条联系方式数据");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return i;
    }
}
