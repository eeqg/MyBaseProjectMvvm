package com.example.wp.mybaseprojectmvvm.common;

import com.example.wp.mybaseprojectmvvm.cart.CartFragment;
import com.example.wp.mybaseprojectmvvm.find.FindFragment;
import com.example.wp.mybaseprojectmvvm.home.HomeFragment;
import com.example.wp.mybaseprojectmvvm.mine.MineFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * Created by wp on 2019/3/22.
 */
public class MainHelper {
	
	private static MainHelper INSTANCE;
	
	private MainHelper() {
	}
	
	public static synchronized MainHelper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MainHelper();
		}
		return INSTANCE;
	}
	
	public List<Fragment> getFragmentList() {
		List<Fragment> fragments = new ArrayList<>();
		fragments.add(new HomeFragment());
		fragments.add(new FindFragment());
		fragments.add(new CartFragment());
		fragments.add(new MineFragment());
		return fragments;
	}
}
