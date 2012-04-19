package com.group_finity.mascot.environment;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Random;

public class ManyAreas extends ArrayList<Area> {
	
	private Random RNG = new Random();
	public boolean visible = true;
	
	public boolean onLeft(final Point p) {
		for (int i=0;i<this.size();i++) {
			Wall w = new Wall(this.get(i),false);
			if (w.isOn(p)) return true;
		}
		return false;
	}
	
	public boolean onRight(final Point p) {
		for (int i=0;i<this.size();i++) {
			Wall w = new Wall(this.get(i),true);
			if (w.isOn(p)) return true;
		}
		return false;
	}
	
	public boolean onTop(final Point p) {
		for (int i=0;i<this.size();i++) {
			FloorCeiling w = new FloorCeiling(this.get(i),false);
			if (w.isOn(p)) return true;
		}
		return false;
	}
	
	public boolean onBottom(final Point p) {
		for (int i=0;i<this.size();i++) {
			FloorCeiling w = new FloorCeiling(this.get(i),true);
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
		for (int i=0;i<this.size();i++) {
			Wall w = new Wall(this.get(i),false);
			if (w.isOn(p)) return w;
		}
		return null;
	}
	
	public Wall getRight(final Point p) {
		for (int i=0;i<this.size();i++) {
			Wall w = new Wall(this.get(i),true);
			if (w.isOn(p)) return w;
		}
		return null;
	}
	
	public FloorCeiling getTop(final Point p) {
		for (int i=0;i<this.size();i++) {
			FloorCeiling w = new FloorCeiling(this.get(i),false);
			if (w.isOn(p)) return w;
		}
		return null;
	}
	
	public FloorCeiling getBottom(final Point p) {
		for (int i=0;i<this.size();i++) {
			FloorCeiling w = new FloorCeiling(this.get(i),true);
			if (w.isOn(p)) return w;
		}
		return null;
	}

}
