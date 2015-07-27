package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.Fragment.WeatherFragment;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.Topbar;
import com.coolweather.app.model.Topbar.topbarClickListener;
import com.coolweather.app.model.WeatherInfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;

public class WeatherFragmentActivity extends FragmentActivity {
	/*
	 * ViewPager
	 */
	private ViewPager m_vp;
	/*
	 * 用户已选择城市数目
	 */
	int citylist;
	/**
	 * topbar
	 */
	private Topbar topbar;
	/*
	 * 城市list
	 */
	private ArrayList<Fragment> fragmentList;
	private int list = 3;
	// 标题列表
	ArrayList<String> titleList = new ArrayList<String>();
	private CoolWeatherDB db;
	private List<WeatherInfo> userlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_weatherfragment);
		m_vp = (ViewPager) findViewById(R.id.id_viewpager);
		m_vp.setOffscreenPageLimit(4);
		topbar = (Topbar) findViewById(R.id.id_topbar_weatherfragment);
		topbar.setTitleIsvisable(false);
		topbar.setOnTopbarClickListener(new topbarClickListener() {

			@Override
			public void rightClick() {
				// TODO Auto-generated method stub

				startActivity(new Intent(WeatherFragmentActivity.this,
						UserList.class));
			}

			@Override
			public void leftClick() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WeatherFragmentActivity.this,
						ChooseAreaActivity.class);
				intent.putExtra("from_weather_activity", true);
				startActivity(intent);
				finish();
			}
		});

		// 处理城市数目
		// 从数据库获取用户已选择的城市数量
		db = CoolWeatherDB.getInstance(this);

		if (getIntent() != null) {

			db.saveUserList(getIntent().getStringExtra("county_code"),
					getIntent().getStringExtra("county_name"));
		}

		userlist = new ArrayList<WeatherInfo>();
		userlist = db.loadUserList();
		fragmentList = new ArrayList<Fragment>();
		WeatherFragment[] fragemts = new WeatherFragment[userlist.size()];

		for (int i = 0; i < userlist.size(); i++) {
			WeatherFragment fragment = new WeatherFragment();
			fragemts[i] = fragment;

			fragmentList.add(fragemts[i]);
			titleList.add("第" + i + " 页");
		}

		m_vp.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));

	}

	// ViewPager的适配器
	public class MyViewPagerAdapter extends FragmentPagerAdapter {

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {

			Fragment fragment = fragmentList.get(arg0);
			Bundle data = new Bundle();
			WeatherInfo weatherInfo = new WeatherInfo();
			weatherInfo = userlist.get(arg0);
			data.putString("countyCode", weatherInfo.getCountyCode());
			fragment.setArguments(data); // 通过Bundle像fragment传递数据

			return fragment;
		}

		@Override
		public int getCount() {

			return fragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titleList.get(position);
		}

	}

}
