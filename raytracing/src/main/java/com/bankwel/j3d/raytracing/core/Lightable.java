package com.bankwel.j3d.raytracing.core;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Ray.Intensity;
import com.bankwel.j3d.raytracing.core.Unlightable.ReflectPolicy;

public interface Lightable extends Geometry {

	Intensity reflecIntensity(@NotNull Vector u, @NotNull Vector point, @NotNull Vector normal,
			@NotNull ReflectPolicy policy);

	Intensity directIntensity();
}
