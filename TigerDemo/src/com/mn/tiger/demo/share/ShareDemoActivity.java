package com.mn.tiger.demo.share;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.demo.share.plugin.WeiChatSharePlugin;
import com.mn.tiger.share.IShareResultHandler;
import com.mn.tiger.share.TGSharePlugin;
import com.mn.tiger.share.TGSharePluginManager;
import com.mn.tiger.share.TGWeiChatSharePlugin.TGWeiChatMsgBuilder;
import com.mn.tiger.share.result.TGWeiChatShareResult;
import com.mn.tiger.utility.ViewInjector;

public class ShareDemoActivity extends TGActionBarActivity implements OnClickListener
{
	@ViewById(id = R.id.share_to_weichat_btn)
	private Button shareToWeixinBtn;
	
	@ViewById(id = R.id.share_to_weichat_timeline_btn)
	private Button shareToWeixinTimeLineBtn;
	
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
		shareToWeixinBtn.setOnClickListener(this);
		shareToWeixinTimeLineBtn.setOnClickListener(this);
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
				
				break;
			case R.id.share_to_weichat_timeline_btn:
				break;
				
			case R.id.share_to_qq_btn:
				break;
				
			case R.id.share_to_qqzone_btn:
				break;
				
			case R.id.share_to_weibo_btn:
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
}
