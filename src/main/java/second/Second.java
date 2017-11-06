package second;

import org.apache.commons.io.LineIterator;
import common.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

public class Second {

    private UserDao userDao=new UserDao();
    private BikeDao bikeDao=new BikeDao();
    private RecordDao recordDao=new RecordDao();

    public void inputData(){
        try{
            List<User> users= ReadTxt.readUser();
            for(User user:users){
                userDao.insertUser(user);
            }
            List<Bike> bikes= ReadTxt.readBike();
            for(Bike bike:bikes){
                bikeDao.insert(bike);
            }
            LineIterator lineIterator= ReadTxt.readRecord();
            recordDao.insertRecords(lineIterator);
            System.out.println("数据导入结束");
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件读取错误");
        }catch (ParseException e){
            e.printStackTrace();
            System.out.println("日期格式错误");
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("数据传输错误");
        }
    }

    public void forAddress(){
//        List<User> users=userDao.getUser();
//        List<String> address=recordDao.getAddress(users);
//        userDao.updateAddress(users,address);
//        users.forEach(user -> {
//            String address=recordDao.getAddress(user.getId());
//            if(address!=null){
//                userDao.updateAddress(address,user.getId());
//            }
//        });
//        for(User user:users){
//            userDao.updateAddress(user);
//        }
        userDao.updateAddress();
        System.out.println("用户住址更新成功");
    }

    public void forCharge(){
        String sql="update record set charge=case when timestampdiff(minute,start_time,end_time)<=30 then 1.0\n" +
                "  when timestampdiff(minute,start_time,end_time)>30 and timestampdiff(minute,start_time,end_time)<=60 then 1.5\n" +
                "  when timestampdiff(minute,start_time,end_time)>60 and timestampdiff(minute,start_time,end_time)<=90 then 3.0\n" +
                "  else 4.0 end";
        recordDao.update(sql);
        System.out.println("记录表费用结算完毕");
        String sql1="update user,(select user_id,sum(charge) as sum from record group by user_id) as a set user.money=(user.money-a.sum) where user.id=a.user_id";
        recordDao.update(sql1);
        System.out.println("用户表余额结算完毕");
    }

    public void forRepair(){
        String sql="select a.bike_id from (select bike_id,sum(timestampdiff(minute,start_time,end_time)) as time from record group by bike_id) as a where time>=12000";
        ResultSet rs=recordDao.select(sql);
        if(rs==null)
            System.out.println("上月无使用时间超过200个小时的单车");
    }
}
