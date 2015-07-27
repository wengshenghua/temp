package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	private Context mContext;

	/**
	 * Province�������
	 */
	public static final String CREATE_PROVINCE = "create table Province ("
			+ "id integer primary key autoincrement, " + "province_name text, "
			+ "province_code text)";

	/**
	 * City�������
	 */
	public static final String CREATE_CITY = "create table City ("
			+ "id integer primary key autoincrement, " + "city_name text, "
			+ "city_code text, " + "province_id integer)";
	/**
	 * County�������
	 */
	public static final String CREATE_COUNTY = "create table County ("
			+ "id integer primary key autoincrement, " + "county_name text, "
			+ "county_code text, " + "city_id integer)";

	/*
	 * ������Ϣ�������
	 */
	public static final String CREATE_WEATHERINFO = "create table WeatherInfo("
			+ "id integer primary key autoincrement," + "city_name text,"
			+ "countycode text," + "temp text," + "weather text,"
			+ "wind text," + "humidity text," + "time text,"
			+ "weathercode text)";
	/*
	 * �û�ѡ��ĳ���
	 */
	public static final String CREATE_USERLIST = "create table UserList("
			+ "id integer primary key autoincrement," + " county_code text,"
			+ "county_name text)";

	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE); // ����Province��
		db.execSQL(CREATE_CITY); // ����City��
		db.execSQL(CREATE_COUNTY); // ����County��
		db.execSQL(CREATE_WEATHERINFO);
		db.execSQL(CREATE_USERLIST);
		Toast.makeText(mContext, "���ݿ⽨���ɹ�", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		switch (oldVersion) {
		case 1:
			db.execSQL(CREATE_WEATHERINFO);// �Ӱ汾1���汾2
		//	Toast.makeText(mContext, "�汾1��2�ɹ�", Toast.LENGTH_SHORT).show();
		case 2:
			db.execSQL(CREATE_USERLIST);// �汾2���汾3
		//	Toast.makeText(mContext, "�汾2��3�ɹ�", Toast.LENGTH_SHORT).show();
		default:
		}
	}
}