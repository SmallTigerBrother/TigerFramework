package com.mn.tiger.task;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;

import com.mn.tiger.task.invoke.TGTaskInvoker;
import com.mn.tiger.task.invoke.TGTaskParams;
import com.mn.tiger.task.result.TGTaskResultHandler;
import com.mn.tiger.task.utils.TGTaskIDCreator;
import com.mn.tiger.utility.LogTools;

public class TGTaskManager
{
	/**
	 * 日志标签
	 */
	protected static final String LOG_TAG = TGTaskManager.class.getSimpleName();
	
	private static TGTaskManager instance;
	
	private static TGTaskInvoker invoker;
	
	/**
	 * 任务动作: 开始任务
	 */
	public static final int TASK_START_CODE = 1;
	/**
	 * 任务动作: 取消任务
	 */
	public static final int TASK_CANCEL_CODE = 2;
	/**
	 * 任务动作: 暂停任务，任务会保留在任务队列中
	 */
	public static final int TASK_PAUSE_CODE = 3;
	/**
	 * 任务动作: 停止任务
	 */
	public static final int TASK_STOP_CODE = 4;
	
	private TGTaskManager()
	{
		
	}
	
	public static synchronized TGTaskManager getInstance(Context context)
	{
		if (instance == null)
		{
			instance = new TGTaskManager();
			invoker = new TGTaskInvoker(context);
		}

		return instance;
	}
	
	/**
	 * 
	 * 该方法的作用: 添加开始任务
	 * @author pWX197040
	 * @date 2014年8月11日
	 * @param task
	 * @return
	 */
	public int startTask(TGTaskParams taskParams)
	{
		if(taskParams == null)
		{
			return -1;
		}
	
		LogTools.d(LOG_TAG, "[Method:startTask]");
		taskParams.setTaskMode(TASK_START_CODE);
		
		return invoker.invoke(taskParams);
	}
	
	/**
	 * 取消任务
	 * @author pWX219598
	 * @date 2014年5月21日
	 * @param task
	 */
	public void cancelTask(int taskId, int taskType)
	{
		if(taskId < 0)
		{
			return;
		}
	
		LogTools.d(LOG_TAG, "[Method:cancelTask]" + "  taskID-->" + taskId);
		
		TGTaskParams taskParams = new TGTaskParams();
		taskParams.setTaskID(taskId);
		taskParams.setTaskType(taskType);
		taskParams.setTaskMode(TASK_CANCEL_CODE);
		invoker.invoke(taskParams);
	}
	
	/**
	 * 停止任务
	 * @author pWX219598
	 * @date 2014年5月21日
	 * @param task
	 */
	public void stopTask(int taskId, int taskType)
	{
		if(taskId < 0)
		{
			return;
		}
	
		LogTools.d(LOG_TAG, "[Method:stopTask]" + "  taskID-->" + taskId);
		
		TGTaskParams taskParams = new TGTaskParams();
		taskParams.setTaskID(taskId);
		taskParams.setTaskType(taskType);
		taskParams.setTaskMode(TASK_STOP_CODE);
		invoker.invoke(taskParams);
	}
	
	/**
	 * 停止任务
	 * @author pWX219598
	 * @date 2014年5月21日
	 * @param task
	 */
	public void pauseTask(int taskId, int taskType)
	{
		if(taskId < 0)
		{
			return;
		}
	
		LogTools.d(LOG_TAG, "[Method:pauseTask]" + "  taskID-->" + taskId);
		
		TGTaskParams taskParams = new TGTaskParams();
		taskParams.setTaskID(taskId);
		taskParams.setTaskType(taskType);
		taskParams.setTaskMode(TASK_PAUSE_CODE);
		invoker.invoke(taskParams);
	}
	
	/**
	 * 该方法的作用:创建任务参数
	 * @author pWX219598
	 * @date 2014年6月3日
	 * @param params
	 *            hashmap
	 * @param taskClsName
	 * @param taskResultHandler
	 * @return
	 */
	public static TGTaskParams createTaskParams(HashMap<String, String> params, String taskClsName,
			TGTaskResultHandler taskResultHandler)
	{
		return createTaskParams(params, taskClsName, taskResultHandler, 0);
	}

	/**
	 * 该方法的作用:创建任务参数
	 * @author pWX219598
	 * @date 2014年6月3日
	 * @param params
	 *            hashmap
	 * @param taskClsName
	 * @param taskResultHandler
	 * @param weight
	 * @return
	 */
	public static TGTaskParams createTaskParams(HashMap<String, String> params, String taskClsName,
			TGTaskResultHandler taskResultHandler, int weight)
	{
		return createTaskParams(params, taskClsName, taskResultHandler, weight, TGTaskIDCreator.createNextTaskID());
	}
	
