package com.bankwel.j3d.raytracing.core;

import java.awt.Color;

import javax.validation.constraints.NotNull;

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

	public Intensity totalIntensity() {
		Intensity itn = intensity;
		if (secondaryRef != null)
			itn = itn.plus(secondaryRef.totalIntensity());
		if (secondaryTrans != null)
			itn = itn.plus(secondaryTrans.totalIntensity());
		return itn;
	}

	public static class Intensity {

		private float ir;
		private float ig;
		private float ib;

		public Intensity() {
			ir = 0;
			ig = 0;
			ib = 0;
		}

		public Intensity(float ir, float ig, float ib) {
			ir = Math.abs(ir);
			ig = Math.abs(ig);
			ib = Math.abs(ib);
			float max = Math.max(ir, Math.max(ig, ib));
			if (max == 0)
				max = 1;
			this.ir = ir / max;
			this.ig = ig / max;
			this.ib = ib / max;
		}

		public float getIr() {
			return ir;
		}

		public float getIg() {
			return ig;
		}

		public float getIb() {
			return ib;
		}

		public Intensity plus(Intensity i) {
			return new Intensity(ir + i.getIr(), ig + i.getIg(), ib + i.getIb());
		}

		public Intensity mul(Intensity i) {
			return new Intensity(ir * i.getIr(), ig * i.getIg(), ib * i.getIb());
		}

		public Color toColor() {
			return new Color(ir, ig, ib);
		}

	}
}
