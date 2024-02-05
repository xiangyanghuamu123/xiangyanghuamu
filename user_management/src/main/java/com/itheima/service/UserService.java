package com.itheima.service;

import cn.afterturn.easypoi.csv.CsvExportUtil;
import cn.afterturn.easypoi.csv.entity.CsvExportParams;
import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.word.WordExportUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.ResourceMapper;
import com.itheima.mapper.UserMapper;
import com.itheima.pojo.Resource;
import com.itheima.pojo.User;
import com.itheima.utils.EntityUtils;
import com.itheima.utils.ExcelExportEngine;
import com.opencsv.CSVWriter;
import com.zaxxer.hikari.HikariDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;


import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ResourceMapper resourceMapper;


    public User findById(Long id) {
//        根据id查询用户，并且用户中附带公共用品数据
        User user = userMapper.selectByPrimaryKey(id);
//        再查询办公用品数据

        Resource resource = new Resource();
        resource.setUserId(id);
        List<Resource> resourceList = resourceMapper.select(resource);
        user.setResourceList(resourceList);
        return user;
    }

    //    查询所有
    public List<User> findAll() {
        return userMapper.selectAll();
    }

    //    分页查询
    public List<User> findPage(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);  //开启分页
        Page<User> userPage = (Page<User>) userMapper.selectAll(); //实现查询
        return userPage.getResult();
    }

    /* public void downLoadXlsByJxl(HttpServletResponse response) throws Exception {
 //   编号 姓名 手机号 入职日期 现住址
         ServletOutputStream outputStream = response.getOutputStream();
 //        创建了一个全新的工作薄
         WritableWorkbook workbook = Workbook.createWorkbook(outputStream);
 //        创建了一个工作表
         WritableSheet sheet = workbook.createSheet("一个JXL入门", 0);

 //        调整列宽
         sheet.setColumnView(0,5); //  第一个参数：列的索引值  第二个参数：1代表一个标准字母的宽度
         sheet.setColumnView(1,8); //  第一个参数：列的索引值  第二个参数：1代表一个标准字母的宽度
         sheet.setColumnView(2,15); //  第一个参数：列的索引值  第二个参数：1代表一个标准字母的宽度
         sheet.setColumnView(3,15); //  第一个参数：列的索引值  第二个参数：1代表一个标准字母的宽度
         sheet.setColumnView(4,30); //  第一个参数：列的索引值  第二个参数：1代表一个标准字母的宽度

 //        处理标题开始
         String[] titles = new String[]{"编号","姓名","手机号","入职日期","现住址"};
         Label label = null;
         for (int i = 0; i < titles.length; i++) {
             label = new Label(i,0,titles[i]); //列脚标, 行脚标, 单元格中的内容
             sheet.addCell(label);
         }
 //        处理标题结束
 //        查询所有用户的数据
         List<User> userList = userMapper.selectAll();

         int rowIndex = 1;
         for (User user : userList) {
             label = new Label(0,rowIndex,user.getId().toString()); //列脚标, 行脚标, 单元格中的内容 编号
             sheet.addCell(label);

             label = new Label(1,rowIndex,user.getUserName()); //列脚标, 行脚标, 单元格中的内容 姓名
             sheet.addCell(label);

             label = new Label(2,rowIndex,user.getPhone()); //列脚标, 行脚标, 单元格中的内容 手机号
             sheet.addCell(label);

             label = new Label(3,rowIndex,simpleDateFormat.format( user.getHireDate())); //列脚标, 行脚标, 单元格中的内容 入职日期
             sheet.addCell(label);

             label = new Label(4,rowIndex,user.getAddress()); //列脚标, 行脚标, 单元格中的内容 现住址
             sheet.addCell(label);
             rowIndex++;
         }
 //        文件的导出 一个流（outputStream）两个头（文件的打开方式 in-line attachment，文件的下载时mime类型） application/vnd.ms-excel
         String filename="一个JXL入门.xls";
         response.setHeader("content-disposition","attachment;filename="+ new String(filename.getBytes(),"ISO8859-1"));
         response.setContentType("application/vnd.ms-excel");
         workbook.write();
         workbook.close();
         outputStream.close();
     }
 */
