package com.cocoon.reggieTakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cocoon.reggieTakeout.emtity.User;
import com.cocoon.reggieTakeout.mapper.UserMapper;
import com.cocoon.reggieTakeout.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
