package Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import bean.ContactService;
import bean.Fund;
import bean.Organisation;

import com.config.AppConfig;


public class XMLUtils {
	
	private StringBuilder sb = null;
	private String outputFileName = null;
	private AppConfig appConfig = null;
	private SFDCHandler sfdcHandler = null;
	ArrayList<Fund> fundList = new ArrayList<Fund>();
	ArrayList<ContactService> csList = new ArrayList<ContactService>();

	private static final Logger LOGGER = Logger.getLogger(XMLUtils.class);
	
	String header = "";
	String footer = "";
	String funds = "";
	String org = "";

	public XMLUtils(){

	}

	public XMLUtils(AppConfig appConfig) throws Exception{
		sb = new StringBuilder();
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
		outputFileName = "ExportXML_" + dateFormat.format(date);
		this.appConfig = appConfig;
		sfdcHandler = new SFDCHandler(appConfig);
		header = XMLHandler.getXMLHeader();
		footer = XMLHandler.getXMLFooter();
	}

	public void generateXMLFile() throws Exception{
		XMLUtils xmlObj = new XMLUtils();
		
		sfdcHandler.populateData(xmlObj);
		
		funds = XMLHandler.getFundsXML(xmlObj.fundList);
		
		String xmlString = header + funds + footer;

		writeToXMLFile(xmlString);
	}

	private  void writeToXMLFile(String s){
		try {
			FileHandler.write(s, true, appConfig, outputFileName);
		} catch (Exception e) {
			LOGGER.error("Exception occur while writing to file : ", e);
		}
	}

	


}
