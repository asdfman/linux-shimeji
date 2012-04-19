package com.group_finity.mascot.environment;

import java.awt.Point;

public class Location {

	private int x;
	private int y;
	
	private int dx;
	private int dy;
	public int getX() {
		return this.x;
	}
	public void setX(final int x) {
		this.x = x;
	}
	public int getY() {
		return this.y;
	}
	public void setY(final int y) {
		this.y = y;
	}
	public int getDx() {
		return this.dx;
	}
	public void setDx(final int dx) {
		this.dx = dx;
	}
	public int getDy() {
		return this.dy;
	}
	public void setDy(final int dy) {
		this.dy = dy;
	}
	
	public void set(final Point value){
		setDx( (getDx()+value.x-getX())/2);
		setDy( (getDy()+value.y-getY())/2);
		
		setX(value.x);
		setY(value.y);
	}
}
