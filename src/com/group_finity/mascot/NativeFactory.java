package com.group_finity.mascot;

import java.awt.image.BufferedImage;

import com.group_finity.mascot.environment.Environment;
import com.group_finity.mascot.image.NativeImage;
import com.group_finity.mascot.image.TranslucentWindow;
import com.sun.jna.Platform;

/**
 * ネイティブ環境へのアクセスを提供する.
 * {@link #getInstance()} は実行環境によって Windows 用あるいは汎用のサブクラスのインスタンスを返す.
 * @author Yuki
 */
public abstract class NativeFactory {

	private static final NativeFactory instance;

	/**
	 * サブクラスのインスタンスを作成しておく.
	 */
	static {
		final String basePackage = NativeFactory.class.getName().substring(0, NativeFactory.class.getName().lastIndexOf('.'));

		final String subPackage = Platform.isX11() ? "x11" : "generic";

		try {
			final Class<? extends NativeFactory> impl = (Class<? extends NativeFactory>)Class.forName(basePackage+"."+subPackage+".NativeFactoryImpl");

			instance = impl.newInstance();

		} catch (final ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (final InstantiationException e) {
			throw new RuntimeException(e);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 実行環境に応じたサブクラスのインスタンスを取得する.
	 * @return
	 */
	public static NativeFactory getInstance() {
		return instance;
	}

	/**
	 * 環境オブジェクトを取得する.
	 * @return 環境オブジェクト.
	 */
	public abstract Environment getEnvironment();

	/**
	 * 指定された BufferedImage に対応するネイティブな画像を取得する.
	 * この画像は {@link TranslucentWindow} のマスキングに使用できる.
	 * @param src
	 * @return
	 */
	public abstract NativeImage newNativeImage(BufferedImage src);

	/**
	 * 半透明表示が可能なウィンドウを作成する.
	 * @return
	 */
	public abstract TranslucentWindow newTransparentWindow();
}
