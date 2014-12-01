package com.mn.tiger.widget.viewpager;

import java.util.ArrayList;

import com.mn.tiger.log.LogTools;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 可自动翻页的ViewPager
 */
public class TGAutoFlipViewPager extends ViewPager
{
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 当前显示的页码
	 */
	private int currentPageNum = 0;

	/**
	 * 用于接收定时翻页消息的Handler
	 */
	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			setCurrentItem(msg.what);
		};
	};
	
	/**
	 * 是否继续翻页
	 */
	private boolean isContinue = true;
	
	/**
	 * 是否正在滚动
	 */
	private boolean isSrolling = false;
	
	/**
	 * 翻页周期
	 */
	private int duration = 4000;
	
	/**
	 * 内置的页码改变监听器
	 */
	private OnPageChangeListener internalPageChangeListener = null;
	
	/**
	 * 定时发送翻页消息的线程
	 */
	private Thread thread = new Thread()
	{
		public void run() 
		{
			while (true)
			{
				if(isContinue && isSrolling)
				{
					currentPageNum++;
					handler.obtainMessage(currentPageNum).sendToTarget();
				}
				
				try
				{
					Thread.sleep(duration);
				}
				catch (InterruptedException e)
				{
					LogTools.e(LOG_TAG, e.getMessage(), e);
				}
			}
		};
	};
	
	public TGAutoFlipViewPager(Context context)
	{
		super(context);
		setListeners();
	}

	public TGAutoFlipViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setListeners();
	}
	
	/**
	 * 设置触摸事件监听器
	 */
	private void setListeners()
	{
		setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
					//当按下、滑动时不能自动滚动
					case MotionEvent.ACTION_DOWN:
					case MotionEvent.ACTION_MOVE:
						isContinue = false;
						break;
					case MotionEvent.ACTION_UP://当手指抬起后不能自动滚动
						isContinue = true;
						break;
					default:
						isContinue = true;
						break;
				}
				return false;
			}
		});
		
		super.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int page)
			{
				currentPageNum = page;
				if(null != internalPageChangeListener)
				{
					internalPageChangeListener.onPageSelected(page);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
				if(null != internalPageChangeListener)
				{
					internalPageChangeListener.onPageScrolled(arg0, arg1, arg2);
				}
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				if(null != internalPageChangeListener)
				{
					internalPageChangeListener.onPageScrollStateChanged(arg0);
				}
			}
		});
	}
	
	@Override
	public void setAdapter(PagerAdapter adapter)
	{
		super.setAdapter(adapter);
		thread.start();
	}
	
	/**
	 * 开始滚动
	 */
	public void startScroll()
	{
		isSrolling = true;
		isContinue = true;
		
		setCurrentItem(currentPageNum);
	}
	
	@Override
	public void setCurrentItem(int item)
	{
		//计算当前页码，确保可以自动滚动
		if(item < 100 && null != getAdapter())
		{
			currentPageNum = item + ((TGPagerAdapter)getAdapter()).getPagers().size() * 100 - 1;
		}
		super.setCurrentItem(currentPageNum);
	}
	
	//计算当前页码，确保可以自动滚动
	@Override
	public void setCurrentItem(int item, boolean smoothScroll)
	{
		if(item < 100 && null != getAdapter())
		{
			currentPageNum = item + ((TGPagerAdapter)getAdapter()).getPagers().size() * 100 - 1;
		}
		
		super.setCurrentItem(currentPageNum, smoothScroll);
	}
	
	/**
	 * 停止自动滚动
	 */
	public void stopScroll()
	{
		isSrolling = false;
	}
	
	/**
	 * 设置滚动周期
	 * @param duration
	 */
	public void setSrollDuration(int duration)
	{
		this.duration = duration;
	}
	
	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener)
	{
		this.internalPageChangeListener = listener;
	}
	
	/**
	 * 循环滚动PagerAdapter
	 * @author za4480
	 *
	 */
	public static class CirclePagerAdapter extends TGPagerAdapter
	{
		/**
		 * @param views 各个页面的视图
		 */
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
			//计算页码，取余数
			super.destroyItem(container, position % getPagers().size(), object);
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			//计算页码，取余数
			return super.instantiateItem(container, position % getPagers().size());
		}
	}
	
}
