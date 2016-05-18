package com.bankwel.j3d.raytracing.core;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bankwel.j3d.raytracing.core.Ray.Intensity;

public class Scene {

	private List<Lightable> sources = new ArrayList<Lightable>();
	private List<Unlightable> objects = new ArrayList<Unlightable>();

	private Intensity backgroud = new Intensity();

	private static final Logger logger = LoggerFactory.getLogger(Scene.class);

	public Scene addGeom(@NotNull Geometry geometry) {
		if (geometry instanceof Lightable) {
			sources.add((Lightable) geometry);
			logger.info("Geometry recognized as Lightable has been added to the scene.");
		} else if (geometry instanceof Unlightable) {
			objects.add((Unlightable) geometry);
			logger.info("Geometry recognized as Unlightable has been added to the scene.");
		} else {
			logger.info("Geometry does not seem to have a specified type with Lightable or Unlightable");
		}

		return this;
	}

	public Scene backgroud(@NotNull Intensity backgroud) {
		this.backgroud = backgroud;
		return this;
	}

	public void trace(@NotNull Ray ray) {
		int depth = ray.getDepth();
		if (depth > Config.MAX_DEPTH)
			return;

		float min = 0;
		Geometry geom = null;

		for (Geometry source : sources) {
			float solution = -source.soluteRayEquation(ray);
			if (solution < 0 && solution > min) {
				min = solution;
				geom = source;
			}
		}

		for (Geometry object : objects) {
			float solution = -object.soluteRayEquation(ray);
			if (solution < 0 && solution > min) {
				min = solution;
				geom = object;
			}
		}
		min = -min;
		if (geom == null) {
			ray.setIntensity(backgroud);
			return;
		}
		if (geom instanceof Lightable) {
			Lightable lightable = (Lightable) geom;
			ray.setIntensity(lightable.directIntensity());
			return;
		}
		if (!(geom instanceof Unlightable)) {
			return;
		}
		Unlightable unlightable = (Unlightable) geom;
		Vector u = ray.getDirection();
		Vector p0 = ray.getOrigin();
		Vector i = p0.plus(u.mul(min));
		Vector n = unlightable.normalAt(i);
		
		Ray r = ray.reflectedBy(i, n);
		
	}

}
