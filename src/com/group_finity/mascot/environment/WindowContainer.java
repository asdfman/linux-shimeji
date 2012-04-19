package com.group_finity.mascot.environment;

import java.awt.Rectangle;
import java.awt.Point;
import java.util.Hashtable;
import java.util.Iterator;

public class WindowContainer extends Hashtable<Number,Area> {
	
	public boolean onLeft(final Point p) {
		Iterator<Area> temp = this.values().iterator();
		while (temp.hasNext()) {
			Wall w = new Wall(temp.next(),false);
			if (w.isOn(p)) return true;
		}
		return false;
	}
	
	public boolean onRight(final Point p) {
		Iterator<Area> temp = this.values().iterator();
		while (temp.hasNext()) {
			Wall w = new Wall(temp.next(),true);
			if (w.isOn(p)) return true;
		}
		return false;
	}
	
	public boolean onTop(final Point p) {
		Iterator<Area> temp = this.values().iterator();
		while (temp.hasNext()) {
			FloorCeiling w = new FloorCeiling(temp.next(),false);
			if (w.isOn(p)) return true;
		}
		return false;
	}
	
	public boolean onBottom(final Point p) {
		Iterator<Area> temp = this.values().iterator();
		while (temp.hasNext()) {
			FloorCeiling w = new FloorCeiling(temp.next(),true);
			if (w.isOn(p)) return true;
		}
		return false;
	}
	
	public Area getRandom() {
		if (size() > 0) {
			return get(RNG.nextInt(size()));
		}
		return null;
	}
	
	public Wall getLeft(final Point p) {
		Iterator<Area> temp = this.values().iterator();
		while (temp.hasNext()) {
			Wall w = new Wall(temp.next(),false);
			if (w.isOn(p)) return w;
		}
		return null;
	}
	
	public Wall getRight(final Point p) {
		Iterator<Area> temp = this.values().iterator();
		while (temp.hasNext()) {
			Wall w = new Wall(temp.next(),true);
			if (w.isOn(p)) return w;
		}
		return null;
	}
	
	public FloorCeiling getTop(final Point p) {
		Iterator<Area> temp = this.values().iterator();
		while (temp.hasNext()) {
			FloorCeiling w = new FloorCeiling(temp.next(),false);
			if (w.isOn(p)) return w;
		}
		return null;
	}
	
	public FloorCeiling getBottom(final Point p) {
		Iterator<Area> temp = this.values().iterator();
		while (temp.hasNext()) {
			FloorCeiling w = new FloorCeiling(temp.next(),true);
			if (w.isOn(p)) return w;
		}
		return null;
	}

}
