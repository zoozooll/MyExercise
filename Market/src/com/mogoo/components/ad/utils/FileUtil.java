package com.mogoo.components.ad.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.os.Environment;

public class FileUtil
{

	private String SDPath = null;

	public FileUtil()
	{
		super();
		SDPath = Environment.getExternalStorageDirectory() + "/";

	}

	public String getSDPath()
	{
		return SDPath;
	}

	public void setSDPath(String sDPath)
	{
		SDPath = sDPath;
	}

	/**
	 * 在外部的存储卡上创建一个文件
	 * 
	 * @param filename
	 * @return
	 */
	private File createSDFile(String filename)
	{

		File file = new File(SDPath + filename);

		try
		{
			if (file.createNewFile())
			{
				return file;
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 创建一个目录
	 * 
	 * @param foldername
	 * @return
	 */
	private File createSDDir(String foldername)
	{
		File file = new File(SDPath + foldername);
		if (!file.exists())
		{
			if (file.mkdirs())
			{
				return file;
			}
		}
		return null;
	}

	private boolean isFileExist(String filename)
	{
		File file = new File(SDPath + filename);
		return file.exists();

	}

	private File write2SDFromInput(String path, String filename,
			InputStream inputStream)
	{
		File file = null;
		OutputStream output = null;
		try
		{
			File tempf = createSDDir(path);
			file = createSDFile(path + filename);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[1024 * 4];
			while (inputStream.read(buffer) != -1)
			{
				output.write(buffer);
			}
			output.flush();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				output.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 保存图片文件到SD卡指定目录
	 * 
	 * @param bm
	 *            图片
	 * @param FolderName
	 *            文件夹名称
	 * @param fileName
	 *            文件名
	 * @throws IOException
	 */
	public void saveFile(Bitmap bm, String FolderName, String fileName)
			throws IOException
	{
		File dirFile = new File(SDPath + FolderName);
		if (!dirFile.exists())
		{
			dirFile.mkdir();
		}
		File myCaptureFile = new File(SDPath + FolderName + "/" + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.PNG, 80, bos);
		bos.flush();
		bos.close();
	}

}
