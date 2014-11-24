package Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.config.AppConfig;

import bean.Account;
import bean.Contact;
import bean.ContactService;
import bean.Fund;
import bean.Organisation;
import bean.Service;

public class XMLHandler {

	private static final Logger LOGGER = Logger.getLogger(XMLHandler.class);

	public static String getXMLHeader(){
		
		return	"<?xml version=\"1.0\" encoding=\"UTF-8\"  standalone=\"no\" ?>\n"+
		"<data>\n";
	}

	public static void getFundsXML(Fund f, AppConfig appConfig, String fileName){
		String s = "";
		s = "<funds>\n";
		writeToXMLFile(s,appConfig,fileName);
		s = "\t<fund  id=\"" +f.getId() +"\" code=\""+f.getCode()+"\" />\n";
		writeToXMLFile(s,appConfig,fileName);
		s = "</funds>\n";
		writeToXMLFile(s,appConfig,fileName);
		//return retString;
	}

	public static void getCSXML(ArrayList<ContactService> csList, HashMap<String,ArrayList<Service>> serviceMap , AppConfig appConfig, String fileName){
		String s = "";
		Integer count = 1;

		s = "<services>\n";
		writeToXMLFile(s,appConfig,fileName);
		for(ContactService cs: csList){
			if(serviceMap.get(cs.getService()) != null){
				for(Service ser : serviceMap.get(cs.getService())){
					s = "\t<service id=\""+cs.getId()+"\" iakey=\""+cs.getFundCode() + "-" + cs.getInvestorAccountCode()+"\" fundCode=\""+cs.getFundCode()+"\" accountCode=\""+cs.getInvestorAccountCode()+"\" slxService=\""+cs.getServiceText()+"\" deliveryRule=\"\" contactid=\""+cs.getContact()+"\" accountid=\""+cs.getAccountId()+ "\" itgFundCode=\""+ser.getFundCode()+"\" itgCategory=\""+ser.getCategory()+"\"/>\n";
					writeToXMLFile(s,appConfig,fileName);
					count++;
				}
			}else{
				s = "\t<service id=\""+cs.getId()+"\" iakey=\""+cs.getFundCode() + "-" + cs.getInvestorAccountCode()+"\" fundCode=\""+cs.getFundCode()+"\" accountCode=\""+cs.getInvestorAccountCode()+"\" slxService=\""+cs.getServiceText()+"\" deliveryRule=\"\" contactid=\""+cs.getContact()+"\" accountid=\""+cs.getAccountId()+ "\" itgFundCode=\"\" itgCategory=\"\" />\n";
				writeToXMLFile(s,appConfig,fileName);
				count++;
			}
			
		}
		s = "</services>\n";
		writeToXMLFile(s,appConfig,fileName);
	}

	public static void getOrgXML(ArrayList<Organisation> orgList , AppConfig appConfig, String fileName){
		String s = "";

		Integer count = 1;
		s = "<orgs>\n";
		writeToXMLFile(s,appConfig,fileName);

		for(Organisation org : orgList){
			s = "\t<org id=\"" + org.getId()+"\" name=\""+org.getName()+"\"/>\n";
			writeToXMLFile(s,appConfig,fileName);
			count++;
		}
		s = "</orgs>\n";
		writeToXMLFile(s,appConfig,fileName);
	}

	public static void getAccountsXML(ArrayList<Account> accList , AppConfig appConfig, String fileName){
		String s = "";

		Integer count = 1;
		s = "<accounts>\n";
		writeToXMLFile(s,appConfig,fileName);

		for(Account acc : accList){
			s = "\t<account id=\""+acc.getId()+"\" code=\""+acc.getCode()+"\" name=\""+acc.getName()+"\" status=\""+acc.getStatus()+ "\" orgid=\""+acc.getOrganisation()+"\" />\n";
			writeToXMLFile(s,appConfig,fileName);
			count++;
		}
		
		s = "</accounts>\n";
		writeToXMLFile(s,appConfig,fileName);
	}
	
	public static void getContactsXML(ArrayList<Contact> conList , AppConfig appConfig, String fileName){
		String s = "";
		
		Integer count = 1;
		s = "<contacts>\n";
		writeToXMLFile(s,appConfig,fileName);
		
		for(Contact con : conList){
			s = "\t<contact id=\""+con.getId()+"\" email=\""+con.getEmail()+"\" orgid=\""+con.getOrgId()+"\" lastName=\""+con.getLastName()+"\" firstName=\""+con.getFirstName()+"\" prefix=\""+con.getPrefix()+"\" salutation=\""+con.getSalutation()+"\" formattedSalutation=\""+con.getFormattedSalutation()+",\" formattedAddress=\""+con.getFormattedAddress()+"\" address1=\""+con.getAddress1()+"\" city=\""+con.getCity()+"\" state=\""+con.getState()+"\" postalCode=\""+con.getPostalCode()+"\" />\n";
			writeToXMLFile(s,appConfig,fileName);
			count++;
		}
		
		s = "</contacts>\n";
		writeToXMLFile(s,appConfig,fileName);
	}

	public static String getXMLFooter(){
		return "\n</data>\n";
	}
	
	private static void writeToXMLFile(String s, AppConfig appConfig, String fileName){
		try {
			FileHandler.write(s, true, appConfig, fileName);
		} catch (Exception e) {
			LOGGER.error("Exception occur while writing to file : ", e);
		}
	}

}
