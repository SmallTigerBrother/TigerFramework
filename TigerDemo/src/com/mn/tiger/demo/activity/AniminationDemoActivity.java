package com.mn.tiger.demo.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.ViewInjector;

public class AniminationDemoActivity extends TGActionBarActivity implements OnClickListener
{
	@ViewById(id = R.id.test_btn_1)
	private Button testBtn_1;
	
	@ViewById(id = R.id.test_btn_2)
	private Button testBtn_2;
	
	@ViewById(id = R.id.test_btn_3)
	private Button testBtn_3;
	
	@ViewById(id = R.id.test_btn_4)
	private Button testBtn_4;
	
	@ViewById(id = R.id.test_image)
	private ImageView testImageView;
	
	@ViewById(id = R.id.test_text)
	private TextView testTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation_demo_layout);
		ViewInjector.initInjectedView(this, this);
		testBtn_1.setOnClickListener(this);
		testBtn_2.setOnClickListener(this);
		testBtn_3.setOnClickListener(this);
		testBtn_4.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.test_btn_1:
				ObjectAnimator animator_1 = ObjectAnimator.ofInt(
						testImageView, "backgroundColor", 0xff000000, 0xffffffff);
				animator_1.setDuration(10000);
				animator_1.addUpdateListener(new AnimatorUpdateListener()
				{
					@Override
					public void onAnimationUpdate(ValueAnimator animation)
					{
						testImageView.setBackgroundColor((Integer) animation.getAnimatedValue());
						testImageView.invalidate();
						testImageView.postInvalidate();
					}
				});
				animator_1.start();
				break;
				
			case R.id.test_btn_2:
				ObjectAnimator animator_2 = ObjectAnimator.ofFloat(
						testTextView, "translationX", 0, 360f);
				animator_2.setDuration(2000);
//				animator_2.start();
				
				ObjectAnimator animator_3 = ObjectAnimator.ofFloat(
						testTextView, "textSize", 14f, 32f);
				animator_3.setDuration(1000);
				
				ObjectAnimator animator_4 = ObjectAnimator.ofInt(
						testTextView, "textColor", 
						0xff000000, 0xffffffff);
				animator_4.setDuration(3000);
				
//				animator_3.start();
				AnimatorSet animatorSet = new AnimatorSet();
				animatorSet.playTogether(animator_2, animator_3);
//				animatorSet.play(animator_2);
//				animatorSet.play(animator_3);
				animatorSet.setInterpolator(new BounceInterpolator());
				animatorSet.play(animator_4).after(animator_3);
				animatorSet.start();
				
				break;

			case R.id.test_btn_3:
//				ObjectAnimator animator_3 = ObjectAnimator.ofFloat(
//						testTextView, "textSize", 14f, 32f);
//				animator_3.setDuration(1000);
//				animator_3.start();
				break;

			case R.id.test_btn_4:
//				ObjectAnimator animator_4 = ObjectAnimator.ofInt(
//						testTextView, "textColor", 
//						0xff000000, 0xffffffff);
//				animator_4.setDuration(1000);
//				animator_4.start();
				break;
			default:
				break;
		}
	}
}
