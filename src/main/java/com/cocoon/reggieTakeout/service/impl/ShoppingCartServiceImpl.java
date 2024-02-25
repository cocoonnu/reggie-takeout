package com.cocoon.reggieTakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cocoon.reggieTakeout.emtity.ShoppingCart;
import com.cocoon.reggieTakeout.mapper.ShoppingCartMapper;
import com.cocoon.reggieTakeout.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
