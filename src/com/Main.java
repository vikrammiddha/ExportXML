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
		mainObj.initiateExportProcess();
		
	}
	
	public void initiateExportProcess(){
		XMLUtils obj;
		try {
			obj = new XMLUtils(appConfig);
			obj.generateXMLFiles();
		} catch (Exception e) {
			LOGGER.error("Exception in initiating the export process :" + e.getCause());
		}
		
	}

}