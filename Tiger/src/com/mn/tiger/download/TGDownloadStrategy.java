package com.mn.tiger.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.Context;
import android.text.TextUtils;

import com.mn.tiger.download.TGDownloadReceiver.IDownloadReceiveListener;
import com.mn.tiger.log.LogTools;
import com.mn.tiger.request.TGHttpLoader;
import com.mn.tiger.request.error.TGErrorMsgEnum;
import com.mn.tiger.request.sync.client.DefaultHttpClient;
import com.mn.tiger.request.sync.client.TGHttpClient;
import com.mn.tiger.request.sync.method.TGGetMethod;
import com.mn.tiger.request.sync.method.TGHttpMethod;
import com.mn.tiger.request.sync.method.TGPostMethod;
import com.mn.tiger.utility.Commons;
import com.mn.tiger.utility.FileUtils;
import com.mn.tiger.utility.MD5;

public class TGDownloadStrategy implements IDownloadStrategy
{
	/**
	 * 日志标识
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();

	/**
	 * 上下文信息
	 */
	protected Context context;

	/**
	 * 下载监听
	 */
	protected IDownloadListener downloadListener;

	/**
	 * 下载任务
	 */
	protected TGDownloadTask downloadTask = null;
	
	/**
	 * 下载信息
	 */
	protected TGDownloader downloader;
	
	/**
	 * 下载进度
	 */
	private int progress = -1; 

	/**
	 * 构造函数
	 * @date 2014年8月5日
	 * @param context
	 * @param downloadTask
	 * @param listener
	 */
	public TGDownloadStrategy(Context context, TGDownloadTask downloadTask, 
			IDownloadListener listener)
	{
		this.context = context;
		this.downloadTask = downloadTask;
		this.downloadListener = listener;
	}

	@Override
	public void download(TGDownloadParams downloadParams)
	{
		LogTools.p(LOG_TAG, "[Method:download]");
		// 获取下载参数
		downloader = getDownloader(downloadParams);
		// 通知下载开始
		downloadListener.downloadStart(downloader);
		// 执行下载
		executeDownload(context, downloader);
	}
	
	/**
	 * 
	 * 该方法的作用: 获取下载信息
	 * @date 2014年8月18日
	 * @param downloadParams
	 * @return
	 */
	protected TGDownloader getDownloader(TGDownloadParams downloadParams)
	{
		TGDownloader downloader = null;
		downloader = TGDownloadDBHelper.getInstance(context).getDownloader(
				downloadParams.getUrlString(), downloadParams.getParams());
		
		if(downloader == null)
		{
			downloader = new TGDownloader();
			downloader.setId(downloadTask.getTaskID().toString());
			downloader.setUrlString(downloadParams.getUrlString());
			downloader.setParams(downloadParams.getParams());
			downloader.setRequestType(downloadParams.getRequestType());
			downloader.setType(downloadParams.getDownloadType());
			downloader.setSavePath(downloadParams.getSavePath());
			downloader.setTaskClsName(downloadParams.getTaskClsName());
			downloader.setParamsClsName(downloadParams.getClass().getName());
		}
		// 设置下载状态为开始
		downloader.setDownloadStatus(TGDownloadManager.DOWNLOAD_STARTING);
		
		return downloader;
	}
	
	/**
	 * 
	 * 该方法的作用: 请求下载
	 * @date 2014年8月5日
	 * @param context
	 * @param downloader
	 */
	protected void executeDownload(Context context, TGDownloader downloader)
	{
		LogTools.p(LOG_TAG, "[Method:executeDownload]");

		// 任务结束，不执行下载
		if (null == downloadTask)
		{
			return;
		}
		
		TGHttpClient httpClient = new DefaultHttpClient(context);

		// 创建post请求的方法
		TGHttpMethod httpMethod = getHttpMethod(downloader);
		
		if(downloader.isBreakPoints())
		{
			httpMethod.setProperty("Range", "bytes="+ downloader.getCompleteSize() + "-" + (downloader.getFileSize() - 1));
		}
		
		TGDownloadReceiver receiver = new TGDownloadReceiver(context, downloader, 
				downloadTask, receiveListener);
		
		// 执行下载操作
		httpClient.executeHttpMethod(httpMethod, receiver);
	}

	/**
	 * 该方法的作用:获取请求方法类
	 * @date 2014年1月23日
	 * @param TGDownloader
	 * @return
	 */
	protected TGHttpMethod getHttpMethod(TGDownloader download){
		LogTools.i(LOG_TAG, "[Method:getHttpMethod] requestUrl:" + download.getUrlString());
		// 创建post请求的方法
		TGHttpMethod httpMethod = null;
		if (download.getRequestType() == TGHttpLoader.REQUEST_POST)
		{
			httpMethod = new TGPostMethod(context, download.getUrlString(), download.getParams());
		}
		else
		{
			httpMethod = new TGGetMethod(context, download.getUrlString(), download.getParams());
		}
		return httpMethod;
	}

