package com.group_finity.mascot.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.group_finity.mascot.action.Action;
import com.group_finity.mascot.exception.ActionInstantiationException;
import com.group_finity.mascot.exception.ConfigurationException;

public class ActionRef implements IActionBuilder {

	private static final Logger log = Logger.getLogger(ActionRef.class.getName());

	private final Configuration configuration;

	private final String name;

	private final Map<String, String> params = new LinkedHashMap<String, String>();

	public ActionRef(final Configuration configuration, final Entry refNode) {
		this.configuration = configuration;

		this.name = refNode.getAttribute("名前");
		this.getParams().putAll(refNode.getAttributes());
		log.log(Level.INFO, "動作参照読み込み({0})", this);

	// Convert every non-jump ActionReference to multiwindow format.
	// Necessary for using 'standard' configurations. 
		if (!name.contains("ジャンプ")) {
			String s = params.get("目的地X");
			if (s != null) {
				if (s.contains("environment.activeIE")) {
					s = s.replaceAll("environment.activeIE","curIE");
					params.put("目的地X",s);
				}
			}
			s = params.get("目的地Y");
			if (s != null) {
				if (s.contains("environment.activeIE")) {
					s = s.replaceAll("environment.activeIE","curIE");
					params.put("目的地Y",s);
				}
			}
			s = params.get("条件");
			if (s != null) {
				if (s.contains("environment.activeIE")) {
					s = s.replaceAll("environment.activeIE","curIE");
					params.put("目的地X",s);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "動作参照(" + getName() + ")";
	}

	private String getName() {
		return this.name;
	}

	private Map<String, String> getParams() {
		return this.params;
	}

	private Configuration getConfiguration() {
		return this.configuration;
	}

	@Override
	public void validate() throws ConfigurationException {
		if (!getConfiguration().getActionBuilders().containsKey(getName())) {
			throw new ConfigurationException("対応する動作が存在しません(" + this + ")");
		}
	}

	public Action buildAction(final Map<String, String> params) throws ActionInstantiationException {
		final Map<String, String> newParams = new LinkedHashMap<String, String>(params);
		newParams.putAll(getParams());
		return this.getConfiguration().buildAction(getName(), newParams);
	}
}
