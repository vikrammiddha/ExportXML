package Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import bean.Account;
import bean.Contact;
import bean.ContactService;
import bean.Fund;
import bean.Investment;
import bean.Organisation;
import bean.Service;

import com.config.AppConfig;


public class XMLUtils {

	private String outputFileName = null;
	private AppConfig appConfig = null;
	private SFDCHandler sfdcHandler = null;
	private int fundNumber = 1;
	
	ArrayList<Fund> fundList = new ArrayList<Fund>();
	ArrayList<ContactService> csList = new ArrayList<ContactService>();
	ArrayList<Organisation> orgList = new ArrayList<Organisation>();
	ArrayList<Account> accList = new ArrayList<Account>();
	ArrayList<Contact> conList = new ArrayList<Contact>();
	ArrayList<Investment> investmentList = new ArrayList<Investment>();
	
	HashMap<String,ArrayList<Service>> serviceMap = new HashMap<String,ArrayList<Service>>();

	private static final Logger LOGGER = Logger.getLogger(XMLUtils.class);

	String header = "";
	String footer = "";
	String funds = "";
	String org = "";
	String cs = "";
	String acc = "";
	String con = "";

	public XMLUtils(){

	}

	public XMLUtils(AppConfig appConfig) throws Exception{
		this.appConfig = appConfig;
		sfdcHandler = new SFDCHandler(appConfig);
		header = XMLHandler.getXMLHeader();
		footer = XMLHandler.getXMLFooter();
	}

	public void generateServiceExportXMLFiles(){
		

		ArrayList<Fund> fundList = new ArrayList<Fund>();
		try {
			fundList = sfdcHandler.getFunds();
		} catch (Exception e1) {
			LOGGER.error("Error occured while retreiving Funds from Salesforce. Cause :" + e1.getCause());
		}

		for(Fund f : fundList){
			try {
				createXMLFile(f);
			} catch (Exception e) {
				LOGGER.error("Error occured while generating File for Fund :" + f.getId());
			}
		}

	}

	private void createXMLFile(Fund fund) throws Exception{
		
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss") ;
		outputFileName = fund.getId() + "_ExportXML_" + dateFormat.format(date);
		
		writeToXMLFile(header, outputFileName);
		
		LOGGER.debug("Processing Fund Number : " + fundNumber + ",  Id : " + fund.getId());
		fundNumber++;
		
		XMLUtils xmlObj = new XMLUtils();
		
		XMLHandler.getFundsXML(fund, appConfig, outputFileName);
		
		sfdcHandler.populateData(fund, xmlObj, serviceMap);
		
		XMLHandler.getOrgXML(xmlObj.orgList, appConfig, outputFileName);
		
		XMLHandler.getAccountsXML(xmlObj.accList, appConfig, outputFileName);
		
		XMLHandler.getContactsXML(xmlObj.conList, appConfig, outputFileName);
		
		XMLHandler.getCSXML(xmlObj.csList, serviceMap ,appConfig, outputFileName);
		
		//String xmlString = header + funds + org + acc + con + cs + footer;

		writeToXMLFile(footer, outputFileName);
		
		LOGGER.debug("Writing to File Completed.....");
		
		serviceMap.clear();
	}
	
	
	public void generateFundInfoXMLFiles(){
		ArrayList<Fund> fundList = new ArrayList<Fund>();
		try {
			fundList = sfdcHandler.getFundsForFundInfoExport();
		} catch (Exception e1) {
			LOGGER.error("Error occured while retreiving Funds for Funds Info Export from Salesforce. Cause :" + e1.getCause() + e1.getMessage() + e1.getStackTrace());
		}

		try {
			createXMLFileForFundExport(fundList);
		} catch (Exception e) {
			LOGGER.error("Error occured while generating File for Fund " +  e.getCause() + e.getMessage());
		}
	}
	
