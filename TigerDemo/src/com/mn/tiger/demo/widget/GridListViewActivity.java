package com.mn.tiger.demo.widget;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.adpter.TGGridListAdapter;
import com.mn.tiger.widget.adpter.TGGridListViewHolder;

public class GridListViewActivity extends TGActionBarActivity
{
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		listView = new ListView(this);
		setContentView(listView);
		ViewInjector.initInjectedView(this, this);
		
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		for (int i = 0; i < 5; i++)
		{
			arrayList.add(i);
		}
		
		listView.setAdapter(new TGGridListAdapter<Integer>(this, arrayList, R.layout.grid_list_item, 
				GridViewHolder.class, 4));
	}
	
	public static class GridViewHolder extends TGGridListViewHolder<Integer>
	{
		@ViewById(id = R.id.grid_list_item_image)
		private ImageView imageView;
		
		@ViewById(id = R.id.grid_list_item_text)
		private TextView textView;
		
		@Override
		protected void fillData(int rowIndex, int columnIndex, Integer itemData, 
				View childGridView, LinearLayout rowLayout, ViewGroup parent)
		{
			imageView.setBackgroundResource(R.drawable.ic_launcher);
			textView.setText(itemData + "");
			textView.setTextSize(24);
		}
	}
}
