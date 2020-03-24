package com.legaoyi.persistence.jpa.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.legaoyi.persistence.jpa.dao.GeneralDao;
import com.legaoyi.persistence.jpa.exception.IllegalEntityException;
import com.legaoyi.persistence.jpa.exception.IllegalEntityFieldException;
import com.legaoyi.persistence.jpa.model.Pagination;
import com.legaoyi.persistence.jpa.util.JsonUtil;
import com.legaoyi.persistence.jpa.util.Reflect2Entity;

/**
 * @author gaoshengbo
 */
@Repository("generalDao")
public class GeneralDaoImpl implements GeneralDao {

    public static final Map<String, String> operators = new HashMap<String, String>();

    private static final String ENTITY_METHOD_REMOVE = "remove";

    static {
        operators.put("gt", ">");
        operators.put("gte", ">=");
        operators.put("lt", "<");
        operators.put("lte", "<=");
        operators.put("eq", "=");
        operators.put("neq", "<>");
        operators.put("like", "like");
        operators.put("rlike", "like");
        operators.put("null", "null");
        operators.put("notNull", "notNull");
    }

    protected static final Logger logger = LoggerFactory.getLogger(GeneralDaoImpl.class);

    private EntityManager em;

    private static final Map<String, EntityType<?>> tables = new HashMap<String, EntityType<?>>();

    private String getSelectStr(String[] selectFields) {
        StringBuilder sb = new StringBuilder();
        if (selectFields != null) {
            sb.append("select ");
            boolean first = true;
            for (String field : selectFields) {
                if (!first) {
                    sb.append(",");
                } else {
                    first = false;
                }
                sb.append(field);
            }
        }
        sb.append(" ");
        return sb.toString();
    }

    private String getWhereStr(Map<String, Object> andCondition) {
        StringBuilder sb = new StringBuilder();
        if (andCondition != null && andCondition.size() > 0) {
            sb.append(" where ");
            boolean first = true;
            int index = 1;
            for (String key : andCondition.keySet()) {
                if (!first) {
                    sb.append(" and ");
                } else {
                    first = false;
                }
                int n = key.lastIndexOf(".");
                boolean set = false;
                if (n > 0) {
                    sb.append(key.substring(0, n));
                    String operator = operators.get(key.substring(n + 1)).toLowerCase();
                    if (operator != null) {
                        if (operator.equals("null")) {
                            sb.append(" is null ");
                            continue;
                        } else if (operator.equals("notNull")) {
                            sb.append(" is not null ");
                            continue;
                        }

                        sb.append(" ");
                        sb.append(operator);
                        sb.append("?");
                        sb.append(index++);
                        set = true;
                    }
                }
                if (!set) {
                    sb.append(key);
                    sb.append(" =?");
                    sb.append(index++);
                }

            }
        }
        return sb.toString();
    }

    private void setParameter(Class<?> entityType, Query q, Map<String, Object> andCondition) throws Exception {
        if (andCondition != null) {
            int i = 0;
            for (String key : andCondition.keySet()) {
                Object val = andCondition.get(key);
                int n = key.lastIndexOf(".");
                if (n > 0) {
                    String operator = operators.get(key.substring(n + 1)).toLowerCase();
                    if (operator != null && (operator.equals("null") || operator.equals("notNull"))) {
                        continue;
                    }

                    key = key.substring(0, n);
                    if (operator != null && "like".equals(operator)) {
                        val = "%" + val + "%";
                    } else if (operator != null && "rlike".equals(operator)) {
                        val = val + "%";
                    }
                }
                val = getFieldValue(entityType, key, val);
                if (val == null) {
                    new IllegalArgumentException(key);
                }
                q.setParameter(i + 1, val);
                i++;
            }
        }
    }