	private void createXMLFileForFundExport(ArrayList<Fund> fundList){
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss") ;
		outputFileName =  "-sp_web_exportfundinfo-" + dateFormat.format(date);
		
		String header = XMLHandler.getFundInfoXMLHeader();
		writeToXMLFileForFundsInfoExport(header, outputFileName);
		
		XMLHandler.getFundsXMLForFundsInfo(fundList, appConfig, outputFileName);
		
		String footer = XMLHandler.getXMLFooterForFundExport();
		
		writeToXMLFileForFundsInfoExport(footer, outputFileName);
		
	}
	
	
	public void generateContactLevelServicesXMLFiles(){
		XMLUtils xmlObj = new XMLUtils();
		try {
			sfdcHandler.populateData(null, xmlObj, serviceMap);
		} catch (Exception e1) {
			LOGGER.error("Error occured while retreiving Funds for Funds Info Export from Salesforce. Cause :" + e1.getCause() + e1.getMessage() + e1.getStackTrace());
		}

		try {
			createXMLFileForContactLevelService(xmlObj);
		} catch (Exception e) {
			LOGGER.error("Error occured while generating File for Fund " +  e.getCause() + e.getMessage());
		}
	}
	
	private void createXMLFileForContactLevelService(XMLUtils xmlObj){
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss") ;
		outputFileName =  "-sp_web_exportcontactlevelservices-" + dateFormat.format(date);
		
		String header = XMLHandler.getContactLevelServiceXMLHeader();
		writeToXMLFileForContactLevelServiceExport(header, outputFileName);
		
		XMLHandler.getContactServicesForContactLevelService(xmlObj.csList, serviceMap, appConfig, outputFileName);
		
		String footer = XMLHandler.getXMLFooterForFundExport();
		
		writeToXMLFileForContactLevelServiceExport(footer, outputFileName);
		
	}
	
	
	public void generateValuationXMLFiles(){
		XMLUtils xmlObj = new XMLUtils();
		try {
			sfdcHandler.populateInvestmentData(xmlObj);
		} catch (Exception e1) {
			LOGGER.error("Error occured while retreiving Investment data Export from Salesforce. Cause :" + e1.getCause() + e1.getMessage() + e1.getStackTrace());
		}

		try {
			createXMLFileForValuations(xmlObj);
		} catch (Exception e) {
			LOGGER.error("Error occured while generating File for Fund " +  e.getCause() + e.getMessage());
		}
	}
	
	private void createXMLFileForValuations(XMLUtils xmlObj){
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss") ;
		outputFileName =  "-sp_web_exportvaluation-" + dateFormat.format(date);
		
		String header = XMLHandler.getValuationXMLHeader();
		writeToXMLFileForValuationExport(header, outputFileName);
		
		XMLHandler.getInvestments(xmlObj.investmentList, appConfig, outputFileName);
		
		String footer = XMLHandler.getXMLFooterForValuation();
		
		writeToXMLFileForValuationExport(footer, outputFileName);
		
	}
	
	
	

	private  void writeToXMLFile(String s, String fileName){
		try {
			FileHandler.write(s, true, appConfig, fileName);
		} catch (Exception e) {
			LOGGER.error("Exception occur while writing to file : ", e);
		}
	}
	
	private void writeToXMLFileForFundsInfoExport(String s, String fileName){
		try {
			FileHandler.write(s, true, appConfig, fileName, appConfig.getFundInfoExportOutputDirectory());
		} catch (Exception e) {
			LOGGER.error("Exception occur while writing to file : ", e);
		}
	}

	private void writeToXMLFileForContactLevelServiceExport(String s, String fileName){
		try {
			FileHandler.write(s, true, appConfig, fileName, appConfig.getContactLevelServicesOutputDirectory());
		} catch (Exception e) {
			LOGGER.error("Exception occur while writing to file : ", e);
		}
	}
	
	private void writeToXMLFileForValuationExport(String s, String fileName){
		try {
			FileHandler.write(s, true, appConfig, fileName, appConfig.getValuationOutputDirectory());
		} catch (Exception e) {
			LOGGER.error("Exception occur while writing to file : ", e);
		}
	}


}
