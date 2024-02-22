package com.cocoon.reggieTakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cocoon.reggieTakeout.emtity.DishFlavor;
import com.cocoon.reggieTakeout.mapper.DishFlavorMapper;
import com.cocoon.reggieTakeout.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
