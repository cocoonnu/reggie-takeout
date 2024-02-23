package com.cocoon.reggieTakeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cocoon.reggieTakeout.dto.DishDto;
import com.cocoon.reggieTakeout.emtity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    /** 保存菜品，并且将口味数据添加到dish_flavor表中 **/
    void saveWithFlavor(DishDto dishDto);

    /** 更新菜品，并该菜品的口味数据覆盖更新 **/
    void updateWithFlavor(DishDto dishDto);

    /** 删除菜品，并该菜品的口味数据一并删除 **/
    void batchDeleteWithFlavor(List<Long> ids);

    /** 通过菜品id返回对应的DishDto对象 **/
    DishDto getDishDtoById(Long id);
}
