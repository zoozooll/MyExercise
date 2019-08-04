package com.butterfly.vv.model;

import com.beem.project.btf.BeemApplication;
import com.pullToRefresh.ui.PullToRefreshBase.PullType;

public class Start {
	private String val;
	private String valOff;
	private final PullType pullAction;
	public static final String END_VAL = "-1";

	public Start() {
		this.pullAction = PullType.PullDown;
	}
	public Start(String start) {
		super();
		this.val = start;
		this.pullAction = PullType.PullDown;
	}
	public Start(Start start, PullType action) {
		this.pullAction = action;
		setStart(start);
	}
	public void setStart(Start start) {
		if (start == null) {
			this.val = null;
			this.valOff = null;
		} else {
			this.val = start.val;
			this.valOff = start.valOff;
		}
	}
	public String getVal() {
		return val;
	}
	public void setVal(String start) {
		this.val = start;
	}
	public boolean isEnd() {
		if (!BeemApplication.isNetworkOk()) {
			return END_VAL.equals(valOff);
		} else {
			return END_VAL.equals(val);
		}
	}
	public boolean isStart() {
		if (!BeemApplication.isNetworkOk()) {
			return valOff == null;
		} else {
			return val == null;
		}
	}
	public String getValOff() {
		return valOff;
	}
	public void setValOff(String valOff) {
		this.valOff = valOff;
	}
	public PullType getPullAction() {
		return pullAction;
	}
	@Override
	public String toString() {
		return "Start [val=" + val + ", valOff=" + valOff + ", pullAction="
				+ pullAction + "]";
	}
}
