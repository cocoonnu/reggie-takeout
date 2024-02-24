package com.cocoon.reggieTakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cocoon.reggieTakeout.common.GlobalResult;
import com.cocoon.reggieTakeout.constant.GlobalConstant;
import com.cocoon.reggieTakeout.dto.DishDto;
import com.cocoon.reggieTakeout.emtity.Category;
import com.cocoon.reggieTakeout.emtity.Dish;
import com.cocoon.reggieTakeout.emtity.DishFlavor;
import com.cocoon.reggieTakeout.service.CategoryService;
import com.cocoon.reggieTakeout.service.DishFlavorService;
import com.cocoon.reggieTakeout.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /** 新增菜品 **/
    @PostMapping
    public GlobalResult<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return GlobalResult.success("新增菜品成功");
    }

    /** 查询菜品列表 **/
    @GetMapping("/list")
    public GlobalResult<List<DishDto>> getList(Dish dish) {
        // 先封装dishList
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(dish.getCategoryId() != null ,Dish::getCategoryId, dish.getCategoryId());
        lqw.eq(Dish::getStatus, 1);
        List<Dish> dishList = dishService.list(lqw);

        // 再封装dishDtoList
        List<DishDto> dishDtoList = dishList.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            // 获取categoryName
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) dishDto.setCategoryName(category.getName());

            // 获取flavors
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            if (dishFlavorList != null) dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());

        return GlobalResult.success(dishDtoList);
    }

    /** 分页查询菜品列表 **/
    @GetMapping("/page")
    public GlobalResult<Page<DishDto>> page(int page, int pageSize, @RequestParam(required = false) String name) {
        // 封装好dishPageInfo对象
        Page<Dish> dishPageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.like(!StringUtils.isEmpty(name), Dish::getName, name);
        lqw.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPageInfo, lqw);

        // 封装dishDtoPageInfo对象，列表中添加categoryName属性
        Page<DishDto> dishDtoPageInfo = new Page<>();
        // 除了record属性，其余属性都拷贝dishPageInfo
        BeanUtils.copyProperties(dishPageInfo, dishDtoPageInfo, "records");
        List<Dish> records = dishPageInfo.getRecords();
        // 基于records封装dishDtoList
        List<DishDto> dishDtoList = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null) dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPageInfo.setRecords(dishDtoList);
        return GlobalResult.success(dishDtoPageInfo);
    }

    /** 通过dishId响应对应的DishDto对象 **/
    @GetMapping("{id}")
    public GlobalResult<DishDto> getDishDtoById(@PathVariable Long id) {
        DishDto dishDto = dishService.getDishDtoById(id);
        return GlobalResult.success(dishDto);
    }

    /** 更新菜品 **/
    @PutMapping
    public GlobalResult<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return GlobalResult.success("更新菜品成功");
    }

    /** 删除菜品 **/
    @DeleteMapping
    public GlobalResult<String> batchDelete(@RequestParam List<Long> ids) {
        dishService.batchDeleteWithFlavor(ids);
        return GlobalResult.success("批量删除成功");
    }

    /** 启售、停售菜品 **/
    @PostMapping("/status/{status}")
    public GlobalResult<String> batchUpdateStatus(@PathVariable Integer status, @RequestParam List<Long> ids) {
        LambdaUpdateWrapper<Dish> lqw = new LambdaUpdateWrapper<>();
        lqw.in(Dish::getId, ids);
        lqw.set(Dish::getStatus, status);
        dishService.update(lqw);
        String statusStr = status == 1 ? "启售" : "停售";
        return GlobalResult.success("批量" + statusStr + "成功");
    }
}
