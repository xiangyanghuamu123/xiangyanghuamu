package com.itheima.controller;

import com.itheima.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stat")
public class StatController {

    @Autowired
    private StatService statService;

    @GetMapping(value = "/columnCharts",name =" 统计各部门人数" )
    public List<Map> columnCharts(){
        //[{deptName:"",num:11},{},{}]
        return statService.columnCharts();
    }

    // 月份入职人数统计 lineCharts()
    @GetMapping(value = "/lineCharts",name =" 统计各部门人数" )
    public List<Map> lineCharts(){
//         [{name:"01",num:2},{}]
        return statService.lineCharts();
    }

    // 员工地方来源统计 pieCharts()

    @GetMapping(value = "/pieCharts",name =" 员工地方来源统计" )
    public List<Map<String,Object>> pieCharts(){
//[
//        {
//            name: '山东省',
//                    y: 7,
//                drilldown: '山东省',
//                id:'山东省',
//                data:[
//            {name:'济南市',y:2},
//            {name:'威海市',y:1},
//            {name:'青岛市',y:4}
//			]
//
//        }
//]
        return statService.pieCharts();
    }

    @GetMapping("/pieECharts")
    public Map pieECharts(){

        return statService.pieECharts();

    }

}
