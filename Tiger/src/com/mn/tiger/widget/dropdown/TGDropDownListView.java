package com.mn.tiger.widget.dropdown;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.mn.tiger.utility.CR;

/**
 * 该类作用及功能说明 下拉列表类，实现依附于某个View的下拉列表
 * 
 * @version V2.0
 * @see JDK1.6,android-8
 */
public class TGDropDownListView extends TGDropDownAdapterView
{
	private static final String TAG = TGDropDownListView.class.getSimpleName();
	private static final int NO_ANIM = 0;
	private int popuwindowWidth;
	private int popuwindowHeight;
	/**
	 * 下拉列表
	 */
	private ListView listView;
	/**
	 * 主视图
	 */
	private View mainView;

	/**
	 * 该方法的作用:获取DropDownListView对象，同时设置popuwindow的宽高，帶動畫效果
	 * 
	 * @date 2014年4月17日
	 */
	public TGDropDownListView(Context context, int width, int height, int animationStyle)
	{
		super(context);
		View rootView = LayoutInflater.from(context).inflate(
				CR.getLayoutId(context, "mjet_webview_dropdown_listview"), null);
		mainView = rootView.findViewById(CR.getIdId(context, "mjet_dropdown_listview_main"));
		listView = (ListView) mainView.findViewById(CR.getIdId(context, "mjet_dropdown_listview"));
		listView.setSelector(context.getResources().getDrawable(
				CR.getDrawableId(context, "mjet_webview_dropdown_list_selector")));
		this.popuwindowHeight = height;
		this.popuwindowWidth = width;
		super.popupWindow = new PopupWindow(mainView, popuwindowWidth, popuwindowHeight, true);
		popupWindow.setAnimationStyle(animationStyle);

		mainView.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent keyEvent)
			{
				if (keyCode == KeyEvent.KEYCODE_BACK)
				{
					TGDropDownListView.this.dismiss();
					return true;
				}
				return false;
			}
		});
		// 设置点击“返回”键，使popupWindow消失，并且不影响背景
		// super.popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	/**
	 * 该方法的作用:获取DropDownListView对象，同时设置popuwindow的宽高
	 * 
	 * @date 2014年4月17日
	 */
	public TGDropDownListView(Context context, int width, int height)
	{
		this(context, width, height, NO_ANIM);
	}

	/**
	 * 该方法的作用:設置popuwindow的touch事件的监听
	 * 
	 * @date 2014年4月17日
	 */
	@Override
	public void setOnTouchListener(OnTouchListener touchListener)
	{
		if (null != mainView && null != touchListener)
		{
			mainView.setOnTouchListener(touchListener);
		}
	}

	/**
	 * 该方法的作用:設置listview中item的點擊監聽
	 * 
	 * @date 2014年4月17日
	 */
	@Override
	public void setOnItemClickListener(OnItemClickListener listener)
	{
		if (null != listView && null != listener)
		{
			listView.setOnItemClickListener(listener);
		}
	}

	/**
	 * 该方法的作用:跳转到listview中position位置
	 * 
	 * @date 2014年4月17日
	 */
	@Override
	public void setSelection(final int position)
	{
		if (null != listView && position >= 0 && position < listView.getCount())
		{
			listView.setSelection(position);
		}
	}

	/**
	 * 该方法的作用:设置子视图的背景
	 * 
	 * @date 2013-1-11
	 * @param position
	 *            子视图的位置
	 * @param selector
	 *            背景图片
	 */
	public void setChildBackgroud(int position, Drawable selector)
	{
		if (position < 0 || position >= listView.getCount() || null == listView || null == selector)
		{
			return;
		}
		View childView = listView.getChildAt(position);
		if (null != childView)
		{
			listView.setSelection(position);
			listView.setItemChecked(position, true);
			childView.setBackgroundDrawable(selector);
		}
	}

	/**
	 * 该方法的作用:设置选择框的背景
	 * 
	 * @date 2013-1-11
	 * @param drawable
	 *            背景图片
	 */
	public void setSelector(Drawable selector)
	{
		if (null != selector && null != listView)
		{
			listView.setSelector(selector);
		}
	}

	/**
	 * 该方法的作用:popuwindow相對他父控件的上方位置
	 * 
	 * @date 2014年4月17日
	 */
	@Override
	public int getTop()
	{
		return mainView.getTop();
	}

	/**
	 * 该方法的作用:popuwindow相對他父控件的底部位置
	 * 
	 * @date 2014年4月17日
	 */
	public int getBottom()
	{
		return mainView.getBottom();
	}

	/**
	 * 该方法的作用:popuwindow相對他父控件的左邊位置
	 * 
	 * @date 2014年4月17日
	 */
	@Override
	public int getLeft()
	{
		return mainView.getLeft();
	}

	/**
	 * 该方法的作用:popuwindow相對他父控件的右邊位置
	 * 
	 * @date 2014年4月17日
	 */
	@Override
	public int getRight()
	{
		return mainView.getRight();
	}
	

	/**
	 * 该方法的作用:获取视图屏幕坐标
	 * 
	 * @date 2014年4月17日
	 */
	@Override
	public void getLocationOnScreen(int[] location)
	{
		mainView.getLocationOnScreen(location);
	}
	/**
	 * 该方法的作用:获取视图显示矩形
	 * 
	 * @date 2014年4月17日
	 */
	@Override
	public void getGlobalVisibleRect(Rect rect)
	{
		mainView.getGlobalVisibleRect(rect);
	}

	/**
	 * 该方法的作用:获取popuwindow的高度
	 * 
	 * @date 2014年4月17日
	 */
	@Override
	public int getHeight()
	{
		return popuwindowHeight;
	}

	/**
	 * 该方法的作用:获取popuwindow的宽度
	 * 
	 * @date 2014年4月17日
	 */
	@Override
	public int getWidth()
	{
		return popuwindowWidth;
	}

	/**
	 * 该方法的作用:popuwindow的背景图片
	 * 
	 * @date 2014年4月17日
	 */
	@Override
	public void setBackgroundDrawable(Drawable drawable)
	{
		if (null != drawable && null != mainView)
		{
			mainView.setBackgroundDrawable(drawable);
		}
	}

	/**
	 * 该方法的作用:popuwindow的背景图片
	 * 
	 * @date 2014年4月17日
	 */
	@Override
	public void setBackgroundResource(int resId)
	{
		if (null != mainView)
		{
			mainView.setBackgroundResource(resId);
		}
	}

	/**
	 * 该方法的作用:popuwindow的背景色
	 * 
	 * @date 2014年4月17日
	 */
	@Override
	public void setBackgroundColor(int color)
	{
		if (null != mainView)
		{
			mainView.setBackgroundColor(color);
		}
	}

	/**
	 * 该方法的作用:设置listview的adapter
	 * 
	 * @date 2014年4月17日
	 */
	@Override
	public void setAdapter(ListAdapter adapter)
	{
		if (null != adapter && null != listView)
		{
			listView.setAdapter(adapter);
		}
	}

	/**
	 * 该方法的作用: 设置列表分隔线
	 * 
	 * @date 2013-1-11
	 * @param divider
	 *            列表分隔线图片
	 */
	public void setDivider(Drawable divider)
	{
		if (null != listView)
		{
			listView.setDivider(divider);
		}
	}

	/**
	 * 该方法的作用:设置列表分隔线高度
	 * 
	 * @date 2013-1-11
	 * @param height
	 *            列表分隔线高度
	 */
	public void setDividerHeight(int height)
	{
		if (null != listView)
		{
			listView.setDividerHeight(height);
		}
	}

	/**
	 * 该方法的作用:返回class的tag
	 * 
	 * @date 2014年4月17日
	 */
	@Override
	public String getTag()
	{
		return TAG;
	}
}
