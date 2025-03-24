package com.punkipunk.game_test.ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Boton con imagen.
 */

public class ImageButton extends Component {

	private final BufferedImage[] images;
	private final ClickListener clicker;

	public ImageButton(float x, float y, int width, int height, BufferedImage[] images, ClickListener clicker) {
		super(x, y, width, height);
		this.images = images;
		this.clicker = clicker;
	}

	@Override
	public void tick() {
	}

	@Override
	public void render(Graphics g) {
		if (over) g.drawImage(images[1], (int) x, (int) y, width, height, null);
		else g.drawImage(images[0], (int) x, (int) y, width, height, null);
	}

	@Override
	public void onClick() {
		clicker.onClick();
	}

}