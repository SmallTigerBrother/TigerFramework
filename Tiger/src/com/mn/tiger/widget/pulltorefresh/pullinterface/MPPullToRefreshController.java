package com.mn.tiger.widget.pulltorefresh.pullinterface;

import android.content.Context;
import android.widget.Toast;

import com.mn.tiger.utility.CR;
import com.mn.tiger.utility.LogTools;
import com.mn.tiger.widget.pulltorefresh.MPPullToRefreshListView;

/**
 * 该类作用及功能说明
 * 列表刷新控制器实现类
 * @author l00220455
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public class MPPullToRefreshController implements IPullToRefreshController 
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 当前的拖动方向
	 */
	private int curPullOrientation = PULL_ORIENTATION_UP;
	
	/**
	 * 当前页码
	 */
	private int currentPage = 1;
	
	/**
	 * 当前显示的所有页数
	 */
	private int currentTotalPage = 0;
	
	/**
	 * 列表需要显示的所有页数
	 */
	private int totalPage = 0;
	
	private Context context;
	
	/**
	 * 拖动刷新列表
	 */
	private MPPullToRefreshListView<?> listView;
	
	/**
	 * 起始页码
	 */
	private int startPageNum = 1;
	
	/**
	 * 起始页码与 1 之间的差值
	 */
	private int deltaTotalPage = 0;
	
	public MPPullToRefreshController(Context context, MPPullToRefreshListView<?> listView) 
	{
		this.context = context;
		this.listView = listView;
	}
	
	/**
	 * 该方法的作用:
	 * 判断是否需要刷新界面
	 * @author l00220455
	 * @date 2013-11-1
	 * @return 无需刷新返回false，需要刷新返回true
	 */
	@Override
	public boolean checkNeedRefreshPullUp()
	{
		int curPage = countCurPagePullUp();
		if(curPage != getCurrentPage())
		{
			setCurrentPage(curPage);
			if(currentTotalPage < getShowPageMost())
			{
				currentTotalPage++;
			}
			
			LogTools.d(LOG_TAG, "[Method:checkNeedRefreshPullUp]" + "-->curPage == " + curPage + 
					";-->currentTotalPage == " + currentTotalPage);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * 该方法的作用:
	 * 向下拖动时计算当前页码
	 * @author l00220455
	 * @date 2014年2月10日
	 * @return
	 */
	protected int countCurPagePullDown()
	{
		int curPage = currentPage;
		
		if (getCurPullOrientation() == PULL_ORIENTATION_UP) 
		{
			return countCurPagePullDownAfterPullUp(curPage);
		} 
		else
		{
			return countCurPagePullDownAfterPullDown(curPage);
		}
	}
	
	/**
	 * 该方法的作用:
	 * 向上拖动后向下拖动时计算当前页码
	 * @author l00220455
	 * @date 2014年2月10日
	 * @param currentPage
	 * @return
	 */
	protected int countCurPagePullDownAfterPullUp(int currentPage) 
	{
		int curPage = currentPage - getShowPageMost();
		if(curPage < startPageNum)
		{
			Toast.makeText(context, context.getString(CR.getStringsId(context, 
					"mjet_pull_to_refresh_already_firstpage")), Toast.LENGTH_SHORT).show();
			curPage = curPage + getShowPageMost();
		}
		else 
		{
			setCurPullOrientation(PULL_ORIENTATION_DOWN);
		}
		
		return curPage;
	}

	/**
	 * 该方法的作用:
	 * 向下拖动后向下拖动时计算当前页码
	 * @author l00220455
	 * @date 2014年2月10日
	 * @param currentPage
	 * @return
	 */
	protected int countCurPagePullDownAfterPullDown(int currentPage)
	{
		int curPage = currentPage - 1;
		
		if(curPage < startPageNum)
		{
			Toast.makeText(context, context.getString(CR.getStringsId(context, 
					"mjet_pull_to_refresh_already_firstpage")), Toast.LENGTH_SHORT).show();
			curPage++;
		}
		
		return curPage;
	}
	
	/**
	 * 该方法的作用:
	 * 判断是否需要刷新界面
	 * @author l00220455
	 * @date 2013-11-1
	 * @return 无需刷新返回false，需要刷新返回true
	 */
	@Override
	public boolean checkNeedRefreshPullDown()
	{
		int curPage = countCurPagePullDown();
		if(curPage != getCurrentPage())
		{
			setCurrentPage(curPage);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 该方法的作用:
	 * 向上拖动时计算当前页码
	 * @author l00220455
	 * @date 2014年2月10日
	 * @return
	 */
	public int countCurPagePullUp()
	{
		int curPage = currentPage;
		
		if(getCurPullOrientation() == PULL_ORIENTATION_UP)
		{
			return countCurPagePullUpAfterPullUp(curPage);
		}
		else
		{
			return countCurPagePullUpAfterPullDown(curPage);
		}
	}

	/**
	 * 该方法的作用:
	 * 向上拖动后向上拖动时计算当前页码
	 * @author l00220455
	 * @date 2014年2月10日
	 * @param currentPage
	 * @return
	 */
	protected int countCurPagePullUpAfterPullUp(int currentPage) 
	{
		int curPage = currentPage + 1;
		if (curPage > totalPage + deltaTotalPage) 
		{
			Toast.makeText(context, context.getString(CR.getStringsId(
					context, "mjet_pull_to_refresh_already_lastpage")),
					Toast.LENGTH_SHORT).show();
			curPage--;
		}
		
		return curPage;
	}

	/**
	 * 该方法的作用:
	 * 向下拖动后向上拖动时计算当前页码
	 * @author l00220455
	 * @date 2014年2月10日
	 * @param currentPage
	 * @return
	 */
	protected int countCurPagePullUpAfterPullDown(int currentPage) 
	{
		int curPage = currentPage + getShowPageMost();
		if (curPage > totalPage + deltaTotalPage) 
		{
			Toast.makeText(context, context.getString(CR.getStringsId(
					context, "mjet_pull_to_refresh_already_lastpage")), 
					Toast.LENGTH_SHORT).show();
			curPage = curPage - getShowPageMost();
		}
		else 
		{
			setCurPullOrientation(PULL_ORIENTATION_UP);
		}
		
		return curPage;
	}

	/**
	 * 该方法的作用:
	 * 设置当前显示的总页数
	 * @author l00220455
	 * @date 2014年2月10日
	 * @param currentTotalPage
	 */
	protected void setCurrentTotalPage(int currentTotalPage) 
	{
		this.currentTotalPage = currentTotalPage;
	}
	
	@Override
	public int getCurrentTotalPage() 
	{
		return currentTotalPage;
	}
	
	@Override
	public void setTotalPage(int totalPage) 
	{
		this.totalPage = totalPage;
	}

	@Override
	public int getTotalPage() 
	{
		return totalPage;
	}
	
	@Override
	public int getCurrentPage() 
	{
		return currentPage;
	}
	
	@Override
	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}

	@Override
	public int getShowPageMost()
	{
		return 3;
	}

	@Override
	public int getShowNumPerPage()
	{
		return 10;
	}
	
	@Override
	public int getCurPullOrientation()
	{
		return curPullOrientation;
	}

	/**
	 * 该方法的作用:
	 * 设置当前拖动方向
	 * @author l00220455
	 * @date 2014年2月10日
	 * @param pullOrientation
	 */
	protected void setCurPullOrientation(int pullOrientation)
	{
		this.curPullOrientation = pullOrientation;
	}

	protected Context getContext()
	{
		return context;
	}

	@Override
	public void onPullDownToRefreshWithoutNetwork()
	{
		listView.onRefreshComplete();
	}

	@Override
	public void onPullUpToRefreshWithoutNetwork()
	{
		listView.onRefreshComplete();
	}

	@Override
	public void reset()
	{
		currentPage = startPageNum;
		curPullOrientation = PULL_ORIENTATION_UP;
		currentTotalPage = 0;
	}

	/**
	 * 该方法的作用:
	 * 设置拖动刷新列表
	 * @author l00220455
	 * @date 2014年2月10日
	 * @param listView
	 */
	public void setPullToRefreshListView(MPPullToRefreshListView<?> listView) 
	{
		this.listView = listView;
	}
	
	/**
	 * 该方法的作用:
	 * 获取拖动刷新列表
	 * @author l00220455
	 * @date 2014年2月10日
	 * @return
	 */
	public MPPullToRefreshListView<?> getPullToRefreshListView() 
	{
		return listView;
	}

	@Override
	public int getStartPageNum() 
	{
		return startPageNum;
	}

	/**
	 * 该方法的作用:
	 * 设置其实页码
	 * @author l00220455
	 * @date 2014年2月10日
	 * @param startPageNum
	 */
	public void setStartPageNum(int startPageNum) 
	{
		this.startPageNum = startPageNum;
		this.deltaTotalPage = startPageNum - 1;
	}
	
	/**
	 * 该方法的作用:
	 * 获取DeltaTotalPage（起始页码与 1 之间的差值）
	 * @author l00220455
	 * @date 2014年2月10日
	 * @return
	 */
	protected int getDeltaTotalPage() 
	{
		return deltaTotalPage;
	}
}
