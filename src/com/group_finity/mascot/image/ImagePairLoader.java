package com.group_finity.mascot.image;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;



/**
 *　画像ペアを読み込む.
 */
public class ImagePairLoader {

	/**
	 * 画像ペアを読み込む.
	 *
	 * 左向き画像を読み込んで、右向き画像を自動生成する.
	 *
	 * @param name 読み込みたい左向き画像.
	 * @param center 画像の中央座標.
	 * @return 読み込んだ画像ペア.
	 */
	public static ImagePair load(final String name, final Point center) throws IOException {

		// flip では半透明にならない画像があるらしいので
		// shime1.png に対して shime1-r.png を反転画像として使用するようにして回避。
		String rightName = name.replaceAll("¥¥.[a-zA-Z]+$", "-r$0");

		final BufferedImage leftImage = ImageIO.read(ImagePairLoader.class.getResource(name));


		final BufferedImage rightImage;
		/*
		if ( ImagePairLoader.class.getResource(rightName)==null ) {
			rightImage = flip(leftImage);
		} else {
			rightImage = ImageIO.read(ImagePairLoader.class.getResource(rightName));
		}
		*/
		rightImage = flip(leftImage);

		return new ImagePair(new MascotImage(leftImage, center), new MascotImage(rightImage, new Point(rightImage
				.getWidth()
				- center.x, center.y)));
	}

	/**
	 * 画像を左右反転させる.
	 * @param src 左右反転したい画像
	 * @return　左右反転した
	 */
	private static BufferedImage flip(final BufferedImage src) {

		final BufferedImage copy = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);

		for (int y = 0; y < src.getHeight(); ++y) {
			for (int x = 0; x < src.getWidth(); ++x) {
				copy.setRGB(copy.getWidth() - x - 1, y, src.getRGB(x, y));
			}
		}
		return copy;
	}

}