//   上传用户数据
    public void uploadExcel(MultipartFile file) throws Exception {
//        有内容的workbook工作薄
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
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
                phone = row.getCell(1).getNumericCellValue() + "";
            }
            String province = row.getCell(2).getStringCellValue(); //省份
            String city = row.getCell(3).getStringCellValue(); //城市
            Integer salary = ((Double) row.getCell(4).getNumericCellValue()).intValue(); //工资
            Date hireDate = simpleDateFormat.parse(row.getCell(5).getStringCellValue()); //入职日期
            Date birthDay = simpleDateFormat.parse(row.getCell(6).getStringCellValue()); //出生日期
            String address = row.getCell(7).getStringCellValue(); //现住地址

            System.out.println(userName + ":" + phone + ":" + province + ":" + city + ":" + salary + ":" + hireDate + ":" + birthDay + ":" + address);

            user = new User();

            user.setUserName(userName);
            user.setPhone(phone);
            user.setProvince(province);
            user.setCity(city);
            user.setSalary(salary);
            user.setHireDate(hireDate);
            user.setBirthday(birthDay);
            user.setAddress(address);
//            执行插入user方法
            userMapper.insert(user);
        }


    }

    //    使用POI导出用户列表数据
    public void downLoadXlsxByPoi(HttpServletResponse response) throws Exception {
        /*导出用户数据基本思路：
        1、创建一个全新的工作薄
        2、创建全新的工作表
        3、处理固定的标题  编号 姓名  手机号 入职日期 现住址
        4、从第二行开始循环遍历 向单元格中放入数据*/
//        1、创建一个全新的工作薄
        Workbook workbook = new XSSFWorkbook("");
//        2、创建全新的工作表
        Sheet sheet = workbook.createSheet("用户数据");
//        设置列宽
        sheet.setColumnWidth(0, 5 * 256);  // 1代表的是一个标准字母宽度的256分之一
        sheet.setColumnWidth(1, 8 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 30 * 256);

//         3、处理固定的标题  编号 姓名  手机号 入职日期 现住址
        String[] titles = new String[]{"编号", "姓名", "手机号", "入职日期", "现住址"};
        Row titleRow = sheet.createRow(0);
        Cell cell = null;
        for (int i = 0; i < 5; i++) {
            cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
//        4、从第二行开始循环遍历 向单元格中放入数据
        List<User> userList = userMapper.selectAll();
        int rowIndex = 1;
        Row row = null;
        for (User user : userList) {
            row = sheet.createRow(rowIndex);
//            编号 姓名  手机号 入职日期 现住址
            cell = row.createCell(0);
            cell.setCellValue(user.getId());

            cell = row.createCell(1);
            cell.setCellValue(user.getUserName());

            cell = row.createCell(2);
            cell.setCellValue(user.getPhone());

            cell = row.createCell(3);
            cell.setCellValue(simpleDateFormat.format(user.getHireDate()));

            cell = row.createCell(4);
            cell.setCellValue(user.getAddress());

            rowIndex++;
        }

//        一个流两个头
        String filename = "员工数据.xlsx";
        response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        workbook.write(response.getOutputStream());

    }

    //    使用POI导出用户列表数据--带样式
    public void downLoadXlsxByPoiWithCellStyle(HttpServletResponse response) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("有样式的数据");

        sheet.setColumnWidth(0, 5 * 256);
        sheet.setColumnWidth(1, 8 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 30 * 256);

//        需求：1、边框线：全边框  2、行高：42   3、合并单元格：第1行的第1个单元格到第5个单元格 4、对齐方式：水平垂直都要居中 5、字体：黑体18号字
        CellStyle bigTitleRowCellStyle = workbook.createCellStyle();
        bigTitleRowCellStyle.setBorderBottom(BorderStyle.THIN); //下边框  BorderStyle.THIN 细线
        bigTitleRowCellStyle.setBorderLeft(BorderStyle.THIN);  //左边框
        bigTitleRowCellStyle.setBorderRight(BorderStyle.THIN);  //右边框
        bigTitleRowCellStyle.setBorderTop(BorderStyle.THIN);  //上边框
//        对齐方式： 水平对齐  垂直对齐
        bigTitleRowCellStyle.setAlignment(HorizontalAlignment.CENTER); //水平居中对齐
        bigTitleRowCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中对齐
//        创建字体
        Font font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 18);
