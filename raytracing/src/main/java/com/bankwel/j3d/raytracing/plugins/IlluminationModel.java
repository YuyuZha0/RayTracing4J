package com.bankwel.j3d.raytracing.plugins;

import com.bankwel.j3d.raytracing.model.Vector;

public class IlluminationModel {

	public static float lambert(Vector n, Vector u) {
		return Math.abs(n.dot(u));
	}

	public static float phong(Vector n, Vector u, Vector l, float ns) {
		return (float) Math.pow(Math.abs(l.sub(u).normalize().dot(n)), ns);
	}
}
