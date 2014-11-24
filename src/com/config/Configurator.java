package com.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configurator {
	
	private static Logger LOGGER = Logger.getLogger(Configurator.class);

	/**
	 * Private constructor.
	 */
	private Configurator() {
		throw new UnsupportedOperationException("Class is not instantiable.");
	}

	/**
	 * initialize and get the Configuration
	 * 
	 * @return
	 */
	public static AppConfig getAppConfig() {

		//LOGGER.info("Configurator:getAppConfig(): Configuring the Application credentials .........................");
		
		Properties props = new Properties();
		AppConfig appConfig = new AppConfig();

		try {
			props.load(new FileInputStream("silverlane_exportxml.properties"));

			// SFDC
			appConfig.setEndPoint(props.getProperty("sfdc.endPoint"));
			appConfig.setSfdcUserName(props.getProperty("sfdc.sfdcUsername"));
			appConfig.setSfdcPassword(props.getProperty("sfdc.sfdcPassword"));
			appConfig.setOutputDirectory(props.getProperty("serviceexport.outputDirectory"));
			appConfig.setRestEndPoint(props.getProperty("sfdc.restEndPoint"));
			
		} catch (IOException e) {
			LOGGER.error("Exception while configuring the Application credentials ..." + e);
		}
		return appConfig;
	}


}
