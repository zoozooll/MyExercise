package com.beem.project.btf.ui.entity;

import java.util.HashMap;
import java.util.Map;

import com.beem.project.btf.ui.fragment.TalkFragement.FragmentType;
import com.butterfly.vv.model.ImageFolderItem;

/**
 * 保存聊天，评论的草稿类
 * @author waizhu tan
 */
public class ReviewData {
	private static ReviewData instance;
	private FragmentType type;
	private String jid;
	private String gid;
	private String create_time;
	private Map<String, String> info = new HashMap<String, String>();
	private Map<String, ImageFolderItem> msg = new HashMap<String, ImageFolderItem>();

	private ReviewData() {
	}
	public static ReviewData getInstance() {
		if (instance == null) {
			synchronized (ReviewData.class) {
				if (instance == null) {
					instance = new ReviewData();
				}
			}
		}
		return instance;
	}
	public FragmentType getType() {
		return type;
	}
	public void setType(FragmentType type) {
		this.type = type;
	}
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public Map<String, String> getInfo() {
		return info;
	}
	public void setInfo(Map<String, String> info) {
		this.info = info;
	}
}
