package com.mn.tiger.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mn.tiger.utility.CR;
import com.mn.tiger.widget.TGImageButton;
import com.mn.tiger.widget.TGNavigationBar;

public abstract class TGFragment extends Fragment
{
	protected View mainView;
	
	/**
	 * 是否显示导航条
	 */
	private boolean navigationBarVisible = false;
	
	/**
	 * 导航条
	 */
	private TGNavigationBar navigationBar;
	
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) 
	{
		if(null == mainView)
		{
			mainView = inflater.inflate(CR.getLayoutId(getActivity(), "tiger_fragment"), null);
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
			
			FrameLayout paneLayout = (FrameLayout) mainView.findViewById(CR.getIdId(getActivity(),
					"panel"));
			paneLayout.addView(onCreateView(inflater, savedInstanceState));
		}
		return mainView;
	};
	
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
	
	public boolean isNavigationBarVisible()
	{
		return navigationBarVisible;
	}
	
	public void setNavigationBarVisible(boolean navigationBarVisible)
	{
		this.navigationBarVisible = navigationBarVisible;
	}
	
}