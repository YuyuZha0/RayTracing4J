package com.bankwel.j3d.raytracing.model.core;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.model.Intensity;
import com.bankwel.j3d.raytracing.model.Vector;

public interface Source extends Geometry {

	Equivalent sourceAs(@NotNull Vector point, @NotNull List<Intersectable> shelters);

	public static class Equivalent {

		private boolean sheltered;
		private Intensity intensity;
		private Vector center;

		public Equivalent() {
			sheltered = true;
		}

		public Equivalent(@NotNull Intensity intensity, @NotNull Vector center) {
			sheltered = false;
			this.intensity = intensity;
			this.center = center;
		}

		public boolean isSheltered() {
			return sheltered;
		}

		public void setSheltered(boolean sheltered) {
			this.sheltered = sheltered;
		}

		public Intensity getIntensity() {
			return intensity;
		}

		public void setIntensity(Intensity intensity) {
			this.intensity = intensity;
		}

		public Vector getCenter() {
			return center;
		}

		public void setCenter(Vector center) {
			this.center = center;
		}
	}

}
