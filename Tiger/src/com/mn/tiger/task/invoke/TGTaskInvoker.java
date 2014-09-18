package com.mn.tiger.task.invoke;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.mn.tiger.service.TGRemoteService;
import com.mn.tiger.system.AppConfiguration;
import com.mn.tiger.task.TGTask;
import com.mn.tiger.task.TGTaskManager;
import com.mn.tiger.task.dispatch.TGDispatcher;
import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明 Android任务请求类
 * 
 * @author l00220455
 * @date 2014年3月18日
 */
public class TGTaskInvoker
{
	/**
	 * 日志标签
	 */
	protected static final String LOG_TAG = TGTaskInvoker.class.getSimpleName();
	
	/**
	 * 上下文信息
	 */
	private Context context;
	
	/**
	 * 启动的service名称
	 */
	private static String action;

	/**
	 * 远程数据交互的aidl接口 
	 */
	private static TGRemoteService remoteService;

	/**
	 * service链接
	 */
	private ServiceConnection serviceConnection;
	
	/**
	 * remoteService未启动时，临时存储task的集合
	 */
	private static Vector<TGTaskParams> waitingTaskParams;
	
	/**
	 * 构造函数
	 * @author pWX197040
	 * @date 2014年7月26日
	 * @param context
	 */
	public TGTaskInvoker(Context context)
	{
		this.context = context;
	}

	/**
	 * 该方法的作用: 任务调度
	 * @author pWX197040
	 * @date 2014年7月26日
	 * @param taskParams
	 * @return
	 */
	public int invoke(TGTaskParams taskParams)
	{
		LogTools.p(LOG_TAG, "[Method:invoke]");
		// 不在service里执行, 直接调度task
		if(!AppConfiguration.isRunAtService())
		{
			return invokeTask(context, taskParams);
		}
		
		// 在service里执行, 在remoteService里调度task
		try
		{
			if(null != remoteService)
			{
				remoteService.invoke(taskParams);
			}
			else
			{
				LogTools.e(LOG_TAG, "The connection between this and service hasn't prepared");
				//将任务添加到等待任务队列中
				getWaitingTaskList().add(taskParams);
				TGTaskInvoker.initService(context.getApplicationContext(), action);
			}
		}
		catch (RemoteException e)
		{
			LogTools.e(LOG_TAG, "[method:invoke] remote exception.", e);
		}
		return taskParams.getTaskID();
	}
	
	/**
	 * 
	 * 该方法的作用: 任务调度
	 * @author pWX197040
	 * @date 2014年7月26日
	 * @param taskParams
	 * @return
	 */
	public static int invokeTask(Context context, TGTaskParams taskParams)
	{
		LogTools.p(LOG_TAG, "[Method:invoke]");
		TGTask task = null;
		switch (taskParams.getTaskMode())
		{
			case TGTaskManager.TASK_START_CODE:
				// 创建任务
				task = createTask(context, taskParams);
				// 分发并执行任务
				if (null != task)
				{
					TGDispatcher.getInstance(context).dispatchAndExecuteTask(task);
				}
				break;
			case TGTaskManager.TASK_CANCEL_CODE:
				// 结束任务并删除
				TGDispatcher.getInstance(context).cancelTask(taskParams.getTaskID(),
						taskParams.getTaskType());
				break;
			case TGTaskManager.TASK_PAUSE_CODE:
				// 结束任务并删除
				TGDispatcher.getInstance(context).pauseTask(taskParams.getTaskID(),
						taskParams.getTaskType());
				break;
			case TGTaskManager.TASK_STOP_CODE:
				// 结束任务并删除
				TGDispatcher.getInstance(context).stopTask(taskParams.getTaskID(),
						taskParams.getTaskType());
				break;
			default:
				break;
		}
		
		// 返回任务id
		if(task == null || task.getTaskID() < 0)
		{
			return -1;
		}
		else
		{
			return task.getTaskID();
		}
	}
	
