package com.mn.tiger.demo.template.viewpager.horizontal.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mn.tiger.app.TGFragment;
import com.mn.tiger.demo.R;

public class PagerFragment_3 extends TGFragment
{
	@Override
	protected View onCreateView(LayoutInflater inflater, Bundle arg1)
	{
		View view = inflater.inflate(R.layout.pager_fragment, null);
		view.setBackgroundColor(Color.GREEN);
		return view;
	}

}
