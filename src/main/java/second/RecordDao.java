package second;

import com.mysql.jdbc.PreparedStatement;
import org.apache.commons.io.LineIterator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import common.*;

public class RecordDao {

    private MyDB myDB=new MyDB();

    public void insertRecords(LineIterator lineIterator)throws SQLException,ParseException{
        myDB.connection.setAutoCommit(false);
        String sql="insert into record(user_id,bike_id,origin,start_time,destination,end_time) values(?,?,?,?,?,?)";
        myDB.pstmt = (PreparedStatement) myDB.connection.prepareStatement(sql);
        int count=1;
        long start = System.currentTimeMillis();
        while (lineIterator.hasNext()){
            String line = lineIterator.nextLine();
            String[] custArray = line.split(";");
            myDB.pstmt.setLong(1, Long.valueOf(custArray[0]));
            myDB.pstmt.setLong(2, Long.valueOf(custArray[1]));
            myDB.pstmt.setString(3, custArray[2]);
            myDB.pstmt.setTimestamp(4, Timestamp.valueOf(ReadTxt.format(custArray[3])));
            myDB.pstmt.setString(5, custArray[4]);
            myDB.pstmt.setTimestamp(6, Timestamp.valueOf(ReadTxt.format(custArray[5])));
            myDB.pstmt.addBatch();
            if(count%5000==0){
                myDB.pstmt.executeBatch();
                myDB.connection.commit();
                myDB.pstmt.clearBatch();
                System.out.println("第"+count/5000+"批数据导入成功");
            }
//            if(count/5000==20)
//                break;
            count++;
        }
        myDB.pstmt.executeBatch();
        myDB.connection.commit();
        long end = System.currentTimeMillis();
        myDB.Close();
        System.out.println("数据导入结束，总共耗时："+(end-start)/1000+"秒");
    }

    public void update(String sql){
        try {
            myDB.pstmt = (PreparedStatement) myDB.connection.prepareStatement(sql);
            myDB.pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ResultSet select(String sql){
        try{
            myDB.pstmt = (PreparedStatement) myDB.connection.prepareStatement(sql);
            myDB.rs= myDB.pstmt.executeQuery();
            int rowCount=myDB.rs.getRow();
            if(rowCount==0)
                return null;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return myDB.rs;
    }

    public String getAddress(long id){
        String address=null;
        try{
            String sql="select destination from record where user_id="+id+" and date_format(end_time,'%T')>='18:00:00' and date_format(end_time,'%T')<='24:00:00' group by destination order by count(destination) desc limit 1";
            myDB.pstmt=(PreparedStatement) myDB.connection.prepareStatement(sql);
            myDB.rs=myDB.pstmt.executeQuery();
            if(myDB.rs.next()){
                address = myDB.rs.getString(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return address;
    }

    public List<String> getAddress(List<User> users){
        List<String> addresses=new ArrayList<>();
        for(User user:users){
            String address=getAddress(user.getId());
            addresses.add(address);
        }
        return addresses;
    }
}
