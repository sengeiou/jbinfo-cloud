package cn.jbinfo.cloud.core.condition;


import java.util.ArrayList;
import java.util.List;

public class Pagination implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int DEFAULT_PAGE_SIZE = 20;
	private int DEFAULT_CURRENTPAGE = 1;

	private int pageSize; // 每页默认20条数据
	private int currentPage; // 当前页
	private int totalPages=1; // 总页数
	private int totalCount; // 总数据数
	
	/**
	 * 起始行
	 */
	private Integer firstResult;
	
	private List<?> list;

	public Pagination(int totalCount, int pageSize) {
		this.init(totalCount, pageSize);
	}

	public Pagination() {
		this.pageSize = DEFAULT_PAGE_SIZE;
		this.currentPage = DEFAULT_CURRENTPAGE;
	}

	/**
	 * 初始化分页参数:需要先设置totalCount
	 */

	public void init(int totalCount, int pageSize) {
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		if ((totalCount % pageSize) == 0) {
			totalPages = totalCount / pageSize;
		} else {
			totalPages = totalCount / pageSize + 1;
		}
		this.firstResult = pageSize* (currentPage - 1);

	}
	
	public void init() {
		init(this.totalCount,this.pageSize);
	}
	
	public void init(Long totalCount, int pageSize) {
		init(totalCount.intValue(),pageSize);
	}

	public void init(int totalCount, int pageSize, int currentPage) {
		this.currentPage = currentPage;
		this.init(totalCount, pageSize);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	public void setTotalCount(Long totalCount) {
		if(totalCount!=null)
			this.totalCount = totalCount.intValue();
	}


	public Integer getFirstResult() {
		 this.firstResult = pageSize* (currentPage - 1);
		 if(firstResult<0) firstResult =0; 
		 return this.firstResult;
	}

	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}

	/**
	 * 功能:针对list内存分页方法
	 * @param list 需要分页的总数据
	 * @return 当前分页完的数据
	 */
	public <T> List<T> getPageList(List<T> list){
		init(list.size(), this.getPageSize());
		List<T> result = new ArrayList<>();
		for(int i=getFirstResult();i<getCurrentPage()*getPageSize()&&i<list.size();i+=1){
			result.add(list.get(i));
		}
		return result; 
	}
	
	public <T> void setList(List<T> list){
		this.list = list;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getList(){
		return ((List<T>)list);
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	
	
	
	@Override
	public String toString() {
		return "Pagination [DEFAULT_PAGE_SIZE=" + DEFAULT_PAGE_SIZE
				+ ", DEFAULT_CURRENTPAGE=" + DEFAULT_CURRENTPAGE
				+ ", pageSize=" + pageSize + ", currentPage=" + currentPage
				+ ", totalPages=" + totalPages + ", totalCount=" + totalCount
				+ "]";
	}
	
}
