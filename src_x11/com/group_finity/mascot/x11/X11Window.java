package com.group_finity.mascot.x11;

import java.awt.Point;

public class X11Window {

	private Point window_anchor = new Point();
	private boolean dragged;
	private int top;
	private int bottom;
	private int left;
	private int right;
	private int top_border;
	private int left_border;
	private int bottom_border;
	private int right_border;
	
	public int getTop_border() {
		return top_border;
	}

	public void setTop_border(int top_border) {
		this.top_border = top_border;
	}

	public int getLeft_border() {
		return left_border;
	}

	public void setLeft_border(int left_border) {
		this.left_border = left_border;
	}

	public int getBottom_border() {
		return bottom_border;
	}

	public void setBottom_border(int bottom_border) {
		this.bottom_border = bottom_border;
	}

	public int getRight_border() {
		return right_border;
	}
	
	public int getHeight_border() {
		return top_border+bottom_border;
	}
	
	public int getWidth_border() {
		return right_border+left_border;
	}

	public void setRight_border(int right_border) {
		this.right_border = right_border;
	}


	
	X11Window(int x, int y){
		
		window_anchor.setLocation(x, y);
		dragged = false;
	}
	
	X11Window(){
		dragged = false;
	}
	
	public int getX(){
		return window_anchor.x;
	}
	
	public int getY(){
		return window_anchor.y;
	}
	
	public void setX(int val){
	  if(!dragged)
	  window_anchor.x = val;
	}
	
	public void setY(int val){
	  if(!dragged)
	  window_anchor.y = val;
	}
	
	public void setDragged(boolean b){
		dragged = b;
	}
	
	public boolean getDragged(){
		return dragged;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getBottom() {
		return bottom;
	}

	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getRight() {
		return right;
	}

	public void setRight(int right) {
		this.right = right;
	}
	
	public String toString() {
		return new String("Frame Border: top:"+top_border+" left:"+left_border+" bottom:"+bottom_border+" right:"+right_border);
	}
	
	
	
	
}
