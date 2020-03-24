package com.reacheng.rc.service.impl;

import com.reacheng.rc.dao.MenuRepository;
import com.reacheng.rc.entity.Gadmin;
import com.reacheng.rc.entity.Menu;
import com.reacheng.rc.entity.User;
import com.reacheng.rc.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service(value = "menuService")
public class MenuServiceImpl extends BaseServiceImpl <Menu, Integer> implements MenuService {


    @Autowired
    private MenuRepository menuRepository;
    @Override
    public List <Menu> getRecursionList(List <Menu> list) {
        return this.getRecursionList(list,0);
    }

    @Override
    public List <Menu> getRecursionList(List <Menu> list, int parent_id) {
        List<Menu> new_menu = new ArrayList();

        for (Menu user:list){
            if(user.getParentId() == parent_id){
                List<Menu> children = this.getRecursionList(list,user.getMenuId());
                if(children.size()>0){
                    user.setChildren(children);
                }
                new_menu.add(user);
            }
        }
        return new_menu;
    }

    @Override
    public List <Menu> getUserMenus(User user) {
        Gadmin gadmin = user.getGadmin();
        List<Menu> list = new ArrayList<Menu>(gadmin.getMenus());
        Collections.sort(list, new Comparator <Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
               return o1.getMenuId().compareTo(o2.getMenuId());
            }
        });
        return list;
    }

    @Override
    public List <Menu> getParentId(int parent_id) {
        return menuRepository.getParentId(parent_id);
    }


}
