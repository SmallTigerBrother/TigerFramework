package com.mn.tiger.widget.adpter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
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
	
	private int convertViewLayoutId;
	
	private Class<? extends TGViewHolder<T>> viewHolderClass;
	
	/**
	 * 
	 * @param context
	 * @param items 列表填充数据
	 * @param viewHolderClass ViewHolder类名
	 */
	public TGListAdapter(Context context, List<T> items,int convertViewLayoutId, 
			Class<? extends TGViewHolder<T>> viewHolderClass)
	{
		this.context = context;
		this.items = new ArrayList<T>();
		if(null != items)
		{
			items.addAll(items);
		}
		
		this.viewHolderClass = viewHolderClass;
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
		
		return items.size();
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
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(null != adapter)
		{
			return adapter.getView(position, convertView, parent);
		}
		
		TGViewHolder<T> viewHolder = null;
		if(null == convertView)
		{
			try
			{
				convertView = LayoutInflater.from(getContext()).inflate(convertViewLayoutId, null);
				viewHolder = initViewHolder();
				viewHolder.initView(convertView);
				convertView.setTag(viewHolder);
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
		else
		{
			viewHolder = (TGViewHolder<T>) convertView.getTag();
		}
		
		viewHolder.fillData(items.get(position), position);
		
		return convertView;
	}

	/**
	 * 初始化ViewHolder
	 * @return
	 */
	protected TGViewHolder<T> initViewHolder()
	{
		TGViewHolder<T> viewHolder = null;
		try
		{
			viewHolder = viewHolderClass.newInstance();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		
		return viewHolder;
	}
	
	/**
	 * 该方法的作用:更新列表数据
	 * @date 2013-1-17
	 * @param data 列表数据
	 */
	public void updateData(List<T> data)
	{
		if(null != data)
		{
			this.items.clear();
			this.items.addAll(data);
			notifyDataSetChanged();
		}
	}
	
	public void updateData(T[] data)
	{
		if(null != data)
		{
			this.items.clear();
			this.items.addAll(Arrays.asList(data));
			notifyDataSetChanged();
		}
	}
	
	public void appendData(List<T> data)
	{
		if(null != data)
		{
			this.items.addAll(data);
			notifyDataSetChanged();
		}
	}
	
	public void appendData(T[] data)
	{
		if(null != data)
		{
			this.items.addAll(Arrays.asList(data));
			notifyDataSetChanged();
		}
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
