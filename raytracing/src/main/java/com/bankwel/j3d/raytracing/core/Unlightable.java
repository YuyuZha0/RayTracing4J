package com.bankwel.j3d.raytracing.core;

import com.bankwel.j3d.raytracing.core.Ray.Intensity;

public interface Unlightable extends Geometry {

	Vector normalAt(Vector point);

	boolean isTransparentAt(Vector point);

	float indexAt(Vector point);

	ReflectPolicy reflectPolicyAt(Vector point);

	Intensity intensityAt(Vector point);

	enum ReflectPolicy {

		DIFFUSE(1), MIRROR(2);
		public final int value;

		private ReflectPolicy(int value) {
			this.value = value;
		}

	}
}
