package com.beem.project.btf.ui.entity;

import com.butterfly.vv.db.ormhelper.bean.BaseDB;
import com.j256.ormlite.field.DatabaseField;

/**
 * @ClassName: FontDB
 * @Description: 字体库
 * @author: yuedong bao
 * @date: 2015-11-24 下午5:12:38
 */
public class FontDB extends BaseDB {
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private String name;//字体库名称
	@DatabaseField
	private String url;//字体库zip包远端路径
	@DatabaseField
	private String path;//字体库文件路径
	@DatabaseField
	private boolean isLocal;//是否本地字体
	@DatabaseField
	private int isDownloaded;//是否下载了

	@Override
	public void saveToDatabase() {
	}
}
