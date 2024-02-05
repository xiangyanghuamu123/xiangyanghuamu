package com.itheima.test;

import com.itheima.pojo.User;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

//读取一个excel中的内容
public class POIDemo3 {

    public static void main(String[] args) throws Exception {
//        用户名	手机号	省份	城市	工资	入职日期	出生日期	现住地址

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

//        有内容的workbook工作薄
        Workbook workbook = new XSSFWorkbook(new FileInputStream("D://用户导入测试数据.xlsx"));
//        获取到第一个工作表
        Sheet sheet = workbook.getSheetAt(0);

        int lastRowIndex = sheet.getLastRowNum();//当前sheet的最后一行的索引值
//        读取工作表中的内容
        Row row = null;
        User user = null;
        for (int i = 1; i <= lastRowIndex; i++) {
            row = sheet.getRow(i);
//            用户名	手机号	省份	城市	工资	入职日期	出生日期	现住地址
            String userName = row.getCell(0).getStringCellValue(); //用户名
            String phone = null; //手机号
            try {
                phone = row.getCell(1).getStringCellValue();
            } catch (Exception e) {
                phone = row.getCell(1).getNumericCellValue()+"";
            }
            String province = row.getCell(2).getStringCellValue(); //省份
            String city = row.getCell(3).getStringCellValue(); //城市
            Integer salary =( (Double)row.getCell(4).getNumericCellValue()).intValue(); //工资
            Date hireDate = simpleDateFormat.parse(row.getCell(5).getStringCellValue()) ; //入职日期
            Date birthDay = simpleDateFormat.parse(row.getCell(6).getStringCellValue()) ; //出生日期
            String address = row.getCell(7).getStringCellValue(); //现住地址

            System.out.println(userName+":"+phone+":"+province+":"+city+":"+salary+":"+hireDate+":"+birthDay+":"+address);
            user.setUserName(userName);
            user.setPhone(phone);
            user.setProvince(province);
            user.setCity(city);
            user.setSalary(salary);
            user.setHireDate(hireDate);
            user.setBirthday(birthDay);
            user.setAddress(address);
//            执行插入user方法
        }
    }
}
