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
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;

import com.mn.tiger.utility.CR;

/**
 * 该类作用及功能说明:GridView风格的popuwindow
 * 
 * @author zWX200279
 * @date 2014年4月17日
 */
public class DropDownGridView extends DropDownAdapterView
{
	private static final String TAG = DropDownGridView.class.getSimpleName();
	private GridView gridView;
	private View mainView;
	private int popuwindowWidth;
	private int popuwindowHeight;
	private static final int NO_ANIM = 0;

	/**
	 * 该方法的作用:获取DropDownGridView对象，同时设置popuwindow的宽高，带动画效果
	 * 
	 * @author zWX200279
	 * @date 2014年4月17日
	 */
	public DropDownGridView(Context context, int width, int height, int animationStyle)
	{
		super(context);
		mainView = LayoutInflater.from(context).inflate(
				CR.getLayoutId(context, "mjet_webview_dropdown_gridview"), null);
		gridView = (GridView) mainView.findViewById(CR.getIdId(context, "mjet_dropdown_gridview"));
		super.popupWindow = new PopupWindow(mainView, width, height, true);
		this.popuwindowHeight = height;
		this.popuwindowWidth = width;
		popupWindow.setAnimationStyle(animationStyle);

		mainView.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent keyEvent)
			{
				if (keyCode == KeyEvent.KEYCODE_BACK)
				{
					DropDownGridView.this.dismiss();
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 该方法的作用:获取DropDownGridView对象，同时设置popuwindow的宽高，不带动画效果
	 * 
	 * @author zWX200279
	 * @date 2014年4月17日
	 */
	public DropDownGridView(Context context, int width, int height)
	{
		this(context, width, height, NO_ANIM);
	}

	/**
	 * 该方法的作用:设置gridView的item点击监听
	 * 
	 * @author zWX200279
	 * @date 2014年4月17日
	 */
	@Override
	public void setOnItemClickListener(OnItemClickListener listener)
	{
		if (null != gridView && null != listener)
		{
			gridView.setOnItemClickListener(listener);
		}
	}

	/**
	 * 该方法的作用:设置gridview中选中position位置项
	 * 
	 * @author zWX200279
	 * @date 2014年4月17日
	 */
	@Override
	public void setSelection(int position)
	{
		if (null != gridView && position >= 0 && position < gridView.getCount())
		{
			gridView.setSelection(position);
		}
	}

	/**
	 * 该方法的作用:设置选择框的背景
	 * 
	 * @author l00220455
	 * @date 2013-1-11
	 * @param drawable
	 *            背景图片
	 */
	public void setSelector(Drawable selector)
	{
		if (null != selector && null != gridView)
		{
			gridView.setSelector(selector);
		}
	}

	/**
	 * 该方法的作用:获取popuwindow的高度
	 * 
	 * @author zWX200279
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
	 * @author zWX200279
	 * @date 2014年4月17日
	 */
	@Override
	public int getWidth()
	{
		return popuwindowWidth;
	}

	/**
	 * 该方法的作用:设置popuwindow的背景圖片
	 * 
	 * @author zWX200279
	 * @date 2014年4月17日
	 */
	@Override
	public void setBackgroundDrawable(Drawable drawable)
	{
		mainView.setBackgroundDrawable(drawable);
	}

	/**
	 * 该方法的作用:popuwindow相對他父控件的上方位置
	 * 
	 * @author zWX200279
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
	 * @author zWX200279
	 * @date 2014年4月17日
	 */
	@Override
	public int getBottom()
	{
		return mainView.getBottom();
	}

	/**
	 * 该方法的作用:popuwindow相對他父控件的左边位置
	 * 
	 * @author zWX200279
	 * @date 2014年4月17日
	 */
	@Override
	public int getLeft()
	{
		return mainView.getLeft();
	}

	/**
	 * 该方法的作用:popuwindow相對他父控件的右边位置
	 * 
	 * @author zWX200279
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
	 * @author zWX200279
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
		 * @author zWX200279
		 * @date 2014年4月17日
		 */
	@Override
	public void getGlobalVisibleRect(Rect rect)
	{
		mainView.getGlobalVisibleRect(rect);
	}

	/**
	 * 该方法的作用:设置popuwindow的背景图
	 * 
	 * @author zWX200279
	 * @date 2014年4月17日
	 */
	@Override
	public void setBackgroundResource(int resId)
	{
		mainView.setBackgroundResource(resId);
	}

	/**
	 * 该方法的作用:设置popuwindow的背景色
	 * 
	 * @author zWX200279
	 * @date 2014年4月17日
	 */
	@Override
	public void setBackgroundColor(int color)
	{
		mainView.setBackgroundColor(color);
	}

	/**
	 * 该方法的作用:设置popuwindow的touch監聽
	 * 
	 * @author zWX200279
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
	 * 该方法的作用:设置gridView的adapter
	 * 
	 * @author zWX200279
	 * @date 2014年4月17日
	 */
	@Override
	public void setAdapter(ListAdapter adapter)
	{
		gridView.setAdapter(adapter);
	}

	/**
	 * 该方法的作用:设置gridView有多少列
	 * 
	 * @author zWX200279
	 * @date 2014年4月17日
	 */
	public void setNumColumns(int numColumns)
	{
		gridView.setNumColumns(numColumns);
	}

	/**
	 * 该方法的作用:设置item间水平和垂直位置的间距
	 * 
	 * @author zWX200279
	 * @date 2014年4月17日
	 * @param horizontalSpacing
	 * @param verticalSpacing
	 */
	public void setSpace(int horizontalSpacing, int verticalSpacing)
	{
		gridView.setHorizontalSpacing(horizontalSpacing);
		gridView.setVerticalSpacing(verticalSpacing);
	}

	/**
	 * 该方法的作用:返回class的tag
	 * 
	 * @author zWX200279
	 * @date 2014年4月17日
	 */
	@Override
	public String getTag()
	{
		return TAG;
	}

}
