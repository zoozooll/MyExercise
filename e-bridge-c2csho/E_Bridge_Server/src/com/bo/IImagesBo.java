package com.bo;

import java.io.File;

import com.vo.Vender;

public interface IImagesBo {

	//图片上传的方法
	public void upload(File src, File dst,String imageURL);
	
}
