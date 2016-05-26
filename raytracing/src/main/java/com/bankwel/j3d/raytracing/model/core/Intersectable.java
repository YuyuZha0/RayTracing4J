package com.bankwel.j3d.raytracing.model.core;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.model.Ray;
import com.bankwel.j3d.raytracing.model.Scene;
import com.bankwel.j3d.raytracing.model.Vector;

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
