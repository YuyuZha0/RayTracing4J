package com.bankwel.j3d.raytracing.model;

import com.bankwel.j3d.raytracing.plugins.MathUtils;

/**
 * 
 * @author yuyuzhao
 * @since 2016年4月25日
 *
 */
public class Vector {

	public float x;
	public float y;
	public float z;

	public Vector() {
		x = 0;
		y = 0;
		z = 0;
	}

	public Vector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector rev() {
		return new Vector(-x, -y, -z);
	}

	public Vector plus(Vector v) {
		return new Vector(this.x + v.x, this.y + v.y, this.z + v.z);
	}

	public Vector sub(Vector v) {
		return new Vector(this.x - v.x, this.y - v.y, this.z - v.z);
	}

	public float dot(Vector v) {
		return x * v.x + y * v.y + z * v.z;
	}

	public Vector mul(float f) {
		return new Vector(this.x * f, this.y * f, this.z * f);
	}

	public Vector cross(Vector v) {
		return new Vector(this.y * v.z - this.z * v.y, this.z * v.x - this.x * v.z, this.y * v.z - this.z * v.y);
	}

	public float length() {
		return MathUtils.sqrt(x * x + y * y + z * z);
	}

	public Vector normalize() {
		float n = length();
		return new Vector(x / n, y / n, z / n);
	}

	@Override
	public String toString() {
		return new StringBuffer().append('<').append(x).append(',').append(y).append(',').append(z).append('>')
				.toString();
	}
}
