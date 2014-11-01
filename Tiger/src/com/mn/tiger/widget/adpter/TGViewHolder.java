package com.mn.tiger.widget.adpter;

import android.view.View;

import com.mn.tiger.utility.ViewInjector;

/**
 * 自定义的ViewHolder
 */
public abstract class TGViewHolder<T>
{
	public TGViewHolder()
	{
	}
	
	public void initView(View convertView)
	{
		ViewInjector.initInjectedView(this, convertView);
	}
	
	public abstract void fillData(T itemData, int position);
	
}
