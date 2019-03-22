package com.example.wp.mybaseprojectmvvm;

import android.view.MenuItem;

import com.example.wp.mybaseprojectmvvm.databinding.ActivityMainBinding;
import com.example.wp.mybaseprojectmvvm.helper.MainHelper;
import com.example.wp.resource.basic.BasicActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends BasicActivity<ActivityMainBinding> {
	
	private List<Fragment> fragments;
	private int currentPageIndex = -1;
	
	@Override
	public int getContentView() {
		return R.layout.activity_main;
	}
	
	@Override
	public void init() {
		fragments = MainHelper.getInstance().getFragmentList();
	}
	
	@Override
	public void initView() {
		dataBinding.navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
		switchFragment(0);
	}
	
	private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
			new BottomNavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
					switch (menuItem.getItemId()) {
						case R.id.home:
							switchFragment(0);
							return true;
						case R.id.find:
							switchFragment(1);
							return true;
						case R.id.cart:
							switchFragment(2);
							return true;
						case R.id.mine:
							switchFragment(3);
							return true;
					}
					return false;
				}
			};
	
	/**
	 * switch page
	 */
	private void switchFragment(int pageIndex) {
		if (pageIndex == currentPageIndex) {
			return;
		}
		Fragment fragment = fragments.get(pageIndex);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		if (currentPageIndex >= 0) {
			transaction.hide(fragments.get(currentPageIndex));
		}
		if (fragment.isAdded()) {
			transaction.show(fragment).commitAllowingStateLoss();
		} else {
			transaction.add(R.id.flContent, fragment).commitAllowingStateLoss();
		}
		currentPageIndex = pageIndex;
	}
}
