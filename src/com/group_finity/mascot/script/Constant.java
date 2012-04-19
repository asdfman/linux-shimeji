package com.group_finity.mascot.script;

public class Constant extends Variable {

	private final Object value;

	public Constant(final Object value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return this.value==null ? "null" : this.value.toString();
	}

	private Object getValue() {
		return this.value;
	}
	
	@Override
	public void init() {
	}

	@Override
	public void initFrame() {
	}

	@Override
	public Object get(final VariableMap variables) {
		return getValue();
	}

}
