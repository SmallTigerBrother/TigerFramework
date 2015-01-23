package com.mn.tiger.demo.authorize;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.app.TGFragment;
import com.mn.tiger.authorize.IRegisterCallback;
import com.mn.tiger.authorize.TGAuthorizeResult;
import com.mn.tiger.demo.R;
import com.mn.tiger.demo.authorize.MediaAuthorizer.IVertificationCodeCallback;
import com.mn.tiger.utility.StringUtils;
import com.mn.tiger.utility.ToastUtils;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.CountDownTimeView;
import com.mn.tiger.widget.CountDownTimeView.OnTimeChangedListener;
import com.mn.tiger.widget.dialog.TGDialog;

/**
 * 注册界面
 */
public class RegisterActivity extends TGActionBarActivity implements DialogInterface.OnClickListener
{
	/**
	 * 返回值——注册成功
	 */
	public static final int RESULT_CODE_SUCCESS = 1;
	
	/**
	 * 返回值——注册失败
	 */
	public static final int RESULT_CODE_FAILED = -1;
	
	/**
	 * 输入手机号界面
	 */
	private InputMobileFragment inputMobileFragment;
	
	/**
	 * 输入验证码界面
	 */
	private InputVertificationCodeFragment inputCodeFragment;
	
	/**
	 * 设置密码界面
	 */
	private InputPasswordFragment inputPasswordFragment;
	
	/**
	 * 认证类
	 */
	private MediaAuthorizer authorizer;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 验证码
	 */
	private String code;
	
	/**
	 * 发送验证码提示对话框
	 */
	private TGDialog sendCodeDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setNavigationBarVisible(false);
		
		setContentView(R.layout.register_activity);
		ViewInjector.initInjectedView(this, this);
		
		setBarTitleText(getString(R.string.register));
		
		authorizer = new MediaAuthorizer(this, null, null);
		
