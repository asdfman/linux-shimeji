package com.group_finity.mascot.action;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.group_finity.mascot.animation.Animation;
import com.group_finity.mascot.exception.LostGroundException;
import com.group_finity.mascot.exception.VariableException;
import com.group_finity.mascot.script.VariableMap;

/**
 * 移動しないアクション.
 * @author Yuki Yamada
 */
public class Stay extends BorderedAction {

	private static final Logger log = Logger.getLogger(Stay.class.getName());

	public Stay(final List<Animation> animations, final VariableMap params) {
		super(animations, params);

	}

	@Override
	protected void tick() throws LostGroundException, VariableException {

		super.tick();

		if ((getBorder() != null) && !getBorder().isOn(getMascot().getAnchor())) {
			// 地面から離れてしまった
			log.log(Level.INFO, "枠を見失った({0},{1})", new Object[] { getMascot(), this });
			throw new LostGroundException();
		}

		// アニメーションさせる
		getAnimation().next(getMascot(), getTime());

	}

}
