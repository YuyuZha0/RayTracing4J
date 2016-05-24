package com.bankwel.j3d.raytracing.geometrys;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.bankwel.j3d.raytracing.core.Vector;
import com.bankwel.j3d.raytracing.core.model.Intersectable;
import com.bankwel.j3d.raytracing.core.model.Source;
import com.bankwel.j3d.raytracing.core.model.Surface.IlluminationIndex;
import com.bankwel.j3d.raytracing.plugins.MathUtils;

public class PointSource implements Source {

	private Vector position;
	private Intensity intensity;

	public PointSource(@NotNull Vector position, @NotNull Intensity intensity) {
		this.position = position;
		this.intensity = intensity;
	}

	@Override
	public Intensity intensityAt(Vector u, Vector point, Vector normal, IlluminationIndex ii,
			List<Intersectable> shelters) {
		Vector L = position.sub(point);
		Vector l = L.normalize();
		if (isInShadow(point, l, L.length(), shelters))
			return new Intensity();
		float kd = ii.getDiffuse();
		float ks = ii.getSpecular();
		kd *= normal.dot(l);
		Vector h = l.sub(u).normalize();
		ks *= MathUtils.pow(normal.dot(h), ii.getHighlight());
		return intensity.multiply(kd + ks).decline(L);
	}

	private boolean isInShadow(Vector p0, Vector u, float max, List<Intersectable> shelters) {
		for (Intersectable shelter : shelters) {
			float[] fs = shelter.allIntersections(p0, u);
			if (fs == null)
				continue;
			for (int i = 0; i < fs.length; i++)
				// 此处应该为0 但是由于浮点运算的精度限制因此会出现噪点
				if (MathUtils.greaterThanZero(fs[i]) && fs[i] < max) {
					return true;
				}
		}
		return false;
	}

}
