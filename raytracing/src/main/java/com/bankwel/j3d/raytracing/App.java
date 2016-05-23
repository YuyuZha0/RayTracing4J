package com.bankwel.j3d.raytracing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.bankwel.j3d.raytracing.core.Pixel;
import com.bankwel.j3d.raytracing.core.Ray;
import com.bankwel.j3d.raytracing.core.Scene;
import com.bankwel.j3d.raytracing.core.Vector;
import com.bankwel.j3d.raytracing.core.model.Source.Intensity;
import com.bankwel.j3d.raytracing.geometrys.DotSource;
import com.bankwel.j3d.raytracing.geometrys.Plain;
import com.bankwel.j3d.raytracing.geometrys.Sphere;
import com.bankwel.j3d.raytracing.plugins.UI;

public class App {

	public static void main(String[] args) {

		BufferedImage image = new BufferedImage(600, 400, BufferedImage.TYPE_INT_ARGB);
		List<Pixel> pixels = new ArrayList<Pixel>();
		int w = image.getWidth();
		int h = image.getHeight();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				pixels.add(new Pixel(i, j));
			}
		}

		Scene scene = new Scene();
		Sphere sphere1 = new Sphere(new Vector(150, 100, 0), 100);
		Sphere sphere2 = new Sphere(new Vector(200, 300, 100), 70);
		Plain plain = new Plain(new Vector(w / 2, h / 2, 500), new Vector(-1, -1, -1));
		sphere1.illuminationIndex(1, 1, 1).color(1, 1, 1);
		sphere2.illuminationIndex(1, 1, 8).color(1, 1, 1);
		plain.illuminationIndex(1, 1, 1);
		DotSource dotLight1 = new DotSource(new Vector(0, 0, -600), new Intensity(1, 1, 1));
		DotSource dotLight2 = new DotSource(new Vector(500, 0, -600), new Intensity(0.1f, 0.1f, 0.8f));
		scene.add(plain).add(dotLight1);
		Vector viewPoint = new Vector(w / 2, h / 2, -1000);
		pixels.forEach(pixel -> {
			Ray ray = pixel.ray(viewPoint);
			scene.trace(ray);
			pixel.setIntensity(ray.countIntensity());
		});

		pixels.forEach(pixel -> {
			pixel.render(image);
		});

		UI.display(image);
	}
}
