package com.mm.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mm.modules.sys.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜单管理
 *
 * @author lwl
 */
@Mapper
public interface MenuDao extends BaseMapper<MenuEntity> {

    @Select("SELECT * FROM menu WHERE id in ( " +
            " SELECT menu_id FROM role_menu WHERE role_id IN ( " +
            "   SELECT role_id FROM user_role WHERE user_id = ${userId} " +
            " )) ORDER BY sort,id DESC")
    List<MenuEntity> listByUserId(@Param("userId") Long userId);

}
