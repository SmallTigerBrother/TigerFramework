package com.mn.tiger.widget.pulltorefresh.library.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.Toast;

import com.mn.tiger.request.TGHttpRequest;
import com.mn.tiger.request.async.TGHttpAsyncRequester;
import com.mn.tiger.request.async.TGHttpAsyncRequester.OnCancelListener;
import com.mn.tiger.request.async.TGHttpAsyncRequester.RequestListener;
import com.mn.tiger.utility.CR;
import com.mn.tiger.utility.Commons;
import com.mn.tiger.utility.LogTools;
import com.mn.tiger.widget.adpter.TGListAdapter;
import com.mn.tiger.widget.pulltorefresh.TGPullToRefreshListView;
import com.mn.tiger.widget.pulltorefresh.TGPullToRefreshListView.OnRefreshListenerPlus;
import com.mn.tiger.widget.pulltorefresh.library.model.PageModel;

/**
 * 该类作用及功能说明
 * 拖动刷新列表适配器
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2013-10-26
 */
public class TGPullToRefreshListAdapter<T> extends TGListAdapter<T> implements OnRefreshListenerPlus, 
    RequestListener<PageModel<T>>, OnCancelListener
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
	 * 列表刷新方式——追加
	 */
	public static final int REFRESH_LISTVIEW_APPEND = 1;
	
	/**
	 * 列表刷新方式——重置
	 */
	public static final int REFRESH_LISTVIEW_RESET = 2;
	
	
	/**
	 * 拖动刷新列表
	 */
	private TGPullToRefreshListView<T> listView = null;
	
	/**
	 * 请求Url
	 */
	private String requestUrl = null;
	
	/**
	 * 请求任务
	 */
	private TGHttpAsyncRequester<PageModel<T>> requester = null;
	
	/**
	 * 请求类型
	 */
	private int requestType = TGHttpRequest.REQUEST_GET;
	
	/**
	 * 请求参数
	 */
	private Object requestParams = new HashMap<String, String>();
	
	/**
	 * 当前页码的请求参数Key值
	 */
	private String currentPageKey = null;
	
	/**
	 * 列表刷新类型
	 */
	private int listViewRefreshType = REFRESH_LISTVIEW_RESET;
	
	/**
	 * @date 2013-10-26
	 * 构造函数
	 * @param context
	 * @param items 列表数据
	 * @param httpErrorHandler 错误处理接口
	 */
	public TGPullToRefreshListAdapter(Context context, List<T> items) 
	{
		super(context, items);
	}
	
	/**
	 * @date 2014年4月21日
	 * @param context
	 * @param adapter 其他Adapter
	 * @param httpErrorHandler 错误处理接口
	 */
	public TGPullToRefreshListAdapter(Context context, BaseAdapter adapter)
	{
		super(context, adapter);
	}
	
	/**
	 * 该方法的作用:
	 * 获取网络请求参数，绑定服务时，必须重载该方法
	 * @date 2013-11-12
	 * @param currentPage
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Object getRequestParams(int currentPage)
	{
		if(null != currentPageKey && null != requestParams && requestParams instanceof HashMap<?, ?>)
		{
			((HashMap<String, String>)requestParams).put(currentPageKey, currentPage + "");
		}
		return requestParams;
	}
	
	/**
	 * 该方法的作用:
	 * 执行请求任务
	 * @date 2013-10-26
	 * @param params
	 */
	public void excuteRequest(String requestUrl, Object params, int listViewRefreshType)
	{
		Commons.cancelAsyncTask(requester);
		
		this.listViewRefreshType = listViewRefreshType;
		
		if(null != requester && null != params)
		{
			switch (requestType)
			{
				case TGHttpRequest.REQUEST_GET:
					requester.get(requestUrl, PageModel.class.getName(), params, this);
					break;

				case TGHttpRequest.REQUEST_POST:
					requester.post(requestUrl, PageModel.class.getName(), params, this);
					break;

				case TGHttpRequest.REQUEST_PUT:
					requester.put(requestUrl, PageModel.class.getName(), params, this);

					break;

				case TGHttpRequest.REQUEST_DELETE:
					requester.delete(requestUrl, PageModel.class.getName(), params, this);
					
					break;

				default:
					break;
			}
		}
		else 
		{
			if(null == requester)
			{
				throw new NullPointerException("The requester should not be null");
			}
			if(null == params)
			{
				throw new NullPointerException("The request params should not be null");
			}
		}
	}
	
	/**
	 * 该方法的作用:
	 * 执行请求任务
	 * @date 2013-10-26
	 * @param params
	 */
	public void excuteRequest(TGHttpAsyncRequester<PageModel<T>> requester, String requestUrl, 
			Object params, int listViewRefreshType)
	{
		Commons.cancelAsyncTask(requester);
		
		this.listViewRefreshType = listViewRefreshType;
		
		if(null != requester && null != params)
		{
			switch (requestType)
			{
				case TGHttpRequest.REQUEST_GET:
					requester.get(requestUrl, PageModel.class.getName(), params, this);
					break;

				case TGHttpRequest.REQUEST_POST:
					requester.post(requestUrl, PageModel.class.getName(), params, this);
					break;

				case TGHttpRequest.REQUEST_PUT:
					requester.put(requestUrl, PageModel.class.getName(), params, this);

					break;

				case TGHttpRequest.REQUEST_DELETE:
					requester.delete(requestUrl, PageModel.class.getName(), params, this);
					
					break;

				default:
					break;
			}
		}
		else 
		{
			if(null == requester)
			{
				throw new NullPointerException("The requester should not be null");
			}
			if(null == params)
			{
				throw new NullPointerException("The request params should not be null");
			}
		}
	}
	
	/**
	 * 该方法的作用:
	 * 取消请求任务
	 * @date 2013-11-8
	 */
	public void cancelRequester()
	{
		Commons.cancelAsyncTask(requester);
	}
	
	/**
	 * 该方法的作用:
	 * 设置请求参数
	 * @date 2013-11-15
	 * @param params
	 */
	public void setRequestParams(Object params)
	{
		this.requestParams = params;
	}
	
	/**
	 * 该方法的作用:
	 * 绑定拖动刷新列表
	 * @date 2013-10-26
	 * @param listView
	 */
	public void bindListView(TGPullToRefreshListView<T> listView) 
	{
		this.listView = listView;
	}

	/**
	 * 该方法的作用:
	 * 获取绑定的拖动刷新列表
	 * @date 2013-10-26
	 * @return
	 */
	public TGPullToRefreshListView<T> getListView() 
	{
		return listView;
	}
	
	/**
	 * 该方法的作用:
	 * 设置请求类型
	 * @date 2013-10-26
	 * @param requestType
	 */
	public void setRequestType(int requestType)
	{
		this.requestType = requestType;
	}
	
	/**
	 * 该方法的作用:
	 * 设置当前页码请求参数的key值
	 * @date 2014年1月27日
	 * @param currentPageKey
	 */
	public void setCurrentPageKey(String currentPageKey)
	{
		this.currentPageKey = currentPageKey;
	}
	
	/**
	 * 该方法的作用:
	 * 获取当前页码请求参数的key值
	 * @date 2014年1月27日
	 * @return
	 */
	protected String getCurrentPageKey()
	{
		return currentPageKey;
	}
	
	/**
	 * 获取请求Url
	 * @return
	 */
	public String getRequestUrl()
	{
		return requestUrl;
	}
	
	/**
	 * 设置请求类
	 * @param requester
	 */
	public void setRequester(TGHttpAsyncRequester<PageModel<T>> requester)
	{
		this.requester = requester;
	}
	
	/********************************OnRefreshListenerPlus*********************************/
	//TODO OnRefreshListenerPlus
	/**
	 * 向下拖动回调方法
	 */
	@Override
	public void onPullDownToRefresh(int currentPage) 
	{
		excuteRequest(requestUrl, getRequestParams(currentPage), 
				REFRESH_LISTVIEW_APPEND);
	}
	
	/**
	 * 向上拖动回调方法
	 */
	@Override
	public void onPullUpToRefresh(int currentPage) 
	{
		excuteRequest(requestUrl, getRequestParams(currentPage), 
				REFRESH_LISTVIEW_APPEND);
	}
	
	
	/********************************OnCancelListener*********************************/
	//TODO OnCancelListener
	
	/**
	 * 请求取消的回调方法
	 */
	@Override
	public void onRequestCancel()
	{
		//停止拖动
		listView.onRefreshComplete();
	}
	
	/********************************RequestListener*********************************/
	//TODO RequestListener
	
	/**
	 * 请求启动时的回调方法
	 */
	@Override
	public void onRequestStart()
	{
		//TODO 显示对话框
	}

	/**
	 * 请求成功时的回调方法
	 */
	@Override
	public void onRequestSuccess(PageModel<T> result)
	{
		//消失对话框
		
		//设置总页数
		setTotalPage(result.getTotalPage());
		// 刷新列表
		if(listViewRefreshType == REFRESH_LISTVIEW_APPEND)
		{
			appendListData(result.getPage());
		}
		else
		{
			resetListData(result.getPage());
		}
		
		//停止拖动
		listView.onRefreshComplete();
	}

	/**
	 * 请求异常时的回调方法
	 */
	@Override
	public void onRequestError(int code, String message)
	{
		//TODO 消失对话框
		
		//停止拖动
		listView.onRefreshComplete();
		
		
		//TODO 提示异常
	}
	

	/********************************PullToRefreshController*********************************/
	//TODO PullToRefreshController
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
	
	/**
	 * 起始页码
	 */
	private int startPageNum = 1;
	
	/**
	 * 起始页码与 1 之间的差值
	 */
	private int deltaTotalPage = 0;
	
	/**
	 * 该方法的作用:
	 * 判断是否需要刷新界面
	 * @date 2013-11-1
	 * @return 无需刷新返回false，需要刷新返回true
	 */
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
	 * @date 2014年2月10日
	 * @param currentPage
	 * @return
	 */
	protected int countCurPagePullDownAfterPullUp(int currentPage) 
	{
		int curPage = currentPage - getShowPageMost();
		if(curPage < startPageNum)
		{
			Toast.makeText(getContext(), getContext().getString(CR.getStringsId(getContext(), 
					"tiger_pull_to_refresh_already_firstpage")), Toast.LENGTH_SHORT).show();
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
	 * @date 2014年2月10日
	 * @param currentPage
	 * @return
	 */
	protected int countCurPagePullDownAfterPullDown(int currentPage)
	{
		int curPage = currentPage - 1;
		
		if(curPage < startPageNum)
		{
			Toast.makeText(getContext(), getContext().getString(CR.getStringsId(getContext(), 
					"tiger_pull_to_refresh_already_firstpage")), Toast.LENGTH_SHORT).show();
			curPage++;
		}
		
		return curPage;
	}
	
	/**
	 * 该方法的作用:
	 * 判断是否需要刷新界面
	 * @date 2013-11-1
	 * @return 无需刷新返回false，需要刷新返回true
	 */
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
	 * @date 2014年2月10日
	 * @param currentPage
	 * @return
	 */
	protected int countCurPagePullUpAfterPullUp(int currentPage) 
	{
		int curPage = currentPage + 1;
		if (curPage > totalPage + deltaTotalPage) 
		{
			Toast.makeText(getContext(), getContext().getString(CR.getStringsId(
					getContext(), "tiger_pull_to_refresh_already_lastpage")),
					Toast.LENGTH_SHORT).show();
			curPage--;
		}
		
		return curPage;
	}

	/**
	 * 该方法的作用:
	 * 向下拖动后向上拖动时计算当前页码
	 * @date 2014年2月10日
	 * @param currentPage
	 * @return
	 */
	protected int countCurPagePullUpAfterPullDown(int currentPage) 
	{
		int curPage = currentPage + getShowPageMost();
		if (curPage > totalPage + deltaTotalPage) 
		{
			Toast.makeText(getContext(), getContext().getString(CR.getStringsId(
					getContext(), "tiger_pull_to_refresh_already_lastpage")), 
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
	 * @date 2014年2月10日
	 * @param currentTotalPage
	 */
	protected void setCurrentTotalPage(int currentTotalPage) 
	{
		this.currentTotalPage = currentTotalPage;
	}
	
	/**
	 * 获取当前已显示的总页数
	 * @return
	 */
	public int getCurrentTotalPage() 
	{
		return currentTotalPage;
	}
	
	/**
	 * 设置总页数
	 * @param totalPage
	 */
	public void setTotalPage(int totalPage) 
	{
		this.totalPage = totalPage;
	}

	/**
	 * 获取总页数
	 * @return
	 */
	public int getTotalPage() 
	{
		return totalPage;
	}
	
	/**
	 * 获取当前页码
	 */
	public int getCurrentPage() 
	{
		return currentPage;
	}
	
	/**
	 * 设置当前页码
	 * @param currentPage
	 */
	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}

	/**
	 * 获取允许显示的最大页数
	 * @return
	 */
	public int getShowPageMost()
	{
		return 1000;
	}

	/**
	 * 获取每页显示的行数
	 * @return
	 */
	public int getShowNumPerPage()
	{
		return 10;
	}
	
	/**
	 * 获取当前拖动方向
	 * @return
	 */
	public int getCurPullOrientation()
	{
		return curPullOrientation;
	}

	/**
	 * 该方法的作用:
	 * 设置当前拖动方向
	 * @date 2014年2月10日
	 * @param pullOrientation
	 */
	protected void setCurPullOrientation(int pullOrientation)
	{
		this.curPullOrientation = pullOrientation;
	}

	public void resetPullMetaData()
	{
		currentPage = startPageNum;
		curPullOrientation = PULL_ORIENTATION_UP;
		currentTotalPage = 0;
	}

	public int getStartPageNum() 
	{
		return startPageNum;
	}

	/**
	 * 该方法的作用:
	 * 设置其实页码
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
	 * @date 2014年2月10日
	 * @return
	 */
	protected int getDeltaTotalPage() 
	{
		return deltaTotalPage;
	}
	
	/********************************PullToRefreshDataController*********************************/
	//TODO PullToRefreshDataController
	
	/**
	 * 分页数据保存List
	 */
	@SuppressLint("UseSparseArrays")
	private TGPageList<TGPage<T>> pageList = new TGPageList<TGPage<T>>();

	/**
	 * 向列表中追加数据
	 * @param items
	 */
	public void appendListData(List<T> items)
	{
		if(getTotalPage() >= 1)
		{
			listView.getRefreshableView().setEmptyView(null);
			listView.setPullToRefreshEnabled(true);
			
			// 获取已显示列表数据
			getListItems();
			// 更新列表数据
			updatePageList(items);
			// 更新列表显示
			updateListView();
			// 调整Listview定位位置
			adjustPosition(items);
		}
	}

	/**
	 * 重置列表数据
	 * @param items
	 */
	public void resetListData(List<T> items)
	{
		if(getTotalPage() >= 1)
		{
			listView.getRefreshableView().setEmptyView(null);
			
			listView.setPullToRefreshEnabled(true);
			listView.setPullDownEnable(true);
			listView.setPullUpEnable(true);
			
			// 重置拖动刷新控制器
			resetPullMetaData();
			// 重置列表显示数据
			getListItems().clear();
			// 重置列表分页数据
			pageList.clear();;
			// 更新列表显示数据
			updatePageList(items);
			// 更新列表显示
			updateListView();
		}
	}
	
	/**
	 * 该方法的作用: 调整Listview定位位置
	 * 
	 * @date 2014年6月12日
	 * @param items
	 */
	private void adjustPosition(List<T> items)
	{
		if ((null != items) && (!items.isEmpty()))
		{
			int position;
			if (getCurPullOrientation() == PULL_ORIENTATION_UP)
			{
				position = getListItems().size() - items.size();
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
	 * 
	 * @date 2014年6月12日
	 * @param position
	 * @param isPullUp
	 */
	private void setSelection(final int position, final boolean isPullUp)
	{
		getListView().getRefreshableView().post(new Runnable()
		{
			public void run()
			{
				if (isPullUp)
				{
					getListView().getRefreshableView().setSelectionFromTop(position + 1,
							getListView().getRefreshableView().getHeight());
				}
				else
				{
					getListView().getRefreshableView().setSelection(position + 1);
				}
			}
		});
	}

	/**
	 * 该方法的作用:更新列表
	 * 
	 * @date 2012-8-28
	 * @param resultItems
	 *            需要更新的数据
	 */
	private void updatePageList(List<T> resultItems)
	{
		// 若更新数据不为空，则更新列表
		if ((null != resultItems) && (!resultItems.isEmpty()))
		{
			if (getCurPullOrientation() == PULL_ORIENTATION_UP)
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
			// 清空数据
			getListItems().clear();
			pageList.clear();
			setCurrentPage(1);
			Toast.makeText(getContext(),
					getContext().getString(CR.getStringsId(getContext(), "tiger_pull_to_refresh_no_data")),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 该方法的作用:列表上拉时更新数据
	 * 
	 * @date 2012-8-28
	 * @param resultItems
	 *            需要更新的数据
	 */
	private void updatePageListPullUp(List<T> resultItems)
	{
		// 删除首页数据
		removeFirstPageItems();

		// 添加最新一页的数据
		pageList.add(convertList2ListPage(resultItems));

		// 添加最新一页的数据
		getListItems().addAll(getListItems().size(), resultItems);
	}

	/**
	 * 该方法的作用: 将List转换为MPListPage
	 * 
	 * @date 2014年3月13日
	 * @param items
	 * @return
	 */
	private TGPage<T> convertList2ListPage(List<T> items)
	{
		TGPage<T> curPage = new TGPage<T>();
		curPage.addAll(items);
		curPage.setPageNum(getCurrentPage());
		return curPage;
	}

	/**
	 * 该方法的作用: 删除首页数据
	 * 
	 * @date 2014年3月13日
	 */
	private void removeFirstPageItems()
	{
		if ((getListItems().size() > 0) && (getCurrentTotalPage() >= getShowPageMost()) && pageList.size() > 0)
		{
			// 获取当前首页的数据
			TGPage<T> curFirstPage = pageList.get(0);
			removePageFromList(curFirstPage);
			// 移除分页数据的首页数据
			pageList.remove(0);
		}
	}

	/**
	 * 该方法的作用: 从列表数据中删除某一页
	 * 
	 * @date 2014年3月13日
	 * @param listPage
	 */
	private void removePageFromList(TGPage<T> listPage)
	{
		List<T> listItems = getListItems();

		T item = null;
		// 移除多余的Item，删除当前首页的数据
		for (int i = 0; i < listPage.size(); i++)
		{
			item = listPage.get(i);
			if (listItems.contains(item))
			{
				listItems.remove(item);
			}
		}
	}

	/**
	 * 该方法的作用:列表下拉时更新列表
	 * 
	 * @date 2012-8-28
	 * @param resultItems
	 *            需要更新的数据
	 */
	private void updatePageListPullDown(List<T> resultItems)
	{
		removeLastPageItems();

		// 添加第一页的数据
		pageList.add(0, convertList2ListPage(resultItems));

		// 列表显示数据中添加第一页数据
		getListItems().addAll(0, resultItems);
	}

	/**
	 * 该方法的作用: 删除尾页数据
	 * 
	 * @date 2014年3月13日
	 */
	private void removeLastPageItems()
	{
		// 若显示页数大于等于最大可显示页数，则删除尾页的数据
		if (pageList.size() >= getShowPageMost() && getListItems().size() > 0)
		{
			// 读取最后一页的数据
			TGPage<T> lastPage = pageList.get(pageList.size() - 1);
			// 移除最后一页的数据
			removePageFromList(lastPage);
			// 移除分页数据的最后一页
			pageList.remove(pageList.size() - 1);
		}
	}

	/**
	 * 该方法的作用: 更新列表框
	 * 
	 * @date 2012-8-28
	 */
	@SuppressWarnings("unchecked")
	private void updateListView()
	{
		if (null != getListItems())
		{
			TGListAdapter<T> listAdapter = (TGListAdapter<T>) ((HeaderViewListAdapter)getListView()
					.getRefreshableView().getAdapter()).getWrappedAdapter();
			listAdapter.updateListData(getListItems());
		}
	}

	/**
	 * 该方法的作用: 获取列表填充数据
	 * @date 2013-11-1
	 * @return
	 */
	@Override
	public List<T> getListItems()
	{
		List<T> listItems = super.getListItems();
		if (null == listItems)
		{
			listItems = new ArrayList<T>();
		}

		return listItems;
	}

	/**
	 * 删除列表数据
	 * @param pageNum 页码
	 * @param item 数据
	 */
	public void removeListItem(int pageNum, T item)
	{
		// 移除数据
		pageList.getPage(pageNum).remove(item);
		getListItems().remove(item);
		// 更新列表
		updateListData(getListItems());
	}

	/**
	 * 获取列表数据
	 * @param pageNum 页码
	 * @param indexInPage 数据在某页中的索引
	 * @return
	 */
	public T getListItem(int pageNum, int indexInPage)
	{
		return pageList.getPage(pageNum).get(indexInPage);
	}

	/**
	 * 向列表中插入数据
	 * @param pageNum 页码
	 * @param indexInPage 希望插入页中的某个索引位置
	 * @param item 数据
	 */
	public void insertListItem(int pageNum, int indexInPage, T item)
	{
		// 将数据插入到分页数组
		pageList.getPage(pageNum).add(indexInPage, item);

		// 重新初始化列表显示数据
		reInitListItems();

		// 更新列表
		updateListData(getListItems());
	}

	/**
	 * 重新初始化数据
	 */
	private void reInitListItems()
	{
		getListItems().clear();
		int pageCount = pageList.size();
		for (int i = 0; i < pageCount; i++)
		{
			getListItems().addAll(pageList.getPage(i));
		}
	}

	/**
	 * 该方法的作用: 获取分页数据
	 * 
	 * @date 2014年3月13日
	 * @return
	 */
	public TGPageList<TGPage<T>> getPageList()
	{
		return pageList;
	}

	/**
	 * 分页列表
	 * @author Dalang
	 *
	 * @param <E>
	 */
	public static class TGPageList<E extends TGPage<?>> extends ArrayList<E>
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
			// 更新index后面的索引值
			for (int i = index; i < this.size(); i++)
			{
				pageIndexMap.put(this.get(i).getPageNum(), i);
			}
		}

		/**
		 * 该方法的作用: 根据页码读取某页的数据
		 * 
		 * @date 2014年3月13日
		 * @param pageNum
		 * @return
		 */
		public E getPage(int pageNum)
		{
			return super.get(pageIndexMap.get(pageNum));
		}
	}

	/**
	 * 分页数据
	 * @author Dalang
	 *
	 * @param <T>
	 */
	public static class TGPage<T> extends ArrayList<T>
	{
		/**
		 * @date 2014年3月13日
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 分页的页码
		 */
		private int pageNum = -1;

		/**
		 * 获取页码
		 * @return
		 */
		public int getPageNum()
		{
			return pageNum;
		}

		/**
		 * 设置页码
		 * @param pageNum
		 */
		public void setPageNum(int pageNum)
		{
			this.pageNum = pageNum;
		}
	}
	
}
