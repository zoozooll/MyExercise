package com.action;

import com.bo.ISearchBo;
import com.vo.PageBean;

public class SearchAction extends BasicAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 419083245272251720L;
	private String entityName;//表名	
	private Integer page;//当前页
	private Integer pageSize;//最大显示数
	private String condition;//查询条件
	private PageBean pageBean;
	
	
	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	
	public String list() throws Exception {
		String result=null;
		ISearchBo searchBo=null;
		try {
			result = null;
			System.out.println("------------------"+condition+entityName+page+pageSize);
			searchBo=(ISearchBo) getBean("searchBo");
			pageBean=searchBo.list(entityName, page,pageSize, condition);			
			if("Product".equals(entityName)){
				this.getRequest().getSession().setAttribute("productss", pageBean.getList());
				result="pro";
			}else if("Purchaser".equals(entityName)){						
				this.getRequest().getSession().setAttribute("purchasers", pageBean.getListPurchaser());
System.out.println("--------------"+pageBean.getListPurchaser());
				result="pur";				
			}else{
				result=ERROR;
			}
		} catch (Exception e) {			
			e.printStackTrace();
			result=ERROR;
		}	
		
		countList(searchBo);
		return result;
	}
	
	public void countList(ISearchBo searchBo) throws Exception {		
		try {
			Integer count = searchBo.countList(entityName);
			System.out.println("查到"+count+"条数据!");
			getRequest().getSession().setAttribute("count", count);
		} catch (Exception e) {			
			e.printStackTrace();			
		}	
		
	}
	
	
	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	
}
