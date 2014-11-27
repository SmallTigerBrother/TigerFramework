package com.mn.tiger.widget.jazzyviewpager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.mn.tiger.widget.viewpager.TGPagerAdapter;

import android.view.View;
import android.view.ViewGroup;


public class JazzyPagerAdapter extends TGPagerAdapter
{
	private HashMap<Integer, Object> mObjs = new LinkedHashMap<Integer, Object>();
	
	private JazzyViewPager viewPager = null;
	
	public JazzyPagerAdapter(ArrayList<View> views)
	{
		super(views);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		Object object = super.instantiateItem(container, position);
		setObjectForPosition(object, position);
		
		return object;
	}
	
	@Override
	public boolean isViewFromObject(View view, Object obj)
	{
		if (view instanceof OutlineContainer)
		{
			return ((OutlineContainer) view).getChildAt(0) == obj;
		}
		else
		{
			return view == obj;
		}
	}
	
	public void setObjectForPosition(Object obj, int position)
	{
		mObjs.put(Integer.valueOf(position), obj);
	}
	
	public View findViewFromObject(int position)
	{
		Object o = mObjs.get(Integer.valueOf(position));
		if (o == null)
		{
			return null;
		}
		
		View v;
		for (int i = 0; i < viewPager.getChildCount(); i++)
		{
			v = viewPager.getChildAt(i);
			if (isViewFromObject(v, o))
			{
				return v;
			}
		}
		return null;
	}

	public void bindViewPager(JazzyViewPager viewPager)
	{
		this.viewPager = viewPager;
	}

}
