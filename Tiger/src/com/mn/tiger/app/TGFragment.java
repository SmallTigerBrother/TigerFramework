package com.mn.tiger.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mn.tiger.utility.CR;
import com.mn.tiger.widget.TGImageButton;
import com.mn.tiger.widget.TGNavigationBar;

/**
 * 自定义Fragment基类
 */
public abstract class TGFragment extends Fragment
{
	/**
	 * 主视图
	 */
	private View mainView;
	
	/**
	 * 是否显示导航条
	 */
	private boolean navigationBarVisible = false;
	
	/**
	 * 导航条
	 */
	private TGNavigationBar navigationBar;
	
	private FragmentActivity activity;
	
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) 
	{
		if(null == mainView)
		{
			mainView = inflater.inflate(CR.getLayoutId(getActivity(), "tiger_fragment"), null);
			//初始化导航条
			navigationBar = (TGNavigationBar) mainView.findViewById(CR.getIdId(getActivity(),
					"navigationbar"));
			initNavigationResource(navigationBar);
			if(navigationBarVisible)
			{
				navigationBar.setVisibility(View.VISIBLE);
			}
			else
			{
				navigationBar.setVisibility(View.GONE);
			}
			
			//加入自定义视图
			FrameLayout paneLayout = (FrameLayout) mainView.findViewById(CR.getIdId(getActivity(),
					"panel"));
			FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
			paneLayout.addView(onCreateView(inflater, savedInstanceState), layoutParams);
		}
		else
		{
			((ViewGroup)mainView.getParent()).removeView(mainView);
		}
		
		afterCreateView();
		
		return mainView;
	};
	
	/**
	 * 在onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState)中调用，初始化完成视图后调用
	 */
	protected void afterCreateView()
	{
		
	}
	
	/**
	 * 创建视图，Fragment的onCreateView方法中调用，只有首次创建视图时调用，若视图可以重用，则不会调用该方法
	 * @param inflater
	 * @param savedInstanceState
	 * @return
	 */
	protected abstract View onCreateView(LayoutInflater inflater,
			Bundle savedInstanceState);
	
	/**
	 * 该方法的作用: 初始化导航条资源
	 * @date 2013-11-8
	 * @param navigationBar
	 */
	protected void initNavigationResource(TGNavigationBar navigationBar)
	{
	}
	
	/**
	 * 该方法的作用: 获取导航条左按钮
	 * 
	 * @date 2013-11-18
	 * @return
	 */
	public TGImageButton getLeftBarButton()
	{
		return navigationBar.getLeftNaviButton();
	}

	/**
	 * 该方法的作用: 获取导航条右按钮
	 * 
	 * @date 2013-11-18
	 * @return
	 */
	public TGImageButton getRightBarButton()
	{
		return navigationBar.getRightNaviButton();
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
		return navigationBar.getMiddleTextView();
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
	
	/**
	 * 导航条是否显示
	 * @return
	 */
	public boolean isNavigationBarVisible()
	{
		return navigationBarVisible;
	}
	
	/**
	 * 设置导航条是否显示
	 * @param navigationBarVisible
	 */
	public void setNavigationBarVisible(boolean navigationBarVisible)
	{
		this.navigationBarVisible = navigationBarVisible;
	}
	
	/**
	 * 设置activity
	 * @param activity
	 */
	public void setActivity(FragmentActivity activity)
	{
		this.activity = activity;
	}
	
	protected FragmentActivity getFragmentActivity()
	{
		if(null == activity)
		{
			return getActivity();
		}
		
		return activity;
	}
}
