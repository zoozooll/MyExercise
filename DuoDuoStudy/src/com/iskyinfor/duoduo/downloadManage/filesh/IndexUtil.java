package com.iskyinfor.duoduo.downloadManage.filesh;

import java.io.File;

import android.util.Log;

public class IndexUtil {
	public static final String TAG = "HashFile";

	/**
	 * �����ļ�
	 * 
	 * @param strPath
	 *            �������ļ�
	 */
	public static void indexFile(String strPath) {
		try {
			/* ����Ŀ¼��ÿ���ļ�����HASH */
			File dir = new File(strPath);
			/* ���������¼������Ϣ */
			if (!dir.exists()) {
				String strInfo = new String("Error: error path :");
				strInfo += strPath;
				Log.i(TAG, strInfo);
				return;
			}

			if (dir.isFile()) {
				System.out.println("�����ļ�: " + dir.getPath());
				if (dir.getPath().contains(" ")) {
					String strInfo = new String("Error: space :");
					strInfo += strPath;
					Log.i(TAG, strInfo);
					return;
				}
				HashFile file = new HashFile();
				file.setStrFullPath(dir.getPath());
				/* ���HASH�ɹ���д��Ϣ���ļ� */
				if (file.Hash()) {
					Log.i(TAG, "name:" + file.getStrFileName());
					Log.i(TAG, "hash:" + file.getStrHash());
					Log.i(TAG, "type:" + file.getStrFileType());
					Log.i(TAG, "size:" + String.valueOf(file.getNFileSize()));
					Log.i(TAG, "path:" + file.getStrFilePath());

				}
				/* ���HASHʧ����дʧ����Ϣ����־�ļ� */
				else {
					String strInfo = new String("Error: index error:");
					strInfo += strPath;
					strInfo += "\r\n";
					Log.i(TAG, strInfo);
				}
			} else if (dir.isDirectory()) {
				File[] list = dir.listFiles();
				if (list != null) {
					for (int i = 0; i < list.length; i++) {
						indexFile(list[i].getPath());
					}
				}
			} else {
				/* д����Ϣ��ʾ��Ч��·�� */
				String strInfo = new String("Warn: error path");
				strInfo += strPath;
				strInfo += "\r\n";
				Log.i(TAG, strInfo);
			}
		} catch (NullPointerException e) {
		}
	}
}
