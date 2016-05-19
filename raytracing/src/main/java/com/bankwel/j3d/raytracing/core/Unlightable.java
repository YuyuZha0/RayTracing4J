package com.bankwel.j3d.raytracing.core;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Ray.Intensity;

public interface Unlightable extends Geometry {

	Vector normalAt(@NotNull Vector point);

	boolean isTransparentAt(@NotNull Vector point);

	float indexAt(@NotNull Vector point);

	ReflectPolicy reflectPolicyAt(@NotNull Vector point);

	Intensity intensityAt(@NotNull Vector point);

	enum ReflectPolicy {

		DIFFUSE(1), MIRROR(2);
		public final int value;

		private ReflectPolicy(int value) {
			this.value = value;
		}

	}
}
