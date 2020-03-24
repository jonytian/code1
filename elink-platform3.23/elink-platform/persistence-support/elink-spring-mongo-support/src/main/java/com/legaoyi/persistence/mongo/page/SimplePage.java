package com.legaoyi.persistence.mongo.page;

/**
 * 简单分页类
 *
 *
 */
public class SimplePage implements Paginable {

    protected int total = 0;

    protected int pageSize = DEF_COUNT;

    protected int pageNo = 1;

    public static final int DEF_COUNT = 15;

    public SimplePage() {}

    /**
     * 构造器
     *
     * @param pageNo 页码
     * @param pageSize 每页几条数据
     * @param total 总共几条数据
     */
    public SimplePage(int pageNo, int pageSize, int total) {
        setTotal(total);
        setPageSize(pageSize);
        setPageNo(pageNo);
        adjustPageNo();
    }

    /**
     * 检查页码 checkPageNo
     *
     * @param pageNo
     * @return if pageNo==null or pageNo<1 then return 1 else return pageNo
     */
    public static int cpn(Integer pageNo) {
        return (pageNo == null || pageNo < 1) ? 1 : pageNo;
    }

    /**
     * 调整页码，使不超过最大页数
     */
    public void adjustPageNo() {
        if (this.pageNo == 1) {
            return;
        }
        int tp = getPages();
        if (this.pageNo > tp) {
            this.pageNo = tp;
        }
    }

    /**
     * 获得页码
     */
    @Override
    public int getPageNo() {
        return this.pageNo;
    }

    /**
     * 每页几条数据
     */
    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    /**
     * 总共几条数据
     */
    @Override
    public int getTotal() {
        return this.total;
    }

    /**
     * 总共几页
     */
    @Override
    public int getPages() {
        int totalPage = this.total / this.pageSize;
        if (totalPage == 0 || this.total % this.pageSize != 0) {
            totalPage++;
        }
        return totalPage;
    }

    /**
     * 是否第一页
     */
    @Override
    public boolean isFirstPage() {
        return this.pageNo <= 1;
    }

    /**
     * 是否最后一页
     */
    @Override
    public boolean isLastPage() {
        return this.pageNo >= getPages();
    }

    /**
     * 下一页页码
     */
    @Override
    public int getNextPage() {
        if (isLastPage()) {
            return this.pageNo;
        } else {
            return this.pageNo + 1;
        }
    }

    /**
     * 上一页页码
     */
    @Override
    public int getPrePage() {
        if (isFirstPage()) {
            return this.pageNo;
        } else {
            return this.pageNo - 1;
        }
    }

    /**
     * if totalCount<0 then totalCount=0
     *
     * @param total
     */
    public void setTotal(int total) {
        if (total < 0) {
            this.total = 0;
        } else {
            this.total = total;
        }
    }

    /**
     * if pageSize< 1 then pageSize=DEF_COUNT
     *
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        if (pageSize < 1) {
            this.pageSize = DEF_COUNT;
        } else {
            this.pageSize = pageSize;
        }
    }

    /**
     * if pageNo < 1 then pageNo=1
     *
     * @param pageNo
     */
    public void setPageNo(int pageNo) {
        if (pageNo < 1) {
            this.pageNo = 1;
        } else {
            this.pageNo = pageNo;
        }
    }
}
