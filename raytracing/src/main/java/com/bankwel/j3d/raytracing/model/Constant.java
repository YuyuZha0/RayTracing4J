package com.bankwel.j3d.raytracing.model;

public class Constant {

	public static final int MAX_DEPTH = 3;

	public static final float ZERO_APPROX = 1e-1f;

	public static final Vector RAY_DECLINE_FACTOR = new Vector(4e-7f, 2e-4f, 1e-1f);

	public static void main(String[] args) {
		float d = 400;
		System.out.println(d * d * RAY_DECLINE_FACTOR.x);
		System.out.println(d * RAY_DECLINE_FACTOR.y);
		System.out.println(RAY_DECLINE_FACTOR.z);
	}

}
