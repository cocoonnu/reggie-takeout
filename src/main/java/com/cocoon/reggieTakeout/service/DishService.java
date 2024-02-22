package com.cocoon.reggieTakeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cocoon.reggieTakeout.dto.DishDto;
import com.cocoon.reggieTakeout.emtity.Dish;

public interface DishService extends IService<Dish> {
    /** 保存菜品，并且将口味数据添加到dish_flavor表中 **/
    void saveWithFlavor(DishDto dishDto);
}
