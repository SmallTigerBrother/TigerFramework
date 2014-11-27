package com.mn.tiger.utility;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtils
{
	public static int CUSTOM_LAYOUT_RES_ID = 0;
	public static int CUSTOM_LAYOUT_TEXT_VIEW_ID = 0;
	public static int DURATION = Toast.LENGTH_SHORT;
	private static Toast mLastToast;

	public static void showToast(Context ctx, String message)
	{
		showToast(ctx, message, Toast.LENGTH_LONG);
	}

	public static void showToast(Context ctx, int msgResId)
	{
		if (ctx != null)
		{
			String message = ctx.getString(msgResId);
			showToast(ctx, message, DURATION);
		}
	}

	private static Toast getCustomToast(Context ctx, int layoutResId, int textViewId, String msg)
	{
		View customView = LayoutInflater.from(ctx).inflate(layoutResId, null);
		TextView textView = (TextView) customView.findViewById(textViewId);
		Toast toast = new Toast(ctx);
		if (textView != null && customView != null)
		{
			textView.setText(msg);
			toast.setView(customView);
		}
		return toast;
	}

	public static void showToast(Context ctx, int msgResId, Object... args)
	{
		if (ctx != null)
		{
			String msg = ctx.getString(msgResId, args);
			showToast(ctx, msg);
		}
	}

	public static void showToast(Context ctx, String message, int duration)
	{
		if (!TextUtils.isEmpty(message) && ctx != null)
		{
			cancelLastToast();
			if (CUSTOM_LAYOUT_RES_ID != 0 && CUSTOM_LAYOUT_TEXT_VIEW_ID != 0)
			{
				mLastToast = getCustomToast(ctx, CUSTOM_LAYOUT_RES_ID, CUSTOM_LAYOUT_TEXT_VIEW_ID,
						message);
				mLastToast.setDuration(duration);
				mLastToast.setGravity(Gravity.CENTER, 0, 0);
				mLastToast.show();
			}
			else
			{
				Toast.makeText(ctx, message, duration).show();
			}
		}
	}

	public static void cancelLastToast()
	{
		if (mLastToast != null)
		{
			try
			{
				mLastToast.cancel();
			}
			catch (Exception e)
			{
			}
		}
	}

	public static void showToast(Context ctx, int msgResId, int duration)
	{
		if (ctx != null)
		{
			String msg = ctx.getString(msgResId, msgResId);
			showToast(ctx, msg, duration);
		}
	}
}
