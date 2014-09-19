package com.mn.tiger.task.utils;

/**
 * 该类作用及功能说明 任务ID创造器
 * 
 * @date 2014年3月17日
 */
public class TGTaskIDCreator
{
	private static int CURRENT_TASK_ID = 1;

	/**
	 * 该方法的作用: 自动生成一个任务的ID
	 * 
	 * @date 2014年3月17日
	 * @return
	 */
	public static int createNextTaskID()
	{
		// //当前任务自动 + 1
		// CURRENT_TASK_ID += 1;
		// return CURRENT_TASK_ID;
		
		// 获取当前时间后9位
		String time = System.currentTimeMillis() + System.nanoTime() + "";
		CURRENT_TASK_ID = Integer.valueOf(time.substring(time.length() - 9, time.length()));
		return CURRENT_TASK_ID;

	}

}
