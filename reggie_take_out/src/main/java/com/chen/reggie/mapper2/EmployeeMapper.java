package com.chen.reggie.mapper2;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.reggie.entity1.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author chen
 * @create 2022/9/26 16:41
 */

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
