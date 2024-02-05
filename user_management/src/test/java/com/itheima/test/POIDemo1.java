package com.itheima.test;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;

//创建一个低版本的excel,并且向其中的一个单元格中随便写一句话
public class POIDemo1 {

    public static void main(String[] args) throws Exception {
//        创建一个全新工作薄
        Workbook workbook = new HSSFWorkbook();
//        在工作薄中创建新的工作表
        Sheet sheet = workbook.createSheet("POI操作Excel");
//        在工作表中创建行
        Row row = sheet.createRow(0);
//        在行中创建单元格
        Cell cell = row.createCell(0);
//        在单元格中写入内容
        cell.setCellValue("这是我第一次玩POI");
        workbook.write(new FileOutputStream("d:/test.xls"));
    }
}