		setupViews();
	}
	
	private void setupViews()
	{
		inputMobileFragment = new InputMobileFragment();
		inputMobileFragment.setActivity(this);
		
		inputCodeFragment = new InputVertificationCodeFragment();
		inputCodeFragment.setActivity(this);
		
		inputPasswordFragment = new InputPasswordFragment();
		inputPasswordFragment.setActivity(this);
		
		//显示输入手机号界面
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().add(R.id.register_main, inputMobileFragment).commit();
	}
	
	/**
	 * 显示输入验证码界面
	 */
	private void showInputCodeFragment()
	{
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.register_main, inputCodeFragment).commit();
		inputCodeFragment.startCountDown();
	}
	
	/**
	 * 显示设置密码界面
	 */
	private void showInputPasswordFragment()
	{
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.register_main, inputPasswordFragment).commit();
	}
	
	/**
	 * 显示发送验证码提示对话框
	 * @param mobile
	 */
	private void showSendCodeDialog(String mobile)
	{
		if(!TextUtils.isEmpty(mobile) && StringUtils.isPhoneNumber(mobile))
		{
			this.mobile = mobile;
			//显示发送验证码的对话框
			if(null == sendCodeDialog)
			{
				sendCodeDialog = new TGDialog(this);
				sendCodeDialog.setTitleText(getString(R.string.confirm_mobile));
				
				
				sendCodeDialog.setLeftButton(getString(R.string.cancel), this);
				sendCodeDialog.setRightButton(getString(R.string.ok), this);
			}
			
			String tips = getString(R.string.we_will_send_code_to_mobile);
			sendCodeDialog.setBodyText(String.format(tips, mobile));
			sendCodeDialog.show();
		}
		else
		{
			ToastUtils.showToast(RegisterActivity.this, 
					R.string.please_input_valid_mobile);
		}
	}
	
	/**
	 * 请求验证码
	 */
	private void requestVerificationCode()
	{
		authorizer.requestVerificationCode(mobile, new IVertificationCodeCallback()
		{
			@Override
			public void onSuccess()
			{
				ToastUtils.showToast(RegisterActivity.this, 
						R.string.vertification_code_send_success);
				showInputCodeFragment();
				sendCodeDialog.dismiss();
			}
			
			@Override
			public void onError(int code, String message)
			{
				ToastUtils.showToast(RegisterActivity.this, message);
			}
		});
	}
	
	/**
	 * 校验验证码
	 * @param code
	 */
	private void checkVertificationCode(String code)
	{
		this.code = code;
		authorizer.checkVertificationCode(mobile, code, new IVertificationCodeCallback()
		{
			@Override
			public void onSuccess()
			{
				ToastUtils.showToast(RegisterActivity.this, R.string.code_check_ok);
				showInputPasswordFragment();
			}
			
			@Override
			public void onError(int code, String message)
			{
				ToastUtils.showToast(RegisterActivity.this, message);
			}
		});
	}
	
	/**
	 * 注册账号
	 * @param password 密码
	 */
	private void register(String password)
	{
		authorizer.register(mobile, password, new IRegisterCallback()
		{
			@Override
			public void onSuccess(TGAuthorizeResult authorizeResult)
			{
				RegisterActivity.this.setResult(RESULT_CODE_SUCCESS);
				RegisterActivity.this.finish();;
			}

			@Override
			public void onError(int code, String message)
			{
				ToastUtils.showToast(RegisterActivity.this, message);
			}
		}, code, mobile, 0);
	}
	
	/**
	 * 确认手机号对话框按钮点击方法
	 */
	@Override
	public void onClick(final DialogInterface dialog, int which)
	{
		switch (which)
		{
			case TGDialog.BUTTON_LEFT:
				dialog.dismiss();
				break;
				
			case TGDialog.BUTTON_RIGHT:
				//请求验证码
				requestVerificationCode();
				break;
			default:
				break;
		}
	}
	
	/**
	 * 添加手机号的Fragment
	 */
	private class InputMobileFragment extends TGFragment
	{
		@ViewById(id = R.id.input_mobile_edit)
		private EditText mobileInput;
		
		@ViewById(id = R.id.input_mobile_next_btn)
		private Button nextButton;
		
		@ViewById(id = R.id.input_mobile_protocol)
		private CheckBox protocalCheckBox;
		
		public InputMobileFragment()
		{
			super();
			this.setNavigationBarVisible(true);
		}

		@Override
		protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState)
		{
			View view = inflater.inflate(R.layout.input_moblie_fragment, null);
			ViewInjector.initInjectedView(this, view);
			this.setBarTitleText(RegisterActivity.this.getString(R.string.register));
			showLeftBarButton(true);
			
			nextButton.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					//若未勾选“已阅读用户协议”，提示用户勾选
					if(protocalCheckBox.isChecked())
					{
						showSendCodeDialog(mobileInput.getText().toString());
					}
					else
					{
						ToastUtils.showToast(RegisterActivity.this, 
								R.string.please_agree_user_protocol);
					}
				}
			});
			return view;
		}
	}
	
	/**
	 * 输入验证码Fragment
	 */
	private class InputVertificationCodeFragment extends TGFragment implements 
	    OnClickListener,OnTimeChangedListener
	{
		@ViewById(id = R.id.input_code_edit)
		private EditText codeInput;
		
		@ViewById(id = R.id.resend_code_button)
		private CountDownTimeView resendCodeBtn;
		
		@ViewById(id = R.id.input_code_next_btn)
		private Button nextButton;

		public InputVertificationCodeFragment()
		{
			super();
			this.setNavigationBarVisible(true);
		}

		@Override
		protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState)
		{
			View view = inflater.inflate(R.layout.input_vertification_code_fragment, null);
			ViewInjector.initInjectedView(this, view);
			this.setBarTitleText(RegisterActivity.this.getString(R.string.input_vertificationcode));
			showLeftBarButton(true);
			
			resendCodeBtn.setOnClickListener(this);
			nextButton.setOnClickListener(this);
			startCountDown();
			
			return view;
		}
		
		/**
		 * 启动倒计时
		 */
		public void startCountDown()
		{
			if(null != resendCodeBtn)
			{
				resendCodeBtn.setDeltaTime(60 * 1000, this);
				resendCodeBtn.start();
			}
		}
		
		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
				case R.id.resend_code_button:
					//重新发送验证码
					requestVerificationCode();
					break;
				case R.id.input_code_next_btn:
					//向服务器确认验证码
					checkVertificationCode(codeInput.getText().toString());
					break;

				default:
					break;
			}
		}
		
		@Override
		public void onTimeStart(View view, long lastTime)
		{
			resendCodeBtn.setText(String.format(getString(R.string.resend_code_later), 
					(int)(lastTime / 1000)));
			resendCodeBtn.setClickable(false);
			resendCodeBtn.setBackgroundResource(R.drawable.resend_code_bg_unable);
		}

		@Override
		public void onTimeChanged(View view, long lastTime)
		{
			resendCodeBtn.setText(String.format(getString(R.string.resend_code_later), 
					(int)(lastTime / 1000)));
			resendCodeBtn.setBackgroundResource(R.drawable.resend_code_bg_unable);
		}

		@Override
		public void onTimeEnd(View view)
		{
			resendCodeBtn.setText(R.string.resend_code);
			resendCodeBtn.setBackgroundResource(R.drawable.resend_code_able);
			resendCodeBtn.setClickable(true);
		}

		@Override
		public void onStop(View view, long lastTime)
		{
			resendCodeBtn.setClickable(true);
			resendCodeBtn.setBackgroundResource(R.drawable.resend_code_able);
		}
	}
	
	/**
	 * 设置密码Fragment
	 */
	private class InputPasswordFragment extends TGFragment
	{
		@ViewById(id = R.id.input_password_edit)
		private EditText inputPassword;
		
		@ViewById(id = R.id.register_over_btn)
		private Button registerOverBtn;
		
		public InputPasswordFragment()
		{
			super();
			this.setNavigationBarVisible(true);
		}

		@Override
		protected View onCreateView(LayoutInflater inflater, Bundle savedInstanceState)
		{
			View view = inflater.inflate(R.layout.input_password_fragment, null);
			ViewInjector.initInjectedView(this, view);
			this.setBarTitleText(RegisterActivity.this.getString(R.string.set_password));
			showLeftBarButton(true);
			
			registerOverBtn.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					//注册账号
					register(inputPassword.getText().toString());
				}
			});
			return view;
		}
	}
	
}
