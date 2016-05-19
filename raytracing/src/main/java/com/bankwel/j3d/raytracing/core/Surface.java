package com.bankwel.j3d.raytracing.core;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Ray.Intensity;

public interface Surface extends Geometry {

	Vector normalAt(@NotNull Vector point);

	boolean isTransparentAt(@NotNull Vector point);

	float mirrorIndexAt(@NotNull Vector point);

	float diffuseIndexAt(@NotNull Vector point);

	float refractionIndexAt(@NotNull Vector point);

	float smoothIndexAt(@NotNull Vector point);

	Intensity intensityAt(@NotNull Vector point);

}
