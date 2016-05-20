package com.bankwel.j3d.raytracing.core.model;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Vector;

/**
 * 
 * @author yuyuzhao
 *
 */
public interface Surface extends Geometry {

	IlluminationIndex illuminationIndexAt(@NotNull Vector point);

	ColorIndex colorIndexAt(@NotNull Vector point);

	RefractionIndex refractionIndexAt(@NotNull Vector point);

	static class IlluminationIndex implements Index {

		private float specular;
		private float diffuse;
		private float highlight;

		public IlluminationIndex(float specular, float diffuse, float highlight) {
			specular = Math.abs(specular);
			diffuse = Math.abs(diffuse);
			highlight = Math.abs(highlight);

			float z = Math.max(specular, diffuse);
			if (z == 0)
				z = 1;
			this.specular = specular / z;
			this.diffuse = diffuse / z;
			this.highlight = highlight;
		}

		public float getSpecular() {
			return specular;
		}

		public void setSpecular(float specular) {
			this.specular = specular;
		}

		public float getDiffuse() {
			return diffuse;
		}

		public void setDiffuse(float diffuse) {
			this.diffuse = diffuse;
		}

		public float getHighlight() {
			return highlight;
		}

		public void setHighlight(float highlight) {
			this.highlight = highlight;
		}

	}

	/**
	 * The reflection index of red,green and blue at the point,each index must
	 * less than 1;
	 * 
	 * @author yuyuhzhao
	 *
	 */
	static class ColorIndex implements Index {

		private float red;
		private float green;
		private float blue;

		public ColorIndex() {
			red = 1;
			green = 1;
			blue = 1;
		}

		public ColorIndex(float red, float green, float blue) {
			red = Math.abs(red);
			green = Math.abs(green);
			blue = Math.abs(blue);
			float max = Math.max(red, Math.max(green, blue));
			max = Math.max(max, 1);
			this.red = red / max;
			this.green = green / max;
			this.blue = blue / max;
		}

		public float getRed() {
			return red;
		}

		public void setRed(float red) {
			this.red = red;
		}

		public float getGreen() {
			return green;
		}

		public void setGreen(float green) {
			this.green = green;
		}

		public float getBlue() {
			return blue;
		}

		public void setBlue(float blue) {
			this.blue = blue;
		}

	}

	static class RefractionIndex implements Index {

		private boolean transparent;
		private float front;
		private float back;

		/**
		 * Constructer if the surface is not transparent.
		 */
		public RefractionIndex() {
			transparent = false;
		}

		public RefractionIndex(float front, float back) {
			transparent = true;
			this.front = Math.abs(front);
			this.back = Math.abs(back);
		}

		public boolean isTransparent() {
			return transparent;
		}

		public void setTransparent(boolean transparent) {
			this.transparent = transparent;
		}

		public float getFront() {
			return front;
		}

		public void setFront(float front) {
			this.front = front;
		}

		public float getBack() {
			return back;
		}

		public void setBack(float back) {
			this.back = back;
		}
	}

}
