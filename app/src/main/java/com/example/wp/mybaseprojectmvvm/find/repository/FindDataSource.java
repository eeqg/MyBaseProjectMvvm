package com.example.wp.mybaseprojectmvvm.find.repository;

import com.example.wp.mybaseprojectmvvm.find.repository.bean.MovieListBean;

import io.reactivex.Observable;

/**
 * Created by wp on 2019/3/22.
 */
public interface FindDataSource {
	Observable<MovieListBean> listMovie();
}
