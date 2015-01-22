package com.mn.tiger.demo.widget.viewpager.horizontal.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.mn.tiger.app.TGTabActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.widget.tab.TGTabView.OnTabChangeListener;

/**
 * 实现类似底部带Tab，分页是Fragment的界面
 * @author Dalang
 */
public class ViewPagerWithFragmentActivity extends TGTabActivity implements 
    OnTabChangeListener, OnPageChangeListener
{
	/**
	 * tab默认显示的资源
	 */
	private int[] tabDefaultIconResId = {R.drawable.tiger_search_submit_icon, 
			R.drawable.tiger_search_close_press, R.drawable.loading_icon_down};
	
	/**
	 * tab高亮显示的资源
	 */
	private int[] tabHighLightIconResId = {R.drawable.loading_icon_round, 
			R.drawable.tiger_search_submit_icon, R.drawable.ic_launcher};
	
	/**
	 * tab名称
	 */
	private String[] tabNames = {"Fragment_1", "Fragment_2", "Fragment_3"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Fragment[] fragments = new Fragment[3];
		fragments[0] = new PagerFragment_1();
		fragments[1] = new PagerFragment_2();
		fragments[2] = new PagerFragment_3();
		
		TabModel[] tabModels = new TabModel[3];
		TabModel tabModel;
		for(int i = 0 ; i < tabNames.length; i++)
		{
			tabModel = new TabModel();
			tabModel.setDefaultResId(tabDefaultIconResId[i]);
			tabModel.setTabName(tabNames[i]);
			tabModel.setHighlightResId(tabHighLightIconResId[i]);
			tabModel.setHighlightTextColor(Color.BLUE);
			tabModel.setHighlightTextSize(24f);
			tabModels[i] = tabModel;
		}
		
		setTabs(tabModels, fragments);
	}
}
