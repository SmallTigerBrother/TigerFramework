package com.mn.tiger.widget.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mn.tiger.utility.CR;

/**
 * 该类作用及功能说明
 * 对话框参数类
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public class TGDialogParams 
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	private Context context;
	
	/**
	 * 对话框宽度
	 */
	private int dialogWidth = -10;
	
	public TGDialogParams(Context context)
	{
		this.context = context;
	}
	
	/**
	 * 该方法的作用:
	 * 获取对话框背景资源
	 * @date 2014年2月10日
	 * @return
	 */
	public Drawable getBackgroundResource()
	{
		return context.getResources().getDrawable(CR.getDrawableId(context, "mjet_dialog_bg"));
	}
	
	/**
	 * 获取只有一个按钮时的宽度
	 * @return 只有一个按钮时的宽度
	 */
	public int getButtonWidthOfOne()
	{
		return (int) (getDialogWidth() * 0.45);
	}

	/**
	 * 获取只有两个按钮时的宽度
	 * @return 只有两个按钮时的宽度
	 */
	public int getButtonWidthOfDouble()
	{
		return (int) (getDialogWidth() * 0.4);
	}

	/**
	 * 获取只有三个按钮时的宽度
	 * @return 只有三个按钮时的宽度
	 */
	public int getButtonWidthOfThree()
	{
		return (int) (getDialogWidth() * 0.27);
	}

	/**
	 * 获取对话框的宽度
	 * @return 对话框的宽度
	 */
	@SuppressWarnings("deprecation")
	public int getDialogWidth()
	{
		if(dialogWidth == -10)
		{
			WindowManager windowManager = (WindowManager) getContext().getSystemService(
					Context.WINDOW_SERVICE);
			int screenWidth = windowManager.getDefaultDisplay().getWidth();
			
			dialogWidth = (int) (screenWidth * 0.9);
		}
		
		return dialogWidth;
	}
	
	/**
	 * 获取对话框的高度
	 * @return 对话框的高度
	 */
	public int getDialogHeight()
	{
		return LinearLayout.LayoutParams.WRAP_CONTENT;
	}

	/**
	 * 获取左按钮的文本颜色
	 * @return 左按钮的文本颜色
	 */
	public int getLeftButtonTextColor()
	{
		return context.getResources().getColor(CR.getColorId(getContext(), "mjet_x1e1e1e"));
	}
	
	/**
	 * 获取中按钮的文本颜色
	 * @return 中按钮的文本颜色
	 */
	public int getMiddleButtonTextColor()
	{
		return context.getResources().getColor(CR.getColorId(getContext(), "mjet_x1e1e1e"));
	}
	
	/**
	 * 获取右按钮的文本颜色
	 * @return 右按钮的文本颜色
	 */
	public int getRightButtonTextColor()
	{
		return context.getResources().getColor(CR.getColorId(getContext(), "mjet_x1e1e1e"));
	}
	
	/**
	 * 获取左按钮的文本颜色
	 * @return 左按钮的文本颜色
	 */
	public float getLeftButtonTextSize()
	{
		return 16f;
	}
	
	/**
	 * 获取中按钮的文本颜色
	 * @return 中按钮的文本颜色
	 */
	public float getMiddleButtonTextSize()
	{
		return 16f;
	}
	
	/**
	 * 获取右按钮的文本颜色
	 * @return 右按钮的文本颜色
	 */
	public float getRightButtonTextSize()
	{
		return 16f;
	}
	
	/**
	 * 获取左按钮的背景
	 * @return 左按钮的背景
	 */
	public Drawable getLeftButtonBackground()
	{
		return context.getResources().getDrawable(CR.getDrawableId(context, "mjet_dialog_button_selector"));
	}
	
	/**
	 * 获取中按钮的背景
	 * @return 中按钮的背景
	 */
	public Drawable getMiddleButtonBackground()
	{
		return context.getResources().getDrawable(CR.getDrawableId(context, "mjet_dialog_button_selector"));
	}
	
	/**
	 * 获取右按钮的背景
	 * @return 右按钮的背景
	 */
	public Drawable getRightButtonBackground()
	{
		return context.getResources().getDrawable(CR.getDrawableId(context, "mjet_dialog_button_selector"));
	}

	/**
	 * 获取对话框的主题
	 * @return 对话框的主题
	 */
	public int getDialogTheme()
	{
		return CR.getStyleId(context, "mjet_baseDialog");
	}
	
	/**
	 * 标题栏文字大小
	 * @return
	 */
	public float getTitleTextSize()
	{
		return 22f;
	}
	
	/**
	 * 标题栏文字颜色
	 * @return
	 */
	public int getTitleTextColor()
	{
		return context.getResources().getColor(CR.getColorId(getContext(), "mjet_x1e1e1e"));
	}

	/**
	 * 获取中间显示区域文字大小
	 * @return
	 */
	public float getBodyTextSize()
	{
		return 18f;
	}
	
	/**
	 * 获取中间显示区域文字颜色
	 * @return
	 */
	public int getBodyTextColor()
	{
		return context.getResources().getColor(CR.getColorId(getContext(), "mjet_x414141"));
	}
	
	protected Context getContext()
	{
		return context;
	}
}
