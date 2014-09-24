package com.mn.tiger.widget;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.mn.tiger.utility.LogTools;
import com.mn.tiger.widget.adpter.TGPagerAdapter;

public class TGBannerViewPager extends ViewPager
{
	private int currentPageNum = -1;

	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			setCurrentItem(msg.what);
		};
	};
	
	private boolean isContinue = true;
	
	private boolean isSrolling = false;
	
	private int duration = 2000;
	
	private Thread thread = new Thread()
	{
		public void run() 
		{
			while (isContinue && isSrolling)
			{
				currentPageNum++;
				handler.obtainMessage(currentPageNum).sendToTarget();
				try
				{
					Thread.sleep(duration);
				}
				catch (InterruptedException e)
				{
					LogTools.d(VIEW_LOG_TAG, e.getMessage(), e);
				}
			}
		};
	};
	
	public TGBannerViewPager(Context context)
	{
		super(context);
		setListeners();
	}

	public TGBannerViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setListeners();
	}
	
	private void setListeners()
	{
		setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
					case MotionEvent.ACTION_DOWN:
					case MotionEvent.ACTION_MOVE:
						isContinue = false;
						break;
					case MotionEvent.ACTION_UP:
						isContinue = true;
						break;
					default:
						isContinue = true;
						break;
				}
				return false;
			}
		});
		
		setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int arg0)
			{
				currentPageNum = arg0;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				
			}
		});
	}
	
	@Override
	public void setAdapter(PagerAdapter adapter)
	{
		super.setAdapter(adapter);
		thread.start();
	}
	
	public void startScroll()
	{
		isSrolling = true;
		
		if(currentPageNum == -1)
		{
			currentPageNum = ((TGPagerAdapter)getAdapter()).getPagers().size() * 100 - 1;
		}
		
		setCurrentItem(currentPageNum);
	}
	
	@Override
	public void setCurrentItem(int item)
	{
		if(item < 100)
		{
			currentPageNum = item + ((TGPagerAdapter)getAdapter()).getPagers().size() * 100 - 1;
		}
		super.setCurrentItem(currentPageNum);
	}
	
	@Override
	public void setCurrentItem(int item, boolean smoothScroll)
	{
		if(item < 100)
		{
			currentPageNum = item + ((TGPagerAdapter)getAdapter()).getPagers().size() * 100 - 1;
		}
		
		super.setCurrentItem(currentPageNum, smoothScroll);
	}
	
	public void stopScroll()
	{
		isSrolling = false;
	}
	
	public void setSrollDuration(int duration)
	{
		this.duration = duration;
	}
	
	public static class CirclePagerAdapter extends TGPagerAdapter
	{
		public CirclePagerAdapter(ArrayList<View> views)
		{
			super(views);
		}
		
		@Override
		public int getCount()
		{
			return Integer.MAX_VALUE;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			super.destroyItem(container, position % getPagers().size(), object);
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			return super.instantiateItem(container, position % getPagers().size());
		}
	}
}
