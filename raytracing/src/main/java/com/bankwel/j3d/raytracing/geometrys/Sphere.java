package com.bankwel.j3d.raytracing.geometrys;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Geometry;
import com.bankwel.j3d.raytracing.core.Ray;
import com.bankwel.j3d.raytracing.core.Ray.Intensity;
import com.bankwel.j3d.raytracing.core.Vector;

public class Sphere implements Geometry {

	private Vector center;
	private float radius;
	private float index = 1;

	private Vector I = null;
	private Vector N = null;

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
	public boolean intersectWith(Ray ray) {
		Vector u = ray.getDirection();
		Vector p0 = ray.getOrigin();
		Vector p = center;
		Vector dp = p.sub(p0);
		float r = radius;
		float udp = u.dot(dp);
		float delta = udp * udp - dp.dot(dp) + r * r;
		if (delta < 0)
			return false;
		float s = udp - (float) Math.sqrt(delta);
		if (s < 0)
			return false;
		I = p0.plus(u.times(s));
		N = I.sub(p).normalize();
		return true;
	}

	@Override
	public Ray reflect(Ray ray) {
		if (N == null)
			return null;
		ray.setIntensity(new Intensity(1,1,1));
		return ray.reflectedBy(I, N);
	}

	@Override
	public Ray refract(Ray ray) {
		if (N == null)
			return null;
		return ray.refractedBy(I, N, index);
	}

	public static void main(String[] args) {
		Geometry geometry = new Sphere(new Vector(2, 0, 0), 3);
		Ray ray = new Ray(new Vector(-2, 1, 0), new Vector(1, 0, 0));
		System.out.println(geometry.intersectWith(ray));
		System.out.println(geometry.reflect(ray));
		System.out.println(geometry.refract(ray));
	}

}
