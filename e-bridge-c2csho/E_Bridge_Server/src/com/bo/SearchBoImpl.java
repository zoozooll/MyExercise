package com.bo;

import java.util.ArrayList;
import java.util.List;

import com.dao.ISearchDao;
import com.vo.PageBean;
import com.vo.Product;
import com.vo.Purchaser;

public class SearchBoImpl implements ISearchBo {

	private ISearchDao searchDao;
	

	public ISearchDao getSearchDao() {
		return searchDao;
	}

	public void setSearchDao(ISearchDao searchDao) {
		this.searchDao = searchDao;
	}

	public Integer countList(String entityName) throws Exception {		
		return searchDao.countList(entityName);
	}

	public PageBean list(String entityName, Integer page,
			Integer maxResults, String condition) throws Exception {		
		int allRow = searchDao.countList(entityName); // �ܼ�¼��
		int totalPage = PageBean.countTotalPage(maxResults, allRow);    //��ҳ��
        final int offset = PageBean.countOffset(maxResults, page);    //��ǰҳ��ʼ��¼
     	List<Product> listPro=new ArrayList<Product>();//
    	List<Purchaser> listPur=new ArrayList<Purchaser>();
    	
		final int length = maxResults; // ÿҳ��¼��
		final int currentPage = PageBean.countCurrentPage(page);
		List<Object> list = searchDao.list(entityName, offset, maxResults, condition); // "һҳ"�ļ�¼;
		// �ѷ�ҳ��Ϣ���浽Bean��
		PageBean pageBean = new PageBean();
        pageBean.setPageSize(maxResults);    
        pageBean.setCurrentPage(currentPage);
        pageBean.setAllRow(allRow);
        pageBean.setTotalPage(totalPage);       
        if("Product".equals(entityName)){			
			for (Object o : list) {
				Product p=(Product)o;
				System.out.println("add product"+p.getProName());
				listPro.add(p);
				System.out.println(listPro+"--------");
				pageBean.setList(listPro);				
			}			
		}
        if("Purchaser".equals(entityName)){	
			for (Object o : list) {
				Purchaser p=(Purchaser)o;
				listPur.add(p);
				System.out.println(listPur);
				pageBean.setListPurchaser(listPur);
			}	
				
		}		
        pageBean.init();
        return pageBean;


	}
	/*public List<Object> list(String entityName, Integer curPage,
			Integer maxResults, String condition) throws Exception {
		return searchDao.list(entityName, curPage, maxResults, condition);
	}*/

}
