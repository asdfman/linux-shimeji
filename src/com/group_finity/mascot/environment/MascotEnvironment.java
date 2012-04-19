package com.group_finity.mascot.environment;

import java.awt.Point;

import com.group_finity.mascot.Mascot;
import com.group_finity.mascot.NativeFactory;
import java.util.ArrayList;

public class MascotEnvironment {

	private Environment impl = NativeFactory.getInstance().getEnvironment();

	private Mascot mascot;

	private Area currentWorkArea;
	
	public MascotEnvironment(Mascot mascot) {
		this.mascot = mascot;
	}

	/**
	 * マスコットを含むスクリーンを取得する
	 * @return
	 */
	public Area getWorkArea() {

		if ( currentWorkArea!=null ) {
			// NOTE Windows マルチモニタ対応 Windowsのワークエリアはメインのスクリーンより小さい。
			// 現在のスクリーンがワークエリアを含んでおり、かつマスコットがワークエリアに含まれているならばワークエリアを優先する。
			if ( currentWorkArea!=impl.getWorkArea() && currentWorkArea.toRectangle().contains(impl.getWorkArea().toRectangle()) ) {
				if (impl.getWorkArea().contains(mascot.getAnchor().x, mascot.getAnchor().y)) {
					currentWorkArea = impl.getWorkArea();
					return currentWorkArea;
				}
			}

			// NOTE マルチモニタ対応 マスコットが複数のモニタに同時に含まれる場合があるが、
			// その場合は現在のモニタを優先する
			if ( currentWorkArea.contains(mascot.getAnchor().x, mascot.getAnchor().y) ) {
				return currentWorkArea;
			}
		}

		// まずワークエリアに含まれているか調べる
		if (impl.getWorkArea().contains(mascot.getAnchor().x, mascot.getAnchor().y)) {
			currentWorkArea = impl.getWorkArea();
			return currentWorkArea;
		}

		// 各モニタに含まれているか調べる
		for( Area area: impl.getScreens() ) {
			if ( area.contains(mascot.getAnchor().x, mascot.getAnchor().y) ) {
				currentWorkArea = area;
				return currentWorkArea;
			}
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

	public Border getCeiling() {
		return getCeiling(false);
	}
	public Border getCeiling(boolean ignoreSeparator) {
		if (getIE().onBottom(mascot.getAnchor())) {
			FloorCeiling fc = getIE().getBottom(mascot.getAnchor());
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

	public Border getFloor(boolean ignoreSeparator) {
		if (getIE().onTop(mascot.getAnchor())) {
			FloorCeiling fc = getIE().getTop(mascot.getAnchor());
			if (currentWorkArea == null) currentWorkArea = impl.getWorkArea();
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

	public Border getWall(boolean ignoreSeparator) {
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
				mascot.setCurW(getIE().getLeft(mascot.getAnchor()));
				return getIE().getLeft(mascot.getAnchor());
			}

			if ( getWorkArea().getRightBorder().isOn(mascot.getAnchor()) ) {
				if ( !ignoreSeparator || isScreenLeftRight() ) {
					mascot.setCurW(null);
					return getWorkArea().getRightBorder();
				}
			}
			
		} else {
			if (getIE().onRight(mascot.getAnchor())) {
				mascot.setCurW(getIE().getRight(mascot.getAnchor()));
				return getIE().getRight(mascot.getAnchor());
			}

			if ( getWorkArea().getLeftBorder().isOn(mascot.getAnchor()) ) {
				if ( !ignoreSeparator ||isScreenLeftRight() ) {
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

	private boolean isScreenLeftRight() {
		return impl.isScreenLeftRight(mascot.getAnchor());
	}

}
