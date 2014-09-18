package com.mn.tiger.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mn.tiger.utility.CR;
import com.mn.tiger.utility.DisplayUtils;

/**
 * 对话框扩展类
 */
public class MPDialog extends Dialog implements IDialog
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 根视图
	 */
	private LinearLayout rootView;
	
	/*********************Title*****************************/
	/**
	 * 标题栏Layout
	 */
	private LinearLayout titleLayout;
	
	/**
	 * 标题栏文本视图
	 */
	private TextView titleTextView;
	
	/**
	 * 标题栏是否被自定义
	 */
	private boolean isTitleCustom = false;
	
	
	/***********************Body***************************/
	
	/**
	 * Body区域layout
	 */
	private LinearLayout bodyLayout;
	
	/**
	 * Body区域文本视图
	 */
	private TextView bodyTextView;
	
	/**
	 * Body区域是否被自定义
	 */
	private boolean isBodyCustom = false;
	
	/***********************Bottom***************************/
	
	/**
	 * 底部栏Layout
	 */
	private RelativeLayout bottomLayout;
	
	/**
	 * 底部栏是否被自定义
	 */
	private boolean isBottomCustom = false;
	
	/**
	 * 底部栏左按钮
	 */
	private Button leftButton;
	
	/**
	 * 底部栏左按钮是否显示
	 */
	private boolean showLeftButton = false;
	
	/**
	 * 底部栏中按钮
	 */
	private Button middleButton;
	
	/**
	 * 底部栏中按钮是否显示
	 */
	private boolean showMiddleButton = false;
	
	/**
	 * 底部栏右按钮
	 */
	private Button rightButton;
	
	/**
	 * 底部栏右按钮是否显示
	 */
	private boolean showRightButton = false;
	
	/**
	 * 对话框显示参数
	 */
	private MPDialogParams dialogParams = null;
	
	public MPDialog(Context context)
	{
		super(context, CR.getStyleId(context, "mjet_baseDialog"));
		dialogParams = new MPDialogParams(getContext());
		setupDialog();
	}
	
	public MPDialog(Context context, MPDialogParams dialogParams)
	{
		super(context, dialogParams.getDialogTheme());
		this.dialogParams = dialogParams;
		setupDialog();
	}
	
	public MPDialog(Context context, int theme)
	{
		super(context, theme);
		dialogParams = new MPDialogParams(getContext());
		setupDialog();
	}
	
	public MPDialog(Context context, boolean cancelable,
			IDialog.OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		dialogParams = new MPDialogParams(getContext());
		setupDialog();
	}
	
	public MPDialog(Context context, boolean cancelable,
			IDialog.OnCancelListener cancelListener, MPDialogParams dialogParams)
	{
		super(context, cancelable, cancelListener);
		this.dialogParams = dialogParams;
		setupDialog();
	}
	
	/**
	 * 初始化对话框
	 */
	private void setupDialog()
	{
		//设置不现实标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//初始化主视图
		rootView = new LinearLayout(getContext());
		rootView.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				dialogParams.getDialogWidth(), dialogParams.getDialogHeight());
		params.gravity = Gravity.CENTER_HORIZONTAL;
		rootView.setBackgroundDrawable(dialogParams.getBackgroundResource());
		rootView.setPadding(DisplayUtils.dip2px(getContext(), 8), DisplayUtils.dip2px(getContext(), 8), 
				DisplayUtils.dip2px(getContext(), 8), DisplayUtils.dip2px(getContext(), 8));
		setContentView(rootView, params);
		
		//初始化Title
		setupTitleView();
		
		//初始化Body
		setupBodyView();
		
		//初始化Bottom
		setupBottomView();
		
	}
	
	/**
	 * 该方法的作用:
	 * 初始化标题栏
	 * @author l00220455
	 * @date 2014年2月10日
	 */
	protected void setupTitleView()
	{
		//初始化标题栏Layout
		titleLayout = new LinearLayout(getContext());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		titleLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
		
		//初始化标题栏Textview
		titleTextView = new TextView(getContext());
		titleTextView.setTextSize(dialogParams.getTitleTextSize());
		titleTextView.setTextColor(dialogParams.getTitleTextColor());
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		titleTextView.setGravity(Gravity.CENTER);
		textParams.leftMargin = DisplayUtils.dip2px(getContext(), 16);
		textParams.rightMargin = DisplayUtils.dip2px(getContext(), 16);
		textParams.topMargin = DisplayUtils.dip2px(getContext(), 4);
		
		titleLayout.addView(titleTextView, textParams);
		rootView.addView(titleLayout, layoutParams);
	}
	
	/**
	 * 该方法的作用:
	 * 初始化Body区域
	 * @author l00220455
	 * @date 2014年2月10日
	 */
	protected void setupBodyView()
	{
		//初始化中间显示区Layout
		bodyLayout = new LinearLayout(getContext());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		bodyLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
		
		//初始化中间显示区Textview
		bodyTextView = new TextView(getContext());
		bodyTextView.setTextSize(dialogParams.getBodyTextSize());
		bodyTextView.setTextColor(dialogParams.getBodyTextColor());
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
	    bodyTextView.setGravity(Gravity.CENTER);
		
		textParams.leftMargin = DisplayUtils.dip2px(getContext(), 8);
		textParams.rightMargin = DisplayUtils.dip2px(getContext(), 8);
		textParams.topMargin = DisplayUtils.dip2px(getContext(), 8);
		
		bodyLayout.addView(bodyTextView, textParams);
		rootView.addView(bodyLayout, layoutParams);
	}
	
	/**
	 * 该方法的作用:
	 * 初始化底部操作栏
	 * @author l00220455
	 * @date 2014年2月10日
	 */
	protected void setupBottomView()
	{
		//初始化中间显示区Layout
		bottomLayout = new RelativeLayout(getContext());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		
		layoutParams.bottomMargin = DisplayUtils.dip2px(getContext(), 8);
		layoutParams.topMargin = DisplayUtils.dip2px(getContext(), 16);
		
		rootView.addView(bottomLayout, layoutParams);
	}
	
	/**
	 * 设置左按钮
	 * @param text 按钮文本
	 * @param listener 点击事件监听器
	 */
	public void setLeftButton(CharSequence text, final OnClickListener listener)
	{
		if(isBottomCustom || null == text)
		{
			return;
		}
		
		if(!showLeftButton)
		{
			//初始化LeftButton
			showLeftButton = true;
			leftButton = initLeftButton();
			bottomLayout.addView(leftButton);
		}
		
		//设置显示参数，点击事件
		leftButton.setText(text);
		leftButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(null != listener)
				{
					listener.onClick(MPDialog.this, IDialog.BUTTON_LEFT);
				}
			}
		});
		
		//设置左按钮的位置
		setLeftButtonPosition();
		bottomLayout.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 该方法的作用:
	 * 初始化左按钮
	 * @author l00220455
	 * @date 2014年1月6日
	 * @return
	 */
	protected Button initLeftButton()
	{
		Button leftButton = new Button(getContext());
		leftButton.setTextSize(dialogParams.getLeftButtonTextSize());
		leftButton.setTextColor(dialogParams.getLeftButtonTextColor());
		leftButton.setBackgroundDrawable(dialogParams.getLeftButtonBackground());
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		leftButton.setLayoutParams(layoutParams);
		
		return leftButton;
	}
	
	/**
	 * 该方法的作用:
	 * 设置左按钮位置
	 * @author l00220455
	 * @date 2014年2月10日
	 */
	protected void setLeftButtonPosition()
	{
		if(!showLeftButton)
		{
			return;
		}
		
		RelativeLayout.LayoutParams leftParams = (RelativeLayout.LayoutParams) leftButton.getLayoutParams();
		//若存在中间按钮，则按照三个按钮计算大小，否则按两个按钮计算大小
		if(showMiddleButton)
		{
			//计算左按钮的位置
			leftParams.width = dialogParams.getButtonWidthOfThree();
			leftParams.leftMargin = (int)((dialogParams.getDialogWidth() - dialogParams.getButtonWidthOfThree() * 3) /6);
			//更新中间按钮的宽度
			RelativeLayout.LayoutParams middleParams = (RelativeLayout.LayoutParams)middleButton.getLayoutParams();
			middleParams.width = dialogParams.getButtonWidthOfThree();
			middleButton.setLayoutParams(middleParams);
		}
		else 
		{
			//计算左按钮的位置
			leftParams.width = dialogParams.getButtonWidthOfDouble();
			leftParams.leftMargin = (int)((dialogParams.getDialogWidth() - dialogParams.getButtonWidthOfDouble() * 2) / 4);
		}
		
		leftButton.setLayoutParams(leftParams);
	}
	
	/**
	 * 设置中按钮
	 * @param text 按钮文本
	 * @param listener 点击事件监听器
	 */
	public void setMiddleButton(CharSequence text, final OnClickListener listener)
	{
		if(isBottomCustom || null == text)
		{
			return;
		}
		
		if(!showMiddleButton)
		{
			//初始化MiddleButton
			showMiddleButton = true;
			middleButton = initMiddleButton();
			bottomLayout.addView(middleButton);
		}
		
		//设置显示参数，点击事件
		middleButton.setText(text);
		middleButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(null != listener)
				{
					listener.onClick(MPDialog.this, IDialog.BUTTON_MIDDLE);
				}
			}
		});
		
		//设置按钮显示位置
		setMiddleButtonPosition();
		bottomLayout.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 该方法的作用:
	 * 初始化中间按钮
	 * @author l00220455
	 * @date 2014年1月6日
	 * @return
	 */
	protected Button initMiddleButton()
	{
		Button middleButton = new Button(getContext());
		middleButton.setTextSize(dialogParams.getMiddleButtonTextSize());
		middleButton.setTextColor(dialogParams.getMiddleButtonTextColor());
		middleButton.setBackgroundDrawable(dialogParams.getMiddleButtonBackground());
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		middleButton.setLayoutParams(layoutParams);
		
		return middleButton;
	}
	
	
	/**
	 * 该方法的作用:
	 * 设置中按钮位置
	 * @author l00220455
	 * @date 2014年2月10日
	 */
	protected void setMiddleButtonPosition()
	{
		if(!showMiddleButton)
		{
			return;
		}
		
		RelativeLayout.LayoutParams middleParams = (RelativeLayout.LayoutParams) middleButton.getLayoutParams();
		//若存在中间按钮，则按照三个按钮计算大小，否则按两个按钮计算大小
		if(showLeftButton || showRightButton)
		{
			middleParams.width = dialogParams.getButtonWidthOfThree();
			
			//计算左按钮的位置
			if(showLeftButton)
			{
				RelativeLayout.LayoutParams leftParams = (RelativeLayout.LayoutParams) leftButton.getLayoutParams();
				leftParams.width = dialogParams.getButtonWidthOfThree();
				leftParams.leftMargin = (int)((dialogParams.getDialogWidth() - dialogParams.getButtonWidthOfThree() * 3)/6);
				leftButton.setLayoutParams(leftParams);
			}
			
			//计算右按钮的位置
			if(showRightButton)
			{
				RelativeLayout.LayoutParams rightParams = (RelativeLayout.LayoutParams) rightButton.getLayoutParams();
				rightParams.width = dialogParams.getButtonWidthOfThree();
				rightParams.rightMargin = (int)((dialogParams.getDialogWidth() - dialogParams.getButtonWidthOfThree() * 3)/6);
				rightButton.setLayoutParams(rightParams);
			}
		}
		else 
		{
			//计算中按钮的大小
			middleParams.width = dialogParams.getButtonWidthOfOne();
		}
		
		middleButton.setLayoutParams(middleParams);
	}
	
	/**
	 * 设置右按钮
	 * @param text 按钮文本
	 * @param listener 点击事件监听器
	 */
	public void setRightButton(CharSequence text, final OnClickListener listener)
	{
		if(isBottomCustom || null == text)
		{
			return;
		}
		
		if(!showRightButton)
		{
			//初始化RightButton
			showRightButton = true;
			rightButton = initRightButton();
			bottomLayout.addView(rightButton);
		}
		
		//设置显示参数，点击事件
		rightButton.setText(text);
		rightButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(null != listener)
				{
					listener.onClick(MPDialog.this, IDialog.BUTTON_RIGHT);
				}
			}
		});
		
		//设置按钮显示位置
		setRightButtonPosition();
		bottomLayout.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 该方法的作用:
	 * 初始化右按钮
	 * @author l00220455
	 * @date 2014年1月6日
	 * @return
	 */
	protected Button initRightButton()
	{
		Button rightButton = new Button(getContext());
		rightButton.setTextSize(dialogParams.getRightButtonTextSize());
		rightButton.setTextColor(dialogParams.getRightButtonTextColor());
		rightButton.setBackgroundDrawable(dialogParams.getRightButtonBackground());
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		rightButton.setLayoutParams(layoutParams);
		
		return rightButton;
	}
	
	/**
	 * 该方法的作用:
	 * 设置右按钮位置
	 * @author l00220455
	 * @date 2014年2月10日
	 */
	protected void setRightButtonPosition()
	{
		if(!showRightButton)
		{
			return;
		}
		
		RelativeLayout.LayoutParams rightParams = (RelativeLayout.LayoutParams) rightButton.getLayoutParams();
		//若存在中间按钮，则按照三个按钮计算大小，否则按两个按钮计算大小
		if(showMiddleButton)
		{
			//计算右按钮的位置
			rightParams.width = dialogParams.getButtonWidthOfThree();
			rightParams.rightMargin = (int)((dialogParams.getDialogWidth() - dialogParams.getButtonWidthOfThree() * 3) /6);
			
			//更新中间按钮的宽度
			RelativeLayout.LayoutParams middleParams = (RelativeLayout.LayoutParams)middleButton.getLayoutParams();
			middleParams.width = dialogParams.getButtonWidthOfThree();
			middleButton.setLayoutParams(middleParams);
		}
		else 
		{
			//计算右按钮的位置
			rightParams.width = dialogParams.getButtonWidthOfDouble();
			rightParams.rightMargin = (int)((dialogParams.getDialogWidth() - dialogParams.getButtonWidthOfDouble() * 2) /4);
		}
		
		rightButton.setLayoutParams(rightParams);
	}
	
	/**
	 * 该方法的作用:
	 *  获取底部按钮
	 * @author l00220455
	 * @date 2014年2月10日
	 * @param witch 按钮标识
	 * @return 按钮
	 */
	protected Button getBottomButton(int witch)
	{
		switch (witch)
		{
		case IDialog.BUTTON_LEFT:
			return leftButton;
		case IDialog.BUTTON_MIDDLE:
			return middleButton;
		case IDialog.BUTTON_RIGHT:
			return rightButton;

		default:
			return null;
		}
	}
	
	/**
	 * 该方法的作用:获取左边按钮
	 * @author yWX158243
	 * @date 2014年3月28日
	 * @return
	 */
	public Button getLeftButton(){
		return getBottomButton(IDialog.BUTTON_LEFT);
	}
	
	/**
	 * 该方法的作用:获取右边按钮
	 * @author yWX158243
	 * @date 2014年3月28日
	 * @return
	 */
	public Button getRightButton(){
		return getBottomButton(IDialog.BUTTON_RIGHT);
	}

	/**
	 * 该方法的作用:获取中间按钮
	 * @author yWX158243
	 * @date 2014年3月28日
	 * @return
	 */
	public Button getMiddleButton(){
		return getBottomButton(IDialog.BUTTON_MIDDLE);
	}
	
	/**
	 * 该方法的作用:
	 * 获取标题栏Layout
	 * @author l00220455
	 * @date 2014年2月10日
	 * @return
	 */
	protected LinearLayout getTitleLayout()
	{
		return titleLayout;
	}
	
	/**
	 * 该方法的作用:获取对话框头部Layout填充的view
	 * @author l00220455
	 * @date 2013-1-30
	 * @return 对话框头部视图
	 */
	public View getTitleContentView()
	{
		return this.titleLayout.getChildAt(0);
	}
	
	/**
	 * 设置标题栏自定义视图
	 * @param contentView 自定义视图
	 * @param layoutParams 视图显示参数
	 */
	public void setTitleContentView(View contentView, ViewGroup.LayoutParams layoutParams)
	{
		if(null == contentView)
		{
			return;
		}
		
		this.titleLayout.removeAllViews();
		if(null == layoutParams)
		{
			this.titleLayout.addView(contentView);
		}
		else 
		{
			this.titleLayout.addView(contentView, layoutParams);
		}
		
		isTitleCustom = true;
	}
	
	/**
	 * 设置标题栏可见性
	 * @param visibility
	 */
	public void setTitleVisibility(int visibility)
	{
		this.titleLayout.setVisibility(visibility);
	}
	
	/**
	 * 设置标题栏文本
	 * @param title
	 */
	public void setTitleText(CharSequence title)
	{
		if(isTitleCustom || null == title)
		{
			return;
		}
		
		titleTextView.setText(title);
		titleTextView.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 设置标题栏文本颜色
	 * @param color
	 */
	public void setTitleTextColor(int color)
	{
		if(isTitleCustom)
		{
			return;
		}
		
		titleTextView.setTextColor(color);
		titleTextView.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 获取Body区域Layout
	 * @return
	 */
	protected LinearLayout getBodyLayout()
	{
		return bodyLayout;
	}
	
	/**
	 * 设置中间显示区域自定义视图
	 * @param contentView 自定义视图
	 * @param layoutParams 视图显示参数
	 */
	public void setBodyContentView(View contentView, ViewGroup.LayoutParams layoutParams)
	{
		if(null == contentView)
		{
			return;
		}
		
		this.bodyLayout.removeAllViews();
		if(null == layoutParams)
		{
			this.bodyLayout.addView(contentView);
		}
		else 
		{
			this.bodyLayout.addView(contentView, layoutParams);
		}
		
		isBodyCustom = true;
	}
	
	/**
	 * 获取Body区域文本视图
	 * @return
	 */
	public TextView getBodyTextView()
	{
		return bodyTextView;
	}
	
	/**
	 * 设置中间显示区域可见性
	 * @param visibility
	 */
	public void setBodyVisibility(int visibility)
	{
		this.bodyLayout.setVisibility(visibility);
	}
	
	/**
	 * 设置中间显示区域文本内容
	 * @param text
	 */
	public void setBodyText(CharSequence text)
	{
		if(isBodyCustom || null == text)
		{
			return;
		}
		
		bodyTextView.setText(text);
		bodyTextView.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 设置中间显示区域文本颜色
	 * @param color
	 */
	public void setBodyTextColor(int color)
	{
		if(isBodyCustom)
		{
			return;
		}
		
		bodyTextView.setTextColor(color);
		bodyTextView.setVisibility(View.VISIBLE);
	}

	/**
	 * 获取底部操作栏Layout
	 * @return
	 */
	protected RelativeLayout getBottomLayout()
	{
		return bottomLayout;
	}
	
	/**
	 * 设置底部操作栏自定义视图
	 * @param contentView 自定义视图
	 * @param layoutParams 视图显示参数
	 */
	public void setBottomContentView(View contentView, ViewGroup.LayoutParams layoutParams)
	{
		if(null == contentView)
		{
			return;
		}
		
		this.bottomLayout.removeAllViews();
		if(null == layoutParams)
		{
			this.bottomLayout.addView(contentView);
		}
		else 
		{
			this.bottomLayout.addView(contentView, layoutParams);
		}
		
		isBottomCustom = true;
		showLeftButton = false;
		showRightButton = false;
		showMiddleButton = false;
	}
	
	/**
	 * 设置底部操作栏视图可见性
	 * @param visibility
	 */
	public void setBottomVisibility(int visibility)
	{
		bottomLayout.setVisibility(visibility);
	}
	
	/**
	 * 设置对话框背景
	 * @param drawable 对话框背景
	 */
	@Override
	public void setBackgroundDrawable(Drawable drawable)
	{
		rootView.setBackgroundDrawable(drawable);
	}
	
	/**
	 * 该方法的作用:
	 * 获取根视图
	 * @author l00220455
	 * @date 2014年1月6日
	 * @return
	 */
	protected LinearLayout getRootView()
	{
		return rootView;
	}
	
	/**
	 * 该方法的作用:
	 * 获取对话框参数
	 * @author l00220455
	 * @date 2014年1月6日
	 * @return
	 */
	public MPDialogParams getDialogParams()
	{
		return dialogParams;
	}
	
	/**
	 * 该方法的作用:
	 * 设置对话框参数
	 * @author l00220455
	 * @date 2014年1月6日
	 * @param dialogParams
	 */
	public void setDialogParams(MPDialogParams dialogParams)
	{
		this.dialogParams = dialogParams;
	}
	
	@Override
	public Window getWindow()
	{
		return super.getWindow();
	}
}
