package com.iskyinfor.duoduo.downloadManage;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.iskyinfor.duoduo.downloadManage.provider.ProviderInterface;
import com.iskyinfor.duoduo.downloadManage.setting.SettingUtils;
import com.iskyinfor.duoduo.ui.UiHelp;

public class DownloadServiceStub {

	/**
	 * 封装了数据库操作
	 */
	private ProviderInterface mDataFace = null;

	private Context context;

	public DownloadServiceStub(Context context) {
		this.context = context;
		this.mDataFace =  new ProviderInterface(context);
	}

	/**
	 * 通过仅URL添加下载任务
	 */
	public void addDownloadTaskByUrl(String url)  {
		if(UiHelp.SDCardExist()){
			
			DownloadTask item = new DownloadTask(context);
			item.url = url;
			item.setDefaultValueIfNeed();
			mDataFace.addDownloadTask(context, item);
		}else{
			Toast.makeText(context, "sdcard不可用，无法进行下载!", Toast.LENGTH_SHORT).show();
		}
	}

	
	
	
	
     
	public boolean isFileExit(String fileName){
		String tempPath=Environment.getExternalStorageDirectory()
		+ File.separator + SettingUtils.DOWNLOAD_SAVE_BASE_DIR+File.separator+SettingUtils.SAVE_APPLICATION_DIR+File.separator+fileName;
		File file=new File(tempPath);
		if(file.exists()){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	public void addDownloadFileByCustom(String url, String resId,
			String fileName, String extendName, String name, int downType,
			String filePath, int silentMode, int netState)
			  {
		DownloadTask item = new DownloadTask(context);
		item.url = url;
		item.resourceId = resId;
		item.fileName = fileName;
		item.extendName = extendName;
		item.name = name;
		item.downType = downType;
		item.filePath = filePath;
		item.silentMode = silentMode;
		item.netState = netState;
		

		item.setDefaultValueIfNeed();
		mDataFace.addDownloadTask(context, item);
	}

	/**
	 * 正对Application类型应用设置的新增方法
	 * @param url
	 * @param resId
	 * @param name
	 * @param silentMode
	 * @param netState
	 * @param packageName
	 * @ 
	 */
	
	 
	public void addDownloadTaskForApplication(String url, String resId,
			String name, int silentMode, int netState, String packageName)
			  {
		DownloadTask item = new DownloadTask(context);

		item.url = url;
		item.resourceId = resId;
		item.extendName = ".apk";
		item.fileName = resId + item.extendName;
		item.downType = DownloadTask.DownType.FILE_CACHE;
		item.filePath = null;
		item.name = name;
		item.silentMode = silentMode;
		item.netState = netState;
		item.packageName = packageName;
		item.setDefaultValueIfNeed();
		mDataFace.addDownloadTask(context, item);
	}

	/**
	 * 查询未下载完成的任务列表
	 * 
	 */
	 
	public List<DownloadTask> queryTaskByUnFinishState()   {
		List<DownloadTask> list = mDataFace.queryTaskByUnFinishState();
		return list;
	}

	/**
	 * 查询已下载未完成的任务列表
	 * 
	 */
	 
	public List<DownloadTask> queryTaskByFinishState()   {
		List<DownloadTask> list = mDataFace.queryTaskByFinishState();
		return list;
	}
	
	
	/**
	 * 查询已下载成功的任务列表
	 * 
	 */
	 
	public List<DownloadTask> querySuccessTask()   {
		List<DownloadTask> list = mDataFace.querySuccessTask();
		return list;
	}
	
	/**
	 * 查询已下载未成功的任务列表
	 * 
	 */
	 
	public List<DownloadTask> queryUnSuccessTask()   {
		List<DownloadTask> list = mDataFace.queryUnSuccessTask();
		return list;
	}

	/**
	 * 通过ResId查询任务的当前状态
	 */
	 
	public DownloadTask getTaskByResId(String resId)   {
		return mDataFace.queryTaskByResId(resId);
	}

	/**
	 * 取消下载任务 TODO 取消下载任务前是否要判断下载任务是否终止？
	 */
	 
	public void cancelDownloadTask(String resId)   {
		mDataFace.updateTaskStateByResId(resId, DownloadTask.State.CANCEL);
	}

	/**
	 * 暂停下载任务 TODO 暂停下载任务前是否判断下载任务是否终止？
	 */
	 
	public void pauseDownloadTask(String resId)   {
		mDataFace.updateTaskStateByResId(resId, DownloadTask.State.PAUSE);
	}

	/**
	 * 通过指定的ResId重新启动下载任务,仅针对下载暂停或者下载错误时使用
	 */
	 
	public void revertDownloadTask(String resId)   {
		mDataFace.updateTaskStateByResId(resId, DownloadTask.State.WAIT);
	}

	/**
	 * 根据URL创建ResId的接口
	 */
	 
	public String buildResourceId(String url)   {
		return DownloadTask.buildResourceId(url);
	}

	/**
	 * 查询下载成功的任务
	 * 
	 * @return
	 * @ 
	 */
	public List<DownloadTask> queryDownloadSuccessTaskIfFinish()
			  {
		return mDataFace.queryTaskIfFinish(DownloadTask.State.SUCCESS);
	}

	/**
	 * 查询取消下载的任务
	 * 
	 * @return
	 * @ 
	 */
	 
	public List<DownloadTask> queryDownloadCancelTaskIfFinish()
			  {
		return mDataFace.queryTaskIfFinish(DownloadTask.State.CANCEL);
	}

	/**
	 * 查询下载失败的任务
	 */
	 
	public List<DownloadTask> queryDownloadFailTaskIfFinish()
			  {
		return mDataFace.queryTaskIfFinish(DownloadTask.State.FAIL);
	}

	/**
	 * 查询下载错误的任务
	 */
	 
	public List<DownloadTask> queryDownloadErrorTaskIfUnfinish()
			  {
		return mDataFace.queryTaskIfUnfinish(DownloadTask.State.ERROR);
	}

	/**
	 * 查询下载暂停的任务
	 */
	 
	public List<DownloadTask> queryDownloadPauseTaskIfUnfinish()
			  {
		return mDataFace.queryTaskIfUnfinish(DownloadTask.State.PAUSE);
	}

	/**
	 * 查询正在下载状态的任务
	 */
	 
	public List<DownloadTask> queryDownloadRunningTaskIfUnfinish()
			  {
		return mDataFace.queryTaskIfUnfinish(DownloadTask.State.RUNNING);
	}

	/**
	 * 查询等待状态的任务
	 */
	 
	public List<DownloadTask> queryDownloadWaitTaskIfUnfinish()
			  {
		return mDataFace.queryTaskIfUnfinish(DownloadTask.State.WAIT);
	}

	/**
	 * <pre>
	 * 将新增记录时的判断，将记录分为几种
	 *  NEW_TASK = 0 :  第一次下载的任务，可以直接添加下载地址 ;
	 *  DOWNLOADING_TASK = 1 : 正在下载的任务，取消任务后可重新下载
	 *  DOWNLOADED_TASK = 2 ：已下载的网络任务，添加下载会覆盖相关记录
	 * </pre>
	 */
	 
	public int getHistoryStateByResid(String resId)   {
		return mDataFace.getHistoryStateByResid(resId);
	}

	/**
	 * 移除任务
	 */
	 
	public boolean deleteDownloadTask(String resId)   {
		int count = mDataFace.deleteTaskByResId(resId);
		return count > 0 ? true : false;

	}

}
