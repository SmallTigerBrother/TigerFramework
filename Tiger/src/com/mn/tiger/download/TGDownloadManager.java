package com.mn.tiger.download;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.mn.tiger.datastorage.db.sqlite.Selector;
import com.mn.tiger.download.observe.TGDownloadObserveController;
import com.mn.tiger.download.observe.TGDownloadObserver;
import com.mn.tiger.task.TGTask;
import com.mn.tiger.task.TGTaskManager;
import com.mn.tiger.task.invoke.TGTaskParams;
import com.mn.tiger.task.result.TGTaskResult;
import com.mn.tiger.task.result.TGTaskResultHandler;
import com.mn.tiger.utility.FileUtils;
import com.mn.tiger.utility.LogTools;

/**
 * 
 * 该类作用及功能说明: 下载管理类
 * 
 * @date 2014年8月18日
 */
public class TGDownloadManager
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();

	/**
	 * 下载状态
	 */
	public static final int DOWNLOAD_WAITING = -2;
	
	public static final int DOWNLOAD_STARTING = -1;

	public static final int DOWNLOAD_DOWNLOADING = 0;

	public static final int DOWNLOAD_SUCCEED = 1;

	public static final int DOWNLOAD_FAILED = 2;

	public static final int DOWNLOAD_STOP = 3;

	/**
	 * 上下文信息
	 */
	private Context mContext;

	/**
	 * 构造方法
	 * 
	 * @date 2014年6月24日
	 * @param context
	 */
	public TGDownloadManager(Context context)
	{
		mContext = context;
	}

	/**
	 * 
	 * 该方法的作用: 开始下载
	 * 
	 * @date 2014年6月20日
	 * @param downloadParams
	 */
	public int start(TGDownloadParams downloadParams)
	{
		return enqueue(downloadParams);
	}

	/**
	 * 
	 * 该方法的作用: 取消下载
	 * 
	 * @date 2014年6月20日
	 * @param taskId
	 */
	public void cancel(int taskId)
	{
		TGTaskManager.getInstance(mContext).cancelTask(taskId, TGTask.TASK_TYPE_DOWNLOAD);
	}

	/**
	 * 
	 * 该方法的作用: 停止下载
	 * 
	 * @date 2014年6月20日
	 * @param taskId
	 */
	public void pause(int taskId)
	{
		TGTaskManager.getInstance(mContext).stopTask(taskId, TGTask.TASK_TYPE_DOWNLOAD);
	}

	/**
	 * 
	 * 该方法的作用: 启动传入类型所有下载任务
	 * 
	 * @date 2014年8月26日
	 */
	public void startAll(String type)
	{
		List<TGDownloader> downloaders = TGDownloadDBHelper.getInstance(mContext).getDownloader(type);

		if (null != downloaders)
		{
			TGDownloadParams downloadParams = null;
			for (TGDownloader downloader : downloaders)
			{
				// 获取下载参数
				downloadParams = TGDownloadParamsFactory.getDownloadParams(downloader
						.getTaskClsName());

				downloadParams.setRequestType(downloader.getRequestType());
				downloadParams.setSavePath(downloader.getSavePath());
				downloadParams.setUrlString(downloader.getUrlString());
				downloadParams.setParams(downloader.getParams());
				if (!TextUtils.isEmpty(downloader.getTaskClsName()))
				{
					downloadParams.setTaskClsName(downloader.getTaskClsName());
				}
				start(downloadParams);
			}
		}
	}

	/**
	 * 
	 * 该方法的作用: 取消传入类型所有下载任务
	 * 
	 * @date 2014年8月26日
	 */
	public void cancelAll(String type)
	{
		List<TGDownloader> downloaders = TGDownloadDBHelper.getInstance(mContext).getDownloader(type);

		if (null != downloaders)
		{
			for (TGDownloader downloader : downloaders)
			{
				cancel(Integer.valueOf(downloader.getId()));
			}
		}
	}

	/**
	 * 
	 * 该方法的作用: 停止传入类型所有下载任务
	 * 
	 * @date 2014年8月26日
	 */
	public void pauseAll(String type)
	{
		List<TGDownloader> downloaders = TGDownloadDBHelper.getInstance(mContext).getDownloader(type);

		if (null != downloaders)
		{
			for (TGDownloader downloader : downloaders)
			{
				pause(Integer.valueOf(downloader.getId()));
			}
		}
	}
	
	/**
	 * 
	 * 该方法的作用: 启动所有下载任务
	 * 
	 * @date 2014年8月26日
	 */
	public void startAll()
	{
		List<TGDownloader> downloaders = TGDownloadDBHelper.getInstance(mContext).getAllDownloader();

		if (null != downloaders)
		{
			TGDownloadParams downloadParams = null;
			for (TGDownloader downloader : downloaders)
			{
				// 获取下载参数
				downloadParams = TGDownloadParamsFactory.getDownloadParams(downloader
						.getTaskClsName());

				downloadParams.setRequestType(downloader.getRequestType());
				downloadParams.setSavePath(downloader.getSavePath());
				downloadParams.setUrlString(downloader.getUrlString());
				downloadParams.setParams(downloader.getParams());
				if (!TextUtils.isEmpty(downloader.getTaskClsName()))
				{
					downloadParams.setTaskClsName(downloader.getTaskClsName());
				}
				start(downloadParams);
			}
		}
	}

	/**
	 * 
	 * 该方法的作用: 取消所有下载任务
	 * 
	 * @date 2014年8月26日
	 */
	public void cancelAll()
	{
		List<TGDownloader> downloaders = TGDownloadDBHelper.getInstance(mContext).getAllDownloader();
		if (null != downloaders)
		{
			for (TGDownloader downloader : downloaders)
			{
				cancel(Integer.valueOf(downloader.getId()));
			}
		}
	}

	/**
	 * 
	 * 该方法的作用: 停止所有下载任务
	 * 
	 * @date 2014年8月26日
	 */
	public void pauseAll()
	{
		List<TGDownloader> downloaders = TGDownloadDBHelper.getInstance(mContext).getAllDownloader();

		if (null != downloaders)
		{
			for (TGDownloader downloader : downloaders)
			{
				pause(Integer.valueOf(downloader.getId()));
			}
		}
	}

	/**
	 * 
	 * 该方法的作用: 把下载任务添加到下载队列，返回任务id
	 * 
	 * @date 2014年6月18日
	 * @param downloadParams
	 * @return
	 */
	private int enqueue(TGDownloadParams downloadParams)
	{
		LogTools.p(LOG_TAG, "[Method:enqueue] start");
		
		final Bundle params = new Bundle();
		params.putSerializable("downloadParams", downloadParams);

		if (TextUtils.isEmpty(downloadParams.getTaskClsName()))
		{
			downloadParams.setTaskClsName(TGDownloadTask.class.getName());
		}
		
		// 获取本地是否有相同的下载任务
		TGDownloader downloader = getLocalDownloader(downloadParams);
		// 检查本地下载任务和下载文件
		downloader = checkLocalDownloader(downloader);
		
		// 构建任务参数，当没有taskID传入时，直接构建新任务下载。
		// 当有taskID传入时，需要在下载队列中查看是否已经有该下载任务，如果有，则不不需要新加入任务。
		TGTaskParams taskParams = null;
		if(downloader != null)
		{
			taskParams = TGTaskManager.createTaskParams(params,
					downloadParams.getTaskClsName(), getResultHandler(), downloadParams.getWeight(), Integer.parseInt(downloader.getId()));
		}
		else
		{
			taskParams = TGTaskManager.createTaskParams(params,
				downloadParams.getTaskClsName(), getResultHandler(), downloadParams.getWeight());
		}

		taskParams.setBundleParams(params);
		taskParams.setTaskType(TGTask.TASK_TYPE_DOWNLOAD);

		// 启动任务
		return TGTaskManager.getInstance(mContext).startTask(taskParams);
	}

	/**
	 * 
	 * 该方法的作用: 根据下载参数从本地数据库中查询对应的downloader，查询策略由使用者自行决定。
	 *              如果没有重复的下载任务，返回null
	 * @date 2014年9月3日
	 * @return
	 */
	public TGDownloader getLocalDownloader(TGDownloadParams downloadParams)
	{
		return null;
	}
	
	/**
	 * 
	 * 该方法的作用: 检测本地文件和本地下载记录是否一致
	 * @date 2014年9月3日
	 * @param downloader
	 */
	public TGDownloader checkLocalDownloader(TGDownloader downloader)
	{
		if(downloader == null)
		{
			return null;
		}
		
		// 如果数据库中有记录，但本地文件不存在，删除数据库记录
		String savePath = downloader.getSavePath();
		File file = FileUtils.getFile(savePath);
		if(file == null || !file.exists())
		{
			delDownloader(downloader);
			return null;
		}
		
		return downloader;
	}
	
	/**
	 * 
	 * 该方法的作用: 根据传入的key，注册数据观察者
	 * 
	 * @date 2014年3月31日
	 * @param entityType
	 * @param observer
	 */
	public void registerDataSetObserver(int taskId, TGDownloadObserver observer)
	{
		TGDownloadObserveController.getInstance().registerDataSetObserver(String.valueOf(taskId),
				observer);
	}

	/**
	 * 该方法的作用: 取消注册observer
	 * 
	 * @date 2014年3月31日
	 * @param observer
	 */
	public void unregisterObserver(TGDownloadObserver observer)
	{
		TGDownloadObserveController.getInstance().unregisterObserver(observer);
	}

	/**
	 * 
	 * 该方法的作用:获取文件下载信息
	 * 
	 * @date 2014年8月19日
	 * @param urlstr
	 * @param params
	 * @return
	 */
	public TGDownloader getDownloadInfo(String urlstr, String params)
	{
		TGDownloader downloader = null;
		downloader = TGDownloadDBHelper.getInstance(mContext).getDownloader(urlstr, params);

		return downloader;
	}

	/**
	 * 
	 * 该方法的作用: 根据下载类型查询下载任务信息
	 * 
	 * @date 2014年8月24日
	 * @param downloadType
	 * @return
	 */
	public List<TGDownloader> getDownloadInfoByType(String downloadType)
	{
		return TGDownloadDBHelper.getInstance(mContext).getDownloader(downloadType);
	}

	/**
	 * 
	 * 该方法的作用:根据docid获取文件下载信息（edm用）
	 * 
	 * @date 2014年8月19日
	 * @param docId
	 * @return
	 */
	public TGDownloader getDownloadByDocId(String docId)
	{
		TGDownloader downloader = null;
		downloader = TGDownloadDBHelper.getInstance(mContext).getDownloaderByDocId(docId);

		return downloader;
	}

	/**
	 * 
	 * 该方法的作用: 根据传入sql查询下载任务信息
	 * 
	 * @date 2014年8月24日
	 * @param selector
	 * @return
	 */
	public List<TGDownloader> getDownloadInfoBySQL(Selector selector)
	{
		return TGDownloadDBHelper.getInstance(mContext).getDownloaderBySql(selector);
	}
	
	/**
	 * 
	 * 该方法的作用: 删除下载任务
	 * 
	 * @date 2014年8月24日
	 * @param downloader
	 * @return
	 */
	public void delDownloader(TGDownloader downloader)
	{
		TGDownloadDBHelper.getInstance(mContext).deleteDownloader(downloader);
	}

	/**
	 * 下载任务回传handler
	 */
	private TGTaskResultHandler resultHandler = new TGTaskResultHandler()
	{
		@Override
		public void handleTaskResult(TGTaskResult result)
		{
			TGDownloadObserveController.getInstance().notifyChange(result);
		}
	};

	public Context getContext()
	{
		return mContext;
	}

	public void setContext(Context mContext)
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
