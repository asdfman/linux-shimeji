package com.group_finity.mascot.environment;

import java.awt.Point;

public class Wall implements Border {

	private Area area;

	private boolean right;

	public Wall(final Area area, final boolean right) {
		this.area = area;
		this.right = right;
	}

	public Area getArea() {
		return this.area;
	}

	public boolean isRight() {
		return this.right;
	}

	public int getX() {
		return isRight() ? getArea().getRight() : getArea().getLeft();
	}

	public int getTop() {
		return getArea().getTop();
	}

	public int getBottom() {
		return getArea().getBottom();
	}

	public int getDX() {
		return isRight() ? getArea().getDright() : getArea().getDleft();
	}

	public int getDTop() {
		return getArea().getDtop();
	}

	public int getDBottom() {
		return getArea().getDbottom();
	}
	
	public int getHeight() {
		return getArea().getHeight();
	}

	@Override
	public boolean isOn(final Point location) {
		return getArea().isVisible() && (getX() == location.x) && (getTop() <= location.y) && (location.y <= getBottom());
	}

	public Point move(final Point location) {

		if (!getArea().isVisible()) {
			return location;
		}
		
		final int d = getBottom() - getDBottom() - (getTop() - getDTop());
		if ( d==0 ) {
			return location;
		}

		final Point newLocation = new Point(location.x + getDX(), (location.y - (getTop() - getDTop()))
				* (getBottom() - getTop()) / d + getTop());

		if ((Math.abs(newLocation.x - location.x) >= 80) || (Math.abs(newLocation.y - location.y) >= 80)) {
			return location;
		}
		return newLocation;
	}
}
