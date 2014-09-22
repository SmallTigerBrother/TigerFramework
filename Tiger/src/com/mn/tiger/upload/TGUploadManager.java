package com.mn.tiger.upload;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.mn.tiger.task.TGTask;
import com.mn.tiger.task.TGTaskManager;
import com.mn.tiger.task.invoke.TGTaskParams;
import com.mn.tiger.task.result.TGTaskResult;
import com.mn.tiger.task.result.TGTaskResultHandler;
import com.mn.tiger.upload.observe.TGUploadObserveController;
import com.mn.tiger.upload.observe.TGUploadObserver;
import com.mn.tiger.utility.LogTools;

/**
 * 
 * 该类作用及功能说明: 上传管理类
 * 
 * @date 2014年6月18日
 */
public class TGUploadManager
{	
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 上传状态
	 */
	public static final int UPLOAD_WAITING = -2;
	
	public static final int UPLOAD_STARTING = -1;
	
	public static final int UPLOAD_UPLOADING = 0;

	public static final int UPLOAD_SUCCEED = 1;

	public static final int UPLOAD_FAILED = 2;
	
	public static final int UPLOAD_STOP = 3;
	
	/**
	 * 上下文信息
	 */
	private Context mContext;
	
	/**
	 * 构造方法
	 * @date 2014年6月24日
	 * @param context
	 */
	public TGUploadManager(Context context)
	{
		mContext = context;
	}
	
	/**
	 * 
	 * 该方法的作用: 开始上传
	 * @date 2014年6月20日
	 * @param uploadParams
	 */
	public int start(TGUploadParams uploadParams)
	{
		return enqueue(uploadParams);
	}
	
	/**
	 * 
	 * 该方法的作用: 取消上传
	 * @date 2014年6月20日
	 * @param taskId
	 */
	public void cancel(int taskId)
	{
		TGTaskManager.getInstance(mContext).cancelTask(taskId, TGTask.TASK_TYPE_UPLOAD);
	}
	
	/**
	 * 
	 * 该方法的作用: 停止上传
	 * @date 2014年6月20日
	 * @param taskId
	 */
	public void pause(int taskId)
	{
		TGTaskManager.getInstance(mContext).stopTask(taskId, TGTask.TASK_TYPE_UPLOAD);
	}
	
	/**
	 * 
	 * 该方法的作用: 启动传入类型所有上传任务
	 * 
	 * @date 2014年8月26日
	 */
	public void startAll(String type)
	{
		List<TGUploader> uploaders = TGUploadDBHelper.getInstance(mContext).getUploaderByType(type);

		if (null != uploaders)
		{
			TGUploadParams uploadParams = null;
			try
			{
				for (TGUploader uploader : uploaders)
				{
					// 获取上传参数
					uploadParams = (TGUploadParams) Class.forName(uploader.getParamsClsName()).newInstance();
					uploadParams.setFilePath(uploader.getFilePath());
					uploadParams.setServiceURL(uploader.getServiceURL());
					if (!TextUtils.isEmpty(uploader.getTaskClsName()))
					{
						uploadParams.setTaskClsName(uploader.getTaskClsName());
					}
					start(uploadParams);
				}
			}
			catch (Exception e)
			{
				LogTools.e(LOG_TAG, e.getMessage(), e);
			}
		}
	}

	/**
	 * 
	 * 该方法的作用: 取消传入类型所有上传任务
	 * 
	 * @date 2014年8月26日
	 */
	public void cancelAll(String type)
	{
		List<TGUploader> uploaders = TGUploadDBHelper.getInstance(mContext).getUploaderByType(type);

		if (null != uploaders)
		{
			for (TGUploader uploader : uploaders)
			{
				cancel(Integer.valueOf(uploader.getId()));
			}
		}
	}

	/**
	 * 该方法的作用: 停止传入类型所有上传任务
	 * 
	 * @date 2014年8月26日
	 */
	public void pauseAll(String type)
	{
		List<TGUploader> uploaders = TGUploadDBHelper.getInstance(mContext).getUploaderByType(type);

		if (null != uploaders)
		{
			for (TGUploader uploader : uploaders)
			{
				pause(Integer.valueOf(uploader.getId()));
			}
		}
	}
	
