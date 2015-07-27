package com.coolweather.app.model;



import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coolweather.app.R;



public class Topbar extends RelativeLayout {

	private Button leftButton, rightButton;
	private TextView tvTitle;

	private int leftTextColor;
	private Drawable leftBackground;
	private String leftText;

	private int rightTextColor;
	private Drawable rightBackground;
	private String rightText;

	private float tilteTextSize;
	private int titleTextColor;
	private String title;

	private LayoutParams leftParams, rightParams, titleParams;

	public interface topbarClickListener {
		public void leftClick();

		public void rightClick();
	}

	private topbarClickListener listener;

	public void setOnTopbarClickListener(topbarClickListener listener) {
		this.listener = listener;

	}

	public Topbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.Topbar);

		leftTextColor = ta.getColor(R.styleable.Topbar_leftTextColor, 0);
		leftBackground = ta.getDrawable(R.styleable.Topbar_leftBackground);
		leftText = ta.getString(R.styleable.Topbar_leftText);

		rightTextColor = ta.getColor(R.styleable.Topbar_rightTextColor, 0);
		rightBackground = ta.getDrawable(R.styleable.Topbar_rightBackground);
		rightText = ta.getString(R.styleable.Topbar_rightText);

		tilteTextSize = ta.getDimension(R.styleable.Topbar_titleTextSize, 0);
		titleTextColor = ta.getColor(R.styleable.Topbar_titleTextColor, 0);
		title = ta.getString(R.styleable.Topbar_title);

		ta.recycle();

		leftButton = new Button(context);
		rightButton = new Button(context);
		tvTitle = new TextView(context);

		leftButton.setTextColor(leftTextColor);
		leftButton.setBackground(leftBackground);
		leftButton.setText(leftText);

		rightButton.setTextColor(rightTextColor);
		rightButton.setBackground(rightBackground);
		rightButton.setText(rightText);

		tvTitle.setTextSize(tilteTextSize);
		tvTitle.setTextColor(titleTextColor);
		tvTitle.setText(title);
		tvTitle.setGravity(Gravity.CENTER);

		setBackgroundColor(Color.parseColor("#6D6399"));

		leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
		addView(leftButton, leftParams);

		rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
		addView(rightButton, rightParams);

		titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
		addView(tvTitle, titleParams);

		leftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				listener.leftClick();
			}
		});

		rightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				listener.rightClick();
			}
		});
	}

	public void setLeftIsvisable(boolean flag) {
		if (flag) {
			leftButton.setVisibility(View.VISIBLE);
		} else {
			leftButton.setVisibility(View.GONE);
		}

	}

	public void setRightIsvisable(boolean flag) {
		if (flag) {
			rightButton.setVisibility(View.VISIBLE);
		} else {
			rightButton.setVisibility(View.GONE);
		}

	}

	public void setTitleIsvisable(boolean flag) {
		if (flag) {
			tvTitle.setVisibility(View.VISIBLE);
		} else {
			tvTitle.setVisibility(View.GONE);
		}

	}
	public void setTitleText(String text){
		tvTitle.setText(text);
	}

}