//        把字体放入到样式中
        bigTitleRowCellStyle.setFont(font);

        Row bigTitleRow = sheet.createRow(0);
        bigTitleRow.setHeightInPoints(42); //设置行高
        for (int i = 0; i < 5; i++) {
            Cell cell = bigTitleRow.createCell(i);
            cell.setCellStyle(bigTitleRowCellStyle);
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4)); //int firstRow 起始行, int lastRow 结束行, int firstCol 开始列, int lastCol 结束列
//        向单元格中放入一句话
        sheet.getRow(0).getCell(0).setCellValue("用户信息数据");


//        小标题的样式
        CellStyle littleTitleRowCellStyle = workbook.createCellStyle();
//        样式的克隆
        littleTitleRowCellStyle.cloneStyleFrom(bigTitleRowCellStyle);
//        创建字体  宋体12号字加粗
        Font littleFont = workbook.createFont();
        littleFont.setFontName("宋体");
        littleFont.setFontHeightInPoints((short) 12);
        littleFont.setBold(true);
//        把字体放入到样式中
        littleTitleRowCellStyle.setFont(littleFont);


//        内容的样式
        CellStyle contentRowCellStyle = workbook.createCellStyle();
//        样式的克隆
        contentRowCellStyle.cloneStyleFrom(littleTitleRowCellStyle);
        contentRowCellStyle.setAlignment(HorizontalAlignment.LEFT);
//        创建字体  宋体12号字加粗
        Font contentFont = workbook.createFont();
        contentFont.setFontName("宋体");
        contentFont.setFontHeightInPoints((short) 11);
        contentFont.setBold(false);
//        把字体放入到样式中
        contentRowCellStyle.setFont(contentFont);

