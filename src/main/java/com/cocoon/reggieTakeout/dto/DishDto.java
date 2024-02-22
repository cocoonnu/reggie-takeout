package com.cocoon.reggieTakeout.dto;

import com.cocoon.reggieTakeout.emtity.Dish;
import com.cocoon.reggieTakeout.emtity.DishFlavor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors;

    private String categoryName;

    private Integer copies;
}
