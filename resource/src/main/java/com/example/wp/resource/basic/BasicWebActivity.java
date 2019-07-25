package com.example.wp.resource.basic;

import android.graphics.Bitmap;
import android.graphics.drawable.ClipDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.example.wp.resource.R;
import com.example.wp.resource.databinding.ActivityBasicWebBinding;
import com.example.wp.resource.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wp on 2019/4/27.
 */
public abstract class BasicWebActivity extends BasicActivity<ActivityBasicWebBinding> {
	protected String title;
	protected String url;
	private ClipDrawable progressDrawable;
	private List<String> urlList = new ArrayList<>();
	
	protected void setTitleBackgroundColor(int color) {
		dataBinding.toolbar.setBackgroundColor(color);
	}
	
	@Override
	public int getContentView() {
		return R.layout.activity_basic_web;
	}
	
	@Override
	public void init() {
	
	}
	
	@Override
	public void initView() {
		observeBack();
		observeFinish();
		observeRefresh();
		observeProgressView();
		observeContent();
		
		start();
		setTitle(title);
	}
	
	private void observeBack() {
		dataBinding.ivBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	private void observeFinish() {
		this.dataBinding.ivFinish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void observeRefresh() {
		this.dataBinding.ivRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				WebView webView;
				int childCount = dataBinding.flContent.getChildCount();
				if (childCount == 1) {
					webView = getWebView();
				} else {
					webView = (WebView) dataBinding.flContent.getChildAt(childCount - 1);
				}
				webView.reload();
			}
		});
	}
	
	private void observeProgressView() {
		progressDrawable = (ClipDrawable) dataBinding.progressView.getBackground();
	}
	
	private void observeContent() {
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		this.dataBinding.flContent.addView(getContentLayout(), layoutParams);
		setupWebView(getWebView());
	}
	
	private void setupWebView(WebView webView) {
		//声明WebSettings子类
		WebSettings webSettings = webView.getSettings();
		
		//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
		webSettings.setJavaScriptEnabled(true);
		// 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
		// 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
		
		//支持插件
		// webSettings.setPluginsEnabled(true);
		
		//设置自适应屏幕，两者合用
		webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
		webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
		
		//缩放操作
		webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
		webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
		webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
		webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 设置网页内容自适应屏幕大小
		
		//其他细节操作
		// webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); //关闭webview中缓存
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAllowFileAccess(true); //设置可以访问文件
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
		webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
		webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
		
		webSettings.setDomStorageEnabled(true);
		//设置可以访问文件
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			webSettings.setAllowFileAccessFromFileURLs(true);
			webSettings.setAllowUniversalAccessFromFileURLs(true);
		}
		// 5.0以上允许加载http和https混合的页面(5.0以下默认允许，5.0+默认禁止)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		
		webView.setWebViewClient(new MyWebViewClient());
		webView.setWebChromeClient(new MyWebChromeClient());
	}
	
	private void addWeb(String url) {
		WebView webView = new WebView(this);
		setupWebView(webView);
		webView.loadUrl(url);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT
		);
		webView.setLayoutParams(params);
		dataBinding.flContent.addView(webView);
	}
	
	protected abstract WebView getWebView();
	
	protected abstract View getContentLayout();
	
	protected void start() {
		getWebView().loadUrl(url);
	}
	
	protected void setTitle(String title) {
		dataBinding.tvTitle.setText(title);
	}
	
	protected class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			LogUtils.d("-----url = " + url);
			onLoadUrl(url);
			if (!urlList.contains(url)) {
				if (urlList.size() > 0) {
					addWeb(url);
				} else {
					view.loadUrl(url);
				}
				urlList.add(url);
				return true;
			} else {
				// return super.shouldOverrideUrlLoading(view, url);
				view.loadUrl(url);
				return true;
			}
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			LogUtils.d("onPageStarted-----url = " + url);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			if (TextUtils.isEmpty(title)) {
				setTitle(view.getTitle());
			}
		}
		
		@Override
		public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
			super.onReceivedError(view, request, error);
		}
	}
	
	public class MyWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// LogUtils.d("-----" + newProgress);
			if (newProgress == 100) {
				dataBinding.progressView.setVisibility(View.GONE);
				progressDrawable.setLevel(0);
			} else {
				if (dataBinding.progressView.getVisibility() == View.GONE) {
					dataBinding.progressView.setVisibility(View.VISIBLE);
				}
				progressDrawable.setLevel(newProgress * 100);
			}
			super.onProgressChanged(view, newProgress);
		}
	}
	
	protected void onLoadUrl(String url) {
	
	}
	
	@Override
	public void onBackPressed() {
		// if (webView.canGoBack()) {
		// 	webView.goBack();
		if (urlList.size() > 1) {
			urlList.remove(urlList.size() - 1);
			dataBinding.flContent.removeViewAt(dataBinding.flContent.getChildCount() - 1);
		} else {
			super.onBackPressed();
		}
	}
	
	@Override
	protected void onDestroy() {
		for (int index = 0; index < dataBinding.flContent.getChildCount(); index++) {
			WebView webView;
			if (index == 0) {
				webView = getWebView();
			} else {
				webView = (WebView) dataBinding.flContent.getChildAt(index);
			}
			if (webView != null) {
				//先让 WebView 加载null内容，然后移除 WebView，再销毁 WebView，最后置空
				webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
				webView.clearHistory();
				
				((ViewGroup) webView.getParent()).removeView(webView);
				webView.destroy();
				webView = null;
			}
		}
		super.onDestroy();
	}
}
