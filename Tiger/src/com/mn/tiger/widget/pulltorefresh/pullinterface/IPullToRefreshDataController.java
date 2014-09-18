package com.mn.tiger.widget.pulltorefresh.pullinterface;

import java.util.List;

import com.mn.tiger.widget.pulltorefresh.MPPullToRefreshListView;

/**
 * 该类作用及功能说明
 * 列表数据刷新控制器
 * @author l00220455
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public interface IPullToRefreshDataController<T>
{
	/**
	 * 列表刷新方式——追加
	 */
	public static final int REFRESH_LISTVIEW_APPEND = 1;
	
	/**
	 * 列表刷新方式——重置
	 */
	public static final int REFRESH_LISTVIEW_RESET = 2;
	
	/**
	 * 该方法的作用:
	 * 追加式刷新列表数据
	 * @author l00220455
	 * @date 2014年2月10日
	 * @param items
	 */
	void refreshListViewAppend(List<T> items);
	
	/**
	 * 该方法的作用:
	 * 重置刷新列表数据
	 * @author l00220455
	 * @date 2014年2月10日
	 * @param items
	 */
	void refreshListViewReset(List<T> items);
	
	/**
	 * 该方法的作用:
	 * 获取拖动刷新列表
	 * @author l00220455
	 * @date 2014年2月10日
	 * @return
	 */
    MPPullToRefreshListView<T> getPullToRefreshListView();
    
    /**
     * 该方法的作用:
     * 删除某页某数据
     * @author l00220455
     * @date 2014年3月13日
     * @param pageNum
     * @param item
     */
    void removeListItem(int pageNum, T item);
    
    /**
     * 该方法的作用:
     * 获取列表中某一页，某一个索引位置的数据
     * @author l00220455
     * @date 2014年3月27日
     * @param pageNum
     * @param indexInPage
     */
    T getListItem(int pageNum, int indexInPage);
    
    /**
     * 该方法的作用:
     * 将某一数据插入到列表中的某一页某一个索引位置
     * @author l00220455
     * @date 2014年3月27日
     * @param pageNum
     * @param indexInPage
     * @param item
     */
    void insertListItem(int pageNum, int indexInPage, T item);
}
