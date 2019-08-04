package com.tcl.manager.application;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Debug;
import android.view.InflateException;

import com.tcl.framework.log.NLog;
import com.tcl.framework.util.FileUtils; 
import com.tcl.manager.util.Constant;

/**
 * . Author: raezlu Date: 13-9-9 Time: 下午8:11
 */
public class AMOOMHelper
{

	private final static String TAG = "OOMHelper";

	@SuppressWarnings("unused")
	private final static String OOM_DIR = "oom";
	private final static String OOM_SUFFIX = ".hprof";

	private static ThreadLocal<SimpleDateFormat> sLocalDateFormat = new ThreadLocal<SimpleDateFormat>()
	{
		protected SimpleDateFormat initialValue()
		{
			return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss.SSS");
		}
	};

	private AMOOMHelper()
	{
		// static usage.
	}

	public static boolean dumpHprofIfNeeded(Context context, Throwable e)
	{
		if (!Constant.DEBUG)
		{
			// dump only in debug mode.
			return false;
		}
		if (context == null)
		{
			// no valid context.
			return false;
		}
		if (e == null || !isOOM(e))
		{
			// dump only when oom.
			return false;
		}
		try
		{
			String dir = getOOMDir(context);
			String name = getDate() + "#" + e.getClass().getSimpleName() + OOM_SUFFIX;
			String path = dir != null ? dir + File.separator + name : null;
			File file = path != null ? new File(path) : null;
			// delete others if needed.
			// if (file != null && !Constant.DEBUG) {
			// // keep only one dump file in non-package-debuggable mode.
			// FileUtils.deleteFile(file.getParentFile().getAbsolutePath());
			// }
			// perform dump.
			if (file != null && ensureDir(file.getParentFile()))
			{
				Debug.dumpHprofData(path);
			}
		}
		catch (Throwable t)
		{
			NLog.d(TAG, "fail to dump hprof %s", t);
		}
		return true;
	}

	public static String getHprofDir(Context context)
	{
		if (context == null)
		{
			return null;
		}
		return getOOMDir(context);
	}

	public static boolean isOOM(Throwable e)
	{
		int loopCount = 0;
		while (e != null && loopCount++ < 5)
		{
			if (isOOMInner(e))
			{
				return true;
			}
			e = e.getCause();
		}
		return false;
	}

	private static boolean isOOMInner(Throwable e)
	{
		if (e == null)
		{
			return false;
		}
		return (e instanceof OutOfMemoryError) || (e instanceof InflateException);
	}

	private static String getOOMDir(Context context)
	{
		return AMDirectorManager.getInstance().getDirectoryPath(AMDirType.crash);
	}

	private static String getDate()
	{
		return sLocalDateFormat.get().format(new Date(System.currentTimeMillis()));
	}

	private static boolean isDirValid(File dir)
	{
		return dir != null && dir.isDirectory() && dir.exists();
	}

	private static boolean ensureDir(File dir)
	{
		if (dir == null)
		{
			return false;
		}
		if (!isDirValid(dir))
		{
			FileUtils.deleteFile(dir.getAbsolutePath());
			return dir.mkdirs();
		}
		return true;
	}
}
