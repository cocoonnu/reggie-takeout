package com.cocoon.reggieTakeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cocoon.reggieTakeout.emtity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
