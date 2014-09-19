package com.mn.tiger.widget.pulltorefresh.library.request;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.mn.tiger.request.async.TGAsyncTask;
import com.mn.tiger.request.error.IHttpErrorHandler;
import com.mn.tiger.widget.pulltorefresh.pullinterface.IPullToRefreshDataController;

/**
 * 该类作用及功能说明
 * 拖动刷新列表网络请求任务
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2013-10-26
 */
public abstract class TGPullToRefreshTask<Result> extends TGAsyncTask<Result> 
{
	/**
	 * 总页数，必须在子类中赋值
	 */
	private int totalPage = 0;
	
	/**
	 * 列表更新方式
	 */
	private int listViewRefreshType = IPullToRefreshDataController.REFRESH_LISTVIEW_RESET;

	/**
	 * @see BaseAsyncTask#BaseAsyncTask(Context, String, ErrorInterface, Handler, int)
	 */
	public TGPullToRefreshTask(Context context, String requestUrl,
			IHttpErrorHandler httpErrorHandler, Handler handler, int requestType) 
	{
		super(context, requestUrl, httpErrorHandler, handler, requestType);
	}
	
	@Override
	protected void sendMessage(Result result)
	{
		Handler handler = getHandler();
		if(null != handler)
		{
			Message msg = new Message();
			msg.what = getMessageWhat();
			msg.obj = getRequestResult();
			//arg1设置为总页数
			msg.arg1 = totalPage;
			//arg2设置为列表刷新类型
			msg.arg2 = listViewRefreshType;
			
			handler.sendMessage(msg);
		}
	}
	
	/**
	 * 该方法的作用:
	 * 设置列表刷新方式
	 * @date 2013-10-26
	 * @param listViewRefreshType
	 */
	public void setListViewRefreshType(int listViewRefreshType) 
	{
		this.listViewRefreshType = listViewRefreshType;
	}

	/**
	 * 该方法的作用:
	 * 获取列表刷新方式
	 * @date 2013-10-26
	 * @return
	 */
	public int getListViewRefreshType() 
	{
		return listViewRefreshType;
	}
	
	/**
	 * 该方法的作用:
	 * 设置总页数
	 * @date 2014年2月10日
	 * @param totalPage
	 */
	public void setTotalPage(int totalPage)
	{
		this.totalPage = totalPage;
	}
	
	/**
	 * 该方法的作用:
	 * 获取总页数
	 * @date 2014年2月10日
	 * @return
	 */
	public int getTotalPage()
	{
		return totalPage;
	}
}
