package com.bankwel.j3d.raytracing.core.model;

import com.bankwel.j3d.raytracing.core.Vector;

public abstract class MonotoneSurface extends Surface {

	private IntensityRate intensityIndex = new IntensityRate();
	private IllumIndex illuminationIndex = new IllumIndex(1, 1, 1);
	private float refractIndex = 0;

	public MonotoneSurface reflection(float red, float green, float blue) {
		intensityIndex = new IntensityRate(red, green, blue);
		return this;
	}

	public MonotoneSurface illumination(float specular, float diffuse, float highlight) {
		this.illuminationIndex = new IllumIndex(specular, diffuse, highlight);
		return this;
	}

	public MonotoneSurface refraction(float index) {
		this.refractIndex = index;
		return this;
	}

	@Override
	protected IllumIndex illuminationIndexAt(Vector point) {
		return illuminationIndex;
	}

	@Override
	protected IntensityRate reflRateAt(Vector point) {
		return intensityIndex;
	}

	@Override
	protected float refrIndexAt(Vector point, boolean outside) {
		if (refractIndex <= 0)
			return 0;
		return outside ? refractIndex : 1 / refractIndex;
	}

}
