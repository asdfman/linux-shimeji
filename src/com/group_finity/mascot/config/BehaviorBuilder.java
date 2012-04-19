package com.group_finity.mascot.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.group_finity.mascot.behavior.Behavior;
import com.group_finity.mascot.behavior.UserBehavior;
import com.group_finity.mascot.exception.ActionInstantiationException;
import com.group_finity.mascot.exception.BehaviorInstantiationException;
import com.group_finity.mascot.exception.ConfigurationException;
import com.group_finity.mascot.exception.VariableException;
import com.group_finity.mascot.script.Variable;
import com.group_finity.mascot.script.VariableMap;

public class BehaviorBuilder {

	private static final Logger log = Logger.getLogger(BehaviorBuilder.class.getName());

	private final Configuration configuration;

	private final String name;

	private final String actionName;

	private final int frequency;

	private final List<String> conditions;

	private final boolean nextAdditive;

	private final List<BehaviorBuilder> nextBehaviorBuilders = new ArrayList<BehaviorBuilder>();

	private final Map<String, String> params = new LinkedHashMap<String, String>();

	public BehaviorBuilder(final Configuration configuration, final Entry behaviorNode, final List<String> conditions) {
		this.configuration = configuration;
		this.name = behaviorNode.getAttribute("名前");
		this.actionName = behaviorNode.getAttribute("動作") == null ? getName() : behaviorNode.getAttribute("動作");
		this.frequency = Integer.parseInt(behaviorNode.getAttribute("頻度"));
		this.conditions = new ArrayList<String>(conditions);
		this.getConditions().add(behaviorNode.getAttribute("条件"));

		log.log(Level.INFO, "行動読み込み開始({0})", this);

		this.getParams().putAll(behaviorNode.getAttributes());
		this.getParams().remove("名前");
		this.getParams().remove("動作");
		this.getParams().remove("頻度");
		this.getParams().remove("条件");

		boolean nextAdditive = true;

		for (final Entry nextList : behaviorNode.selectChildren("次の行動リスト")) {

			log.log(Level.INFO, "次の行動リスト...");

			nextAdditive = Boolean.parseBoolean(nextList.getAttribute("追加"));

			loadBehaviors(nextList, new ArrayList<String>());
		}

		this.nextAdditive = nextAdditive;

		log.log(Level.INFO, "行動読み込み完了({0})", this);

	}

	@Override
	public String toString() {
		return "行動(" + getName() + "," + getFrequency() + "," + getActionName() + ")";
	}

	private void loadBehaviors(final Entry list, final List<String> conditions) {

		for (final Entry node : list.getChildren()) {

			if (node.getName().equals("条件")) {

				final List<String> newConditions = new ArrayList<String>(conditions);
				newConditions.add(node.getAttribute("条件"));

				loadBehaviors(node, newConditions);

			} else if (node.getName().equals("行動参照")) {
				final BehaviorBuilder behavior = new BehaviorBuilder(getConfiguration(), node, conditions);
				getNextBehaviorBuilders().add(behavior);
			}
		}
	}

	public void validate() throws ConfigurationException {

		if ( !getConfiguration().getActionBuilders().containsKey(getActionName()) ) {
			throw new ConfigurationException("対応する動作が存在しません("+this+")");
		}
	}

	public Behavior buildBehavior() throws BehaviorInstantiationException {

		try {
			return new UserBehavior(getName(),
						getConfiguration().buildAction(getActionName(),
								getParams()), getConfiguration() );
		} catch (final ActionInstantiationException e) {
			throw new BehaviorInstantiationException("対応する動作の初期化に失敗しました("+this+")", e);
		}
	}


	public boolean isEffective(final VariableMap context) throws VariableException {

		for (final String condition : getConditions()) {
			if (condition != null) {
				if (!(Boolean) Variable.parse(condition).get(context)) {
					return false;
				}
			}
		}

		return true;
	}

	String getName() {
		return this.name;
	}

	int getFrequency() {
		return this.frequency;
	}

	private String getActionName() {
		return this.actionName;
	}

	private Map<String, String> getParams() {
		return this.params;
	}

	private List<String> getConditions() {
		return this.conditions;
	}

	private Configuration getConfiguration() {
		return this.configuration;
	}

	boolean isNextAdditive() {
		return this.nextAdditive;
	}

	List<BehaviorBuilder> getNextBehaviorBuilders() {
		return this.nextBehaviorBuilders;
	}
}
