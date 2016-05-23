package com.bankwel.j3d.raytracing.core.model;

import java.awt.Color;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Config;
import com.bankwel.j3d.raytracing.core.Vector;
import com.bankwel.j3d.raytracing.core.model.Surface.ColorIndex;

public interface Source extends Geometry {

	boolean isInShadow(@NotNull Vector point, @NotNull List<VisibleSurface> surfaces);

	Intensity intensityAt(@NotNull Vector u, @NotNull Vector point, @NotNull Vector normal, @NotNull Surface Surface);

	public static class Intensity {

		private float red;
		private float green;
		private float blue;

		private static float max = 1;

		public Intensity() {
			red = 0;
			green = 0;
			blue = 0;
		}

		public Intensity(float red, float green, float blue) {
			this.red = red;
			this.green = green;
			this.blue = blue;
			max = Math.max(max, max());
		}

		private float max() {
			return Math.max(red, Math.max(green, blue));
		}

		public Intensity decline(@NotNull Vector vec) {
			float d = vec.normal();
			float f = new Vector(d * d, d, 1).dot(Config.RAY_DECLINE_FACTOR);
			if (f == 0)
				throw new ArithmeticException();
			return new Intensity(red / f, green / f, blue / f);
		}

		public Intensity join(@NotNull Intensity intensity) {
			return new Intensity(red + intensity.getRed(), green + intensity.green, blue + intensity.getBlue());
		}

		public Intensity multiply(float rate) {
			return new Intensity(red * rate, green * rate, blue * rate);
		}

		public Intensity reduce(@NotNull ColorIndex colorIndex) {
			return new Intensity(red * colorIndex.getRed(), green * colorIndex.getGreen(), blue * colorIndex.getBlue());
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
