package com.group_finity.mascot.generic;

import java.awt.Point;

import com.group_finity.mascot.environment.Area;
import com.group_finity.mascot.environment.WindowContainer;
import com.group_finity.mascot.environment.Environment;
import java.util.ArrayList;


/**
 * Java では取得が難しい環境情報をJNIを使用して取得する.
 */
class GenericEnvironment extends Environment {

    private static Area activeIE;
	private static WindowContainer IE;
	private static int dockValue;
	private static ArrayList<Number> curVisibleWin;

	@Override
	public void tick() {
		super.tick();
//		this.activeIE.setVisible(false);
	}

	@Override
	public void moveActiveIE(final Point point) {
	}

	@Override
	public void restoreIE() {

	}

	@Override
	public Area getWorkArea() {
		return getScreen();
	}

	@Override
	public Area getActiveIE() {
		return this.activeIE;
	}

	@Override
	public WindowContainer getIE() {
		return this.IE;
	}

	@Override
	public ArrayList<Number> getVisible() {
		return this.curVisibleWin;
	}

	@Override
	public int getDockValue() {
		return this.dockValue;
	}

}
