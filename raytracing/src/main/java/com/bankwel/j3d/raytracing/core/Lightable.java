package com.bankwel.j3d.raytracing.core;

import com.bankwel.j3d.raytracing.core.Ray.Intensity;
import com.bankwel.j3d.raytracing.core.Unlightable.ReflectPolicy;

public interface Lightable extends Geometry {

	Intensity reflecIntensity(Vector u, Vector point, Vector normal, ReflectPolicy policy);

	Intensity directIntensity();
}