	/**
	 * 
	 * 该方法的作用: 获取上下文信息
	 * @date 2014年7月31日
	 * @return
	 */
	protected Context getContext()
	{
		return context;
	}
	
	/**
	 * 
	 * 该方法的作用: 下载过程中
	 * @date 2014年8月19日
	 * @param downloader
	 */
	private void downloadDownloading(TGDownloader downloader)
	{
		// 如果是断点下载，更新本地记录
		downloader.setDownloadStatus(TGDownloadManager.DOWNLOAD_DOWNLOADING);
		TGDownloadDBHelper.getInstance(context).updateDownloader(downloader);
		
		// 服务端没返回文件长度时，不返回进度
		if(downloader.getFileSize() <= 0){
			return;
		}
		
		int currentProgress = (int) (downloader.getCompleteSize() * 100 / downloader.getFileSize());
		
		// 每1%, 向外推一次进度
		if (downloadListener != null && currentProgress != progress)
		{
			progress = currentProgress;
			downloadListener.downloadProgress(downloader, progress);
		}
	}
	
	/**
	 * 
	 * 该方法的作用: 下载文件完成，删除数据库记录
	 * @date 2014年8月19日
	 * @param downloader
	 */
	protected void downloadFinish(TGDownloader downloader)
	{
		// 如果是断点下载，删除本地记录
		downloader.setDownloadStatus(TGDownloadManager.DOWNLOAD_SUCCEED);
		TGDownloadDBHelper.getInstance(context).deleteDownloader(downloader);
		
		if (downloadListener != null)
		{
			downloadListener.downloadSucceed(downloader);
		}
		else
		{
			LogTools.e(LOG_TAG,
					"[Method:failedDownload]  downloadListener is null,Please set ReceiveListener on Construct..");
		}
	}
	
	/**
	 * 
	 * 该方法的作用: 下载文件过程中出现异常，如果不是断点下载，删除本地文件
	 * @date 2014年8月19日
	 * @param downloader
	 */
	private void downloadFailed(TGDownloader downloader)
	{
		downloader.setDownloadStatus(TGDownloadManager.DOWNLOAD_FAILED);
		// 如果不是断点下载，删除本地文件和数据库记录; 断点下载，更新本地数据库下载状态
    	if(!downloader.isBreakPoints())
    	{
    		FileUtils.deleteFile(downloader.getSavePath());
    		TGDownloadDBHelper.getInstance(context).deleteDownloader(downloader);
    	}
    	else
    	{
    		TGDownloadDBHelper.getInstance(context).updateDownloader(downloader);
    	}
		
		if (downloadListener != null)
		{
			downloadListener.downloadFailed(downloader);
		}
		else
		{
			LogTools.e(LOG_TAG,
					"[Method:failedDownload]  downloadListener is null,Please set ReceiveListener on Construct..");
		}
	}
	
	/**
	 * 
	 * 该方法的作用: 下载文件出错，直接删除本地文件和数据库记录
	 * @date 2014年8月19日
	 * @param downloader
	 */
	private void downloadError(TGDownloader downloader)
	{
		downloader.setDownloadStatus(TGDownloadManager.DOWNLOAD_FAILED);
		// 删除本地文件和数据库记录
		FileUtils.deleteFile(downloader.getSavePath());
		TGDownloadDBHelper.getInstance(context).deleteDownloader(downloader);
		
		if (downloadListener != null)
		{
			downloadListener.downloadFailed(downloader);
		}
		else
		{
			LogTools.e(LOG_TAG,
					"[Method:failedDownload]  downloadListener is null,Please set ReceiveListener on Construct..");
		}
	}
	
	/**
	 * 
	 * 该方法的作用: 停止下载，如果不是断点下载，删除本地文件
	 * @date 2014年8月19日
	 * @param downloader
	 */
	private void downloadStop(TGDownloader downloader)
	{
		downloader.setDownloadStatus(TGDownloadManager.DOWNLOAD_PAUSE);
		// 如果不是断点下载，删除本地文件和数据库记录; 断点下载，更新本地数据库下载状态
    	if(!downloader.isBreakPoints())
    	{
    		FileUtils.deleteFile(downloader.getSavePath());
    		TGDownloadDBHelper.getInstance(context).deleteDownloader(downloader);
    	}
    	else
    	{
    		TGDownloadDBHelper.getInstance(context).updateDownloader(downloader);
    	}
		
		if (downloadListener != null)
		{
			downloadListener.downloadPause(downloader);
		}
		else
		{
			LogTools.e(LOG_TAG,
					"[Method:failedDownload]  downloadListener is null,Please set ReceiveListener on Construct..");
		}
	}
	
