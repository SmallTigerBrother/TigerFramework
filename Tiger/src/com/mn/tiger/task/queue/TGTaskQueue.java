package com.mn.tiger.task.queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;

import com.mn.tiger.task.ITaskListener;
import com.mn.tiger.task.TGTask;
import com.mn.tiger.task.TGTask.MPTaskState;
import com.mn.tiger.task.TGTask.TaskError;
import com.mn.tiger.task.dispatch.TGDispatcher;
import com.mn.tiger.task.dispatch.TGLock.onLockListener;
import com.mn.tiger.task.dispatch.TGLock.onUnLockListener;
import com.mn.tiger.task.thread.TGThreadPool;
import com.mn.tiger.utility.LogTools;

/**
 * 
 * 该类作用及功能说明: 可并行执行多个任务的队列
 * 
 * @date 2014年6月25日
 */
public class TGTaskQueue extends AbsTaskQueue
{
	/**
	 * @date 2014年6月25日
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 队列类型（和队列中所存储任务类型相同）
	 */
	private int type;
	
	/**
	 * 该队列中最大支持并行线程数
	 */
	private int MAX_THREAD_NUM = 3;
	
	/**
	 * 该队列中正在运行任务个数
	 */
	protected List<Integer> runningTaskList = new ArrayList<Integer>();
	
	/**
	 * 队列状态
	 */
	private MPQueueState state = MPQueueState.WAITING;
	
	/**
	 * 任务监听
	 */
	protected ITaskListener taskListener;
	
	/**
	 * 执行该队列任务的线程池
	 */
	private TGThreadPool threadPool;
	
	private Context context;
	
	/**
	 * 构造函数
	 * @date 2014年6月25日
	 */
	public TGTaskQueue(Context context, int type)
	{
		super();
		this.context = context;
		this.type = type;
		threadPool = new TGThreadPool(Pool.getPoolSizeByType(type));
	}
	
	@Override
	public synchronized void executeNextTask()
	{
		LogTools.d(LOG_TAG, "[Method:executeNextTask] task type: " + type);

		//若当前队列状态为暂停，则直接退出
		if(state == MPQueueState.PAUSE)
		{
			LogTools.p(LOG_TAG, "[Method:executeNextTask] the queue has been paused!");
			return;
		}
		
		// Task按运行状态和weight排序
		sortTaskQueue();
		
		// 获取队列中的任务总数
		int totalTask = getTaskArray().size();
		LogTools.d(LOG_TAG, "[Method:executeNextTask] task"+ type + " count: " + totalTask);
		// 若已无任务，队列状态改为等待
		if(totalTask <= 0)
		{
			state = MPQueueState.WAITING;
		}
		
		// 队列中有任务，取最大个数等待任务执行
		boolean loop = true;
		int index = 0;
		
		do
		{
			LogTools.d(LOG_TAG, "[Method:executeNextTask] runningTaskNum: " + runningTaskList.size());
			if(totalTask > index && runningTaskList.size() <= getMAX_THREAD_NUM())
			{
				TGTask runTask = getTaskArray().get(this.get(index));
				if (runTask != null && runTask.getTaskState() == MPTaskState.WAITING)
				{
					runTask.executeTask(threadPool);
					runningTaskList.add(runTask.getTaskID());
					state = MPQueueState.RUNNING;
				}
			}
			else
			{
				loop = false;
			}
			
			index++;
			
		}while(loop);
	}
	
