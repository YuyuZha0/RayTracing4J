package com.bankwel.j3d.raytracing.core;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bankwel.j3d.raytracing.core.Geometry.NoSolutionException;
import com.bankwel.j3d.raytracing.core.Ray.Intensity;

public class Scene {

	private List<Lightable> sources = new ArrayList<Lightable>();
	private List<Surface> objects = new ArrayList<Surface>();

	private Intensity backgroud = new Intensity();

	private static final Logger logger = LoggerFactory.getLogger(Scene.class);

	public Scene add(@NotNull Geometry geometry) {
		if (geometry instanceof Lightable) {
			sources.add((Lightable) geometry);
			logger.info("Geometry recognized as Lightable has been added to the scene.");
		} else if (geometry instanceof Surface) {
			objects.add((Surface) geometry);
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
		Intersection intersection = nearestIntersection(ray);
		if (intersection == null) {
			ray.setIntensity(backgroud);
			return;
		}
		Geometry geometry = intersection.getRelevant();
		if (geometry instanceof Lightable) {
			Lightable lightable = (Lightable) geometry;
			ray.setIntensity(lightable.directIntensity());
			return;
		} else if (geometry instanceof Surface) {
			Surface surface = (Surface) geometry;
			Vector point = intersection.getPosition();
			Vector normal = surface.normalAt(point);

			doReflection(ray, point, normal);
			doRefraction(ray, point, normal, surface);
			computeIntensity(ray, point, normal, surface);
		}
	}

	private Intersection nearestIntersection(@NotNull Ray ray) {
		float nst = 0;
		Geometry geom = null;
		for (Geometry source : sources) {
			try {
				float solution = source.intersection(ray.getOrigin(), ray.getDirection());
				if (nst <= 0) {
					nst = solution;
					geom = source;
				} else if (solution < nst) {
					nst = solution;
					geom = source;
				}
			} catch (NoSolutionException e) {
			}
		}

		for (Geometry object : objects) {
			float solution = 0;
			try {
				solution = object.intersection(ray.getOrigin(), ray.getDirection());
				if (nst <= 0) {
					nst = solution;
					geom = object;
				} else if (solution < nst) {
					nst = solution;
					geom = object;
				}
			} catch (NoSolutionException e) {
			}

		}

		if (geom == null)
			return null;
		return new Intersection(ray.getOrigin().plus(ray.getDirection().mul(nst)), geom);
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
		if (surface.isTransparentAt(point)) {
			Ray t = ray.refractedBy(point, normal, surface.refractionIndexAt(point));
			if (t != null) {
				t.setDepth(ray.getDepth() + 1);
				trace(t);
				ray.setSecondaryTrans(t);
			}
		}
	}

	private void computeIntensity(Ray ray, Vector point, Vector normal, Surface surface) {
		Intensity base = surface.intensityAt(point);
		Intensity i = new Intensity();
		for (Lightable source : sources) {
			Intensity intensity = source.reflecIntensity(ray.getDirection(), point, normal, surface);
			if (intensity != null)
				i = i.plus(intensity);
		}
		ray.setIntensity(base.mul(i));
	}

	public static class Intersection {

		private Vector position;
		private Geometry relevant;

		public Intersection(@NotNull Vector position, @NotNull Geometry relevant) {
			this.position = position;
			this.relevant = relevant;
		}

		public Vector getPosition() {
			return position;
		}

		public void setPosition(Vector position) {
			this.position = position;
		}

		public Geometry getRelevant() {
			return relevant;
		}

		public void setRelevant(Geometry relevant) {
			this.relevant = relevant;
		}

	}

}