//        编号	姓名	手机号	入职日期	现住址

        Row titleRow = sheet.createRow(1);
        titleRow.setHeightInPoints(31.5F);
        String[] titles = new String[]{"编号", "姓名", "手机号", "入职日期", "现住址"};
        for (int i = 0; i < 5; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
            cell.setCellStyle(littleTitleRowCellStyle);
        }

        List<User> userList = userMapper.selectAll();
        int rowIndex = 2;
        Row row = null;
        Cell cell = null;
        for (User user : userList) {
            row = sheet.createRow(rowIndex);
            cell = row.createCell(0);
            cell.setCellStyle(contentRowCellStyle);
            cell.setCellValue(user.getId());

            cell = row.createCell(1);
            cell.setCellStyle(contentRowCellStyle);
            cell.setCellValue(user.getUserName());

            cell = row.createCell(2);
            cell.setCellStyle(contentRowCellStyle);
            cell.setCellValue(user.getPhone());

            cell = row.createCell(3);
            cell.setCellStyle(contentRowCellStyle);
            cell.setCellValue(simpleDateFormat.format(user.getHireDate()));

            cell = row.createCell(4);
            cell.setCellStyle(contentRowCellStyle);
            cell.setCellValue(user.getAddress());

            rowIndex++;
        }

        String filename = "员工数据.xlsx";
        response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        workbook.write(response.getOutputStream());

    }

    public void downLoadXlsxByPoiWithTemplate(HttpServletResponse response) throws Exception {
//        1、获取到模板
        File rootFile = new File(ResourceUtils.getURL("classpath:").getPath()); //获取项目的根目录
        File templateFile = new File(rootFile, "/excel_template/userList.xlsx");
        Workbook workbook = new XSSFWorkbook(templateFile);
//       2、查询所有的用户数据
        List<User> userList = userMapper.selectAll();
//       3、放入到模板中
        Sheet sheet = workbook.getSheetAt(0);

//        获取准备好的内容单元格的样式 第2个sheet的第一行的第一个单元格中
        CellStyle contentRowCellStyle = workbook.getSheetAt(1).getRow(0).getCell(0).getCellStyle();
        int rowIndex = 2;
        Row row = null;
        Cell cell = null;
        for (User user : userList) {
            row = sheet.createRow(rowIndex);
            row.setHeightInPoints(15);

            cell = row.createCell(0);
            cell.setCellStyle(contentRowCellStyle);
            cell.setCellValue(user.getId());

            cell = row.createCell(1);
            cell.setCellStyle(contentRowCellStyle);
            cell.setCellValue(user.getUserName());

            cell = row.createCell(2);
            cell.setCellStyle(contentRowCellStyle);
            cell.setCellValue(user.getPhone());

            cell = row.createCell(3);
            cell.setCellStyle(contentRowCellStyle);
            cell.setCellValue(simpleDateFormat.format(user.getHireDate()));

            cell = row.createCell(4);
            cell.setCellStyle(contentRowCellStyle);
            cell.setCellValue(user.getAddress());
            rowIndex++;
        }
//        把第二个sheet删除
        workbook.removeSheetAt(1);

//        4、导出文件
        String filename = "员工数据.xlsx";
        response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        workbook.write(response.getOutputStream());
    }

    //    导出用户的详细信息--使用模板
    public void downloadUserInfoByTemplate(Long id, HttpServletResponse response) throws Exception {
        //        1、获取到模板
        File rootFile = new File(ResourceUtils.getURL("classpath:").getPath()); //获取项目的根目录
        File templateFile = new File(rootFile, "/excel_template/userInfo.xlsx");
        Workbook workbook = new XSSFWorkbook(templateFile);
        Sheet sheet = workbook.getSheetAt(0);
//        2、根据ID获取某一个用户数据
        User user = userMapper.selectByPrimaryKey(id);
//        3、把用户数据放入到模板中

//        用户名  第2行第2列
        sheet.getRow(1).getCell(1).setCellValue(user.getUserName());
//        手机号  第3行第2列
        sheet.getRow(2).getCell(1).setCellValue(user.getPhone());
//        生日   第4行第2列
        sheet.getRow(3).getCell(1).setCellValue(simpleDateFormat.format(user.getBirthday()));
//        工资   第5行第2列
        sheet.getRow(4).getCell(1).setCellValue(user.getSalary());
//        入职日期  第6行第2列
        sheet.getRow(5).getCell(1).setCellValue(simpleDateFormat.format(user.getHireDate()));
//        省份   第7行第2列
        sheet.getRow(6).getCell(1).setCellValue(user.getProvince());
//        现住址  第8行第2列
        sheet.getRow(7).getCell(1).setCellValue(user.getAddress());
//        司龄  第6行第4列          使用公式稍后处理 =CONCATENATE(DATEDIF(B6,TODAY(),"Y"),"年",DATEDIF(B6,TODAY(),"YM"),"个月")
//        sheet.getRow(5).getCell(3).setCellFormula("CONCATENATE(DATEDIF(B6,TODAY(),\"Y\"),\"年\",DATEDIF(B6,TODAY(),\"YM\"),\"个月\")");
//        城市  第7行第4列
        sheet.getRow(6).getCell(3).setCellValue(user.getCity());
//        照片的位置
//        开始处理照片
//        先创建一个字节输出流
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        读取图片 放入了一个带有缓存区的图片类中
        BufferedImage bufferedImage = ImageIO.read(new File(rootFile, user.getPhoto()));
//       把图片写入到了字节输出流中
//        user.getPhoto()
        String extName = user.getPhoto().substring( user.getPhoto().lastIndexOf(".")+1).toUpperCase();
        ImageIO.write(bufferedImage,extName,byteArrayOutputStream);
//        Patriarch 控制图片的写入 和ClientAnchor 指定图片的位置
        Drawing patriarch = sheet.createDrawingPatriarch();
//        指定图片的位置         开始列3 开始行2   结束列4  结束行5
//        偏移的单位：是一个英式公制的单位  1厘米=360000
        ClientAnchor anchor = new XSSFClientAnchor(0,0,0,0,2,1,4,5);
//        开始把图片写入到sheet指定的位置
        int format = 0;
        switch (extName){
            case "JPG":{
                format = XSSFWorkbook.PICTURE_TYPE_JPEG;
            }
            case "JPEG":{
                format = XSSFWorkbook.PICTURE_TYPE_JPEG;
            }
            case "PNG":{
                format = XSSFWorkbook.PICTURE_TYPE_PNG;
            }
        }

        patriarch.createPicture(anchor,workbook.addPicture(byteArrayOutputStream.toByteArray(),format));
//       处理照片结束

//        4、导出文件
        String filename = "员工(" + user.getUserName() + ")详细数据.xlsx";
        response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        workbook.write(response.getOutputStream());


    }

    public void downloadUserInfoByTemplate2(Long id, HttpServletResponse response) throws Exception {
//       1、获取模板

        File rootFile = new File(ResourceUtils.getURL("classpath:").getPath()); //获取项目的根目录
        File templateFile = new File(rootFile, "/excel_template/userInfo2.xlsx");
        Workbook workbook = new XSSFWorkbook(templateFile);
        //        2、根据ID获取某一个用户数据
        User user = userMapper.selectByPrimaryKey(id);
//        3、通过自定义的引擎放入数据
        workbook = ExcelExportEngine.writeToExcel(user,workbook, rootFile.getPath()+ user.getPhoto());
//       4 导出文件
        String filename = "员工(" + user.getUserName() + ")详细数据.xlsx";
        response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        workbook.write(response.getOutputStream());
    }

