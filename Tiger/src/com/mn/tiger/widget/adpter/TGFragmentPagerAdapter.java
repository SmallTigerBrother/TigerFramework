package com.mn.tiger.widget.adpter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

public class TGFragmentPagerAdapter extends FragmentPagerAdapter
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 所有页面
	 */
	private ArrayList<Fragment> pagers;
	
	public TGFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> pagers)
	{
		super(fm);
		this.pagers = new ArrayList<Fragment>();
		if(null != pagers)
		{
			this.pagers.addAll(pagers);
		}
	}

	@Override
	public Fragment getItem(int page)
	{
		return pagers.get(page);
	}

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
	 * 获取页面的位置
	 */
	@Override
	public int getItemPosition(Object object) 
	{
		return POSITION_NONE;
	}
}
