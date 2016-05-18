package com.bankwel.j3d.raytracing.core;

public interface Geometry {

	/**
	 * If solution exists,solution > 0
	 * 
	 * @param ray
	 * @return
	 */
	float soluteRayEquation(Ray ray);
}
