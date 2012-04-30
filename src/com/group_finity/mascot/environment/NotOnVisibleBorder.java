
package com.group_finity.mascot.environment;

import java.awt.Point;

public class NotOnVisibleBorder implements Border {

	public static final NotOnVisibleBorder INSTANCE = new NotOnVisibleBorder();

	private NotOnVisibleBorder() {

	}

	@Override
	public boolean isOn(final Point location) {
		return false;
	}

	@Override
	public Point move(final Point location) {
		return location;
	}
}
