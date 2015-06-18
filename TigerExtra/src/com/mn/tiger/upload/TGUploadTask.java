package com.mn.tiger.upload;

import android.os.Bundle;

import com.mn.tiger.log.LogTools;
import com.mn.tiger.task.TGTask;

/**
 * 
 * 该类作用及功能说明: 上传任务类
 * 
 * @date 2014年6月28日
 */
public class TGUploadTask extends TGTask
{
	/**
	 * 日志标签
	 */
	protected static final String LOG_TAG = TGUploadTask.class.getSimpleName();
	
	/**
	 * 上传策略
	 */
	protected IUploadStrategy uploadStrategy = null;
	
	/**
	 * 上传信息 
	 */
	private TGUploadParams uploadParams;
	
	/**
	 * 构造函数
	 * @date 2014年6月28日
	 */
	public TGUploadTask()
	{
		super();
		this.setType(TASK_TYPE_UPLOAD);
	}
	
	/**
	 * 该方法的作用:
	 * 执行自身（任务真正的执行方法）
	 * @date 2014年3月17日
	 * @return
	 */
	@Override
	protected TGTaskState executeOnSubThread()
	{
		// 后台执行上传任务
		uploadInBackground();
		// 任务完成，结束任务
		return TGTaskState.FINISHED;
	}
	
	/**
	 * 
	 * 该方法的作用: 后台执行上传任务
	 * @date 2014年6月28日
	 * @return
	 */
	protected void uploadInBackground()
	{
		LogTools.p(LOG_TAG, "[Metohd:uploadInBackground]" + "; taskid: " + this.getTaskID());
		
		executeUpload();
	}
	
	/**
	 * 
	 * 该方法的作用: 获取上传任务参数
	 * @date 2014年6月19日
	 * @return
	 */
	protected TGUploadParams getUploadParams()
	{
		if (null == getParams())
		{
			return null;
		}
		
		if(null == uploadParams)
		{
			Bundle params = (Bundle) getParams();
			uploadParams = (TGUploadParams) params.getSerializable("uploadParams");
		}
		
		return uploadParams;
	}
	
	/**
	 * 
	 * 该方法的作用: 执行上传任务
	 * @date 2014年7月23日
	 */
	protected void executeUpload()
	{
		// 上传
		uploadStrategy = new TGUploadStrategy(getContext(), this);
		uploadStrategy.upload(getUploadParams());
	}
	
	@Override
	protected void onTaskCancel() 
	{
		TGUploader uploader = TGUploader.getInstanse(getUploadParams());
		onUploadCanceled(uploader);
		
		if(null != uploadStrategy)
		{
			uploadStrategy.shutdown();
		}
		super.onTaskCancel();
	}
	
	@Override
	protected void onTaskPause()
	{
		TGUploader uploader = TGUploader.getInstanse(getUploadParams());
		onUploadStop(uploader);
		
		if(null != uploadStrategy)
		{
			uploadStrategy.shutdown();
		}
		
		super.onTaskPause();
	}
	
	/**
	 * 上传任务，暂停时的克隆方法，设置新的执行时间，放到上传任务的最后执行
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		TGTask task = (TGTask)super.clone();
		
		return task;
	}
	
	/**
	 * 上传任务启动时的回调方法
	 * @param uploader
	 */
	void onUploadStart(TGUploader uploader)
	{
		LogTools.p(LOG_TAG, "[Metohd:uploadStart]");
		sendUploadResult(uploader);
	}
	
	/**
	 * 上传任务成功的回调方法
	 * @param uploader
	 */
	void onUploadSuccess(TGUploader uploader)
	{
		LogTools.p(LOG_TAG, "[Metohd:uploadSucceed]" + "; taskid: " + TGUploadTask.this.getTaskID());
		sendUploadResult(uploader);
		onTaskFinished();
	}
	
	/**
	 * 上传进度回调方法
	 * @param uploader
	 * @param progress
	 */
	void onUploadProgress(TGUploader uploader, int progress)
	{
		LogTools.d(LOG_TAG, "[Metohd:uploadProgress]" + "; taskid: " + TGUploadTask.this.getTaskID());
		sendUploadResult(uploader);
		onTaskChanged(progress);
	}
	
	/**
	 * 上传失败的回调方法
	 * @param uploader
	 */
	void onUploadFailed(TGUploader uploader)
	{
		LogTools.p(LOG_TAG, "[Metohd:uploadFailed]" + "; taskid: " + TGUploadTask.this.getTaskID());
		sendUploadResult(uploader);
		
		onTaskError(uploader.getErrorCode(), uploader.getErrorMsg());
	}
	
	/**
	 * 上传取消的回调方法
	 * @param uploader
	 */
	void onUploadCanceled(TGUploader uploader)
	{
		LogTools.p(LOG_TAG, "[Metohd:uploadCanceled]" + "; taskid: " + TGUploadTask.this.getTaskID());
		sendUploadResult(uploader);
	}
	
	/**
	 * 上传停止的回调方法
	 * @param uploader
	 */
	void onUploadStop(TGUploader uploader)
	{
		LogTools.p(LOG_TAG, "[Metohd:uploadStop]" + "; taskid: " + TGUploadTask.this.getTaskID());
		sendUploadResult(uploader);
	}
	
	/**
	 * 发送上传结果
	 * @param uploader
	 */
	private void sendUploadResult(TGUploader uploader)
	{
		sendTaskResult(uploader);
	}
	
	public IUploadStrategy getUploadStrategy()
	{
		return uploadStrategy;
	}

	public void setUploadStrategy(IUploadStrategy uploadStrategy)
	{
		this.uploadStrategy = uploadStrategy;
	}

}
