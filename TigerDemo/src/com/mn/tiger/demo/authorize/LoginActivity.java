package com.mn.tiger.demo.authorize;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.authorize.IAuthorizeCallback;
import com.mn.tiger.authorize.TGAuthorizeResult;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.ToastUtils;
import com.mn.tiger.utility.ViewInjector;

/**
 * 登录界面
 */
public class LoginActivity extends TGActionBarActivity implements OnClickListener, IAuthorizeCallback
{
	/**
	 * 返回值——登录成功
	 */
	public static final int RESULT_CODE_SUCCESS = 2;
	
	/**
	 * 返回值——登录失败
	 */
	public static final int RESULT_CODE_FAILED = -2;
	
	@ViewById(id = R.id.user_account_edit)
	private EditText userAcountEdit;
	
	@ViewById(id = R.id.user_password_edit)
	private EditText passwordEdit;
	
	@ViewById(id = R.id.forget_password)
	private TextView forgetPassword;
	
	@ViewById(id = R.id.quick_register)
	private TextView quickRegister;

	@ViewById(id = R.id.login_btn)
	private Button loginButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		ViewInjector.initInjectedView(this, this);
		
		setBarTitleText(getString(R.string.login));
		showLeftBarButton(true);
		
		setupViews();
	}
	
	private void setupViews()
	{
		forgetPassword.setOnClickListener(this);
		quickRegister.setOnClickListener(this);
		loginButton.setOnClickListener(this);
		
		userAcountEdit.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
				
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				
			}
		});
		
		passwordEdit.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				
			}
		});
	}

	@Override
	public void onClick(View v)
	{
		Intent intent = null;
		switch (v.getId())
		{
			case R.id.login_btn:
				//执行登录
				login(userAcountEdit.getText().toString(), passwordEdit.getText().toString());
				break;
			case R.id.forget_password:
				
				break;
				
			case R.id.quick_register:
				//启动登录界面
				intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
				finish();
				
				break;

			default:
				break;
		}
	}

	/**
	 * 登录
	 * @param account
	 * @param password
	 */
	private void login(String account, String password)
	{
		MediaAuthorizer authorizer = new MediaAuthorizer(this, account, password);
		authorizer.authorize(this);
	}

	@Override
	public void onCancel()
	{
		
	}

	@Override
	public void onError(int code, String message, String detail)
	{
		//处理登录异常
		switch (code)
		{
			case MediaAuthorizer.AUTHORIZE_ERROR_ACCOUNT_PASSWORD_NULL:
				ToastUtils.showToast(this, message);
				break;
				
			case MediaAuthorizer.AUTHORIZE_ERROR_PASSWORD_ENCRYPT:
				ToastUtils.showToast(this, message);
				break;

			default:
				ToastUtils.showToast(this, message);
				break;
		}
	}

	@Override
	public void onSuccess(TGAuthorizeResult result)
	{
		this.setResult(RESULT_CODE_SUCCESS);
		this.finish();
	}
}
