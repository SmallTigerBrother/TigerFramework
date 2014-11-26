package com.mn.tiger.app;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.TextView;

import com.mn.tiger.utility.CR;
import com.mn.tiger.widget.TGImageButton;
import com.mn.tiger.widget.TGNavigationBar;

/**
 * 带导航条的Activity基类
 */
public class TGActionBarActivity extends ActionBarActivity
{
	/**
	 * 导航条
	 */
	private TGNavigationBar navigationBar;

	/**
	 * 是否显示导航条
	 */
	private boolean navigationBarVisible = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//添加到Application中
		((TGApplication)getApplication()).addActivityToStack(this);
	}

	@Override
	public void setContentView(View view, LayoutParams params)
	{
		if (navigationBarVisible)
		{
			initNavigationBar();
		}
		
		super.setContentView(view, params);
	}
	
	@Override
	public void setContentView(int layoutResID)
	{
		LayoutInflater inflater = LayoutInflater.from(this);
		View contentView = inflater.inflate(layoutResID, null);
		
		setContentView(contentView);
	}

	@Override
	public void setContentView(View view)
	{
		if (navigationBarVisible)
		{
			initNavigationBar();
		}
		
		super.setContentView(view);
	}
	
	/**
	 * 初始化导航条，将导航条设置到ActionBar中
	 */
	private void initNavigationBar()
	{
		ActionBar actionBar = getSupportActionBar();
		if(null != actionBar)
		{
			//将导航条设置到ActionBar中
			navigationBar = new TGNavigationBar(this);
			initNavigationResource(navigationBar);
			
			ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
					ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
			
			actionBar.setCustomView(navigationBar, layoutParams);
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		}
		else
		{
			//actionbar为null有两种可能，一种是theme设置错误，一种是Window设置成NoTitle
			throw new RuntimeException("The ActionBar is null, please check the activity or application theme, "
					+ "and make sure you do not use Window’s feature == FEATURE_NO_TITLE");
		}
	}

	/**
	 * 该方法的作用: 初始化导航条资源
	 * @date 2013-11-8
	 * @param navigationBar
	 */
	protected void initNavigationResource(TGNavigationBar navigationBar)
	{
		navigationBar.setBackgroundResource(CR.getDrawableId(this,
				"tiger_header_background"));

		navigationBar.getLeftNaviButton().setBackgroundResource(
				CR.getDrawableId(this, "tiger_nav_back_button_selector"));

		showLeftBarButton(true);
		navigationBar.getLeftNaviButton().setOnClickListener(
				new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						finish();
					}
				});
		navigationBar.getRightNaviButton().setBackgroundResource(
				CR.getDrawableId(this, "tiger_nav_refresh_button_selector"));
	}

	/**
	 * 该方法的作用: 获取导航条左按钮
	 * 
	 * @date 2013-11-18
	 * @return
	 */
	public TGImageButton getLeftBarButton()
	{
		if (null != navigationBar)
		{
			return navigationBar.getLeftNaviButton();
		}

		return null;
	}

	/**
	 * 该方法的作用: 获取导航条右按钮
	 * 
	 * @date 2013-11-18
	 * @return
	 */
	public TGImageButton getRightBarButton()
	{
		if (null != navigationBar)
		{
			return navigationBar.getRightNaviButton();
		}

		return null;
	}

	/**
	 * 该方法的作用: 获取导航条
	 * 
	 * @date 2013-11-8
	 * @return
	 */
	public TGNavigationBar getNavigationBar()
	{
		return navigationBar;
	}

	/**
	 * 该方法的作用: 获取导航条中间TextView
	 * 
	 * @date 2013-11-18
	 * @return
	 */
	public TextView getMiddleTextView()
	{
		if (null != navigationBar)
		{
			return navigationBar.getMiddleTextView();
		}

		return null;
	}

	/**
	 * 该方法的作用: 设置导航条标题文本
	 * 
	 * @date 2013-11-18
	 * @param titleText
	 * @return
	 */
	public boolean setBarTitleText(String titleText)
	{
		TextView middleTextView = getMiddleTextView();
		if (null != middleTextView)
		{
			middleTextView.setText(titleText);
			return true;
		}

		return false;
	}

	/**
	 * 该方法的作用: 显示导航条左按钮
	 * 
	 * @date 2013-11-18
	 * @param show
	 */
	public void showLeftBarButton(boolean show)
	{
		TGImageButton leftButton = getLeftBarButton();
		if (null != leftButton)
		{
			if (show)
			{
				leftButton.setVisibility(View.VISIBLE);
			}
			else
			{
				leftButton.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 该方法的作用: 显示导航条右按钮
	 * 
	 * @date 2013-11-18
	 * @param show
	 */
	public void showRightBarButton(boolean show)
	{
		TGImageButton rightButton = getRightBarButton();
		if (null != rightButton)
		{
			if (show)
			{
				rightButton.setVisibility(View.VISIBLE);
			}
			else
			{
				rightButton.setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected void onDestroy() 
	{
		((TGApplication)getApplication()).removeActivityFromStack(this);
		super.onDestroy();
	}
	
	/**
	 * 设置导航条是否可见
	 * @param navigationBarVisible
	 */
	public void setNavigationBarVisible(boolean navigationBarVisible)
	{
		this.navigationBarVisible = navigationBarVisible;
		if(!navigationBarVisible)
		{
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
	}

}
