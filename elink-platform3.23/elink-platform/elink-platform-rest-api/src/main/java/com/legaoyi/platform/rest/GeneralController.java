package com.legaoyi.platform.rest;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.legaoyi.common.util.Result;
import com.legaoyi.persistence.jpa.exception.IllegalEntityException;
import com.legaoyi.persistence.jpa.exception.IllegalEntityFieldException;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.platform.ext.service.ExtendService;

/**
 * restful风格的通用crud接口
 *
 * @author gaoshengbo
 *
 */
@RestController("generalController")
@RequestMapping(value = "/common", produces = {"application/json"})
public class GeneralController extends BaseController {

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    /**
     * 根据id查找,http Get方法请求，url样例：http://localhost:8080/aas/api/common/user/1.json
     *
     * @param entityName
     * @param id
     * @param userType
     * @return Result
     * @throws Exception
     */
    @RequestMapping(value = {"/{entityName}/{id}"}, method = {RequestMethod.GET})
    public Result get(@PathVariable String entityName, @PathVariable String id, @RequestHeader(name = "_userType") Short userType) throws Exception {
        ExtendService extendService = getExtendService(entityName);
        Object obj = null;
        if (extendService != null) {
            obj = extendService.get(id);
        } else {
            obj = this.service.get(entityName, id);
        }
        return new Result(obj);
    }

    /**
     * 条件查询
     *
     * @param entityName
     * @param select
     * @param orderBy
     * @param isParent
     * @param desc
     * @param pageSize
     * @param pageNo
     * @param countable
     * @param enterpriseId
     * @param form
     * @return Result
     * @throws Exception
     */
    @RequestMapping(value = {"query/{entityName}"}, method = {RequestMethod.POST})
    public Result query(@RequestHeader(name = "_enterpriseId") String enterpriseId, @PathVariable String entityName,
            @RequestParam(required = false) String select, @RequestParam(required = false) String orderBy, @RequestParam(required = false) boolean isParent,
            @RequestParam(required = false) boolean desc, @RequestParam(defaultValue = "20") int pageSize, @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(required = false) boolean countable, @RequestBody Map<String, Object> form) throws Exception {
        String[] selectFields = null;
        if (!StringUtils.isBlank(select)) {
            selectFields = select.split(",");
        }

        // 要求每个entity都必须包含enterpriseId属性，否则报错
        boolean isExistEnterpriseId = false;
        try {
            isExistEnterpriseId = this.service.isExistField(entityName, "enterpriseId");
        } catch (IllegalEntityException e) {
            isExistEnterpriseId = true;
        } catch (IllegalEntityFieldException e) {
            isExistEnterpriseId = false;
        }

        if (isExistEnterpriseId) {
            boolean bool = false;
            for (String key : form.keySet()) {
                if (key.startsWith("enterpriseId.")) {// 是否下级企业
                    String postEnterpriseId = (String) form.get(key);
                    if (StringUtils.isNotBlank(postEnterpriseId) && postEnterpriseId.startsWith(enterpriseId)) {
                        bool = true;
                        break;
                    }
                }
            }
            if (!bool) {
                if (isParent) {
                    String postEnterpriseId = (String) form.get("enterpriseId");
                    // 上级企业可以查询下级企业数据
                    enterpriseId = StringUtils.isNotBlank(postEnterpriseId) && postEnterpriseId.startsWith(enterpriseId) ? postEnterpriseId : enterpriseId;
                    form.put("enterpriseId.rlike", enterpriseId);
                } else {
                    form.put("enterpriseId.eq", enterpriseId);
                }
            }
        }

        ExtendService extendService = getExtendService(entityName);
        if (extendService != null) {
            return new Result(extendService.query(selectFields, orderBy, desc, pageSize, pageNo, countable, form));
        }

        if (countable) {
            return new Result(this.service.pageFind(entityName, selectFields, orderBy, desc, pageSize, pageNo, form));
        }
        return new Result(this.service.find(entityName, selectFields, orderBy, desc, pageSize, pageNo, form));
    }

