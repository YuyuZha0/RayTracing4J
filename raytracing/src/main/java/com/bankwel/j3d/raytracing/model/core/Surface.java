package com.bankwel.j3d.raytracing.model.core;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.model.Intensity;
import com.bankwel.j3d.raytracing.model.Ray;
import com.bankwel.j3d.raytracing.model.Scene;
import com.bankwel.j3d.raytracing.model.Vector;
import com.bankwel.j3d.raytracing.model.core.Source.Equivalent;
import com.bankwel.j3d.raytracing.plugins.IlluminationModel;
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
		IntensityRate rate = reflRateAt(point);
		Intensity direct = directIntensity(scene, ray, point, normal);
		Intensity reflect = reflectionIntensity(ray, scene, point, normal);
		Intensity refract = refractIntensity(ray, scene, point, normal);
		Intensity intensity = new Intensity();
		intensity.join(scene.getAmbient()).join(direct).join(reflect).join(refract).reduce(rate)
				.join(refract.reduce(rate.complement()));
		ray.setIntensity(intensity);
	}

	private Intensity reflectionIntensity(Ray ray, Scene scene, Vector point, Vector normal) {
		Ray refl = ray.reflectedBy(point, normal);
		if (refl == null)
			return new Intensity();
		refl.setDepth(ray.getDepth() + 1);
		return refl.trace(scene);
	}

	private Intensity refractIntensity(Ray ray, Scene scene, Vector point, Vector normal) {
		float index = refrIndexAt(point, ray.getDirection().dot(normal) < 0);
		Intensity intensity = new Intensity();
		if (index <= 0)
			return intensity;
		Ray trans = ray.refractedBy(point, normal, index);
		if (trans == null)
			return intensity;
		trans.setDepth(ray.getDepth() + 1);
		return intensity
				.join(illuminationIntensity(trans.trace(scene), trans.getOrigin(), point, normal, ray.getDirection()));
	}

	private Intensity directIntensity(Scene scene, Ray ray, Vector point, Vector normal) {
		Intensity intensity = new Intensity();
		for (Source source : scene.getSources()) {
			Equivalent equivalent = source.sourceAs(point, scene.getSurfaces());
			if (!equivalent.isSheltered())
				intensity.join(illuminationIntensity(equivalent.getIntensity(), equivalent.getCenter(), point, normal,
						ray.getDirection()));
		}
		return intensity;
	}

	private Intensity illuminationIntensity(Intensity base, Vector center, Vector point, Vector normal, Vector in) {
		IlluminationIndex index = illuminationIndexAt(point);
		float ks = index.getSpecular();
		float kd = index.getDiffuse();
		kd *= IlluminationModel.lambert(normal, in);
		return base.reduce(kd + ks);
	}

	protected abstract Vector normalAt(@NotNull Vector point);

	protected abstract IlluminationIndex illuminationIndexAt(@NotNull Vector point);

	protected abstract IntensityRate reflRateAt(@NotNull Vector point);

	protected abstract float refrIndexAt(@NotNull Vector point, boolean outside);

	public static interface SurfaceIndex {
	};

	public static class IlluminationIndex implements SurfaceIndex {

		private float specular;
		private float diffuse;
		private float highlight;

		public IlluminationIndex(float specular, float diffuse, float highlight) {
			specular = MathUtils.abs(specular);
			diffuse = MathUtils.abs(diffuse);
			highlight = MathUtils.abs(highlight);

			this.specular = specular;
			this.diffuse = diffuse;
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
			return comp;
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
