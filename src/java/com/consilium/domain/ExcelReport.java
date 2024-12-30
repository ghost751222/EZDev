package com.consilium.domain;

import java.io.Serializable;

public class ExcelReport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1492386346649834042L;
	private String reportKey;
	private String templateFile;
	private String outputName;
	private String reportInstance;
	private String queryPath;
	/**
	 * @return the reportKey
	 */
	public String getReportKey() {
		return reportKey;
	}
	/**
	 * @param reportKey the reportKey to set
	 */
	public void setReportKey(String reportKey) {
		this.reportKey = reportKey;
	}
	/**
	 * @return the templateFile
	 */
	public String getTemplateFile() {
		return templateFile;
	}
	/**
	 * @param templateFile the templateFile to set
	 */
	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}
	/**
	 * @return the outputName
	 */
	public String getOutputName() {
		return outputName;
	}
	/**
	 * @param outputName the outputName to set
	 */
	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}
	/**
	 * @return the reportInstance
	 */
	public String getReportInstance() {
		return reportInstance;
	}
	/**
	 * @param reportInstance the reportInstance to set
	 */
	public void setReportInstance(String reportInstance) {
		this.reportInstance = reportInstance;
	}
	/**
	 * @return the queryPath
	 */
	public String getQueryPath() {
		return queryPath;
	}
	/**
	 * @param queryPath the queryPath to set
	 */
	public void setQueryPath(String queryPath) {
		this.queryPath = queryPath;
	}
	
}
