package com.mn.tiger.widget.dropdown;

import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;

/**
 * 该类作用及功能说明
 * 下拉列表基类，实现下拉列表的基本功能
 * @author l00220455
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2012-9-25
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public abstract class DropDownAdapterView extends DropDownView
{
    public DropDownAdapterView(Context context) 
    {
		super(context);
	}

	/**
     * 该方法的作用:设置Item点击事件
     * @author l00220455
     * @date 2012-9-25
     * @param listener
     */
	public abstract void setOnItemClickListener(OnItemClickListener listener);
	
	/**
	 * 该方法的作用: 设置适配器
	 * @author l00220455
	 * @date 2013-1-11
	 * @param adapter 适配器
	 */
	public abstract void setAdapter(ListAdapter adapter);
	
	/**
	 * 该方法的作用:设置选中项
	 * @author l00220455
	 * @date 2012-9-25
	 * @param position 选中项的位置
	 */
	public abstract void setSelection(int position);
}
