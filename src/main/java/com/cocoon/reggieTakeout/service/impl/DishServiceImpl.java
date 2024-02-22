package com.cocoon.reggieTakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cocoon.reggieTakeout.dto.DishDto;
import com.cocoon.reggieTakeout.emtity.Dish;
import com.cocoon.reggieTakeout.emtity.DishFlavor;
import com.cocoon.reggieTakeout.mapper.DishMapper;
import com.cocoon.reggieTakeout.service.DishFlavorService;
import com.cocoon.reggieTakeout.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    public void saveWithFlavor(DishDto dishDto) {
        // 先保存菜品数据
        this.save(dishDto);

        // 将菜品id存储到每一个口味数据中
        Long dishId = dishDto.getId();
        List<DishFlavor> dishFlavorList = dishDto.getFlavors();
        for (DishFlavor dishFlavor : dishFlavorList) {
            dishFlavor.setDishId(dishId);
        }

        // 最后保存口味数据列表
        dishFlavorService.saveBatch(dishFlavorList);
    }
}