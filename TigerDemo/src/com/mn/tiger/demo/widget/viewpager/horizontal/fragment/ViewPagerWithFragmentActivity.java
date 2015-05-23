package com.mn.tiger.demo.widget.viewpager.horizontal.fragment;

import android.graphics.Color;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.mn.tiger.app.TGTabActivity;
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
	private String[] defaultIconResName = {"tiger_search_submit_icon", 
			"tiger_search_close_press", "loading_icon_down"};
	
	/**
	 * tab高亮显示的资源
	 */
	private String[] highLightIconResName = {"loading_icon_round", 
			"tiger_search_submit_icon", "ic_launcher"};
	
	/**
	 * tab名称
	 */
	private String[] tabNames = {"Fragment_1", "Fragment_2", "Fragment_3"};
	
	@Override
	protected TabModel[] onInitTabs()
	{
		TabModel[] tabModels = new TabModel[3];
		TabModel tabModel;
		for(int i = 0 ; i < tabNames.length; i++)
		{
			tabModel = new TabModel();
			tabModel.setDefaultResName(defaultIconResName[i]);
			tabModel.setTabName(tabNames[i]);
			tabModel.setHighlightResName(highLightIconResName[i]);
			tabModel.setHighlightTextColor(Color.BLUE);
			tabModel.setHighlightTextSize(24f);
			
			switch (i)
			{
				case 0:
					tabModel.bindFragment(new PagerFragment_1());
					break;
					
				case 1:
					tabModel.bindFragment(new PagerFragment_2());
					break;
					
				case 2:
					tabModel.bindFragment(new PagerFragment_3());
					break;
				default:
					break;
			}
			
			tabModels[i] = tabModel;
		}
		
		return tabModels;
	}
}
