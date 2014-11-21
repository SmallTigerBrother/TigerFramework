package com.mn.tiger.request.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.mn.tiger.log.LogTools;

/**
 * 该类作用及功能说明 
 * 线程池(创建可比较的线程)
 * 
 * @date 2014年4月14日
 */
public class TGThreadPoolExecutor extends ThreadPoolExecutor
{
	private final String TAG = this.getClass().getSimpleName();
	
	/**
	 * @see ThreadPoolExecutor#ThreadPoolExecutor(int, int, long, TimeUnit,
	 *      BlockingQueue)
	 */
	public TGThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
			TimeUnit unit, BlockingQueue<Runnable> workQueue)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	/**
	 * @see ThreadPoolExecutor#ThreadPoolExecutor(int, int, long, TimeUnit,
	 *      BlockingQueue, RejectedExecutionHandler)
	 */
	public TGThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
			TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
	}

	/**
	 * @see ThreadPoolExecutor#ThreadPoolExecutor(int, int, long, TimeUnit,
	 *      BlockingQueue, ThreadFactory)
	 */
	public TGThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
			TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
	}

	/**
	 * @see ThreadPoolExecutor#ThreadPoolExecutor(int, int, long, TimeUnit,
	 *      BlockingQueue, ThreadFactory, RejectedExecutionHandler)
	 */
	public TGThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
			TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
			RejectedExecutionHandler handler)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
	}

	@Override
	protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value)
	{
		return new ComparableFutureTask<T>(runnable, value);
	}

	@Override
	protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable)
	{
		return new ComparableFutureTask<T>(callable);
	}

	@Override
	public void execute(Runnable command)
	{
		super.execute(command);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task)
	{
		return super.submit(task);
	}
	
	//执行顺序:beforeExecute ->execute ->afterExecute
	@Override
	protected void beforeExecute(Thread t, Runnable r)
	{	
		super.beforeExecute(t, r);
	}
	
	@Override
	protected void afterExecute(Runnable r, Throwable t)
	{
		super.afterExecute(r, t);
		//因线程池会把所有的异常都给吃掉，在get的时候，才能获取到异常，因而捕获异常，并打印异常信息
		if(t == null && r instanceof Future<?>)
    	{
    		try{
    			((Future<?>)r).get();
    		}
    		catch(CancellationException ce)
    		{
    			t = ce;
    		}
    		catch (ExecutionException ee) {
    			t = ee.getCause();
			}
    		catch (InterruptedException ie) {
    			Thread.currentThread().interrupt();
			}
    	}
    	if(t != null)
    	{
    		LogTools.e(TAG, t.getMessage(),t);
    	}
	}

	/**
	 * 该类作用及功能说明 实现了Comparable接口的FutureTask </br>
	 * newTaskFor必须创建实现了Comparable接口的FutureTask对象，不然会报异常
	 * 
	 * @date 2014年4月14日
	 */
	protected class ComparableFutureTask<V> extends FutureTask<V> implements
			Comparable<ComparableFutureTask<V>>
	{
		private Object object;

		public ComparableFutureTask(Callable<V> callable)
		{
			super(callable);
			object = callable;
		}

		public ComparableFutureTask(Runnable runnable, V result)
		{
			super(runnable, result);
			object = runnable;
		}

		@Override
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public int compareTo(ComparableFutureTask<V> o)
		{
			if (this == o)
			{
				return 0;
			}
			if (o == null)
			{
				return -1; // high priority
			}
			if (object != null && o.object != null)
			{
				if (object.getClass().equals(o.object.getClass()))
				{
					if (object instanceof Comparable)
					{
						// 调用实现了Comparable接口的runnable的compareTo方法
						return ((Comparable) object).compareTo(o.object);
					}
					else{
						LogTools.p(TAG, "[Method:compareTo]  This FutureTask not implements Comparable...");
					}
				}
			}
			return 0;
		}
	}
}