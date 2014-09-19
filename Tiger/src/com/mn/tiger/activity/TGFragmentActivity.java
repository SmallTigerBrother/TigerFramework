package com.mn.tiger.activity;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mn.tiger.app.IApplication;
import com.mn.tiger.request.error.IHttpErrorHandler;
import com.mn.tiger.request.error.TGHttpErrorHandler;
import com.mn.tiger.utility.CR;
import com.mn.tiger.widget.TGImageButton;
import com.mn.tiger.widget.TGNavigationBar;

public class TGFragmentActivity extends FragmentActivity
{
	/**
	 * 检查服务返回数据错误处理器
	 */
	private IHttpErrorHandler httpErrorHandler = null;

	/**
	 * 导航条
	 */
	private TGNavigationBar navigationBar;

	/**
	 * 是否显示导航条
	 */
	private int navigationBarVisibility = View.VISIBLE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		this.httpErrorHandler = initHttpErrorHandler();
		
		//添加到Application中
		Application application = getApplication();
		if(application instanceof IApplication)
		{
			((IApplication)application).addActivityToStack(this);
		}
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
		if (getNavigationBarVisibility() == View.VISIBLE)
		{
			LayoutInflater inflater = LayoutInflater.from(this);
			View mainView = inflater.inflate(CR.getLayoutId(this, "mjet_base_activity"), null);
			super.setContentView(mainView, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));

			LinearLayout panelContent = (LinearLayout) findViewById(
					CR.getIdId(this, "mjet_panel"));

			panelContent.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			navigationBar = (TGNavigationBar) findViewById(CR.getIdId(this, "mjet_navigationbar"));
			initNavigationResource(navigationBar);
		}
		else if(getNavigationBarVisibility() == View.GONE)
		{
			LayoutInflater inflater = LayoutInflater.from(this);
			View mainView = inflater.inflate(CR.getLayoutId(this, "mjet_base_activity"), null);
			super.setContentView(mainView, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));

			LinearLayout panelContent = (LinearLayout) findViewById(
					CR.getIdId(this, "mjet_panel"));

			panelContent.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			navigationBar = (TGNavigationBar) findViewById(CR.getIdId(this, "mjet_navigationbar"));
			initNavigationResource(navigationBar);
			navigationBar.setVisibility(View.GONE);
		}
		else
		{
			super.setContentView(view,new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));
		}
	}

	/**
	 * 该方法的作用: 初始化导航条资源
	 * 
	 * @date 2013-11-8
	 * @param navigationBar
	 */
	protected void initNavigationResource(TGNavigationBar navigationBar)
	{
		navigationBar.setBackgroundResource(CR.getDrawableId(this,
				"mjet_header_background"));

		navigationBar.getLeftNaviButton().setBackgroundResource(
				CR.getDrawableId(this, "mjet_nav_back_button_selector"));

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
				CR.getDrawableId(this, "mjet_nav_refresh_button_selector"));

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

	protected void onDestroy() 
	{
		Application application = getApplication();
		if(application instanceof IApplication)
		{
			((IApplication)application).removeActivityFromStack(this);
		}
		super.onDestroy();
	}
	
	protected IHttpErrorHandler initHttpErrorHandler()
	{
		return new TGHttpErrorHandler(this);
	}

	public IHttpErrorHandler getHttpErrorHandler()
	{
		return httpErrorHandler;
	}

	public int getNavigationBarVisibility()
	{
		return navigationBarVisibility;
	}

	public void setNavigationBarVisibility(int navigationBarVisibility)
	{
		this.navigationBarVisibility = navigationBarVisibility;
	}

}
