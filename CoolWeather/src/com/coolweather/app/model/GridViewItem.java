package com.coolweather.app.model;

public class GridViewItem {
	private int imageId;
	private String tabText;

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getTabText() {
		return tabText;
	}

	public void setTabText(String tabText) {
		this.tabText = tabText;
	}

	public GridViewItem(int imageId, String tabText) {
		super();
		this.imageId = imageId;
		this.tabText = tabText;
	}

	

}
