package com.mn.tiger.task.queue;

import com.mn.tiger.task.thread.TGFixedThreadPool;

public class TGDownloadTaskQueue extends TGTaskQueue
{
	private static final long serialVersionUID = 1L;

	public TGDownloadTaskQueue()
	{
		super();
		setThreadPool(new TGFixedThreadPool(getMAX_THREAD_NUM()));
	}
	
	@Override
	public int getMAX_THREAD_NUM()
	{
		return 5;
	}
	
	@Override
	public synchronized boolean pauseTask(int taskId)
	{
		//TODO 
		return super.pauseTask(taskId);
	}
}
