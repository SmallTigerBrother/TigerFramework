package com.mn.tiger.widget;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 该类作用及功能说明
 * 顶部导航条
 * @author l00220455
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2013-8-30
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public class MPNavigationBar extends RelativeLayout
{	
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 中间标题字体大小
	 */
	public static final float MIDDLE_TEXT_SIZE = 18f;
	
	/**
	 * 中间标题字体颜色
	 */
	public static final int MIDDLE_TEXT_COLOR = 0xFFFFFFFF;
	
	/**
	 * 按钮字体大小
	 */
	public static final float BUTTON_TEXT_SIZE = 14f;
	
	/**
	 * 按钮字体颜色
	 */
	public static final int BUTTON_TEXT_COLOR = 0xFFFFFFFF;
	
	/**
	 * 左导航区Layout
	 */
	private LinearLayout leftNaviLayout;
	
	/**
	 * 右导航区Layout
	 */
	private LinearLayout rightNaviLayout;
	
	/**
	 * 中间导航区Layout
	 */
	private LinearLayout middleNaviLayout;
	
	/**
	 * 默认左导航按钮
	 */
	private MPImageButton leftNaviButton;
	
	/**
	 * 默认右导航按钮
	 */
	private MPImageButton rightNaviButton;
	
	/**
	 * 中间标题TextView
	 */
	private TextView middleTextView;
	
	public MPNavigationBar(Context context) 
	{
		super(context);
		setupViews();
	}

	public MPNavigationBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setupViews();
	}
	
	/**
	 * 该方法的作用:
	 * 初始化试图
	 * @author l00220455
	 * @date 2013-8-30
	 */
	protected void setupViews()
	{
		LayoutParams leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		leftParams.addRule(RelativeLayout.CENTER_VERTICAL);
		this.addView(getLeftNaviLayout(), leftParams);
		
		LinearLayout.LayoutParams leftBtnParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		getLeftNaviLayout().addView(getLeftNaviButton(), leftBtnParams);
		
		LayoutParams rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rightParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		this.addView(getRightNaviLayout(), rightParams);
		
		LinearLayout.LayoutParams rightBtnParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		getRightNaviLayout().addView(getRightNaviButton(), rightBtnParams);
		
		LayoutParams middleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		middleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		middleParams.addRule(RelativeLayout.CENTER_VERTICAL);
		
		middleParams.leftMargin = dip2px(getContext(), 50);
		middleParams.rightMargin = dip2px(getContext(), 50);
		
		this.addView(getMiddleNaviLayout(), middleParams);
		
		LinearLayout.LayoutParams middleTextParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		middleTextParams.gravity = Gravity.CENTER_VERTICAL;
		getMiddleNaviLayout().addView(getMiddleTextView(), middleTextParams);
	}
	
	/**
	 * 该方法的作用:
	 * 获取左导航Layout
	 * @author l00220455
	 * @date 2013-8-30
	 * @return
	 */
	public LinearLayout getLeftNaviLayout()
	{
		if(null == leftNaviLayout)
		{
			leftNaviLayout = new LinearLayout(getContext());
			leftNaviLayout.setHorizontalGravity(Gravity.CENTER_VERTICAL);
		}
		
		return leftNaviLayout;
	}
	
	/**
	 * 该方法的作用:
	 * 获取右导航Layout
	 * @author l00220455
	 * @date 2013-8-30
	 * @return
	 */
	public LinearLayout getRightNaviLayout()
	{
		if(null == rightNaviLayout)
		{
			rightNaviLayout = new LinearLayout(getContext());
			rightNaviLayout.setHorizontalGravity(Gravity.CENTER_VERTICAL);
		}
		
		return rightNaviLayout;
	} 
	
	/**
	 * 该方法的作用:
	 * 获取中间导航Layout
	 * @author l00220455
	 * @date 2013-8-30
	 * @return
	 */
	public LinearLayout getMiddleNaviLayout()
	{
		if(null == middleNaviLayout)
		{
			middleNaviLayout = new LinearLayout(getContext());
			middleNaviLayout.setOrientation(LinearLayout.HORIZONTAL);
			middleNaviLayout.setHorizontalGravity(Gravity.CENTER_VERTICAL);
		}
		
		return middleNaviLayout;
	}
	
	/**
	 * 该方法的作用:
	 * 获取左导航按钮
	 * @author l00220455
	 * @date 2013-8-30
	 * @return
	 */
	public MPImageButton getLeftNaviButton()
	{
		if(null == leftNaviButton)
		{
			leftNaviButton = new MPImageButton(getContext());
			leftNaviButton.setVisibility(View.INVISIBLE);
			leftNaviButton.setTextColor(BUTTON_TEXT_COLOR);
			leftNaviButton.setTextSize(BUTTON_TEXT_SIZE);
		}
		
		return leftNaviButton;
	}
	
	/**
	 * 该方法的作用:
	 * 设置左侧导航按钮
	 * @author l00220455
	 * @date 2014年3月23日
	 * @param leftNaviButton
	 */
	public void setLeftNaviButton(MPImageButton leftNaviButton)
	{
		leftNaviLayout.removeAllViews();
		this.leftNaviButton = leftNaviButton;
		leftNaviLayout.addView(leftNaviButton);
	}
	
	/**
	 * 该方法的作用:
	 * 获取右导航按钮
	 * @author l00220455
	 * @date 2013-8-30
	 * @return
	 */
	public MPImageButton getRightNaviButton()
	{
		if(null == rightNaviButton)
		{
			rightNaviButton = new MPImageButton(getContext());
			rightNaviButton.setVisibility(View.INVISIBLE);
			rightNaviButton.setTextColor(BUTTON_TEXT_COLOR);
			rightNaviButton.setTextSize(BUTTON_TEXT_SIZE);
		}
		
		return rightNaviButton;		
	}
	
	/**
	 * 该方法的作用:
	 * 设置右侧导航按钮
	 * @author l00220455
	 * @date 2014年3月23日
	 * @param rightNaviButton
	 */
	public void setRightNaviButton(MPImageButton rightNaviButton)
	{
		rightNaviLayout.removeAllViews();
		this.rightNaviButton = rightNaviButton;
		rightNaviLayout.addView(rightNaviButton);
	}
	
	/**
	 * 该方法的作用:
	 * 获取中间标题TextView
	 * @author l00220455
	 * @date 2013-8-30
	 * @return
	 */
	public TextView getMiddleTextView()
	{
		if(null == middleTextView)
		{
			middleTextView = new MPMarqueeText(getContext());
			middleTextView.setSingleLine();
			middleTextView.setTextColor(MIDDLE_TEXT_COLOR);
			middleTextView.setTextSize(MIDDLE_TEXT_SIZE);
			middleTextView.setGravity(Gravity.CENTER);
		}
		
		return middleTextView;
	}
	
	/**
	 * 该方法的作用:
	 * 设置标题文本
	 * @author l00220455
	 * @date 2013-8-30
	 * @param text
	 * @return
	 */
	public boolean setMiddleText(String text)
	{
		TextView middleText = getMiddleTextView();
		
		if (null != middleText) 
		{
			middleText.setText(convertI18n(text));
			return true;
		}
		
		return false;
	}
		
	/**
	 * 该方法的作用:
	 * 设置中间标题文本
	 * @author l00220455
	 * @date 2013-8-30
	 * @param text
	 * @param options 文本显示参数
	 * @return
	 */
	public boolean setMiddleText(String text, JSONObject options)
	{
		TextView middleText = getMiddleTextView();
		
		if (null != middleText) 
		{
			middleText.setText(convertI18n(text));
		}
		
		return false;
	}
	
	/**
	 * 该方法的作用:
	 * 设置左导航按钮是否可用
	 * @author l00220455
	 * @date 2013-8-30
	 * @param enabled
	 */
	public void setLeftButtonEnabled(boolean enabled) 
	{
		getLeftNaviButton().setEnabled(enabled);
	}
	
	/**
	 * 该方法的作用:
	 * 设置右导航按钮是否可用
	 * @author l00220455
	 * @date 2013-8-30
	 * @param enabled
	 */
	public void setRightButtonEnabled(boolean enabled) 
	{
		getRightNaviButton().setEnabled(enabled);
	}

	/**
	 * 该方法的作用:
	 * dip转换为px
	 * @author l00220455
	 * @date 2013-8-30
	 * @param context
	 * @param dipValue
	 * @return
	 */
	protected int dip2px(Context context, float dipValue)
	{ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
    }
	
	/**
	 * 该方法的作用:检查string是否有国际化
	 * @author l00220455
	 * @date 2013-2-27
	 * @param string
	 * @return 若有对应的国际化字符串，则返回国际化的字符串；若没有，返回原字符串
	 */
	protected String convertI18n(String string)
	{
		String result = string;
		if(!TextUtils.isEmpty(string))
		{
			if(string.contains("$i18n_"))
			{
				String subStr = string.substring(string.indexOf("_")+1);
				int resId = this.getResources().getIdentifier(subStr, "string", getContext().getPackageName());
				if(resId != 0)
				{
					result = this.getResources().getString(resId);
				}
			}
		}
		return result;
	}
	
}
