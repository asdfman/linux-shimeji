package com.group_finity.mascot.action;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.group_finity.mascot.animation.Animation;
import com.group_finity.mascot.exception.LostGroundException;
import com.group_finity.mascot.exception.VariableException;
import com.group_finity.mascot.script.VariableMap;

/**
 * ドラッグに抵抗するアクション.
 */
public class Regist extends ActionBase {

	private static final Logger log = Logger.getLogger(Regist.class.getName());

	public Regist(final List<Animation> animations, final VariableMap params) {
		super(animations, params);
	}

	@Override
	public boolean hasNext() throws VariableException {

		final boolean notMoved = Math.abs(getEnvironment().getCursor().getX() - getMascot().getAnchor().x) < 5;

		return super.hasNext() && notMoved;
	}

	@Override
	protected void tick() throws LostGroundException, VariableException {

		// アニメーションさせる
		final Animation animation = getAnimation();
		animation.next(getMascot(), getTime());

		if (getTime() + 1 >= getAnimation().getDuration()) {
			// 期間が過ぎたので終了.

			getMascot().setLookRight(Math.random() < 0.5);

			log.log(Level.INFO, "マウスから逃げた({0},{1})", new Object[] { getMascot(), this });
			throw new LostGroundException();
		}

	}

}
