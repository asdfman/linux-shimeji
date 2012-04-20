package com.group_finity.mascot.environment;

import java.awt.Rectangle;
import java.awt.Point;
import java.util.Hashtable;
import java.util.Iterator;

/*
 * Hashtable extension storing XWindow ID's and Area objects
 * representing the window dimensions. Methods called by
 * mascots (via MascotEnvironment) when choosing new actions
 * to check whether they're on a border of any kind.
 */

public class WindowContainer extends Hashtable<Number,Area> {
	
/*
 * onBorder, getBorder methods - Called by mascots when 
 * determining the next action. Iterate through all windows,
 * get the specific border type and call its isOn() to 
 * check whether the mascot anchor is on the border. One 
 * method for plain boolean checks, one for getting the border
 * when needing to decide movement destinations based on it.
 *
 */
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
