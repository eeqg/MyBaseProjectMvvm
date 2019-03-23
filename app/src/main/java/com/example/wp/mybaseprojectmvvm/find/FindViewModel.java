package com.example.wp.mybaseprojectmvvm.find;

import com.example.wp.mybaseprojectmvvm.find.repository.FindRepository;
import com.example.wp.mybaseprojectmvvm.find.repository.bean.MovieListBean;
import com.example.wp.resource.basic.model.BasicViewModel;
import com.example.wp.resource.basic.model.ModelLiveData;
import com.example.wp.resource.utils.LogUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wp on 2019/3/22.
 */
public class FindViewModel extends BasicViewModel {
	private final FindRepository findRepository;
	
	private ModelLiveData<MovieListBean> movieListLiveData = new ModelLiveData<>();
	
	public FindViewModel() {
		this.findRepository = FindRepository.getInstance();
	}
	
	public ModelLiveData<MovieListBean> getMovieListLiveData() {
		return movieListLiveData;
	}
	
	public Disposable listMovie(int start, int count) {
		return this.findRepository.listMovie(start, count)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(this.movieListLiveData.dispose());
	}
	
	public void testRxLifecycle(){
		Observable.interval(1, TimeUnit.SECONDS)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<Long>() {
					@Override
					public void accept(Long aLong) throws Exception {
						LogUtils.d("----"+aLong);
					}
				});
	}
}
