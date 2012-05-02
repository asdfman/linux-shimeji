package com.group_finity.mascot.environment;

import java.awt.Point;
import java.awt.Rectangle;

import java.util.Vector;
import com.group_finity.mascot.Mascot;
import com.group_finity.mascot.NativeFactory;
import java.util.ArrayList;
import java.util.Iterator;

public class MascotEnvironment {

	private Environment impl = NativeFactory.getInstance().getEnvironment();

	private Mascot mascot;

	private int numScreens;

	private Area currentWorkArea;
	
	public MascotEnvironment(Mascot mascot) {
		this.mascot = mascot;
		this.numScreens = getComplexScreen().getAreas().size();
	}

	/**
	 * マスコットを含むスクリーンを取得する
	 * @return
	 */
	public Area getWorkArea() {
		if (numScreens == 1) {
			if (currentWorkArea == null) {
				currentWorkArea = impl.getWorkArea();
			}
			return currentWorkArea;
		}

		//if ( currentWorkArea!=null ) {
			 //NOTE Windows マルチモニタ対応 Windowsのワークエリアはメインのスクリーンより小さい。
			 //現在のスクリーンがワークエリアを含んでおり、かつマスコットがワークエリアに含まれているならばワークエリアを優先する。
			//if ( currentWorkArea!=impl.getWorkArea() && currentWorkArea.toRectangle().contains(impl.getWorkArea().toRectangle()) ) {
				//if (impl.getWorkArea().contains(mascot.getAnchor().x, mascot.getAnchor().y)) {
					//currentWorkArea = impl.getWorkArea();
					//return currentWorkArea;
				//}
			//}

			 //NOTE マルチモニタ対応 マスコットが複数のモニタに同時に含まれる場合があるが、
			 //その場合は現在のモニタを優先する
			//if ( currentWorkArea.contains(mascot.getAnchor().x, mascot.getAnchor().y) ) {
				//return currentWorkArea;
			//}
		//}

		 //まずワークエリアに含まれているか調べる
		//if (impl.getWorkArea().contains(mascot.getAnchor().x, mascot.getAnchor().y)) {
			//currentWorkArea = impl.getWorkArea();
			//return currentWorkArea;
		//}

		 //各モニタに含まれているか調べる
		if (currentWorkArea != null) {
			if (currentWorkArea.getTopBorder().isOn(mascot.getAnchor())) {
				return currentWorkArea;
			}
		}
		int cnt=0;
		Area a = new Area();
		for( Area area: impl.getScreens() ) {
			if ( area.contains(mascot.getAnchor().x, mascot.getAnchor().y) ) {
				cnt++;
				a = area;
			}
		}
		if (cnt == 1) {
			currentWorkArea = a;
			return currentWorkArea;
		} else if (cnt == 2) {
			Iterator<Area> iter = impl.getScreens().iterator();
			Area temp = iter.next();
			if (!mascot.isLookRight()) {
				currentWorkArea = temp;
				return currentWorkArea;
			} else {
				currentWorkArea = iter.next();
				return currentWorkArea;
			}
		}

		if (impl.getScreens().iterator().hasNext()) {
			currentWorkArea = impl.getScreens().iterator().next();
			return currentWorkArea;
		}

		currentWorkArea = impl.getWorkArea();
		return currentWorkArea;

	}

	public Area getActiveIE() {
		return impl.getActiveIE();
	}

    public WindowContainer getIE() {
        return impl.getIE();
    }

	public ArrayList<Number> getVisible() {
		return impl.getVisible();
	}

	public Border getCeiling() {
		return getCeiling(false);
	}

	public Border getCeiling(boolean ignoreSeparator) {
		if (getIE().onBottom(mascot.getAnchor())) {
			FloorCeiling fc = getIE().getBottom(mascot.getAnchor());
			if (!checkLayering(fc.getArea())) {
				mascot.setCurFC(null);
				return NotOnVisibleBorder.INSTANCE;
			}
			mascot.setCurFC(fc);
			return fc;
		}
		if ( getWorkArea().getTopBorder().isOn(mascot.getAnchor()) ) {
			if ( !ignoreSeparator ||isScreenTopBottom() ) {
				mascot.setCurFC(null);
				return getWorkArea().getTopBorder();
			}
		}
		mascot.setCurFC(null);
		return NotOnBorder.INSTANCE;
	}

	public ComplexArea getComplexScreen() {
		return impl.getComplexScreen();
	}

	public Location getCursor() {
		return impl.getCursor();
	}

