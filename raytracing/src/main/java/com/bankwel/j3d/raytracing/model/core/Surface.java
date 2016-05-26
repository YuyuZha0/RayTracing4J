package com.bankwel.j3d.raytracing.model.core;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.model.Ray;
import com.bankwel.j3d.raytracing.model.Scene;
import com.bankwel.j3d.raytracing.model.Vector;
import com.bankwel.j3d.raytracing.model.core.Source.Intensity;
import com.bankwel.j3d.raytracing.plugins.MathUtils;

/**
 * 
 * @author yuyuzhao
 *
 */
public abstract class Surface implements Intersectable {

	@Override
	public final void onIntersecting(Ray ray, Scene scene, Vector point) {
		Vector normal = normalAt(point);
		computeIntensity(ray, scene, point, normal);
		reflect(ray, scene, point, normal);
		refract(ray, scene, point, normal);
	}

	private void computeIntensity(Ray ray, Scene scene, Vector point, Vector normal) {
		Intensity intensity = ray.getIntensity();
		intensity.join(scene.getAmbient());
		for (Source source : scene.getSources()) {
			intensity.join(source.intensityAt(ray.getDirection(), point, normal, illuminationIndexAt(point),
					scene.getSurfaces()));
		}
		intensity.reduce(reflRateAt(point));
	}

	private void reflect(Ray ray, Scene scene, Vector point, Vector normal) {
		Ray r = ray.reflectedBy(point, normal);
		if (r != null) {
			r.setDepth(ray.getDepth() + 1);
			r.trace(scene);
			r.getIntensity().reduce(reflRateAt(point));
			ray.setSecondaryRefl(r);
		}
	}

	private void refract(Ray ray, Scene scene, Vector point, Vector normal) {
		float index = refrIndexAt(point, ray.getDirection().dot(normal) < 0);
		if (index <= 0)
			return;
		Ray t = ray.refractedBy(point, normal, index);
		if (t != null) {
			t.setDepth(ray.getDepth() + 1);
			t.trace(scene);
			t.getIntensity().reduce(reflRateAt(point).complement());
			ray.setSecondaryTrans(t);
		}
	}

	protected abstract Vector normalAt(@NotNull Vector point);

	protected abstract IllumIndex illuminationIndexAt(@NotNull Vector point);

	protected abstract IntensityRate reflRateAt(@NotNull Vector point);

	protected abstract float refrIndexAt(@NotNull Vector point, boolean outside);

	public static interface SurfaceIndex {
	};

	public static class IllumIndex implements SurfaceIndex {

		private float specular;
		private float diffuse;
		private float highlight;

		public IllumIndex(float specular, float diffuse, float highlight) {
			specular = MathUtils.abs(specular);
			diffuse = MathUtils.abs(diffuse);
			highlight = MathUtils.abs(highlight);

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
	public static class IntensityRate implements SurfaceIndex {

		private float red;
		private float green;
		private float blue;

		public IntensityRate() {
			red = 1;
			green = 1;
			blue = 1;
		}

		public IntensityRate(float red, float green, float blue) {
			red = Math.abs(red);
			green = Math.abs(green);
			blue = Math.abs(blue);
			float max = MathUtils.max(red, green, blue, 1);
			this.red = red / max;
			this.green = green / max;
			this.blue = blue / max;
		}

		public IntensityRate complement() {
			IntensityRate comp = new IntensityRate();
			comp.setRed(1 - red);
			comp.setGreen(1 - green);
			comp.setBlue(1 - blue);
			return this;
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

}
