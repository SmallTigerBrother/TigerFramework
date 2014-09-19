package com.mn.tiger.widget.adpter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * 该类作用及功能说明
 * 自定义的PagerAdapter
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public class TGPagerAdapter extends PagerAdapter
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 所有页面
	 */
	protected ArrayList<View> pagers;
	
	/**
	 * @date 2013-4-2
	 * 构造函数
	 * @param views 所有页面
	 */
	public TGPagerAdapter(ArrayList<View> views)
	{
		this.pagers = views;
	}
	
	/**
	 * 初始化各个页面
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) 
	{
		((ViewPager)container).addView(pagers.get(position));
		return pagers.get(position);
	}

	/**
	 * 获取页面个数
	 */
	@Override
	public int getCount()
	{
		return pagers.size();
	}

	/**
	 * 页面是否从Object生成的
	 */
	@Override
	public boolean isViewFromObject(View view, Object object) 
	{
		return view == object;
	}
	
	/**
	 * 销毁页面
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) 
	{
		((ViewPager)container).removeView(pagers.get(position));
	}
	
	/**
	 * 获取页面的位置
	 */
	@Override
	public int getItemPosition(Object object) 
	{
		return POSITION_NONE;
	}
	
	/**
	 * 该方法的作用:更新所有页面
	 * @date 2013-4-2
	 * @param pagers 新页面列表
	 */
	public void updatePagers(ArrayList<View> pagers)
	{
		this.pagers = pagers;
		this.notifyDataSetChanged();
	}
}
