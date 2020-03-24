package com.legaoyi.management.rest;

import java.util.HashMap;
import java.util.List;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.legaoyi.common.util.Result;
import com.legaoyi.management.model.Dictionary;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.persistence.jpa.util.JsonUtil;
import com.legaoyi.platform.ext.service.ExtendService;
import com.legaoyi.platform.rest.BaseController;

@RestController("dictionaryController")
@RequestMapping(value = "/system", produces = {"application/json"})
public class DictionaryController extends BaseController {

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    @RequestMapping(value = "/objects", method = RequestMethod.GET)
    public Result getObjects() throws Exception {
        Map<String, Object> andCondition = Maps.newHashMap();
        andCondition.put("type.eq", 1);
        List<?> list = this.service.find(Dictionary.ENTITY_NAME, null, false, andCondition);
        List<Object> retList = Lists.newArrayList();
        if (list != null) {
            Dictionary dictionary = null;
            for (Object o : list) {
                dictionary = (Dictionary) o;
                retList.add(JsonUtil.convertStringToObject(dictionary.getContent(), Map.class));
            }
        }
        return new Result(retList);
    }

    @RequestMapping(value = "/object/{entity}/description", method = RequestMethod.GET)
    public Result getObjectDescription(@PathVariable String entity) throws Exception {
        return getDictionary(entity, 2);
    }

    @RequestMapping(value = "/message/{messageId}/description", method = RequestMethod.GET)
    public Result getMessageDescription(@PathVariable String messageId) throws Exception {
        return getDictionary(messageId, 3);
    }

    @RequestMapping(value = "/message/{messageId}/example", method = RequestMethod.GET)
    public Result getMessageExample(@PathVariable String messageId) throws Exception {
        return getDictionary(messageId, 4);
    }

    /**
     * 企业字典
     * 
     * @param type
     * @param enterpriseId
     * @param isParent
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/dictionary/{type}", method = RequestMethod.GET)
    public Result getDictionary(@PathVariable int type, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(defaultValue = "true") boolean isParent) throws Exception {
        Map<String, Object> condition = new HashMap<String, Object>();
        if (isParent && enterpriseId.length() > 9) {// 取父级企业定义的字典代码
            condition.put("enterpriseId.eq", enterpriseId.substring(0, 9));
        } else {
            condition.put("enterpriseId.eq", enterpriseId);
        }
        condition.put("type.eq", type);
        return new Result(this.service.find(Dictionary.ENTITY_NAME, null, false, condition));
    }

    /**
     * 系统全局字典
     * 
     * @param type
     * @param code
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sysDictionary/{type}", method = RequestMethod.GET)
    public Result getSysDictionary(@PathVariable int type, @RequestParam(required = false) String code) throws Exception {
        Map<String, Object> condition = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(code)) {
            condition.put("code.eq", code);
        }
        condition.put("type.eq", type);
        return new Result(this.service.find(Dictionary.ENTITY_NAME, null, false, condition));
    }

    /**
     * 字典基础资料条件查询
     *
     * @param entityName
     * @param select
     * @param orderBy
     * @param desc
     * @param pageSize
     * @param pageNo
     * @param countable
     * @param form
     * @return Result
     * @throws Exception
     */
    @RequestMapping(value = {"dictionary/{entityName}"}, method = {RequestMethod.POST})
    public Result queryDictionary(@PathVariable String entityName, @RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false, defaultValue = "true") boolean isParent, @RequestParam(required = false) String select,
            @RequestParam(required = false) String orderBy, @RequestParam(required = false) boolean desc, @RequestParam(defaultValue = "20") int pageSize, @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(required = false) boolean countable, @RequestBody Map<String, Object> form) throws Exception {
        String[] selectFields = null;
        if (!StringUtils.isBlank(select)) {
            selectFields = select.split(",");
        }

        if (!isParent) {
            form.put("enterpriseId.eq", enterpriseId);
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

    private Result getDictionary(String code, int type) throws Exception {
        Map<String, Object> andCondition = Maps.newHashMap();
        andCondition.put("code.eq", code);
        andCondition.put("type.eq", type);
        List<?> list = this.service.find(Dictionary.ENTITY_NAME, null, false, andCondition);
        if (list != null && !list.isEmpty()) {
            Dictionary dictionary = (Dictionary) list.get(0);
            return new Result(JsonUtil.convertStringToObject(dictionary.getContent(), Map.class));
        }
        return new Result(Result.RESP_CODE_ERROR, "参数有误", null);
    }
}
