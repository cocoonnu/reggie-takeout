package com.cocoon.reggieTakeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cocoon.reggieTakeout.dto.SetmealDto;
import com.cocoon.reggieTakeout.emtity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /** 保存套餐数据同时更新setmeal_dish表 **/
    void saveWithSetmealDish(SetmealDto setmealDto);

    /** 删除套餐，并该套餐的一并删除 **/
    void deleteWithSetmealDish(List<Long> ids);
}
