package com.mn.tiger.demo.share;

import java.util.ArrayList;

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
import com.mn.tiger.share.TGWeChatSharePlugin.TGWeChatTextMsgBuilder;
import com.mn.tiger.share.TGWeiBoSharePlugin.TGWeiBoMsgBuilder;
import com.mn.tiger.share.result.TGQQShareResult;
import com.mn.tiger.share.result.TGWeChatShareResult;
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
		setContentView(R.layout.share_demo_activity);
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
		TGWeChatTextMsgBuilder msgBuilder = new TGWeChatTextMsgBuilder(0);
		
		WeiChatSharePlugin weiChatSharePlugin = 
				(WeiChatSharePlugin) TGSharePluginManager.getInstance().getPlugin(
						TGSharePluginManager.TAG_WEI_CHAT);
		
		weiChatSharePlugin.share(this, msgBuilder, new IShareResultHandler<TGWeChatShareResult>()
		{
			@Override
			public void handleShareResult(TGWeChatShareResult shareResult)
			{
				
			}
		});
	}
	
	private void shareToWeiChatTimeLine()
	{
		TGWeChatTextMsgBuilder msgBuilder = new TGWeChatTextMsgBuilder(0);
		msgBuilder.setText("Test");
		
		WeiChatTimeLineSharePlugin weiChatTimeLineSharePlugin = 
				(WeiChatTimeLineSharePlugin) TGSharePluginManager.getInstance().getPlugin(
						TGSharePluginManager.TAG_WEI_CHAT_TIME_LINE);
		
		weiChatTimeLineSharePlugin.share(this, msgBuilder, new IShareResultHandler<TGWeChatShareResult>()
		{
			@Override
			public void handleShareResult(TGWeChatShareResult shareResult)
			{
				
			}
		});
	}
	
	private void shareToQQ()
	{
		TGQQShareMsgBuilder msgBuilder = new TGQQShareMsgBuilder(0);
		msgBuilder.setQQTitle("Test");
		msgBuilder.setQQSummary("自己开发的ShareSDK");
//		msgBuilder.setQQKeyType(QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		msgBuilder.setQQAppName("Test");
		msgBuilder.setQQTargetUrl("http://www.baidu.com");
		
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
		msgBuilder.setQQTitle("Test");
		msgBuilder.setQQSummary("自己开发的ShareSDK");
		msgBuilder.setQQTargetUrl("http://www.baidu.com");
		
		ArrayList<String> imageUrls = new ArrayList<String>();
		imageUrls.add("http://img3.douban.com/lpic/s3635685.jpg");
		
		msgBuilder.setQQImageUrl(imageUrls);
		
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
