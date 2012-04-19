package com.group_finity.mascot.script;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;

import com.group_finity.mascot.exception.VariableException;

public class VariableMap extends AbstractMap<String, Object> implements Bindings {

	private final Map<String, Variable> rawMap = new LinkedHashMap<String, Variable>();

	public Map<String, Variable> getRawMap() {
		return this.rawMap;
	}

	public void init() {
		for (final Variable o : this.getRawMap().values()) {
			o.init();
		}
	}

	public void initFrame() {
		for (final Variable o : this.getRawMap().values()) {
			o.initFrame();
		}
	}

	private final Set<Map.Entry<String, Object>> entrySet = new AbstractSet<Entry<String, Object>>() {

		@Override
		public Iterator<Map.Entry<String, Object>> iterator() {

			return new Iterator<Entry<String, Object>>() {

				private Iterator<Map.Entry<String, Variable>> rawIterator = VariableMap.this.getRawMap().entrySet()
						.iterator();

				@Override
				public boolean hasNext() {
					return this.rawIterator.hasNext();
				}

				@Override
				public Map.Entry<String, Object> next() {
					final Map.Entry<String, Variable> rawKeyValue = this.rawIterator.next();
					final Object value = rawKeyValue.getValue();

					return new Map.Entry<String, Object>() {

						@Override
						public String getKey() {
							return rawKeyValue.getKey();
						}

						@Override
						public Object getValue() {
							try {
								return ((Variable) value).get(VariableMap.this);
							} catch (final VariableException e) {
								throw new RuntimeException(e);
							}
						}

						@Override
						public Object setValue(final Object value) {
							throw new UnsupportedOperationException("setValue is not supported");
						}

					};
				}

				@Override
				public void remove() {
					this.rawIterator.remove();
				}

			};
		}

		@Override
		public int size() {
			return VariableMap.this.getRawMap().size();
		}

	};

	@Override
	public Set<Map.Entry<String, Object>> entrySet() {
		return this.entrySet;
	}

	@Override
	public Object put(final String key, final Object value) {
		Object result;
		
		if (value instanceof Variable) {
			result = this.getRawMap().put(key, (Variable)value);
		} else {
			result = this.getRawMap().put(key, new Constant(value));
		}

		return result;

	}

}
