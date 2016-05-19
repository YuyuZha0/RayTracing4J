package com.bankwel.j3d.raytracing.geometrys;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Lightable;
import com.bankwel.j3d.raytracing.core.Ray.Intensity;
import com.bankwel.j3d.raytracing.core.Surface;
import com.bankwel.j3d.raytracing.core.Vector;

public class DotLight implements Lightable {

	private Vector position;
	private Intensity intensity;

	public DotLight(@NotNull Vector position, @NotNull Intensity intensity) {
		this.position = position;
		this.intensity = intensity;
	}

	@Override
	public float intersection(Vector p0, Vector u) throws NoSolutionException {
		throw new NoSolutionException();
	}

	@Override
	public Intensity reflecIntensity(Vector u, Vector point, Vector normal, Surface surface) {
		Vector L = position.sub(point);
		Vector l = L.normalize();
		try {
			surface.intersection(point, l);
		} catch (NoSolutionException e) {
			float kd = normal.dot(l) * surface.diffuseIndexAt(point);
			Vector h = l.sub(u).normalize();
			float km = (float) Math.pow(normal.dot(h), surface.smoothIndexAt(point)) * surface.mirrorIndexAt(point);
			return intensity.mul(kd + km);
		}
		return null;
	}

	@Override
	public Intensity directIntensity() {
		return null;
	}

}
