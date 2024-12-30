package com.consilium.domain;

import java.io.Serializable;

public class BBPostAttachement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 305593886935630415L;

		private int iD;
		private int postId;
		private String fileName;
		private int fileSize;
		private String fileContent;
		/**
		 * @return the iD
		 */
		public int getiD() {
			return iD;
		}
		/**
		 * @param iD the iD to set
		 */
		public void setiD(int iD) {
			this.iD = iD;
		}
		/**
		 * @return the postId
		 */
		public int getPostId() {
			return postId;
		}
		/**
		 * @param postId the postId to set
		 */
		public void setPostId(int postId) {
			this.postId = postId;
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
		/**
		 * @return the fileSize
		 */
		public int getFileSize() {
			return fileSize;
		}
		/**
		 * @param fileSize the fileSize to set
		 */
		public void setFileSize(int fileSize) {
			this.fileSize = fileSize;
		}
		/**
		 * @return the fileContent
		 */
		public String getFileContent() {
			return fileContent;
		}
		/**
		 * @param fileContent the fileContent to set
		 */
		public void setFileContent(String fileContent) {
			this.fileContent = fileContent;
		}
		
}
