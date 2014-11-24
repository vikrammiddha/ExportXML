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

	public static String getFundInfoXMLHeader(){
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n" + 
				"<serviceexport xmlns=\"http://schemas.angelogordon.com/website/2012/exports/services/1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://schemas.angelogordon.com/website/2012/exports/services/1.0 exportservice.xsd\" version=\"1.3a\">"+
				//"<metadata>\n" +
				"<data>\n";
	}

	public static String getContactLevelServiceXMLHeader(){
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n" + 
				"<fundinfoexport xmlns=\"http://schemas.angelogordon.com/website/2012/exports/funds/1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://schemas.angelogordon.com/website/2012/exports/funds/1.0 exportfundinfo.xsd\" version=\"0.90\">\n" +
				//"<metadata>\n" +
				"<data>\n";
	}

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
	
	public static void getContactServicesForContactLevelService(ArrayList<ContactService> csList, HashMap<String,ArrayList<Service>> serviceMap , AppConfig appConfig, String fileName){
		String s = "";
		
		s = "<services>\n";
		writeToXMLFileForContactLevelServiceExport(s,appConfig,fileName);
		for(ContactService cs: csList){
			if(serviceMap.get(cs.getService()) != null){
				for(Service ser : serviceMap.get(cs.getService())){
					s = "\t<service id=\""+cs.getId()+"\" slxService=\""+cs.getServiceText()+"\" deliveryRule=\""+cs.getDeliveryRule()+ "\" contactid=\""+cs.getContact()+"\" itgCategory=\""+ser.getCategory()+"\"/>\n";
					writeToXMLFileForContactLevelServiceExport(s,appConfig,fileName);
				}
			}else{
				s = "\t<service id=\""+cs.getId()+"\" slxService=\""+cs.getServiceText()+"\" deliveryRule=\""+cs.getDeliveryRule()+ "\" contactid=\""+cs.getContact()+"\" itgCategory=\"\"/>\n";
				writeToXMLFileForContactLevelServiceExport(s,appConfig,fileName);
			}

		}
		s = "</services>\n";
		writeToXMLFileForContactLevelServiceExport(s,appConfig,fileName);
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


	public static void getFundsXMLForFundsInfo(ArrayList<Fund> fundList,AppConfig appConfig, String fileName){

		String s = "";

		for(Fund fund : fundList){
			s = "<fund code=\""+fund.getCode()+"\" id=\""+fund.getId()+"\">\n";
			writeToXMLFileForFundsInfoExport(s,appConfig,fileName);
			writeToXMLFileForFundsInfoExport("<attributes>\n",appConfig,fileName);
			if(!"".equals(fund.getFundEI())){
				writeToXMLFileForFundsInfoExport("<attribute key=\"FUND_EIN\" value=\""+fund.getFundEI()+"\"/>\n",appConfig,fileName);
			}
			if(!"".equals(fund.getItgFamily())){
				writeToXMLFileForFundsInfoExport("<attribute key=\"ITG_FAMILY\" value=\""+fund.getItgFamily()+"\"/>\n",appConfig,fileName);
			}
			if(!"".equals(fund.getType())){
				writeToXMLFileForFundsInfoExport("<attribute key=\"ITG_FUND_TYPE\" value=\""+fund.getType()+"\"/>\n",appConfig,fileName);
			}
			if(!"".equals(fund.getItgTaxLink())){
				writeToXMLFileForFundsInfoExport("<attribute key=\"ITG_TAX_LINK\" value=\""+fund.getItgTaxLink()+"\"/>\n",appConfig,fileName);
			}
			if(!"".equals(fund.getWebValuationColumns())){
				writeToXMLFileForFundsInfoExport("<attribute key=\"ITG_VALUATION_COLUMNS\" value=\""+fund.getWebValuationColumns()+"\"/>\n",appConfig,fileName);
			}
			if(!"".equals(fund.getName())){
				writeToXMLFileForFundsInfoExport("<attribute key=\"NAME\" value=\""+fund.getName()+"\"/>\n",appConfig,fileName);
			}
			if(!"".equals(fund.getItgTaxNote())){
				writeToXMLFileForFundsInfoExport("<attribute key=\"ITG_TAX_NOTE\" value=\""+fund.getItgTaxNote()+"\"/>\n",appConfig,fileName);
			}
			if(!"".equals(fund.getItgObWebsite())){
				writeToXMLFileForFundsInfoExport("<attribute key=\"ITG_ONWEBSITE\" value=\""+fund.getItgObWebsite()+"\"/>\n",appConfig,fileName);
			}

			writeToXMLFileForFundsInfoExport("</attributes>\n</fund>\n",appConfig,fileName);
		}

	}

	public static String getXMLFooter(){
		return "\n</data>\n";
	}

	public static String getXMLFooterForFundExport(){
		String footer = "</data>\n" +
				"</fundinfoexport>";

		return footer;
	}

	private static void writeToXMLFile(String s, AppConfig appConfig, String fileName){
		try {
			FileHandler.write(s, true, appConfig, fileName);
		} catch (Exception e) {
			LOGGER.error("Exception occur while writing to file : ", e);
		}
	}

	private static void writeToXMLFileForFundsInfoExport(String s, AppConfig appConfig, String fileName){
		try {
			FileHandler.write(s, true, appConfig, fileName, appConfig.getFundInfoExportOutputDirectory());
		} catch (Exception e) {
			LOGGER.error("Exception occur while writing to file : ", e);
		}
	}
	
	private static void writeToXMLFileForContactLevelServiceExport(String s, AppConfig appConfig, String fileName){
		try {
			FileHandler.write(s, true, appConfig, fileName, appConfig.getContactLevelServicesOutputDirectory());
		} catch (Exception e) {
			LOGGER.error("Exception occur while writing to file : ", e);
		}
	}

}
