package com.cocoon.reggieTakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cocoon.reggieTakeout.common.GlobalResult;
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

    /** 分页查询员工列表 **/
    @GetMapping("/page")
    public GlobalResult<Page<DishDto>> page(int page, int pageSize, @RequestParam(required = false) String name) {
        // 封装好dishPageInfo对象
        Page<Dish> dishPageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.like(!StringUtils.isEmpty(name), Dish::getName, name);
        lqw.orderByAsc(Dish::getUpdateTime);
        dishService.page(dishPageInfo, lqw);

        // 封装dishDtoPageInfo对象，列表中添加categoryName属性
        Page<DishDto> dishDtoPageInfo = new Page<>();
        // 除了record属性，其余属性都拷贝dishPageInfo
        BeanUtils.copyProperties(dishPageInfo, dishDtoPageInfo, "records");
        List<Dish> records = dishPageInfo.getRecords();
        // 基于records封装dishDtoList
        List<DishDto> dishDtoList = records.stream().map(item -> {
            Category category = categoryService.getById(item.getCategoryId());
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            if (category != null) dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPageInfo.setRecords(dishDtoList);
        return GlobalResult.success(dishDtoPageInfo);
    }
}
