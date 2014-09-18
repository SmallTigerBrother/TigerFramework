package com.mn.tiger.widget.dropdown;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明 下拉列表基类，实现下拉列表的基本功能
 * 
 * @author l00220455
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2012-9-25 Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights
 *       reserved.
 */
public abstract class DropDownView
{
	/**
	 * 运行环境
	 */
	protected Context context;

	/**
	 * 下拉列表框
	 */
	protected PopupWindow popupWindow;

	/**
	 * @author l00220455
	 * @date 2012-9-25 构造函数
	 * @param context
	 *            运行环境
	 */
	public DropDownView(Context context)
	{
		this.context = context;
	}

	/**
	 * 该方法的作用: 设置触摸监听器
	 * 
	 * @author l00220455
	 * @date 2013-1-11
	 * @param touchListener
	 *            触摸事件监听器
	 */
	public abstract void setOnTouchListener(OnTouchListener touchListener);

	/**
	 * 该方法的作用:显示下拉列表
	 * 
	 * @author l00220455
	 * @date 2012-9-25
	 * @param parent
	 *            依附的View
	 * @param xoff
	 *            X偏移量
	 * @param yoff
	 *            Y偏移量
	 */
	@SuppressWarnings("deprecation")
	public void showAsDropDown(View parent, int xoff, int yoff)
	{
		LogTools.d(getTag(), "showAsDropDown");
		// 设置当点击popupWindwo以外的区域时，关闭popupWindow
		// 设置聚焦
		popupWindow.setFocusable(true);

		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);

		// 刷新状态（必须刷新，否则无效）
		popupWindow.update();
		popupWindow.showAsDropDown(parent, xoff, yoff);
	}

	/**
	 * 该方法的作用:显示popuwindow在指定控件的右边
	 * 
	 * @author zWX200279
	 * @date 2014年4月2日
	 * @param parent
	 *            指定参照控件
	 * @param xoff
	 *            x偏移
	 * @param yoff
	 *            y偏移
	 */
	@SuppressWarnings("deprecation")
	public void showAsDropRight(View parent, int xoff, int yoff)
	{
		LogTools.d(getTag(), "showAsDropRight");
		// 计算相对showAsDropDown的实际偏移
		int xoffset = xoff + parent.getWidth();
		int yoffset = yoff - parent.getHeight();
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		popupWindow.showAsDropDown(parent, xoffset, yoffset);
	}

	/**
	 * 该方法的作用:显示popuwindow在指定控件的左邊
	 * 
	 * @author zWX200279
	 * @date 2014年4月2日
	 * @param parent
	 * @param xoff
	 * @param yoff
	 */
	@SuppressWarnings("deprecation")
	public void showAsDropLeft(View parent, int xoff, int yoff)
	{
		LogTools.d(getTag(), "showAsDropLeft");
		// 计算相对showAsDropDown的实际偏移
		int xoffset = xoff - getWidth();
		int yoffset = yoff - parent.getHeight();
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		popupWindow.showAsDropDown(parent, xoffset, yoffset);
	}

	/**
	 * 该方法的作用:显示popuwindow在指定控件的上面
	 * 
	 * @author zWX200279
	 * @date 2014年4月2日
	 * @param parent
	 * @param xoff
	 * @param yoff
	 */
	@SuppressWarnings("deprecation")
	public void showAsDropTop(View parent, int xoff, int yoff)
	{
		LogTools.d(getTag(), "showAsDropTop");
		int yoffset = yoff - getHeight() - parent.getHeight();
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		popupWindow.showAsDropDown(parent, xoff, yoffset);
	}

	/**
	 * 该方法的作用:显示popuwindow在屏幕的中央
	 * 
	 * @author zWX200279
	 * @date 2014年4月2日
	 * @param parent
	 * @param x
	 * @param y
	 */
	public void showAsCenter(View parent, int x, int y)
	{
		LogTools.d(getTag(), "showAsCenter-CENTER");
		showAsCenter(parent, x, y, Gravity.CENTER);
	}

	/**
	 * 该方法的作用:可通过gravity自定义显示的位置
	 * 
	 * @author zWX200279
	 * @date 2014年4月2日
	 * @param parent
	 * @param x
	 * @param y
	 * @param gravity
	 */
	@SuppressWarnings("deprecation")
	public void showAsCenter(View parent, int x, int y, int gravity)
	{
		LogTools.d(getTag(), "showAsCenter-gravity");
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.update();
		popupWindow.showAtLocation(parent, gravity, x, y);
	}

	/**
	 * 该方法的作用:移除下列表
	 * 
	 * @author l00220455
	 * @date 2012-9-25
	 */
	public void dismiss()
	{
		popupWindow.dismiss();
	}

	/**
	 * 该方法的作用:返回当前下拉列表的显示状态，显示在当前窗口返回true，未显示在当前窗口返回false
	 * 
	 * @author l00220455
	 * @date 2012-9-25
	 * @return 当前下拉列表的显示状态，显示在当前窗口返回true，未显示在当前窗口返回false
	 */
	public boolean isShowing()
	{
		return popupWindow.isShowing();
	}

	/**
	 * 该方法的作用: 设置背景图片
	 * 
	 * @author l00220455
	 * @date 2013-1-11
	 * @param drawable
	 *            背景图片
	 */
	public abstract void setBackgroundDrawable(Drawable drawable);

	/**
	 * 该方法的作用: 设置背景图片的ID
	 * 
	 * @author l00220455
	 * @date 2013-1-11
	 * @param resId
	 *            背景图片的ID
	 */
	public abstract void setBackgroundResource(int resId);

	/**
	 * 该方法的作用: 设置背景颜色
	 * 
	 * @author l00220455
	 * @date 2013-1-11
	 * @param color
	 *            背景颜色
	 */
	public abstract void setBackgroundColor(int color);

	/**
	 * 该方法的作用: 获取顶部坐标
	 * 
	 * @author l00220455
	 * @date 2013-1-11
	 * @return 顶部坐标
	 */
	public abstract int getTop();

	/**
	 * 该方法的作用: 获取底部坐标
	 * 
	 * @author l00220455
	 * @date 2013-1-11
	 * @return 底部坐标
	 */
	public abstract int getBottom();

	/**
	 * 该方法的作用: 获取左侧坐标
	 * 
	 * @author l00220455
	 * @date 2013-1-11
	 * @return 左侧坐标
	 */
	public abstract int getLeft();

	/**
	 * 该方法的作用: 获取右侧坐标
	 * 
	 * @author l00220455
	 * @date 2013-1-11
	 * @return 右侧坐标
	 */
	public abstract int getRight();

	/**
	 * 该方法的作用: 获取视图屏幕坐标
	 * 
	 * @author l00220455
	 * @date 2013-1-11
	 * @return 视图屏幕坐标
	 */
	public abstract void getLocationOnScreen(int[] location);

	/**
	 * 该方法的作用: 获取视图高度
	 * 
	 * @author l00220455
	 * @date 2013-1-11
	 * @return 视图高度
	 */
	public abstract int getHeight();

	/**
	 * 该方法的作用: 获取视图宽度
	 * 
	 * @author l00220455
	 * @date 2013-1-11
	 * @return 视图宽度
	 */
	public abstract int getWidth();

	/**
	 * 该方法的作用: 获取视图显示矩形
	 * 
	 * @author l00220455
	 * @date 2013-1-11
	 * @return 视图显示矩形
	 */
	public abstract void getGlobalVisibleRect(Rect rect);

	public abstract String getTag();
}
