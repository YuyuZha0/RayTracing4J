package com.bankwel.j3d.raytracing.core;

import javax.validation.constraints.NotNull;

public interface Geometry {

	/**
	 * The solution of p = p0 + x*u(x>0)
	 * 
	 * @param ray
	 * @return
	 */
	float intersection(@NotNull Vector p0, @NotNull Vector u) throws NoSolutionException;

	static class NoSolutionException extends Exception {

		private static final long serialVersionUID = -5379972296551058289L;

		public NoSolutionException() {
			super();
		}

		public NoSolutionException(String msg) {
			super(msg);
		}
	}

}
