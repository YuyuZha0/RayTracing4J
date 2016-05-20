package com.bankwel.j3d.raytracing.core;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.model.Source.Intensity;

public class Ray {

	private Vector origin;
	private Vector direction;
	private int depth = 0;

	private Ray secondaryRef = null;
	private Ray secondaryTrans = null;

	private Intensity intensity = new Intensity();

	public Ray(@NotNull Vector origin, @NotNull Vector direction) {
		this.origin = origin;
		this.direction = direction.normalize();
	}

	public Vector getOrigin() {
		return origin;
	}

	public void setOrigin(Vector origin) {
		this.origin = origin;
	}

	public Vector getDirection() {
		return direction;
	}

	public void setDirection(Vector direction) {
		this.direction = direction;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Intensity getIntensity() {
		return intensity;
	}

	public void setIntensity(Intensity intensity) {
		this.intensity = intensity;
	}

	public Ray getSecondaryRef() {
		return secondaryRef;
	}

	public void setSecondaryRef(Ray secondaryRef) {
		this.secondaryRef = secondaryRef;
	}

	public Ray getSecondaryTrans() {
		return secondaryTrans;
	}

	public void setSecondaryTrans(Ray secondaryTrans) {
		this.secondaryTrans = secondaryTrans;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append('[');
		buffer.append(origin);
		buffer.append(" + ?*");
		buffer.append(direction);
		buffer.append(":depth=");
		buffer.append(depth);
		buffer.append(']');
		return buffer.toString();
	}

	/**
	 * 
	 * @param i
	 *            The intersect point
	 * @param n
	 *            The normal at intersect point
	 */
	public Ray reflectedBy(@NotNull Vector i, @NotNull Vector n) {
		Vector u = direction;
		return new Ray(i, u.sub(n.mul(2 * u.dot(n))));
	}

	/**
	 * 
	 * @param i
	 *            The intersect point
	 * @param n
	 *            The normal at intersect point
	 * @param index
	 *            The relative refractive index of the material,index > 1
	 * @return
	 */
	public Ray refractedBy(@NotNull Vector i, @NotNull Vector n, float index) {
		Vector u = direction;
		double cosI = Math.abs(u.dot(n));
		double cosR = Math.sqrt((cosI * cosI - 1) / index / index + 1);
		return new Ray(i, u.mul(1 / index).sub(n.mul((float) (cosR - cosI / index))));
	}

	public Intensity countIntensity() {
		Intensity itn = intensity;
		if (secondaryRef != null)
			itn = itn.join(secondaryRef.countIntensity());
		if (secondaryTrans != null)
			itn = itn.join(secondaryTrans.countIntensity());
		return itn;
	}

	
}
