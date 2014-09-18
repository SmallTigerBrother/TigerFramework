package com.mn.tiger.request.threadpool;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 该类作用及功能说明
 * 带缓存、优先级控制的线程池（根据提供的键值，缓存线程）
 * 
 * @author yWX158243
 * @date 2014年4月17日
 */
public class TGPriorityThreadPool extends TGThreadPoolExecutor
{
	/** 缓存提交到线程池中的线程 */
	protected ConcurrentHashMap<String, Object> runnableCache = new ConcurrentHashMap<String, Object>();

	/**
	 * @see TGThreadPoolExecutor#ThreadPoolExecutor(int, int, long, TimeUnit,
	 *      BlockingQueue)
	 */
	public TGPriorityThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
			TimeUnit unit)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit,
				new PriorityBlockingQueue<Runnable>());
	}

	/**
	 * @see TGThreadPoolExecutor#ThreadPoolExecutor(int, int, long, TimeUnit,
	 *      BlockingQueue, RejectedExecutionHandler)
	 */
	public TGPriorityThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
			TimeUnit unit, RejectedExecutionHandler handler)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit,
				new PriorityBlockingQueue<Runnable>(), handler);
	}

	/**
	 * @see TGThreadPoolExecutor#ThreadPoolExecutor(int, int, long, TimeUnit,
	 *      ThreadFactory)
	 */
	public TGPriorityThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
			TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit,
				new PriorityBlockingQueue<Runnable>(), threadFactory);
	}

	/**
	 * @see TGThreadPoolExecutor#ThreadPoolExecutor(int, int, long, TimeUnit,
	 *      BlockingQueue, ThreadFactory, RejectedExecutionHandler)
	 */
	public TGPriorityThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime,
			TimeUnit unit, ThreadFactory threadFactory, RejectedExecutionHandler handler)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit,
				new PriorityBlockingQueue<Runnable>(), threadFactory, handler);
	}

	/**
	 * 该方法的作用:提交线程到线程池
	 * 
	 * @author yWX158243
	 * @date 2014年4月17日
	 * @param task
	 * @param key
	 *            缓存线程的键，确保其唯一性
	 * @return
	 */
	public <T> Future<T> submit(Callable<T> task, String key)
	{
		if (null != runnableCache)
		{
			runnableCache.put(key, task);
		}
		return super.submit(task);
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t)
	{
		super.afterExecute(r, t);
		//删除缓存中的线程
		if (null != runnableCache && null == t)
		{
			if (runnableCache.containsValue(r))
			{
				Iterator<Map.Entry<String, Object>> itors = runnableCache.entrySet().iterator();
				while (itors.hasNext())
				{
					Map.Entry<String, Object> entry = itors.next();
					if (entry.getValue() == r)
					{
						itors.remove();
						break;
					}
				}
			}
		}
	}

	/**
	 * 该方法的作用:获取线程
	 * 
	 * @author yWX158243
	 * @date 2014年4月17日
	 * @param key
	 * @return
	 */
	public Object getRunnable(String key)
	{
		if (null != runnableCache)
		{
			return runnableCache.get(key);
		}
		return null;
	}
	
	/**
	 * 该方法的作用:销毁线程池
	 * @author yWX158243
	 * @date 2014年4月21日
	 */
	public void destory(){
		shutdownNow();
	}
}
