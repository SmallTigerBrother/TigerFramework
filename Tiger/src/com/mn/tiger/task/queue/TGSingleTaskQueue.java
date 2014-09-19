package com.mn.tiger.task.queue;

import android.content.Context;

import com.mn.tiger.task.ITaskListener;
import com.mn.tiger.task.TGTask;
import com.mn.tiger.task.thread.TGThreadPool;
import com.mn.tiger.utility.LogTools;


/**
 * 该类作用及功能说明
 * 独立任务队列
 * @date 2014年8月22日
 */
public class TGSingleTaskQueue extends TGTaskQueue
{
	/**
	 * @date 2014年8月22日
	 */
	private static final long serialVersionUID = 1L;
	
	private TGTask singleTask = null;
	
	public TGSingleTaskQueue(Context context, int type)
	{
		super(context, type);
		setThreadPool(new TGThreadPool(1));
		setTaskListener(new DefaultTaskListener());
	}
	
	@Override
	protected void sortTaskQueue()
	{
		
	}
	
	@Override
	public synchronized void addLast(TGTask task)
	{
		this.singleTask = task;
		this.singleTask.setTaskListener(taskListener);
	}
	
	@Override
	public synchronized boolean remove(Object object)
	{
		this.singleTask = null;
		return true;
	}

	@Override
	public void executeNextTask()
	{
		if(null != singleTask)
		{
			singleTask.executeTask(getThreadPool());
		}
	}
	
	@Override
	public void pause()
	{
		if(null != singleTask)
		{
			this.singleTask.cancel();
			this.singleTask = null;
		}
	}
	
	@Override
	public synchronized boolean remove(Integer taskId)
	{
		if(null != singleTask)
		{
			this.singleTask.destory();
			this.singleTask = null;
		}
		
		return true;
	}
	
	@Override
	public synchronized void restart()
	{
		
	}
	
	@Override
	public TGTask getTask(int taskId)
	{
		return null;
	}
	
	/**
	 * 该类作用及功能说明: 默认任务队列监听
	 * 
	 * @date 2014年6月25日
	 */
	public class DefaultTaskListener implements ITaskListener
	{
		@Override
		public synchronized void onTaskStart()
		{
			LogTools.d(LOG_TAG, "[Method:DefaultTaskListener->onTaskStart]");
		}

		@Override
		public synchronized void onTaskChanged(int progress)
		{
			LogTools.d(LOG_TAG, "[Method:DefaultTaskListener->onTaskChanged] progress: " + progress);
		}

		@Override
		public synchronized void onTaskFinished(int taskId)
		{
			LogTools.d(LOG_TAG, "[Method:DefaultTaskListener->onTaskFinished] taskId: " + taskId);
			 singleTask = null;
		}

		@Override
		public synchronized void onTaskError(int taskId, int code, Object msg)
		{
			LogTools.p(LOG_TAG, "[Method:DefaultTaskListener->onTaskError] taskId: " + taskId + "; errorCode: "+ code);
		    singleTask = null;
		}

		@Override
		public synchronized void onTaskCancel(int taskId)
		{
			LogTools.d(LOG_TAG, "[Method:DefaultTaskListener->onTaskCancel] taskId: " + taskId);
			singleTask = null;
		}
		
		@Override
		public void onTaskPause(int taskId)
		{
		}
		
		@Override
		public void onTaskStop(int taskId)
		{
		}
	}
}
