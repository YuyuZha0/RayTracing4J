package com.bankwel.j3d.raytracing.geometrys;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Ray;
import com.bankwel.j3d.raytracing.core.Ray.Intensity;
import com.bankwel.j3d.raytracing.core.Unlightable;
import com.bankwel.j3d.raytracing.core.Vector;

public class Sphere implements Unlightable {

	private Vector center;
	private float radius;
	private float index = 1;

	public Sphere(@NotNull Vector center, float radius) {
		this.center = center;
		this.radius = radius;
	}

	public Vector getCenter() {
		return center;
	}

	public void setCenter(Vector center) {
		this.center = center;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getIndex() {
		return index;
	}

	public void setIndex(float index) {
		this.index = index;
	}

	@Override
	public float intersection(Ray ray) throws NoSolutionException {
		Vector u = ray.getDirection();
		Vector p0 = ray.getOrigin();
		Vector p = center;
		Vector dp = p.sub(p0);
		float r = radius;
		float udp = u.dot(dp);
		float delta = udp * udp - dp.dot(dp) + r * r;
		if (delta < 0)
			throw new NoSolutionException();
		float sqrt = (float) Math.sqrt(delta);
		float s = udp - sqrt;
		if (s <= 0) {
			s = udp + sqrt;
		}
		if (s <= 0)
			throw new NoSolutionException();
		return s;
	}

	@Override
	public Vector normalAt(Vector point) {
		return point.sub(center).normalize();
	}

	@Override
	public boolean isTransparentAt(Vector point) {
		return false;
	}

	@Override
	public float indexAt(Vector point) {
		return index;
	}

	@Override
	public ReflectPolicy reflectPolicyAt(Vector point) {
		return ReflectPolicy.DIFFUSE;
	}

	@Override
	public Intensity intensityAt(Vector point) {
		return new Intensity(1, 1, 1);
	}

}