	/**
	 * 该方法的作用:创建任务参数
	 * @author pWX219598
	 * @date 2014年6月3日
	 * @param params
	 *            hashmap
	 * @param taskClsName
	 * @param taskResultHandler
	 * @param weight
	 * @param taskId
	 * @return
	 */
	public static TGTaskParams createTaskParams(HashMap<String, String> params, String taskClsName,
			TGTaskResultHandler taskResultHandler, int weight, int taskId)
	{
		TGTaskParams taskParams = new TGTaskParams();
		taskParams.setMapParams(params);
		taskParams.setTaskClsName(taskClsName);
		
		taskParams.setTaskID(taskId);
		taskParams.setTaskMode(TASK_START_CODE);
		if (null != taskResultHandler)
		{
			taskParams.setMessenger(taskResultHandler.getMessenger());
		}
		if (weight > 0)
		{
			taskParams.setTaskWeight(weight);
		}
		
		return taskParams;
	}
	
	/**
	 * 该方法的作用:创建任务参数
	 * @author pWX219598
	 * @date 2014年6月3日
	 * @param param
	 *            String
	 * @param taskClsName
	 * @param taskResultHandler
	 * @return
	 */
	public static TGTaskParams createTaskParams(String param, String taskClsName,
			TGTaskResultHandler taskResultHandler)
	{
		return createTaskParams(param, taskClsName, taskResultHandler, 0);
	}
	
	/**
	 * 该方法的作用:创建任务参数
	 * @author pWX219598
	 * @date 2014年6月3日
	 * @param param
	 *            String
	 * @param taskClsName
	 * @param taskResultHandler
	 * @param weight
	 * @return
	 */
	public static TGTaskParams createTaskParams(String param, String taskClsName,
			TGTaskResultHandler taskResultHandler, int weight)
	{
		return createTaskParams(param, taskClsName, taskResultHandler, weight, TGTaskIDCreator.createNextTaskID());
	}
	
	/**
	 * 该方法的作用:创建任务参数
	 * @author pWX219598
	 * @date 2014年6月3日
	 * @param param
	 *            String
	 * @param taskClsName
	 * @param taskResultHandler
	 * @param weight
	 * @param taskId
	 * @return
	 */
	public static TGTaskParams createTaskParams(String param, String taskClsName,
			TGTaskResultHandler taskResultHandler, int weight, int taskId)
	{
		TGTaskParams taskParams = new TGTaskParams();
		taskParams.setStringParams(param);
		taskParams.setTaskClsName(taskClsName);
		
		taskParams.setTaskID(taskId);
		taskParams.setTaskMode(TASK_START_CODE);
		if (null != taskResultHandler)
		{
			taskParams.setMessenger(taskResultHandler.getMessenger());
		}
		if (weight > 0)
		{
			taskParams.setTaskWeight(weight);
		}

		return taskParams;
	}

	/**
	 * 该方法的作用:创建任务参数
	 * @author pWX219598
	 * @date 2014年6月3日
	 * @param params
	 *            Bundle
	 * @param taskClsName
	 * @param taskResultHandler
	 * @return
	 */
	public static TGTaskParams createTaskParams(Bundle params, String taskClsName,
			TGTaskResultHandler taskResultHandler)
	{
		return createTaskParams(params, taskClsName, taskResultHandler, 0);
	}
	
	/**
	 * 该方法的作用:创建任务参数
	 * @author pWX219598
	 * @date 2014年6月3日
	 * @param params
	 *            Bundle
	 * @param taskClsName
	 * @param taskResultHandler
	 * @param weight
	 * @return
	 */
	public static TGTaskParams createTaskParams(Bundle params, String taskClsName,
			TGTaskResultHandler taskResultHandler, int weight)
	{
		return createTaskParams(params, taskClsName, taskResultHandler, weight, TGTaskIDCreator.createNextTaskID());
	}
	
	/**
	 * 该方法的作用:创建任务参数
	 * @author pWX219598
	 * @date 2014年6月3日
	 * @param params
	 *            Bundle
	 * @param taskClsName
	 * @param taskResultHandler
	 * @param weight
	 * @param taskId
	 * @return
	 */
	public static TGTaskParams createTaskParams(Bundle params, String taskClsName,
			TGTaskResultHandler taskResultHandler, int weight, int taskId)
	{
		TGTaskParams taskParams = new TGTaskParams();
		taskParams.setBundleParams(params);
		taskParams.setTaskClsName(taskClsName);
		
		taskParams.setTaskID(taskId);
		taskParams.setTaskMode(TASK_START_CODE);
		if (null != taskResultHandler)
		{
			taskParams.setMessenger(taskResultHandler.getMessenger());
		}
		if (weight > 0)
		{
			taskParams.setTaskWeight(weight);
		}

		return taskParams;
	}
}
