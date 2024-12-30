package com.consilium.excel.models.caseresponse;

public class CaseResponseDataObject {


	//未回報案號
	protected String actionId1 = "";
	//未回報接收時間
	protected String receiveTime1 = "";

	protected String actionId = "";
	
	protected String receiveTime = "";
	
	protected String responseTime = "";
	
	public void setActionId(String actionId)
	{
		this.actionId = actionId;
	}
	
	public void setReceiveTime(String receiveTime)
	{
		this.receiveTime = receiveTime;
	}
	
	public void setResponseTime(String responseTime)
	{
		this.responseTime = responseTime;
	}
	
	public String getActionId()
	{
		return this.actionId;
	}
	
	public String getReceiveTime()
	{
		return this.receiveTime;
	}
	
	public String getResponseTime()
	{
		return this.responseTime;
	}

	public String getActionId1() {
		return actionId1;
	}

	public void setActionId1(String actionId1) {
		this.actionId1 = actionId1;
	}

	public String getReceiveTime1() {
		return receiveTime1;
	}

	public void setReceiveTime1(String receiveTime1) {
		this.receiveTime1 = receiveTime1;
	}
}
