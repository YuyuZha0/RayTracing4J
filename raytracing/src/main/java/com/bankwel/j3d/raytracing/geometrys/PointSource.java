package com.bankwel.j3d.raytracing.geometrys;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.model.Intensity;
import com.bankwel.j3d.raytracing.model.Vector;
import com.bankwel.j3d.raytracing.model.core.Intersectable;
import com.bankwel.j3d.raytracing.model.core.Source;
import com.bankwel.j3d.raytracing.plugins.MathUtils;

public class PointSource implements Source {

	private Vector position;
	private Intensity intensity;

	public PointSource(@NotNull Vector position, @NotNull Intensity intensity) {
		this.position = position;
		this.intensity = intensity;
	}

	@Override
	public Equivalent sourceAs(Vector point, List<Intersectable> shelters) {
		Vector vector = position.sub(point);
		if (isInShadow(point, vector.normalize(), vector.length(), shelters))
			return new Equivalent();
		return new Equivalent(intensity.copy().decline(vector), position);
	}

	private boolean isInShadow(Vector p0, Vector u, float max, List<Intersectable> shelters) {
		for (Intersectable shelter : shelters) {
			float[] fs = shelter.allIntersections(p0, u);
			if (fs == null)
				continue;
			for (int i = 0; i < fs.length; i++)
				// 此处应该为0 但是由于浮点运算的精度限制因此会出现噪点,如果在阴影中，那么光源和交点之间的连线和其它物体相交
				if (MathUtils.greaterThanZero(fs[i]) && fs[i] < max) {
					return true;
				}
		}
		return false;
	}

}
