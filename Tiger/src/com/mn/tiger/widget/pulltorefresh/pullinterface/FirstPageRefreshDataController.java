package com.mn.tiger.widget.pulltorefresh.pullinterface;

import java.util.List;

import android.content.Context;

import com.mn.tiger.widget.pulltorefresh.MPPullToRefreshListView;

/**
 * 该类作用及功能说明
 * 首页刷新数据控制器
 * @author l00220455
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public class FirstPageRefreshDataController<T> extends MPPullToRefreshDataController<T>
{

	public FirstPageRefreshDataController(Context context, MPPullToRefreshListView<T> listView)
	{
		super(context, listView);
	}
	
	@Override
	public void refreshListViewReset(List<T> items)
	{
		if(getPullToRefreshListView().getPullToRefreshController().getTotalPage() >= 1)
		{
			getPullToRefreshListView().getRefreshableView().setEmptyView(null);
			
			getPullToRefreshListView().setPullToRefreshEnabled(true);
			getPullToRefreshListView().setPullDownEnable(true);
			getPullToRefreshListView().setPullUpEnable(true);
			
			updateListViewDataReset(items);
		}
		else
		{
			
		}
	}

}
