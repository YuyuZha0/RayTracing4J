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
		return new Ray(i, u.sub(n.times(2 * u.dot(n))));
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
		return new Ray(i, u.times(1 / index).sub(n.times((float) (cosR - cosI / index))));
	}

	public Intensity totalIntensity() {
		Intensity itn = intensity;
		if (secondaryRef != null)
			itn = itn.combine(secondaryRef.totalIntensity());
		if (secondaryTrans != null)
			itn = itn.combine(secondaryTrans.totalIntensity());
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
			this.ir = ir;
			this.ig = ig;
			this.ib = ib;
		}

		public float getIr() {
			return ir;
		}

		public void setIr(float ir) {
			this.ir = ir;
		}

		public float getIg() {
			return ig;
		}

		public void setIg(float ig) {
			this.ig = ig;
		}

		public float getIb() {
			return ib;
		}

		public void setIb(float ib) {
			this.ib = ib;
		}

		public Intensity combine(Intensity i) {
			return new Intensity(ir + i.getIr(), ig + i.getIg(), ib + i.getIb());
		}

		public Intensity attenuate(float a) {
			return new Intensity(a * ir, a * ig, a * ib);
		}

		public Intensity attenuate(float a, float b, float c) {
			return new Intensity(a * ir, b * ig, c * ib);
		}

		public Color toColor() {
			float i = (float) Math.sqrt(ir * ir + ig * ig + ib * ib);
			return new Color(ir / i, ig / i, ib / i);
		}

	}
}