	/**
	 * 
	 * 该方法的作用: 启动所有上传任务
	 * 
	 * @date 2014年8月26日
	 */
	public void startAll()
	{
		List<TGUploader> uploaders = TGUploadDBHelper.getInstance(mContext).getAllUploader();

		if (null != uploaders)
		{
			TGUploadParams uploadParams = null;
			
			try
			{
				for (TGUploader uploader : uploaders)
				{
					// 获取上传参数
					uploadParams = (TGUploadParams) Class.forName(uploader.getParamsClsName()).newInstance();
					uploadParams.setFilePath(uploader.getFilePath());
					uploadParams.setServiceURL(uploader.getServiceURL());
					if (!TextUtils.isEmpty(uploader.getTaskClsName()))
					{
						uploadParams.setTaskClsName(uploader.getTaskClsName());
					}
					start(uploadParams);
				}
			}
			catch (Exception e)
			{
				LogTools.e(LOG_TAG, e.getMessage(), e);
			}
		}
	}

	/**
	 * 该方法的作用: 取消所有上传任务
	 * 
	 * @date 2014年8月26日
	 */
	public void cancelAll()
	{
		List<TGUploader> uploaders = TGUploadDBHelper.getInstance(mContext).getAllUploader();
		if (null != uploaders)
		{
			for (TGUploader uploader : uploaders)
			{
				cancel(Integer.valueOf(uploader.getId()));
			}
		}
	}

	/**
	 * 
	 * 该方法的作用: 停止所有上传任务
	 * 
	 * @date 2014年8月26日
	 */
	public void pauseAll()
	{
		List<TGUploader> uploaders = TGUploadDBHelper.getInstance(mContext).getAllUploader();

		if (null != uploaders)
		{
			for (TGUploader uploader : uploaders)
			{
				pause(Integer.valueOf(uploader.getId()));
			}
		}
	}
	
	/**
	 * 
	 * 该方法的作用: 把Uploader任务添加到上传队列，返回任务id
	 * @date 2014年6月18日
	 * @param uploader
	 * @return
	 */
	private int enqueue(TGUploadParams uploadParams) 
	{
		LogTools.p(LOG_TAG, "[Method:enqueue] start");
		
		final Bundle params = new Bundle();
		params.putSerializable("uploadParams", uploadParams);
		
		// 查找数据库中是否存在该条数据
		TGUploader uploader = getUploadInfo(uploadParams.getFilePath());
		TGTaskParams taskParams = null;
		// 构建任务参数
		if(uploader != null)
		{
			taskParams = TGTaskManager.createTaskParams(params,
					uploadParams.getTaskClsName(), getResultHandler(), uploadParams.getWeight(), Integer.parseInt(uploader.getId()));
		}
		else
		{
			taskParams = TGTaskManager.createTaskParams(params,
					uploadParams.getTaskClsName(), getResultHandler(), uploadParams.getWeight());
		}
		
		taskParams.setBundleParams(params);
		taskParams.setTaskType(TGTask.TASK_TYPE_UPLOAD);
		
		// 启动任务
		return TGTaskManager.getInstance(mContext).startTask(taskParams);
    }
	
	/**
	 * 
	 * 该方法的作用: 根据传入的key，注册数据观察者
	 * 
	 * @date 2014年3月31日
	 * @param entityType
	 * @param observer
	 */
	public void registerDataSetObserver(int taskId, TGUploadObserver observer)
	{
		TGUploadObserveController.getInstance().registerDataSetObserver(
				String.valueOf(taskId), observer);
	}

	/**
	 * 该方法的作用: 取消注册observer
	 * 
	 * @date 2014年3月31日
	 * @param observer
	 */
	public void unregisterObserver(TGUploadObserver observer)
	{
		TGUploadObserveController.getInstance().unregisterObserver(observer);
	}
	
	/**
	 * 
	 * 该方法的作用:获取文件上传信息
	 * 
	 * @date 2014年8月19日
	 * @param filePath
	 * @return
	 */
	public TGUploader getUploadInfo(String filePath)
	{
		TGUploader uploader = null;
		uploader = TGUploadDBHelper.getInstance(mContext).getUploader(filePath);

		return uploader;
	}

	/**
	 * 
	 * 该方法的作用: 根据上传类型查询上传任务信息
	 * 
	 * @date 2014年8月24日
	 * @param uploadType
	 * @return
	 */
	public List<TGUploader> getUploadInfoByType(String uploadType)
	{
		return TGUploadDBHelper.getInstance(mContext).getUploaderByType(uploadType);
	}
	
	/**
	 * 上传任务回传handler
	 */
	private TGTaskResultHandler resultHandler = new TGTaskResultHandler()
	{
		@Override
		public void handleTaskResult(TGTaskResult result)
		{
			TGUploadObserveController.getInstance().notifyChange(result);
		}
	};
	
	public Context getmContext()
	{
		return mContext;
	}

	public void setmContext(Context mContext)
	{
		this.mContext = mContext;
	}

	public TGTaskResultHandler getResultHandler()
	{
		return resultHandler;
	}

	public void setResultHandler(TGTaskResultHandler resultHandler)
	{
		this.resultHandler = resultHandler;
	}
	
}
