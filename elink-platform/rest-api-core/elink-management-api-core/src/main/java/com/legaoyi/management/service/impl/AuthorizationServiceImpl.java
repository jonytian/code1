
package com.legaoyi.management.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.legaoyi.common.util.Constants;
import com.legaoyi.management.service.AuthorizationService;
import com.legaoyi.persistence.jpa.service.GeneralService;

/**
 * @author gaoshengbo
 */
@Service("authorizationService")
public class AuthorizationServiceImpl implements AuthorizationService {

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    @Override
    @Cacheable(value = Constants.CACHE_NAME_AAS_NAVIGATION_CACHE, key = "#enterpriseId", unless = "#result == null")
    public List<?> getNavigation(String enterpriseId) throws Exception {
        List<?> categorys = null;
        long count = this.service.countBySql("SELECT role_id as roleId from system_role_enterprise where enterprise_id= ? ", enterpriseId);
        if (count > 0) {
            String sql = "SELECT id, name,url,icon FROM system_resource_category a "
                    + "WHERE a.type=1 AND a.parent_id = '-1' AND a.id IN ( SELECT DISTINCT b.parent_id "
                    + "	FROM system_resource_category b WHERE b.type=1 AND b.parent_id != '-1' "
                    + "	AND EXISTS ( SELECT 1 FROM (SELECT DISTINCT category_id FROM system_resource c "
                    + "	WHERE c.state= 1 AND EXISTS ( SELECT 1 FROM system_role_resource d WHERE c.id = d.resource_id AND d.role_id IN "
                    + " (SELECT role_id as roleId from system_role_enterprise where enterprise_id= ?))) e WHERE e.category_id = b.id)) order by order_by";
            categorys = this.service.findBySql(sql, enterpriseId);
        }
        if (categorys == null || categorys.isEmpty()) {
            String sql = "SELECT id, name,url,icon FROM system_resource_category WHERE type=1 AND parent_id = '-1' order by order_by";
            categorys = this.service.findBySql(sql);
        }
        return categorys;
    }

    @Override
    public List<?> getMenuNavigation(String enterpriseId) throws Exception {
        List<?> categorys = null;
        long count = this.service.countBySql("SELECT role_id as roleId from system_role_enterprise where enterprise_id= ? ", enterpriseId);
        if (count > 0) {
            String sql = "SELECT id, name,url,icon FROM system_resource_category a WHERE a.type=1 AND a.parent_id != '-1' "
                    + " AND EXISTS ( SELECT 1 FROM (SELECT DISTINCT category_id FROM system_resource c "
                    + " WHERE c.state= 1 AND EXISTS ( SELECT 1 FROM system_role_resource d WHERE c.id = d.resource_id AND d.role_id IN "
                    + " (SELECT role_id as roleId from system_role_enterprise where enterprise_id= ?))) e WHERE e.category_id = a.id) order by order_by";
            categorys = this.service.findBySql(sql, enterpriseId);
        }
        if (categorys == null || categorys.isEmpty()) {
            String sql = "SELECT id, name,url,icon FROM system_resource_category WHERE type=1 AND parent_id != '-1' order by order_by";
            categorys = this.service.findBySql(sql);
        }
        return categorys;
    }

    @Override
    @Cacheable(value = Constants.CACHE_NAME_AAS_NAVIGATION_ROLE_CACHE, key = "#roleId", unless = "#result == null")
    public List<?> getNavigation(String enterpriseId, String roleId) throws Exception {
        List<?> categorys = null;
        long count =
                this.service.countBySql("SELECT role_id as roleId from security_user_resource where enterprise_id= ? AND role_id = ?", enterpriseId, roleId);
        if (count > 0) {
            String sql = "SELECT id, name,url,icon FROM system_resource_category a "
                    + "WHERE a.type=1 AND a.parent_id = '-1' AND a.id IN ( SELECT DISTINCT b.parent_id "
                    + " FROM system_resource_category b WHERE b.type=1 AND b.parent_id != '-1' "
                    + " AND EXISTS ( SELECT 1 FROM (SELECT DISTINCT category_id FROM system_resource c "
                    + " WHERE c.state= 1 AND EXISTS ( SELECT 1 FROM security_user_resource d WHERE c.id = d.resource_id AND enterprise_id= ? AND d.role_id=? )) e WHERE e.category_id = b.id)) order by order_by";
            categorys = this.service.findBySql(sql, enterpriseId, roleId);
        }
        if (categorys != null && categorys.isEmpty()) {
            categorys = null;
        }
        return categorys;
    }

