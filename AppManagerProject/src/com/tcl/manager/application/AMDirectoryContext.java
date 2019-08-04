package com.tcl.manager.application;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.os.Environment;

import com.tcl.framework.fs.Directory;
import com.tcl.framework.fs.DirectroyContext;
import com.tcl.framework.util.TimeConstants;

class AMDirectoryContext extends DirectroyContext
{

	private Context mContext;

	public AMDirectoryContext(Context context, String rootFolder)
	{
		mContext = context;
		initContext(rootFolder);
	}

	@Override
	public void initContext(String root)
	{
		String rootPath = null;
		if (!Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED))
		{
			File fileDir = mContext.getFilesDir();

			rootPath = fileDir.getAbsolutePath() + File.separator + root;
		}
		else
		{
			rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + root;
		}

		super.initContext(rootPath);
	}

	@Override
	protected Collection<Directory> initDirectories()
	{
		List<Directory> children = new ArrayList<Directory>();

		Directory dir = newDirectory(AMDirType.log);
		children.add(dir);
		dir = newDirectory(AMDirType.image);
		children.add(dir);
		dir = newDirectory(AMDirType.crash);
		children.add(dir);
		dir = newDirectory(AMDirType.apps);
		children.add(dir);
		dir = newDirectory(AMDirType.cache);
		children.add(dir);
		return children;
	}

	private Directory newDirectory(AMDirType type)
	{
		Directory child = new Directory(type.toString(), null);
		child.setType(type.value());
		if (type.equals(AMDirType.cache))
		{
			child.setForCache(true);
			child.setExpiredTime(TimeConstants.ONE_DAY_MS);
		}

		return child;
	}
}
