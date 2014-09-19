package com.mn.tiger.widget.dropdown;

import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;

/**
 * 该类作用及功能说明
 * 下拉列表基类，实现下拉列表的基本功能
 * @see JDK1.6,android-8
 * @date 2012-9-25
 */
public abstract class TGDropDownAdapterView extends TGDropDownView
{
    public TGDropDownAdapterView(Context context) 
    {
		super(context);
	}

	/**
     * 该方法的作用:设置Item点击事件
     * @date 2012-9-25
     * @param listener
     */
	public abstract void setOnItemClickListener(OnItemClickListener listener);
	
	/**
	 * 该方法的作用: 设置适配器
	 * @date 2013-1-11
	 * @param adapter 适配器
	 */
	public abstract void setAdapter(ListAdapter adapter);
	
	/**
	 * 该方法的作用:设置选中项
	 * @date 2012-9-25
	 * @param position 选中项的位置
	 */
	public abstract void setSelection(int position);
}
