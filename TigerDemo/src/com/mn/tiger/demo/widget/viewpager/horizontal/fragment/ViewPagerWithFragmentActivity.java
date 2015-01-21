package com.mn.tiger.demo.widget.viewpager.horizontal.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.adpter.TGListAdapter;
import com.mn.tiger.widget.adpter.TGViewHolder;
import com.mn.tiger.widget.tab.TGTabView;
import com.mn.tiger.widget.tab.TGTabView.LayoutParams;
import com.mn.tiger.widget.tab.TGTabView.OnTabChangeListener;
import com.mn.tiger.widget.viewpager.TGFragmentPagerAdapter;

/**
 * 实现类似底部带Tab，分页是Fragment的界面
 * @author Dalang
 */
public class ViewPagerWithFragmentActivity extends TGActionBarActivity implements 
    OnTabChangeListener, OnPageChangeListener
{
	@ViewById(id = R.id.view_pager)
	private ViewPager viewPager;
	
	@ViewById(id = R.id.view_pager_tab)
	private TGTabView tabView;
	
	/**
	 * 填充到ViewPager中的Fragment数组
	 */
	private ArrayList<Fragment> fragments;
	
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
		setContentView(R.layout.viewpager_fragment_tabview_activity);
		ViewInjector.initInjectedView(this, this);
		
		setupViews();
	}

	private void setupViews()
	{
		//初始化ViewPager
		fragments = new ArrayList<Fragment>();
		fragments.add(new PagerFragment_1());
		fragments.add(new PagerFragment_2());
		fragments.add(new PagerFragment_3());
		
		viewPager.setAdapter(new TGFragmentPagerAdapter(getSupportFragmentManager(), 
				fragments));
		viewPager.setOnPageChangeListener(this);
		
		//初始化TabView
		ArrayList<TabModel> tabModels = new ArrayList<TabModel>();
		TabModel tabModel;
		for(int i = 0 ; i < tabNames.length; i++)
		{
			tabModel = new TabModel();
			tabModel.setImageResId(tabDefaultIconResId[i]);
			tabModel.setTabName(tabNames[i]);
			tabModel.setHighlightResId(tabHighLightIconResId[i]);
			tabModels.add(tabModel);
		}
		
		tabView.setAdapter(new TGListAdapter<TabModel>(this, tabModels,
				R.layout.fragment_tab_item, TabViewHolder.class));
		tabView.setOnTabChangeListener(this);
		tabView.setSelection(0);
	}
	
	@Override
	public void onTabChanged(TGTabView tabView, int lastTabIndex, int currentTabIndex)
	{
		// 还原上一个选中的tab, 若lastTabIndex > 0，则说明存在已选中的tab
		if (lastTabIndex >= 0)
		{
			TabViewHolder holder = (TabViewHolder) tabView.getTabItem(lastTabIndex).getConvertView().getTag();
			TabModel tabModel = (TabModel) tabView.getAdapter().getItem(lastTabIndex);
			holder.getImageView().setImageResource(tabModel.getImageResId());
		}
		
		// 高亮显示当前选中的tab
		TabViewHolder holder = (TabViewHolder) tabView.getTabItem(currentTabIndex).getConvertView().getTag();
		TabModel tabModel = (TabModel) tabView.getAdapter().getItem(currentTabIndex);
		holder.getImageView().setImageResource(tabModel.getHighlightResId());

		// 切换Page
		viewPager.setCurrentItem(currentTabIndex, false);
	}

	@Override
	public void onPageSelected(int page)
	{
		tabView.setSelection(page);
	}
	
	@Override
	public void onPageScrollStateChanged(int state)
	{
	}

	@Override
	public void onPageScrolled(int page, float arg1, int arg2)
	{
	}
	
	public class TabViewHolder extends TGViewHolder<TabModel>
	{
		@ViewById(id = R.id.tab_item_image)
		private ImageView imageView;
		
		@ViewById(id = R.id.tab_item_name)
		private TextView textView;
		
		@Override
		public View initView(View convertView, ViewGroup parent)
		{
			View view = super.initView(convertView, parent);
			LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 
					LayoutParams.WRAP_CONTENT, 1);
			view.setLayoutParams(layoutParams);
			return view;
		}
		
		@Override
		public void fillData(ViewGroup parent, View convertView, TabModel itemData, int position)
		{
			imageView.setImageResource(itemData.getImageResId());
			textView.setText(itemData.getTabName());
		}
		
		public ImageView getImageView()
		{
			return imageView;
		}
	}
	
	public static class TabModel
	{
		private int imageResId;
		
		private int highlightResId;
		
		private String tabName;

		public int getImageResId()
		{
			return imageResId;
		}

		public void setImageResId(int imageResId)
		{
			this.imageResId = imageResId;
		}

		public String getTabName()
		{
			return tabName;
		}

		public void setTabName(String tabName)
		{
			this.tabName = tabName;
		}

		public int getHighlightResId()
		{
			return highlightResId;
		}

		public void setHighlightResId(int highlightResId)
		{
			this.highlightResId = highlightResId;
		}
	}
}
