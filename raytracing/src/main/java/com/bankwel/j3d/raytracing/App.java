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
		Sphere sphere1 = new Sphere(new Vector(150, 100, 0), 20);
		Sphere sphere2 = new Sphere(new Vector(200, 200, 100), 70);
		Plain plain = new Plain(new Vector(w / 2, h / 2, 500), new Vector(0, -10, -1));
		sphere1.illumination(1, 9, 1.4f).color(0.5f, 0.8f, 0.2f);
		sphere2.illumination(1, 1, 8).color(1, 1, 1);
		plain.illumination(1, 7, 1.8f).color(0.7f, 0.4f, 0.9f);
		DotSource dotLight1 = new DotSource(new Vector(0, 0, -900), new Intensity(1, 1, 1));
		DotSource dotLight2 = new DotSource(new Vector(500, 0, -600), new Intensity(0.1f, 0.1f, 0.8f));
		scene.add(sphere1).add(sphere2).add(dotLight1).add(plain);
		Vector viewPoint = new Vector(w / 2, h / 2, -1000);
		pixels.forEach(pixel -> {
			Ray ray = pixel.ray(viewPoint).trace(scene);
			pixel.setIntensity(ray.getIntensity());
		}); 

		pixels.forEach(pixel -> {
			pixel.render(image);
		});

		UI.display(image);
	}
}
