package com.bo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.dao.IImageDao;
import com.vo.Image;

public class ImagesBoImpl implements IImagesBo {
	private IImageDao imageDao;
	
	private static final int BUFFER_SIZE = 16 * 1024;

	/**
	 * @return the imageDao
	 */
	public IImageDao getImageDao() {
		return imageDao;
	}

	/**
	 * @param imageDao the imageDao to set
	 */
	public void setImageDao(IImageDao imageDao) {
		this.imageDao = imageDao;
	}

	public void upload(File src, File dst,String imageURL) {
		try {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new BufferedInputStream(new FileInputStream(src),
						BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dst),
						BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				while (in.read(buffer) > 0) {
					out.write(buffer);
				}
				out.flush();
				Image image=new Image();
				image.setImgPath(imageURL);
				imageDao.saveImage(image);
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



}
