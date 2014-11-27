package com.mn.tiger.widget.adpter;

import android.app.Activity;
import android.view.View;

import com.mn.tiger.utility.ViewInjector;

/**
 * 自定义的ViewHolder
 */
public abstract class TGViewHolder<T>
{
	private Activity activity;
	
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
	public View initView(View convertView)
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
	 * 填充数据
	 * @param itemData
	 * @param position
	 */
	public abstract void fillData(T itemData, int position);

	public Activity getActivity()
	{
		return activity;
	}

	public void setActivity(Activity activity)
	{
		this.activity = activity;
	}

}
