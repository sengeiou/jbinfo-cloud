package cn.jbinfo.cloud.jpa.specification.condition;

import org.springframework.data.domain.Range;

import javax.persistence.criteria.*;


public class BetweenSpecification<T> extends AbstractSpecification<T> {
    private final String property;
    private final Range range;

    public BetweenSpecification(String property, Range range) {
        this.property = property;
        this.range = range;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        //From from = getRoot(property, root);
        //String field = getProperty(property);
        //return cb.between(from.get(field), range.getLowerBound(), range.getUpperBound());
        throw new RuntimeException("不支持between");
    }
}
