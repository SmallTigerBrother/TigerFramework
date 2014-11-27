package com.mn.tiger.demo;


import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.activity.NavigationBarActivity;
import com.mn.tiger.demo.activity.SlidingActivity;
import com.mn.tiger.demo.template.viewpager.horizontal.HorizontalViewPagerActivity;
import com.mn.tiger.demo.template.viewpager.horizontal.fragment.ViewPagerWithFragmentActivity;
import com.mn.tiger.demo.template.viewpager.vertical.VerticalViewPagerActivity;
import com.mn.tiger.demo.widget.searchview.SearchViewActivity;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.adpter.TGListAdapter;
import com.mn.tiger.widget.adpter.TGViewHolder;

public class MainActivity extends TGActionBarActivity implements OnItemClickListener
{
	@ViewById(id = R.id.demo_list)
	private ListView demoListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		ViewInjector.initInjectedView(this, this);
		
		ArrayList<DemoModel> demoModels = new ArrayList<MainActivity.DemoModel>();
		demoModels.add(DemoModel.ActionBarDemo);
		demoModels.add(DemoModel.SildingMenuDemo);
		demoModels.add(DemoModel.HorizontalViewPagerWithFragment);
		demoModels.add(DemoModel.HorizontalViewPager);
		demoModels.add(DemoModel.VerticalViewPager);
		
		demoListView.setAdapter(new TGListAdapter<DemoModel>(this, demoModels, -1, 
				ViewHolder.class));
		demoListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, 
			int position, long id)
	{
		Intent intent = new Intent();
		DemoModel itemData = (DemoModel) parent.getAdapter().getItem(position);
		switch (itemData)
		{
			case ActionBarDemo:
				intent.setClass(this, NavigationBarActivity.class);
				break;

			case SildingMenuDemo:
				intent.setClass(this, SlidingActivity.class);
				break;

			case HorizontalViewPagerWithFragment:
				intent.setClass(this, ViewPagerWithFragmentActivity.class);
				break;
				
			case VerticalViewPager:
				intent.setClass(this, VerticalViewPagerActivity.class);
				break;
				
			case HorizontalViewPager:
				intent.setClass(this, HorizontalViewPagerActivity.class);
				break;
				
			case SearchViewActivity:
				intent.setClass(this, SearchViewActivity.class);
				break;

			default:
				break;
		}
		this.startActivity(intent);
	}
	
	public static class ViewHolder extends TGViewHolder<DemoModel>
	{
		private TextView textView;
		
		@Override
		public View initView(View convertView)
		{
			textView = new TextView(getActivity());
			textView.setTextSize(24);
			return textView;
		}
		
		@Override
		public void fillData(DemoModel itemData, int position)
		{
			textView.setText(itemData.demoType);
		}
	}
	
	public static enum DemoModel
	{
		ActionBarDemo("ActionBar"),
		SildingMenuDemo("SldingMenu"),
		
		SearchViewActivity("SearchViewActivity"),
		
		HorizontalViewPager("HorizontalViewPager"),
		
		VerticalViewPager("VerticalViewPager"),
		
		HorizontalViewPagerWithFragment("HorizontalViewPager + Fragment + TabView");
		
		private DemoModel(String demoType)
		{
			this.demoType = demoType;
		}
		
		public final String demoType;
	}
}
