package com.bankwel.j3d.raytracing.model;

import java.awt.Color;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.model.core.Surface.IntensityRate;
import com.bankwel.j3d.raytracing.plugins.MathUtils;

public class Intensity {
	private float red;
	private float green;
	private float blue;

	public Intensity() {
		red = 0;
		green = 0;
		blue = 0;
	}

	public Intensity(float red, float green, float blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public float max() {
		return MathUtils.max(red, green, blue);
	}

	public Intensity copy() {
		return new Intensity(red, green, blue);
	}

	/**
	 * operator '+=' </br>
	 * 
	 * @param intensity
	 * @return this
	 */
	public Intensity join(@NotNull Intensity intensity) {
		red += intensity.getRed();
		green += intensity.getGreen();
		blue += intensity.getBlue();
		return this;
	}

	/**
	 * operator '*='</br>
	 * 
	 * @param index
	 * @return this
	 */
	public Intensity reduce(@NotNull IntensityRate index) {
		red *= index.getRed();
		green *= index.getGreen();
		blue *= index.getBlue();
		return this;
	}

	/**
	 * operator '*='</br>
	 * 
	 * @param rate
	 * @return this
	 */
	public Intensity reduce(float rate) {
		red *= rate;
		green *= rate;
		blue *= rate;
		return this;
	}

	/**
	 * operator '*='</br>
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return this
	 */
	public Intensity reduce(float r, float g, float b) {
		red *= r;
		green *= g;
		blue *= b;
		return this;
	}

	/**
	 * decline by distance.
	 * 
	 * @param vec
	 * @return this
	 */
	public Intensity decline(@NotNull Vector vec) {
		float d = vec.length();
		float f = new Vector(d * d, d, 1).dot(Constant.RAY_DECLINE_FACTOR);
		if (f == 0)
			throw new ArithmeticException();
		return reduce(1 / f);
	}

	public float getRed() {
		return red;
	}

	public float getGreen() {
		return green;
	}

	public float getBlue() {
		return blue;
	}

	public Color toColor() {
		return new Color(resize(red), resize(green), resize(blue));
	}

	private float resize(float value) {
		value = Math.max(0, value);
		value = Math.min(1, value);
		return value;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append('(');
		buffer.append(red);
		buffer.append(',');
		buffer.append(green);
		buffer.append(',');
		buffer.append(blue);
		buffer.append(')');
		return buffer.toString();
	}

}
