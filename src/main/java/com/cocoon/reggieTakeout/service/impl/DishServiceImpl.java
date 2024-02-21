package com.cocoon.reggieTakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cocoon.reggieTakeout.emtity.Dish;
import com.cocoon.reggieTakeout.mapper.DishMapper;
import com.cocoon.reggieTakeout.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}