package com.mn.tiger.download.observe;

import android.database.Observable;

import com.mn.tiger.download.TGDownloader;
import com.mn.tiger.log.LogTools;

/**
 * 
 * 该类作用及功能说明：用一个key的观察者，（取消）注册观察者到arraylist
 * 
 * @date 2014年3月31日
 */
public class TGDownloadObservable extends Observable<TGDownloadObserver>
{
	/**
	 * log tag
	 */
	public static final String TAG = TGDownloadObservable.class.getSimpleName();
	
	/**
	 * 取消注册
	 */
	@Override
	public void unregisterObserver(TGDownloadObserver observer)
	{
		super.unregisterObserver(observer);
		if (mObservers == null || (mObservers != null && mObservers.size() == 0))
		{
			mOnObserverChangeListener.onObserverClear(observer.getKey());
		}
	}

	/**
	 * 取消注册传入key对应的所有观察者
	 */
	@Override
	public void unregisterAll()
	{
		if(null == mObservers && mObservers.size() > 0)
		{
			String key = mObservers.get(0).getKey();
			super.unregisterAll();
			mOnObserverChangeListener.onObserverClear(key);
		}
	}

	/**
	 * 
	 * 该方法的作用: 通知观察者
	 * 
	 * @date 2014年3月31日
	 */
	public void notifyChange(Object result)
	{
		TGDownloader downloader = (TGDownloader)result;
		switch(downloader.getDownloadStatus())
		{
			case TGDownloader.DOWNLOAD_FAILED:
				LogTools.i(TAG,"[Method:notifyChange], Status:DOWNLOAD_FAILED ;" + "observer size: " + mObservers.size());
				for (TGDownloadObserver observer : mObservers)
				{
					if(observer != null)
					{
						observer.onFailed(downloader, downloader.getErrorCode(), downloader.getErrorMsg());
					}
				}
				unregisterAll();
				break;
			case TGDownloader.DOWNLOAD_SUCCEED:
				LogTools.i(TAG,"[Method:notifyChange], Status:DOWNLOAD_SUCCEED ;" + "observer size: " + mObservers.size());
				for (TGDownloadObserver observer : mObservers)
				{
					if(observer != null)
					{
						observer.onSuccess(downloader);
					}
				}
				unregisterAll();
				break;
			case TGDownloader.DOWNLOAD_DOWNLOADING:
				LogTools.i(TAG,"[Method:notifyChange], Status:DOWNLOAD_DOWNLOADING ;" + "observer size: " + mObservers.size());
				for (TGDownloadObserver observer : mObservers)
				{
					if(observer != null && downloader.getFileSize() > 0)
					{
    					int progress = (int) (downloader.getCompleteSize() * 100 / downloader.getFileSize());
    					observer.onProgress(downloader, progress);
					}
				}
				break;
			case TGDownloader.DOWNLOAD_PAUSE:
				LogTools.i(TAG,"[Method:notifyChange], Status:DOWNLOAD_STOP ;" + "observer size: " + mObservers.size());
				for (TGDownloadObserver observer : mObservers)
				{
					if(observer != null)
					{
						observer.onPause(downloader);
					}
				}
				unregisterAll();
				break;
			case TGDownloader.DOWNLOAD_STARTING:
				LogTools.i(TAG,"[Method:notifyChange], Status:DOWNLOAD_STARTING ;" + "observer size: " + mObservers.size());
				for (TGDownloadObserver observer : mObservers)
				{
					if(observer != null)
					{
						observer.onStart(downloader);
					}
				}
				break;
				
			case TGDownloader.DOWNLOAD_CANCEL:
				LogTools.i(TAG,"[Method:notifyChange], Status:DOWNLOAD_CANCEL ;" + "observer size: " + mObservers.size());
				for (TGDownloadObserver observer : mObservers)
				{
					if(observer != null)
					{
						observer.onCancel(downloader);
					}
				}

			default:
				break;
		}
	}

	/**
	 * 
	 * 该方法的作用: 判断该observer是否已经注册过，已经注册返回true，否则返回false
	 * 
	 * @date 2014年3月31日
	 * @param observer
	 * @return
	 */
	public boolean isExistObserver(TGDownloadObserver observer)
	{
		for (TGDownloadObserver mObserver : mObservers)
		{
			if (mObserver.getKey().equals(observer.getKey()))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * 观察者集合是否被清空监听
	 */
	private OnObserverChangeListener mOnObserverChangeListener;

	/**
	 * 
	 * 该类作用及功能说明:
	 * 观察者集合是否被清空监听，当mObservers被清空时，回调onObserverClear方法，把该集合从观察者控制类中移除
	 * 
	 * @date 2014年3月31日
	 */
	public interface OnObserverChangeListener
	{

		/**
		 * 
		 * 该方法的作用: 清空观察者集合回调方法.
		 * 
		 * @date 2014年3月31日
		 * @param uri
		 *            观察者对应key
		 */
		void onObserverClear(String key);
	}

	/**
	 * 
	 * 该方法的作用: 设置观察者变化监听
	 * 
	 * @date 2014年3月31日
	 * @param l
	 */
	public void setOnObserverChangeListener(OnObserverChangeListener l)
	{
		mOnObserverChangeListener = l;
	}
}
