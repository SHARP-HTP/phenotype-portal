package edu.mayo.phenoportal.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Image implements IsSerializable {

	private String imagePath;
	private int width;
	private int height;

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("imagePath: " + imagePath + "\n");
		sb.append("width: " + width + "\n");
		sb.append("height: " + height);
		return sb.toString();
	}

}
