package com.mn.tiger.widget.adpter;

import android.content.Context;
import android.view.View;

import com.mn.tiger.utility.ViewInjector;

/**
 * 自定义的ViewHolder
 */
public abstract class TGViewHolder<T>
{
	private Context context;
	
	/**
	 * 搭配使用的Adapter
	 */
	private TGListAdapter<T> adapter;
	
	public TGViewHolder()
	{
	}
	
	void setContext(Context context)
	{
		this.context = context;
	}
	
	public Context getContext()
	{
		return context;
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
	
	public TGListAdapter<T> getAdapter()
	{
		return adapter;
	}

	public void setAdapter(TGListAdapter<T> adapter)
	{
		this.adapter = adapter;
	}
	
	/**
	 * 填充数据
	 * @param itemData
	 * @param position
	 */
	public abstract void fillData(T itemData, int position);

}
