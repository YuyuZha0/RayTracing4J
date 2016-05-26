package com.bankwel.j3d.raytracing.model.core;

import java.awt.Color;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.model.Constant;
import com.bankwel.j3d.raytracing.model.Vector;
import com.bankwel.j3d.raytracing.model.core.Surface.IllumIndex;
import com.bankwel.j3d.raytracing.model.core.Surface.IntensityRate;
import com.bankwel.j3d.raytracing.plugins.MathUtils;

public interface Source extends Geometry {

	Intensity intensityAt(@NotNull Vector u, @NotNull Vector point, @NotNull Vector normal,
			@NotNull IllumIndex illuminationIndex, @NotNull List<Intersectable> shelters);

	public static class Intensity {

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

		public Intensity decline(@NotNull Vector vec) {
			float d = vec.length();
			float f = new Vector(d * d, d, 1).dot(Constant.RAY_DECLINE_FACTOR);
			if (f == 0)
				throw new ArithmeticException();
			return new Intensity(red / f, green / f, blue / f);
		}

		/**
		 * operator '+=' </br>
		 * 
		 * @param intensity
		 */
		public void join(@NotNull Intensity intensity) {
			red += intensity.getRed();
			green += intensity.getGreen();
			blue += intensity.getBlue();
		}

		/**
		 * operator '*='</br>
		 * 
		 * @param index
		 */
		public void reduce(@NotNull IntensityRate index) {
			red *= index.getRed();
			green *= index.getGreen();
			blue *= index.getBlue();
		}

		public Intensity multiply(float rate) {
			return new Intensity(red * rate, green * rate, blue * rate);
		}

		public Intensity multiply(float r, float g, float b) {
			return new Intensity(red * r, green * g, blue * b);
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
}
