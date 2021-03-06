package com.mn.tiger.task;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;

import com.mn.tiger.log.LogTools;
import com.mn.tiger.task.invoke.TGTaskInvoker;
import com.mn.tiger.task.invoke.TGTaskParams;
import com.mn.tiger.task.result.TGTaskResultHandler;
import com.mn.tiger.task.utils.TGTaskIDCreator;

/**
 * 任务管理器，单例类
 */
public class TGTaskManager
{
	/**
	 * 日志标签
	 */
	protected static final String LOG_TAG = TGTaskManager.class.getSimpleName();
	
	/**
	 * Manager单例对象
	 */
	private static TGTaskManager instance;
	
	/**
	 * invoker单例对象
	 */
	private static TGTaskInvoker invoker;
	
	/**
	 * 任务动作: 开始任务
	 */
	public static final int TASK_START_MODE = 1;
	
	/**
	 * 任务动作: 取消任务
	 */
	public static final int TASK_CANCEL_MODE = 2;
	
	/**
	 * 任务动作: 暂停任务，任务会保留在任务队列中
	 */
	public static final int TASK_PAUSE_MODE = 3;
	
	/**
	 * 获取对象实例
	 * @return
	 */
	public static synchronized TGTaskManager getInstance()
	{
		if (null == instance)
		{
			instance = new TGTaskManager();
		}
		
		if(null == invoker)
		{
			invoker = new TGTaskInvoker();
		}

		return instance;
	}
	
	private TGTaskManager()
	{
		
	}
	
	/**
	 * 
	 * 该方法的作用: 添加开始任务
	 * @date 2014年8月11日
	 * @param taskParams
	 * @return
	 */
	public int startTask(Context context, TGTaskParams taskParams)
	{
		if(taskParams == null)
		{
			return -1;
		}
	
		LogTools.d(LOG_TAG, "[Method:startTask]");
		taskParams.setTaskMode(TASK_START_MODE);
		
		return invoker.invokeTask(context, taskParams);
	}
	
	/**
	 * 启动有序任务列表
	 * @param context
	 * @param taskList
	 * @return
	 */
	public int startScheduleTaskList(Context context, TGScheduleTaskList taskList)
	{
		if(null == taskList || taskList.isEmpty())
		{
			return -1;
		}
		
		LogTools.d(LOG_TAG, "[Method:startScheduleTaskList]");
		taskList.setTaskMode(TASK_START_MODE);
		return invoker.invokeScheduleTaskList(context, taskList);
	}
	
	/**
	 * 取消任务
	 * @param taskId
	 * @param taskType
	 */
	public void cancelTask(int taskId, int taskType)
	{
		if(taskId < 0)
		{
			LogTools.e(LOG_TAG, "[Method:cancelTask] the task is invalid; " + "  taskID-->" + taskId);
			return;
		}
	
		LogTools.d(LOG_TAG, "[Method:cancelTask]" + "  taskID-->" + taskId);
		
		TGTaskParams taskParams = new TGTaskParams();
		taskParams.setTaskID(taskId);
		taskParams.setTaskType(taskType);
		taskParams.setTaskMode(TASK_CANCEL_MODE);
		invoker.invokeTask(null, taskParams);
	}
	
	/**
	 * 取消有序任务列表
	 * @param context
	 * @param taskList
	 */
	public void cancelScheduleTaskList(Context context, TGScheduleTaskList taskList)
	{
		if(null == taskList || taskList.isEmpty())
		{
			return;
		}
		
		LogTools.d(LOG_TAG, "[Method:startScheduleTaskList]");
		taskList.setTaskMode(TASK_CANCEL_MODE);
		invoker.invokeScheduleTaskList(context, taskList);
	}
	
