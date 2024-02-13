package com.cocoon.reggieTakeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cocoon.reggieTakeout.emtity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * 员工实体类的数据访问层接口
 * @author cocoon
 */
@Mapper //数据访问层，负责对实体类数据访问操作，包括数据的增删改查
public interface EmployeeMapper extends BaseMapper<Employee> {

}
