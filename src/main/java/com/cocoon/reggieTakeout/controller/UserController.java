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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SendCode sendCode;

    @Autowired
    private RedisTemplate redisTemplate;

    /** 发送短信 **/
    @PostMapping("/sendMsg")
    private GlobalResult<String> sendMsg(HttpSession session, @RequestBody User user) {
        String code = RandomUtil.buildCheckCode(4);
        // sendCode.sendMessage("18579152306", code);

        // 使用缓存存储验证码
        // session.setAttribute(user.getPhone(), code);
        redisTemplate.opsForValue().set(user.getPhone(), code, 5, TimeUnit.MINUTES);

        log.info("短信验证码：{}", code);
        return GlobalResult.success("手机验证码短信发送成功");
    }

    /** 用户登录 **/
    @PostMapping("/login")
    private GlobalResult<User> login(HttpSession session, @RequestBody UserLoginDto userLoginDto) {
        String phone = userLoginDto.getPhone();

        // 从缓存中获取验证码
        // String codeInSession = (String) session.getAttribute(phone);
        String codeInSession = (String) redisTemplate.opsForValue().get(phone);

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
            // 设置用户缓存
            // session.setAttribute(GlobalConstant.USER_ID, user.getId());
            redisTemplate.opsForValue().set(GlobalConstant.USER_ID, user.getId(), 7, TimeUnit.DAYS);

            // 删除验证码缓存
            redisTemplate.delete(phone);
            return GlobalResult.success(user);
        }
        return GlobalResult.error("用户登录失败");
    }

    /** 用户登出 **/
    @PostMapping("/loginout")
    public GlobalResult<String> logout(HttpServletRequest request) {
        // request.getSession().removeAttribute(GlobalConstant.USER_ID);
        redisTemplate.delete(GlobalConstant.USER_ID);
        return GlobalResult.success("退出成功");
    }
}
