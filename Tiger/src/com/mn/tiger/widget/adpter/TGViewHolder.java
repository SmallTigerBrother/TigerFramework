package com.mn.tiger.widget.adpter;

import android.util.SparseArray;
import android.view.View;

/**
 * 自定义的ViewHolder
 */
public class TGViewHolder
{
	/**
	 * 根据id获取子view
	 * @param convertView 列表行view
	 * @param id 子view的ID号
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View convertView, int id)
	{
		SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
		if (viewHolder == null)
		{
			viewHolder = new SparseArray<View>();
			convertView.setTag(viewHolder);
		}
		
		View childView = viewHolder.get(id);
		if (childView == null)
		{
			childView = convertView.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}
}
