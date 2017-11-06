package first;

import java.io.IOException;
import java.util.List;
import common.*;
public class First {

    private static ContactWayDao contactWayDao = new ContactWayDao();
    private static StudentDao studentDao = new StudentDao();

    public void inputData() {
        try {
            long start = System.currentTimeMillis();
            List<Student> students = ReadXLS.readXLS();
            for (Student student : students) {
                studentDao.insertStudent(student);
            }
            List<ContactWay> contactWays = ReadTxt.readTxt();
            for (ContactWay contactWay : contactWays) {
                contactWayDao.insertContactWay(contactWay);
            }
            long end = System.currentTimeMillis();
            System.out.println("导入数据所用时间:" + (end - start) / 1000 + "秒");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件读取错误");
        }
    }

    public void selectWXX() {
        List<String> departments = studentDao.getDepartment("王小星");
        System.out.print("\"王小星\"同学所在宿舍楼的所有院系为:");
        for (String str : departments) {
            System.out.print(str + ",");
        }
        System.out.println();
    }

    public void updateCharge(String dormitory,long add) {
        String sql = "update student set charge="+add+" where dormitory='"+dormitory+"'";
        studentDao.update(sql);
        System.out.println("费用修改成功");
    }

    public void exchangeDormitory(String deparment){
        String sql="update student s,(select distinct(dormitory),sex,charge from student b where b.department='"+deparment+"') as a set s.dormitory=a.dormitory,s.charge=a.charge where s.department='"+deparment+"' and s.sex!=a.sex;";
        long start = System.currentTimeMillis();
        studentDao.update(sql);
        long end = System.currentTimeMillis();
        System.out.println("软件学院男生女生交换宿舍成功,总耗时："+(end - start)+ "豪秒");
    }

}