//    百万数据的导出 1、肯定使用高版本的excel 2、使用sax方式解析Excel（XML）
//     限制：1、不能使用模板 2、不能使用太多的样式
    public void downLoadMillion(HttpServletResponse response) throws Exception {
//        指定使用的是sax方式解析
        Workbook workbook = new SXSSFWorkbook();  //sax方式就是逐行解析
//        Workbook workbook = new XSSFWorkbook(); //dom4j的方式
//        导出500W条数据 不可能放到同一个sheet中 规定：每个sheet不能超过100W条数据
        int page = 1;
        int num = 0 ;// 记录了处理数据的个数
        int rowIndex = 1; //记录的是每个sheet的行索引
        Row row = null;
        Sheet sheet = null;
        while (true){
            List<User> userList = this.findPage(page, 100000);
            if(CollectionUtils.isEmpty(userList)){
                break; //用户数据为空 跳出循环
            }
//           0   1000000  2000000  3000000  4000000  5000000
            if(num%1000000==0){  //表示应该创建新的标题
                sheet = workbook.createSheet("第"+((num/1000000)+1)+"个工作表");
                rowIndex = 1; //每个sheet中的行索引重置
//            设置小标题
//            编号	姓名	手机号	入职日期	现住址
                String[] titles = new String[]{"编号","姓名","手机号","入职日期","现住址"};
                Row titleRow = sheet.createRow(0);
                for (int i = 0; i < 5; i++) {
                    titleRow.createCell(i).setCellValue(titles[i]);
                }
            }
            for (User user : userList) {
                row = sheet.createRow(rowIndex);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getUserName());
                row.createCell(2).setCellValue(user.getPhone());
                row.createCell(3).setCellValue(simpleDateFormat.format(user.getHireDate()));
                row.createCell(4).setCellValue(user.getAddress());

                rowIndex++;
                num++;
            }
            page++; //当前页码加1
        }

        String filename = "百万用户数据的导出.xlsx";
        response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        workbook.write(response.getOutputStream());

    }
