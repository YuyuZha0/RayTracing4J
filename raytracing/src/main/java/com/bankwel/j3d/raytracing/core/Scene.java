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
	private List<Unlightable> objects = new ArrayList<Unlightable>();

	private Intensity backgroud = new Intensity();

	private static final Logger logger = LoggerFactory.getLogger(Scene.class);

	public Scene add(@NotNull Geometry geometry) {
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
		float nst = 0;
		Geometry geom = null;
		int geomType = 0;

		for (Geometry source : sources) {
			try {
				float solution = source.intersection(ray);
				if (nst <= 0) {
					nst = solution;
					geom = source;
					geomType = 1;
				} else if (solution < nst) {
					nst = solution;
					geom = source;
					geomType = 1;
				}
			} catch (NoSolutionException e) {
			}
		}

		for (Geometry object : objects) {
			float solution = 0;
			try {
				solution = object.intersection(ray);
				if (nst <= 0) {
					nst = solution;
					geom = object;
					geomType = 2;
				} else if (solution < nst) {
					nst = solution;
					geom = object;
					geomType = 2;
				}
			} catch (NoSolutionException e) {
			}

		}
		if (geomType == 0) {
			ray.setIntensity(backgroud);
			return;
		}
		if (geomType == 1) {
			Lightable lightable = (Lightable) geom;
			ray.setIntensity(lightable.directIntensity());
			return;
		}
		if (geomType != 2) {
			return;
		}
		Unlightable unlightable = (Unlightable) geom;
		// point = p0 + x*u
		Vector u = ray.getDirection();
		Vector p0 = ray.getOrigin();
		Vector point = p0.plus(u.mul(nst));
		Vector normal = unlightable.normalAt(point);

		doReflection(ray, point, normal);
		doRefraction(ray, point, normal, unlightable);
		computeIntensity(ray, point, normal, unlightable);
	}

	private void doReflection(Ray ray, Vector point, Vector normal) {
		Ray r = ray.reflectedBy(point, normal);
		if (r != null) {
			r.setDepth(ray.getDepth() + 1);
			trace(r);
			ray.setSecondaryRef(r);
		}
	}

	private void doRefraction(Ray ray, Vector point, Vector normal, Unlightable unlightable) {
		if (unlightable.isTransparentAt(point)) {
			Ray t = ray.refractedBy(point, normal, unlightable.indexAt(point));
			if (t != null) {
				t.setDepth(ray.getDepth() + 1);
				trace(t);
				ray.setSecondaryTrans(t);
			}
		}
	}

	private void computeIntensity(Ray ray, Vector point, Vector normal, Unlightable unlightable) {
		Intensity base = unlightable.intensityAt(point);
		for (Lightable source : sources) {
			Intensity intensity = source.reflecIntensity(ray.getDirection(), point, normal,
					unlightable.reflectPolicyAt(point));
			base = base.mul(intensity);
		}
		ray.setIntensity(base);
	}

}
