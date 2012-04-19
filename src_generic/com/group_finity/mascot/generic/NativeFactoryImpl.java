package com.group_finity.mascot.generic;

import java.awt.image.BufferedImage;

import com.group_finity.mascot.NativeFactory;
import com.group_finity.mascot.environment.Environment;
import com.group_finity.mascot.image.NativeImage;
import com.group_finity.mascot.image.TranslucentWindow;

public class NativeFactoryImpl extends NativeFactory {

	private Environment environment = new GenericEnvironment();
	@Override
	public Environment getEnvironment() {
		return this.environment;
	}
	@Override
	public NativeImage newNativeImage(final BufferedImage src) {
		return new GenericNativeImage(src);
	}
	@Override
	public TranslucentWindow newTransparentWindow() {
		return new GenericTranslucentWindow();
	}
}
