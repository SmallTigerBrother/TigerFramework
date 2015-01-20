package com.mn.tiger.demo.authorize;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.authorize.AbsAuthorizer;
import com.mn.tiger.authorize.IAuthorizeCallback;
import com.mn.tiger.authorize.TGAuthorizeResult;
import com.mn.tiger.authorize.TGWeiBoAuthorizer;
import com.mn.tiger.authorize.TGQQAuthorizer;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.ViewInjector;

public class AuthorizeDemoActivity extends TGActionBarActivity implements OnClickListener, IAuthorizeCallback
{
	@ViewById(id = R.id.qq_login_btn)
	private Button qqAuthButton;
	
	@ViewById(id = R.id.weibo_login_btn)
	private Button weiboAuthButton;
	
	private AbsAuthorizer authorizer = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_demo_activity);
		ViewInjector.initInjectedView(this, this);
		setupViews();
	}
	
	private void setupViews()
	{
		qqAuthButton.setOnClickListener(this);
		weiboAuthButton.setOnClickListener(this);
	}
	
	public void onClick(View v) 
	{
		switch (v.getId())
		{
			case R.id.qq_login_btn:
				authorizer = new TGQQAuthorizer(this, "");
				break;
			case R.id.weibo_login_btn:
				authorizer = new TGWeiBoAuthorizer(this, "");
				break;

			default:
				break;
		}
		
		authorizer.authorize(this);
	}

	@Override
	public void onSuccess(TGAuthorizeResult result)
	{
		//TODO 认证成功，登录自己的认证系统
	}

	@Override
	public void onError(int code, String message, String detail)
	{
		//TODO 认证失败，提示用户
	}

	@Override
	public void onCancel()
	{
		//TODO 认证取消，提示用户
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(null != authorizer)
		{
			authorizer.onActivityResult(requestCode, resultCode, data);
		}
	}
}
