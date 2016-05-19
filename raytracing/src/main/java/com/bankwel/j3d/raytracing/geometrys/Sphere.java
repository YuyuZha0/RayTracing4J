package com.bankwel.j3d.raytracing.geometrys;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Vector;

public class Sphere extends SimpleObject {

	private Vector center;
	private float radius;

	public Sphere(@NotNull Vector center, @NotNull float radius) {
		this.center = center;
		this.radius = radius;
	}

	@Override
	public float intersection(Vector p0, Vector u) throws NoSolutionException {
		Vector p = center;
		Vector dp = p.sub(p0);
		float r = radius;
		float udp = u.dot(dp);
		float delta = udp * udp - dp.dot(dp) + r * r;
		if (delta < 0) {
			throw new NoSolutionException();
		}
		float sqrt = (float) Math.sqrt(delta);
		float s = udp - sqrt;
		if (s <= 0) {
			s = udp + sqrt;
		}
		if (s <= 0)
			throw new NoSolutionException();
		return s;
	}

	@Override
	public Vector normalAt(Vector point) {
		return point.sub(center).normalize();
	}

}
