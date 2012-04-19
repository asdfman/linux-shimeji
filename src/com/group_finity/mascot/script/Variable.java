package com.group_finity.mascot.script;

import com.group_finity.mascot.exception.VariableException;

public abstract class Variable {
	
	public static Variable parse(final String source) throws VariableException {
		
		Variable result = null;
		
		if ( source!=null ) {
			if ( source.startsWith("${") && source.endsWith("}")) {
				result = new Script(source.substring(2, source.length()-1), false);
			} else if ( source.startsWith("#{") && source.endsWith("}")) {
				result = new Script(source.substring(2, source.length()-1), true);
			} else {
				result = new Constant(parseConstant(source));
			}
		}
		
		return result;
	}

	private static Object parseConstant(final String source) {
		Object result = null;

		if (source != null) {
			if (source.equals("null")) {
				result = null;
			}
			if (source.equals("true")) {
				result = Boolean.TRUE;
			} else if (source.equals("false")) {
				result = Boolean.FALSE;
			} else {
				try {
					result = Double.parseDouble(source);
				} catch (final NumberFormatException e) {
					result = source;
				}
			}
		}

		return result;
	}

	public abstract void init();
	
	public abstract void initFrame();

	public abstract Object get(VariableMap variables) throws VariableException;

}
