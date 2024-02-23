package com.cocoon.reggieTakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cocoon.reggieTakeout.common.GlobalResult;
import com.cocoon.reggieTakeout.emtity.Category;
import com.cocoon.reggieTakeout.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /** 新增分类 **/
    @PostMapping
    public GlobalResult<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return GlobalResult.success("新增分类成功");
    }

    /** 分类列表分页查询 **/
    @GetMapping("/page")
    public GlobalResult<Page<Category>> page(int page, int pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Category::getSort);
        categoryService.page(pageInfo, lqw);
        return GlobalResult.success(pageInfo);
    }

    /** 分类列表按类型查询 **/
    @GetMapping("/list")
    public GlobalResult<List<Category>> list(int type) {
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Category::getType, type);
        lqw.orderByDesc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> categoryList = categoryService.list(lqw);
        return GlobalResult.success(categoryList);
    }

    /** 删除分类 **/
    @DeleteMapping()
    public GlobalResult<String> delete(Long ids) {
        categoryService.remove(ids);
        return GlobalResult.success("分类信息删除成功");
    }

    /** 修改分类 **/
    @PutMapping()
    public GlobalResult<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return GlobalResult.success("分类信息修改成功");
    }
}
