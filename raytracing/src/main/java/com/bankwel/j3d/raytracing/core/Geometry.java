package com.bankwel.j3d.raytracing.core;

import javax.validation.constraints.NotNull;

public interface Geometry {

	boolean intersectWith(@NotNull Ray ray);

	Ray reflect(@NotNull Ray ray);

	Ray refract(@NotNull Ray ray);
}
