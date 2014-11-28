package com.mn.tiger.demo.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;

/**
 * 部分自定义的导航条，仅示例middlelayout；
 * 完全重定义，可以参考SearchViewInActionBarActivity（将TGSearchView加入到TGNavigationBar中）
 */
public class NavigationBarActivity extends TGActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setNavigationBarVisible(true);
		setContentView(R.layout.activity_navigationbar);
		
		//设置背景色
		getNavigationBar().setBackgroundColor(Color.BLUE);
		
		//移除中间layout的所有视图
		getNavigationBar().getMiddleNaviLayout().removeAllViews();
		
		//将ImageView加入到中间layout
		ImageView imageView = new ImageView(this);
		imageView.setImageResource(R.drawable.tiger_search_submit_icon);
		getNavigationBar().getMiddleNaviLayout().addView(imageView);
	}
	
}
