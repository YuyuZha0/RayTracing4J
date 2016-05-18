package com.bankwel.j3d.raytracing.core;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.validation.constraints.NotNull;

public class Pixel {

	private int x;
	private int y;
	private int rgb;

	//private static final Logger logger = LoggerFactory.getLogger(Pixel.class);

	public Pixel(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Ray ray(@NotNull Vector viewPoint) {
		Vector origin = viewPoint;
		Vector direction = new Vector(x - viewPoint.x, y - viewPoint.y, -viewPoint.z);
		return new Ray(origin, direction);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getRgb() {
		return rgb;
	}

	public void setRgb(int rgb) {
		this.rgb = rgb;
	}

	public void render(BufferedImage image) {
		//logger.info("Rendering pixel {}.", this);
		image.setRGB(x, y, rgb);
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append('(');
		buffer.append(x);
		buffer.append(',');
		buffer.append(y);
		buffer.append(':');
		buffer.append(rgb);
		buffer.append(')');
		return buffer.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(Color.BLACK.getRGB());
	}
}
