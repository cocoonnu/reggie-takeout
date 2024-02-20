package com.cocoon.reggieTakeout.emtity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    /** 创建时间，插入时自动填充 **/
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间，插入和更新时自动填充 **/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 创建人id，插入时自动填充 **/
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /** 更新人id，插入和更新时自动填充 **/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
