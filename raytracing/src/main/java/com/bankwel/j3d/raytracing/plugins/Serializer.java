package com.bankwel.j3d.raytracing.plugins;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Serializer {

	private static final Logger logger = LoggerFactory.getLogger(Serializer.class);

	public static void save(BufferedImage image, String path, String format) {
		if (image == null)
			return;
		File file = new File(path + File.separator + nameGenerator() + "." + format);
		if (!file.exists())
			file.mkdirs();
		try {
			ImageIO.write(image, format, file);
			logger.info("Image has been saved to path[{}] successfully.", path);
		} catch (IOException e) {
			logger.error("Save failed.", e);
		}
	}

	private static String nameGenerator() {
		return new SimpleDateFormat("yyyy-MM-dd-").format(new Date()) + UUID.randomUUID().toString();
	}

	public static void main(String[] args) {
		System.out.println(nameGenerator());
	}
}
