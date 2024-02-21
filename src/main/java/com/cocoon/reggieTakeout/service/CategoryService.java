package com.cocoon.reggieTakeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cocoon.reggieTakeout.emtity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
