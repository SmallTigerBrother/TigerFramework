package com.mn.tiger.task.queue;

import com.mn.tiger.task.thread.TGFixedThreadPool;

public class TGHttpTaskQueue extends TGTaskQueue
{
	private static final long serialVersionUID = 1L;

	public TGHttpTaskQueue()
	{
		super();
		setThreadPool(new TGFixedThreadPool(getMAX_THREAD_NUM()));
	}
	
	@Override
	public int getMAX_THREAD_NUM()
	{
		return 256;
	}
}
