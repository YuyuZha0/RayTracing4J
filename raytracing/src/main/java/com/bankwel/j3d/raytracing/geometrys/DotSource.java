package com.bankwel.j3d.raytracing.geometrys;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Vector;
import com.bankwel.j3d.raytracing.core.model.Source;
import com.bankwel.j3d.raytracing.core.model.Surface;
import com.bankwel.j3d.raytracing.core.model.Surface.IlluminationIndex;

public class DotSource implements Source {

	private Vector position;
	private Intensity intensity;

	public DotSource(@NotNull Vector position, @NotNull Intensity intensity) {
		this.position = position;
		this.intensity = intensity;
	}

	@Override
	public Intensity intensityAt(Vector u, Vector point, Vector normal, Surface surface) {
		Vector L = position.sub(point);
		Vector l = L.normalize();
		IlluminationIndex ii = surface.illuminationIndexAt(point);
		float kd = ii.getDiffuse();
		float ks = ii.getSpecular();
		kd *= normal.dot(l);
		Vector h = l.sub(u).normalize();
		ks *= (float) Math.pow(normal.dot(h), ii.getHighlight());
		return intensity.multiply(kd + ks);
	}

}
