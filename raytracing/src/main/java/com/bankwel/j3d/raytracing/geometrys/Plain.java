package com.bankwel.j3d.raytracing.geometrys;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Vector;

public class Plain extends MonotoneSurface {

	private Vector point;
	private Vector normal;

	public Plain(@NotNull Vector point, @NotNull Vector normal) {
		this.point = point;
		this.normal = normal.normalize();
	}

	@Override
	public float intersection(Vector p0, Vector u) throws NoIntersectionException {
		float delta = u.dot(normal);
		if (delta == 0)
			throw new NoIntersectionException();
		float s = point.sub(p0).dot(normal) / delta;

		return s;
	}

	@Override
	public Vector normalAt(Vector point) {
		return normal;
	}

	@Override
	public RefractionIndex refractionIndexAt(Vector point) {
		return new RefractionIndex();
	}

}
