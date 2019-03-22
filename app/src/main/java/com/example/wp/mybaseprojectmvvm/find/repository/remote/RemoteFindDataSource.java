package com.example.wp.mybaseprojectmvvm.find.repository.remote;

import com.example.wp.mybaseprojectmvvm.find.repository.FindDataSource;
import com.example.wp.mybaseprojectmvvm.find.repository.bean.MovieListBean;

import io.reactivex.Observable;

/**
 * Created by wp on 2019/3/22.
 */
public class RemoteFindDataSource implements FindDataSource {
	@Override
	public Observable<MovieListBean> listMovie() {
		return null;
	}
}
