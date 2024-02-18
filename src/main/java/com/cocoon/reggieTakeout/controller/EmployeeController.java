package com.cocoon.reggieTakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cocoon.reggieTakeout.common.R;
import com.cocoon.reggieTakeout.constant.GlobalConstant;
import com.cocoon.reggieTakeout.emtity.Employee;
import com.cocoon.reggieTakeout.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 密码加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // mp条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<Employee>();
        lqw.eq(Employee::getUsername, employee.getUsername());
        Employee queriedEmployee = employeeService.getOne(lqw);

        // 分情况判断
        if (queriedEmployee == null) return R.error("该用户不存在");
        if (!queriedEmployee.getPassword().equals(password)) return R.error("密码错误");
        if (queriedEmployee.getStatus() == 0) return R.error("该用户已被禁用");

        request.getSession().setAttribute(GlobalConstant.EMPLOYEE_ID, queriedEmployee.getId());
        return R.success(queriedEmployee);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employeeId");
        return R.success("退出成功");
    }
}
