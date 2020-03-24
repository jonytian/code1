package com.legaoyi.persistence.jpa.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.legaoyi.persistence.jpa.page.Paginable;
import com.legaoyi.persistence.jpa.page.SimplePage;

/**
 * 列表分页。包含list属性。
 *
 *
 */
@SuppressWarnings("serial")
@XmlRootElement
public class Pagination extends SimplePage implements java.io.Serializable, Paginable {

    public Pagination() {}

    /**
     * 构造器
     *
     * @param pageNo 页码
     * @param pageSize 每页几条数据
     * @param total 总共几条数据
     */
    public Pagination(int pageNo, int pageSize, int total) {
        super(pageNo, pageSize, total);
    }

    /**
     * 构造器
     *
     * @param pageNo 页码
     * @param pageSize 每页几条数据
     * @param total 总共几条数据
     * @param rows 分页内容
     */
    public Pagination(int pageNo, int pageSize, int total, List<?> rows) {
        super(pageNo, pageSize, total);
        this.rows = rows;
    }

    /**
     * 第一条数据位置
     *
     * @return
     */
    public int getFirstResult() {
        return (this.pageNo - 1) * this.pageSize;
    }

    /**
     * 当前页的数据
     */
    private List<?> rows;

    /**
     * 获得分页内容
     *
     * @return
     */
    @XmlElementWrapper(name="rows") 
    @XmlElement(name = "row")
    public List<?> getRows() {
        return this.rows;
    }

    /**
     * 设置分页内容
     *
     * @param rows
     */
    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
