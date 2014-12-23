package com.mn.tiger.widget.adpter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.mn.tiger.utility.ViewInjector;

public abstract class TGExpandableGroupViewHolder<T>
{
	private Activity activity;
	
	/**
	 * 搭配使用的Adapter
	 */
	@SuppressWarnings("rawtypes")
	private TGExpandableListAdapter adapter;
	
	public TGExpandableGroupViewHolder()
	{
	}
	
	/**
	 * 初始化列表行视图
	 * @param convertView
	 * @return 需要返回convertView
	 */
	public View initView(View convertView)
	{
		ViewInjector.initInjectedView(this, convertView);
		return convertView;
	}
	
	/**
	 * 更新列表行的尺寸
	 * @param itemData
	 * @param position
	 */
	public void updateViewDimension(T itemData, int groupPosition, boolean isExpanded, 
			ViewGroup parent)
	{
		
	}
	
	/**
	 * 填充数据
	 * @param itemData
	 * @param position
	 */
	public abstract void fillData(T itemData, int groupPosition, boolean isExpanded);

	public Activity getActivity()
	{
		return activity;
	}

	public void setActivity(Activity activity)
	{
		this.activity = activity;
	}

	@SuppressWarnings("rawtypes")
	public TGExpandableListAdapter getAdapter()
	{
		return adapter;
	}

	@SuppressWarnings("rawtypes")
	void setAdapter(TGExpandableListAdapter adapter)
	{
		this.adapter = adapter;
	}
}
