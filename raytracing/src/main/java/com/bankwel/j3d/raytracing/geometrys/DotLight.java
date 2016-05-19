package com.bankwel.j3d.raytracing.geometrys;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Lightable;
import com.bankwel.j3d.raytracing.core.Ray;
import com.bankwel.j3d.raytracing.core.Ray.Intensity;
import com.bankwel.j3d.raytracing.core.Unlightable.ReflectPolicy;
import com.bankwel.j3d.raytracing.core.Vector;

public class DotLight implements Lightable {

	private Vector position;
	private Intensity intensity;

	public DotLight(@NotNull Vector position, @NotNull Intensity intensity) {
		this.position = position;
		this.intensity = intensity;
	}

	@Override
	public float intersection(Ray ray) throws NoSolutionException {
		throw new NoSolutionException();
	}

	@Override
	public Intensity reflecIntensity(Vector u, Vector point, Vector normal, ReflectPolicy policy) {
		Vector l = position.sub(point).normalize();
		float k = 1;
		if (policy.value == ReflectPolicy.DIFFUSE.value) {
			k = normal.dot(l);
		} else if (policy.value == ReflectPolicy.MIRROR.value) {
			Vector h = l.sub(u).normalize();
			k = normal.dot(h);
		}
		return intensity.mul(k);
	}

	@Override
	public Intensity directIntensity() {
		return null;
	}

}
