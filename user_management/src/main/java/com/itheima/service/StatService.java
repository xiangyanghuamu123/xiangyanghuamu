package com.itheima.service;

import com.itheima.mapper.UserMapper;
import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class StatService {

    @Autowired
    private UserMapper userMapper;

    public List<Map> columnCharts() {
       return userMapper.columnCharts();
    }

    public List<Map> lineCharts() {
        return userMapper.lineCharts();
    }

    public List<Map<String,Object>> pieCharts() {
//        resultMapList是最终接收的数据
        List<Map<String,Object>> resultMapList = new ArrayList<Map<String,Object>>();
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
//        },
//]

//        查询了所有人员数据 resultMapList需要的数据都可以从userList组装
        List<User> userList = userMapper.selectAll();
//        根据省份分组
        Map<String, List<User>> provinceMap = userList.stream().collect(Collectors.groupingBy(User::getProvince));
        for (String provinceName : provinceMap.keySet()) {
           Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("name",provinceName);
            resultMap.put("drilldown",provinceName);
            resultMap.put("id",provinceName);

//            当前省份下所有的用户
            List<User> cityUserList = provinceMap.get(provinceName);
            resultMap.put("y",cityUserList.size());

//            根据城市cityName分组
            Map<String, List<User>> cityMap = cityUserList.stream().collect(Collectors.groupingBy(User::getCity));

            List<Map<String,Object>> cityMapList = new ArrayList<>();

            for (String cityName : cityMap.keySet()) {
                Map<String,Object> dataMap = new HashMap<>();
                dataMap.put("name",cityName);
                dataMap.put("y",cityMap.get(cityName).size());
                cityMapList.add(dataMap);
            }

            resultMap.put("data",cityMapList);

            resultMapList.add(resultMap);
        }
        return resultMapList;
    }

    //        {"province":[{name:"河北省",value:9},{}],"city":[{name:XX,value:9},{name:XX,value:9},{name:XX,value:9}]}
    public Map pieECharts() {

        Map resultMap = new HashMap();
        List<Map> provinceMapList = new ArrayList<>();
        List<Map> cityMapList = new ArrayList<>();

        Example example = new Example(User.class);
        example.setOrderByClause("province , city");
        List<User> userList = userMapper.selectByExample(example);

        Map<String, List<User>> provinceMap = userList.stream().collect(Collectors.groupingBy(User::getProvince, LinkedHashMap::new, Collectors.toList()));
        for (String provinceName : provinceMap.keySet()) {
            Map pMap = new HashMap();
            pMap.put("name",provinceName);
            pMap.put("value",provinceMap.get(provinceName).size());
            provinceMapList.add(pMap);

            List<User> cityUserList =  provinceMap.get(provinceName);
            Map<String, List<User>> cityMap = cityUserList.stream().collect(Collectors.groupingBy(User::getCity, LinkedHashMap::new, Collectors.toList()));
            for (String cityName : cityMap.keySet()) {
                Map cMap = new HashMap();
                cMap.put("name",cityName);
                cMap.put("value",cityMap.get(cityName).size());
                cityMapList.add(cMap);
            }

        }

        resultMap.put("province",provinceMapList);
        resultMap.put("city",cityMapList);

        return resultMap;
    }
}
