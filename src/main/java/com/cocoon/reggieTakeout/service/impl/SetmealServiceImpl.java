package com.cocoon.reggieTakeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cocoon.reggieTakeout.common.CustomException;
import com.cocoon.reggieTakeout.dto.SetmealDto;
import com.cocoon.reggieTakeout.emtity.Dish;
import com.cocoon.reggieTakeout.emtity.DishFlavor;
import com.cocoon.reggieTakeout.emtity.Setmeal;
import com.cocoon.reggieTakeout.emtity.SetmealDish;
import com.cocoon.reggieTakeout.mapper.SetmealMapper;
import com.cocoon.reggieTakeout.service.SetmealDishService;
import com.cocoon.reggieTakeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public void saveWithSetmealDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        Long setmealId = setmealDto.getId();
        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishList) {
            // 存储关联表setmeal_dish的SetmealId字段
            setmealDish.setSetmealId(setmealId);
        }
        setmealDishService.saveBatch(setmealDishList);
    }

    @Override
    public void deleteWithSetmealDish(List<Long> ids) {
        // 首先判断选中的数组中是否有启售的
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId, ids).eq(Setmeal::getStatus, 1);
        int count = this.count(setmealLambdaQueryWrapper);
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，请先停售再进行删除");
        }

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getDishId, ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
        this.removeByIds(ids);
    }
}