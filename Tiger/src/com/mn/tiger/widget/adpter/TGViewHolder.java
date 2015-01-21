package com.mn.tiger.widget.adpter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.mn.tiger.utility.ViewInjector;

/**
 * 自定义的ViewHolder
 */
public abstract class TGViewHolder<T>
{
	private Activity activity;
	
	/**
	 * 列表航layoutID
	 */
	private int layoutId;
	
	/**
	 * 搭配使用的Adapter
	 */
	private TGListAdapter<T> adapter;
	
	public TGViewHolder()
	{
	}
	
	/**
	 * 初始化列表行视图
	 * @param convertView
	 * @return 需要返回convertView
	 */
	public View initView(View convertView, ViewGroup parent)
	{
		ViewInjector.initInjectedView(this, convertView);
		return convertView;
	}
	
	/**
	 * 获取匹配的Adapter
	 * @return
	 */
	public TGListAdapter<T> getAdapter()
	{
		return adapter;
	}

	/**
	 * 设置匹配的Adapter
	 * @param adapter
	 */
	void setAdapter(TGListAdapter<T> adapter)
	{
		this.adapter = adapter;
	}
	
	/**
	 * 更新列表行的尺寸
	 * @param itemData
	 * @param position
	 */
	protected void updateViewDimension(ViewGroup parent, View convertView, T itemData, int position)
	{
		
	}
	
	/**
	 * 填充数据
	 * @param itemData
	 * @param position
	 */
	public abstract void fillData(ViewGroup parent, View convertView, T itemData, int position);

	protected Activity getActivity()
	{
		return activity;
	}

	void setActivity(Activity activity)
	{
		this.activity = activity;
	}
	
	void setLayoutId(int layoutId)
	{
		this.layoutId = layoutId;
	}
	
	protected int getLayoutId()
	{
		return layoutId;
	}

}
