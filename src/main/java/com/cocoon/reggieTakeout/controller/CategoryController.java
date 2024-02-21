package com.cocoon.reggieTakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cocoon.reggieTakeout.common.GlobalResult;
import com.cocoon.reggieTakeout.emtity.Category;
import com.cocoon.reggieTakeout.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public GlobalResult<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return GlobalResult.success("新增分类成功");
    }

    @GetMapping("/page")
    public GlobalResult<Page> page(int page, int pageSize) {
        Page pageInfo = new Page();
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Category::getSort);
        categoryService.page(pageInfo, lqw);
        return GlobalResult.success(pageInfo);
    }

    @DeleteMapping()
    public GlobalResult<String> delete(Long ids) {
        categoryService.remove(ids);
        return GlobalResult.success("分类信息删除成功");
    }

    @PutMapping()
    public GlobalResult<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return GlobalResult.success("分类信息修改成功");
    }
}
