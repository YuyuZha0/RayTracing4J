package com.bankwel.j3d.raytracing.geometrys;

import com.bankwel.j3d.raytracing.core.Vector;
import com.bankwel.j3d.raytracing.core.model.VisibleSurface;

public abstract class MonotoneSurface implements VisibleSurface {

	private ColorIndex colorIndex = new ColorIndex();
	private IlluminationIndex illuminationIndex = new IlluminationIndex(1, 1, 1);

	public MonotoneSurface color(float red, float green, float blue) {
		colorIndex = new ColorIndex(red, green, blue);
		return this;
	}

	public MonotoneSurface illuminationIndex(float specular, float diffuse, float highlight) {
		return this;
	}

	@Override
	public IlluminationIndex illuminationIndexAt(Vector point) {
		return illuminationIndex;
	}

	@Override
	public ColorIndex colorIndexAt(Vector point) {
		return colorIndex;
	}
}