	/**
	 * 停止任务
	 * @date 2014年5月21日
	 * @param task
	 */
	public void pauseTask(int taskId, int taskType)
	{
		if(taskId < 0)
		{
			LogTools.e(LOG_TAG, "[Method:pauseTask] the task is invalid; " + "  taskID-->" + taskId);
			return;
		}
	
		LogTools.d(LOG_TAG, "[Method:pauseTask]" + "  taskID-->" + taskId);
		
		TGTaskParams taskParams = new TGTaskParams();
		taskParams.setTaskID(taskId);
		taskParams.setTaskType(taskType);
		taskParams.setTaskMode(TASK_PAUSE_MODE);
		invoker.invokeTask(null, taskParams);
	}
	
	/**
	 * 该方法的作用:创建任务参数
	 * @param params
	 * @param taskClsName
	 * @param taskResultHandler
	 * @return
	 */
	public static TGTaskParams createTaskParams(HashMap<String, String> params, String taskClsName,
			TGTaskResultHandler taskResultHandler)
	{
		return createTaskParams(params, taskClsName, taskResultHandler, TGTaskIDCreator.createNextTaskID());
	}
	
	/**
	 * 该方法的作用:创建任务参数
	 * @date 2014年6月3日
	 * @param params
	 *            hashmap
	 * @param taskClsName
	 * @param taskResultHandler
	 * @return
	 */
	public static TGTaskParams createTaskParams(HashMap<String, String> params, String taskClsName,
			TGTaskResultHandler taskResultHandler, int taskId)
	{
		TGTaskParams taskParams = new TGTaskParams();
		taskParams.setMapParams(params);
		taskParams.setTaskClsName(taskClsName);
		
		taskParams.setTaskID(taskId);
		taskParams.setTaskMode(TASK_START_MODE);
		if (null != taskResultHandler)
		{
			taskParams.setMessenger(taskResultHandler.getMessenger());
		}
		
		return taskParams;
	}
	
	/**
	 * 该方法的作用:创建任务参数
	 * @param param
	 * @param taskClsName
	 * @param taskResultHandler
	 * @return
	 */
	public static TGTaskParams createTaskParams(String param, String taskClsName,
			TGTaskResultHandler taskResultHandler)
	{
		return createTaskParams(param, taskClsName, taskResultHandler, TGTaskIDCreator.createNextTaskID());
	}
	
	/**
	 * 该方法的作用:创建任务参数
	 * @param param
	 * @param taskClsName
	 * @param taskResultHandler
	 * @param taskId 指定的任务ID号
	 * @return
	 */
	public static TGTaskParams createTaskParams(String param, String taskClsName,
			TGTaskResultHandler taskResultHandler, int taskId)
	{
		TGTaskParams taskParams = new TGTaskParams();
		taskParams.setStringParams(param);
		taskParams.setTaskClsName(taskClsName);
		
		taskParams.setTaskID(taskId);
		taskParams.setTaskMode(TASK_START_MODE);
		if (null != taskResultHandler)
		{
			taskParams.setMessenger(taskResultHandler.getMessenger());
		}

		return taskParams;
	}

	/**
	 * 该方法的作用:创建任务参数
	 * @param params
	 * @param taskClsName
	 * @param taskResultHandler
	 * @return
	 */
	public static TGTaskParams createTaskParams(Bundle params, String taskClsName,
			TGTaskResultHandler taskResultHandler)
	{
		return createTaskParams(params, taskClsName, taskResultHandler, TGTaskIDCreator.createNextTaskID());
	}
	
	/**
	 * 该方法的作用:创建任务参数
	 * @date 2014年6月3日
	 * @param params
	 *            Bundle
	 * @param taskClsName
	 * @param taskResultHandler
	 * @param taskId 指定的任务ID号
	 * @return
	 */
	public static TGTaskParams createTaskParams(Bundle params, String taskClsName,
			TGTaskResultHandler taskResultHandler, int taskId)
	{
		TGTaskParams taskParams = new TGTaskParams();
		taskParams.setBundleParams(params);
		taskParams.setTaskClsName(taskClsName);
		
		taskParams.setTaskID(taskId);
		taskParams.setTaskMode(TASK_START_MODE);
		if (null != taskResultHandler)
		{
			taskParams.setMessenger(taskResultHandler.getMessenger());
		}

		return taskParams;
	}
}
