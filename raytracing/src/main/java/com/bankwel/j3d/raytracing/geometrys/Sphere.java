package com.bankwel.j3d.raytracing.geometrys;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Vector;
import com.bankwel.j3d.raytracing.core.model.MonotoneSurface;
import com.bankwel.j3d.raytracing.plugins.MathUtils;

public class Sphere extends MonotoneSurface {

	private Vector center;
	private float radius;
	private RefractionIndex refractionIndex = new RefractionIndex();

	public Sphere(@NotNull Vector center, @NotNull float radius) {
		this.center = center;
		this.radius = radius;
	}

	@Override
	public float[] allIntersections(Vector p0, Vector u) {
		Vector p = center;
		Vector dp = p.sub(p0);
		float r = radius;
		float udp = u.dot(dp);
		float delta = udp * udp - dp.dot(dp) + r * r;
		if (delta < 0)
			return null;
		if (delta == 0)
			return new float[] { udp };
		float sqrt = MathUtils.sqrt(delta);
		return new float[] { udp - sqrt, udp + sqrt };
	}

	@Override
	protected Vector normalAt(Vector point) {
		return point.sub(center).normalize();
	}

	@Override
	protected RefractionIndex refractionIndexAt(Vector point) {
		return refractionIndex;
	}

}
