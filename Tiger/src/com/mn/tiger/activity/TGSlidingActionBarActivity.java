/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mn.tiger.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.mn.tiger.widget.slidingmenu.SlidingActivityHelper;
import com.mn.tiger.widget.slidingmenu.SlidingMenu;
import com.mn.tiger.widget.slidingmenu.SlidingMenu.SlideMode;
import com.mn.tiger.widget.slidingmenu.SlidingMenu.SlideTouchMode;

/**
 * The Class SlidingFragmentActivity.
 */
public class TGSlidingActionBarActivity extends TGActionBarActivity
{
	/** The m helper. */
	private SlidingActivityHelper mHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mHelper = new SlidingActivityHelper(this);
		mHelper.onCreate(savedInstanceState);
	}

	@Override
	public void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate(savedInstanceState);
	}

	@Override
	public View findViewById(int id)
	{
		View v = super.findViewById(id);
		if (v != null)
		{
			return v;
		}
			
		return mHelper.findViewById(id);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		mHelper.onSaveInstanceState(outState);
	}

	@Override
	public void setContentView(int id)
	{
		setContentView(getLayoutInflater().inflate(id, null));
	}

	@Override
	public void setContentView(View v)
	{
		setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, 
				LayoutParams.MATCH_PARENT));
	}

	@Override
	public void setContentView(View v, LayoutParams params)
	{
		super.setContentView(v, params);
		mHelper.registerAboveContentView(v, params);
	}

	public void setBehindContentView(int id)
	{
		setBehindContentView(getLayoutInflater().inflate(id, null));
	}

	public void setBehindContentView(View view)
	{
		mHelper.setBehindContentView(view);
	}
	
	public void setSecondaryBehindContentView(View view)
	{
		mHelper.setSecondaryBehindContentView(view);
	}

	public void setAboveOffset(int offset)
	{
		mHelper.setAboveOffset(offset);
	}

	public void setBehindOffset(int offset)
	{
		mHelper.setBehindOffset(offset);
	}
	
	public void setSildeMode(SlideMode mode)
	{
		mHelper.setSildeMode(mode);
	}
	
	public void setTouchModeAbove(SlideTouchMode mode)
	{
		mHelper.setTouchModeAbove(mode);
	}

	public SlidingMenu getSlidingMenu()
	{
		return mHelper.getSlidingMenu();
	}

	public void toggle()
	{
		mHelper.toggle();
	}

	public void showContent()
	{
		mHelper.showContent();
	}

	public void showMenu()
	{
		mHelper.showMenu();
	}

	public void showSecondaryMenu()
	{
		mHelper.showSecondaryMenu();
	}

	public void setSlidingActionBarEnabled(boolean b)
	{
		mHelper.setSlidingActionBarEnabled(b);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		boolean b = mHelper.onKeyUp(keyCode, event);
		if (b)
		{
			return b;
		}
		return super.onKeyUp(keyCode, event);
	}

}