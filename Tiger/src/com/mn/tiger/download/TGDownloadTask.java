package com.mn.tiger.download;

import android.os.Bundle;

import com.mn.tiger.download.observe.TGDownloadObserveController;
import com.mn.tiger.request.error.IHttpErrorHandler;
import com.mn.tiger.task.TGTask;
import com.mn.tiger.utility.LogTools;

/**
 * 
 * 该类作用及功能说明: 下载任务类
 * 
 * @date 2014年8月18日
 */
public class TGDownloadTask extends TGTask
{
	/**
	 * 下载策略
	 */
	private IDownloadStrategy downloadStrategy = null;
	
	/**
	 * 下载信息
	 */
	private TGDownloadParams mDownloadParams;
	
	/**
	 * 异常处理接口
	 */
	private IHttpErrorHandler httpErrorHandler;
	
	/**
	 * 下载任务监听
	 */
	private IDownloadListener downloadListener = new DefaultDownloadListener();
	
	/**
	 * 构造函数
	 * @date 2014年6月28日
	 */
	public TGDownloadTask()
	{
		super();
		this.setType(TASK_TYPE_DOWNLOAD);
	}
	
	/**
	 * 该方法的作用:
	 * 执行自身（任务真正的执行方法）
	 * @date 2014年3月17日
	 * @return
	 */
	@Override
	protected MPTaskState executeOnSubThread()
	{
		// 后台执行下载任务
		downloadInBackground();
		// 任务完成，结束任务
		return MPTaskState.FINISHED;
	}
	
	/**
	 * 
	 * 该方法的作用: 后台执行下载任务
	 * @date 2014年6月28日
	 * @return
	 */
	protected void downloadInBackground()
	{
		LogTools.p(LOG_TAG, "[Metohd:downloadInBackground]" + "; taskid: " + this.getTaskID());
		mDownloadParams = getDownloadParams();
		
		executeDownload();
	}
	
	/**
	 * 
	 * 该方法的作用: 获取下载任务参数
	 * @date 2014年6月19日
	 * @return
	 */
	protected TGDownloadParams getDownloadParams()
	{
		if (null == getParams())
		{
			return null;
		}
		
		Bundle params = (Bundle) getParams();
		TGDownloadParams downloadParams = (TGDownloadParams) params.getSerializable("downloadParams");

		return downloadParams;
	}
	
	/**
	 * 
	 * 该方法的作用: 执行下载任务
	 * @date 2014年7月23日
	 */
	protected void executeDownload()
	{
		// 下载
		downloadStrategy = new TGDownloadStrategy(getContext(), this, downloadListener, httpErrorHandler);
		downloadStrategy.download(mDownloadParams);
	}
	
	@Override
	protected void onTaskCancel() 
	{
		TGDownloadObserveController.getInstance().unregisterObserverByKey(String.valueOf(this.getTaskID()));
		super.onTaskCancel();
	}
	
	/**
	 * 下载任务，暂停时的克隆方法，设置新的执行时间，放到下载任务的最后执行
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		TGTask task = (TGTask)super.clone();
		task.setExecutionTime(System.currentTimeMillis());
		
		return task;
	}
	
	/**
	 * 
	 * 该类作用及功能说明 : 默认下载监听
	 * 
	 * @date 2014年8月25日
	 */
	public class DefaultDownloadListener implements IDownloadListener
	{
		private final String LOG_TAG = this.getClass().getSimpleName();
		
		@Override
		public void downloadStart(TGDownloader downloader) 
		{
			LogTools.p(LOG_TAG, "[Metohd:downloadStart]" + "; taskid: " + TGDownloadTask.this.getTaskID());
			sendDownloadResult(downloader);
		}
		
		@Override
		public void downloadSucceed(TGDownloader downloader)
		{
			LogTools.p(LOG_TAG, "[Metohd:downloadSucceed]" + "; taskid: " + TGDownloadTask.this.getTaskID());
			sendDownloadResult(downloader);
			onTaskFinished();
		}
		
		@Override
		public void downloadProgress(TGDownloader downloader, int progress)
		{
			LogTools.d(LOG_TAG, "[Metohd:downloadProgress]" + "; taskid: " + TGDownloadTask.this.getTaskID() + "; progress:" + progress);
			
			sendDownloadResult(downloader);
			
			onTaskChanged(progress);
		}
		
		@Override
		public void downloadFailed(TGDownloader downloader)
		{
			LogTools.p(LOG_TAG, "[Metohd:downloadFailed]" + "; taskid: " + TGDownloadTask.this.getTaskID());
			sendDownloadResult(downloader);
			onTaskError(((TGDownloader)downloader).getErrorCode(), ((TGDownloader)downloader).getErrorMsg());
		}
		
		@Override
		public void downloadStop(TGDownloader downloader)
		{
			LogTools.p(LOG_TAG, "[Metohd:downloadStop]" + "; taskid: " + TGDownloadTask.this.getTaskID());
			sendDownloadResult(downloader);
		}
		
		@Override
		public void downloadCanceled(TGDownloader downloader)
		{
			LogTools.p(LOG_TAG, "[Metohd:downloadCanceled]" + "; taskid: " + TGDownloadTask.this.getTaskID());
			sendDownloadResult(downloader);
		}
		
		private void sendDownloadResult(Object downloader)
		{
			sendTaskResult(downloader);
		}
	}

	public IDownloadStrategy getDownloadStrategy()
	{
		return downloadStrategy;
	}

	public void setDownloadStrategy(IDownloadStrategy downloadStrategy)
	{
		this.downloadStrategy = downloadStrategy;
	}

	public IHttpErrorHandler getHttpErrorHandler()
	{
		return httpErrorHandler;
	}

	public void setHttpErrorHandler(IHttpErrorHandler httpErrorHandler)
	{
		this.httpErrorHandler = httpErrorHandler;
	}

	public IDownloadListener getDownloadListener()
	{
		return downloadListener;
	}

	public void setDownloadListener(IDownloadListener downloadListener)
	{
		this.downloadListener = downloadListener;
	}

	public TGDownloadParams getmDownloadParams()
	{
		return mDownloadParams;
	}

	public void setmDownloadParams(TGDownloadParams mDownloadParams)
	{
		this.mDownloadParams = mDownloadParams;
	}
}
