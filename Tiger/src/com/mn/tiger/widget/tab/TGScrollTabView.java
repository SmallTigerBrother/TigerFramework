package com.mn.tiger.widget.tab;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;

import com.mn.tiger.widget.tab.TGTabView.OnTabChangeListener;
import com.mn.tiger.widget.tab.TGTabView.TabItem;

/**
 * 该类作用及功能说明:可滑动的TabView
 * 
 * @date 2014-3-10
 */
public class TGScrollTabView extends HorizontalScrollView
{
	private ScorllViewStateListener scorllViewStateListener;
	protected TGTabView mpTabView;
	private int lastX = 0;
	/**
	 * 发送message的时间间隔，用以判断scrollview是否停止滚动
	 */
	private static final int TIME_INTERVAL = 15;
	/**
	 * 用于循环监测scrollview是否停止滚动
	 */
	private Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			// 当前scrollview滚动的位置
			int currentX = TGScrollTabView.this.getScrollX();
			if (lastX == currentX)
			{
				stopScroll(TGScrollTabView.this);
			}
			else
			{
				lastX = currentX;
				// 当前的位置和上次的位置不一致，则重复监测
				handler.sendMessageDelayed(handler.obtainMessage(), TIME_INTERVAL);
			}

		}

	};

	public TGScrollTabView(Context context)
	{
		super(context);
		init();
	}

	public TGScrollTabView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public TGScrollTabView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * 该方法的作用:初始化，将MPTabview加入HorizontalScrollView中
	 * 
	 * @date 2014-2-19
	 */

	private void init()
	{
		this.setHorizontalScrollBarEnabled(false);
		mpTabView = new TGTabView(getContext());
		addView(mpTabView);
	}

	/**
	 * 该方法的作用:将OnTabChangeListener传入MPTabview中
	 * 
	 * @date 2014-3-10
	 * @param onTabChangeListener
	 */
	public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener)
	{
		mpTabView.setOnTabChangeListener(onTabChangeListener);
	}

	public boolean onTouchEvent(MotionEvent ev)
	{
		int action = ev.getAction();
		int y = (int) ev.getRawX();
		lastX = 0;
		switch (action)
		{
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				if (y != 0)
				{
					handler.sendMessageDelayed(handler.obtainMessage(), TIME_INTERVAL);
				}
				break;

			default:
				break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 该方法的作用:当scrollview停止滚动后，判断当前scrollview的位置
	 * 
	 * @date 2014-3-10
	 * @param view
	 */
	protected void stopScroll(View view)
	{
		if (mpTabView.getMeasuredWidth() <= getScrollOffset())
		{
			scorllViewStateListener.onRight();
			return;
		}
		else if (TGScrollTabView.this.getScrollX() <= 0)
		{
			scorllViewStateListener.onLeft();
			return;
		}
		else
		{
			int scrollX = TGScrollTabView.this.getScrollX();
			ArrayList<TabItem> tabItems = mpTabView.getTabItems();
			int sum = 0;
			// 保存上次计算的位置，用于滚动
			int storeSum = 0;
			int offset = 0;
			View itemView;
			int itemWidth = 0;
			for (int i = 0; i < tabItems.size(); i++)
			{
				itemView = tabItems.get(i).getContentView();
				// 获取item的宽度
				itemWidth = itemView.getMeasuredWidth();
				sum += itemWidth;
				// 判断总的宽度是否大于scrollView滚动的距离
				if (sum > scrollX)
				{
					// 计算滚动的距离和当前已经计算的items的宽度差
					offset = Math.abs(sum - scrollX);
					// 判断差值是否大于当前item宽度的一半，以此判断是往左移还是右移
					if (offset < itemWidth / 2)
					{
						itemView = tabItems.get(i + 1).getContentView();
						TGScrollTabView.this.scrollTo(sum, itemView.getMeasuredWidth());
					}
					else
					{
						TGScrollTabView.this.scrollTo(storeSum, sum);
					}

					break;
				}
				storeSum = sum;
			}
		}

	}

	/**
	 * 该方法的作用:获取单前scrollView偏移了多少
	 * 
	 * @date 2014-3-10
	 * @return
	 */
	private int getScrollOffset()
	{
		return TGScrollTabView.this.getMeasuredWidth() + TGScrollTabView.this.getScrollX() + 5;
	}

	/**
	 * 该方法的作用:将adapter传给MPTabView设置其中的item子view
	 * 
	 * @date 2014-3-14
	 * @param adapter
	 */
	public void setAdapter(BaseAdapter adapter)
	{
		mpTabView.setAdapter(adapter);
	}

	/**
	 * 该方法的作用:监听当前滑动到的位置状态
	 * 
	 * @date 2014-3-10
	 * @param scorllViewStateListener
	 */
	public void setOnScrollViewState(ScorllViewStateListener scorllViewStateListener)
	{
		this.scorllViewStateListener = scorllViewStateListener;
	}

	public static interface ScorllViewStateListener
	{
		/** 滑动到了顶端 */
		public void onLeft();

		/** 滑动到了底部 */
		public void onRight();

	}

}
