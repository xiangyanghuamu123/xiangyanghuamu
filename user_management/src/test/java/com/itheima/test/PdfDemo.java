package com.itheima.test;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class PdfDemo {

    public static void main(String[] args) throws Exception {
        String filePath = "d:\\test01.jasper"; //模板文件
        FileInputStream inputStream = new FileInputStream(filePath);
        Map params = new HashMap();
        params.put("userNameP","张三");
        params.put("phoneP","13800000000");
        JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, new JREmptyDataSource());
        JasperExportManager.exportReportToPdfStream(jasperPrint,new FileOutputStream("d:\\test01.pdf"));
    }
}
