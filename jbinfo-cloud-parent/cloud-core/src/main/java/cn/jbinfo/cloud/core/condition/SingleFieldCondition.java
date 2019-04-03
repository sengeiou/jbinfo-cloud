package cn.jbinfo.cloud.core.condition;

import cn.jbinfo.cloud.core.utils.DateUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * <code>SingleFieldCondition.java</code>
 * <p>功能:单个字段查询条件实现类
 */
public class SingleFieldCondition extends Condition implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4466041047921086476L;
    private String field;
    private Object value;
    private Object newValue;
    private String operator;
    private String type;


    public String getOperator() {
        return operator;
    }

    public Condition setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public String getField() {
        return field;
    }

    public Condition setField(String field) {
        this.field = field;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public Condition setType(String type) {
        this.type = type;
        return this;
    }

    public Object getNewValue() {
        return newValue;
    }

    /**
     * 校验是否 操作符
     */
    private static boolean isOperator(String str) {
        return "gt".equals(str) || "lt".equals(str) || "eq".equals(str) || "nq".equals(str) || "like".equals(str) || "rlike".equals(str) || "in".equals(str) || "nin".equals(str);
    }


    /**
     * 返回sql操作符
     */
    private static String getOperator(String str) {
        if ("gt".equals(str)) {
            return ">=";
        } else if ("lt".equals(str)) {
            return "<=";
        } else if ("in".equals(str)) {
            return "in";
        } else if ("nin".equals(str)) {  //not in操作符
            return "not in";
        } else if ("nq".equals(str)) {
            return "!=";
        } else if ("like".equals(str)) {
            return "like";
        } else if ("rlike".equals(str)) {
            return "rlike";
        } else {//eq 或其他
            return "=";
        }
    }


    /**
     * 设置value值
     */
    public Condition setValue(Object value) {
        this.value = value;
        if (value == null || value.toString().trim().equals("")) {
            return this;
        }
        //字符串类型
        if ("string".equals(type)) {
            if ("like".equals(operator)) {
                //对通配符进行转义
                String thisVal = value.toString().trim()
                        .replaceAll("_", "\\\\_")            //替换'_'
                        .replaceAll("%", "\\\\%")            //替换'%'
                        ;
                this.newValue = "%" + thisVal + "%";
            } else if ("rlike".equals(operator)) {
                //对通配符进行转义
                String thisVal = value.toString().trim()
                        .replaceAll("_", "\\\\_")            //替换'_'
                        .replaceAll("%", "\\\\%")            //替换'%'
                        ;
                this.newValue = thisVal + "%";
                this.operator = "like";
            } else if ("in".equals(operator) || "not in".equals(operator)) {
                if (this.value instanceof String) {
                    this.newValue = value.toString().trim().split(",");
                } else {
                    this.newValue = this.value;
                }
            } else {
                this.newValue = value.toString().trim();
            }
        }
        //日期类型
        else if ("date".equals(type)) {

            Date date = null;
            try {
                //gt 大于一个日期 + " 00:00:00"
                if (getOperator("gt").equals(operator)) {
                    date = DateUtils.parse(value.toString().trim() + " 00:00:00", DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS);
                    //减一秒操作
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.SECOND, -1);
                    date = calendar.getTime();
                } else if (getOperator("lt").equals(operator)) { //lt 小于一个日期 + " 23:59:59"
                    date = DateUtils.parse(value.toString().trim() + " 23:59:59", DateUtils.PATTERN_YYYY_MM_DD_HH_MM_SS);
                } else {
                    date = DateUtils.parse(value.toString().trim(), DateUtils.PATTERN_YYYY_MM_DD);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.newValue = date;
        } else if ("float".equals(type)) {
            this.newValue = Float.parseFloat(value.toString().trim());
        } else { //整型
            //其他 int 或  string 用,隔开的 字符串  转化为 String [] 类型
            if ("in".equals(operator) || "not in".equals(operator)) {
                if (this.value instanceof String) {
                    this.newValue = value.toString().trim().split(",");
                } else {
                    this.newValue = this.value;
                }
            } else {
                if (this.value instanceof Long) {
                    this.newValue = this.value;
                } else {
                    this.newValue = Integer.valueOf(this.value.toString().trim());
                }
            }

        }
        return this;
    }

    @Override
    protected void parse(String parameter) {
        String[] array = parameter.split(Condition.FIELD_INTERNAL_DELIMETER2);
        if (array.length <= 1) {
            array = parameter.split(Condition.FIELD_INTERNAL_DELIMETER);
        }
        String field = array[0];
        setField(field);

        //只有一个参数(字符串模糊查询) 字段
        if (array.length == 1) {
            setType("string");
            setOperator("like");
        } else if (array.length == 2) {  //只有二个参数            字段_操作符/字段_数据类型
            //第2个是操作符，则默认是字符类型
            if (isOperator(array[1])) {
                setOperator(getOperator(array[1]));
                setType("string");
            } else {    //字段_数据类型
                setOperator("=");
                setType(array[1]);
            }
        } else if (array.length == 3) { //有三个参数(非字符串查询)   字段名_类型_操作符
            setType(array[1]);
            setOperator(getOperator(array[2]));
        }
    }

    @Override
    public String toString() {
        return "SingleFieldCondition [field=" + field + ", value=" + value + ", newValue="
                + newValue + ", operator=" + operator + ", type=" + type + "]";
    }


    @Override
    public int hashCode() {
        return toString().hashCode();
    }


    public static void main(String[] args) {
        String s = "baidu_360_aaa_ca";
        String a = s.replaceAll("_", "\\\\_");
        System.out.println(a);

		/*
		Condition c = Condition.parseCondition("sex_int_gt");
		c.setValue("1");
		System.out.println(c.toString());
		*/
    }

}
