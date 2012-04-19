package com.group_finity.mascot.action;

import java.awt.Point;
import java.util.List;
import java.util.logging.Logger;

import com.group_finity.mascot.Mascot;
import com.group_finity.mascot.animation.Animation;
import com.group_finity.mascot.environment.Border;
import com.group_finity.mascot.exception.LostGroundException;
import com.group_finity.mascot.exception.VariableException;
import com.group_finity.mascot.script.VariableMap;

/**
 * 枠にくっついて動くアクションの基底クラス.
 * @author Yuki Yamada
 */
public abstract class BorderedAction extends ActionBase {

	private static final Logger log = Logger.getLogger(BorderedAction.class.getName());

	private static final String PARAMETER_BORDERTYPE = "枠";

	public static final String DEFAULT_BORDERTYPE = null;

	public static final String BORDERTYPE_CEILING = "天井";

	public static final String BORDERTYPE_WALL = "壁";

	public static final String BORDERTYPE_FLOOR = "地面";

	private Border border;

	public BorderedAction(final List<Animation> animations, final VariableMap params) {
		super(animations, params);
	}

	@Override
	public void init(final Mascot mascot) throws VariableException {
		super.init(mascot);

		final String borderType = getBorderType();

		if (BORDERTYPE_CEILING.equals(borderType)) {
			this.setBorder(getEnvironment().getCeiling());
		} else if (BORDERTYPE_WALL.equals(borderType)) {
			this.setBorder(getEnvironment().getWall());
		} else if (BORDERTYPE_FLOOR.equals(borderType)) {
			this.setBorder(getEnvironment().getFloor());
		}


	}

	@Override
	protected void tick() throws LostGroundException, VariableException {

		if (getBorder() != null) {
			// 枠が動いているかも
			getMascot().setAnchor(getBorder().move(getMascot().getAnchor()));
		}
	}

	private String getBorderType() throws VariableException {
		return eval(PARAMETER_BORDERTYPE, String.class, DEFAULT_BORDERTYPE);
	}

	private void setBorder(final Border border) {
		this.border = border;

	}
	protected Border getBorder() {
		return this.border;
	}



}
