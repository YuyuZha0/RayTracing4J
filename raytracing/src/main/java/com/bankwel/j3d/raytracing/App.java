package com.bankwel.j3d.raytracing;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.bankwel.j3d.raytracing.core.Pixel;
import com.bankwel.j3d.raytracing.core.Ray;
import com.bankwel.j3d.raytracing.core.Ray.Intensity;
import com.bankwel.j3d.raytracing.core.Scene;
import com.bankwel.j3d.raytracing.core.Vector;
import com.bankwel.j3d.raytracing.geometrys.DotLight;
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
		Sphere sphere = new Sphere(new Vector(w / 2, h / 2, 0), 100);
		DotLight dotLight = new DotLight(new Vector(0, 0, -600), new Intensity(1, 1, 1));

		scene.add(sphere).add(dotLight);
		sphere.setIndex(2);

		Vector viewPoint = new Vector(w / 2, h / 2, -600);
		pixels.forEach(pixel -> {
			Ray ray = pixel.ray(viewPoint);
			scene.trace(ray);
			pixel.setRgb(ray.getIntensity().toColor().getRGB());
		});

		pixels.forEach(pixel -> {
			pixel.render(image);
		});

		UI.display(image);
	}
}
