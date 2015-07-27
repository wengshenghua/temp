package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.R.color;
import android.app.ActionBar.LayoutParams;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Scene;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.coolweather.app.R;
import com.coolweather.app.apapter.CommonAdapter;
import com.coolweather.app.apapter.ViewHolder;
import com.coolweather.app.model.GridViewItem;

public class MainActivityGroup extends ActivityGroup implements
		OnItemClickListener {

	/**
	 * tab �ؼ�
	 */
	private GridView mGridView;
	/**
	 * ��ͨͼƬid����
	 */

	private List<GridViewItem> myimagedatas;
	/**
	 * ��ͼƬ id����
	 */
	private List<GridViewItem> mImageLightIds;
	/**
	 * װ��Activity������
	 */
	private LinearLayout mLinearLayout;
	List<View> aViews = new ArrayList<View>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_grid_view);
		initDatas();
		initView();
		mGridView.setAdapter(new CommonAdapter<GridViewItem>(
				MainActivityGroup.this, myimagedatas, R.layout.item_gridview) {

			@Override
			public void convert(ViewHolder holder, GridViewItem item) {
				// TODO Auto-generated method stub
				holder.setText(R.id.id_item_text, item.getTabText())
						.setImageResource(R.id.id_item_image, item.getImageId());
			}
		});

		mGridView.setOnItemClickListener(this);
		startActivity(0);
	}

	/**
	 * ����position������ͬActivity
	 * 
	 * @param i
	 */
	private void startActivity(int position) {
		// TODO Auto-generated method stub
		// ��������������View
		mLinearLayout.removeAllViews();
		Intent intent = null;
		if (position == 0) {
			intent = new Intent(MainActivityGroup.this,
					WeatherFragmentActivity.class);
			intent.putExtra("county_code",
					getIntent().getStringExtra("county_code"));
			intent.putExtra("county_name",
					getIntent().getStringExtra("county_name"));
		} else if (position == 1) {
			intent = new Intent(MainActivityGroup.this, SceneActivity.class);
		} else if (position == 2) {
			intent = new Intent(MainActivityGroup.this, MyActivity.class);
		}

		// ��Activityת����View
		View view = getLocalActivityManager().startActivity("intent", intent)
				.getDecorView();

		// ��Activityת���ɵ�View��ӵ�����
		mLinearLayout.addView(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

	}

	private void initDatas() {
		// TODO Auto-generated method stub
		myimagedatas = new ArrayList<GridViewItem>();
		GridViewItem a = new GridViewItem(R.drawable.tab_weather, "天气");
		myimagedatas.add(a);
		a = new GridViewItem(R.drawable.tab_shijing, "时景");
		myimagedatas.add(a);
		a = new GridViewItem(R.drawable.tab_my, "我");
		myimagedatas.add(a);

	}

	private void initView() {
		// TODO Auto-generated method stub
		mLinearLayout = (LinearLayout) findViewById(R.id.id_ll_activity_group);
		mGridView = (GridView) findViewById(R.id.id_girdview);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		startActivity(position);
		
		
				

		
		
	}

	private void Switch(int position) {
		// TODO Auto-generated method stub

	}

}
