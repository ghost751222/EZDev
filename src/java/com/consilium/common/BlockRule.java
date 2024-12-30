package com.consilium.common;

public enum BlockRule {
	DirectHandup (1),
	AfterPlayinghandup (2),
	TransferSpecificAgent(3),
	LowPriorityAssign (4),
	HighPriorityAssign(5);
	
	private final int ruleValue;
	private BlockRule (int value) {
		this.ruleValue = value;
	}
	public int getBlockRule() {
		return ruleValue;
	}
}