    /**
     * 条件统计记录
     *
     * @param entityName
     * @param enterpriseId
     * @param isParent
     * @param form
     * @return Result
     * @throws Exception
     */
    @RequestMapping(value = {"count/{entityName}"}, method = {RequestMethod.POST})
    public Result count(@PathVariable String entityName, @RequestHeader(name = "_enterpriseId") String enterpriseId,
            @RequestParam(required = false) boolean isParent, @RequestBody Map<String, Object> form) throws Exception {
        // 要求每个entity都必须包含enterpriseId属性，否则报错
        boolean isExistEnterpriseId = false;
        try {
            isExistEnterpriseId = this.service.isExistField(entityName, "enterpriseId");
        } catch (IllegalEntityException e) {
            isExistEnterpriseId = true;
        } catch (IllegalEntityFieldException e) {
            isExistEnterpriseId = false;
        }

        if (isExistEnterpriseId) {
            boolean bool = false;
            for (String key : form.keySet()) {
                if (key.startsWith("enterpriseId.")) {// 是否下级企业
                    String postEnterpriseId = (String) form.get(key);
                    if (StringUtils.isNotBlank(postEnterpriseId) && postEnterpriseId.startsWith(enterpriseId)) {
                        bool = true;
                        break;
                    }
                }
            }
            if (!bool) {
                if (isParent) {
                    String postEnterpriseId = (String) form.get("enterpriseId");
                    // 上级企业可以查询下级企业数据
                    enterpriseId = StringUtils.isNotBlank(postEnterpriseId) && postEnterpriseId.startsWith(enterpriseId) ? postEnterpriseId : enterpriseId;
                    form.put("enterpriseId.rlike", enterpriseId);
                } else {
                    form.put("enterpriseId.eq", enterpriseId);
                }
            }
        }

        ExtendService extendService = getExtendService(entityName);
        if (extendService != null) {
            return new Result(extendService.count(form));
        }

        return new Result(this.service.count(entityName, form));
    }

    /**
     * 新增记录
     *
     * @param entityName
     * @param form
     * @param enterpriseId
     * @return Result
     * @throws Exception
     */
    @RequestMapping(value = {"/{entityName}"}, method = {RequestMethod.POST})
    public Result post(@PathVariable String entityName, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestBody Map<String, Object> form)
            throws Exception {
        // 要求每个entity都必须包含enterpriseId属性，否则报错
        String postEnterpriseId = (String) form.get("enterpriseId");
        // 系统管理员，下级子公司
        if (!StringUtils.isBlank(postEnterpriseId) && postEnterpriseId.startsWith(enterpriseId)) {
            form.put("enterpriseId", postEnterpriseId);
        } else {
            form.put("enterpriseId", enterpriseId);
        }
        ExtendService extendService = getExtendService(entityName);
        if (extendService != null) {
            return new Result(extendService.persist(form));
        }
        return new Result(this.service.persist(entityName, form));
    }

    /**
     * 修改记录
     *
     * @param entityName
     * @param id
     * @param enterpriseId
     * @param form
     * @return Result
     * @throws Exception
     */
    @RequestMapping(value = {"/{entityName}/{id}"}, method = {RequestMethod.PUT})
    public Result put(@PathVariable String entityName, @PathVariable String id, @RequestHeader(name = "_enterpriseId") String enterpriseId,
            @RequestBody Map<String, Object> form) throws Exception {
        return this.patch(entityName, id, enterpriseId, form);
    }

    /**
     * 增量修改记录
     *
     * @param entityName
     * @param id
     * @param enterpriseId
     * @param form
     * @return Result
     * @throws Exception
     */
    @RequestMapping(value = {"/{entityName}/{id}"}, method = {RequestMethod.PATCH})
    public Result patch(@PathVariable String entityName, @PathVariable String id, @RequestHeader(name = "_enterpriseId") String enterpriseId,
            @RequestBody Map<String, Object> form) throws Exception {
        String postEnterpriseId = (String) form.get("enterpriseId");
        // 系统管理员，下级子公司
        if (!StringUtils.isBlank(postEnterpriseId)) {
            form.put("enterpriseId", postEnterpriseId);
        } else {
            form.put("enterpriseId", enterpriseId);
        }
        ExtendService extendService = getExtendService(entityName);
        if (extendService != null) {
            return new Result(extendService.merge(id, form));
        }
        return new Result(this.service.merge(entityName, id, form));
    }

    /**
     * 删除
     *
     * @param entityName
     * @param id
     * @return Result
     * @throws Exception
     */
    @RequestMapping(value = {"/{entityName}/{id}"}, method = {RequestMethod.DELETE})
    public Result delete(@PathVariable String entityName, @PathVariable String id) throws Exception {
        String ids[] = id.split(",");
        ExtendService extendService = getExtendService(entityName);
        if (extendService != null) {
            extendService.delete(ids);
        } else {
            this.service.delete(entityName, ids);
        }
        return new Result();
    }
}
