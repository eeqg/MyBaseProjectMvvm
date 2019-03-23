package com.example.wp.mybaseprojectmvvm.find.repository.remote;

import com.example.wp.mybaseprojectmvvm.common.ResourceNetwork;
import com.example.wp.mybaseprojectmvvm.find.repository.FindDataSource;
import com.example.wp.mybaseprojectmvvm.find.repository.bean.MovieListBean;

import io.reactivex.Observable;

/**
 * Created by wp on 2019/3/22.
 */
public class RemoteFindDataSource implements FindDataSource {
	
	private static RemoteFindDataSource INSTANCE;
	
	private FindService findService;
	
	public static RemoteFindDataSource getInstance() {
		if (INSTANCE == null) {
			synchronized (RemoteFindDataSource.class) {
				if (INSTANCE == null) {
					INSTANCE = new RemoteFindDataSource();
				}
			}
		}
		return INSTANCE;
	}
	
	private RemoteFindDataSource() {
		this.findService = new ResourceNetwork("https://api.douban.com/v2/movie/", "")
				.createService(FindService.class);
	}
	
	@Override
	public Observable<MovieListBean> listMovie(int start, int count) {
		return this.findService.listMovie(start, count);
	}
}
