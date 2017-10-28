package com.action;

import java.io.File;


import org.apache.struts2.ServletActionContext;

import com.bo.IImagesBo;
import com.vo.Purchaser;
import com.vo.Vender;

public class MutiFileUploadAction extends BasicAction {
	private File[] uploads;

	private String[] uploadFileNames;

	private String[] uploadContentTypes;
	
	private IImagesBo imagesBo;

	public File[] getUpload() {
		return this.uploads;
	}

	public void setUpload(File[] upload) {
		this.uploads = upload;
	}

	public String[] getUploadFileName() {
		return this.uploadFileNames;
	}

	public void setUploadFileName(String[] uploadFileName) {
		this.uploadFileNames = uploadFileName;
	}

	public String[] getUploadContentType() {
		return this.uploadContentTypes;
	}

	public void setUploadContentType(String[] uploadContentType) {
		this.uploadContentTypes = uploadContentType;
	}
	
	

	public IImagesBo getImagesBo() {
		return imagesBo;
	}

	public void setImagesBo(IImagesBo imagesBo) {
		this.imagesBo = imagesBo;
	}

	@Override
	public String execute() {
		imagesBo=(IImagesBo)getBean("imagesBo");
		Purchaser purchaser=(Purchaser) (this.getRequest().getSession().getAttribute("purchaser"));
		File file = new File(ServletActionContext.getServletContext()
				.getRealPath("images")+purchaser.getVender().getVenShortname());
		if(!file.exists()){
			file.mkdir();
		}
		for(int i=0;i<uploads.length;i++){
			File imageFile = new File(file+uploadFileNames[i]);
			String imageURL="images/"+purchaser.getVender().getVenShortname()+"/"+uploadFileNames[i];
			imagesBo.upload(uploads[i], imageFile,imageURL);
			
		}
		return SUCCESS;
	}
}
