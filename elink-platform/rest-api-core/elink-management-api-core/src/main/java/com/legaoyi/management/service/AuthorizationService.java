package com.legaoyi.management.service;

import java.util.List;

/**
 * @author gaoshengbo
 */
public interface AuthorizationService {

    /**
     * 获取企业有权限查看的菜单项目
     *
     * @param enterpriseId
     * @param categoryId
     * @return List
     * @throws Exception
     */
    public List<?> getLinkMenu(String enterpriseId, String categoryId) throws Exception;

    /**
     * 获取企业有权限查看的菜单项目
     *
     * @param enterpriseId
     * @param roleId
     * @param categoryId
     * @return List
     * @throws Exception
     */
    public List<?> getLinkMenu(String enterpriseId, String roleId, String categoryId) throws Exception;

    /**
     * 获取企业有权限查看的菜单项目
     *
     * @param enterpriseId
     * @param categoryId
     * @return List
     * @throws Exception
     */
    public List<?> getLinkMenuByCategoryId(String enterpriseId, String categoryId) throws Exception;

    /**
     * 获取企业有权限查看的导航栏
     *
     * @param enterpriseId
     * @return List
     * @throws Exception
     */
    public List<?> getMenuNavigation(String enterpriseId) throws Exception;

    /**
     * 获取企业有权限查看的导航栏
     *
     * @param enterpriseId
     * @return List
     * @throws Exception
     */
    public List<?> getNavigation(String enterpriseId) throws Exception;

    /**
     * 获取企业有权限查看的导航栏
     *
     * @param enterpriseId
     * @param roleId
     * @return List
     * @throws Exception
     */
    public List<?> getNavigation(String enterpriseId, String roleId) throws Exception;
}
