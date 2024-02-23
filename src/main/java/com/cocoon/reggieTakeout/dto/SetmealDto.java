package com.cocoon.reggieTakeout.dto;

import com.cocoon.reggieTakeout.emtity.Setmeal;
import com.cocoon.reggieTakeout.emtity.SetmealDish;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;
    private String categoryName;
}
