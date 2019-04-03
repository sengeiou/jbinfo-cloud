package cn.jbinfo.cloud.core.condition;

import java.io.Serializable;


/**
 * <code>ListPageQuery.java</code>
 * <p>功能:List查询封装对象
 */
public class ListPageQuery implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -2504823418194131828L;

    private Pagination pagination;
    private ConditionMap conditions;

    //每页行数
    private Integer rows;
    //当前页
    private Integer page;

    public Pagination getPagination() {
        if (pagination == null) {
            pagination = new Pagination();
            if (rows != null)
                pagination.setPageSize(rows);
            if (page != null)
                pagination.setCurrentPage(page);
        }
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public ConditionMap getConditions() {
        if (conditions == null)
            conditions = new ConditionMap();
        return conditions;
    }

    public void setConditions(ConditionMap conditions) {
        this.conditions = conditions;
    }


    public void setRows(Integer rows) {
        this.rows = rows;
    }


    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public String toString() {
        return "ListPageQuery [pagination=" + pagination + ", conditions="
                + conditions + "]";
    }

}
