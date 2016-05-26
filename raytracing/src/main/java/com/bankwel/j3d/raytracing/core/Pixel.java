package com.bankwel.j3d.raytracing.core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.model.Source.Intensity;

public class Pixel {

	private int x;
	private int y;
	private int divide = 1;
	private Intensity intensity;

	public Pixel(int x, int y, int divide) {
		this.x = x;
		this.y = y;
		if (divide > 0)
			this.divide = divide;
	}

	private Ray ray(Vector viewPoint, float x, float y) {
		Vector direction = new Vector(x - viewPoint.x, y - viewPoint.y, -viewPoint.z);
		return new Ray(viewPoint, direction);
	}

	public List<Ray> ray(@NotNull Vector viewPoint) {
		List<Ray> rays = new ArrayList<Ray>();
		float dif = 1f / divide;
		for (int i = 0; i < divide; i++)
			for (int j = 0; j < divide; j++)
				rays.add(ray(viewPoint, x + dif * i, y + dif * j));
		return rays;
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
		float dif = 1f / 2;
		System.out.println(dif);
	}
}
