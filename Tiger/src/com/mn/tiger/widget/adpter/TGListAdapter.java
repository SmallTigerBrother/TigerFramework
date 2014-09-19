package com.mn.tiger.widget.adpter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 该类作用及功能说明
 * 基础列表适配器类，实现适配器的基本功能
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2012-12-31
 */
public class TGListAdapter<T> extends BaseAdapter 
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 运行环境
	 */
	private Context context;
	
	/**
	 * 列表填充数据
	 */
	private List<T> items = null;
	
	private BaseAdapter adapter = null;
	
	/**
	 * @date 2012-12-28
	 * 构造函数
	 * @param context 运行环境
	 * @param items 列表填充数据
	 */
	public TGListAdapter(Context context, List<T> items)
	{
		this.context = context;
		this.items = items;
		if(null == items)
		{
			 this.items = new ArrayList<T>();
		}
	}
	
	/**
	 * @date 2014年4月21日
	 * @param context
	 * @param adapter 其他Adapter
	 */
	public TGListAdapter(Context context, BaseAdapter adapter)
	{
		this.context = context;
		this.adapter = adapter;
	}
	
	/**
	 * @see BaseAdapter#getCount()
	 */
	@Override
	public int getCount() 
	{
		if(null != adapter)
		{
			return adapter.getCount();
		}
		
		if(null != items)
		{
			return items.size();
		}
		return 0;
	}

	/**
	 * @see BaseAdapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) 
	{
		if(null != adapter)
		{
			return adapter.getItem(position);
		}
		return items.get(position);
	}

	/**
	 * @see BaseAdapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) 
	{
		if(null != adapter)
		{
			return adapter.getItemId(position);
		}
		return position;
	}

	
	/**
	 * @see BaseAdapter#getView(int, View, ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(null != adapter)
		{
			return adapter.getView(position, convertView, parent);
		}
		
		return null;
	}

	/**
	 * 该方法的作用:更新列表数据
	 * @date 2013-1-17
	 * @param data 列表数据
	 */
	public void updateListData(List<T> data)
	{
		if(null != data)
		{
			this.items = data;
			notifyDataSetChanged();
		}
	}
	
	/**
	 * 该方法的作用:
	 * 设置列表数据
	 * @date 2014年2月10日
	 * @param items
	 */
	protected void setItems(List<T> items)
	{
		this.items = items;
	}
	
	/**
	 * 该方法的作用:
	 * 获取列表数据
	 * @date 2014年2月10日
	 * @return
	 */
	public List<T> getListItems()
	{
		return this.items;
	}
	
	public BaseAdapter getDecoratedAdapter()
	{
		return adapter;
	}
	
	/**
	 * 该方法的作用:
	 * 设置Context
	 * @date 2014年2月10日
	 * @param context
	 */
	public void setContext(Context context)
	{
		this.context = context;
	}
	
	/**
	 * 该方法的作用:
	 * 获取Context
	 * @date 2014年2月10日
	 * @return
	 */
	protected Context getContext()
	{
		return context;
	}
}
