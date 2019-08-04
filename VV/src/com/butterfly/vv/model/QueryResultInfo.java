package com.butterfly.vv.model;

import com.google.gson.annotations.Expose;

/**
 * 数据库查询返回结果
 * @author chaohui liu
 * @param <T> 返回的结果集
 */
public class QueryResultInfo<T> {
	@Expose
	String result; // 数据库查询结果返回的状态
	@Expose
	String error_msg;// 数据库查询的反馈信息
	@Expose
	Integer count; // 结果集大小
	@Expose
	T data; // 结果集

	public QueryResultInfo() {
		// TODO Auto-generated constructor stub
	}
	public QueryResultInfo(String result, String error_msg, Integer count,
			T queryData) {
		super();
		this.result = result;
		this.error_msg = error_msg;
		this.count = count;
		this.data = queryData;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getError_msg() {
		return error_msg;
	}
	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
}
