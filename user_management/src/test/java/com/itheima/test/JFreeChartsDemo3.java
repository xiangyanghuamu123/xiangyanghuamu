package com.itheima.test;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.File;

//柱状图
public class JFreeChartsDemo3 {

    public static void main(String[] args) throws Exception {
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

        ChartUtils.saveChartAsPNG(new File("d:\\chart33.png"),chart,400,300);

    }

}
