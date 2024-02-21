package com.cocoon.reggieTakeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cocoon.reggieTakeout.emtity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
