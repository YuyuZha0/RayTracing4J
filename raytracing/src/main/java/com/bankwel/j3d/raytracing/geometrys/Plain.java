package com.bankwel.j3d.raytracing.geometrys;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.model.Vector;
import com.bankwel.j3d.raytracing.model.core.MonotoneSurface;

public class Plain extends MonotoneSurface {

	private Vector point;
	private Vector normal;

	public Plain(@NotNull Vector point, @NotNull Vector normal) {
		this.point = point;
		this.normal = normal.normalize();
	}

	@Override
	public float[] allIntersections(Vector p0, Vector u) {
		float delta = u.dot(normal);
		// 当光线方向和平面法线方向向量夹角小于等于90°的时候没有交点
		if (delta > -1e-4)
			return null;
		float s = point.sub(p0).dot(normal) / delta;
		return new float[] { s };
	}

	@Override
	public Vector normalAt(Vector point) {
		return normal;
	}

}
