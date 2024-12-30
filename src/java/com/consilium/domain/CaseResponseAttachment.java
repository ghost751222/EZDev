package com.consilium.domain;

import java.io.Serializable;

public class CaseResponseAttachment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6567654572454080415L;
	private int fileNo;
	private int responseNo;
	private String fileName;
	private int fileSize;
	private String fileContent;
	private String fileType;
	public int getFileNo() {
		return fileNo;
	}
	public void setFileNo(int fileNo) {
		this.fileNo = fileNo;
	}
	public int getResponseNo() {
		return responseNo;
	}
	public void setResponseNo(int responseNo) {
		this.responseNo = responseNo;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileContent() {
		return fileContent;
	}
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
