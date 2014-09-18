package com.mn.tiger.widget.tab;

import java.util.ArrayList;

import android.content.Context;
import android.widget.BaseAdapter;

/**
 * TabView自定义Adapter
 */
public abstract class MPTabViewAdapter<T> extends BaseAdapter
{
	/**
	 * 所有items
	 */
	private ArrayList<T> items;
	
	private Context context;
	
	public MPTabViewAdapter(Context context, ArrayList<T> items)
	{
		this.context = context;
		
		if(null != items)
		{
			this.items = items;
		}
		else 
		{
			items = new ArrayList<T>();
		}
	}
	
	@Override
	public int getCount()
	{
		return items.size();
	}

	@Override
	public Object getItem(int position)
	{
		return items.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	/**
	 * 更新数据
	 * @param items 新数据
	 */
	public void updateData(ArrayList<T> items)
	{
		if(null != items)
		{
			this.items = items;
		}
		else 
		{
			items = new ArrayList<T>();
		}
		
		notifyDataSetChanged();
	}
	
	public Context getContext()
	{
		return context;
	}
	
	/**
	 * 获取所有items
	 * @return 所有items
	 */
	public ArrayList<T> getItems()
	{
		return items;
	}
}
