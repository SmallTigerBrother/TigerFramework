package com.mn.tiger.widget.adpter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mn.tiger.utility.ViewInjector;

public abstract class TGGridListViewHolder<T> extends TGViewHolder<T>
{
	@Override
	public View initView(View convertView, ViewGroup parent)
	{
		int columnNum = getColumnNum();
		
		LinearLayout linearLayout = new LinearLayout(getActivity());
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		
		View childGridView;
		for(int i = 0; i < columnNum; i++)
		{
			childGridView = initChildGridView(i, linearLayout, parent);
			linearLayout.addView(childGridView, layoutParams);
		}
		
		return linearLayout;
	}
	
	protected abstract View initChildGridView(int columIndex , 
			LinearLayout rowLayout, ViewGroup parent);
	
	@Override
	public void fillData(ViewGroup parent, View convertView, T itemData, int position)
	{
		int columnNum = getColumnNum();
		List<T> allItems = getAdapter().getListItems();
		int allItemsCount = allItems.size();
		
		LinearLayout rowLayout = (LinearLayout) convertView;
		View childGridView;
		int childDataIndex = 0;
		for(int i = 0; i< columnNum; i++)
		{
			childGridView = rowLayout.getChildAt(i);
			childDataIndex = position * columnNum + i;
			if(childDataIndex < allItemsCount)
			{
				childGridView.setVisibility(View.VISIBLE);
				childGridView.setEnabled(true);
				ViewInjector.initInjectedView(this, childGridView);
				this.fillData(i, position, itemData, childGridView, rowLayout, parent);
			}
			else
			{
				childGridView.setVisibility(View.INVISIBLE);
				childGridView.setEnabled(false);
			}
		}
	}
	
	protected abstract void fillData(int rowIndex, int columnIndex, T itemData,
			View childGridView, LinearLayout rowLayout, ViewGroup parent);
	
	@Override
	protected void updateViewDimension(ViewGroup parent, View convertView, T itemData,
			int position)
	{
		int columnNum = getColumnNum();
		List<T> allItems = getAdapter().getListItems();
		int allItemsCount = allItems.size();
		
		LinearLayout rowLayout = (LinearLayout) convertView;
		View childGridView;
		int childDataIndex = 0;
		for(int i = 0; i < columnNum; i++)
		{
			childGridView = rowLayout.getChildAt(i);
			childDataIndex = position * columnNum + i;
			if(childDataIndex < allItemsCount)
			{
				updateViewDimension(parent, (LinearLayout) convertView, 
						childGridView, allItems.get(childDataIndex), i, position);
			}
		}
	}
	
	protected void updateViewDimension(ViewGroup parent, LinearLayout rowLayout, 
			View childGridView, T itemData, int rowIndex, int columnIndex)
	{
		
	}
	
	protected abstract int getColumnNum();
}
