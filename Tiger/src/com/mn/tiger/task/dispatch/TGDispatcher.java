package com.mn.tiger.task.dispatch;

import android.content.Context;
import android.util.SparseArray;

import com.mn.tiger.task.TGTask;
import com.mn.tiger.task.dispatch.TGLock.onLockListener;
import com.mn.tiger.task.dispatch.TGLock.onUnLockListener;
import com.mn.tiger.task.queue.TGSingleTaskQueue;
import com.mn.tiger.task.queue.TGTaskQueue;
import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明 任务分发管理器
 * 
 * @author l00220455
 * @date 2014年3月17日
 */
public class TGDispatcher
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();

	/**
	 * 任务队列数组
	 */
	private SparseArray<TGTaskQueue> taskQueues;
	
	/**
	 * 分发器单例对象
	 */
	private static TGDispatcher dispatcher;
	
	/**
	 * 任务分发管理锁
	 */
	private TGLock lock;
	
	/**
	 * 上下文信息
	 */
	private Context context;

	/**
	 * 该方法的作用: 获取单例对象
	 * 
	 * @author l00220455
	 * @date 2014年3月17日
	 * @return
	 */
	public synchronized static TGDispatcher getInstance(Context context)
	{
		if (null == dispatcher)
		{
			dispatcher = new TGDispatcher(context);
		}

		dispatcher.context = context;
		
		return dispatcher;
	}

	/**
	 * 构造方法
	 * 
	 * @author l00220455
	 * @date 2014年3月17日
	 */
	private TGDispatcher(Context context)
	{
		this.context = context;
	}

	/**
	 * 该方法的作用: 分配并执行任务
	 * 
	 * @author l00220455
	 * @date 2014年3月17日
	 * @param task
	 */
	public void dispatchAndExecuteTask(TGTask task)
	{
		LogTools.p(LOG_TAG, "[Method:dispatchAndExecuteTask]");
		
		// 缓存中查找该任务应该加入的队列
		TGTaskQueue taskQueue = getTaskQueue(task.getType());
		
		// 如果任务在队列中不存在，将任务加入队列
		if(taskQueue.getTask(task.getTaskID()) == null){
    		// 将任务添加到末尾
    		taskQueue.addLast(task);
		}

		// 执行下一个任务
		taskQueue.executeNextTask();
	}
	
	/**
	 * 该方法的作用:
	 * 根据任务类型获取TaskQueue
	 * @author l00220455
	 * @date 2014年8月15日
	 * @param taskType
	 * @return
	 */
	protected TGTaskQueue getTaskQueue(int taskType)
	{
		if(getTaskQueues().indexOfKey(taskType) >= 0)
		{
			// 已有队列，直接使用
			return getTaskQueues().get(taskType);
		}
		else
		{
			// 队列不存在，创建新队列，并把队列加入队列集合
			TGTaskQueue taskQueue = null;
			
			switch (taskType)
			{
				case TGTask.TASK_TYPE_HTTP:
					taskQueue = new TGTaskQueue(context, taskType);
					taskQueue.setMAX_THREAD_NUM(128);
					break;
					
				case TGTask.TASK_TYPE_UPLOAD:
					taskQueue = new TGTaskQueue(context, taskType);
					taskQueue.setMAX_THREAD_NUM(3);
					break;
					
				case TGTask.TASK_TYPE_DOWNLOAD:
					taskQueue = new TGTaskQueue(context, taskType);
					taskQueue.setMAX_THREAD_NUM(3);
					break;
					
				case TGTask.TASK_TYPE_LOGIN:
					//登录任务使用独立任务队列
					taskQueue = new TGSingleTaskQueue(context, taskType);
					break;
					
				default:
					taskQueue = new TGTaskQueue(context, taskType);
					taskQueue.setMAX_THREAD_NUM(128);
					break;
			}
			
			taskQueue.setType(taskType);
			getTaskQueues().append(taskType, taskQueue);
			
			return taskQueue;
		}
	}
	
	/**
	 * 该方法的作用:
	 * 获取任务队列列表
	 * @author l00220455
	 * @date 2014年8月22日
	 * @return
	 */
	private SparseArray<TGTaskQueue> getTaskQueues()
	{
		if (taskQueues == null)
		{
			taskQueues = new SparseArray<TGTaskQueue>();
		}
		
		return taskQueues;
	}
	
	/**
	 * 该方法的作用: 执行所有任务队列
	 * @author l00220455
	 * @date 2014年3月17日
	 */
	public void executeAllTaskQueues()
	{
		LogTools.p(LOG_TAG, "[Method:executeAllTaskQueues]");
		TGTaskQueue taskQueue = null;
		for (int i = 0; i < getTaskQueues().size(); i++)
		{
			taskQueue = getTaskQueues().valueAt(i);
			taskQueue.restart();
		}
	}

	/**
	 * 该方法的作用: 暂停所有任务队列
	 * 
	 * @author l00220455
	 * @date 2014年3月17日
	 */
	public void pauseAllTaskQueues()
	{
		LogTools.p(LOG_TAG, "[Method:pauseAllTaskQueues]");
		TGTaskQueue taskQueue = null;
		for (int i = 0; i < getTaskQueues().size(); i++)
		{
			taskQueue = getTaskQueues().valueAt(i);
			taskQueue.pause();
		}
	}
	
	/**
	 * 该方法的作用:
	 * 根据任务类型暂停任务队列
	 * @author l00220455
	 * @date 2014年9月4日
	 * @param taskType
	 */
	public void pauseTaskQueue(int taskType)
	{
		LogTools.p(LOG_TAG, "[Method:pauseTaskQueue]");
		TGTaskQueue taskQueue = getTaskQueues().get(taskType);
		
		if(null != taskQueue)
		{
			taskQueue.pause();
		}
	}
	
	/**
	 * 该方法的作用:
	 * @author l00220455
	 * @date 2014年9月4日
	 */
	public void cancelAllTasks()
	{
		LogTools.p(LOG_TAG, "[Method:cancelAllTasks]");
		TGTaskQueue taskQueue = null;
		for (int i = 0; i < getTaskQueues().size(); i++)
		{
			taskQueue = getTaskQueues().valueAt(i);
			if(null != taskQueue)
			{
				taskQueue.cancelAllTasks();
			}
		}
	}
	
	
	
	/**
	 * 该方法的作用:
	 * 取消任务
	 * @author l00220455
	 * @date 2014年3月20日
	 * @param task
	 * @return
	 */
	public boolean cancelTask(int taskId, int taskType)
	{
		if(taskId < 0 || taskType < 0)
		{
			LogTools.p(LOG_TAG, "[Method:cancelTask] task info is error.");
			return false;
		}
		
		TGTaskQueue taskQueue = getTaskQueue(taskType);
		TGTask task = taskQueue.getTask(taskId);
		
		if(task != null)
		{
			task.cancel();
			return true;
		}
		else
		{
			LogTools.p(LOG_TAG, "[Method:cancelTask] taskQueue is empty.");
			return false;
		}
	}
	
	/**
	 * 该方法的作用:
	 * 暂停任务
	 * @author l00220455
	 * @date 2014年3月20日
	 * @param task
	 * @return
	 */
	public boolean pauseTask(int taskId, int taskType)
	{
		if(taskId < 0 || taskType < 0)
		{
			LogTools.p(LOG_TAG, "[Method:pauseTask] task info is error.");
			return false;
		}
		
		TGTaskQueue taskQueue = getTaskQueue(taskType);
		TGTask task = taskQueue.getTask(taskId);
		
		if(task != null)
		{
			task.pause();
			return true;
		}
		else
		{
			LogTools.p(LOG_TAG, "[Method:pauseTask] taskQueue is empty.");
			return false;
		}
	}

	/**
	 * 该方法的作用:
	 * 停止任务
	 * @author l00220455
	 * @date 2014年3月20日
	 * @param task
	 * @return
	 */
	public boolean stopTask(int taskId, int taskType)
	{
		if(taskId < 0 || taskType < 0)
		{
			LogTools.p(LOG_TAG, "[Method:stopTask] task info is error.");
			return false;
		}
		
		TGTaskQueue taskQueue = getTaskQueue(taskType);
		TGTask task = taskQueue.getTask(taskId);
		
		if(task != null)
		{
			task.stop();
			return true;
		}
		else
		{
			LogTools.p(LOG_TAG, "[Method:stopTask] taskQueue is empty.");
			return false;
		}
	}
	
	/**
	 * 该方法的作用: 对分发器加锁，暂停所有已派发任务
	 * 
	 * @author l00220455
	 * @date 2014年3月17日
	 * @return
	 */
	public void lock(final onLockListener onLockListener)
	{
		getLock().lock(new onLockListener()
		{
			@Override
			public void onLockSuccess()
			{
				if(null != onLockListener)
				{
					onLockListener.onLockSuccess();
				}
			}
			
			@Override
			public void onLockFailed()
			{
				if(null != onLockListener)
				{
					onLockListener.onLockSuccess();
				}
			}
		});
	}

	/**
	 * 该方法的作用: 对分发器解锁
	 * 
	 * @author l00220455
	 * @date 2014年3月17日
	 * @return
	 */
	public void unLock(final onUnLockListener onUnLockListener)
	{
		getLock().unLock(new onUnLockListener()
		{
			@Override
			public void onUnLockSuccess()
			{
				if(null != onUnLockListener)
				{
					onUnLockListener.onUnLockSuccess();
				}
			}
			
			@Override
			public void onUnLockFailed()
			{
				if(null != onUnLockListener)
				{
					onUnLockListener.onUnLockFailed();
				}
			}
		});
	}
	
	/**
	 * 该方法的作用: 获取锁
	 * 
	 * @author l00220455
	 * @date 2014年3月17日
	 * @return
	 */
	public TGLock getLock()
	{
		if(null == lock)
		{
			lock = new TGLock(context);
		}
		
		return lock;
	}

	/**
	 * 该方法的作用: 设置锁
	 * 
	 * @author l00220455
	 * @date 2014年3月17日
	 * @param lock
	 */
	public void setLock(TGLock lock)
	{
		this.lock = lock;
	}
	
	public Context getContext()
	{
		return context;
	}
	
	public void setContext(Context context)
	{
		this.context = context;
	}
}
