package com.mn.tiger.task.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 该类作用及功能说明
 * 线程池
 * @author l00220455
 * @date 2014年8月23日
 */
public class TGThreadPool
{
	private ExecutorService executorService;

	/**
	 * 最大线程数
	 */
	private int poolSize;

	/**
	 * 默认线程数
	 */
	private static final int DEFAULT_POOL_SIZE = 64;

	public TGThreadPool(int poolSize)
	{
		init(poolSize);
	}
	
	/**
	 * 该方法的作用:
	 * 初始化方法
	 * @author l00220455
	 * @date 2014年8月23日
	 * @param poolSize
	 */
	public void init(int poolSize)
	{
		this.poolSize = poolSize;
		if (this.poolSize <= 0)
		{
			this.poolSize = DEFAULT_POOL_SIZE;
		}
		executorService = Executors.newFixedThreadPool(this.poolSize);
	}

	/**
	 * 该方法的作用:
	 * 执行线程
	 * @author l00220455
	 * @date 2014年8月23日
	 * @param runnale
	 */
	public void execute(Runnable runnale)
	{
		createExecutor();
		executorService.execute(runnale);
	}

	/**
	 * 该方法的作用:
	 * 销毁
	 * @author l00220455
	 * @date 2014年8月23日
	 */
	public void destroy()
	{
		createExecutor();
		executorService.shutdownNow();
	}
	
	/**
	 * 该方法的作用:
	 * 创建Excecutor对象
	 * @author l00220455
	 * @date 2014年8月23日
	 */
	private void createExecutor()
	{
		if (executorService != null)
		{
			return;
		}

		init(poolSize);
	}
}
