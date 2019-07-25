package com.example.wp.mybaseprojectmvvm.find;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wp.mybaseprojectmvvm.R;
import com.example.wp.mybaseprojectmvvm.databinding.ItemMovieListBinding;
import com.example.wp.mybaseprojectmvvm.find.repository.bean.MovieItemBean;
import com.example.wp.mybaseprojectmvvm.find.repository.bean.MovieListBean;
import com.example.wp.resource.basic.BasicRecyclerAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import cn.shyman.library.refresh.RecyclerAdapter;

/**
 * Created by wp on 2018/4/10.
 */

public class FindMovieListAdapter extends BasicRecyclerAdapter<MovieListBean> {
	
	public FindMovieListAdapter(Context context) {
		super(context);
	}
	
	@Override
	public void updateAdapterInfo(@NonNull MovieListBean movieInfoBean) {
		this.adapterInfo.movieList.addAll(movieInfoBean.movieList);
	}
	
	@Override
	public boolean hasMore() {
		if (this.adapterInfo == null) {
			return false;
		}
		return this.adapterInfo.total > this.getItemCount();
	}
	
	@Override
	public int getItemCount() {
		int count = 0;
		if (this.adapterInfo != null && this.adapterInfo.movieList != null) {
			count = this.adapterInfo.movieList.size();
		}
		return count;
	}
	
	@Override
	public MovieItemBean getItem(int position) {
		return this.adapterInfo.movieList.get(position);
	}
	
	@Override
	public RecyclerAdapter.ItemHolder onCreateItemHolder(int viewType) {
		return new RecyclerAdapter.ItemHolder() {
			ItemMovieListBinding dataBinding;
			
			@Override
			protected View onCreateView(ViewGroup parent) {
				this.dataBinding = DataBindingUtil.inflate(inflater, R.layout.item_movie_list, parent, false);
				return this.dataBinding.getRoot();
			}
			
			@Override
			protected void onBindView(int position) {
				this.dataBinding.setIndex(String.valueOf(position + 1));
				this.dataBinding.setMovieItemBean(adapterInfo.movieList.get(position));
				this.dataBinding.executePendingBindings();
			}
		};
	}
}
