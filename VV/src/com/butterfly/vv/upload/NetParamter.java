package com.butterfly.vv.upload;

import org.apache.http.message.BasicNameValuePair;

public class NetParamter extends BasicNameValuePair {
	private NetParamterType netParamterType;
	private boolean isCreateThumb;

	public boolean isCreateThumb() {
		return isCreateThumb;
	}
	public void setCreateThumb(boolean isCreateThumb) {
		this.isCreateThumb = isCreateThumb;
	}

	public enum NetParamterType {
		type_str, type_int, type_file;
	}

	public NetParamter(String name, String value,
			NetParamterType pNetParamterType, boolean isCreateThumb) {
		super(name, value);
		this.netParamterType = pNetParamterType;
		this.isCreateThumb = isCreateThumb;
	}
	public NetParamter(String name, String value,
			NetParamterType pNetParamterType) {
		this(name, value, pNetParamterType, false);
	}
	public NetParamter(String name, String value) {
		this(name, value, NetParamterType.type_str, false);
	}
	public NetParamterType getNetParamterType() {
		return netParamterType;
	}
	public void setNetParamterType(NetParamterType netParamterType) {
		this.netParamterType = netParamterType;
	}
	@Override
	public String toString() {
		if (isCreateThumb) {
			return super.toString() + "||isCreateThumb=" + isCreateThumb;
		} else {
			return super.toString();
		}
	}
}
