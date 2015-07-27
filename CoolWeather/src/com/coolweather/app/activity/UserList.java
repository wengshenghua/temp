package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;

import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.coolweather.app.R;
import com.coolweather.app.apapter.CommonAdapter;
import com.coolweather.app.apapter.ViewHolder;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.GridViewItem;
import com.coolweather.app.model.WeatherInfo;

public class UserList extends Activity implements OnItemClickListener {

	private ListView lV;
	private CoolWeatherDB db;
	private List<WeatherInfo> userlist;
	private String[] data;
	// private ArrayAdapter<String> adapter;
	private Button backButton;
	private CommonAdapter<WeatherInfo> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_userlist);
		userlist = new ArrayList<WeatherInfo>();
		initView();
		initData();
		// adapter = new
		// ArrayAdapter<String>(UserList.this,R.layout.item_listview, data);
		adapter = new CommonAdapter<WeatherInfo>(UserList.this, userlist,
				R.layout.item_listview) {

			@Override
			public void convert(ViewHolder holder, WeatherInfo t) {
				// TODO Auto-generated method stub
				holder.setText(R.id.id_item_cityname, t.getCountyName());
			}
		};

		lV.setAdapter(adapter);
		lV.setOnItemClickListener(this);
		backButton.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(UserList.this, MainActivityGroup.class));
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		db = CoolWeatherDB.getInstance(this);

		userlist = db.loadUserList();
		// int i = 0;

		// data = new String[userlist.size()];
		// for (WeatherInfo weatherInfo : userlist) {

		// data[i] = weatherInfo.getCountyName();
		// i++;
		// }

	}

	private void initView() {
		// TODO Auto-generated method stub
		lV = (ListView) findViewById(R.id.id_userlistLV);
		backButton = (Button) findViewById(R.id.id_button);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
			long arg3) {
		// TODO Auto-generated method stub

		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("确认删除吗？").setTitle("提示")
				.setPositiveButton("确认!", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int index) {
						// TODO Auto-generated method stub
						WeatherInfo weatherInfo = userlist.get(position);
						db.DedletItemOfUserList(weatherInfo.getCountyCode());

						userlist.remove(position);

						adapter.notifyDataSetChanged();
						Toast.makeText(UserList.this, "删除成功", Toast.LENGTH_SHORT).show();
						dialog.dismiss();

					}
				}).setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).create().show();

	}

}
