package com.bankwel.j3d.raytracing.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bankwel.j3d.raytracing.core.Ray.Intensity;

public class Scene {

	private List<Geometry> geometrys = new ArrayList<Geometry>();

	private static final Logger logger = LoggerFactory.getLogger(Scene.class);

	public Scene addGeom(@NotNull Geometry geometry) {
		geometrys.add(geometry);
		logger.info("Geometry has been added to the scene.");
		return this;
	}

	public Scene addGemo(@NotNull Collection<Geometry> c) {
		geometrys.addAll(c);
		logger.info("Geometries has been added to the scene.");
		return this;
	}

	public void trace(@NotNull Ray ray) {
		Geometry geometry = getNearestGeometry(ray);
		int depth = ray.getDepth();
		if (geometry == null || depth > Config.MAX_DEPTH) {
			stopTracing(ray);
			return;
		}
		Ray r = geometry.reflect(ray);
		if (r != null) {
			r.setDepth(depth + 1);
			trace(r);
		}
		Ray t = geometry.refract(ray);
		if (t != null) {
			t.setDepth(depth + 1);
			trace(t);
		}
		ray.setSecondaryRef(r);
		ray.setSecondaryTrans(t);
		// logger.info("[{}] has been successfully traced.", ray);
	}

	private Geometry getNearestGeometry(Ray ray) {
		Geometry geometry = geometrys.get(0);
		if (geometry.intersectWith(ray))
			return geometry;
		return null;
	}

	private void stopTracing(@NotNull Ray ray) {
		ray.setIntensity(new Intensity(0, 0, 0));
	}

}
