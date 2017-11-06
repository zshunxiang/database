package first;

import com.mysql.jdbc.PreparedStatement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import common.*;

public class StudentDao {

    private MyDB myDB=new MyDB();

    public int insertStudent(Student student){
        int i = 0;
        String sql = "insert into student (department,stuNo,name,sex,school,dormitory,charge) values(?,?,?,?,?,?,?)";
        try {
            myDB.pstmt = (PreparedStatement) myDB.connection.prepareStatement(sql);
            myDB.pstmt.setString(1, student.getDepartment());
            myDB.pstmt.setString(2, student.getStuNo());
            myDB.pstmt.setString(3, student.getName());
            myDB.pstmt.setString(4, student.getSex());
            myDB.pstmt.setString(5, student.getSchool());
            myDB.pstmt.setString(6, student.getDormitory());
            myDB.pstmt.setLong(7, student.getCharge());
            i = myDB.pstmt.executeUpdate();
            System.out.println("成功插入一条学生数据");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public List<String> getDepartment(String name){
        String sql = "select distinct(a.department) from student a,student b where a.dormitory=b.dormitory and b.name='"+name+"'";
        List<String> strings=new ArrayList<String>();
        try{
            myDB.pstmt=(PreparedStatement)myDB.connection.prepareStatement(sql);
            myDB.rs=myDB.pstmt.executeQuery();
            while (myDB.rs.next()){
                strings.add(myDB.rs.getString(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return strings;
    }

    public void update(String sql){
        try {
            myDB.pstmt = (PreparedStatement) myDB.connection.prepareStatement(sql);
            myDB.pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Student select(String name){
        String sql="select * from student where name='"+name+"'";
        Student student=new Student();
        try{
            myDB.pstmt=(PreparedStatement)myDB.connection.prepareStatement(sql);
            myDB.rs=myDB.pstmt.executeQuery();
            while (myDB.rs.next()) {
                student.setDepartment(myDB.rs.getString(0));
                student.setStuNo(myDB.rs.getString(1));
                student.setName(myDB.rs.getString(2));
                student.setSex(myDB.rs.getString(3));
                student.setSchool(myDB.rs.getString(4));
                student.setDormitory(myDB.rs.getString(5));
                student.setCharge(myDB.rs.getLong(6));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return student;
    }
}
