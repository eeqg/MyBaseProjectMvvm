package com.example.wp.resource.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wp.resource.R;
import com.example.wp.resource.basic.model.StatusInfo;

import cn.shyman.library.refresh.RefreshLayout;
import cn.shyman.library.refresh.RefreshStatus;

public class BasicRefreshStatus extends LinearLayout implements RefreshStatus<StatusInfo> {
	private ImageView ivStatus;
	private TextView tvStatus;
	private TextView tvCheck;
	private TextView tvReload;
	
	private RotateAnimation animation;
	
	private RefreshLayout.OnRefreshListener oldOnRefreshListener;
	
	public BasicRefreshStatus(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void initOnRefreshListener(RefreshLayout.OnRefreshListener onTryRefreshListener) {
		this.oldOnRefreshListener = onTryRefreshListener;
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		this.ivStatus = (ImageView) findViewById(R.id.ivStatus);
		this.ivStatus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (oldOnRefreshListener != null) {
					oldOnRefreshListener.notifyRefresh();
				}
			}
		});
		this.tvStatus = (TextView) findViewById(R.id.tvStatus);
		this.tvCheck = (TextView) findViewById(R.id.tvCheck);
		this.tvReload = (TextView) findViewById(R.id.tvReload);
		this.tvReload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (oldOnRefreshListener != null) {
					oldOnRefreshListener.notifyRefresh();
				}
			}
		});
		
		// Animation scaleAnimation = new ScaleAnimation(
		// 		1.0F, 0.55F, 1.0F, 0.55F,
		// 		Animation.RELATIVE_TO_SELF, 0.5F,
		// 		Animation.RELATIVE_TO_SELF, 0.5F);
		// scaleAnimation.setDuration(800);
		// scaleAnimation.setRepeatMode(Animation.REVERSE);
		// scaleAnimation.setRepeatCount(Animation.INFINITE);
		//
		// Animation rotateAnimation = new RotateAnimation(
		// 		0, 360,
		// 		Animation.RELATIVE_TO_SELF, 0.5F,
		// 		Animation.RELATIVE_TO_SELF, 0.5F);
		// rotateAnimation.setDuration(600);
		// scaleAnimation.setRepeatMode(Animation.REVERSE);
		// rotateAnimation.setRepeatCount(Animation.INFINITE);
		//
		// this.animation = new AnimationSet(true);
		// this.animation.setInterpolator(new LinearInterpolator());
		// this.animation.addAnimation(scaleAnimation);
		// this.animation.addAnimation(rotateAnimation);
		
		this.animation = new RotateAnimation(
				0, 3600,
				Animation.RELATIVE_TO_SELF, 0.5F,
				Animation.RELATIVE_TO_SELF, 0.5F);
		this.animation.setDuration(10000);
		this.animation.setInterpolator(new LinearInterpolator());
		this.animation.setRepeatCount(Animation.INFINITE);
	}
	
	@Override
	public void onRefreshScale(float scale) {
	}
	
	@Override
	public void onRefreshReady() {
		this.ivStatus.clearAnimation();
		this.ivStatus.setImageDrawable(null);
		this.ivStatus.startAnimation(this.animation);
		this.tvStatus.setText(R.string.loading_dot);
		this.tvCheck.setVisibility(GONE);
		this.tvReload.setVisibility(GONE);
	}
	
	@Override
	public void onRefresh() {
		this.ivStatus.setImageResource(R.mipmap.ic_loading);
		this.ivStatus.startAnimation(this.animation);
		this.tvStatus.setText(R.string.loading_dot);
		this.tvCheck.setVisibility(GONE);
		this.tvReload.setVisibility(GONE);
	}
	
	@Override
	public boolean onRefreshComplete(StatusInfo statusInfo) {
		this.ivStatus.clearAnimation();
		if (statusInfo == null) {
			this.ivStatus.setImageResource(R.mipmap.img_network_error);
			this.ivStatus.setEnabled(false);
			this.tvStatus.setText(R.string.network_request_error);
			this.tvCheck.setVisibility(VISIBLE);
			this.tvReload.setVisibility(VISIBLE);
			return true;
		} else if (statusInfo.isSuccessful()) {
			this.ivStatus.setImageDrawable(null);
			this.ivStatus.setEnabled(true);
			this.tvStatus.setText(statusInfo.statusMessage);
			this.tvCheck.setVisibility(GONE);
			this.tvReload.setVisibility(GONE);
		} else {
			this.ivStatus.setImageResource(R.mipmap.img_data_error);
			this.ivStatus.setEnabled(true);
			this.tvStatus.setText(statusInfo.statusMessage);
			this.tvCheck.setVisibility(GONE);
			this.tvReload.setVisibility(GONE);
		}
		return !statusInfo.isSuccessful();
	}
}
