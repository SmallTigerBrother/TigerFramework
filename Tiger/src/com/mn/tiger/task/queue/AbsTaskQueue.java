package com.mn.tiger.task.queue;

import java.util.LinkedList;

import android.util.SparseArray;

import com.mn.tiger.task.TGTask;

/**
 * 
 * 该类作用及功能说明: 任务队列
 * 
 * @date 2014年6月25日
 */
public abstract class AbsTaskQueue extends LinkedList<Integer>
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * @date 2014年6月25日
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 任务容器
	 */
	private SparseArray<TGTask> taskArray;
	
	/**
	 * 构造函数
	 * @date 2014年6月25日
	 */
	public AbsTaskQueue()
	{
		taskArray = new SparseArray<TGTask>();
	}

	/**
	 * 
	 * 该方法的作用: 排序队列中的任务
	 * @date 2014年6月25日
	 */
	protected abstract void sortTaskQueue();
	
	/**
	 * 
	 * 该方法的作用: 执行下一个任务
	 * @date 2014年6月25日
	 */
	public abstract void executeNextTask();
	
	/**
	 * 
	 * 该方法的作用: 移除任务
	 * @date 2014年6月25日
	 */
	public synchronized boolean remove(Object object)
	{
		// 从队列中移除任务
		getTaskArray().remove((Integer) object);
		return super.remove(object);
	}
	
	/**
	 * 该方法的作用:
	 * 将任务添加到队列中
	 * @date 2014年5月6日
	 * @param task
	 */
	public synchronized void addLast(TGTask task)
	{
		this.addLast(task.getTaskID());
		getTaskArray().put(task.getTaskID(), task);
	}
	
	/**
	 * 该方法的作用: 暂停任务队列
	 * 
	 * @date 2014年3月17日
	 */
	public void pause()
	{
		
	}
	
	/**
	 * 该方法的作用: 重启任务队列
	 * 
	 * @date 2014年3月17日
	 */
	public void restart()
	{
		
	}
	
	/**
	 * 该方法的作用:
	 * 取消所有任务
	 * @date 2014年9月4日
	 */
	public void cancelAllTasks()
	{
		//删除所有任务
		while (this.size() > 0)
		{
			this.getTask(this.getFirst()).cancel();
		}
	}
	
	@Override
	public void clear()
	{
		super.clear();
		getTaskArray().clear();
	}
	
	/**
	 * 该方法的作用: 获取队列权重
	 * @date 2014年3月17日
	 * @return
	 */
	public synchronized int getWeight()
	{
		int weight = 0;
		int size = size();
		for (int i = 0; i < size; i++)
		{
			weight = weight + getTaskArray().get(this.get(i)).getWeight();
		}

		return weight;
	}
	
	/**
	 * 该方法的作用:
	 * 根据ID获取任务
	 * @date 2014年8月22日
	 * @param taskId
	 * @return
	 */
	public TGTask getTask(int taskId)
	{
		return getTaskArray().get(taskId);
	}
	
	/**
	 * 该方法的作用:
	 * 获取任务列表
	 * @date 2014年8月22日
	 * @return
	 */
	public SparseArray<TGTask> getTaskArray()
	{
		if(null == taskArray)
		{
			taskArray = new SparseArray<TGTask>();
		}
		return taskArray;
	}

	/**
	 * 该方法的作用:
	 * 设置任务列表
	 * @date 2014年8月22日
	 * @param taskArray
	 */
	public void setTaskArray(SparseArray<TGTask> taskArray)
	{
		this.taskArray = taskArray;
	}

	/**
	 * 该类作用及功能说明
	 * 队列状态
	 * @date 2014年3月17日
	 */
	public enum MPQueueState
	{
		/**
		 * 正在等待
		 */
		WAITING,
		/**
		 * 正在运行
		 */
		RUNNING,
		/**
		 * 暂停
		 */
		PAUSE
	}
	
	/**
	 * 
	 * 该类作用及功能说明: 定义各种类型任务线程池并发线程数
	 * 
	 * @date 2014年8月12日
	 */
	protected enum Pool
	{
		HTTP(TGTask.TASK_TYPE_HTTP, 128), UPLOAD(TGTask.TASK_TYPE_UPLOAD, 3), DOWNLOAD(
				TGTask.TASK_TYPE_DOWNLOAD, 3);
		// 成员变量
		private int taskType;
		private int poolSize;

		// 构造方法
		private Pool(int type, int size)
		{
			this.taskType = type;
			this.poolSize = size;
		}

		/**
		 * 根据type获取线程池大小
		 */
		public static int getPoolSizeByType(int type)
		{
			int poolSize = 0;
			for (Pool p : Pool.values())
			{
				if (p.taskType == type)
				{
					poolSize = p.poolSize;
					break;
				}
			}
			return poolSize;
		}
	}
}
