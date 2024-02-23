package com.cocoon.reggieTakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cocoon.reggieTakeout.common.GlobalResult;
import com.cocoon.reggieTakeout.constant.GlobalConstant;
import com.cocoon.reggieTakeout.constant.RandomUtil;
import com.cocoon.reggieTakeout.constant.SendCode;
import com.cocoon.reggieTakeout.dto.UserLoginDto;
import com.cocoon.reggieTakeout.emtity.Setmeal;
import com.cocoon.reggieTakeout.emtity.User;
import com.cocoon.reggieTakeout.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SendCode sendCode;

    /** 发送短信 **/
    @PostMapping("/sendMsg")
    private GlobalResult<String> sendMsg(HttpSession session, @RequestBody User user) {
        String code = RandomUtil.buildCheckCode(4);
        // sendCode.sendMessage("18579152306", code);
        session.setAttribute(user.getPhone(), code);
        log.info("短信验证码：{}", code);
        return GlobalResult.success("手机验证码短信发送成功");
    }

    /** 用户登录 **/
    @PostMapping("/login")
    private GlobalResult<User> login(HttpSession session, @RequestBody UserLoginDto userLoginDto) {
        String phone = userLoginDto.getPhone();
        String codeInSession = (String) session.getAttribute(phone);
        // 验证手机号和验证是否正确
        if (!StringUtils.isEmpty(codeInSession) && codeInSession.equals(userLoginDto.getCode())) {
            session.removeAttribute(phone);
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getPhone, phone);
            User user = userService.getOne(lqw);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute(GlobalConstant.USER_ID, user.getId());
            return GlobalResult.success(user);
        }
        return GlobalResult.error("用户登录失败");
    }
}
