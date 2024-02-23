package com.cocoon.reggieTakeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cocoon.reggieTakeout.common.CustomException;
import com.cocoon.reggieTakeout.dto.DishDto;
import com.cocoon.reggieTakeout.emtity.Dish;
import com.cocoon.reggieTakeout.emtity.DishFlavor;
import com.cocoon.reggieTakeout.mapper.DishMapper;
import com.cocoon.reggieTakeout.service.DishFlavorService;
import com.cocoon.reggieTakeout.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
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

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);
        Long dishId = dishDto.getId();

        // 先清除该菜品的口味数据
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId, dishId);
        dishFlavorService.remove(lqw);

        // 再更新该菜品的口味数据
        List<DishFlavor> dishFlavorList = dishDto.getFlavors();
        for (DishFlavor dishFlavor : dishFlavorList) {
            dishFlavor.setDishId(dishId);
        }

        // 最后保存口味数据列表
        dishFlavorService.saveBatch(dishFlavorList);
    }

    @Override
    public void batchDeleteWithFlavor(List<Long> ids) {
        // 首先判断选中的数组中是否有启售的
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(Dish::getId, ids).eq(Dish::getStatus, 1);
        int count = this.count(dishLambdaQueryWrapper);
        if (count > 0) {
            throw new CustomException("菜品正在售卖中，请先停售再进行删除");
        }

        // 删除关联的dish_flavor表
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        // 删除dish表数据
        this.removeByIds(ids);
    }

    @Override
    public DishDto getDishDtoById(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        // 获取对应的口味列表
        LambdaQueryWrapper<DishFlavor> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId, id).orderByDesc(DishFlavor::getUpdateTime);
        List<DishFlavor> dishFlavorList = dishFlavorService.list(lqw);
        dishDto.setFlavors(dishFlavorList);

        return dishDto;
    }
}