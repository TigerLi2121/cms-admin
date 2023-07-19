package com.mm.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单管理
 *
 * @author lwl
 */
@Data
@TableName("menu")
public class MenuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @TableId
    private Long id;

    /**
     * 父菜单ID，一级菜单为0
     */
    @NotNull(message = "pid is null")
    private Long pid;

    /**
     * 菜单名称
     */
    @NotBlank(message = "name is null")
    private String name;

    /**
     * 菜单URL
     */
    private String path;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:add)
     */
    private String perms;

    /**
     * 类型(1=目录,2=菜单,3=按钮)
     */
    private Integer type;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;

    @TableField(exist = false)
    private List<MenuEntity> children;

}
