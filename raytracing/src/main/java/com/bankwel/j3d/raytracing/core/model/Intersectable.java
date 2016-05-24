package com.bankwel.j3d.raytracing.core.model;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Ray;
import com.bankwel.j3d.raytracing.core.Scene;
import com.bankwel.j3d.raytracing.core.Vector;

public interface Intersectable extends Geometry {

	/**
	 * The solution of p = p0 + x*u
	 * 
	 * @param ray
	 * @return
	 */
	float[] allIntersections(@NotNull Vector p0, @NotNull Vector u);

	void onIntersecting(@NotNull Ray ray, @NotNull Scene scene, @NotNull Vector intersection);
}
