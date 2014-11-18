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
	
	public View initView(View convertView)
	{
		ViewInjector.initInjectedView(this, convertView);
		return convertView;
	}
	
	public abstract void fillData(T itemData, int position);
	
}
