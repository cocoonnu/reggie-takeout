package com.cocoon.reggieTakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cocoon.reggieTakeout.common.GlobalResult;
import com.cocoon.reggieTakeout.constant.GlobalConstant;
import com.cocoon.reggieTakeout.emtity.Employee;
import com.cocoon.reggieTakeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public GlobalResult<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 密码加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // mp条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername());
        Employee queriedEmployee = employeeService.getOne(lqw);

        // 分情况判断
        if (queriedEmployee == null) return GlobalResult.error("该用户不存在");
        if (!queriedEmployee.getPassword().equals(password)) return GlobalResult.error("密码错误");
        if (queriedEmployee.getStatus() == 0) return GlobalResult.error("该用户已被禁用");

        request.getSession().setAttribute(GlobalConstant.EMPLOYEE_ID, queriedEmployee.getId());
        return GlobalResult.success(queriedEmployee);
    }

    @PostMapping("/logout")
    public GlobalResult<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employeeId");
        return GlobalResult.success("退出成功");
    }

    @PostMapping
    public GlobalResult<Employee> save(HttpServletRequest request, @RequestBody Employee employee) {
        // 除了前端传过来的属性还需要封装其他属性，id的话mp会自动随机生成
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long employeeId = (Long)request.getSession().getAttribute(GlobalConstant.EMPLOYEE_ID);
        employee.setCreateUser(employeeId);
        employee.setUpdateUser(employeeId);

        // mp新增员工
        employeeService.save(employee);
        return GlobalResult.success(employee);
    }

    @PostMapping("/page")
    public GlobalResult<Page> page(int page, int pageSize, @RequestParam(required = false) String name) {
        log.info("page={},pageSize={},name={}", page, pageSize, name);
        Page pageInfo = new Page(page, pageSize);

        // 添加name搜索查询和按更新时间排序
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.like(StringUtils.isEmpty(name), Employee::getName, name);
        lqw.orderByDesc(Employee::getUpdateTime);

        // 直接进行分页查询，mp会自动将结果帮我们封装到pageInfo对象中
        employeeService.page(pageInfo, lqw);
        return GlobalResult.success(pageInfo);
    }
}
