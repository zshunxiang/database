package common;

import first.ContactWay;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import second.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReadTxt {

    private static final String TEL_PATH= Constants.USEDIR+"电话.txt";
    private static final String BIKE_PATH=Constants.USEDIR+"bike.txt";
    private static final String RECORD_PATH=Constants.USEDIR+"record.txt";
    private static final String USER_PATH=Constants.USEDIR+"user.txt";

    public static List<ContactWay> readTxt() throws IOException{
        File file = new File(TEL_PATH);
        LineIterator lineIterator = FileUtils.lineIterator(file, "utf-8");
        List<ContactWay> contactWays=new ArrayList<ContactWay>();
        ContactWay contactWay;
        while (lineIterator.hasNext()) {
            contactWay=new ContactWay();
            String line = lineIterator.nextLine();
            if(line.indexOf(";")==-1){
                continue;
            }
            // 行数据转换成数组
            String[] custArray = line.split(";");
            contactWay.setDormitory(custArray[0]);
            contactWay.setTel(custArray[1]);
            contactWays.add(contactWay);
        }
        return contactWays;
    }

    public static List<User> readUser() throws IOException{
        File file = new File(USER_PATH);
        LineIterator lineIterator = FileUtils.lineIterator(file, "utf-8");
        List<User> users=new ArrayList<User>();
        User user;
        while (lineIterator.hasNext()) {
            user=new User();
            String line = lineIterator.nextLine();
            if(line.indexOf(";")==-1){
                continue;
            }
            // 行数据转换成数组
            String[] custArray = line.split(";");
            user.setId(Long.valueOf(custArray[0]));
            user.setName(custArray[1]);
            user.setTel(custArray[2]);
            user.setMoney(Double.valueOf(custArray[3]));
            users.add(user);
        }
        return users;
    }

    public static List<Bike> readBike() throws IOException{
        File file=new File(BIKE_PATH);
        LineIterator lineIterator = FileUtils.lineIterator(file,"utf-8");
        List<Bike> bikes=new ArrayList<Bike>();
        Bike bike;
        while (lineIterator.hasNext()){
            bike=new Bike();
            String line = lineIterator.nextLine();
            bike.setId(Long.valueOf(line));
            bikes.add(bike);
        }
        return bikes;
    }

    public static LineIterator readRecord() throws IOException,ParseException{
        File file=new File(RECORD_PATH);
        LineIterator lineIterator = FileUtils.lineIterator(file,"utf-8");
        return lineIterator;
    }

    public static String format(String s) throws ParseException{
        s=s.replace('-',' ');
        s=s.replace('/','-');
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=simpleDateFormat.parse(s);
        return simpleDateFormat.format(date);
    }
}
