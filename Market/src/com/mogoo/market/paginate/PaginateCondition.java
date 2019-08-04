package com.mogoo.market.paginate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.network.http.NetworkTaskParameter;

/**
 * 分页条件类
 * 
 * @author xjx-motone
 * 
 */
public class PaginateCondition extends NetworkTaskParameter {

	/**
	 * 每页的显示的条目数
	 */
	public static final String PARAM_PAGE_SIZE = "pagesize";

	/** 当前的页数 */
	public static final String PARAM_CURRENT_PAGE = "page";

	/**
	 * 每页显示的条目数
	 */
	public int pageSize;

	/**
	 * 当前的页数
	 */
	public int currentPage = 0;

	public String getUrl() {
		if (getRequestType() == REQUEST_TYPE_GET) {
			Map<String, String> map = HttpUrls.getBaseParamPair();
			map.putAll(getPaginateParamMap());
			if (this.paramsMap != null) {
				map.putAll(this.paramsMap);
			}
			return this.url + HttpUrls.createParamPair(map);
		} else {
			return url;
		}
	}

	public List<NameValuePair> getParamPair() {
		Map<String, String> map = HttpUrls.getBaseParamPair();
		map.putAll(getPaginateParamMap());
		if (this.paramsMap != null) {
			map.putAll(this.paramsMap);
		}
		return HttpUrls.createNameValuePairs(map);
	}

	/**
	 * 取得与分页相关的请求参数对
	 * 
	 * @author 张永辉
	 * @date 2011-12-26
	 * @return
	 */
	private Map<String, String> getPaginateParamMap() {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(PaginateCondition.PARAM_PAGE_SIZE, pageSize + "");
		params.put(PaginateCondition.PARAM_CURRENT_PAGE, currentPage + "");
		return params;
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

}
