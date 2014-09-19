package com.mn.tiger.activity;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
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
import com.mn.tiger.widget.MPImageButton;
import com.mn.tiger.widget.MPNavigationBar;

/**
 * 该类作用及功能说明 Activity基类，重载导航条方法
 * 
 * @version V2.0
 * @see JDK1.6,android-8
 */
public class TGActivity extends Activity
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 是否显示导航条
	 */
	private int navigationBarVisibility = View.VISIBLE;
	
	/**
	 * 检查服务返回数据错误处理器
	 */
	private IHttpErrorHandler httpErrorHandler = null;
	
	/**
	 * 导航条
	 */
	private MPNavigationBar navigationBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		this.httpErrorHandler = initHttpErrorHandler();
		// 添加到Application中
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
			navigationBar = (MPNavigationBar) findViewById(CR.getIdId(this, "mjet_navigationbar"));
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
			navigationBar = (MPNavigationBar) findViewById(CR.getIdId(this, "mjet_navigationbar"));
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
	protected void initNavigationResource(MPNavigationBar navigationBar)
	{
		navigationBar.setBackgroundResource(CR.getDrawableId(this, "mjet_header_background"));

		navigationBar.getLeftNaviButton().setBackgroundResource(
				CR.getDrawableId(this, "mjet_nav_back_button_selector"));

		showLeftBarButton(true);
		navigationBar.getLeftNaviButton().setOnClickListener(new OnClickListener()
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
	 * 该方法的作用:
	 * 登陆成功后回调方法
	 * @date 2014年1月3日
	 */
	public void loginSucceed()
	{

	}

	/**
	 * 该方法的作用:导航条是否显示
	 * @date 2013-10-10
	 * @return
	 */
	protected int getNavigationBarVisibility()
	{
		return navigationBarVisibility;
	}
	
	/**
	 * 该方法的作用:
	 * 初始化Http请求异常处理接口
	 * @date 2014年2月11日
	 * @return
	 */
	protected IHttpErrorHandler initHttpErrorHandler()
	{
		return new TGHttpErrorHandler(this);
	}

	/**
	 * 该方法的作用:
	 * 获取Http请求异常处理接口
	 * @date 2014年2月11日
	 * @return
	 */
	public IHttpErrorHandler getHttpErrorHandler()
	{
		return httpErrorHandler;
	}
	
	@Override
	protected void onDestroy()
	{
		Application application = getApplication();
		if(application instanceof IApplication)
		{
			((IApplication)application).removeActivityFromStack(this);
		}
		super.onDestroy();
	}
	
	public MPImageButton getLeftBarButton()
	{
		if (null != navigationBar)
		{
			return navigationBar.getLeftNaviButton();
		}

		return null;
	}

	public MPImageButton getRightBarButton()
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
	public MPNavigationBar getNavigationBar()
	{
		return navigationBar;
	}

	public TextView getMiddleTextView()
	{
		if (null != navigationBar)
		{
			return navigationBar.getMiddleTextView();
		}
		return null;
	}

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

	public void showLeftBarButton(boolean show)
	{
		MPImageButton leftButton = getLeftBarButton();
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

	public void showRightBarButton(boolean show)
	{
		MPImageButton rightButton = getRightBarButton();
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
	
	protected void setNavigationBarVisibility(int visibility)
	{
		this.navigationBarVisibility = visibility;
		if(null != navigationBar)
		{
			navigationBar.setVisibility(visibility);
		}
	}
}
