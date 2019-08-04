package com.mogoo.market.model;

/**
 * @author csq  推荐的应用信息
 */
public class RecommentAppInfo 
{
	private String id;
	private String name;
	private String iconUrl;
	private String packageName;
	
	public RecommentAppInfo() 
	{
	}
	
	public RecommentAppInfo(String id, String name, String iconUrl,String packageName) 
	{
		super();
		this.id = id;
		this.name = name;
		this.iconUrl = iconUrl;
		this.packageName=packageName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	public String getPackageName() {
		return this.packageName;
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
}
