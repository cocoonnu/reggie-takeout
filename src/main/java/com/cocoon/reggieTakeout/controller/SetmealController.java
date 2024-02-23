package com.cocoon.reggieTakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cocoon.reggieTakeout.common.GlobalResult;
import com.cocoon.reggieTakeout.constant.GlobalConstant;
import com.cocoon.reggieTakeout.dto.SetmealDto;
import com.cocoon.reggieTakeout.emtity.Dish;
import com.cocoon.reggieTakeout.emtity.Employee;
import com.cocoon.reggieTakeout.emtity.Setmeal;
import com.cocoon.reggieTakeout.service.CategoryService;
import com.cocoon.reggieTakeout.service.SetmealDishService;
import com.cocoon.reggieTakeout.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    /** 新增套餐 **/
    @PostMapping
    public GlobalResult<String> add(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithSetmealDish(setmealDto);
        return GlobalResult.success("新增套餐成功");
    }

    /** 分页查询套餐 **/
    @GetMapping("/page")
    public GlobalResult<Page<SetmealDto>> page(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.like(name != null, Setmeal::getName, name);
        setmealService.page(setmealPage, lqw);

        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> setmealDtoList = records.stream().map(item -> {
            Long categoryId = item.getCategoryId();
            String categoryName = categoryService.getById(categoryId).getName();
            SetmealDto setmealDto = new SetmealDto();
            // 先拷贝其他属性
            BeanUtils.copyProperties(item, setmealDto);
            // 再添加属性
            setmealDto.setCategoryName(categoryName);
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(setmealDtoList);
        return GlobalResult.success(setmealDtoPage);
    }

    /** 启售、停售套餐 **/
    @PostMapping("/status/{status}")
    public GlobalResult<String> updateStatus(@PathVariable Integer status, @RequestParam List<Long> ids) {
        List<Setmeal> setmealList = setmealService.listByIds(ids);
        for (Setmeal setmeal : setmealList) {
            setmeal.setStatus(status);
        }
        setmealService.updateBatchById(setmealList);
        String statusStr = status == 1 ? "启售" : "停售";
        return GlobalResult.success("批量" + statusStr + "成功");
    }

    /** 删除套餐 **/
    @DeleteMapping
    public GlobalResult<String> delete(@RequestParam List<Long> ids) {
        setmealService.deleteWithSetmealDish(ids);
        return GlobalResult.success("删除套餐成功");
    }
}
