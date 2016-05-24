package com.bankwel.j3d.raytracing.geometrys;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Vector;
import com.bankwel.j3d.raytracing.core.model.Intersectable;
import com.bankwel.j3d.raytracing.core.model.Source;
import com.bankwel.j3d.raytracing.core.model.Surface.IlluminationIndex;

public class AmbientSource implements Source {

	private Intensity intensity;

	public AmbientSource(@NotNull Intensity intensity) {
		this.intensity = intensity;
	}

	@Override
	public Intensity intensityAt(Vector u, Vector point, Vector normal, IlluminationIndex illuminationIndex,
			List<Intersectable> shelters) {
		return intensity;
	}

}
