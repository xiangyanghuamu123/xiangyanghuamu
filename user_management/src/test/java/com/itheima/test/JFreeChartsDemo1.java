package com.itheima.test;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.File;

//饼状图
public class JFreeChartsDemo1 {

    public static void main(String[] args) throws Exception {
//        需求：统计每个部门的人员
//        1、准备数据  技术部 180 销售部 20  人事部 10
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("技术部",180);
        dataset.setValue("销售部",20);
        dataset.setValue("人事部",10);
        StandardChartTheme chartTheme = new StandardChartTheme("CN");

//        设置大标题的字体
        chartTheme.setExtraLargeFont(new Font("华文宋体",Font.BOLD,20));
//        设置图例的字体
        chartTheme.setRegularFont(new Font("华文宋体",Font.BOLD,15));
//        设置内容 x y轴
        chartTheme.setLargeFont(new Font("华文宋体",Font.BOLD,15));
        ChartFactory.setChartTheme(chartTheme);
//        String title, PieDataset dataset, boolean legend图例, boolean tooltips, boolean urls
//        大标题 数据集 是否显示图例  是否显示提示  是否跳转
//        JFreeChart chart = ChartFactory.createPieChart("各部门人数", dataset, true, false, false);
        JFreeChart chart = ChartFactory.createPieChart3D("各部门人数", dataset, true, false, false);

        ChartUtils.saveChartAsPNG(new File("d:\\chart1.png"),chart,400,300);

    }

}
