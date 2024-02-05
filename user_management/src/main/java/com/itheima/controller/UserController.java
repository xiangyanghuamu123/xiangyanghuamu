package com.itheima.controller;

import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import org.apache.poi.ss.usermodel.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Font;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/findPage")
    public List<User>  findPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "10") Integer pageSize){
        return userService.findPage(page,pageSize);
    }

    @GetMapping(value = "/downLoadXlsByJxl",name = "使用jxl导出excel")
    public void downLoadXlsByJxl(HttpServletResponse response) throws Exception{
//        userService.downLoadXlsByJxl(response);
    }
    @PostMapping(value = "/uploadExcel",name = "上传用户数据")
    public void uploadExcel(MultipartFile file) throws Exception{
//        userService.uploadExcel(file);
        userService.uploadExcelWithEasyPOI(file);
    }

    @GetMapping(value = "/downLoadXlsxByPoi",name = "使用POI导出用户数据")
    public void downLoadXlsxByPoi(HttpServletResponse response) throws Exception{
//        userService.downLoadXlsxByPoi(response);
//        userService.downLoadXlsxByPoiWithCellStyle(response);
        userService.downLoadXlsxByPoiWithTemplate(response);
    }

    @GetMapping(value = "/download",name = "使用POI导出用户详细数据")
    public void downloadUserInfoByTemplate(Long id,HttpServletResponse response) throws Exception{
//        userService.downloadUserInfoByTemplate(id,response);
//        userService.downloadUserInfoByTemplate2(id,response);
//        userService.downloadUserInfoByEasyPOI(id,response);
        userService.downloadUserInfoByPDF(id,response);
    }

    @GetMapping(value = "/downLoadMillion",name = "导出百万数据")
    public void downLoadMillion(HttpServletResponse response) throws Exception{
        userService.downLoadMillion(response);
    }

    @GetMapping(value = "/downLoadCSV",name = "使用csv文件导出百万数据")
    public void downLoadCSV(HttpServletResponse response) throws Exception{
//        userService.downLoadCSV(response);
        userService.downLoadCSVWithEasyPOI(response);
    }

    @GetMapping(value = "/{id}",name = "根据id查询用户数据")
    public User findById(@PathVariable("id") Long id) throws Exception{
       return userService.findById(id);
    }

    @GetMapping(value = "/downloadContract",name = "下载用户的合同文档")
    public void downloadContract(Long id,HttpServletResponse response) throws Exception{
//       userService.downloadContract(id,response);
       userService.downloadContractByEasyPOI(id,response);
    }
    @GetMapping(value = "/downLoadWithEasyPOI",name = "使用EasyPOI方式导出excel")
    public void downLoadWithEasyPOI(HttpServletResponse response) throws Exception{
       userService.downLoadWithEasyPOI(response);
    }

    @GetMapping(value = "/downLoadPDF",name = "导出用户数据到PDF中")
    public void downLoadPDF(HttpServletResponse response) throws Exception{
//        userService.downLoadPDF(response);
        userService.downLoadPDF2(response);
    }
    @GetMapping(value = "/jfreeChart")
    public void jfreeChart(HttpServletResponse response) throws Exception{
        //        需求：每年各部门入职的人数
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(20,"技术部","2011年");
        dataset.addValue(25,"技术部","2012年");
        dataset.addValue(26,"技术部","2013年");
        dataset.addValue(28,"技术部","2014年");

        dataset.addValue(10,"销售部","2011年");
        dataset.addValue(15,"销售部","2012年");
        dataset.addValue(8,"销售部","2013年");
        dataset.addValue(18,"销售部","2014年");

        dataset.addValue(2,"人事部","2011年");
        dataset.addValue(4,"人事部","2012年");
        dataset.addValue(1,"人事部","2013年");
        dataset.addValue(8,"人事部","2014年");

        dataset.addValue(0,"产品部","2011年");
        dataset.addValue(5,"产品部","2012年");
        dataset.addValue(6,"产品部","2013年");
        dataset.addValue(2,"产品部","2014年");

        StandardChartTheme chartTheme = new StandardChartTheme("CN");
//        设置大标题的字体
        chartTheme.setExtraLargeFont(new Font("华文宋体",Font.BOLD,20));
//        设置图例的字体
        chartTheme.setRegularFont(new Font("华文宋体",Font.BOLD,15));
//        设置内容 x y轴
        chartTheme.setLargeFont(new Font("华文宋体",Font.BOLD,15));
        ChartFactory.setChartTheme(chartTheme);

//        大标题 X轴的说明  Y轴的说明 数据集
        JFreeChart chart = ChartFactory.createBarChart("公司人数", "各部门","入职人数",dataset);

        ChartUtils.writeChartAsJPEG(response.getOutputStream(),chart,400,300);

    }
}