    private Object getFieldValue(Class<?> entityType, String key, Object value) throws Exception {
        Object entity = entityType.newInstance();
        Field[] fields = Reflect2Entity.getClassField(entity);
        int index = key.indexOf(".");
        if (index > 0) {
            String fieldName = key.substring(0, index);
            key = key.substring(index + 1);
            for (Field field : fields) {
                if (field.getName().equals(fieldName)) {
                    return getFieldValue(field.getType(), key, value);
                }
            }
        } else {
            Map<String, Object> m = new HashMap<String, Object>();
            m.put(key, value);
            Reflect2Entity.reflect(entity, m);
            for (Field field : fields) {
                if (field.getName().equals(key)) {
                    field.setAccessible(true);
                    return field.get(entity);
                }
            }
        }
        return null;
    }

    private void setParameter(Query q, Object... params) {
        if (params != null) {
            int i = 0;
            for (Object param : params) {
                q.setParameter(i + 1, param);
                i++;
            }
        }
    }

    private Object conver(Class<?> idType, String id) throws Exception {
        if (idType == Integer.class) {
            return Integer.valueOf(id);
        }

        if (idType == Long.class) {
            return Long.valueOf(id);
        }

        if (idType == Short.class) {
            return Short.valueOf(id);
        }
        return null;
    }

    private String getEntityName(EntityType<?> et) {
        return et.getName();
    }

    private Class<?> getEntityType(EntityType<?> et) {
        return et.getBindableJavaType();
    }

    private Class<?> getIdType(EntityType<?> et) {
        Type<?> t = et.getIdType();
        if (null != t) {
            return t.getJavaType();
        }
        return null;
    }

    private Object newEntity(String entityName) throws Exception {
        Class<?> entityClass = getEntityType(tables.get(entityName.toLowerCase()));
        if (null != entityClass) {
            return entityClass.newInstance();
        } else {
            try {
                return Class.forName(entityName).newInstance();
            } catch (Exception e) {
                throw new IllegalEntityException(entityName);
            }
        }
    }

    private void checkField(String entityName, String[] fields) {
        if (fields != null) {
            for (String fieldName : fields) {
                checkField(entityName, fieldName);
            }
        }
    }

    private void checkField(String entityName, String fieldName) {
        if (!this.isExistField(entityName, fieldName)) {
            throw new IllegalEntityFieldException(entityName, fieldName);
        }
    }

    private void checkCondition(String entityName, Map<String, Object> andCondition) {
        if (andCondition != null) {
            for (String key : andCondition.keySet()) {
                int n = key.indexOf(".");
                if (n > 0) {
                    checkField(entityName, key.substring(0, n));
                } else {
                    checkField(entityName, key);
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private static Field[] getClassField(Class cls) {
        Field[] fields = cls.getDeclaredFields();
        Class superclass = cls.getSuperclass();
        if (superclass != null) {
            Field[] fields1 = superclass.getDeclaredFields();
            if (fields1 != null) {
                Field[] fields2 = new Field[fields.length + fields1.length];
                System.arraycopy(fields, 0, fields2, 0, fields.length);
                System.arraycopy(fields1, 0, fields2, fields.length, fields1.length);
                return fields2;
            }
        }
        return fields;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        for (EntityType<?> et : em.getMetamodel().getEntities()) {
            String en = et.getName();
            tables.put(en.toLowerCase(), et);
            tables.put(en.toLowerCase().concat("s"), et);
        }
        this.em = em;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Object get(String entityName, Object id) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("get entityName={},id={}", entityName, JsonUtil.covertObjectToString(id));
        }

        EntityType<?> et = tables.get(entityName.toLowerCase());
        if (et == null) {
            throw new IllegalEntityException(entityName);
        }

        Class<?> idType = getIdType(et);
        Class<?> entityType = getEntityType(et);
        if (id.getClass() == idType) {
            return em.find(entityType, id);
        } else if (id instanceof String) {
            return em.find(entityType, conver(idType, (String) id));
        } else if (id instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) id;
            Object pk = idType.newInstance();
            Reflect2Entity.reflect(pk, map);
            return em.find(entityType, pk);
        }

        throw new IllegalArgumentException(" illegal id type :" + id.getClass());
    }

    @Override
    public List<?> findAll(String entityName) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("findAll,entityName={}", entityName);
        }
        return find(entityName, null, false, null);
    }

