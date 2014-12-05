package com.mn.tiger.share;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.log.Logger;
import com.mn.tiger.share.result.TGQQShareResult;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class TGQQEntryActivity extends TGActionBarActivity implements IUiListener
{
	public static final String SHARE_DATA_KEY = "shareData";
	
	public static final String SHARE_MSG_FACTORY_KEY = "msgFactory";
	
	public static final String SHARE_RESULT_HANDLER_KEY = "shareResultHandler";
	
	private static final Logger LOG = Logger.getLogger(TGQQEntryActivity.class);
	
	private Tencent tencent;
	
	private TGQQSharePlugin plugin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setVisible(false);
		
		plugin = getPlugin();
		if(null != plugin)
		{
			tencent = plugin.getTencent();
		}
		else
		{
			LOG.e("Your had not register qq shareplugin ever");
			return;
		}
		shareToQQ();
	}
	
	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		shareToQQ();
	}
	
	public TGQQSharePlugin getPlugin()
	{
		return (TGQQSharePlugin) TGSharePluginManager.getInstance().getPlugin(
				TGSharePluginManager.TAG_QQ);
	}
	
	protected void shareToQQ()
	{
		if(null != plugin)
		{
			plugin.share2QQ();
		}
		else
		{
			LOG.e("Your had not register qq shareplugin ever");
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(null != tencent)
		{
			tencent.onActivityResult(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onCancel()
	{
		JSONObject response = new JSONObject();
		try
		{
			response.put("ret", TGQQShareResult.USER_CANCEL);
		}
		catch (JSONException e)
		{
			LOG.e(e);
		}
		
		TGSharePluginManager.getInstance().postShareResult(TGSharePluginManager.TAG_QQ, 
				new TGQQShareResult(response));
	}

	@Override
	public void onError(UiError error)
	{
		TGSharePluginManager.getInstance().postShareResult(TGSharePluginManager.TAG_QQ, 
				new TGQQShareResult(error));
	}

	@Override
	public void onComplete(Object response)
	{
		TGSharePluginManager.getInstance().postShareResult(TGSharePluginManager.TAG_QQ, 
				new TGQQShareResult((JSONObject)response));
	}
}
