package com.bankwel.j3d.raytracing.core;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bankwel.j3d.raytracing.core.model.Geometry;
import com.bankwel.j3d.raytracing.core.model.Intersectable;
import com.bankwel.j3d.raytracing.core.model.Source;
import com.bankwel.j3d.raytracing.plugins.MathUtils;

public class Scene {

	private List<Source> sources = new ArrayList<Source>();
	private List<Intersectable> surfaces = new ArrayList<Intersectable>();

	private static final Logger logger = LoggerFactory.getLogger(Scene.class);

	public Scene add(@NotNull Geometry geometry) {
		if (geometry instanceof Source) {
			sources.add((Source) geometry);
			logger.info("Geometry recognized as Source.");
		}
		if (geometry instanceof Intersectable) {
			surfaces.add((Intersectable) geometry);
			logger.info("Geometry recognized as Intersectable.");
		}
		return this;
	}

	public Intersection closestIntersection(@NotNull Vector origin, @NotNull Vector direction) {
		float distance = 0;
		Intersectable intersectable = null;
		for (Intersectable surface : surfaces) {
			float[] solutions = surface.allIntersections(origin, direction);
			if (solutions == null)
				continue;
			float first = firstSolution(solutions);
			if (first == 0)
				continue;
			if (distance == 0) {
				distance = first;
				intersectable = surface;
			} else if (first < distance) {
				distance = first;
				intersectable = surface;
			}
		}
		if (intersectable == null)
			return null;
		return new Intersection(origin.plus(direction.mul(distance)), intersectable);
	}

	private float firstSolution(float[] solutions) {
		float f = solutions[0];
		for (int i = 1; i < solutions.length; i++)
			if (solutions[i] < f && MathUtils.greaterThanZero(solutions[i]))
				f = solutions[i];
		if (MathUtils.equalsZero(f))
			return 0;
		return f;

	}

	public static class Intersection {

		private Vector position;
		private Intersectable relevant;

		public Intersection(@NotNull Vector position, @NotNull Intersectable relevant) {
			this.position = position;
			this.relevant = relevant;
		}

		public Vector getPosition() {
			return position;
		}

		public void setPosition(Vector position) {
			this.position = position;
		}

		public Intersectable getRelevant() {
			return relevant;
		}

		public void setRelevant(Intersectable relevant) {
			this.relevant = relevant;
		}
	}

	public List<Source> getSources() {
		return sources;
	}

	public void setSources(List<Source> sources) {
		this.sources = sources;
	}

	public List<Intersectable> getSurfaces() {
		return surfaces;
	}

	public void setSurfaces(List<Intersectable> surfaces) {
		this.surfaces = surfaces;
	}
}