	@Override
	protected void sortTaskQueue()
	{
		LogTools.d(LOG_TAG, "[Method:sortTaskQueue]");
		
		if (this.size() > 1)
		{
			// 先根据执行时间排序：时间越靠前，越先执行
			Collections.sort(this, new Comparator<Integer>()
			{
				@Override
				public int compare(Integer taskID_1, Integer taskID_2)
				{
					TGTask task_1 = getTaskArray().get(taskID_1);
					TGTask task_2 = getTaskArray().get(taskID_2);
					if(null != task_1 && null != task_2 && task_1.getExecutionTime() >= task_2.getExecutionTime())
					{
						// 执行时间越大，优先级越低
						return -1;
					}
					
					return 1;
				}
			});
			
			// 再根据权重排序：权重越大，越先执行
			Collections.sort(this, new Comparator<Integer>()
			{
				@Override
				public int compare(Integer taskID_1, Integer taskID_2)
				{
					TGTask task_1 = getTaskArray().get(taskID_1);
					TGTask task_2 = getTaskArray().get(taskID_2);
					if(null != task_1 && null != task_2 && task_1.getWeight() >= task_2.getWeight())
					{
						// 权重越大，优先级越高
						return 1;
					}
					
					return -1;
				}
			});
		}
	}
	
	@Override
	public synchronized void addLast(TGTask task)
	{
		task.setTaskListener(getTaskListener());
		super.addLast(task);
	}
	
	/**
	 * 该方法的作用: 移除任务
	 * @date 2014年6月25日
	 */
	public synchronized boolean remove(Integer taskId)
	{
		// 获取被移除任务
		TGTask task = getTaskArray().get(taskId.intValue());
		if(task != null)
		{
			// 如果是正在运行的任务，队列中正在运行任务减1
			if(runningTaskList.size() > 0 && runningTaskList.contains(taskId))
			{
				runningTaskList.remove(taskId);
			}
    		//销毁任务
			task.destory();
			task = null;
		}
		
		// 任务移除队列
		boolean result =  super.remove(taskId);
		
		// 执行队列中下一个等待任务
		LogTools.d(LOG_TAG, "[Method:onRemove] queue size : " + TGTaskQueue.this.size());
		this.executeNextTask();
		
		return result;
	}

	/**
	 * 该方法的作用: 暂停任务队列
	 * 
	 * @date 2014年3月17日
	 */
	public synchronized void pause()
	{
		LogTools.d(LOG_TAG, "[Method:pause]");
		//设置队列运行状态为PAUSE
		state = MPQueueState.PAUSE;
		
		//将当前运行任务的Clone对象放入队列中
		TGTask task = null;
		for(int i = 0; i < runningTaskList.size(); i++)
		{
			// 克隆当前运行任务，重新加入到队列头
			task = getTaskArray().get(runningTaskList.get(i));
			if(null != task)
			{
				// 将当前运行的任务暂停
				task.pause();
			}
		}
		
		runningTaskList.clear();
	}
	
	/**
	 * 该方法的作用: 重启任务队列
	 * 
	 * @date 2014年3月17日
	 */
	public synchronized void restart()
	{
		LogTools.d(LOG_TAG, "[Method:restart]");

		//设置队列运行状态为WAITING
		state = MPQueueState.WAITING;
		//执行下一个任务
		executeNextTask();
	}
	
	/**
	 * 该方法的作用:
	 * 取消所有任务
	 * @date 2014年9月4日
	 */
	public void cancelAllTasks()
	{
		super.cancelAllTasks();
		//修改队列状态
		state = MPQueueState.WAITING;
	}
	
	/**
	 * 该方法的作用:
	 * 获取队列执行的任务类型
	 * @date 2014年8月22日
	 * @return
	 */
	public int getType()
	{
		return type;
	}

	/**
	 * 该方法的作用:
	 * 设置队列执行的任务类型
	 * @date 2014年8月22日
	 * @param type
	 */
	public void setType(int type)
	{
		this.type = type;
	}
	
	/**
	 * 该方法的作用:
	 * 获取最大并行任务数
	 * @date 2014年8月22日
	 * @return
	 */
	public int getMAX_THREAD_NUM()
	{
		return MAX_THREAD_NUM;
	}

	/**
	 * 该方法的作用:
	 * 设置最大并行任务数
	 * @date 2014年8月22日
	 * @param mAX_THREAD_NUM
	 */
	public void setMAX_THREAD_NUM(int mAX_THREAD_NUM)
	{
		MAX_THREAD_NUM = mAX_THREAD_NUM;
	}

