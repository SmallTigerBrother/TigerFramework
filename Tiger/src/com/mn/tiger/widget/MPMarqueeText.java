package com.mn.tiger.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 该类作用及功能说明 走马灯视图
 * 
 * @version V2.0
 * @see JDK1.6,android-8
 */
public class MPMarqueeText extends TextView
{
	public MPMarqueeText(Context con)
	{
		super(con);
	}

	public MPMarqueeText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public MPMarqueeText(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isFocused()
	{
		return true;
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect)
	{
	}
}
