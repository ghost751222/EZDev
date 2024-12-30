package com.consilium.domain;

import java.io.Serializable;
public class LineFileLine implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2423527770568148937L;
	private int lineSeq;
	private String previewFileName;
	private String preview;
	private String content;
	private String createTime;
	private String lastUpdateTime;
	private String fileName;
	/**
	 * @return the lineSeq
	 */
	public int getLineSeq() {
		return lineSeq;
	}
	/**
	 * @param lineSeq the lineSeq to set
	 */
	public void setLineSeq(int lineSeq) {
		this.lineSeq = lineSeq;
	}
	/**
	 * @return the previewFileName
	 */
	public String getPreviewFileName() {
		return previewFileName;
	}
	/**
	 * @param previewFileName the previewFileName to set
	 */
	public void setPreviewFileName(String previewFileName) {
		this.previewFileName = previewFileName;
	}
	/**
	 * @return the preview
	 */
	public String getPreview() {
		return preview;
	}
	/**
	 * @param preview the preview to set
	 */
	public void setPreview(String preview) {
		this.preview = preview;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the lastUpdateTime
	 */
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	/**
	 * @param lastUpdateTime the lastUpdateTime to set
	 */
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
