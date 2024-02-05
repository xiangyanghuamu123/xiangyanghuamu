package com.itheima.mapper;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface UserMapper extends Mapper<User> {
    @Select(value = "select  d.dept_name deptName, count(u.id) num from tb_dept d LEFT JOIN tb_user u on  u.dept_id=d.id GROUP BY d.dept_name")
    List<Map> columnCharts();

    @Select(value = "select m.name, IFNULL(t.num,0) num  from  tb_month m LEFT JOIN " +
            "(select DATE_FORMAT(hire_date,'%m') months ,count(id) num from tb_user GROUP BY DATE_FORMAT(hire_date,'%m')) t " +
            "on m.name = t.months")
    List<Map> lineCharts();
}
