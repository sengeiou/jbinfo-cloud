package cn.jbinfo.cloud.core.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import java.util.*;


/**
 *  <code>CollectionUtil.java</code>
 *  <p>功能:集合工具类,提供各种转换以及获取两个集合的交集等方法
 */
public class CollectionUtil {
	
	/**
	 * 判断集合是否为空
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Collection<?> obj) {
		return obj == null || obj.size() == 0;
	}
	
	
	/**
	 * 将字符串数组值转换成长整型类型集合，将其转换成ArrayList类型结果集，去掉重复数据
	 * @param collection 要转换的集合
	 * @return 结果
	 */
	public static List<String> toLongCollectionByIgnoreRepeat(String[] collection){
		return toStringCollection(collection,ArrayList.class,true);
	}

	/**
	 * 将字符串数组值转换成长整型类型集合，将其转换成ArrayList类型结果集
	 * @param collection 要转换的集合
	 * @return 结果
	 */
	public static List<String> toLongCollection(String[] collection){
		return toStringCollection(collection,ArrayList.class,false);
	}

	/**
	 * 将字符串数组转换成String集合
	 * @param collection 字符串数组
	 * @param arrayType 要转换的结果集合类型
	 * @param ignoreRepeat 忽略重复数据
	 * @return 结果集合
	 */
	@SuppressWarnings("unchecked")
	private static <T extends Collection<String>> T toStringCollection(String[] collection,Class<?> arrayType,boolean ignoreRepeat){
		//检查参数
		if(collection == null){
			return null;
		}
		//如果不存在
		if(arrayType == null){
			arrayType = ArrayList.class;
		}
		//检查类型是否是collection
		if(!Collection.class.isAssignableFrom(arrayType)){
			throw new IllegalArgumentException("arrayType 必须是Collection子类");
		}
		//检查是否是接口
		if(arrayType.isInterface()){
			throw new IllegalArgumentException("arrayType 必须是可以实例化的子类");
		}
		try {
			//实例化结果类
			Collection<String> rs = (Collection<String>)arrayType.newInstance();
			if(ignoreRepeat){
				//使用set去重
				Set<String> sets = new HashSet<>();
				Collections.addAll(sets, collection);
				rs.addAll(sets);
			}else{
				rs.addAll(Arrays.asList(collection));
			}
			return (T) rs;
		}catch (Exception e) {
			throw new IllegalArgumentException("arrayType 必须是Collection子类",e);
		}
	}
	
	
	/**
	 * 将集合转换成特定相应类型
	 * @param list
	 * @param type 类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T>  T[] toArray(List<T> list,Class<T> type){
		if(isEmpty(list)){
			return null;
		}
		T[] result = null;
		//根据类型进行转换
		//字符串
		if(type == String.class){
			result = (T[])new String[list.size()];
		}
		//INTEGER
		else if(type == Integer.class){
			result = (T[])new Integer[list.size()];
		}
		//Long
		else if(type == Long.class){
			result = (T[])new Long[list.size()];
		}
		for(int i = 0;i < list.size();i ++){
			result[i] = list.get(i);
		}
		return result;
	}
	
	/**
	 * 功能：以#号分割String
	 * @param arr 
	 * @return 
	 * @author 文齐辉
	 * @date 2013-1-25 下午6:54:28
	 */
	public static Map<Long,Integer> convertToMap(List<String> arr) {
		//Map<goodsId,duration>
		Map<Long,Integer> tempMap = new HashMap<Long,Integer>();
		if(arr==null){
			return tempMap;
		}
		for (String _obj : arr) {
			_obj = _obj==null?"":_obj;
			String[] _arr = _obj.split("#");
			if(_arr.length!=2){continue;}
			Long id = Long.parseLong(_arr[0]);
			tempMap.put(id, Integer.parseInt(_arr[1]));
		}

		return tempMap;
	}
	

	/**
	 * 功能:转换字符串为String数组并去除重复数据,默认以逗号隔开
	 * 注意：此方法不保证顺序，如果需要保证顺序的请用 @link #toStringCollectionSort
	 * <p>作者文齐辉 2015年8月21日 上午10:49:23
	 * @param str
	 * @return
	 */
	public static List<String> toStringCollection(String str) {
		return toStringCollection(str,",");
	}
	
	/**
	 * <p>
	 * 转换字符串为String数组,默认以逗号隔开,保留重复值
	 * </p>
	 * @param
	 * @return List<String>
	 * @throws
	 */
	public static List<String> toStringCollectionSort(String str) {
		if(!StringUtils.isEmpty(str)) {
			return toLongCollection(str.split(","));
		}
		return new ArrayList<>();
	}

	
	/**
	 * <p>
	 * 转换字符串为Long数组并去除重复数据
	 * </p>
	 * @param
	 * @return List<String>
	 * @throws
	 */
	public static List<String> toStringCollection(String str, String separator) {
		if(!StringUtils.isEmpty(str)) {
			return toLongCollectionByIgnoreRepeat(str.split(separator));
		}
		return new ArrayList<>();
	}
	
	
	
	/**
	 * 将列表以特定字符连接
	 * @param list
	 * @return
	 */
	public static String toString(Collection<?> list,String split){
		if(list == null){
			return null;
		}
		StringBuffer rs = new StringBuffer();
		Iterator<?> it = list.iterator();
		while(it.hasNext()){
			rs.append(it.next());
			if(it.hasNext()){
				rs.append(split);
			}
		}
		return rs.toString();
	}
	
	/**
	 * 获取两个集合的交集
	 * @param <K>
	 * @param <T>
	 * @param list1 集合1
	 * @param list2 集合2
	 * @param result 用于存储结果的结合对象
	 * @return
	 */
	public static <K, T extends Collection<K>> T intersection(T list1, T list2, T result){
		if(list1 == null || list2 == null){
			return result;
		}
		for(K item1 : list1){
			for(K item2 : list2){
				if(item1.equals(item2)){
					result.add(item1);
					break;
				}
			}
		}
		return result;
	}


	/**
	 * 功能:获取list中第一个对象
	 * @param list
	 * @return
	 */
	public static <T> T getFirst(List<T> list) {
		if(isEmpty(list)) return null;
		return list.get(0);
	}

	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 组合成List.
	 *
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 */
	@SuppressWarnings("unchecked")
	public static List extractToList(final Collection collection, final String propertyName) {
		List list = new ArrayList(collection.size());

		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}

		return list;
	}


	/**
	 * 提取集合中的对象的一个属性(通过Getter函数), 组合成由分割符分隔的字符串.
	 *
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @param separator 分隔符.
	 */
	public static String extractToString(final Collection collection, final String propertyName, final String separator) {
		List list = extractToList(collection, propertyName);
		return org.apache.commons.lang3.StringUtils.join(list, separator);
	}


}
