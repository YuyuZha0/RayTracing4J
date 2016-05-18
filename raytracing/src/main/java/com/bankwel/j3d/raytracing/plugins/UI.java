package com.bankwel.j3d.raytracing.plugins;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.validation.constraints.NotNull;

public class UI {

	public static void display(@NotNull BufferedImage image) {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("UI");

		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 5379717747328699594L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, null);
			}
		};

		frame.getContentPane().add(panel);
		panel.updateUI();
	}
}
