package com.mn.tiger.widget.pulltorefresh.pullinterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.HeaderViewListAdapter;
import android.widget.Toast;

import com.mn.tiger.utility.CR;
import com.mn.tiger.widget.adpter.TGListAdapter;
import com.mn.tiger.widget.pulltorefresh.TGPullToRefreshListView;

/**
 * 该类作用及功能说明
 * 拖动刷新数据控制器
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public class TGPullToRefreshDataController<T> implements IPullToRefreshDataController<T>
{	
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 拖动刷新列表
	 */
	private TGPullToRefreshListView<T> pullToRefreshListView = null;
	
	/**
	 * 列表总体数据，用于显示
	 */
	private List<T> listItems = null;
	
	/**
	 * 分页数据保存List
	 */
	@SuppressLint("UseSparseArrays")
	private MPPageList<MPPage<T>> pageList = new MPPageList<MPPage<T>>();
	
	private Context context;
	
	public TGPullToRefreshDataController(Context context, TGPullToRefreshListView<T> listView)
	{
		this.context = context;
		this.setPullToRefreshListView(listView);
	}
	
	public Context getContext()
	{
		return context;
	}
	
	@Override
	public void refreshListViewAppend(List<T> items)
	{
		if(pullToRefreshListView.getPullToRefreshController().getTotalPage() >= 1)
		{
			pullToRefreshListView.getRefreshableView().setEmptyView(null);
			
			pullToRefreshListView.setPullToRefreshEnabled(true);
			
			updateListViewDataAppend(items);
		}
		else
		{
			
		}
	}
	
	/**
	 * 该方法的作用:
	 * 追加式更新列表数据
	 * @date 2014年2月10日
	 * @param items
	 */
	protected void updateListViewDataAppend(List<T> items)
	{
		//获取已显示列表数据
		getListItems();
		//更新列表数据
		updatePageList(items);
		//更新列表显示
		updateListView();
		// 调整Listview定位位置
		adjustPosition(items);
	}
	
	/**
	 * 该方法的作用: 调整Listview定位位置
	 * @date 2014年6月12日
	 * @param items
	 */
	private void adjustPosition(List<T> items) 
	{
		if((null != items) && (!items.isEmpty()))
		{
			int position;
			
			int curPullOrientation = pullToRefreshListView.getPullToRefreshController().getCurPullOrientation();
			if(curPullOrientation == IPullToRefreshController.PULL_ORIENTATION_UP)
			{
				position = listItems.size() - items.size();
				setSelection(position, true);
			}
			else
			{
				position = items.size();
				setSelection(position, false);
			}
		}
	}
	
	/**
	 * 
	 * 该方法的作用: 设置列表位置
	 * @date 2014年6月12日
	 * @param position
	 * @param isPullUp
	 */
	private void setSelection(final int position, final boolean isPullUp)
	{
		pullToRefreshListView.getRefreshableView().post(new Runnable()
		{
			public void run()
			{
				if(isPullUp)
				{
					pullToRefreshListView.getRefreshableView().setSelectionFromTop(position + 1, pullToRefreshListView.getRefreshableView().getHeight());
				}
				else
				{
					pullToRefreshListView.getRefreshableView().setSelection(position + 1);
				}
			}
		});
	}
	
	@Override
	public void refreshListViewReset(List<T> items)
	{
		if(pullToRefreshListView.getPullToRefreshController().getTotalPage() >= 1)
		{
			pullToRefreshListView.getRefreshableView().setEmptyView(null);
			
			pullToRefreshListView.setPullToRefreshEnabled(true);
			//如果数据量小于一页，列表不可拖动
			if(items.size() < pullToRefreshListView.getPullToRefreshController().getShowNumPerPage())
			{
				pullToRefreshListView.setPullToRefreshEnabled(false);
			}
			//重置列表数据
			updateListViewDataReset(items);
		}
		else
		{
			
		}
	}
	
	/**
	 * 该方法的作用:
	 * 重置列表数据
	 * @date 2014年2月10日
	 * @param items
	 */
	protected void updateListViewDataReset(List<T> items)
	{
		//重置拖动刷新控制器
		pullToRefreshListView.getPullToRefreshController().reset();
		//重置列表显示数据
		listItems = new ArrayList<T>();
		//重置列表分页数据
		pageList = new MPPageList<MPPage<T>>();
		//更新列表显示数据
		updatePageList(items);
		//更新列表显示
		updateListView();
	}
	
	/**
	 * 该方法的作用:更新列表
	 * @date 2012-8-28
	 * @param resultItems 需要更新的数据
	 */
	private void updatePageList(List<T> resultItems)
	{
		//若更新数据不为空，则更新列表
		if((null != resultItems) && (!resultItems.isEmpty()))
		{
			int curPullOrientation = pullToRefreshListView.getPullToRefreshController().getCurPullOrientation();
			if(curPullOrientation == IPullToRefreshController.PULL_ORIENTATION_UP)
			{
				updatePageListPullUp(resultItems);
			}
			else
			{
				updatePageListPullDown(resultItems);
			}
		}
		else
		{
			//清空数据
			listItems.clear();
			pageList.clear();
			pullToRefreshListView.getPullToRefreshController().setCurrentPage(1);
			Toast.makeText(getContext(), getContext().getString(CR.getStringsId(getContext(), 
					"mjet_pull_to_refresh_no_data")), Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 该方法的作用:列表上拉时更新数据
	 * @date 2012-8-28
	 * @param resultItems 需要更新的数据
	 */
	private void updatePageListPullUp(List<T> resultItems)
	{
		//删除首页数据
		removeFirstPageItems();
		
		//添加最新一页的数据
		pageList.add(convertList2ListPage(resultItems));
		
		//添加最新一页的数据
		listItems.addAll(listItems.size(),resultItems);
	}
	
	/**
	 * 该方法的作用:
	 * 将List转换为MPListPage
	 * @date 2014年3月13日
	 * @param items
	 * @return
	 */
	private MPPage<T> convertList2ListPage(List<T> items)
	{
		MPPage<T> curPage = new MPPage<T>();
		curPage.addAll(items);
		curPage.setPageNum(pullToRefreshListView.getPullToRefreshController().getCurrentPage());
		return curPage;
	}
	
	/**
	 * 该方法的作用:
	 * 删除首页数据
	 * @date 2014年3月13日
	 */
	private void removeFirstPageItems()
	{
		int currentTotalPage = pullToRefreshListView.getPullToRefreshController().getCurrentTotalPage();
		int mostShowPageNum = pullToRefreshListView.getPullToRefreshController().getShowPageMost();
		
		if((listItems.size() > 0) && (currentTotalPage >= mostShowPageNum) && 
				pageList.size() > 0)
		{
			//获取当前首页的数据
			MPPage<T> curFirstPage = pageList.get(0);
			removePageFromList(curFirstPage);
			//移除分页数据的首页数据
			pageList.remove(0);
		}
	}
	
	/**
	 * 该方法的作用:
	 * 从列表数据中删除某一页
	 * @date 2014年3月13日
	 * @param listPage
	 */
	private void removePageFromList(MPPage<T> listPage)
	{
		listItems = getListItems();
		
		T item = null;
		//移除多余的Item，删除当前首页的数据
		for(int i = 0; i < listPage.size(); i++)
		{
			item = listPage.get(i);
			if(listItems.contains(item))
			{
				listItems.remove(item);
			}
		}
	}
	
	/**
	 * 该方法的作用:列表下拉时更新列表
	 * @date 2012-8-28
	 * @param resultItems 需要更新的数据
	 */
	private void updatePageListPullDown(List<T> resultItems)
	{
		removeLastPageItems();
		
		//添加第一页的数据
		pageList.add(0, convertList2ListPage(resultItems));
		
		//列表显示数据中添加第一页数据
		listItems.addAll(0, resultItems);
	}
	
	/**
	 * 该方法的作用:
	 * 删除尾页数据
	 * @date 2014年3月13日
	 */
	private void removeLastPageItems()
	{
		int mostShowPageNum = pullToRefreshListView.getPullToRefreshController().getShowPageMost();
		
		//若显示页数大于等于最大可显示页数，则删除尾页的数据
		if(pageList.size() >= mostShowPageNum && listItems.size() > 0)
		{
			//读取最后一页的数据
			MPPage<T> lastPage = pageList.get(pageList.size() - 1);
			//移除最后一页的数据
			removePageFromList(lastPage);
			//移除分页数据的最后一页
			pageList.remove(pageList.size() - 1);
		}
	}
	
	/**
	 * 该方法的作用: 更新列表框
	 * @date 2012-8-28
	 */
	@SuppressWarnings("unchecked")
	private void updateListView()
	{
		if(null != listItems)
		{
			TGListAdapter<T> listAdapter = (TGListAdapter<T>) ((HeaderViewListAdapter)
					pullToRefreshListView.getRefreshableView().getAdapter()).getWrappedAdapter();
			listAdapter.updateListData(listItems);
		}
	}
	
	/**
	 * 该方法的作用:
	 * 获取列表填充数据
	 * @date 2013-11-1
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<T> getListItems()
	{
		TGListAdapter<T> listAdapter = (TGListAdapter<T>) ((HeaderViewListAdapter)
				pullToRefreshListView.getRefreshableView().getAdapter()).getWrappedAdapter();
		listItems = listAdapter.getListItems();
		
		if(null == listItems)
		{
			listItems = new ArrayList<T>();
		}
		
		return listItems;
	}

	@Override
	public TGPullToRefreshListView<T> getPullToRefreshListView()
	{
		return pullToRefreshListView;
	}

	/**
	 * 该方法的作用:
	 * 设置拖动刷新列表
	 * @date 2014年2月10日
	 * @param pullToRefreshListView
	 */
	public void setPullToRefreshListView(
			TGPullToRefreshListView<T> pullToRefreshListView) 
	{
		this.pullToRefreshListView = pullToRefreshListView;
	}

	@Override
	public void removeListItem(int pageNum, T item)
	{
		//移除数据
		pageList.getPage(pageNum).remove(item);
		listItems.remove(item);
		//更新列表
		pullToRefreshListView.getAdapter().updateListData(listItems);
	}
	
	@Override
	public T getListItem(int pageNum, int indexInPage)
	{
		return pageList.getPage(pageNum).get(indexInPage);
	}
	
	@Override
	public void insertListItem(int pageNum, int indexInPage, T item)
	{
		//将数据插入到分页数组
		pageList.getPage(pageNum).add(indexInPage, item);
		
		//重新初始化列表显示数据
		reInitListItems();
		
		//更新列表
		pullToRefreshListView.getAdapter().updateListData(listItems);
	}
	
	private void reInitListItems()
	{
		listItems.clear();
		int pageCount = pageList.size();
		for(int i = 0; i < pageCount; i++)
		{
			listItems.addAll(pageList.getPage(i));
		}
	}
	
	/**
	 * 该方法的作用:
	 * 获取分页数据
	 * @date 2014年3月13日
	 * @return
	 */
	public MPPageList<MPPage<T>> getPageList()
	{
		return pageList;
	}
	
	public static class MPPageList<E extends MPPage<?>> extends ArrayList<E>
	{
		/**
		 * @date 2014年3月13日
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * 当前列表中的显示页码，与实际数据页码的映射map
		 */
		@SuppressLint("UseSparseArrays")
		private HashMap<Integer, Integer> pageIndexMap = new HashMap<Integer, Integer>();
		
		@Override
		public boolean add(E object)
		{
			pageIndexMap.put(object.getPageNum(), this.size());
			return super.add(object);
		}
		
		@Override
		public void add(int index, E object)
		{
			super.add(index, object);
			//更新index后面的索引值
			for(int i = index; i < this.size(); i++)
			{
				pageIndexMap.put(this.get(i).getPageNum(), i);
			}
		}
		
		/**
		 * 该方法的作用:
		 * 根据页码读取某页的数据
		 * @date 2014年3月13日
		 * @param pageNum
		 * @return
		 */
		public E getPage(int pageNum)
		{
			return super.get(pageIndexMap.get(pageNum));
		}
	}
	
	public static class MPPage<T> extends ArrayList<T>
	{
		/**
		 * @date 2014年3月13日
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * 分页的页码
		 */
		private int pageNum = -1;

		public int getPageNum()
		{
			return pageNum;
		}

		public void setPageNum(int pageNum)
		{
			this.pageNum = pageNum;
		}
	}
}
