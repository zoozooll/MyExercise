package com.tcl.manager.application;

import java.io.File;

import android.content.Context;

import com.tcl.framework.fs.DirectoryManager;

public class AMDirectorManager
{ 
	public static final String ROOT_FOLDER = "appmanager";
	private static AMDirectorManager sDirManager = null;
	private DirectoryManager mDirectoryManager = null;

	private AMDirectorManager()
	{
	}

	public static boolean initManager(Context context)
	{
		boolean flag = true;
		if (sDirManager == null) {
			synchronized (AMDirectorManager.class) {
				if (sDirManager == null)
				{
					sDirManager = new AMDirectorManager();
					
					flag = sDirManager.init(context);
				}
			}
		}
		return flag;
	}

	private boolean init(Context context)
	{
		DirectoryManager dm = new DirectoryManager(new AMDirectoryContext(context.getApplicationContext(), ROOT_FOLDER));
		boolean ret = dm.buildAndClean();
		if (!ret)
		{
			return false;
		}

		mDirectoryManager = dm;

		return ret;
	}

	private DirectoryManager getDirectoryManager()
	{
		if (sDirManager == null)
		{
			return null;
		}

		return sDirManager.mDirectoryManager;
	}

	private File getDirectory(AMDirType type)
	{
		DirectoryManager manager = getDirectoryManager();
		if (manager == null)
		{
			return null;
		}

		return manager.getDir(type.value());
	}

	public String getDirectoryPath(AMDirType type)
	{
		File file = getDirectory(type);
		if (file == null)
		{
			return null;
		}

		return file.getAbsolutePath();
	}

	public static AMDirectorManager getInstance()
	{ 
		return sDirManager;
	}
}
