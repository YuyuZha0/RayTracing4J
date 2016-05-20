package com.bankwel.j3d.raytracing.core;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bankwel.j3d.raytracing.core.model.Geometry;
import com.bankwel.j3d.raytracing.core.model.VisibleSurface;
import com.bankwel.j3d.raytracing.core.model.Source;
import com.bankwel.j3d.raytracing.core.model.Surface;
import com.bankwel.j3d.raytracing.core.model.VisibleSurface.NoIntersectionException;
import com.bankwel.j3d.raytracing.core.model.Source.Intensity;
import com.bankwel.j3d.raytracing.core.model.Surface.RefractionIndex;

public class Scene {

	private List<Source> sources = new ArrayList<Source>();
	private List<VisibleSurface> surfaces = new ArrayList<VisibleSurface>();

	private Intensity ambient = new Intensity();

	private static final Logger logger = LoggerFactory.getLogger(Scene.class);

	public Scene add(@NotNull Geometry geometry) {
		if (geometry instanceof Source) {
			sources.add((Source) geometry);
			logger.info("Geometry recognized as Source.");
		}
		if (geometry instanceof VisibleSurface) {
			surfaces.add((VisibleSurface) geometry);
			logger.info("Geometry recognized as Surface.");
		}

		return this;
	}

	public Scene ambient(float red, float green, float blue) {
		this.ambient = new Intensity(red, green, blue);
		return this;
	}

	public void trace(@NotNull Ray ray) {
		int depth = ray.getDepth();
		if (depth > Config.MAX_DEPTH)
			return;
		Intersection intersection = closestIntersection(ray);
		if (intersection == null) {
			ray.setIntensity(ambient);
			return;
		}
		VisibleSurface geometry = intersection.getRelevant();
		if (geometry instanceof Source) {
			ray.setIntensity(ambient);
			return;
		} else if (geometry instanceof VisibleSurface) {
			VisibleSurface surface = (VisibleSurface) geometry;
			Vector point = intersection.getPosition();
			Vector normal = surface.normalAt(point);

			doReflection(ray, point, normal);
			doRefraction(ray, point, normal, surface);
			computeIntensity(ray, point, normal, surface);
		}
	}

	private Intersection closestIntersection(@NotNull Ray ray) {
		float distance = 0;
		VisibleSurface geom = null;
		for (VisibleSurface surface : surfaces) {
			try {
				float solution = surface.intersection(ray.getOrigin(), ray.getDirection());
				if (distance <= 0) {
					distance = solution;
					geom = surface;
				} else if (solution < distance) {
					distance = solution;
					geom = surface;
				}
			} catch (NoIntersectionException e) {
			}
		}

		if (geom == null)
			return null;
		return new Intersection(ray.getOrigin().plus(ray.getDirection().mul(distance)), geom);
	}

	private void doReflection(Ray ray, Vector point, Vector normal) {
		Ray r = ray.reflectedBy(point, normal);
		if (r != null) {
			r.setDepth(ray.getDepth() + 1);
			trace(r);
			ray.setSecondaryRef(r);
		}
	}

	private void doRefraction(Ray ray, Vector point, Vector normal, Surface surface) {
		RefractionIndex ri = surface.refractionIndexAt(point);
		if (!ri.isTransparent())
			return;
		Ray t = ray.refractedBy(point, normal, ri.getBack() / ri.getFront());
		if (t != null) {
			t.setDepth(ray.getDepth() + 1);
			trace(t);
			ray.setSecondaryTrans(t);
		}
	}

	private void computeIntensity(Ray ray, Vector point, Vector normal, Surface surface) {
		Intensity intensity = ambient;
		for (Source source : sources) {
			intensity = intensity.join(source.intensityAt(ray.getDirection(), point, normal, surface));
		}
		ray.setIntensity(intensity.reduce(surface.colorIndexAt(point)));
	}

	public static class Intersection {

		private Vector position;
		private VisibleSurface relevant;

		public Intersection(@NotNull Vector position, @NotNull VisibleSurface relevant) {
			this.position = position;
			this.relevant = relevant;
		}

		public Vector getPosition() {
			return position;
		}

		public void setPosition(Vector position) {
			this.position = position;
		}

		public VisibleSurface getRelevant() {
			return relevant;
		}

		public void setRelevant(VisibleSurface relevant) {
			this.relevant = relevant;
		}

	}

}
