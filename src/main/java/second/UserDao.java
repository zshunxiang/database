package second;

import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import common.*;

public class UserDao {

    private MyDB myDB=new MyDB();

    public int insertUser(User user){
        int i=0;
        try {
            String sql = "insert into user (id,name,tel,money) values(?,?,?,?)";
            myDB.pstmt = (PreparedStatement) myDB.connection.prepareStatement(sql);
            myDB.pstmt.setLong(1, user.getId());
            myDB.pstmt.setString(2, user.getName());
            myDB.pstmt.setString(3, user.getTel());
            myDB.pstmt.setDouble(4, user.getMoney());
            i = myDB.pstmt.executeUpdate();
            System.out.println("成功插入一条用户数据");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return i;
    }

    public List<User> getUser(){
        User user=new User();
        List<User> users=new ArrayList<>();
        try{
            String sql="select * from user";
            myDB.pstmt = (PreparedStatement) myDB.connection.prepareStatement(sql);
            myDB.rs=myDB.pstmt.executeQuery();
            while (myDB.rs.next()){
                user.setId(myDB.rs.getLong(1));
                user.setName(myDB.rs.getString(2));
                user.setTel(myDB.rs.getString(3));
                user.setMoney(myDB.rs.getDouble(4));
                user.setAddress(myDB.rs.getString(5));
                users.add(user);
            }
            System.out.println("用户查询结束");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    public void updateAddress(User user){
        try{
            String sql="select destination from record where user_id="+user.getId()+" and date_format(end_time,'%T')>='18:00:00' and date_format(end_time,'%T')<='24:00:00' group by destination order by count(destination) desc limit 1";
            myDB.pstmt=(PreparedStatement) myDB.connection.prepareStatement(sql);
            myDB.rs=myDB.pstmt.executeQuery();
            while (myDB.rs.next()){
                String address=myDB.rs.getString(1);
                sql="update user set address='"+address+"' where id="+user.getId();
                myDB.pstmt=(PreparedStatement) myDB.connection.prepareStatement(sql);
                myDB.pstmt.executeUpdate();
                myDB.pstmt.close();
                System.out.println("成功更新一个住址");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateAddress(){
        long id;
        try{
            String sql="select id from user";
            myDB.pstmt = (PreparedStatement) myDB.connection.prepareStatement(sql);
            myDB.rs=myDB.pstmt.executeQuery();
            while (myDB.rs.next()){
                id=myDB.rs.getLong(1);
                String address=getAddress(id);
                if(address!=null) {
                    sql = "update user set address='" + address + "' where id=" + id;
                    myDB.pstmt = (PreparedStatement) myDB.connection.prepareStatement(sql);
                    myDB.pstmt.executeUpdate();
                    System.out.println("成功更新一个住址");
                }
            }
            System.out.println("用户住址更新结束");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public String getAddress(long id){
        String address=null;
        try{
            String sql="select destination from record where user_id="+id+" and date_format(end_time,'%T')>='18:00:00' and date_format(end_time,'%T')<='24:00:00' group by destination order by count(destination) desc limit 1";
            myDB.pstmt=(PreparedStatement) myDB.connection.prepareStatement(sql);
            ResultSet rs=myDB.pstmt.executeQuery();
            if(rs.next()){
                address = rs.getString(1);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return address;
    }

    public void updateAddress(List<User> users, List<String> strs){
        try{
            int count=1;
            myDB.connection.setAutoCommit(false);
            for(int i=0;i<users.size();i++) {
                String sql = "update user set address='" + strs.get(i) + "' where id=" + users.get(i).getId();
                myDB.pstmt = (PreparedStatement) myDB.connection.prepareStatement(sql);
                myDB.pstmt.addBatch();
                if(count%5000==0){
                    myDB.pstmt.executeBatch();
                    myDB.connection.commit();
                }
                count++;
            }
            myDB.pstmt.executeBatch();
            myDB.connection.commit();
            myDB.Close();
            System.out.println("用户住址更新结束");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
