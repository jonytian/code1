package com.reacheng.rc.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;
@Data
@Entity
@Table(name = "sys_admin_menus")
public class Menu implements Serializable {
    @Id
    @Column(name = "menu_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menuId;
    @Column(name = "menu_name",length = 50)
    @NotEmpty(message = "菜单名称不能为空")
    private String menuName;
    @Column(name = "menu_icon")
    private String menuIcon;
    @Column(name = "menu_link")
    private String menuLink;
    @Column(name = "parent_id",length = 11)
    private Integer parentId = 0;


    @Transient
    private List<Menu> children;

    public Menu() {
    }

    public Menu(Integer menuId) {
        this.menuId = menuId;
    }

    public Menu(String menuName, String menuIcon, String menuLink, Integer parentId, List <Menu> children) {
        this.menuName = menuName;
        this.menuIcon = menuIcon;
        this.menuLink = menuLink;
        this.parentId = parentId;
        this.children = children;
    }



}

