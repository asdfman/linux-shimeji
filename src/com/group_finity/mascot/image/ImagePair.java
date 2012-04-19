package com.group_finity.mascot.image;


/**
 * マスコット画像の左右向きのペア.
 * 
 * マスコットの画像は左右同時に管理できると都合が良い.
 */
public class ImagePair {

	/**
	 * 左を向いている画像.
	 */
	private MascotImage leftImage;

	/**
	 * 右を向いている画像.
	 */
	private MascotImage rightImage;

	/**
	 * 既存の二つの画像から画像ペアを作成する.
	 * @param leftImage　左を向いている画像.
	 * @param rightImage 右を向いている画像.
	 */
	public ImagePair(
			final MascotImage leftImage, final MascotImage rightImage) {
		this.leftImage = leftImage;
		this.rightImage = rightImage;
	}

	/**
	 * 指定した向きを向いた画像を取得する.
	 * @param lookRight 右向きの画像を取得するかどうか.
	 * @return 指定した向きを向いている画像.
	 */
	public MascotImage getImage(final boolean lookRight) {
		return lookRight ? this.getRightImage() : this.getLeftImage();
	}

	private MascotImage getLeftImage() {
		return this.leftImage;
	}
	
	private MascotImage getRightImage() {
		return this.rightImage;
	}
}
