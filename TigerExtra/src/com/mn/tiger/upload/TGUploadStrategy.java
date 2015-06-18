package com.mn.tiger.upload;

import java.net.HttpURLConnection;

import android.content.Context;

import com.mn.tiger.log.LogTools;
import com.mn.tiger.request.client.ApacheHttpClient;
import com.mn.tiger.request.method.ApachePostMethod;
import com.mn.tiger.request.method.ProgressEntiryWrapper.ProgressListener;
import com.mn.tiger.request.method.TGHttpParams;
import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.task.TGTask.TGTaskState;

/**
 * 
 * 该类作用及功能说明: 上传具体策略
 * 
 * @date 2014年7月30日
 */
public class TGUploadStrategy implements IUploadStrategy, ProgressListener
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
	 * 上传任务
	 */
	protected TGUploadTask uploadTask = null;
	
	/**
	 * 上传信息
	 */
	protected TGUploader uploader;
	
	private ApacheHttpClient httpClient;
	
	private long contentLength = 0;
	
	private int progress = 0;
	
	/**
	 * 构造函数
	 * @date 2014年8月5日
	 * @param context
	 * @param uploadTask
	 * @param listener
	 */
	public TGUploadStrategy(Context context, TGUploadTask uploadTask)
	{
		this.context = context;
		this.uploadTask = uploadTask;
	}

	@Override
	public void upload(TGUploadParams uploadParams)
	{
		LogTools.p(LOG_TAG, "[Method:upload]");

		// 获取上传参数
		uploader = getUploader(uploadParams);
		// 通知上传开始
		uploadTask.onUploadStart(uploader);
		// 执行上传
		executeUpload(context, uploader);
	}
	
	/**
	 * 该方法的作用: 获取上传信息
	 * @date 2014年8月18日
	 * @param uploadParams
	 * @return
	 */
	protected TGUploader getUploader(TGUploadParams uploadParams)
	{
		TGUploader uploader = createNewUploader(uploadParams);;
		uploader.setUploadStatus(TGUploadManager.UPLOAD_STARTING);
		
		return uploader;
	}
	
	protected TGUploader createNewUploader(TGUploadParams uploadParams)
	{
		TGUploader uploader = new TGUploader();
		uploader.setId(uploadTask.getTaskID().toString());
		uploader.setServiceURL(uploadParams.getServiceURL());
		uploader.setType(uploadParams.getUploadType());
		uploader.setStringParams(uploadParams.getStringParams());
		uploader.setFileParams(uploadParams.getFileParams());
		uploader.setTaskClsName(uploadParams.getTaskClsName());
		uploader.setParamsClsName(uploadParams.getClass().getName());
		
		return uploader;
	}
	
	/**
	 * 该方法的作用: 请求上传
	 * @date 2014年8月5日
	 * @param context
	 * @param uploader
	 */
	protected void executeUpload(Context context, TGUploader uploader)
	{
		LogTools.p(LOG_TAG, "[Method:executeUpload]");
		
		// 任务结束，不执行上传
		if (null == uploadTask)
		{
			uploadCancel(uploader);
			return;
		}
		
		httpClient = new ApacheHttpClient(context);
		ApachePostMethod postMethod = new ApachePostMethod();
		
		TGHttpParams httpParams = uploader.convertToHttpParams();
		httpParams.setProgressListener(this);
		
		contentLength = httpParams.getContentLength();
		
		postMethod.setReqeustParams(httpParams);
		postMethod.setUrl(uploader.getServiceURL());
		TGHttpResult httpResult = httpClient.execute(postMethod);
		
		if(!catchUploadError(uploader, httpResult))
		{
			uploadSuccess(uploader);
		}
	}
	
	@Override
	public void shutdown()
	{
		httpClient.shutdown();
	}

	/**
	 * 该方法的作用:处理请求结果
	 * 
	 * @date 2014年4月3日
	 * @param uploader
	 * @param httpResult
	 * @return
	 */
	protected boolean catchUploadError(TGUploader uploader, TGHttpResult httpResult)
	{
		if (httpResult != null && httpResult.getResponseCode() != HttpURLConnection.HTTP_OK)
		{
			uploader.setErrorCode(httpResult.getResponseCode());
			uploader.setErrorMsg(httpResult.getResult());
			uploadFailed(uploader);
			
			return true;
		}
		
		return false;
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
	
	@Override
	public void transferred(long num)
	{
		if(contentLength > 0)
		{
			int currentProgress = (int) ((num * 100)/contentLength);
			uploader.setCompleteSize(num);
			if(currentProgress > progress)
			{
				progress = currentProgress;
				uploading(uploader, currentProgress);
			}
		}
	}
	
	/**
	 * 该方法的作用: 上传过程中
	 * @date 2014年8月19日
	 * @param uploader
	 */
	void uploading(TGUploader uploader, int progress)
	{
		// 修改上传状态为正在上传
		uploader.setUploadStatus(TGUploadManager.UPLOAD_UPLOADING);
		
		if(null != uploadTask && uploadTask.getTaskState() == TGTaskState.RUNNING)
		{
			uploadTask.onUploadProgress(uploader, progress);
		}
	}
	
	/**
	 * 
	 * 该方法的作用: 上传文件完成，删除数据库记录
	 * @date 2014年8月19日
	 * @param uploader
	 */
	private void uploadSuccess(TGUploader uploader)
	{
		// 删除本地记录
		uploader.setUploadStatus(TGUploadManager.UPLOAD_SUCCEED);
		TGUploadDBHelper.getInstance(context).deleteUploader(uploader);
		if(null != uploadTask && uploadTask.getTaskState() == TGTaskState.RUNNING)
		{
			uploadTask.onUploadSuccess(uploader);
		}
	}
	
	/**
	 * 
	 * 该方法的作用: 上传文件过程中出现异常，如果不是断点上传，删除本地文件
	 * @date 2014年8月19日
	 * @param uploader
	 */
	protected void uploadFailed(TGUploader uploader)
	{
		uploader.setUploadStatus(TGUploadManager.UPLOAD_FAILED);
		TGUploadDBHelper.getInstance(context).deleteUploader(uploader);
		
		if (null != uploadTask && uploadTask.getTaskState() == TGTaskState.RUNNING)
		{
			uploadTask.onUploadFailed(uploader);
		}
	}
	
	/**
	 * 该方法的作用: 停止上传，如果不是断点上传，删除本地文件
	 * @date 2014年8月19日
	 * @param uploader
	 */
	void uploadStop(TGUploader uploader)
	{
		uploader.setUploadStatus(TGUploadManager.UPLOAD_PAUSE);
		// 如果不是分块上传，删除数据库记录
    	if(true)
    	{
    		TGUploadDBHelper.getInstance(context).deleteUploader(uploader);
    	}
		
		if (null != uploadTask && uploadTask.getTaskState() == TGTaskState.RUNNING)
		{
			uploadTask.onUploadStop(uploader);
		}
	}
	
	/**
	 * 
	 * 该方法的作用: 取消上传，直接删除本地文件和数据库记录
	 * @date 2014年8月19日
	 * @param uploader
	 */
	private void uploadCancel(TGUploader uploader)
	{
		uploader.setUploadStatus(TGUploadManager.UPLOAD_PAUSE);
		// 删除数据库记录
		TGUploadDBHelper.getInstance(context).deleteUploader(uploader);
		if(null != uploadTask && uploadTask.getTaskState() == TGTaskState.RUNNING)
		{
			uploadTask.onUploadCanceled(uploader);
		}
	}

}
