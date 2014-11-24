package com;

import Utils.XMLUtils;

import com.config.AppConfig;
import com.config.Configurator;
import com.sforce.soap.partner.PartnerConnection;
import org.apache.log4j.Logger;




public class Main {

	private static AppConfig appConfig = null;
	static PartnerConnection connection;
	
	private static final Logger LOGGER = Logger.getLogger(Main.class);
	
	public Main(){
		appConfig = Configurator.getAppConfig();
	}

	public static void main(String[] args) {
		
		Main mainObj = new Main();
		LOGGER.debug("=====Starting Service Info Export Process=====");
		//mainObj.initiateServiceExportProcess();
		LOGGER.debug("=====Service Export Process Finished.======");
		
		LOGGER.debug("=====Starting Funds Info Export Process=====");
		//mainObj.initiateFundExportProcess();
		LOGGER.debug("=====Funds Info Export Process Finished=====");
		
		LOGGER.debug("=====Starting Contact Level Services Process=====");
		mainObj.initiateContactLevelServicesProcess();
		LOGGER.debug("=====Funds Contact Level Service Process Finished=====");
		
	}
	
	public void initiateServiceExportProcess(){
		XMLUtils obj;
		try {
			obj = new XMLUtils(appConfig);
			obj.generateServiceExportXMLFiles();
		} catch (Exception e) {
			LOGGER.error("Exception in initiating the export process :" + e.getCause());
		}
		
	}
	
	public void initiateFundExportProcess(){
		XMLUtils obj;
		try {
			obj = new XMLUtils(appConfig);
			obj.generateFundInfoXMLFiles();
		} catch (Exception e) {
			LOGGER.error("Exception in initiating the fund info process :" + e.getCause());
		}
		
	}
	
	public void initiateContactLevelServicesProcess(){
		XMLUtils obj;
		try {
			obj = new XMLUtils(appConfig);
			obj.generateContactLevelServicesXMLFiles();
		} catch (Exception e) {
			LOGGER.error("Exception in initiating the contact level servicess process :" + e.getCause());
		}
		
	}

}