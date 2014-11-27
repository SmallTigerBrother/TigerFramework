package com.mn.tiger.widget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ScrollPageView extends ScrollView implements IScrollable
{
	private boolean canScrollDown = true;
	
	private boolean canScrollUp = true;
	
	private boolean canScrollLeft = false;
	
	private boolean canScrollRight = false;
	
	public ScrollPageView(Context context)
	{
		super(context);
	}
	
	public ScrollPageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public ScrollPageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt)
	{
		super.onScrollChanged(l, t, oldl, oldt);
		
		if(getScrollY() + getHeight() >= computeVerticalScrollRange() - 5)
		{
			canScrollDown = false;
		} 
		else
		{
			canScrollDown = true;
		}
	}

	@Override
	public boolean canScrollLeft()
	{
		return canScrollLeft;
	}

	@Override
	public boolean canScrollRight()
	{
		return canScrollRight;
	}

	@Override
	public boolean canScrollUp()
	{
		return canScrollUp;
	}

	@Override
	public boolean canScrollDown()
	{
		return canScrollDown;
	}
}
