package com.mn.tiger.widget;

import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mn.tiger.utility.DisplayUtils;


/**
 * 该类作用及功能说明
 * 顶部导航条
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2013-8-30
 */
public class TGNavigationBar extends RelativeLayout
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
	public static final int MIDDLE_TEXT_COLOR = 0xFF000000;
	
	/**
	 * 按钮字体大小
	 */
	public static final float BUTTON_TEXT_SIZE = 14f;
	
	/**
	 * 按钮字体颜色
	 */
	public static final int BUTTON_TEXT_COLOR = 0xFF000000;
	
	/**
	 * 左导航区Layout
	 */
	private RelativeLayout leftNaviLayout;
	
	/**
	 * 右导航区Layout
	 */
	private RelativeLayout rightNaviLayout;
	
	/**
	 * 中间导航区Layout
	 */
	private RelativeLayout middleNaviLayout;
	
	/**
	 * 默认左导航按钮
	 */
	private TGImageButton leftNaviButton;
	
	/**
	 * 默认右导航按钮
	 */
	private TGImageButton rightNaviButton;
	
	/**
	 * 中间标题TextView
	 */
	private TextView middleTextView;
	
	public TGNavigationBar(Context context) 
	{
		super(context);
		setupViews();
	}

	public TGNavigationBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setupViews();
	}
	
	/**
	 * 该方法的作用:
	 * 初始化试图
	 * @date 2013-8-30
	 */
	protected void setupViews()
	{
		LayoutParams leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		leftParams.addRule(RelativeLayout.CENTER_VERTICAL);
		this.addView(getLeftNaviLayout(), leftParams);
		
		getLeftNaviLayout().addView(getLeftNaviButton());
		
		LayoutParams rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rightParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		this.addView(getRightNaviLayout(), rightParams);
		
		getRightNaviLayout().addView(getRightNaviButton());
		
		LayoutParams middleParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		middleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		
		this.addView(getMiddleNaviLayout(), middleParams);
		
		RelativeLayout.LayoutParams middleTextParams = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		middleTextParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		getMiddleNaviLayout().addView(getMiddleTextView(), middleTextParams);
	}
	
	/**
	 * 该方法的作用:
	 * 获取左导航Layout
	 * @date 2013-8-30
	 * @return
	 */
	public RelativeLayout getLeftNaviLayout()
	{
		if(null == leftNaviLayout)
		{
			leftNaviLayout = new RelativeLayout(getContext());
		}
		
		return leftNaviLayout;
	}
	
	/**
	 * 该方法的作用:
	 * 获取右导航Layout
	 * @date 2013-8-30
	 * @return
	 */
	public RelativeLayout getRightNaviLayout()
	{
		if(null == rightNaviLayout)
		{
			rightNaviLayout = new RelativeLayout(getContext());
		}
		
		return rightNaviLayout;
	} 
	
	/**
	 * 该方法的作用:
	 * 获取中间导航Layout
	 * @date 2013-8-30
	 * @return
	 */
	public RelativeLayout getMiddleNaviLayout()
	{
		if(null == middleNaviLayout)
		{
			middleNaviLayout = new RelativeLayout(getContext());
			
			middleNaviLayout.setPadding(DisplayUtils.dip2px(getContext(), 50), 
					0, DisplayUtils.dip2px(getContext(), 50), 0);
		}
		
		return middleNaviLayout;
	}
	
	/**
	 * 该方法的作用:
	 * 获取左导航按钮
	 * @date 2013-8-30
	 * @return
	 */
	public TGImageButton getLeftNaviButton()
	{
		if(null == leftNaviButton)
		{
			leftNaviButton = new TGImageButton(getContext());
			leftNaviButton.setVisibility(View.INVISIBLE);
			leftNaviButton.setTextColor(BUTTON_TEXT_COLOR);
			leftNaviButton.setTextSize(BUTTON_TEXT_SIZE);
			leftNaviButton.setPadding(DisplayUtils.dip2px(getContext(), 8),
					DisplayUtils.dip2px(getContext(), 8), 
					DisplayUtils.dip2px(getContext(), 8), 
					DisplayUtils.dip2px(getContext(), 8));
			
			RelativeLayout.LayoutParams leftBtnParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			leftBtnParams.leftMargin = DisplayUtils.dip2px(getContext(), 8);
			
			leftNaviButton.setLayoutParams(leftBtnParams);
		}
		
		return leftNaviButton;
	}
	
	/**
	 * 该方法的作用:
	 * 设置左侧导航按钮
	 * @date 2014年3月23日
	 * @param leftNaviButton
	 */
	public void setLeftNaviButton(TGImageButton leftNaviButton)
	{
		leftNaviLayout.removeAllViews();
		this.leftNaviButton = leftNaviButton;
		leftNaviLayout.addView(leftNaviButton);
	}
	
	/**
	 * 该方法的作用:
	 * 获取右导航按钮
	 * @date 2013-8-30
	 * @return
	 */
	public TGImageButton getRightNaviButton()
	{
		if(null == rightNaviButton)
		{
			rightNaviButton = new TGImageButton(getContext());
			rightNaviButton.setVisibility(View.INVISIBLE);
			rightNaviButton.setTextColor(BUTTON_TEXT_COLOR);
			rightNaviButton.setTextSize(BUTTON_TEXT_SIZE);
			rightNaviButton.setPadding(DisplayUtils.dip2px(getContext(), 8),
					DisplayUtils.dip2px(getContext(), 8), 
					DisplayUtils.dip2px(getContext(), 8), 
					DisplayUtils.dip2px(getContext(), 8));
			
			RelativeLayout.LayoutParams rightBtnParams = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			rightBtnParams.rightMargin = DisplayUtils.dip2px(getContext(), 8);
			rightNaviButton.setLayoutParams(rightBtnParams);
		}
		
		return rightNaviButton;		
	}
	
	/**
	 * 该方法的作用:
	 * 设置右侧导航按钮
	 * @date 2014年3月23日
	 * @param rightNaviButton
	 */
	public void setRightNaviButton(TGImageButton rightNaviButton)
	{
		rightNaviLayout.removeAllViews();
		this.rightNaviButton = rightNaviButton;
		rightNaviLayout.addView(rightNaviButton);
	}
	
	/**
	 * 该方法的作用:
	 * 获取中间标题TextView
	 * @date 2013-8-30
	 * @return
	 */
	public TextView getMiddleTextView()
	{
		if(null == middleTextView)
		{
			middleTextView = new TGMarqueeText(getContext());
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
	 * @date 2013-8-30
	 * @param text
	 * @return
	 */
	public boolean setMiddleText(String text)
	{
		TextView middleText = getMiddleTextView();
		
		if (null != middleText) 
		{
			middleText.setText(text);
			return true;
		}
		
		return false;
	}
		
	/**
	 * 该方法的作用:
	 * 设置中间标题文本
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
			middleText.setText(text);
		}
		
		return false;
	}
	
	/**
	 * 该方法的作用:
	 * 设置左导航按钮是否可用
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
	 * @date 2013-8-30
	 * @param enabled
	 */
	public void setRightButtonEnabled(boolean enabled) 
	{
		getRightNaviButton().setEnabled(enabled);
	}
	
	/**
	 * 布局参数LayoutParams
	 */
	public static class LayoutParams extends RelativeLayout.LayoutParams
	{
		public LayoutParams(int w, int h)
		{
			super(w, h);
		}
		
		public LayoutParams(MarginLayoutParams source)
		{
			super(source);
		}
	}
}
