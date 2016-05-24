package com.bankwel.j3d.raytracing.core.model;

import com.bankwel.j3d.raytracing.core.Vector;

public abstract class MonotoneSurface extends Surface {

	private ColorIndex colorIndex = new ColorIndex();
	private IlluminationIndex illuminationIndex = new IlluminationIndex(1, 1, 1);

	public MonotoneSurface color(float red, float green, float blue) {
		colorIndex = new ColorIndex(red, green, blue);
		return this;
	}

	public MonotoneSurface illumination(float specular, float diffuse, float highlight) {
		this.illuminationIndex = new IlluminationIndex(specular, diffuse, highlight);
		return this;
	}

	@Override
	protected IlluminationIndex illuminationIndexAt(Vector point) {
		return illuminationIndex;
	}

	@Override
	protected ColorIndex colorIndexAt(Vector point) {
		return colorIndex;
	}

}
