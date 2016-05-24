package com.bankwel.j3d.raytracing.core.model;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Ray;
import com.bankwel.j3d.raytracing.core.Scene;
import com.bankwel.j3d.raytracing.core.Vector;
import com.bankwel.j3d.raytracing.core.model.Source.Intensity;

/**
 * 
 * @author yuyuzhao
 *
 */
public abstract class Surface implements Intersectable {

	@Override
	public void onIntersecting(Ray ray, Scene scene, Vector point) {
		Vector normal = normalAt(point);
		reflect(ray, scene, point, normal);
		refract(ray, scene, point, normal);
		computeIntensity(ray, scene, point, normal);
	}

	private void reflect(Ray ray, Scene scene, Vector point, Vector normal) {
		Ray r = ray.reflectedBy(point, normal);
		if (r != null) {
			r.setDepth(ray.getDepth() + 1);
			r.trace(scene);
			ray.setSecondaryRef(r);
		}
	}

	private void refract(Ray ray, Scene scene, Vector point, Vector normal) {
		RefractionIndex ri = refractionIndexAt(point);
		if (!ri.isTransparent())
			return;
		Ray t = ray.refractedBy(point, normal, ri.getBack() / ri.getFront());
		if (t != null) {
			t.setDepth(ray.getDepth() + 1);
			t.trace(scene);
			ray.setSecondaryTrans(t);
		}
	}

	private void computeIntensity(Ray ray, Scene scene, Vector point, Vector normal) {
		Intensity intensity = new Intensity();
		for (Source source : scene.getSources()) {
			intensity = intensity.join(source.intensityAt(ray.getDirection(), point, normal, illuminationIndexAt(point),
					scene.getSurfaces()));
		}
		ray.setIntensity(intensity.reduce(colorIndexAt(point)));
	}

	protected abstract Vector normalAt(@NotNull Vector point);

	protected abstract IlluminationIndex illuminationIndexAt(@NotNull Vector point);

	protected abstract ColorIndex colorIndexAt(@NotNull Vector point);

	protected abstract RefractionIndex refractionIndexAt(@NotNull Vector point);

	public static class IlluminationIndex implements Index {

		private float specular;
		private float diffuse;
		private float highlight;

		public IlluminationIndex(float specular, float diffuse, float highlight) {
			specular = Math.abs(specular);
			diffuse = Math.abs(diffuse);
			highlight = Math.abs(highlight);

			float z = specular + diffuse;
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
	public static class ColorIndex implements Index {

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

	public static class RefractionIndex implements Index {

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