    @Override
    @Cacheable(value = Constants.CACHE_NAME_AAS_MENU_ROLE_CACHE, key = "#roleId+'_'+#categoryId", unless = "#result == null")
    public List<?> getLinkMenu(String enterpriseId, String roleId, String categoryId) throws Exception {
        List<?> resource = null;
        long count =
                this.service.countBySql("SELECT role_id as roleId from security_user_resource where enterprise_id= ? AND role_id = ?", enterpriseId, roleId);
        if (count > 0) {
            String sql = "select * from (SELECT c.id,c.name,c.order_by, c.url,a.name as categoryName,a.order_by as c_order_by, b.name as navName "
                    + " FROM system_resource_category a, system_resource_category b,system_resource c "
                    + " WHERE a.type=1 AND a.id = c.category_id and a.parent_id = b.id ";
            if (!StringUtils.isBlank(categoryId)) {
                sql += " and  b.id='" + categoryId + "'";
            }
            sql += " and c.state= 1 and c.type = 1 and EXISTS ( " + " SELECT 1  FROM security_user_resource d WHERE c.id = d.resource_id "
                    + " AND d.role_id IN ( SELECT role_id AS roleId FROM security_user_resource WHERE enterprise_id = ? AND role_id = ?))) t ORDER BY c_order_by,categoryName,order_by";
            resource = this.service.findBySql(sql, enterpriseId, roleId);
        }
        if (resource != null && resource.isEmpty()) {
            resource = null;
        }
        return resource;
    }

    @Override
    public List<?> getLinkMenuByCategoryId(String enterpriseId, String categoryId) throws Exception {
        List<?> resource = null;
        long count = this.service.countBySql("SELECT role_id as roleId from system_role_enterprise where enterprise_id= ? ", enterpriseId);
        if (count > 0) {
            String sql = "select id,name from (SELECT c.name,c.id,c.order_by, c.url,a.name as categoryName,a.order_by as c_order_by, b.name as navName "
                    + " FROM system_resource_category a, system_resource_category b,system_resource c "
                    + " WHERE a.type=1 AND a.id = c.category_id and a.parent_id = b.id and c.state= 1  and EXISTS ( "
                    + " SELECT 1  FROM system_role_resource d WHERE c.id = d.resource_id "
                    + " AND d.role_id IN ( SELECT role_id AS roleId FROM system_role_enterprise WHERE enterprise_id = ?)) AND a.id =? ) t ORDER BY c_order_by,categoryName,order_by";
            resource = this.service.findBySql(sql, enterpriseId, categoryId);
        }
        if (resource == null || resource.isEmpty()) {
            String sql = "select id,name from (SELECT c.name,c.id,c.order_by, c.url,a.name as categoryName,a.order_by as c_order_by, b.name as navName "
                    + "FROM system_resource_category a, system_resource_category b,system_resource c "
                    + "WHERE a.type=1 AND a.id = c.category_id and a.parent_id = b.id and c.state= 1 and a.id=?) a ORDER BY c_order_by,categoryName,order_by";
            resource = this.service.findBySql(sql, categoryId);
        }
        return resource;
    }

    @Override
    @Cacheable(value = Constants.CACHE_NAME_AAS_MENU_CACHE, key = "#enterpriseId+'_'+#categoryId", unless = "#result == null")
    public List<?> getLinkMenu(String enterpriseId, String categoryId) throws Exception {
        List<?> resource = null;
        long count = this.service.countBySql("SELECT role_id as roleId from system_role_enterprise where enterprise_id= ? ", enterpriseId);
        if (count > 0) {
            String sql = "select * from (SELECT c.id,c.name,c.order_by, c.url,a.name as categoryName,a.order_by as c_order_by, b.name as navName "
                    + " FROM system_resource_category a, system_resource_category b,system_resource c "
                    + " WHERE a.type=1 AND a.id = c.category_id and a.parent_id = b.id";
            if (!StringUtils.isBlank(categoryId)) {
                sql += " and  b.id='" + categoryId + "'";
            }
            sql += " and c.state= 1 and c.type = 1 and EXISTS ( " + " SELECT 1  FROM system_role_resource d WHERE c.id = d.resource_id "
                    + " AND d.role_id IN ( SELECT role_id AS roleId FROM system_role_enterprise WHERE enterprise_id = ?))) t ORDER BY c_order_by,categoryName,order_by";
            resource = this.service.findBySql(sql, enterpriseId);
        }
        if (resource == null || resource.isEmpty()) {
            String sql = "select * from (SELECT c.id,c.name,c.order_by, c.url,a.name as categoryName,a.order_by as c_order_by, b.name as navName "
                    + "FROM system_resource_category a, system_resource_category b,system_resource c "
                    + "WHERE a.type=1 AND a.id = c.category_id and a.parent_id = b.id ";
            if (!StringUtils.isBlank(categoryId)) {
                sql += " and  b.id='" + categoryId + "'";
            }
            sql += " and c.state= 1 and c.type = 1) a ORDER BY c_order_by,categoryName,order_by";
            resource = this.service.findBySql(sql);
        }
        return resource;
    }
}
