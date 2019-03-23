package com.example.wp.mybaseprojectmvvm.find.repository;

import com.example.wp.mybaseprojectmvvm.find.repository.bean.MovieListBean;
import com.example.wp.mybaseprojectmvvm.find.repository.remote.RemoteFindDataSource;

import io.reactivex.Observable;

/**
 * Created by wp on 2019/3/22.
 */
public class FindRepository {
	private static FindRepository INSTANCE;
	
	private RemoteFindDataSource remoteFindDataSource;
	
	public static FindRepository getInstance() {
		if (INSTANCE == null) {
			synchronized (FindRepository.class) {
				if (INSTANCE == null) {
					INSTANCE = new FindRepository();
				}
			}
		}
		return INSTANCE;
	}
	
	private FindRepository() {
		this.remoteFindDataSource = RemoteFindDataSource.getInstance();
	}
	
	public Observable<MovieListBean> listMovie(int start, int count) {
		return this.remoteFindDataSource.listMovie(start, count);
	}
}
