package com.mn.tiger.widget.pulltorefresh.pullinterface;


/**
 * 该类作用及功能说明
 * 拖动刷新控制器
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public interface IPullToRefreshController 
{
	/**
	 * 向上拖动
	 */
	public static final int PULL_ORIENTATION_UP = 1;
	
	/**
	 * 向下拖动
	 */
	public static final int PULL_ORIENTATION_DOWN = 2;
	
	/**
	 * 该方法的作用:
	 * 设置总页数
	 * @date 2014年2月10日
	 * @param totalPage
	 */
	void setTotalPage(int totalPage);
	
	/**
	 * 该方法的作用:
	 * 获取总页数
	 * @date 2014年2月10日
	 * @return
	 */
	int getTotalPage();
	
	/**
	 * 该方法的作用:
	 * 设置当前页码
	 * @date 2014年2月10日
	 * @param currentPage
	 */
	void setCurrentPage(int currentPage);
	
	/**
	 * 该方法的作用:
	 * 获取可以显示的最大页数
	 * @date 2014年2月10日
	 * @return
	 */
	int getShowPageMost();
	
	/**
	 * 该方法的作用:
	 * 获取当前拖动方向
	 * @date 2014年2月10日
	 * @return
	 */
	public int getCurPullOrientation();
	
	/**
	 * 该方法的作用:
	 * 获取当前页码
	 * @date 2014年2月10日
	 * @return
	 */
	int getCurrentPage();
	
	/**
	 * 该方法的作用:
	 * 获取每页显示的行数
	 * @date 2014年2月10日
	 * @return
	 */
	int getShowNumPerPage();
	
	/**
	 * 该方法的作用:
	 * 获取当前显示的总页数
	 * @date 2014年2月10日
	 * @return
	 */
	int getCurrentTotalPage();
	
	/**
	 * 该方法的作用:
	 * 获取起始页码
	 * @date 2014年2月10日
	 * @return
	 */
	int getStartPageNum();

	/**
	 * 该方法的作用:
	 * 检测向上拖动时是否需要刷新列表
	 * @date 2014年2月10日
	 * @return
	 */
	boolean checkNeedRefreshPullUp();
	
	/**
	 * 该方法的作用:
	 * 检测向下拖动时是否需要刷新列表
	 * @date 2014年2月10日
	 * @return
	 */
	boolean checkNeedRefreshPullDown();
	
	/**
	 * 该方法的作用:
	 * 无网络时向下拖动刷新列表
	 * @date 2014年2月10日
	 */
	void onPullDownToRefreshWithoutNetwork();
	
	/**
	 * 该方法的作用:
	 * 无网络时向上拖动刷新列表
	 * @date 2014年2月10日
	 */
	void onPullUpToRefreshWithoutNetwork();
	
	/**
	 * 该方法的作用:
	 * 重置列表刷新控制参数
	 * @date 2014年2月10日
	 */
	void reset();
}