	/**
	 * 该方法的作用:
	 * @author pWX219598
	 * @date 2014年5月16日
	 * @param taskParams
	 * @return
	 */
	public static TGTask createTask(Context context, TGTaskParams taskParams)
	{
		LogTools.d(LOG_TAG, "[Method:createTask]");
		TGTask task = null;
		Bundle data = taskParams.getData();
		try
		{
			task = (TGTask) Class.forName(data.getString("taskClassName")).newInstance();
			task.setMessenger(taskParams.getMessenger());
			task.setTaskID(taskParams.getTaskID());
			task.setType(taskParams.getTaskType());
			task.setParams(parseTaskParams(data));
			task.setExecutionTime(System.currentTimeMillis());
			task.setContext(context);
			if(taskParams.getTaskWeight() > 0)
			{
				task.setWeight(taskParams.getTaskWeight());
			}
			return task;
		}
		catch (Exception e)
		{
			LogTools.e(LOG_TAG, "[method:createTask] create task error.", e);
		}
		return task;
	}
	
	/**
	 * 
	 * 该方法的作用:获取taskParams中Bundle中的任务参数
	 * 
	 * @author pWX219598
	 * @date 2014年5月26日
	 * @param data
	 * @return
	 */
	private static Object parseTaskParams(Bundle data)
	{
		switch (data.getInt("paramType", TGTaskParams.PARAM_TYPE_UNKNOW))
		{
			case TGTaskParams.PARAM_TYPE_MAP:
				return data.getSerializable("params");
			case TGTaskParams.PARAM_TYPE_BUNDLE:
				return data.getBundle("params");
			case TGTaskParams.PARAM_TYPE_STRING:
				return data.getString("params");
			default:
				return null;
		}
	}
	
	/**
	 * 该方法的作用: 初始化服务
	 * @author l00220455
	 * @date 2014年3月18日
	 */
	public static void initService(Context context, String action)
	{
		if(action.equals(TGTaskInvoker.action))
		{
			if (null == remoteService)
			{
				connectRemoteService(context, action);
			}
		}
		else
		{
			connectRemoteService(context, action);
		}
	}

	/**
	 * 该方法的作用:链接到远程服务
	 * @author pWX219598
	 * @date 2014年5月21日
	 * @param context
	 */
	private static void connectRemoteService(Context context, String action)
	{
		TGTaskInvoker.action = action;
		Intent service = new Intent(action);
		context.bindService(service, conn, Context.BIND_AUTO_CREATE);
	}

	/**
	 * 服务链接
	 */
	private static ServiceConnection conn = new ServiceConnection()
	{
		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			remoteService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			remoteService = TGRemoteService.Stub.asInterface(service);
			
			new Thread()
			{
				public void run() 
				{
					for (Iterator<TGTaskParams> iterator = getWaitingTaskList().iterator(); iterator.hasNext();)
					{
						try
						{
							remoteService.invoke(iterator.next());
							iterator.remove();
						}
						catch (RemoteException e)
						{
							LogTools.e(LOG_TAG, "[method:onServiceConnected]", e);
						}
					}
				};
			}.start();
		}
	};

	/**
	 * 断开远程连接
	 * @author pWX219598
	 * @date 2014年5月21日
	 * @param context
	 */
	public static void unConnectRemoteService(Context context)
	{
		context.unbindService(conn);
	}

	/**
	 * 
	 * 该方法的作用: 过去服务链接
	 * @author pWX197040
	 * @date 2014年7月26日
	 * @return
	 */
	public ServiceConnection getServiceConnection()
	{
		return serviceConnection;
	}
	
	/**
	 * 
	 * 该方法的作用: 获取绑定的service
	 * @author pWX197040
	 * @date 2014年7月26日
	 * @return
	 */
	public String getBindServiceAction()
	{
		return action;
	}
	
	/**
	 * 该方法的作用:
	 * 获取等待发送的任务
	 * @author l00220455
	 * @date 2014年6月19日
	 * @return
	 */
	private static List<TGTaskParams> getWaitingTaskList()
	{
		if(null == waitingTaskParams)
		{
			waitingTaskParams = new Vector<TGTaskParams>();
		}
		
		return waitingTaskParams;
	}
	
	/**
	 * 
	 * 该方法的作用: 设置上下文信息
	 * @author pWX197040
	 * @date 2014年7月26日
	 * @param context
	 */
	public void setContext(Context context)
	{
		this.context = context;
	}

	/**
	 * 
	 * 该方法的作用: 获取上下文信息
	 * @author pWX197040
	 * @date 2014年7月26日
	 * @return
	 */
	public Context getContext()
	{
		return context;
	}

}
