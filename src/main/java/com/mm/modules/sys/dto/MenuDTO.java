package com.mm.modules.sys.dto;

import cn.hutool.core.util.NumberUtil;
import com.mm.modules.sys.entity.MenuEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单列表
 *
 * @author lwl
 */
@Data
public class MenuDTO {

    /**
     * 菜单ID
     */
    private Long id;

    /**
     * 父菜单ID，一级菜单为0
     */
    private Long pid;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单URL
     */
    private String path;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 子菜单
     */
    private List<MenuDTO> children;


    /**
     * 用户菜单
     *
     * @param ms
     * @param ps
     * @return
     */
    public static List<MenuDTO> getMenus(List<MenuEntity> ms, List<MenuDTO> ps) {
        List<MenuDTO> sm = new ArrayList<>();
        for (MenuDTO p : ps) {
            List<MenuDTO> cs = new ArrayList<>();
            for (MenuEntity m : ms) {
                if (NumberUtil.equals(m.getPid(), p.getId()) && (m.getType() == 1 || m.getType() == 2)) {
                    cs.add(MenuDTO.convert(m));
                }
            }
            p.setChildren(getMenus(ms, cs));
            sm.add(p);
        }
        return sm;
    }

    public static MenuDTO convert(MenuEntity m) {
        if (m == null) {
            return null;
        }
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(m.getId());
        menuDTO.setPid(m.getPid());
        menuDTO.setName(m.getName());
        menuDTO.setPath(m.getPath());
        menuDTO.setIcon(m.getIcon());
        return menuDTO;
    }
}
