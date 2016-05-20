package com.bankwel.j3d.raytracing.core.model;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Vector;

public interface VisibleSurface extends Surface {

	/**
	 * The solution of p = p0 + x*u(x>0)
	 * 
	 * @param ray
	 * @return
	 */
	float intersection(@NotNull Vector p0, @NotNull Vector u) throws NoIntersectionException;

	Vector normalAt(@NotNull Vector point);

	static class NoIntersectionException extends Exception {

		private static final long serialVersionUID = -5379972296551058289L;

		public NoIntersectionException() {
			super();
		}

		public NoIntersectionException(String msg) {
			super(msg);
		}
	}

}
