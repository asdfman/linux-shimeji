package com.group_finity.mascot.environment;

import java.awt.Rectangle;
import java.util.Hashtable;
import com.group_finity.mascot.Mascot;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Iterator;

public class Area {

	private boolean visible = true;

	private int left;

	private int top;

	private int right;

	private int bottom;

	private int dleft;

	private int dtop;

	private int dright;

	private int dbottom;

	private final Wall leftBorder = new Wall(this, false);

	private final FloorCeiling topBorder = new FloorCeiling(this, false);

	private final Wall rightBorder = new Wall(this, true);

	private final FloorCeiling bottomBorder = new FloorCeiling(this, true);

	private final long windowID;

	public Area(long id) {
		this.windowID = id;
	}

	public Area() {
		this.windowID = 0;
		this.set(new Rectangle(-1,-1,0,0));
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	public int getLeft() {
		return this.left;
	}

	public void setLeft(final int left) {
		this.left = left;
	}

	public int getTop() {
		return this.top;
	}

	public void setTop(final int top) {
		this.top = top;
	}

	public int getRight() {
		return this.right;
	}

	public void setRight(final int right) {
		this.right = right;
	}

	public int getBottom() {
		return this.bottom;
	}

	public void setBottom(final int bottom) {
		this.bottom = bottom;
	}

	public int getDleft() {
		return this.dleft;
	}

	public void setDleft(final int dleft) {
		this.dleft = dleft;
	}

	public int getDtop() {
		return this.dtop;
	}

	public void setDtop(final int dtop) {
		this.dtop = dtop;
	}

	public int getDright() {
		return this.dright;
	}

	public void setDright(final int dright) {
		this.dright = dright;
	}

	public int getDbottom() {
		return this.dbottom;
	}

	public void setDbottom(final int dbottom) {
		this.dbottom = dbottom;
	}

	public Wall getLeftBorder() {
		return this.leftBorder;
	}

	public FloorCeiling getTopBorder() {
		return this.topBorder;
	}

	public Wall getRightBorder() {
		return this.rightBorder;
	}

	public FloorCeiling getBottomBorder() {
		return this.bottomBorder;
	}

	public int getWidth() {
		return getRight() - getLeft();
	}

	public int getHeight() {
		return getBottom() - getTop();
	}

	public void set(final Rectangle value) {

		setDleft(value.x - getLeft());
		setDtop(value.y - getTop());
		setDright(value.x + value.width - getRight());
		setDbottom(value.y + value.height - getBottom());

		setLeft(value.x);
		setTop(value.y);
		setRight(value.x + value.width);
		setBottom(value.y + value.height);
	}

	public boolean contains(final int x, final int y) {

		return (getLeft() <= x) && (x <= getRight()) && (getTop() <= y) && (y <= getBottom());
	}

	public Rectangle toRectangle() {
		return new Rectangle(left, top, right - left, bottom - top);
	}

	@Override
	public String toString() {
		return "Area [left=" + left + ", top=" + top + ", right=" + right + ", bottom=" + bottom + "]";
	}

	public long getID() {
		return this.windowID;
	}

}
