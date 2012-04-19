package com.group_finity.mascot.action;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.group_finity.mascot.Mascot;
import com.group_finity.mascot.animation.Animation;
import com.group_finity.mascot.exception.LostGroundException;
import com.group_finity.mascot.exception.VariableException;
import com.group_finity.mascot.script.VariableMap;

/**
 * 複数のアクションを直列に一つにまとめたアクションの基底クラス.
 */
public abstract class ComplexAction extends ActionBase {

	private static final Logger log = Logger.getLogger(ComplexAction.class.getName());

	private final Action[] actions;

	private int currentAction;

	public ComplexAction(final VariableMap params, final Action... actions) {
		super(new ArrayList<Animation>(), params);
		if (actions.length == 0) {
			throw new IllegalArgumentException("actions.length==0");
		}

		this.actions = actions;
	}

	@Override
	public void init(final Mascot mascot) throws VariableException {
		super.init(mascot);

		if (super.hasNext()) {
			setCurrentAction(0);
			seek();
		}
	}

	protected void seek() throws VariableException {
		if (super.hasNext()) {
			while (getCurrentAction() < getActions().length) {
				if (getAction().hasNext()) {
					break;
				}
				setCurrentAction(getCurrentAction() + 1);
			}
		}
	}

	@Override
	public boolean hasNext() throws VariableException {
		final boolean inrange = this.getCurrentAction() < this.getActions().length;

		return super.hasNext() && inrange && getAction().hasNext();
	}

	@Override
	protected void tick() throws LostGroundException, VariableException {
		if (getAction().hasNext()) {
			getAction().next();
		}
	}

	protected void setCurrentAction(final int currentAction) throws VariableException {
		this.currentAction = currentAction;
		if (super.hasNext()) {
			if (this.getCurrentAction() < getActions().length) {
				getAction().init(getMascot());
			}
		}
	}

	protected int getCurrentAction() {
		return this.currentAction;
	}

	protected Action[] getActions() {
		return this.actions;
	}

	protected Action getAction() {
		return this.getActions()[this.getCurrentAction()];
	}

}