	/**
	 * 该方法的作用:
	 * 获取任务状态
	 * @date 2014年8月22日
	 * @return
	 */
	public MPQueueState getState()
	{
		return state;
	}

	/**
	 * 该方法的作用:
	 * 设置任务状态
	 * @date 2014年8月22日
	 * @param state
	 */
	public void setState(MPQueueState state)
	{
		this.state = state;
	}

	/**
	 * 该方法的作用:
	 * 设置任务监听接口
	 * @date 2014年8月22日
	 * @param taskListener
	 */
	public void setTaskListener(ITaskListener taskListener)
	{
		this.taskListener = taskListener;
	}
	
	/**
	 * 该方法的作用:
	 * 获取线程池
	 * @date 2014年8月22日
	 * @return
	 */
	public TGThreadPool getThreadPool()
	{
		return threadPool;
	}

	/**
	 * 该方法的作用:
	 * 设置线程池
	 * @date 2014年8月22日
	 * @param threadPool
	 */
	public void setThreadPool(TGThreadPool threadPool)
	{
		this.threadPool = threadPool;
	}

	/**
	 * 该方法的作用: 获取默认任务监听
	 * @date 2014年6月25日
	 * @return
	 */
	public ITaskListener getTaskListener()
	{
		if(taskListener == null)
		{
			return taskListener = new DefaultTaskListener();
		}
		return taskListener;
	}
	
	/**
	 * 
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
			TGTaskQueue.this.remove((Integer)taskId);
		}

		@Override
		public synchronized void onTaskError(int taskId, int code, Object msg)
		{
			LogTools.p(LOG_TAG, "[Method:DefaultTaskListener->onTaskError] taskId: " + taskId + "; errorCode: " + code);
			switch (code)
			{
				case TaskError.LOCK_DISPATER_CODE:
					
					//若当前状态不为暂停状态，则立即暂停所有任务
					if(state != MPQueueState.PAUSE)
					{
						//先上锁
						TGDispatcher.getInstance(context).lock(new onLockListener()
						{
							@Override
							public void onLockSuccess()
							{
								TGDispatcher.getInstance(context).pauseAllTaskQueues();
							}
							
							@Override
							public void onLockFailed()
							{
							}
						});
						
						TGDispatcher.getInstance(context).unLock(new onUnLockListener()
						{
							@Override
							public void onUnLockSuccess()
							{
								TGDispatcher.getInstance(context).executeAllTaskQueues();
							}
							
							@Override
							public void onUnLockFailed()
							{
							}
						});
					}
					else
					{
						//当前队列为暂停状态，暂停当前任务
						TGTask task = TGTaskQueue.this.getTask(taskId);
						if(null != task)
						{
							task.pause();
						}
					}
					
					break;

				default:
					TGTaskQueue.this.remove((Integer)taskId);
					break;
			}
		}

		@Override
		public synchronized void onTaskCancel(int taskId)
		{
			LogTools.d(LOG_TAG, "[Method:DefaultTaskListener->onTaskCancel] taskId: " + taskId);
			TGTaskQueue.this.remove((Integer)taskId);
		}
		
		@Override
		public void onTaskPause(int taskId)
		{
			TGTask cloneTask = null;
			try
			{
				cloneTask = (TGTask) getTaskArray().get(taskId).clone();
				TGTaskQueue.this.remove((Integer)taskId);
			}
			catch (CloneNotSupportedException e)
			{
				LogTools.e(LOG_TAG, e.getMessage(), e);
			}
			
			if(null != cloneTask)
			{
				//克隆一个对象，加入到队列中
				TGTaskQueue.this.addLast(cloneTask);
			}
		}

		@Override
		public void onTaskStop(int taskId)
		{
			TGTaskQueue.this.remove((Integer)taskId);
		}
	}
}
