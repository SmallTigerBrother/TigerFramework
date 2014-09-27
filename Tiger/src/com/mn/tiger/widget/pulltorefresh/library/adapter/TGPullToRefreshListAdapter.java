package com.mn.tiger.widget.pulltorefresh.library.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;

import com.mn.tiger.request.TGHttpRequester;
import com.mn.tiger.request.async.TGHttpAsyncRequester;
import com.mn.tiger.request.async.TGHttpAsyncRequester.TGRequestHandler;
import com.mn.tiger.utility.Commons;
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
public class TGPullToRefreshListAdapter<T> extends TGListAdapter<T> implements OnRefreshListenerPlus
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
	private int requestType = TGHttpRequester.REQUEST_UNKNOWN;
	
	/**
	 * 列表刷新类型
	 */
	private int listViewRefreshType = REFRESH_LISTVIEW_RESET;
	
	/**
	 * 请求结构回调方法
	 */
	private TGRequestHandler<PageModel<T>> requestHandler = new TGRequestHandler<PageModel<T>>()
	{
		public void onRequestStart() 
		{
			TGPullToRefreshListAdapter.this.onRequestStart();
		};
		
		@Override
		public void onRequestSuccess(PageModel<T> result)
		{
			TGPullToRefreshListAdapter.this.onRequestSuccess(result);
		}

		@Override
		public void onRequestError(int code, String message)
		{
			TGPullToRefreshListAdapter.this.onRequestError(code, message);
		}
		
		public void onReturnCachedResult(PageModel<T> result) 
		{
			TGPullToRefreshListAdapter.this.onReturnCachedResult(result);
		};
		
		public void onRequestCancel() 
		{
			TGPullToRefreshListAdapter.this.onRequestCancel();
		};
	};
	
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
	protected HashMap<String, String> getRequestParams(int currentPage)
	{
		return null;
	}
	
	/**
	 * 该方法的作用:
	 * 执行请求任务
	 * @date 2013-10-26
	 * @param params
	 */
	public void excuteRequest(String requestUrl, HashMap<String, String> params, int listViewRefreshType)
	{
		requester.setRequestParams(params);
		excuteRequest(requester, requestUrl,listViewRefreshType);
	}
	
	/**
	 * 该方法的作用:
	 * 执行请求任务
	 * @date 2013-10-26
	 * @param params
	 */
	public void excuteRequest(TGHttpAsyncRequester<PageModel<T>> requester, String requestUrl,int listViewRefreshType)
	{
		Commons.cancelAsyncTask(requester);
		
		this.listViewRefreshType = listViewRefreshType;
		
		switch (requestType)
		{
			case TGHttpRequester.REQUEST_GET:
				requester.get(requestUrl, PageModel.class, requestHandler);
				break;

			case TGHttpRequester.REQUEST_POST:
				requester.post(requestUrl, PageModel.class, requestHandler);
				break;

			case TGHttpRequester.REQUEST_PUT:
				requester.put(requestUrl, PageModel.class, requestHandler);

				break;

			case TGHttpRequester.REQUEST_DELETE:
				requester.delete(requestUrl, PageModel.class, requestHandler);
				
				break;

			default:
				throw new RuntimeException("You must set requestType before use this method");
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
		//想下拖动重置刷新列表
		excuteRequest(requestUrl, getRequestParams(currentPage), 
				REFRESH_LISTVIEW_RESET);
	}
	
	/**
	 * 向上拖动回调方法
	 */
	@Override
	public void onPullUpToRefresh(int currentPage) 
	{
		//向上拖动追加列表
		excuteRequest(requestUrl, getRequestParams(currentPage), 
				REFRESH_LISTVIEW_APPEND);
	}
	
	
	/********************************OnCancelListener*********************************/
	
	/**
	 * 请求启动时的回调方法
	 */
	public void onRequestStart()
	{
		//TODO 显示对话框
	}
	
	/**
	 * 请求成功时的回调方法
	 */
	public void onRequestSuccess(PageModel<T> result)
	{
		//消失对话框
		
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
	public void onRequestError(int code, String message)
	{
		//TODO 消失对话框
		
		//停止拖动
		listView.onRefreshComplete();
		
		
		//TODO 提示异常
	}
	
	public void onReturnCachedResult(PageModel<T> result)
	{
	}
	//TODO OnCancelListener
	
	/**
	 * 请求取消的回调方法
	 */
	public void onRequestCancel()
	{
		//停止拖动
		listView.onRefreshComplete();
	}
	
	/********************************PullToRefreshController*********************************/
	//TODO PullToRefreshController
	/**
	 * 当前页码
	 */
	private int currentPage = 1;
	
	/**
	 * 起始页码
	 */
	private int startPageNum = 1;
	
	/**
	 * 起始页码与 1 之间的差值
	 */
	private int deltaTotalPage = 0;
	
	private int showNumPerPage = 10;
	
	/**
	 * 该方法的作用:
	 * 向上拖动时计算当前页码
	 * @date 2014年2月10日
	 * @return
	 */
	public int countCurPagePullUp()
	{
		return currentPage++;
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
	 * 获取每页显示的行数
	 * @return
	 */
	public int getShowNumPerPage()
	{
		return showNumPerPage;
	}
	
	public void setShowNumPerPage(int showNumPerPage)
	{
		this.showNumPerPage = showNumPerPage;
	}
	
	public void resetPullMetaData()
	{
		currentPage = startPageNum;
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
		listView.getRefreshableView().setEmptyView(null);
		
		// 获取已显示列表数据
		getListItems();
		// 更新列表数据
		updatePageList(items);
		// 更新列表显示
		updateListView();
		// 调整Listview定位位置
		adjustPosition(items);
	}

	/**
	 * 重置列表数据
	 * @param items
	 */
	public void resetListData(List<T> items)
	{
		listView.getRefreshableView().setEmptyView(null);
		
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
			position = items.size();
			setSelection(position, false);
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