	public Border getFloor() {
		return getFloor(false);
	}

/*
 * getFloor() - Environment check. Checks both the currently active windows
 * and the bottom of the screen. Returns the border if on one. Controls the
 * current Floor/Ceiling attribute of each mascot.
 */
	public Border getFloor(boolean ignoreSeparator) {
		if (getIE().onTop(mascot.getAnchor())) {
			FloorCeiling fc = getIE().getTop(mascot.getAnchor());
			if (!checkLayering(fc.getArea())) {
				mascot.setCurFC(null);
				return NotOnVisibleBorder.INSTANCE;
			}
			if (currentWorkArea == null) currentWorkArea = impl.getWorkArea();
		// Don't let the mascot get footing on any window ceiling within 64px
		// of the top of the screen.
			if (fc.getY() >= currentWorkArea.getTop()+64) {
				mascot.setCurFC(fc);
				return getIE().getTop(mascot.getAnchor());
			}
		}
		if ( getWorkArea().getBottomBorder().isOn(mascot.getAnchor()) ) {
			if ( !ignoreSeparator || isScreenTopBottom() ) {
				mascot.setCurFC(null);
   				return getWorkArea().getBottomBorder();
			}
		}
		mascot.setCurFC(null);
		return NotOnBorder.INSTANCE;
	}

	public Area getScreen() {
		return impl.getScreen();
	}

	public Border getWall() {
		return getWall(false);
	}

/*
 * getWall() - Environment check. Checks both the currently active windows
 * and the sides of the screen. Returns the border if on one. Controls the
 * current Wall attribute of each mascot.
 */
	public Border getWall(boolean ignoreSeparator) {
	 //Don't let the mascot hold onto window walls when within 64px of
	 //the top of the screen.
		if ( mascot.getAnchor().getY() <= getWorkArea().getTop()+64) { 
			Point p = mascot.getAnchor();
			if (mascot.onBorder()) {
				p.setLocation(p.getX()+1,p.getY());
				mascot.setAnchor(p);
			}
			return NotOnBorder.INSTANCE;
		}
		if ( mascot.isLookRight()) {
			if (getIE().onLeft(mascot.getAnchor())) {
				Wall w = getIE().getLeft(mascot.getAnchor());
				if (!checkLayering(w.getArea())) {
					mascot.setCurW(null);
					return NotOnVisibleBorder.INSTANCE;
				}
				mascot.setCurW(w);
				return w;
			}

			if ( getWorkArea().getRightBorder().isOn(mascot.getAnchor()) ) {
				//if ( !ignoreSeparator || isScreenLeftRight() ) {
				  if (isScreenLeftRight(ignoreSeparator)) {
					mascot.setCurW(null);
					return getWorkArea().getRightBorder();
				}
			}
			
		} else {
			if (getIE().onRight(mascot.getAnchor())) {
				Wall w = getIE().getRight(mascot.getAnchor());
				if (!checkLayering(w.getArea())) {
					mascot.setCurW(null);
					return NotOnVisibleBorder.INSTANCE;
				}
				mascot.setCurW(w);
				return w;
			}

			if ( getWorkArea().getLeftBorder().isOn(mascot.getAnchor()) ) {
				//if ( !ignoreSeparator ||isScreenLeftRight() ) {
				  if (isScreenLeftRight(ignoreSeparator)) {
					mascot.setCurW(null);
					return getWorkArea().getLeftBorder();
				}
			}
		}
		mascot.setCurW(null);
		return NotOnBorder.INSTANCE;
	}

	public void moveActiveIE(Point point) {
		impl.moveActiveIE(point);
	}

	public void restoreIE() {
		impl.restoreIE();
	}

	private boolean isScreenTopBottom() {
		return impl.isScreenTopBottom(mascot.getAnchor());
	}

	private boolean isScreenLeftRight(boolean b) {
		return impl.isScreenLeftRight(mascot.getAnchor(), b);
	}

	public int getDockValue() {
		return impl.getDockValue();
	}

	private boolean checkLayering(Area b) {
		ArrayList<Number> id = getVisible();
		Vector<Rectangle> rects = new Vector<Rectangle>(id.size());
		for (Number n : id) {
			rects.add(getIE().get(n).toRectangle());
		}
		Rectangle r = b.toRectangle();
		int index = 0;
		for (Rectangle rect : rects) {
			if (r.equals(rect)) {
				index = rects.indexOf(rect)+1;
			}
		}
		for (int i = index;i<rects.size();i++) {
			Rectangle rect = rects.get(i);
			if (r.intersects(rect)) {
				Rectangle isect = r.intersection(rect);
				isect.setRect(isect.x,isect.y,isect.getWidth()+1,isect.getHeight()+2);
				if (isect.contains(mascot.getAnchor())) {
					return false;
				}
			}
		}
		return true;
	}

		
}
