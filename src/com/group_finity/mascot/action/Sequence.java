package com.group_finity.mascot.action;

import java.util.logging.Logger;

import com.group_finity.mascot.exception.VariableException;
import com.group_finity.mascot.script.VariableMap;

/**
 * 複数のアクションを直列に一つにまとめたアクション.
 */
public class Sequence extends ComplexAction {

	private static final Logger log = Logger.getLogger(Sequence.class.getName());

	public static final String PARAMETER_LOOP = "繰り返し";

	private static final boolean DEFAULT_LOOP = false;

	public Sequence(final VariableMap params, final Action... actions) {
		super(params, actions);
	}

	@Override
	public boolean hasNext() throws VariableException {

		seek();

		return super.hasNext();
	}

	@Override
	protected void setCurrentAction(final int currentAction) throws VariableException {
		super.setCurrentAction(isLoop() ? currentAction % getActions().length : currentAction);
	}

	private Boolean isLoop() throws VariableException {
		return eval(PARAMETER_LOOP, Boolean.class, DEFAULT_LOOP);
	}

}