	/**
	 * 
	 * 该方法的作用: 取消下载，直接删除本地文件和数据库记录
	 * @date 2014年8月19日
	 * @param downloader
	 */
	private void downloadCancel(TGDownloader downloader)
	{
		downloader.setDownloadStatus(TGDownloadManager.DOWNLOAD_PAUSE);
		// 删除本地文件和数据库记录
		FileUtils.deleteFile(downloader.getSavePath());
		TGDownloadDBHelper.getInstance(context).deleteDownloader(downloader);
		
		if (downloadListener != null)
		{
			downloadListener.downloadCanceled(downloader);
		}
		else
		{
			LogTools.e(LOG_TAG,
					"[Method:failedDownload]  downloadListener is null,Please set ReceiveListener on Construct..");
		}
	}
	
	/**
	 * 接收下载数据回调
	 */
	IDownloadReceiveListener receiveListener = new IDownloadReceiveListener()
	{
		@Override
		public void downloading(TGDownloader downloader)
		{
			// 回调下载中方法
			downloadDownloading(downloader);
		}

		@Override
		public void onFinish(TGDownloader downloader)
		{
			// 下载完成，校验本地文件校验值与服务端下发的校验值是否一致
			if(!equalDownloadFileCheckStr(downloader.getSavePath(), downloader.getCheckKey()))
			{
				downloader.setErrorCode(TGErrorMsgEnum.FAILED_CHECK_FILE_MD5.code);
				downloader.setErrorMsg(TGErrorMsgEnum.getErrorMsg(getContext(), TGErrorMsgEnum.FAILED_CHECK_FILE_MD5));
				downloadError(downloader);
				return;
			}
			// 校验成功，回调下载成功方法
			downloadFinish(downloader);
		}

		@Override
		public void onFailed(TGDownloader downloader)
		{
			// 如果是下载前校验文件流出错，删除出错文件与数据，重新开始下载
			if(downloader.getErrorCode() == TGErrorMsgEnum.FAILED_CHECK_FILE_MD5.code)
			{
				// 删除本地文件
				FileUtils.deleteFile(downloader.getSavePath());
				// 如果是断点下载，删除数据库记录
				if(downloader.isBreakPoints())
		    	{
					TGDownloadDBHelper.getInstance(context).deleteDownloader(downloader);
		    	}
		    	
		    	// 重新下载
		    	downloader.setCompleteSize(0);
		    	downloader.setCheckKey("");
		    	downloader.setErrorCode(0);
		    	downloader.setErrorMsg("");
		    	executeDownload(context, downloader);
				return;
			}
			
			downloadFailed(downloader);
		}

		@Override
		public void onStop(TGDownloader downloader)
		{
			downloadStop(downloader);
		}
	};
	
	/**
	 * 
	 * 该方法的作用: 校验下载完成后的文件流校验值是否和服务端下发的校验值一致
	 *           返回true, 表示下载文件与服务端文件一致；否则不一致
	 *           默认为MD5校验
	 * @date 2014年7月16日
	 * @param fileUrl
	 * @param serverCheckString
	 * @return
	 */
	private boolean equalDownloadFileCheckStr(String fileUrl, String serverCheckString)
	{
		// 服务端没返回md5时，不做校验
		if(TextUtils.isEmpty(serverCheckString))
		{
			return true;
		}
		
		// 文件不存在，返回校验不通过
		File file = FileUtils.getFile(fileUrl);
		if(file == null || !file.exists())
		{
			LogTools.e(LOG_TAG, "[method:equalDownloadFileCheckStr]: " + "file is not exist.");
			return false;
		}
		
		String fileCheckStr = getLocalFileCheckStr(fileUrl);
		LogTools.i(LOG_TAG, "[method:equalDownloadFileCheckStr], " + "fileCheckString: " + fileCheckStr
				+ "\r\n ; serverCheckString: " + serverCheckString + "\r\n ; url: " + fileUrl);
		if (null != fileCheckStr)
		{
			if (fileCheckStr.equals(serverCheckString))
			{
				return true;
			}
			else
			{
				// 删除错误文件
				file.delete();
				
				LogTools.e(LOG_TAG, "[method:equalDownloadFileCheckStr]: " + "check string is not the same.");
			}
		}

		return false;
	}
	
	/**
	 * 
	 * 该方法的作用: 获取本地文件加密字符串: 默认为MD5加密
	 * @date 2014年8月23日
	 * @param filePath
	 * @return
	 */
	protected String getLocalFileCheckStr(String filePath)
	{
		File file = FileUtils.getFile(filePath);
		InputStream inputStream = null;
		String fileCheckStr = "";
		try
		{
			inputStream = new FileInputStream(file);
			fileCheckStr = MD5.md5sum(inputStream);
		}
		catch (FileNotFoundException e)
		{
			LogTools.e(LOG_TAG, "[method:equalDownloadFileCheckStr]: " + e.getMessage(), e);
		}
		finally
		{
			Commons.closeInputStream(inputStream);
		}
		
		return fileCheckStr;
	}

	public IDownloadReceiveListener getReceiveListener()
	{
		return receiveListener;
	}

	public void setReceiveListener(IDownloadReceiveListener receiveListener)
	{
		this.receiveListener = receiveListener;
	}
}