//    使用csv文件导出百万数据
    public void downLoadCSV(HttpServletResponse response) throws Exception{
        ServletOutputStream outputStream = response.getOutputStream();
        String filename = "百万用户数据的导出.csv";
        response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1"));
        response.setContentType("text/csv");
        CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(outputStream,"utf-8"));
//        写入了小标题数据
        String[] titles = new String[]{"编号","姓名","手机号","入职日期","现住址"};
        csvWriter.writeNext(titles);

        int page = 1;
        while (true){
            List<User> userList = this.findPage(page, 200000);
            if(CollectionUtils.isEmpty(userList)){
                break;
            }
            for (User user : userList) {
                csvWriter.writeNext(new String[]{user.getId().toString(),user.getUserName(),
                        user.getPhone(),simpleDateFormat.format(user.getHireDate()),user.getAddress()});
            }
            page++;
            csvWriter.flush();
        }
        csvWriter.close();
    }
//    下载用户的合同文档
    public void downloadContract(Long id,HttpServletResponse response) throws Exception {
//        1、读取到模板
        File rootFile = new File(ResourceUtils.getURL("classpath:").getPath()); //获取项目的根目录
        File templateFile = new File(rootFile, "/word_template/contract_template.docx");
        XWPFDocument word = new XWPFDocument(new FileInputStream(templateFile));
//        2、查询当前用户User--->map
        User user = this.findById(id);
        Map<String,String> params = new HashMap<>();
        params.put("userName",user.getUserName());
        params.put("hireDate",simpleDateFormat.format(user.getHireDate()));
        params.put("address",user.getAddress());
//        3、替换数据
//         处理正文开始
        List<XWPFParagraph> paragraphs = word.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String text = run.getText(0);
                for (String key : params.keySet()) {
                    if(text.contains(key)){
                        run.setText(text.replaceAll(key,params.get(key)),0);
                    }
                }
            }
        }
//         处理正文结束

//      处理表格开始     名称	价值	是否需要归还	照片
        List<Resource> resourceList = user.getResourceList(); //表格中需要的数据
        XWPFTable xwpfTable = word.getTables().get(0);

        XWPFTableRow row = xwpfTable.getRow(0);
        int rowIndex = 1;
        for (Resource resource : resourceList) {
            //        添加行
//            xwpfTable.addRow(row);
            copyRow(xwpfTable,row,rowIndex);
            XWPFTableRow row1 = xwpfTable.getRow(rowIndex);
            row1.getCell(0).setText(resource.getName());
            row1.getCell(1).setText(resource.getPrice().toString());
            row1.getCell(2).setText(resource.getNeedReturn()?"需求":"不需要");

            File imageFile = new File(rootFile,"/static"+resource.getPhoto());
            setCellImage(row1.getCell(3),imageFile);
            rowIndex++;
        }
//     处理表格开始结束
//        4、导出word
        String filename = "员工(" + user.getUserName() + ")合同.docx";
        response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        word.write(response.getOutputStream());
    }

