package com.bankwel.j3d.raytracing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.bankwel.j3d.raytracing.geometrys.PointSource;
import com.bankwel.j3d.raytracing.geometrys.Plain;
import com.bankwel.j3d.raytracing.geometrys.Sphere;
import com.bankwel.j3d.raytracing.model.Intensity;
import com.bankwel.j3d.raytracing.model.Pixel;
import com.bankwel.j3d.raytracing.model.Ray;
import com.bankwel.j3d.raytracing.model.Scene;
import com.bankwel.j3d.raytracing.model.Vector;
import com.bankwel.j3d.raytracing.plugins.UI;

public class App {

	public static void main(String[] args) {

		BufferedImage image = new BufferedImage(600, 400, BufferedImage.TYPE_INT_ARGB);
		List<Pixel> pixels = new ArrayList<Pixel>();
		int w = image.getWidth();
		int h = image.getHeight();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				pixels.add(new Pixel(i, j, 3));
			}
		}

		Scene scene = new Scene();
		Sphere sphere1 = new Sphere(new Vector(150, 100, 100), 90);
		Sphere sphere2 = new Sphere(new Vector(200, 200, 100), 70);
		Sphere sphere3 = new Sphere(new Vector(400, 200, 100), 70);
		Plain plain = new Plain(new Vector(w / 2, h / 2, 500), new Vector(0, -10, -1));
		sphere1.illumination(1, 0, 1.4f).reflection(0.02f, 0.02f, 0.02f).refraction(1.5f);
		sphere2.illumination(0, 1, 1).reflection(0.05f, 0.05f, 0.05f);
		sphere3.illumination(0, 0.7f, 1.3f).reflection(0.1f, 0.2f, 0.2f);
		plain.illumination(1, 1, 1).reflection(0.07f, 0.07f, 0.07f);

		PointSource dot1 = new PointSource(new Vector(0, 0, -200), new Intensity(1, 1, 1));
		PointSource dot2 = new PointSource(new Vector(500, 0, 1700), new Intensity(1, 1, 1));
		scene.add(sphere1).add(sphere2).add(sphere3).add(plain).ambient(new Intensity(0.2f, 0.2f, 0.2f)).add(dot1)
				.add(dot2);
		Vector viewPoint = new Vector(w / 2, h / 2, -1500);
		pixels.forEach(pixel -> {
			Intensity intensity = new Intensity();
			List<Ray> rays = pixel.ray(viewPoint);
			rays.forEach(ray -> {
				intensity.join(ray.trace(scene));
			});
			pixel.setIntensity(intensity.reduce(1f / rays.size()));
		});

		pixels.forEach(pixel -> {
			pixel.render(image);
		});

		UI.display(image);
	}
}
