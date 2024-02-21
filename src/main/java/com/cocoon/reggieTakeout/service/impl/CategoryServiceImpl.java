package com.cocoon.reggieTakeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cocoon.reggieTakeout.common.CustomException;
import com.cocoon.reggieTakeout.emtity.Category;
import com.cocoon.reggieTakeout.emtity.Dish;
import com.cocoon.reggieTakeout.emtity.Setmeal;
import com.cocoon.reggieTakeout.mapper.CategoryMapper;
import com.cocoon.reggieTakeout.service.CategoryService;
import com.cocoon.reggieTakeout.service.DishService;
import com.cocoon.reggieTakeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /** 删除分类的业务逻辑方法 **/
    @Override
    public void remove(Long id) {
        // 查询当前分类是否关联了菜品和套餐
        LambdaQueryWrapper<Dish> lqwByDish = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Setmeal> lqwBySetmeal = new LambdaQueryWrapper<>();
        lqwByDish.eq(Dish::getCategoryId, id);
        lqwBySetmeal.eq(Setmeal::getCategoryId, id);

        if (dishService.count(lqwByDish) != 0) {
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        if (setmealService.count(lqwBySetmeal) != 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        // 都没有关联则按id删除
        super.removeById(id);
    }
}
