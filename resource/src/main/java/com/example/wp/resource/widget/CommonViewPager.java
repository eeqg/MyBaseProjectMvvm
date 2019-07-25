package com.example.wp.resource.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by wp on 2019/5/9.
 */
public class CommonViewPager extends FrameLayout {
	private NoScrollViewPager viewPager;
	private List<View> viewList = new ArrayList<>();
	private List<Object> dataList = new ArrayList<>();
	
	private ViewCreator viewCreator;
	private OnItemClickListener itemClickListener;
	private OnItemSelectedListener itemSelectedListener;
	
	public CommonViewPager(@NonNull Context context) {
		this(context, null);
	}
	
	public CommonViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CommonViewPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	private void init() {
		viewPager = new NoScrollViewPager(getContext());
		viewPager.setOffscreenPageLimit(5);
		viewPager.setClipChildren(false);
		setClipChildren(false);
		addView(viewPager, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
	}
	
	public ViewPager getViewPager() {
		return this.viewPager;
	}
	
	public CommonViewPager setCanScroll(boolean canScroll) {
		viewPager.setCanScroll(canScroll);
		return this;
	}
	
	public void setCurrentItem(int position) {
		viewPager.setCurrentItem(position);
	}
	
	public void setCurrentItem(int position, boolean smoothScroll) {
		viewPager.setCurrentItem(position, smoothScroll);
	}
	
	public CommonViewPager addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
		this.viewPager.addOnPageChangeListener(listener);
		return this;
	}
	
	public CommonViewPager createItemView(ViewCreator creator) {
		this.viewCreator = creator;
		return this;
	}
	
	public CommonViewPager setOnItemClickListener(OnItemClickListener listener) {
		this.itemClickListener = listener;
		return this;
	}
	
	public CommonViewPager setOnItemSelectedListener(OnItemSelectedListener listener) {
		this.itemSelectedListener = listener;
		return this;
	}
	
	public <T> void execute(List<T> dataList) {
		if (dataList == null) {
			return;
		}
		this.dataList.clear();
		this.viewList.clear();
		this.dataList.addAll(dataList);
		generateItemViews();
		
		viewPager.setAdapter(new Adapter());
		
		if (itemSelectedListener != null) {
			this.viewPager.addOnPageChangeListener(pageChangeListener);
		}
	}
	
	private void generateItemViews() {
		if (this.viewCreator == null) {
			throw new RuntimeException("---请先创建itemView!!---");
		}
		for (int i = 0; i < dataList.size(); i++) {
			View view = viewCreator.onCreateView(i);
			this.viewList.add(view);
			final int position = i;
			this.viewCreator.onBindView(view, dataList.get(i), i);
			
			if (itemClickListener != null) {
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						itemClickListener.onItemClick(v, dataList.get(position), position);
					}
				});
			}
			if (itemSelectedListener != null && position == 0) {
				itemSelectedListener.onItemSelected(dataList.get(0), 0);
			}
		}
	}
	
	private class Adapter extends PagerAdapter {
		@Override
		public int getCount() {
			return viewList.size();
		}
		
		@Override
		public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
			return view == object;
		}
		
		@NonNull
		@Override
		public Object instantiateItem(@NonNull ViewGroup container, int position) {
			View view = container.findViewWithTag(position);
			if (view == null) {
				view = viewList.get(position);
				view.setTag(position);
				container.addView(view);
			}
			return view;
		}
		
		@Override
		public void finishUpdate(@NonNull ViewGroup container) {
			super.finishUpdate(container);
		}
		
		@Override
		public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
			container.removeView((View) object);
		}
	}
	
	private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}
		
		@Override
		public void onPageSelected(int position) {
			if (itemSelectedListener != null) {
				itemSelectedListener.onItemSelected(dataList.get(position), position);
			}
		}
		
		@Override
		public void onPageScrollStateChanged(int state) {
		}
	};
	
	public interface ViewCreator<V extends View, T> {
		View onCreateView(int position);
		
		void onBindView(V view, T data, int position);
	}
	
	public interface OnItemClickListener<V extends View, T extends Object> {
		void onItemClick(V view, T data, int position);
	}
	
	public interface OnItemSelectedListener<T extends Object> {
		void onItemSelected(T data, int position);
	}
}
