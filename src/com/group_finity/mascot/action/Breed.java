package com.group_finity.mascot.action;

import java.awt.Point;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.group_finity.mascot.Main;
import com.group_finity.mascot.Mascot;
import com.group_finity.mascot.animation.Animation;
import com.group_finity.mascot.exception.BehaviorInstantiationException;
import com.group_finity.mascot.exception.CantBeAliveException;
import com.group_finity.mascot.exception.LostGroundException;
import com.group_finity.mascot.exception.VariableException;
import com.group_finity.mascot.script.VariableMap;

/**
 * 増殖するアクション.
 * 
 * @author Yuki Yamada
 */
public class Breed extends Animate {

	private static final Logger log = Logger.getLogger(Breed.class.getName());

	public static final String PARAMETER_BORNX = "生まれる場所X";

	private static final int DEFAULT_BORNX = 0;

	public static final String PARAMETER_BORNY = "生まれる場所Y";

	private static final int DEFAULT_BORNY = 0;

	public static final String PARAMETER_BORNBEHAVIOR = "生まれた時の行動";

	private static final String DEFAULT_BORNBEHAVIOR = "";

	public Breed(final List<Animation> animations, final VariableMap params) {
		super(animations, params);
	}

	@Override
	protected void tick() throws LostGroundException, VariableException {

		super.tick();

		if (getTime() == getAnimation().getDuration() - 1) {
			// 増える
			breed();
		}
	}

	private void breed() throws VariableException {

		// マスコットを1個作成
		final Mascot mascot = new Mascot();

		log.log(Level.INFO, "増殖({0},{1},{2})", new Object[] { getMascot(), this, mascot });

		// 範囲外から開始
		if (getMascot().isLookRight()) {
			mascot.setAnchor(new Point(getMascot().getAnchor().x - getBornX(), getMascot().getAnchor().y
					+ getBornY().intValue()));
		} else {
			mascot.setAnchor(new Point(getMascot().getAnchor().x + getBornX(), getMascot().getAnchor().y
					+ getBornY().intValue()));
		}
		mascot.setLookRight(getMascot().isLookRight());

		try {
			mascot.setBehavior(Main.getInstance().getConfiguration().buildBehavior(getBornBehavior()));

			getMascot().getManager().add(mascot);
		
		} catch (final BehaviorInstantiationException e) {
			log.log(Level.SEVERE, "生まれた時の行動の初期化に失敗しました", e);
			mascot.dispose();
		} catch (final CantBeAliveException e) {
			log.log(Level.SEVERE, "生き続けることが出来ない状況", e);
			mascot.dispose();
		}
	}

	private Number getBornY() throws VariableException {
		return eval(PARAMETER_BORNY, Number.class, DEFAULT_BORNY);
	}

	private int getBornX() throws VariableException {
		return eval(PARAMETER_BORNX, Number.class, DEFAULT_BORNX).intValue();
	}

	private String getBornBehavior() throws VariableException {
		return eval(PARAMETER_BORNBEHAVIOR, String.class, DEFAULT_BORNBEHAVIOR);
	}

}
