package com.mn.tiger.task.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TGScheduleThreadPool extends TGThreadPool
{
	private ExecutorService executorService;

	public TGScheduleThreadPool()
	{
		executorService = Executors.newCachedThreadPool();
	}
	
	/**
	 * 该方法的作用:
	 * 执行线程
	 * @date 2014年8月23日
	 * @param runnale
	 */
	public void execute(Runnable runnale)
	{
		executorService.execute(runnale);
	}

	/**
	 * 该方法的作用:
	 * 销毁
	 * @date 2014年8月23日
	 */
	public void destroy()
	{
		executorService.shutdownNow();
	}
}
