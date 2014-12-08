package com.mn.tiger.demo.share;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.demo.share.plugin.QQSharePlugin;
import com.mn.tiger.demo.share.plugin.QQZoneSharePlugin;
import com.mn.tiger.demo.share.plugin.WeiBoSharePlugin;
import com.mn.tiger.demo.share.plugin.WeiChatSharePlugin;
import com.mn.tiger.demo.share.plugin.WeiChatTimeLineSharePlugin;
import com.mn.tiger.share.IShareResultHandler;
import com.mn.tiger.share.TGQQSharePlugin.TGQQShareMsgBuilder;
import com.mn.tiger.share.TGQQZoneSharePlugin.TGQQZoneShareMsgBuilder;
import com.mn.tiger.share.TGSharePluginManager;
import com.mn.tiger.share.TGWeiBoSharePlugin.TGWeiBoMsgBuilder;
import com.mn.tiger.share.TGWeiChatSharePlugin.TGWeiChatMsgBuilder;
import com.mn.tiger.share.result.TGQQShareResult;
import com.mn.tiger.share.result.TGWeiChatShareResult;
import com.mn.tiger.share.result.TGWeiboShareResult;
import com.mn.tiger.utility.ViewInjector;

public class ShareDemoActivity extends TGActionBarActivity implements OnClickListener
{
	@ViewById(id = R.id.share_to_weichat_btn)
	private Button shareToWeiChatBtn;
	
	@ViewById(id = R.id.share_to_weichat_timeline_btn)
	private Button shareToWeiChatTimeLineBtn;
	
	@ViewById(id = R.id.share_to_qq_btn)
	private Button shareToQQBtn;
	
	@ViewById(id = R.id.share_to_qqzone_btn)
	private Button shareToQQZoneBtn;
	
	@ViewById(id = R.id.share_to_weibo_btn)
	private Button shareToWeiBoBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ViewInjector.initInjectedView(this, this);
		setupViews();
	}
	
	private void setupViews()
	{
		shareToWeiChatBtn.setOnClickListener(this);
		shareToWeiChatTimeLineBtn.setOnClickListener(this);
		shareToQQBtn.setOnClickListener(this);
		shareToQQZoneBtn.setOnClickListener(this);
		shareToWeiBoBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.share_to_weichat_btn:
				shareToWeiChat();
				break;
			case R.id.share_to_weichat_timeline_btn:
				shareToWeiChatTimeLine();
				break;
				
			case R.id.share_to_qq_btn:
				shareToQQ();
				break;
				
			case R.id.share_to_qqzone_btn:
				shareToQQZone();
				break;
				
			case R.id.share_to_weibo_btn:
				shareToWeiBo();
				break;

			default:
				break;
		}
	}
	
	private void shareToWeiChat()
	{
		TGWeiChatMsgBuilder msgBuilder = new TGWeiChatMsgBuilder(0);
		msgBuilder.setTitle("Test");
		
		WeiChatSharePlugin weiChatSharePlugin = 
				(WeiChatSharePlugin) TGSharePluginManager.getInstance().getPlugin(
						TGSharePluginManager.TAG_WEI_CHAT);
		
		weiChatSharePlugin.share(this, msgBuilder, new IShareResultHandler<TGWeiChatShareResult>()
		{
			@Override
			public void handleShareResult(TGWeiChatShareResult shareResult)
			{
				
			}
		});
	}
	
	private void shareToWeiChatTimeLine()
	{
		TGWeiChatMsgBuilder msgBuilder = new TGWeiChatMsgBuilder(0);
		msgBuilder.setTitle("Test");
		
		WeiChatTimeLineSharePlugin weiChatTimeLineSharePlugin = 
				(WeiChatTimeLineSharePlugin) TGSharePluginManager.getInstance().getPlugin(
						TGSharePluginManager.TAG_WEI_CHAT_TIME_LINE);
		
		weiChatTimeLineSharePlugin.share(this, msgBuilder, new IShareResultHandler<TGWeiChatShareResult>()
		{
			@Override
			public void handleShareResult(TGWeiChatShareResult shareResult)
			{
				
			}
		});
	}
	
	private void shareToQQ()
	{
		TGQQShareMsgBuilder msgBuilder = new TGQQShareMsgBuilder(0);
		msgBuilder.setQQTitle("");
		
		QQSharePlugin qqSharePlugin = 
				(QQSharePlugin)TGSharePluginManager.getInstance().getPlugin(
						TGSharePluginManager.TAG_QQ);
		
		qqSharePlugin.share(this, msgBuilder, new IShareResultHandler<TGQQShareResult>()
		{
			@Override
			public void handleShareResult(TGQQShareResult shareResult)
			{
				
			}
		});
	}
	
	private void shareToQQZone()
	{
		TGQQZoneShareMsgBuilder msgBuilder = new TGQQZoneShareMsgBuilder(0);
		msgBuilder.setQQTitle("");
		
		QQZoneSharePlugin qqZoneSharePlugin = 
				(QQZoneSharePlugin)TGSharePluginManager.getInstance().getPlugin(
						TGSharePluginManager.TAG_QQ_ZONE);
		
		qqZoneSharePlugin.share(this, msgBuilder, new IShareResultHandler<TGQQShareResult>()
		{
			@Override
			public void handleShareResult(TGQQShareResult shareResult)
			{
				
			}
		});
	}
	
	private void shareToWeiBo()
	{
		TGWeiBoMsgBuilder msgBuilder = new TGWeiBoMsgBuilder(0);
		msgBuilder.setTitle("");
		
		WeiBoSharePlugin weiBoSharePlugin = 
				(WeiBoSharePlugin)TGSharePluginManager.getInstance().getPlugin(
						TGSharePluginManager.TAG_WEI_BO);
		
		weiBoSharePlugin.share(this, msgBuilder, new IShareResultHandler<TGWeiboShareResult>()
		{
			@Override
			public void handleShareResult(TGWeiboShareResult shareResult)
			{

			}
		});
	}
	
}
