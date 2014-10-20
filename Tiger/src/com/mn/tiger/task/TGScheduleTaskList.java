package com.mn.tiger.task;

import java.util.ArrayList;

import android.content.Context;

import com.mn.tiger.task.invoke.TGTaskInvoker;
import com.mn.tiger.task.invoke.TGTaskParams;
import com.mn.tiger.task.utils.TGTaskIDCreator;


public class TGScheduleTaskList extends ArrayList<TGTask>
{
	private static final long serialVersionUID = 1L;
	
	private int taskMode = TGTaskManager.TASK_START_CODE;
	
	private int taskListId = 0;

	public TGScheduleTaskList()
	{
		taskListId = TGTaskIDCreator.createNextTaskID();
	}
	
	public void setTaskMode(int taskMode)
	{
		this.taskMode = taskMode;
	}
	
	public int getTaskMode()
	{
		return taskMode;
	}
	
	public int getTaskListId()
	{
		return taskListId;
	}
	
	public boolean addTaskByParams(Context context, TGTaskParams taskParams)
	{
		return this.add(TGTaskInvoker.createTask(context, taskParams));
	}
	
	public static int getTaskIDInSchedule(int taskListId, int taskIndex)
	{
		return -taskListId + taskIndex;
	}
	
}
