package com.mn.tiger.task;

public interface ITaskListener
{
	/**
	 * 该方法的作用:
	 * 任务启动回调方法
	 * @author l00220455
	 * @date 2014年3月17日
	 */
	void onTaskStart();
	
	/**
	 * 该方法的作用:
	 * 任务变化回调方法
	 * @author l00220455
	 * @date 2014年3月17日
	 * @param progress
	 */
	void onTaskChanged(int progress);
	
	/**
	 * 该方法的作用:
	 * 任务结束回调方法
	 * @author l00220455
	 * @date 2014年3月17日
	 */
	void onTaskFinished(int taskId);
	
	/**
	 * 该方法的作用:
	 * 任务出错回调方法
	 * @author l00220455
	 * @date 2014年3月17日
	 * @param code
	 * @param msg
	 */
	void onTaskError(int taskId, int code, Object msg);
	
	/**
	 * 该方法的作用:
	 * @author l00220455
	 * @date 2014年3月20日
	 * @param taskId
	 */
	void onTaskCancel(int taskId);
	
	/**
	 * 该方法的作用:
	 * 任务暂停
	 * @author l00220455
	 * @date 2014年8月21日
	 * @param taskId
	 */
	void onTaskPause(int taskId);
	
	/**
	 * 该方法的作用:
	 * 任务停止
	 * @author l00220455
	 * @date 2014年8月21日
	 * @param taskId
	 */
	void onTaskStop(int taskId);
}
