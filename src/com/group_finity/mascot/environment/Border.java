package com.group_finity.mascot.environment;

import java.awt.Point;

public interface Border {

	public boolean isOn(Point location);

	public Point move(Point location);
}
