package com.group_finity.mascot.animation;

import com.group_finity.mascot.Mascot;
import com.group_finity.mascot.exception.VariableException;
import com.group_finity.mascot.script.Variable;
import com.group_finity.mascot.script.VariableMap;

public class Animation {

	private Variable condition;

	private final Pose[] poses;

	public Animation(final Variable condition, final Pose... poses) {

		if (poses.length == 0) {
			throw new IllegalArgumentException("poses.length==0");
		}

		this.condition = condition;
		this.poses = poses;
	}

	public boolean isEffective(final VariableMap variables) throws VariableException {
		return (Boolean) getCondition().get(variables);
	}

	public void init() {
		getCondition().init();
	}

	public void initFrame() {
		getCondition().initFrame();
	}

	public void next(final Mascot mascot, final int time) {
		getPoseAt(time).next(mascot);
	}

	public Pose getPoseAt(int time) {

		time %= getDuration();

		for (final Pose pose : getPoses()) {
			time -= pose.getDuration();
			if (time < 0) {
				return pose;
			}
		}

		return null;
	}

	public int getDuration() {

		int duration = 0;
		for (final Pose pose : getPoses()) {
			duration += pose.getDuration();
		}

		return duration;
	}

	private Variable getCondition() {
		return this.condition;
	}

	private Pose[] getPoses() {
		return this.poses;
	}

}
