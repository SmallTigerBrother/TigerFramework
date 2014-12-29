package com.mn.tiger.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mn.tiger.R;


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
		LayoutInflater.from(getContext()).inflate(R.layout.tiger_navigationbar, this);
		
		leftNaviLayout = (RelativeLayout) findViewById(R.id.tiger_navi_left_layout);
		leftNaviButton = (TGImageButton)findViewById(R.id.tiger_navi_left_btn);
		
		rightNaviLayout = (RelativeLayout)findViewById(R.id.tiger_navi_right_layout);
		rightNaviButton = (TGImageButton)findViewById(R.id.tiger_navi_right_btn);
		
		middleNaviLayout = (RelativeLayout)findViewById(R.id.tiger_navi_middle_layout);
		middleTextView = (TextView)findViewById(R.id.tiger_navi_middle_text);
	}
	
	/**
	 * 该方法的作用:
	 * 获取左导航Layout
	 * @date 2013-8-30
	 * @return
	 */
	public RelativeLayout getLeftNaviLayout()
	{
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
		return leftNaviButton;
	}
	
	/**
	 * 该方法的作用:
	 * 获取右导航按钮
	 * @date 2013-8-30
	 * @return
	 */
	public TGImageButton getRightNaviButton()
	{
		return rightNaviButton;		
	}
	
	/**
	 * 该方法的作用:
	 * 获取中间标题TextView
	 * @date 2013-8-30
	 * @return
	 */
	public TextView getMiddleTextView()
	{
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
}
