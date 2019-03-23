package com.example.wp.mybaseprojectmvvm.find.repository.local;

import com.example.wp.mybaseprojectmvvm.find.repository.FindDataSource;
import com.example.wp.mybaseprojectmvvm.find.repository.bean.MovieListBean;

import io.reactivex.Observable;

/**
 * Created by wp on 2019/3/23.
 */
public class LocalFindDataSource implements FindDataSource {
	@Override
	public Observable<MovieListBean> listMovie(int start, int count) {
		return null;
	}
}
