package com.cocoon.reggieTakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cocoon.reggieTakeout.emtity.Employee;
import com.cocoon.reggieTakeout.mapper.EmployeeMapper;
import com.cocoon.reggieTakeout.service.EmployService;
import org.springframework.stereotype.Service;

/**
 * 员工实体类的数据业务层接口实现类
 * @author cocoon
 */
@Service //业务逻辑层，处理具体的业务逻辑
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployService {
}
