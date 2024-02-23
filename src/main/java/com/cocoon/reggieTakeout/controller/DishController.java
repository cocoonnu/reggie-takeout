package com.cocoon.reggieTakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cocoon.reggieTakeout.common.GlobalResult;
import com.cocoon.reggieTakeout.constant.GlobalConstant;
import com.cocoon.reggieTakeout.dto.DishDto;
import com.cocoon.reggieTakeout.emtity.Category;
import com.cocoon.reggieTakeout.emtity.Dish;
import com.cocoon.reggieTakeout.service.CategoryService;
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
    private CategoryService categoryService;

    /** 新增菜品 **/
    @PostMapping
    public GlobalResult<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return GlobalResult.success("新增菜品成功");
    }

    /** 查询菜品列表 **/
    @GetMapping("/list")
    public GlobalResult<List<Dish>> getList(Dish dish) {
        // 查询条件
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(dish.getCategoryId() != null ,Dish::getCategoryId, dish.getCategoryId());
        lqw.eq(Dish::getStatus, 1);

        List<Dish> dishList = dishService.list(lqw);
        return GlobalResult.success(dishList);
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
        List<Dish> dishList = dishService.listByIds(ids);
        for (Dish dish : dishList) {
            dish.setStatus(status);
        }
        dishService.updateBatchById(dishList);
        String statusStr = status == 1 ? "启售" : "停售";
        return GlobalResult.success("批量" + statusStr + "成功");
    }
}
