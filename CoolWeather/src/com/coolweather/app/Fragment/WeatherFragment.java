package com.coolweather.app.Fragment;

import java.util.Date;

import org.json.JSONObject;

import com.coolweather.app.R;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.Topbar;
import com.coolweather.app.model.WeatherInfo;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherFragment extends Fragment {
	private View mMainView;
	private String countyCode;
	private LinearLayout weatherInfoLayout;
	private CoolWeatherDB coolWeatherDB;

	/**
	 * 装载内容的scrollView
	 */
	private ScrollView mScrollView;
	/**
	 * 内容的容器
	 */
	private RelativeLayout mRelativeLayout;
	/**
	 * 发布时间
	 */
	private TextView publishText;
	/**
	 * 天气desp
	 */
	private TextView weatherDespText;
	/**
	 * 天气
	 */
	private TextView tempText;
	/**
	 * 风向
	 */
	private TextView windText;
	/**
	 * 湿度
	 */
	private TextView humidityText;
	/**
	 * 现在的日期
	 */
	private TextView currentDateText;
	/*
	 * 城市名字(non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	private TextView citynameText;

	/*
	 * 下拉刷新控件(non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	private ScrollView sc;
	private LinearLayout header;
	private ImageView arrowImg;
	private ProgressBar headProgress;
	private TextView lastUpdateTxt;
	private TextView tipsTxt;
	private RotateAnimation tipsAnimation;
	private RotateAnimation reverseAnimation;
	private LayoutInflater inflater;
	private LinearLayout globleLayout;
	private int headerHeight; // 头高度
	private int lastHeaderPadding; // 最后一次调用Move Header的Padding
	private boolean isBack; // 从Release 转到 pull
	private int headerState = DONE;
	static final private int RELEASE_To_REFRESH = 0;
	static final private int PULL_To_REFRESH = 1;
	static final private int REFRESHING = 2;
	static final private int DONE = 3;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		 inflater = getActivity().getLayoutInflater();

		mMainView = inflater.inflate(R.layout.weather_layout,
				(ViewGroup) getActivity().findViewById(R.id.id_viewpager),
				false);
		// 取得各种空间实例
		initView();
		// 获取传送过来的城市代码
		Bundle code = getArguments();
		countyCode = code.getString("countyCode");
		if (!TextUtils.isEmpty(countyCode)) {
			publishText.setText("正在加载数据");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			// topbar.setTitleIsvisable(false);

			queryWeatherCode(countyCode);

		} else {
			// û���ش��ŵ�ʱ����ʾ���ݿ��������Ϣ
			// showWeather();
		}

		init(getActivity());
	}

	// 查询城市号码对应的天气号码
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city"
				+ countyCode + ".xml";
		queryFromServer(address, "countyCode", null);
	}

	// 根据天气号码查询天气
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/"
				+ weatherCode + ".html";
		queryFromServer(address, "weatherCode", null);
	}

	/*
	 * 查询另外地址的天气
	 */
	private void queryMoreWeatherInfo(String weatherCode, String weather) {
		String address = "http://www.weather.com.cn/data/sk/" + weatherCode
				+ ".html";
		queryFromServer(address, "weatherMore", weather);
	}

	private void queryFromServer(final String address, final String type,
			final String weather) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				if ("countyCode".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						// �ӷ��������ص������н�������������
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				} else if ("weatherCode".equals(type)) {
					// ������������ص�������Ϣ
					try {
						JSONObject jsonObject = new JSONObject(response);
						JSONObject weatherInfo = jsonObject
								.getJSONObject("weatherinfo");
						String weather = weatherInfo.getString("weather");
						String weatherCode = weatherInfo.getString("cityid");
						queryMoreWeatherInfo(weatherCode, weather);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				} else if ("weatherMore".equals(type)) {

					Utility.handleMoreWeatherResponse(coolWeatherDB, response,
							weather, countyCode, getActivity());

					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showWeather();
						}
					});

				}

			}

			@Override
			public void onError(Exception e) {
			//	getActivity().runOnUiThread(new Runnable() {
		//			@Override
			//		public void run() {
		//			//	publishText.setText("ͬ��ʧ��");
		//			}
		//		});
		}
		});
	}

	// 显示天气啦
	private void showWeather() {
		WeatherInfo weatherInfo = coolWeatherDB.loadWeatherInfo(countyCode);

		tempText.setText(weatherInfo.getTemp());
		windText.setText(weatherInfo.getWind());
		weatherDespText.setText(weatherInfo.getWeather());
		humidityText.setText(weatherInfo.getHumidity());
		publishText.setText("发布时间" + weatherInfo.getTime() + "点");
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		citynameText.setText(weatherInfo.getCountyName());
		// Intent intent = new Intent(this, AutoUpdateService.class);
		// startService(intent);
		onRefreshComplete();

	}

	private void initView() {
		// TODO Auto-generated method stub
		coolWeatherDB = CoolWeatherDB.getInstance(getActivity());
		weatherInfoLayout = (LinearLayout) mMainView
				.findViewById(R.id.weather_info_layout);
		publishText = (TextView) mMainView.findViewById(R.id.id_publish_text);
		weatherDespText = (TextView) mMainView.findViewById(R.id.id_weather);
		tempText = (TextView) mMainView.findViewById(R.id.id_temp);
		windText = (TextView) mMainView.findViewById(R.id.id_wind);
		currentDateText = (TextView) mMainView
				.findViewById(R.id.id_current_date);
		humidityText = (TextView) mMainView.findViewById(R.id.id_humidity);
		citynameText = (TextView) mMainView.findViewById(R.id.id_cityname);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup p = (ViewGroup) mMainView.getParent();
		if (p != null) {
			p.removeAllViewsInLayout();

		}

		return mMainView;
	}

	/*
	 * 开始下拉刷新
	 */
	private void init(Context context) {
		globleLayout = (LinearLayout) mMainView.findViewById(R.id.globleLayout);
		sc = (ScrollView) mMainView.findViewById(R.id.id_scrollView);
		// inflater = (LayoutInflater) getActivity().getSystemService(
		// Context.LAYOUT_INFLATER_SERVICE);

		header = (LinearLayout) inflater.inflate(R.layout.header, null);
		measureView(header);
		headerHeight = header.getMeasuredHeight();
		lastHeaderPadding = (-1 * headerHeight); // 最后一次调用Move Header的Padding
		header.setPadding(0, lastHeaderPadding, 0, 0);
		header.invalidate();
		globleLayout.addView(header, 0);

		headProgress = (ProgressBar) mMainView
				.findViewById(R.id.head_progressBar);
		arrowImg = (ImageView) mMainView.findViewById(R.id.head_arrowImageView);
		arrowImg.setMinimumHeight(50);
		arrowImg.setMinimumWidth(50);
		tipsTxt = (TextView) mMainView.findViewById(R.id.head_tipsTextView);
		lastUpdateTxt = (TextView) mMainView
				.findViewById(R.id.head_lastUpdatedTextView);
		// 箭头转动动画
		tipsAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		tipsAnimation.setInterpolator(new LinearInterpolator());
		tipsAnimation.setDuration(200); // 动画持续时间
		tipsAnimation.setFillAfter(true); // 动画结束后保持动画
		// 箭头反转动画
		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);
		// 为scrollview绑定事件
		sc.setOnTouchListener(new OnTouchListener() {
			private int beginY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					// sc.getScrollY == 0 scrollview 滑动到头了
					// lastHeaderPadding > (-1*headerHeight) 表示header还没完全隐藏起来时
					// headerState != REFRESHING 当正在刷新时
					if ((sc.getScrollY() == 0 || lastHeaderPadding > (-1 * headerHeight))
							&& headerState != REFRESHING) {
						// 拿到滑动的Y轴距离
						int interval = (int) (event.getY() - beginY);
						// 是向下滑动而不是向上滑动
						if (interval > 0) {
							interval = interval / 2;// 下滑阻力
							lastHeaderPadding = interval + (-1 * headerHeight);
							header.setPadding(0, lastHeaderPadding, 0, 0);
							if (lastHeaderPadding > 0) {
								// txView.setText("我要刷新咯");
								headerState = RELEASE_To_REFRESH;
								// 是否已经更新了UI
								if (!isBack) {
									isBack = true; // 到了Release状态，如果往回滑动到了pull则启动动画
									changeHeaderViewByState();
								}
							} else {
								headerState = PULL_To_REFRESH;
								changeHeaderViewByState();
								// txView.setText("看到我了哦");
								// sc.scrollTo(0, headerPadding);
							}
						}
					}
					break;
				case MotionEvent.ACTION_DOWN:
					// 加上下滑阻力与实际滑动距离的差（大概值）
					beginY = (int) ((int) event.getY() + sc.getScrollY() * 1.5);
					break;
				case MotionEvent.ACTION_UP:
					if (headerState != REFRESHING) {
						switch (headerState) {
						case DONE:
							// 什么也不干
							break;
						case PULL_To_REFRESH:
							headerState = DONE;
							lastHeaderPadding = -1 * headerHeight;
							header.setPadding(0, lastHeaderPadding, 0, 0);
							changeHeaderViewByState();
							break;
						case RELEASE_To_REFRESH:
							isBack = false; // 准备开始刷新，此时将不会往回滑动
							headerState = REFRESHING;
							changeHeaderViewByState();
							onRefresh();
							break;
						default:
							break;
						}
					}
					break;
				}
				// 如果Header是完全被隐藏的则让ScrollView正常滑动，让事件继续否则的话就阻断事件
				if (lastHeaderPadding > (-1 * headerHeight)
						&& headerState != REFRESHING) {
					return true;
				} else {
					return false;
				}
			}
		});
	}

	private void changeHeaderViewByState() {
		switch (headerState) {
		case PULL_To_REFRESH:
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				arrowImg.startAnimation(reverseAnimation);
				tipsTxt.setText("下拉刷新");
			}
			tipsTxt.setText("下拉刷新");
			break;
		case RELEASE_To_REFRESH:
			arrowImg.setVisibility(View.VISIBLE);
			headProgress.setVisibility(View.GONE);
			tipsTxt.setVisibility(View.VISIBLE);
			lastUpdateTxt.setVisibility(View.VISIBLE);
			arrowImg.clearAnimation();
			arrowImg.startAnimation(tipsAnimation);
			tipsTxt.setText("松开刷新");
			break;
		case REFRESHING:
			lastHeaderPadding = 0;
			header.setPadding(0, lastHeaderPadding, 0, 0);
			header.invalidate();
			headProgress.setVisibility(View.VISIBLE);
			arrowImg.clearAnimation();
			arrowImg.setVisibility(View.INVISIBLE);
			tipsTxt.setText("正在刷新...");
			lastUpdateTxt.setVisibility(View.VISIBLE);
			break;
		case DONE:
			lastHeaderPadding = -1 * headerHeight;
			header.setPadding(0, lastHeaderPadding, 0, 0);
			header.invalidate();
			headProgress.setVisibility(View.GONE);
			arrowImg.clearAnimation();
			arrowImg.setVisibility(View.VISIBLE);
			tipsTxt.setText("下拉刷新");
			lastUpdateTxt.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private void onRefresh() {
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				try {
					queryWeatherCode(countyCode);
					publishText.setText("正在更新");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				
			}

		}.execute();
	}

	public void onRefreshComplete() {
		headerState = DONE;
		lastUpdateTxt.setText("最近更新:" + new Date().toLocaleString());
		changeHeaderViewByState();
	}

	// 由于OnCreate里面拿不到header的高度所以需要手动计算
	private void measureView(View childView) {
		LayoutParams p = childView.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int height = p.height;
		int childHeightSpec;
		if (height > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(height,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		childView.measure(childWidthSpec, childHeightSpec);
	}

}
