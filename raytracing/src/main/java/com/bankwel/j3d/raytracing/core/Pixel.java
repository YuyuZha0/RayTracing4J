package com.bankwel.j3d.raytracing.core;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.model.Source.Intensity;

public class Pixel {

	private int x;
	private int y;
	private Intensity intensity;

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

	public Intensity getIntensity() {
		return intensity;
	}

	public void setIntensity(Intensity intensity) {
		this.intensity = intensity;
	}

	public void render(@NotNull BufferedImage image) {
		image.setRGB(x, y, intensity.toColor().getRGB());
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append('(');
		buffer.append(x);
		buffer.append(',');
		buffer.append(y);
		buffer.append(':');
		buffer.append(intensity);
		buffer.append(')');
		return buffer.toString();
	}

	public static void main(String[] args) {
		System.out.println(Color.BLACK.getRGB());
	}
}
