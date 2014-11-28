package com.mn.tiger.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.mn.tiger.app.TGSlidingActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.slidingmenu.SlidingMenu.SlideMode;
import com.mn.tiger.widget.slidingmenu.SlidingMenu.SlideTouchMode;

/**
 *带侧滑栏的Activity，demo展示了带左菜单、右菜单的界面
 */
public class SlidingActivity extends TGSlidingActionBarActivity implements OnClickListener
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setNavigationBarVisible(true);
		
		//设置导航条可以跟随侧滑栏滑动
		setSlidingActionBarEnabled(true);
		
		//设置中间主视图
		setContentView(R.layout.sliding_content_view);
		//设置左菜单
		setLeftContentView(R.layout.left_menu);
		//设置右菜单
		setRightContentView(R.layout.right_menu);
		
		//设置菜单宽度（底部便宜量）
		setBehindOffset(200);
		//设置滑动模式
		setSildeMode(SlideMode.LEFT_RIGHT);
		//设置触摸方式
		setTouchModeAbove(SlideTouchMode.TOUCHMODE_FULLSCREEN);
		
		//设置导航条
		setBarTitleText("TigerDemo");
		getLeftBarButton().setText("LeftMenu");
		showRightBarButton(true);
		getRightBarButton().setText("RightMenu");
		getLeftBarButton().setOnClickListener(this);
		getRightBarButton().setOnClickListener(this);
	}
	
	@Override
	public void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		//必须在onPostCreate方法中进行注入
		ViewInjector.initInjectedView(this, this);
	}
	
	@Override
	public void onClick(View v)
	{
		if(v.equals(getLeftBarButton()))
		{
			toggleLeftMenu();
		}
		else if(v.equals(getRightBarButton()))
		{
			toggleRightMenu();
		}
	}
}
