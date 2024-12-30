package com.consilium.domain;

import java.io.Serializable;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6974579886980577457L;
	
	private String userId;
	private String account;
	private String password;
	private String userName;
	private String unitType;
	private String unitCode;
	private String station;
	private Integer userGroup;
	private String lastLoginTime;
	private Integer errorRetry;
	private String lockTime;
	private String lastErrorIP;
	private String recommendUnitType;
	private String recommendUnitCode;
	private String recommendID;
	private String caId;
	private String cityCode;
	private String userPhone;
	private String userCellphone;
	private String userAddrCity;
	private String userAddrTown;
	private String userAddr;
	private String userMail;
	private String isActive;
	private String createTime;
	private String removeTime;
	private String remark;
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}
	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the unitType
	 */
	public String getUnitType() {
		return unitType;
	}
	/**
	 * @param unitType the unitType to set
	 */
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	/**
	 * @return the unitCode
	 */
	public String getUnitCode() {
		return unitCode;
	}
	/**
	 * @param unitCode the unitCode to set
	 */
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	/**
	 * @return the station
	 */
	public String getStation() {
		return station;
	}
	/**
	 * @param station the station to set
	 */
	public void setStation(String station) {
		this.station = station;
	}
	/**
	 * @return the userGroup
	 */
	public Integer getUserGroup() {
		return userGroup;
	}
	/**
	 * @param userGroup the userGroup to set
	 */
	public void setUserGroup(Integer userGroup) {
		this.userGroup = userGroup;
	}
	/**
	 * @return the lastLoginTime
	 */
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	/**
	 * @param lastLoginTime the lastLoginTime to set
	 */
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	/**
	 * @return the errorRetry
	 */
	public Integer getErrorRetry() {
		return errorRetry;
	}
	/**
	 * @param errorRetry the errorRetry to set
	 */
	public void setErrorRetry(Integer errorRetry) {
		this.errorRetry = errorRetry;
	}
	/**
	 * @return the lockTime
	 */
	public String getLockTime() {
		return lockTime;
	}
	/**
	 * @param lockTime the lockTime to set
	 */
	public void setLockTime(String lockTime) {
		this.lockTime = lockTime;
	}
	/**
	 * @return the lastErrorIP
	 */
	public String getLastErrorIP() {
		return lastErrorIP;
	}
	/**
	 * @param lastErrorIP the lastErrorIP to set
	 */
	public void setLastErrorIP(String lastErrorIP) {
		this.lastErrorIP = lastErrorIP;
	}
	/**
	 * @return the recommendUnitType
	 */
	public String getRecommendUnitType() {
		return recommendUnitType;
	}
	/**
	 * @param recommendUnitType the recommendUnitType to set
	 */
	public void setRecommendUnitType(String recommendUnitType) {
		this.recommendUnitType = recommendUnitType;
	}
	/**
	 * @return the recommendUnitCode
	 */
	public String getRecommendUnitCode() {
		return recommendUnitCode;
	}
	/**
	 * @param recommendUnitCode the recommendUnitCode to set
	 */
	public void setRecommendUnitCode(String recommendUnitCode) {
		this.recommendUnitCode = recommendUnitCode;
	}
	/**
	 * @return the recommendID
	 */
	public String getRecommendID() {
		return recommendID;
	}
	/**
	 * @param recommendID the recommendID to set
	 */
	public void setRecommendID(String recommendID) {
		this.recommendID = recommendID;
	}
	/**
	 * @return the caId
	 */
	public String getCaId() {
		return caId;
	}
	/**
	 * @param caId the caId to set
	 */
	public void setCaId(String caId) {
		this.caId = caId;
	}
	/**
	 * @return the cityCode
	 */
	public String getCityCode() {
		return cityCode;
	}
	/**
	 * @param cityCode the cityCode to set
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	/**
	 * @return the userPhone
	 */
	public String getUserPhone() {
		return userPhone;
	}
	/**
	 * @param userPhone the userPhone to set
	 */
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	/**
	 * @return the userCellphone
	 */
	public String getUserCellphone() {
		return userCellphone;
	}
	/**
	 * @param userCellphone the userCellphone to set
	 */
	public void setUserCellphone(String userCellphone) {
		this.userCellphone = userCellphone;
	}
	/**
	 * @return the userAddrCity
	 */
	public String getUserAddrCity() {
		return userAddrCity;
	}
	/**
	 * @param userAddrCity the userAddrCity to set
	 */
	public void setUserAddrCity(String userAddrCity) {
		this.userAddrCity = userAddrCity;
	}
	/**
	 * @return the userAddrTown
	 */
	public String getUserAddrTown() {
		return userAddrTown;
	}
	/**
	 * @param userAddrTown the userAddrTown to set
	 */
	public void setUserAddrTown(String userAddrTown) {
		this.userAddrTown = userAddrTown;
	}
	/**
	 * @return the userAddr
	 */
	public String getUserAddr() {
		return userAddr;
	}
	/**
	 * @param userAddr the userAddr to set
	 */
	public void setUserAddr(String userAddr) {
		this.userAddr = userAddr;
	}
	/**
	 * @return the userMail
	 */
	public String getUserMail() {
		return userMail;
	}
	/**
	 * @param userMail the userMail to set
	 */
	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}
	/**
	 * @return the isActive
	 */
	public String getIsActive() {
		return isActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(String isActive) {
		this.isActive = isActive;
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
	 * @return the removeTime
	 */
	public String getRemoveTime() {
		return removeTime;
	}
	/**
	 * @param removeTime the removeTime to set
	 */
	public void setRemoveTime(String removeTime) {
		this.removeTime = removeTime;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
