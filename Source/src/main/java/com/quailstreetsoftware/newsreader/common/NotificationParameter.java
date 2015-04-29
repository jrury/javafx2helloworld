package com.quailstreetsoftware.newsreader.common;

public class NotificationParameter {

	public enum ParameterEnum {
		SELECTED_SUBSCRIPTION,
		ITEM_CONTENT,
		DEBUG_MESSAGE,
		THREAD_NAME,
		TIME,
		SUBSCRIPTION_URL,
		ID	
	}

	private ParameterEnum parameterType;
	private Object value;
	
	public NotificationParameter(ParameterEnum paramType, final Object value) {
		this.parameterType = paramType;
		this.value = value;
	}

	public String getStringValue() {
		return (this.value instanceof String ? (String) this.value : null);
	}

	public Object getValue() {
		return this.value;
	}

	public ParameterEnum getParameterType() {
		return this.parameterType;
	}
	
	
}
