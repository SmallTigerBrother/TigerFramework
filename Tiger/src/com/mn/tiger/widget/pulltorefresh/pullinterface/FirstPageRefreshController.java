package com.mn.tiger.widget.pulltorefresh.pullinterface;

import android.content.Context;

import com.mn.tiger.widget.pulltorefresh.TGPullToRefreshListView;

/**
 * 该类作用及功能说明
 * 首页刷新控制器
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public class FirstPageRefreshController extends TGPullToRefreshController
{
	public FirstPageRefreshController(Context context, TGPullToRefreshListView<?> listView)
	{
		super(context, listView);
	}
	
	@Override
	public int countCurPagePullDownAfterPullUp(int currentPage)
	{
		//当前页码小于等于  起始页码-1  时，直接返回当前页码
		currentPage = currentPage - getShowPageMost();
		if(currentPage <= getStartPageNum() - 1)
		{
			return currentPage;
		}
		else
		{
			setCurPullOrientation(PULL_ORIENTATION_DOWN);
		}
		
		return currentPage;
	}
	
	@Override
	public int countCurPagePullDownAfterPullDown(int currentPage)
	{
		currentPage = currentPage - 1;
		return currentPage;
	}
}
