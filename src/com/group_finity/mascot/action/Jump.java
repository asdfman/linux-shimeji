package com.group_finity.mascot.action;

import java.awt.Point;
import java.util.List;
import java.util.logging.Logger;

import com.group_finity.mascot.animation.Animation;
import com.group_finity.mascot.exception.LostGroundException;
import com.group_finity.mascot.exception.VariableException;
import com.group_finity.mascot.script.VariableMap;


/**
 * ジャンプするアクション.
 */
public class Jump extends ActionBase {

	private static final Logger log = Logger.getLogger(Jump.class.getName());

	public static final String PARAMETER_TARGETX = "目的地X";
	
	private static final int DEFAULT_PARAMETERX = 0;
	
	public static final String PARAMETER_TARGETY = "目的地Y";
	
	private static final int DEFAULT_PARAMETERY = 0;

	public static final String PARAMETER_VELOCITY = "速度";

	private static final double DEFAULT_VELOCITY = 20.0;

	public Jump(final List<Animation> animations, final VariableMap params) {
		super(animations, params);
		
		
	}
	
	@Override
	public boolean hasNext() throws VariableException {
		final int targetX = getTargetX();
		final int targetY = getTargetY();

		final double distanceX = targetX - getMascot().getAnchor().x;
		final double distanceY = targetY - getMascot().getAnchor().y - Math.abs(distanceX)/2;

		final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
		
		return super.hasNext() && (distance != 0);
	}

	@Override
	protected void tick() throws LostGroundException, VariableException {

		final int targetX = getTargetX();
		final int targetY = getTargetY();

		getMascot().setLookRight(getMascot().getAnchor().x < targetX);

		final double distanceX = targetX - getMascot().getAnchor().x;
		final double distanceY = targetY - getMascot().getAnchor().y - Math.abs(distanceX)/2;

		final double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

		final double velocity = getVelocity();
		
		if (distance != 0) {
			final int velocityX = (int) (velocity * distanceX / distance);
			final int velocityY = (int) (velocity * distanceY / distance);

			getMascot().setAnchor(new Point(getMascot().getAnchor().x + velocityX, 
					getMascot().getAnchor().y + velocityY));
			getAnimation().next(getMascot(),getTime());
		}

		if (distance <= velocity) {
			getMascot().setAnchor(new Point(targetX, targetY));
		}

	}

	private double getVelocity() throws VariableException {
		return eval(PARAMETER_VELOCITY, Number.class, DEFAULT_VELOCITY).doubleValue();
	}

	private int getTargetY() throws VariableException{
		return eval(PARAMETER_TARGETY, Number.class, DEFAULT_PARAMETERY).intValue();
	}

	private int getTargetX() throws VariableException {
		return eval(PARAMETER_TARGETX, Number.class, DEFAULT_PARAMETERX).intValue();
	}

}
