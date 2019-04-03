package cn.jbinfo.cloud.jpa.data;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiaobin
 * @create 2017-09-27 下午12:19
 **/
@Getter
@Setter
public class DataTableParameter {

    /*private List<DataTableOrder> order;

    private List<DataTableColumn> columns;*/

    private Integer start;

    private Integer length;

    //private Map<Search, String> search = new HashMap<>();

    private List<Map<Order, String>> order = new ArrayList<>();

    private List<Map<Column, String>> columns = new ArrayList<>();

    public DataTableParameter() {

    }

    private String alias;

    public enum Order {
        column,
        dir
    }

    public enum Column {
        data,
        name,
        searchable,
        orderable
    }

    public Sort getSort() {
        if (order != null && order.size() > 0) {
            List<Sort.Order> orderList = Lists.newArrayList();
            Sort.Order sortOrder;
            for (Map<Order, String> tableOrder : order) {
                Map<Column, String> col = columns.get(NumberUtils.toInt(tableOrder.get(Order.column), 0));
                if (StringUtils.isBlank(alias)) {
                    sortOrder = new Sort.Order(Sort.Direction.ASC, col.get(Column.data));
                } else {
                    sortOrder = new Sort.Order(Sort.Direction.ASC, alias + "." + col.get(Column.data));
                }
                orderList.add(sortOrder);
            }
            return Sort.by(orderList);
        }
        return null;
    }

    public PageRequest getPageRequest() {
        int pageNo = start / length;
        return PageRequest.of(pageNo, length, getSort());
    }


}
