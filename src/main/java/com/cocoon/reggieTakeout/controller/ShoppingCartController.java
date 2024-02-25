package com.cocoon.reggieTakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cocoon.reggieTakeout.common.BaseContext;
import com.cocoon.reggieTakeout.common.GlobalResult;
import com.cocoon.reggieTakeout.emtity.ShoppingCart;
import com.cocoon.reggieTakeout.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public GlobalResult<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        // 先设置userId
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, userId);

        // 判断是添加的是菜品还是套餐
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        if (dishId != null) {
            lqw.eq(ShoppingCart::getDishId, dishId);
        } else {
            lqw.eq(ShoppingCart::getSetmealId, setmealId);
        }

        // 判断购物车内是否已经存在待添加的菜品或套餐
        ShoppingCart selectedShoppingCart = shoppingCartService.getOne(lqw);
        if (selectedShoppingCart != null) {
            selectedShoppingCart.setNumber(selectedShoppingCart.getNumber() + 1);
            shoppingCartService.updateById(selectedShoppingCart);
        } else {
            shoppingCart.setNumber(1);
            selectedShoppingCart = shoppingCart;
            shoppingCartService.save(shoppingCart);
        }

        return GlobalResult.success(selectedShoppingCart);
    }

    @GetMapping("/list")
    public GlobalResult<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(lqw);
        return GlobalResult.success(shoppingCartList);
    }

    @DeleteMapping("/clean")
    public GlobalResult<String> clean() {
        //条件构造器
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();
        queryWrapper.eq(userId != null, ShoppingCart::getUserId, userId);
        //删除当前用户id的所有购物车数据
        shoppingCartService.remove(queryWrapper);
        return GlobalResult.success("成功清空购物车");
    }
}
