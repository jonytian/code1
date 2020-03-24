package com.legaoyi.management.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.legaoyi.common.util.Result;
import com.legaoyi.management.service.AuthorizationService;
import com.legaoyi.management.service.EnterpriseService;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.platform.ext.service.ExtendService;
import com.legaoyi.platform.model.Enterprise;
import com.legaoyi.platform.model.User;
import com.legaoyi.platform.rest.BaseController;

@RestController("aasController")
@RequestMapping(value = "/aas", produces = {"application/json"})
public class AASController extends BaseController {

    @Autowired
    @Qualifier("enterpriseService")
    private EnterpriseService enterpriseService;

    @Autowired
    @Qualifier("authorizationService")
    private AuthorizationService authorizationService;

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    @RequestMapping(value = "/authentication", method = RequestMethod.GET)
    public Result authenticate() throws Exception {
        return new Result(SecurityContextHolder.getContext().getAuthentication().getDetails());
    }

    @RequestMapping(value = "/authorization/menu", method = RequestMethod.GET)
    public Result authorize(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) String categoryId) throws Exception {
        Map<?, ?> user = (Map<?, ?>) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String roleId = (String) user.get("roleId");
        List<?> list = null;
        if (!StringUtils.isBlank(roleId)) {
            list = this.authorizationService.getLinkMenu(enterpriseId, roleId, categoryId);
        }
        if (list == null) {
            list = this.authorizationService.getLinkMenu(enterpriseId, categoryId);
        }
        return new Result(list);
    }

    @RequestMapping(value = "/navigation", method = RequestMethod.GET)
    public Result navigation(@RequestHeader(name = "_enterpriseId") String enterpriseId) throws Exception {
        Map<?, ?> user = (Map<?, ?>) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String roleId = (String) user.get("roleId");
        List<?> list = null;
        if (!StringUtils.isBlank(roleId)) {
            list = this.authorizationService.getNavigation(enterpriseId, roleId);
        }
        if (list == null) {
            list = this.authorizationService.getNavigation(enterpriseId);
        }
        return new Result(list);
    }

    @RequestMapping(value = "/menu/navigation", method = RequestMethod.GET)
    public Result menuNavigation(@RequestHeader(name = "_enterpriseId") String enterpriseId) throws Exception {
        return new Result(this.authorizationService.getMenuNavigation(enterpriseId));
    }

    @RequestMapping(value = "/menu/{categoryId}", method = RequestMethod.GET)
    public Result menu(@RequestHeader(name = "_enterpriseId") String enterpriseId, @PathVariable String categoryId) throws Exception {
        return new Result(this.authorizationService.getLinkMenuByCategoryId(enterpriseId, categoryId));
    }

    @RequestMapping(value = "/enterprise/register", method = RequestMethod.POST)
    public Result register(@RequestBody Map<String, Object> form) throws Exception {
        return new Result(this.enterpriseService.register(form));
    }

    @RequestMapping(value = {"/user"}, method = {RequestMethod.POST, RequestMethod.PATCH})
    public Result post(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestHeader(name = "_userAccount") String userAccount, @RequestBody Map<String, Object> form) throws Exception {
        // 要求每个entity都必须包含enterpriseId属性，否则报错
        String enterpriseIdStr = (String) form.get("enterpriseId");
        if (StringUtils.isBlank(enterpriseId) || !enterpriseIdStr.startsWith(enterpriseId)) {
            return new Result(Result.RESP_CODE_ERROR, "非法参数！", null);
        }
        String id = (String) form.get("id");

        ExtendService extendService = getExtendService(User.ENTITY_NAME);
        if (extendService != null) {
            if (!StringUtils.isBlank(id)) {
                return new Result(extendService.merge(id, form));
            }
            return new Result(extendService.persist(form));
        } else {
            if (!StringUtils.isBlank(id)) {
                return new Result(this.service.merge(User.ENTITY_NAME, id, form));
            }
            return new Result(this.service.persist(User.ENTITY_NAME, form));
        }
    }

    @RequestMapping(value = "/query/user", method = RequestMethod.POST)
    public Result queryUser(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) String select, @RequestParam(required = false) String orderBy, @RequestParam(required = false) boolean desc,
            @RequestParam(defaultValue = "20") int pageSize, @RequestParam(defaultValue = "1") int pageNo, @RequestParam(required = false) boolean countable, @RequestBody Map<String, Object> form) throws Exception {
        String[] selectFields = null;
        if (!StringUtils.isBlank(select)) {
            selectFields = select.split(",");
        }
        String enterpriseIdcon = (String) form.get("enterpriseId.eq");
        if (StringUtils.isBlank(enterpriseIdcon)) {
            form.put("enterpriseId.eq", enterpriseId);
        } else if (!enterpriseIdcon.startsWith(enterpriseId)) {
            return new Result(Result.RESP_CODE_ERROR, "非法参数！", null);
        }
        if (countable) {
            return new Result(this.service.pageFind(User.ENTITY_NAME, selectFields, orderBy, desc, pageSize, pageNo, form));
        }
        return new Result(this.service.find(User.ENTITY_NAME, selectFields, orderBy, desc, pageSize, pageNo, form));
    }

    @RequestMapping(value = "/query/subsidiary", method = RequestMethod.POST)
    public Result querySubsidiary(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam(required = false) String select, @RequestParam(required = false) String orderBy, @RequestParam(required = false) boolean desc,
            @RequestParam(defaultValue = "20") int pageSize, @RequestParam(defaultValue = "1") int pageNo, @RequestParam(required = false) boolean countable, @RequestBody Map<String, Object> form) throws Exception {
        String[] selectFields = null;
        if (!StringUtils.isBlank(select)) {
            selectFields = select.split(",");
        }
        form.put("id.rlike", enterpriseId);
        if (countable) {
            return new Result(this.service.pageFind(Enterprise.ENTITY_NAME, selectFields, orderBy, desc, pageSize, pageNo, form));
        }
        return new Result(this.service.find(Enterprise.ENTITY_NAME, selectFields, orderBy, desc, pageSize, pageNo, form));
    }

    @GetMapping("/subsidiary")
    public Result getSubsidiary(@RequestHeader(name = "_enterpriseId") String userEnterpriseId, @RequestParam(required = false) String enterpriseId, @RequestParam(required = false) boolean isParent, @RequestParam(required = false) String select)
            throws Exception {
        String[] selectFields = null;
        if (!StringUtils.isBlank(select)) {
            selectFields = select.split(",");
        }

        enterpriseId = StringUtils.isBlank(enterpriseId) ? userEnterpriseId : enterpriseId;
        Map<String, Object> andCondition = new HashMap<String, Object>();
        if (isParent) {
            andCondition.put("parentId.rlike", enterpriseId);
        } else {
            andCondition.put("parentId.eq", enterpriseId);
        }

        return new Result(this.service.find(Enterprise.ENTITY_NAME, selectFields, "id", false, andCondition));
    }
}
