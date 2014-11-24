package com.config;

public final class AppConfig {
	
	
	private String sfdcUserName;
	private String sfdcPassword;
	private String endPoint;
	private String outputDirectory;
	private String restEndPoint;
	private String fundInfoExportOutputDirectory;
	private String contactLevelServicesOutputDirectory;
	
	public String getRestEndPoint() {
		return restEndPoint;
	}
	public void setRestEndPoint(String restEndPoint) {
		this.restEndPoint = restEndPoint;
	}
	
	public String getOutputDirectory() {
		return outputDirectory;
	}
	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	public String getSfdcUserName() {
		return sfdcUserName;
	}
	public void setSfdcUserName(String sfdcUserName) {
		this.sfdcUserName = sfdcUserName;
	}
	public String getSfdcPassword() {
		return sfdcPassword;
	}
	public void setSfdcPassword(String sfdcPassword) {
		this.sfdcPassword = sfdcPassword;
	}
	public String getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
	public String getFundInfoExportOutputDirectory() {
		return fundInfoExportOutputDirectory;
	}
	public void setFundInfoExportOutputDirectory(
			String fundInfoExportOutputDirectory) {
		this.fundInfoExportOutputDirectory = fundInfoExportOutputDirectory;
	}
	public String getContactLevelServicesOutputDirectory() {
		return contactLevelServicesOutputDirectory;
	}
	public void setContactLevelServicesOutputDirectory(
			String contactLevelServicesOutputDirectory) {
		this.contactLevelServicesOutputDirectory = contactLevelServicesOutputDirectory;
	}
	
	
	
}
