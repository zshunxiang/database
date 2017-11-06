package common;

import first.Student;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadXLS {

    private static final String EXCEL_PATH= Constants.USEDIR+"分配方案.xls";

    public static List<Student> readXLS() throws IOException{
        InputStream in = new FileInputStream(EXCEL_PATH);
        HSSFWorkbook hssfWorkbook=new HSSFWorkbook(in);
        Student student;
        List<Student> students=new ArrayList<Student>();
        for(int numberSheet=0;numberSheet<hssfWorkbook.getNumberOfSheets();numberSheet++){
            HSSFSheet hssfSheet=hssfWorkbook.getSheetAt(numberSheet);
            if(hssfSheet == null){
                return null;
            }
            for(int rowNum=1;rowNum<=hssfSheet.getLastRowNum();rowNum++){
                HSSFRow hssfRow=hssfSheet.getRow(rowNum);
                if(hssfRow!=null){
                    student=new Student();
                    HSSFCell department=hssfRow.getCell(0);
                    HSSFCell stuNo=hssfRow.getCell(1);
                    HSSFCell name=hssfRow.getCell(2);
                    HSSFCell sex=hssfRow.getCell(3);
                    HSSFCell school=hssfRow.getCell(4);
                    HSSFCell dormitory=hssfRow.getCell(5);
                    HSSFCell charge=hssfRow.getCell(6);
                    student.setDepartment(getValue(department));
                    student.setStuNo(getValue(stuNo));
                    student.setName(getValue(name));
                    student.setSex(getValue(sex));
                    student.setSchool(getValue(school));
                    student.setDormitory(getValue(dormitory));
                    student.setCharge(Long.valueOf(getValue(charge)));
                    students.add(student);
                }
            }
        }
        return students;
    }

    @SuppressWarnings("static-access")
    private static String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellTypeEnum() == CellType.BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
         } else if (hssfCell.getCellTypeEnum() == CellType.NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(new Double(hssfCell.getNumericCellValue()).longValue());
         } else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }
}
