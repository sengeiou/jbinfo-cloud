package cn.jbinfo.cloud.jpa.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页的进一步封装，结合jquery.table
 *
 * @author xiaobin
 * @create 2017-09-27 上午11:12
 **/
@Getter
@Setter
public class PageTable<T> implements Serializable{

    private List<T> data;

    private Integer draw;

    private Integer recordsTotal;

    //总条数
    private long recordsFiltered;

    public PageTable() {
    }


    public PageTable(Page<T> page) {
        this.data = page.getContent();
        this.recordsFiltered = page.getTotalElements();
        this.recordsTotal = page.getTotalPages();
    }

    public PageTable(Page<T> page, Integer draw) {
        this.data = page.getContent();
        this.recordsFiltered = page.getTotalElements();
        this.recordsTotal = page.getTotalPages();
        this.draw = draw;
    }
}
