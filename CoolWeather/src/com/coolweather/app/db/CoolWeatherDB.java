package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.model.WeatherInfo;

public class CoolWeatherDB {

	/**
	 * ���ݿ���
	 */
	public static final String DB_NAME = "cool_weather";
	/**
	 * ���ݿ�汾
	 */
	public static final int VERSION = 3;
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;

	/**
	 * �����췽��˽�л�
	 */
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * ��ȡCoolWeatherDB��ʵ����
	 * synchronize用一时刻最多只能有一个线程执行这段代码
	 * ����һ���̱߳���ȵ���ǰ�߳�ִ�������������Ժ����ִ����������
	 */

	public synchronized static CoolWeatherDB getInstance(Context context) {
		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}

	/**
	 * ��Provinceʵ���洢�����ݿ⡣
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}

	/**
	 * �����ݿ��ȡȫ�����е�ʡ����Ϣ��
	 */
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor
						.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor
						.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * ��Cityʵ���洢�����ݿ⡣
	 */
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}

	/**
	 * �����ݿ��ȡĳʡ�����еĳ�����Ϣ��
	 */
	public List<City> loadCities(int provinceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?",
				new String[] { String.valueOf(provinceId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor
						.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor
						.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * ��Countyʵ���洢�����ݿ⡣
	 */
	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}
	}

	/**
	 * �����ݿ��ȡĳ���������е�����Ϣ��
	 */
	public List<County> loadCounties(int cityId) {
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?",
				new String[] { String.valueOf(cityId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor
						.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor
						.getColumnIndex("county_code")));
				county.setCityId(cityId);
				list.add(county);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * ��Weather��Ϣʵ���洢�����ݿ⡣
	 */
	public void savaWeatherInfo(WeatherInfo weatherInfo) {

		if (weatherInfo != null) {

			ContentValues values = new ContentValues();
			values.put("city_name", weatherInfo.getCountyName());
			values.put("countycode", weatherInfo.getCountyCode());
			values.put("weathercode", weatherInfo.getWeatherCode());
			values.put("temp", weatherInfo.getTemp());
			values.put("weather", weatherInfo.getWeather());
			values.put("wind", weatherInfo.getWind());
			values.put("humidity", weatherInfo.getHumidity());
			values.put("time", weatherInfo.getTime());
			db.delete(
					"WeatherInfo",
					"countycode=?",
					new String[] { String.valueOf(weatherInfo.getCountyCode()) });
			db.insert("WeatherInfo", null, values);

		}
	}

	/**
	 * �����ݿ��ȡWeather��Ϣ
	 */
	public WeatherInfo loadWeatherInfo(String countyCode) {

		Cursor cursor = db.query("WeatherInfo", null, "countycode = ?",
				new String[] { String.valueOf(countyCode) }, null, null, null);

		WeatherInfo weatherInfo = new WeatherInfo();
		if (cursor.moveToFirst()) {

			weatherInfo.setCountyName(cursor.getString(cursor
					.getColumnIndex("city_name")));

			weatherInfo.setCountyCode(cursor.getString(cursor
					.getColumnIndex("countycode")));
			weatherInfo.setWeatherCode(cursor.getString(cursor
					.getColumnIndex("weathercode")));
			weatherInfo
					.setTemp(cursor.getString(cursor.getColumnIndex("temp")));
			weatherInfo.setWeather(cursor.getString(cursor
					.getColumnIndex("weather")));
			weatherInfo
					.setWind(cursor.getString(cursor.getColumnIndex("wind")));
			weatherInfo.setHumidity(cursor.getString(cursor
					.getColumnIndex("humidity")));
			weatherInfo
					.setTime(cursor.getString(cursor.getColumnIndex("time")));

		}

		return weatherInfo;

	}

	/*
	 * ���û�ѡ��ĳ��д洢�����ݿ�
	 */
	public void saveUserList(String countycode, String countyname) {
		if (countycode != null) {
			ContentValues values = new ContentValues();
			values.put("county_code", countycode);
			values.put("county_name", countyname);

			db.delete("UserList", "county_code=?",
					new String[] { String.valueOf(countycode) });
			db.insert("UserList", null, values);

		}

	}

	/*
	 * ��ȡ�û�ѡ�ŵ��������г���
	 */

	public List<WeatherInfo> loadUserList() {
		List<WeatherInfo> list = new ArrayList<WeatherInfo>();
		Cursor cursor = db
				.query("UserList", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				WeatherInfo weatherInfo = new WeatherInfo();

				weatherInfo.setCountyCode(cursor.getString(cursor
						.getColumnIndex("county_code")));
				weatherInfo.setCountyName(cursor.getString(cursor
						.getColumnIndex("county_name")));
				list.add(weatherInfo);

			} while (cursor.moveToNext());
		}

		return list;

	}

	/*
	 * 删除用户列表的城市
	 */
	public void DedletItemOfUserList(String countycode) {

		db.delete("UserList", "county_code=?",
				new String[] { String.valueOf(countycode) });

	}

	/*
	 * ��ȡ�û��б�������
	 */
	public WeatherInfo loadUsersingle(String countyCode) {
		WeatherInfo weatherInfo = new WeatherInfo();
		Cursor cursor = db.query("UserList", null, "county_code=?",
				new String[] { String.valueOf(countyCode) }, null, null, null);
		if (cursor.moveToFirst()) {
			weatherInfo.setCountyName(cursor.getString(cursor
					.getColumnIndex("county_name")));
			weatherInfo.setCountyCode(cursor.getString(cursor
					.getColumnIndex("county_code")));
			weatherInfo.setWeatherCode(cursor.getString(cursor
					.getColumnIndex("weather_code")));

		}
		return weatherInfo;
	}
}
