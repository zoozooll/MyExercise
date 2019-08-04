
package com.tcl.manager.activity.entity;

import java.util.ArrayList;
import java.util.List;
/**
 * @author wenchao.zhang
 */
public class ExpandableListItem  {
	//图片资源id
	private int imgResId;
	//描述语
    private String desc = "";
    //数字字符串
    private String count = "";
    
    private boolean isLoading = true;
    
    private boolean canExpand = false;
    private boolean isExpanded = true;
    private List<OptimizeChildItem> children = new ArrayList<OptimizeChildItem>();

    

    public ExpandableListItem(int imgResId, String desc, String count
			) {
		super();
		this.imgResId = imgResId;
		this.desc = desc;
		this.count = count;
	}
    



	public int getImgResId() {
		return imgResId;
	}



	public void setImgResId(int imgResId) {
		this.imgResId = imgResId;
	}



	public String getDesc() {
		return desc;
	}



	public void setDesc(String desc) {
		this.desc = desc;
	}



	public String getCount() {
		return count;
	}



	public void setCount(String count) {
		this.count = count;
	}



	public boolean isCanExpand() {
		return canExpand;
	}



	public void setCanExpand(boolean canExpand) {
		this.canExpand = canExpand;
	}



	public boolean isExpanded() {
		return isExpanded;
	}



	public void setExpanded(boolean isExpanded) {
		this.isExpanded = isExpanded;
	}








	public List<OptimizeChildItem> getChildren() {
		return children;
	}



	public void setChildren(List<OptimizeChildItem> children) {
		this.children = children;
	}




	public boolean isLoading() {
		return isLoading;
	}


	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}


}
