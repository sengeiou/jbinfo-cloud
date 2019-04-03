package cn.jbinfo.cloud.core.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  <code>ConditionMap.java</code>
 *  <p>功能:条件Map扩展，主要为了统一方便管理
 */
public class ConditionMap extends HashMap<String, Object> {

	List<Condition> items = new ArrayList<Condition>();

	@Override
	public void clear() {
		items.clear();
		super.clear();
	}

	@Override
	public Object remove(Object key) {
		Condition condition = this.get(key);
		items.remove(condition);
		return super.remove(key);
	}

	public List<Condition> getItems() {
		return items;
	}

	@Override
	public Condition get(Object key) {
		Condition condition = (Condition) super.get(key);
		if (condition == null) {
			condition = Condition.parseCondition(key.toString());
			put(key.toString(), condition);
		}
		return condition;
	}

	@Override
	public Condition put(String key, Object value) {
		items.add((Condition) value);
		return (Condition) super.put(key, value);
	}

	// private

	/**
	 * 
	 */
	private static final long serialVersionUID = 2999593154628884149L;

}
