package com.cocoon.reggieTakeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cocoon.reggieTakeout.emtity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{
}
