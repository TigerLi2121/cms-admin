package com.mm.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyBatisPlusHandler implements MetaObjectHandler {

    public static final String CREATE_TIME = "createdAt";
    public static final String UPDATE_TIME = "updatedAt";

    @Override
    public void insertFill(MetaObject metaObject) {
        Date now = new Date();
        setFieldValByName(CREATE_TIME, now, metaObject);
        setFieldValByName(UPDATE_TIME, now, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Date now = new Date();
        setFieldValByName(UPDATE_TIME, now, metaObject);
    }
}
