package com.example.wp.mybaseprojectmvvm.find;

import com.example.wp.mybaseprojectmvvm.R;
import com.example.wp.mybaseprojectmvvm.databinding.FragmentFindBinding;
import com.example.wp.mybaseprojectmvvm.find.repository.bean.MovieListBean;
import com.example.wp.resource.basic.BasicFragment;
import com.example.wp.resource.basic.model.DataObserver;
import com.example.wp.resource.basic.model.StatusInfo;
import com.example.wp.resource.widget.NormalItemDecoration;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import cn.shyman.library.refresh.OnTaskListener;
import io.reactivex.disposables.Disposable;

/**
 * Created by wp on 2019/3/22.
 */
public class FindFragment extends BasicFragment<FragmentFindBinding> {
	private FindViewModel findViewModel;
	private FindMovieListAdapter findMovieListAdapter;
	
	@Override
	public int getContentView() {
		return R.layout.fragment_find;
	}
	
	@Override
	public void init() {
		this.findViewModel = ViewModelProviders.of(this).get(FindViewModel.class);
	}
	
	@Override
	public void initView() {
		this.dataBinding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));
		this.dataBinding.recycler.addItemDecoration(new NormalItemDecoration(getContext()));
		
		this.findMovieListAdapter = new FindMovieListAdapter(getContext());
		this.findMovieListAdapter.setRefreshLayout(this.dataBinding.refreshLayout);
		this.findMovieListAdapter.setRecyclerView(this.dataBinding.recycler);
		this.findMovieListAdapter.setOnTaskListener(new OnTaskListener<Disposable>() {
			@Override
			public Disposable onTask() {
				int currentPage = findMovieListAdapter.getCurrentPage();
				int count = findMovieListAdapter.getDefaultPageSize();
				int start = (currentPage - 1) * count;
				return findViewModel.listMovie(start, count);
			}
			
			@Override
			public void onCancel(Disposable disposable) {
				disposable.dispose();
			}
		});
		
		subscribe();
		this.findMovieListAdapter.swipeRefresh();
	}
	
	@Override
	public void subscribe() {
		this.findViewModel.getMovieListLiveData().observe(this,
				new DataObserver<MovieListBean>(this) {
					@Override
					protected void dataResult(MovieListBean basicBean) {
						findMovieListAdapter.swipeResult(basicBean);
					}
					
					@Override
					protected void dataStatus(StatusInfo statusInfo) {
						findMovieListAdapter.swipeStatus(statusInfo);
					}
				});
	}
}
