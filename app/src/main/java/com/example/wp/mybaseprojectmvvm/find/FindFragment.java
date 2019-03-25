package com.example.wp.mybaseprojectmvvm.find;

import com.example.wp.mybaseprojectmvvm.R;
import com.example.wp.mybaseprojectmvvm.databinding.FragmentFindBinding;
import com.example.wp.resource.basic.BasicFragment;
import com.example.wp.resource.widget.NormalItemDecoration;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Created by wp on 2019/3/22.
 */
public class FindFragment extends BasicFragment<FragmentFindBinding> {
	@Override
	public int getContentView() {
		return R.layout.fragment_find;
	}
	
	@Override
	public void init() {
	
	}
	
	@Override
	public void initView() {
		this.dataBinding.recycler.setLayoutManager(new LinearLayoutManager(getContext()));
		this.dataBinding.recycler.addItemDecoration(new NormalItemDecoration(getContext()));
	}
}
