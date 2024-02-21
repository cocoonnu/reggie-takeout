package com.cocoon.reggieTakeout.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /** 插入时自动填充字段 **/
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateUser", EmployeeIdThread.getEmployeeId());
        metaObject.setValue("createUser", EmployeeIdThread.getEmployeeId());
    }

    /** 更新时自动填充字段 **/
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", EmployeeIdThread.getEmployeeId());
    }
}