//    向单元格中写入图片
    private void setCellImage(XWPFTableCell cell, File imageFile) {

        XWPFRun run = cell.getParagraphs().get(0).createRun();
//        InputStream pictureData, int pictureType, String filename, int width, int height
        try(FileInputStream inputStream = new FileInputStream(imageFile)) {
            run.addPicture(inputStream,XWPFDocument.PICTURE_TYPE_JPEG,imageFile.getName(), Units.toEMU(100),Units.toEMU(50));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //    用于深克隆行
    private void copyRow(XWPFTable xwpfTable, XWPFTableRow sourceRow, int rowIndex) {
        XWPFTableRow targetRow = xwpfTable.insertNewTableRow(rowIndex);
        targetRow.getCtRow().setTrPr(sourceRow.getCtRow().getTrPr());
//        获取源行的单元格
        List<XWPFTableCell> cells = sourceRow.getTableCells();
        if(CollectionUtils.isEmpty(cells)){
            return;
        }
        XWPFTableCell targetCell = null;
        for (XWPFTableCell cell : cells) {
            targetCell = targetRow.addNewTableCell();
//            附上单元格的样式
//            单元格的属性
            targetCell.getCTTc().setTcPr(cell.getCTTc().getTcPr());
            targetCell.getParagraphs().get(0).getCTP().setPPr(cell.getParagraphs().get(0).getCTP().getPPr());
        }
    }
//    使用EasyPOI方式导出excel
    public void downLoadWithEasyPOI(HttpServletResponse response) throws Exception {
        ExportParams exportParams = new ExportParams("员工信息列表","数据", ExcelType.XSSF);
        List<User> userList = userMapper.selectAll();
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, User.class, userList);

        String filename = "用户数据的导出.xlsx";
        response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        workbook.write(response.getOutputStream());
    }

    public void uploadExcelWithEasyPOI(MultipartFile file) throws Exception {
        ImportParams importParams = new ImportParams();
        importParams.setNeedSave(false);
        importParams.setTitleRows(1);
        importParams.setHeadRows(1);
        List<User> userList = ExcelImportUtil.importExcel(file.getInputStream(), User.class, importParams);
        for (User user : userList) {
            user.setId(null);
            userMapper.insert(user);
        }

    }

    public void downloadUserInfoByEasyPOI(Long id, HttpServletResponse response) throws Exception{
        File rootFile = new File(ResourceUtils.getURL("classpath:").getPath()); //获取项目的根目录
        File templateFile = new File(rootFile, "/excel_template/userInfo3.xlsx");
//        Workbook workbook = new XSSFWorkbook(templateFile);
        TemplateExportParams exportParams = new TemplateExportParams(templateFile.getPath(),true);

        User user = userMapper.selectByPrimaryKey(id);
        Map<String, Object> map = EntityUtils.entityToMap(user);
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setUrl(user.getPhoto());
        imageEntity.setColspan(2); //占用多少列
        imageEntity.setRowspan(4); //占用多少行

        map.put("photo",imageEntity);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams,map);

        String filename = "用户数据.xlsx";
        response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        workbook.write(response.getOutputStream());
    }

    public void downLoadCSVWithEasyPOI(HttpServletResponse response) throws Exception{
        ServletOutputStream outputStream = response.getOutputStream();
        String filename = "百万用户数据的导出.csv";
        response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1"));
        response.setContentType("text/csv");

        CsvExportParams csvExportParams = new CsvExportParams();
        csvExportParams.setExclusions(new String[]{"照片"});

        List<User> userList = userMapper.selectAll();
        CsvExportUtil.exportCsv(csvExportParams,User.class,userList,outputStream);

    }

    public void downloadContractByEasyPOI(Long id, HttpServletResponse response) throws Exception {
//        获取模板文档
        File rootFile = new File(ResourceUtils.getURL("classpath:").getPath()); //获取项目的根目录
        File templateFile = new File(rootFile, "/word_template/contract_template2.docx");
//        准备数据
        User user = this.findById(id);
        Map<String,Object> params = new HashMap<>();
        params.put("userName",user.getUserName());
        params.put("hireDate",simpleDateFormat.format(user.getHireDate()));
        params.put("address",user.getAddress());

        ImageEntity imageEntityContent = new ImageEntity();
        imageEntityContent.setUrl(rootFile.getPath()+user.getPhoto());
        imageEntityContent.setWidth(100);
        imageEntityContent.setHeight(50);
        params.put("photo",imageEntityContent);

        List<Map> resourceMapList = new ArrayList<>();
        Map<String,Object> map = null;
        for (Resource resource : user.getResourceList()) {
            map = new HashMap<>();
            map.put("name",resource.getName());
            map.put("price",resource.getPrice());
            map.put("needReturn",resource.getNeedReturn());
//            map.put("photo",resource.getPhoto()); //需要处理

            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setUrl(rootFile.getPath()+"/static/"+resource.getPhoto());
            map.put("photo",imageEntity);

            resourceMapList.add(map);
        }
        params.put("resourceList",resourceMapList);

//        模板文档结合数据
        XWPFDocument word = WordExportUtil.exportWord07(templateFile.getPath(), params);

//
        String filename = "员工(" + user.getUserName() + ")合同.docx";
        response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1"));
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        word.write(response.getOutputStream());
    }


    @Autowired
    private HikariDataSource hikariDataSource;

    public void downLoadPDF(HttpServletResponse response) throws Exception {
//        1、获取模板文件
        File rootFile = new File(ResourceUtils.getURL("classpath:").getPath()); //获取项目的根目录
        File templateFile = new File(rootFile, "/pdf_template/userList_db.jasper");
//       2、准备数据库的链接
        Map params = new HashMap();
        JasperPrint jasperPrint = JasperFillManager.fillReport(new FileInputStream(templateFile), params, hikariDataSource.getConnection());

        ServletOutputStream outputStream = response.getOutputStream();
        String filename="用户列表数据.pdf";
        response.setContentType("application/pdf");
        response.setHeader("content-disposition","attachment;filename="+new String(filename.getBytes(),"iso8859-1"));

        JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);

    }

    public void downLoadPDF2(HttpServletResponse response) throws Exception {
//        1、获取模板文件
        File rootFile = new File(ResourceUtils.getURL("classpath:").getPath()); //获取项目的根目录
        File templateFile = new File(rootFile, "/pdf_template/userList2.jasper");
//       2、准备数据库的链接
        Map params = new HashMap();
        Example example = new Example(User.class);
        example.setOrderByClause("province");
        List<User> userList = userMapper.selectByExample(example);
//        给userList中的每个user赋hireDateStr的值
        userList = userList.stream().map(user->{
            user.setHireDateStr(simpleDateFormat.format(user.getHireDate()));
            return user;
        }).collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(userList);

        JasperPrint jasperPrint = JasperFillManager.fillReport(new FileInputStream(templateFile), params,dataSource);

        ServletOutputStream outputStream = response.getOutputStream();
        String filename="用户列表数据.pdf";
        response.setContentType("application/pdf");
        response.setHeader("content-disposition","attachment;filename="+new String(filename.getBytes(),"iso8859-1"));

        JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);


    }

    public void downloadUserInfoByPDF(Long id, HttpServletResponse response) throws Exception {
        //        1、获取模板文件
        File rootFile = new File(ResourceUtils.getURL("classpath:").getPath()); //获取项目的根目录
        File templateFile = new File(rootFile, "/pdf_template/userInfo.jasper");
//       2、准备数据库的链接
        User user = userMapper.selectByPrimaryKey(id);
        Map<String, Object> params = EntityUtils.entityToMap(user);
        params.put("salary",user.getSalary().toString());
        params.put("photo",rootFile.getPath()+user.getPhoto());
        JasperPrint jasperPrint = JasperFillManager.fillReport(new FileInputStream(templateFile), params,new JREmptyDataSource());

        ServletOutputStream outputStream = response.getOutputStream();
        String filename="用户详细数据.pdf";
        response.setContentType("application/pdf");
        response.setHeader("content-disposition","attachment;filename="+new String(filename.getBytes(),"iso8859-1"));

        JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);


    }

//    private Connection getCon() throws Exception{
//        Class.forName("com.mysql.jdbc.Driver");
//        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/report_manager_db", "root", "root");
//        return connection;
//    }





}
