package com.example.wp.resource.widget.banner;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.wp.resource.widget.banner.callback.CreateViewCallBack;
import com.example.wp.resource.widget.banner.core.BaseBanner;
import com.example.wp.resource.widget.banner.pager.BannerPager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * viewpage  banner
 */
public class Banner extends BaseBanner<Banner> implements ViewPager.OnPageChangeListener {
	
	private BannerPager mBannerPage;
	private List<View> mItemViews;
	private HandlerTask mHandlerTask;
	
	private ViewPager.OnPageChangeListener onPageChangeListener;
	
	
	public Banner(@NonNull Context context) {
		this(context, null);
	}
	
	public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public Banner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr);
	}
	
	protected void init(Context context, AttributeSet attrs, int defStyleAttr) {
		super.init(context, attrs, defStyleAttr);
		mHandler = new Handler();
		mHandlerTask = new HandlerTask(this);
		mBannerPage = new BannerPager(getContext());
		mBannerPage.setVertical(isVertical);
		mBannerPage.setFocusable(true);
		mBannerPage.addOnPageChangeListener(this);
		addView(mBannerPage, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
		
	}
	
	/**
	 * 设置转换动画
	 *
	 * @param reverseDrawingOrder reverseDrawingOrder
	 * @param transformer         transformer
	 * @return Banner
	 */
	public Banner setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
		if (mBannerPage != null) {
			mBannerPage.setPageTransformer(reverseDrawingOrder, transformer);
		}
		return this;
	}
	
	public Banner setPageTransformer(ViewPager.PageTransformer transformer) {
		
		return setPageTransformer(true, transformer);
	}
	
	/**
	 * 设置垂直滚动 ，此时PageTransformer会被重置
	 * （原因，水平的PageTransformer可能不是你需要的,所以会重置成banner提供的垂直的PageTransformer。）
	 *
	 * @param orientation 滚动方向 HORIZONTAL VERTICAL
	 * @return Banner
	 */
	@Override
	public Banner setOrientation(int orientation) {
		this.isVertical = orientation == VERTICAL;
		if (mBannerPage != null) {
			mBannerPage.setVertical(this.isVertical);
		}
		return this;
	}
	
	/**
	 * 循环滚动
	 *
	 * @param loop true
	 * @return
	 */
	public Banner setLoop(boolean loop) {
		isLoop = loop;
		int dataSize = getBannerData().size();
		int viewSize = getItemViews().size();
		if (dataSize != viewSize) {
			if (viewSize < 2) {
				return this;
			}
			getItemViews().remove(viewSize - 1);
			getItemViews().remove(0);
			mBannerPage.getAdapter().notifyDataSetChanged();
		}
		return this;
	}
	
	/**
	 * BannerPager  onPageChangeListener
	 *
	 * @param onPageChangeListener onPageChangeListener
	 * @return Banner
	 */
	public Banner setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
		this.onPageChangeListener = onPageChangeListener;
		return this;
	}
	
	/**
	 * BannerPager extends viewpager
	 *
	 * @return BannerPager
	 */
	public BannerPager getPageView() {
		return mBannerPage;
	}
	
	/**
	 * see getPageView
	 *
	 * @return
	 * @deprecated
	 */
	public BannerPager getBannerPage() {
		return mBannerPage;
	}
	
	@Override
	public <T extends Object> void execute(List<T> imagesData) {
		stopAutoPlay();
		getItemViews().clear();
		getBannerData().clear();
		if (imagesData != null && imagesData.size() > 0) {
			getBannerData().addAll(imagesData);
		}
		generateItemViews();
		
		if (mIndicatorAble != null) {
			mIndicatorAble.initIndicator(getBannerData().size());
		}
		mBannerPage.setAdapter(new BannerPagerAdapter());
		if (getBannerData().size() > 0) {
			mCurrentItem = isLoop ? 1 : 0;
			mBannerPage.setCurrentItem(mCurrentItem);
		}
		
		if (getBannerData().size() <= 1) {
			mBannerPage.setCanScroll(false);
		} else {
			mBannerPage.setCanScroll(true);
		}
		startAutoPlay();
	}
	
	/**
	 * 返回当前page 索引
	 *
	 * @return
	 */
	@Override
	public int getCurrentItem() {
		return positionIndex(mCurrentItem);
	}
	
	@Override
	public void setCurrentItem(int index) {
		mBannerPage.setCurrentItem(index);
	}
	
	@Override
	public void startAutoPlay() {
		if (isAutoPlay && isLoop && getBannerData().size() > 1) {
			mHandler.removeCallbacks(mHandlerTask);
			turning = true;
			mHandler.postDelayed(mHandlerTask, interval);
		}
	}
	
	@Override
	public void stopAutoPlay() {
		turning = false;
		mHandler.removeCallbacks(mHandlerTask);
	}
	
	private void generateItemViews() {
		boolean def = true;
		if (this.createViewCallBack != null && createViewCallBack.createView(getContext(), null, 0, viewIndex) != null) {
			def = false;
		}
		
		int size = getBannerData().size();
		if (size <= 0) {
			return;
		}
		
		for (int i = 0; i <= (isLoop ? size + 1 : size - 1); i++) {
			if (def) {
				this.createViewCallBack = new CreateViewCallBack<View>() {
					@Override
					public View createView(Context context, ViewGroup parent, int viewType, int viewIndex) {
						if (viewIndex == 0)
							return createImageView(context);
						else
							return createLayout(context);
					}
				};
			}
			View view = createViewCallBack.createView(getContext(), null, 0, viewIndex);
			getItemViews().add(view);
			int index = positionIndex(i);
			
			if (bindViewCallBack != null && getBannerData().size() > 0) {
				bindViewCallBack.bindView(view, getBannerData().get(index), index);
			}
			if (onClickBannerListener != null && getBannerData().size() > 0) {
				final int finalIndex = index;
				view.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						onClickBannerListener.onClickBanner(view, getBannerData().get(finalIndex), finalIndex);
					}
				});
			}
		}
	}
	
	@Override
	protected int positionIndex(int postion) {
		if (!isLoop) {
			return postion;
		}
		int count = getBannerData().size();
		int index = postion - 1;
		if (postion == 0) {
			index = count - 1;
		} else if (postion == count + 1) {
			index = 0;
		}
		return index;
	}
	
	@Override
	public void onPageScrollStateChanged(int state) {
		if (onPageChangeListener != null) {
			onPageChangeListener.onPageScrollStateChanged(state);
		}
		if (mIndicatorAble != null) {
			mIndicatorAble.onBannerScrollStateChanged(state);
		}
		switch (state) {
			case ViewPager.SCROLL_STATE_IDLE:
				computeCurrentItem();
				break;
			case ViewPager.SCROLL_STATE_DRAGGING:
				computeCurrentItem();
				break;
			case ViewPager.SCROLL_STATE_SETTLING:
				break;
		}
	}
	
	private void computeCurrentItem() {
		if (!isLoop) {
			return;
		}
		int size = getBannerData().size();
		if (mCurrentItem == size + 1) {
			mBannerPage.setCurrentItem(mCurrentItem = 1, false);
		} else if (mCurrentItem == 0) {
			mBannerPage.setCurrentItem(mCurrentItem = size, false);
		}
	}
	
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		position = positionIndex(position);
		if (onPageChangeListener != null) {
			onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
		}
		if (mIndicatorAble != null) {
			mIndicatorAble.onBannerScrolled(position, positionOffset, positionOffsetPixels);
		}
	}
	
	@Override
	public void onPageSelected(int position) {
		mCurrentItem = position;
		position = positionIndex(position);
		if (onPageChangeListener != null) {
			onPageChangeListener.onPageSelected(position);
		}
		if (mIndicatorAble != null && getBannerData().size() > 0) {
			mIndicatorAble.onBannerSelected(position, getBannerData().size(), getBannerData().get(position));
		}
	}
	
	private static class HandlerTask implements Runnable {
		WeakReference<Banner> bannerRef;
		
		HandlerTask(Banner banner) {
			bannerRef = new WeakReference<Banner>(banner);
		}
		
		@Override
		public void run() {
			Banner banner = bannerRef.get();
			if (banner == null) {
				return;
			}
			int size = banner.getBannerData().size();
			if (size > 1 && banner.isAutoPlay && banner.isLoop) {
				banner.mCurrentItem = banner.mCurrentItem % (size + 1) + 1;
				if (banner.mCurrentItem == 1) {
					banner.mBannerPage.setCurrentItem(banner.mCurrentItem, false);
					banner.mHandler.post(this);
				} else {
					banner.mBannerPage.setCurrentItem(banner.mCurrentItem);
					banner.mHandler.postDelayed(this, banner.interval);
				}
			}
		}
	}
	
	/**
	 * RecyclerAdapter
	 */
	
	private List<View> getItemViews() {
		if (mItemViews == null) {
			mItemViews = new ArrayList<>();
		}
		return mItemViews;
	}
	
	private class BannerPagerAdapter extends PagerAdapter {
		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return getItemViews().size();
		}
		
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			View view = container.findViewWithTag(position);
			if (view == null) {
				view = getItemViews().get(position);
				view.setTag(position);
				container.addView(getItemViews().get(position));
			}
			
			view.setVisibility(VISIBLE);
			return view;
		}
		
		@Override
		public void finishUpdate(ViewGroup container) {
			super.finishUpdate(container);
			View view = container.findViewWithTag(mBannerPage.getCurrentItem());
			if (view != null) {
				view.setEnabled(true);
			}
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			view.setVisibility(INVISIBLE);
		}
	}
}
