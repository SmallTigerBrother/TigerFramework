package com.mn.tiger.widget.adpter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 该类作用及功能说明
 * 基础可展开列表适配器类，GParam参数为Group数据，CParam参数为Child数据
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2012-12-28
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public abstract class TGExpandableListAdapter<GParam,CParam> extends android.widget.BaseExpandableListAdapter 
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 运行环境
	 */
	protected Context context;
	/**
	 * Group数据
	 */
	protected List<GParam> groups;
	/**
	 * Child数据
	 */
	protected List<List<CParam>> childs;

	/**
	 * @date 2012-12-28
	 * 构造函数
	 * @param context 运行环境
	 * @param groups Group数据
	 * @param childs Child数据
	 */
	public TGExpandableListAdapter(Context context, List<GParam> groups, List<List<CParam>> childs)
	{
		this.context = context;
		this.groups = groups;
		this.childs = childs;
	}
	
	/**
	 * @see BaseExpandableListAdapter#getChild(int, int);
	 */
	@Override
	public Object getChild(int groupPosition, int childPosition) 
	{
		return childs.get(groupPosition).get(childPosition);
	}

	/**
	 * @see BaseExpandableListAdapter#getChildId(int, int);
	 */
	@Override
	public long getChildId(int groupPosition, int childPosition) 
	{
		return childPosition;
	}

	/**
	 * @see BaseExpandableListAdapter#getChildView(int, int, boolean, View, ViewGroup);
	 */
	@Override
	public abstract View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent); 

	/**
	 * @see BaseExpandableListAdapter#getChildrenCount(int);
	 */
	@Override
	public int getChildrenCount(int groupPosition) 
	{
		if(null == childs)
		{
			return 0;
		}
		return childs.get(groupPosition).size();
	}

	/**
	 * @see BaseExpandableListAdapter#getGroup(int);
	 */
	@Override
	public Object getGroup(int groupPosition) 
	{
		return groups.get(groupPosition);
	}

	/**
	 * @see BaseExpandableListAdapter#getGroupCount();
	 */
	@Override
	public int getGroupCount() 
	{
		if(null == groups)
		{
			return 0;
		}
		return groups.size();
	}

	/**
	 * @see BaseExpandableListAdapter#getGroupId(int);
	 */
	@Override
	public long getGroupId(int groupPosition) 
	{
		return groupPosition;
	}

	/**
	 * @see BaseExpandableListAdapter#getGroupView(int, boolean, View, ViewGroup);
	 */
	@Override
	public abstract View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent);

	/**
	 * @see BaseExpandableListAdapter#hasStableIds();
	 */
	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	/**
	 * @see BaseExpandableListAdapter#isChildSelectable(int, int);
	 */
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return true;
	}

}