    @Override
    public List<?> findAll(String entityName, String[] selectFields) throws Exception {
        return find(entityName, selectFields, null, false, null);
    }

    @Override
    public List<?> findBySql(String sql) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("findBySql,sql={}", sql);
        }
        Query q = em.createNativeQuery(sql);
        q.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return q.getResultList();
    }

    @Override
    public List<?> findBySql(String sql, Object... params) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("findBySql,sql={},params={}", sql, JsonUtil.covertObjectToString(params));
        }
        Query q = em.createNativeQuery(sql);
        q.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        setParameter(q, params);
        return q.getResultList();
    }

    @Override
    public List<?> findBySqlWithPage(String sql, int pageSize, int pageNo, Object... params) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("findBySql,sql={},pageSize={},,pageNo={},params={}", sql, pageSize, pageNo, JsonUtil.covertObjectToString(params));
        }
        Query q = em.createNativeQuery(sql);
        if (pageSize > 0) {
            pageNo = pageNo > 0 ? pageNo : 1;
            q.setFirstResult((pageNo - 1) * pageSize);
            q.setMaxResults(pageSize);
        }
        q.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        setParameter(q, params);
        return q.getResultList();
    }

    @Override
    public Pagination pageFindBySql(String sql, int pageSize, int pageNo, Object... params) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("pageFindBySql,sql={},pageSize={},,pageNo={},params={}", sql, pageSize, pageNo, JsonUtil.covertObjectToString(params));
        }

        Query q = em.createNativeQuery(sql);
        if (pageSize > 0) {
            pageNo = pageNo > 0 ? pageNo : 1;
            q.setFirstResult((pageNo - 1) * pageSize);
            q.setMaxResults(pageSize);
        }
        q.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        setParameter(q, params);
        int totalCount = (int) countBySql(sql, params);
        return new Pagination(pageNo, pageSize, totalCount, q.getResultList());
    }

    @Override
    public List<?> findByHql(String hql) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("findByHql,hql={}", hql);
        }
        return em.createQuery(hql).getResultList();
    }

    @Override
    public List<?> findByHql(String hql, Object... params) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("findByHql,hql={},params={}", hql, JsonUtil.covertObjectToString(params));
        }
        Query q = em.createQuery(hql);
        setParameter(q, params);
        return q.getResultList();
    }

    @Override
    public Pagination pageFindByHql(String hql, int pageSize, int pageNo, Object... params) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("pageFindByHql,hql={},pageSize={},pageNo={},params={}", hql, pageSize, pageNo, JsonUtil.covertObjectToString(params));
        }

        Query q = em.createQuery(hql);
        if (pageSize > 0) {
            pageNo = pageNo > 0 ? pageNo : 1;
            q.setFirstResult((pageNo - 1) * pageSize);
            q.setMaxResults(pageSize);
        }
        int totalCount = (int) countByHql(hql, params);
        setParameter(q, params);
        return new Pagination(pageNo, pageSize, totalCount, q.getResultList());
    }

    @Override
    public List<?> find(String entityName, String orderBy, boolean desc, Map<String, Object> andCondition) throws Exception {
        return find(entityName, null, orderBy, desc, andCondition);
    }

    @Override
    public List<?> find(String entityName, String orderBy, boolean desc, int pageSize, int pageNo, Map<String, Object> andCondition) throws Exception {
        return find(entityName, null, orderBy, desc, 0, 0, andCondition);
    }

    @Override
    public List<?> find(String entityName, String[] selectFields, String orderBy, boolean desc, Map<String, Object> andCondition) throws Exception {
        return this.find(entityName, selectFields, orderBy, desc, 0, 0, andCondition);
    }

    @Override
    public List<?> find(String entityName, String[] selectFields, String orderBy, boolean desc, int pageSize, int pageNo, Map<String, Object> andCondition) throws Exception {
        EntityType<?> et = tables.get(entityName.toLowerCase());
        if (et == null) {
            throw new IllegalEntityException(entityName);
        }

        checkCondition(entityName, andCondition);
        checkField(entityName, selectFields);

        StringBuilder sb = new StringBuilder();
        sb.append(getSelectStr(selectFields));
        sb.append(" FROM ");
        sb.append(getEntityName(et));
        sb.append(getWhereStr(andCondition));

        if (orderBy != null && !"".equals(orderBy.trim())) {
            checkField(entityName, orderBy);

            sb.append(" ORDER BY  ");
            sb.append(orderBy);
            if (desc) {
                sb.append(" DESC  ");
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("find,hql={},andCondition={}", sb.toString(), JsonUtil.covertObjectToString(andCondition));
        }

        Query q = null;
        if (selectFields != null && selectFields.length > 0) {
            q = em.createQuery(sb.toString());
        } else {
            q = em.createQuery(sb.toString(), getEntityType(et));
        }

        if (pageSize > 0) {
            pageNo = pageNo > 0 ? pageNo : 1;
            q.setFirstResult((pageNo - 1) * pageSize);
            q.setMaxResults(pageSize);
        }
        setParameter(getEntityType(et), q, andCondition);
        return q.getResultList();
    }

    @Override
    public Pagination pageFind(String entityName, String orderBy, boolean desc, int pageSize, int pageNo, Map<String, Object> andCondition) throws Exception {
        return pageFind(entityName, null, orderBy, desc, pageSize, pageNo, andCondition);
    }

    @Override
    public Pagination pageFind(String entityName, String[] selectFields, String orderBy, boolean desc, int pageSize, int pageNo, Map<String, Object> andCondition) throws Exception {
        EntityType<?> et = tables.get(entityName.toLowerCase());
        if (et == null) {
            throw new IllegalEntityException(entityName);
        }

        checkCondition(entityName, andCondition);
        checkField(entityName, selectFields);

        StringBuilder sb = new StringBuilder();
        sb.append(getSelectStr(selectFields));
        sb.append(" FROM ");
        sb.append(getEntityName(et));
        sb.append(getWhereStr(andCondition));

        if (orderBy != null && !"".equals(orderBy.trim())) {
            checkField(entityName, orderBy);
            sb.append(" ORDER BY  ");
            sb.append(orderBy);

            if (desc) {
                sb.append(" DESC  ");
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("find,hql={},pageSize={},pageNo={},andCondition={}", sb.toString(), pageSize, pageNo, JsonUtil.covertObjectToString(andCondition));
        }

        Query q = null;
        if (selectFields != null && selectFields.length > 0) {
            q = em.createQuery(sb.toString());
        } else {
            q = em.createQuery(sb.toString(), getEntityType(et));
        }

        if (pageSize > 0) {
            pageNo = pageNo > 0 ? pageNo : 1;
            q.setFirstResult((pageNo - 1) * pageSize);
            q.setMaxResults(pageSize);
        }

        setParameter(getEntityType(et), q, andCondition);
        int totalCount = (int) count(entityName, andCondition);
        return new Pagination(pageNo, pageSize, totalCount, q.getResultList());
    }

    @Override
    public long count(String entityName, Map<String, Object> andCondition) throws Exception {
        EntityType<?> et = tables.get(entityName.toLowerCase());
        if (et == null) {
            throw new IllegalEntityException(entityName);
        }
        checkCondition(entityName, andCondition);

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(*) FROM ");
        sb.append(getEntityName(et));
        sb.append(getWhereStr(andCondition));
        if (logger.isDebugEnabled()) {
            logger.debug("count,hql={},andCondition={}", sb.toString(), JsonUtil.covertObjectToString(andCondition));
        }

        Query q = em.createQuery(sb.toString(), Long.class);
        setParameter(getEntityType(et), q, andCondition);
        return (Long) q.getSingleResult();
    }

    @Override
    public long countByHql(String hql, Object... params) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("countByHql,hql={},params=", hql, JsonUtil.covertObjectToString(params));
        }
        String s = hql.toUpperCase();
        int index = s.indexOf("FROM");
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(*)  ");
        sb.append(hql.substring(index));
        Query q = em.createQuery(sb.toString(), Long.class);
        setParameter(q, params);
        return (Long) q.getSingleResult();
    }

    @Override
    public long countBySql(String sql, Object... params) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("countBySql,sql={},params={}", sql, JsonUtil.covertObjectToString(params));
        }
        String s = sql.toUpperCase();
        int index = s.indexOf("FROM");
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT COUNT(*)  ");
        sb.append(sql.substring(index));
        Query q = em.createNativeQuery(sb.toString());
        setParameter(q, params);
        return ((BigInteger) q.getSingleResult()).longValue();
    }

    @Override
    public Object persist(String entityName, Map<String, Object> entity) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("persist entityName={},entity={}", entityName, JsonUtil.covertObjectToString(entity));
        }

        Object ent = newEntity(entityName);
        Reflect2Entity.reflect(ent, entity);
        em.persist(ent);
        em.flush();
        return ent;
    }

    @Override
    public Object persist(Object entity) throws Exception {
        if (em.contains(entity)) {
            entity = em.merge(entity);
        } else {
            em.persist(entity);
        }
        em.flush();
        return entity;
    }

    @Override
    public Object merge(String entityName, Object id, Map<String, Object> entity) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("merge entityName={},id={},entity={}", entityName, JsonUtil.covertObjectToString(id), JsonUtil.covertObjectToString(entity));
        }

        Object et = get(entityName, id);
        if (et == null) {
            throw new IllegalEntityException(entityName);
        }

        Reflect2Entity.reflect(et, entity);
        return em.merge(et);
    }

    @Override
    public Object merge(Object entity) throws Exception {
        return em.merge(entity);
    }

    @Override
    public void delete(String entityName, Object id) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("delete entityName={},id={}", entityName, JsonUtil.covertObjectToString(id));
        }

        Object entity = get(entityName, id);
        if (entity != null) {
            Class<?> clazz = entity.getClass();
            try {
                Method remove = clazz.getDeclaredMethod(ENTITY_METHOD_REMOVE);
                if (remove != null) {
                    remove.invoke(entity);
                }
            } catch (Exception e) {

            }

            em.remove(em.contains(entity) ? entity : em.merge(entity));
            em.flush();
        }
    }

    @Override
    public void delete(String entityName, Map<String, Object> andCondition) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("delete entityName={},andCondition={}", entityName, JsonUtil.covertObjectToString(andCondition));
        }

        checkCondition(entityName, andCondition);

        List<?> list = find(entityName, null, false, andCondition);
        if (list != null && !list.isEmpty()) {
            for (Object entity : list) {
                em.remove(entity);
            }
            em.flush();
        }
    }

    @Override
    public void delete(Object entity) throws Exception {
        em.remove(em.contains(entity) ? entity : em.merge(entity));
        em.flush();
    }
    
    /**
     * 
     * @param sql
     * @param params
     * @return
     * @throws Exception
     */
    public void deleteBySql(String sql, Object... params) throws Exception{
        if (logger.isDebugEnabled()) {
            logger.debug("findBySql,sql={},params={}", sql, JsonUtil.covertObjectToString(params));
        }
        Query q = em.createNativeQuery(sql);
        setParameter(q, params);
        q.executeUpdate();
    }

    @Override
    public boolean isExistField(String entityName, String fieldName) {
        EntityType<?> et = tables.get(entityName.toLowerCase());
        if (et == null) {
            throw new IllegalEntityException(entityName);
        }
        String realFieldName = null;
        int index = fieldName.indexOf(".");
        if (index != -1) {
            realFieldName = fieldName.substring(0, index);
        } else {
            realFieldName = fieldName;
        }
        Class<?> entityClass = getEntityType(et);
        Field[] fields = getClassField(entityClass);
        for (Field field : fields) {
            if (realFieldName.equals(field.getName())) {
                return true;
            }
        }
        return false;
    }
}
