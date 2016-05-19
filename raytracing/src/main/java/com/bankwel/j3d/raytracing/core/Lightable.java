package com.bankwel.j3d.raytracing.core;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Ray.Intensity;

public interface Lightable extends Geometry {

	Intensity reflecIntensity(@NotNull Vector u, @NotNull Vector point, @NotNull Vector normal,
			@NotNull Surface surface);

	Intensity directIntensity();
}
