package Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;

import bean.Account;
import bean.Contact;
import bean.ContactService;
import bean.Fund;
import bean.Investment;
import bean.Organisation;
import bean.Service;

import com.config.AppConfig;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.bind.XmlObject;



public class SFDCHandler {

	private PartnerConnection connection;
	private static final Logger LOGGER = Logger.getLogger(SFDCHandler.class);

	public SFDCHandler(){

	}

	public SFDCHandler(AppConfig appConfig) throws Exception{
		ConnectorConfig config = new ConnectorConfig();
		config.setUsername(appConfig.getSfdcUserName());
		config.setPassword(appConfig.getSfdcPassword());
		config.setAuthEndpoint(appConfig.getEndPoint());
		//config.setCompression(true);
		//config.setTraceFile("traceLogs.txt");
		//config.setTraceMessage(true);
		//config.setPrettyPrintXml(true);

		try{
			connection = Connector.newConnection(config);
			/*String soapEndpoint = config.getServiceEndpoint();
	        String apiVersion = "32.0";
	        String restEndpoint = soapEndpoint.substring(0, soapEndpoint.indexOf("Soap/"))
	            + "async/" + apiVersion;
			config.setRestEndpoint(restEndpoint);
			bulkConnection = new BulkConnection(config);*/
			LOGGER.debug("Auth EndPoint: "+config.getAuthEndpoint());
			//LOGGER.info("Auth EndPoint: "+config.getAuthEndpoint());
			LOGGER.debug("Service EndPoint: "+config.getServiceEndpoint());
			LOGGER.debug("Username: "+config.getUsername());
			LOGGER.debug("SessionId: "+config.getSessionId());
		}catch(Exception e){
			LOGGER.error("Exception while connecting to Salesforce :" + e.getMessage());
			throw new Exception(e);
		}
	}

	public ArrayList<Fund> getFunds() throws Exception{

		LOGGER.debug("Querying Funds.....");

		ArrayList<Fund> fundList = new ArrayList<Fund>();

		/*LOGGER.debug("Quering lastExecutedDate...");
		String lastExecutedDate = "";
		QueryResult custSettingResult = connection.query("Select Last_Exported_Time__c FROM XML_Export__c limit 1");
		if (custSettingResult.getSize() > 0) {
			lastExecutedDate = (String)custSettingResult.getRecords()[0].getField("Last_Exported_Time__c");
			lastExecutedDate = lastExecutedDate.split("-")[0] + "-" + lastExecutedDate.split("-")[2].split("T")[0] + "-" +
					lastExecutedDate.split("-")[1] + "T" + lastExecutedDate.split("-")[2].split("T")[1];
			LOGGER.debug("lastExecutedDate : " + lastExecutedDate);
		}*/

		String fundsQuery = "SELECT Id,Fund_Code__c  FROM Fund__c WHERE Id IN ('a01J000000dK7blIAC', 'a01J000000dK7bgIAC', 'a01J000000dK7bwIAC')";
		//String fundsQuery = "SELECT Id,Fund_Code__c  FROM Fund__c where recordtype.name = 'Fund'";

		QueryResult queryResults = connection.query(fundsQuery);
		LOGGER.debug("Total Funds retreived : " + queryResults.getSize());
		boolean done = false;
		if (queryResults.getSize() > 0) {
			while (!done) {
				ArrayList<String> fundCodeIds = new ArrayList<String>();

				for (SObject s: queryResults.getRecords()) {
					Fund f = new Fund();
					f.setId(s.getId());
					f.setCode(checkForNull((String)s.getField("Fund_Code__c")));
					fundList.add(f);
					fundCodeIds.add(s.getId());

				}

				if (queryResults.isDone()) {
					done = true;
				} else {
					queryResults = connection.queryMore(queryResults.getQueryLocator());
				}
			}

		}

		return fundList ;

	}


	public ArrayList<Fund> getFundsForFundInfoExport() throws Exception{

		LOGGER.debug("Querying Funds.....");

		ArrayList<Fund> fundList = new ArrayList<Fund>();

		//String fundsQuery = "SELECT Id,Fund_Code__c  FROM Fund__c WHERE Id IN ('a01J000000dK7blIAC', 'a01J000000dK7bgIAC', 'a01J000000dK7bwIAC')";
		String fundsQuery = "SELECT Id,Fund_Code__c, Fund_EIN__c,ITG_FAMILY__c,Type__c,ITG_TAX_LINK__c,Web_Valuation_Columns__c,Name"
				+ "  ,ITG_TAX_NOTE__c,ITG_ONWEBSITE__c FROM Fund__c where recordtype.name = 'Fund' and Status__c = 'Active' and ITG_ONWEBSITE__c = true";

		try{
			QueryResult queryResults = connection.query(fundsQuery);
			LOGGER.debug("Total Funds retreived : " + queryResults.getSize());
			boolean done = false;
			if (queryResults.getSize() > 0) {
				while (!done) {
					ArrayList<String> fundCodeIds = new ArrayList<String>();

					for (SObject s: queryResults.getRecords()) {
						Fund f = new Fund();
						f.setId(s.getId());
						f.setCode(checkForNull((String)s.getField("Fund_Code__c")));
						f.setFundEI(checkForNull((String)s.getField("Fund_EIN__c")));
						f.setItgFamily(checkForNull((String)s.getField("ITG_FAMILY")));
						f.setType(checkForNull((String)s.getField("Type__c")));
						f.setItgTaxLink(checkForNull((String)s.getField("ITG_TAX_LINK__c")));
						f.setWebValuationColumns(checkForNull((String)s.getField("Web_Valuation_Columns__c")));
						f.setName(checkForNull((String)s.getField("Name")));
						f.setItgTaxNote(checkForNull((String)s.getField("ITG_TAX_NOTE__c")));
						f.setItgObWebsite(checkForNull((String)s.getField("ITG_ONWEBSITE__c")));

						fundList.add(f);
						fundCodeIds.add(s.getId());

					}

					if (queryResults.isDone()) {
						done = true;
					} else {
						queryResults = connection.queryMore(queryResults.getQueryLocator());
					}
				}

			}

		}catch(Exception e){
			throw new Exception(e);
		}

		return fundList ;

	}

	public void populateInvestmentData(XMLUtils obj) throws Exception{

		ArrayList<Investment> invList = new ArrayList<Investment>();

		try{

			QueryResult invqueryResults = connection.query("SELECT Id,GL_Date__c,Currency__c,Prior_NAV__c,NAV__c,Fund_Code__c,Account_Code__c"
					+ ", IA_Key__c, Investor_Account__c, MTD__c, QTD__c, YTD__c, IRR__c, Total_Committed__c, Total_Called__c"
					+ ", Total_Distributions__c, Total_Transferred__c FROM Investment__c");

			LOGGER.debug("Total Investments retreived : " + invqueryResults.getSize());

			if(invqueryResults.getSize() > 0){

				Boolean done = false;
				while (!done) {

					for (SObject s: invqueryResults.getRecords()) {

						Investment inv = new Investment();
						inv.setAccountCode(checkForNull((String)s.getField("Account_Code__c")));
						inv.setCurrency(checkForNull((String)s.getField("Currency__c")));
						inv.setFundCode(checkForNull((String)s.getField("Fund_Code__c")));
						inv.setGlDate(checkForNull((String)s.getField("GL_Date__c")));
						inv.setIaKey(checkForNull((String)s.getField("IA_Key__c")));
						inv.setInvestorAccount(checkForNull((String)s.getField("Investor_Account__c")));
						inv.setIrr(checkForNull((String)s.getField("IRR__c")));
						inv.setMtd(checkForNull((String)s.getField("MTD__c")));
						inv.setNav(checkForNull((String)s.getField("NAV__c")));
						inv.setPriorNAV(checkForNull((String)s.getField("Fund__c")));
						inv.setQtd(checkForNull((String)s.getField("Prior_NAV__c")));
						inv.setTotalCalled(checkForNull((String)s.getField("Total_Called__c")));
						inv.setTotalCommitted(checkForNull((String)s.getField("Total_Committed__c")));
						inv.setTotalDistributions(checkForNull((String)s.getField("Total_Distributions__c")));
						inv.setTotalTransferred(checkForNull((String)s.getField("Total_Transferred__c")));
						inv.setYtd(checkForNull((String)s.getField("YTD__c")));
						invList.add(inv);


					}


					if (invqueryResults.isDone()) {
						done = true;
					} else {
						invqueryResults = connection.queryMore(invqueryResults.getQueryLocator());
					}
				}
				
				obj.investmentList = invList;
			}

		}catch(Exception e){
			LOGGER.error("Exception occured while getting investment records for valuation export file ." + e.toString());
			throw new Exception(e);
		}

	}

	public void populateData(Fund fund, XMLUtils obj, HashMap<String,ArrayList<Service>> serviceMap) throws Exception {

		ArrayList<ContactService> csList = new ArrayList<ContactService>();
		ArrayList<Organisation> orgList = new ArrayList<Organisation>();
		ArrayList<Account> accList = new ArrayList<Account>();
		ArrayList<Contact> conList = new ArrayList<Contact>();

		HashSet<String> invAccountIds = new HashSet<String>();
		HashSet<String> serviceIds = new HashSet<String>();
		HashSet<String> duplicateOrgCheck = new HashSet<String>();
		HashSet<String> duplicateAccountCheck = new HashSet<String>();
		HashSet<String> duplicateContactCheck = new HashSet<String>();


		try {

			QueryResult csqueryResults ;

			if(fund != null){
				csqueryResults = connection.query("SELECT Id, Service_Text__c, Fund__c, Fund__r.Fund_Code__c, Contact__c, Investor_Account__r.Account_Code__c, Contact__r.AccountId " +
						", Investor_Account__c, Investor_Account__r.Investor_Account_Name__c, Investor_Account__r.Account_Status__c, Delivery_Rule__c,"
						+ "Investor_Account__r.Organization__c,  Investor_Account__r.Organization__r.Name , Service__c "
						+ " FROM Contact_Service__c WHERE Fund__c = '"+fund.getId()+"'");

			}else{
				csqueryResults = connection.query("SELECT Id, Service_Text__c, Fund__c, Fund__r.Fund_Code__c, Contact__c, Investor_Account__r.Account_Code__c, Contact__r.AccountId " +
						", Investor_Account__c, Investor_Account__r.Investor_Account_Name__c, Investor_Account__r.Account_Status__c, Delivery_Rule__c,"
						+ "Investor_Account__r.Organization__c,  Investor_Account__r.Organization__r.Name , Service__c "
						+ " FROM Contact_Service__c WHERE Contact_Level_Service__c  = true");
			}

			LOGGER.debug("Total CS  retreived : " + csqueryResults.getSize());

			Boolean done = false;
			if (csqueryResults.getSize() > 0) {

				while (!done) {

					for (SObject s: csqueryResults.getRecords()) {
						ContactService cs = new ContactService();
						cs.setAccountId(checkForNull(getParentValue(s, "Contact__r", "AccountId", null)));
						cs.setContact(checkForNull((String)s.getField("Contact__c")));
						cs.setFund(checkForNull((String)s.getField("Fund__c")));
						cs.setFundCode(checkForNull(getParentValue(s, "Fund__r", "Fund_Code__c", null)));
						cs.setId(checkForNull(s.getId()));
						cs.setInvestorAccountCode(checkForNull(getParentValue(s, "Investor_Account__r", "Account_Code__c", null)));
						//cs.setInvestorAccountId((String)s.getField("Contact__r.AccountId"));
						cs.setServiceText(checkForNull((String)s.getField("Service_Text__c")));
						cs.setService(checkForNull((String)s.getField("Service__c")));
						cs.setDeliveryRule(checkForNull((String)s.getField("Delivery_Rule__c")));
						serviceIds.add(checkForNull(cs.getService()));
						csList.add(cs);

						if(fund != null){
							Organisation org = new Organisation();
							//LOGGER.debug("CS Id : " + cs.getId());
							org.setId(checkForNull(getParentValue(s, "Investor_Account__r", "Organization__c", null)));
							org.setName(checkForNull(getParentValue(s, "Investor_Account__r", "Organization__r", "Name")));
							//org.setName((String)((XmlObject) s.getField("Investor_Account__r")).getChild("Organization__c").getValue());
							if(!duplicateOrgCheck.contains(org.getId())){
								orgList.add(org);
							}
							duplicateOrgCheck.add(org.getId());

							Account acc = new Account();
							acc.setId(checkForNull((String)s.getField("Investor_Account__c")));
							acc.setName(checkForNull(getParentValue(s, "Investor_Account__r", "Investor_Account_Name__c", null).replaceAll("\"", "\"\"")));
							acc.setCode(checkForNull(getParentValue(s, "Investor_Account__r", "Account_Code__c", null)));
							acc.setOrganisation(checkForNull(getParentValue(s, "Investor_Account__r", "Organization__c", null)));
							acc.setStatus(checkForNull(getParentValue(s, "Investor_Account__r", "Account_Status__c", null)));

							if(!duplicateAccountCheck.contains(acc.getId()))
								accList.add(acc);

							duplicateAccountCheck.add(acc.getId());

							invAccountIds.add(acc.getId());
						}
					}

					if (csqueryResults.isDone()) {
						done = true;
					} else {
						csqueryResults = connection.queryMore(csqueryResults.getQueryLocator());
					}
				}

			}

			//LOGGER.debug("serviceIds : " + serviceIds);

			String serviceWhereClause = "";

			int counterService = 1;

			for(String sId : serviceIds){
				if(sId != null && !sId.equals("null"))
					serviceWhereClause += "'" + sId + "',";

				if(serviceWhereClause.length() > 15000 || serviceIds.size() == counterService ){

					serviceWhereClause = serviceWhereClause.substring(0, serviceWhereClause.length() - 1);

					QueryResult servicequeryResults = connection.query("SELECT Service_Type__c, Web_Category__c, Web_Fund_Code__c FROM Website_Translation__c"
							+ " WHERE Service_Type__c IN (" + serviceWhereClause + ")");
					//+ "FROM Investor_Contacts__c WHERE Investor_Account__c IN ( select Investor_Account__c from contact_service__c where fund__c = '"+fund.getId()+"')");

					LOGGER.debug("Total Web translations retreived : " + servicequeryResults.getSize());

					done = false;
					if (servicequeryResults.getSize() > 0) {

						while (!done) {

							for (SObject s: servicequeryResults.getRecords()) {
								Service ser = new Service();
								ser.setCategory(checkForNull((String)s.getField("Web_Category__c")));
								ser.setFundCode(checkForNull((String)s.getField("Web_Fund_Code__c")));
								ser.setServiceType(checkForNull((String)s.getField("Service_Type__c")));

								if(serviceMap.get(ser.getServiceType()) == null){
									serviceMap.put(ser.getServiceType(), new ArrayList<Service>());
								}

								serviceMap.get(ser.getServiceType()).add(ser);
							}

							if (servicequeryResults.isDone()) {
								done = true;
							} else {
								servicequeryResults = connection.queryMore(servicequeryResults.getQueryLocator());
							}
						}

					}
					serviceWhereClause = "";
				}
				counterService++;
			}

			if(fund != null){
				String invAccWhereClause = "";

				int counter = 1;

				for(String iaId : invAccountIds){
					if(iaId != null && !iaId.equals("null"))
						invAccWhereClause += "'" + iaId + "',";

					if(invAccWhereClause.length() > 15000 || invAccountIds.size() == counter ){

						invAccWhereClause = invAccWhereClause.substring(0, invAccWhereClause.length() - 1);

						QueryResult icqueryResults = connection.query("SELECT Contact_Name__c, Investor_Account__c, SLX_Source__c, Contact__c,Contact__r.AccountId, "
								+ "Contact__r.Name, Contact__r.FirstName, Contact__r.LastName, Contact__r.Email, Contact__r.Preferred_Name__c, "
								+ "Contact__r.Salutation,Contact__r.OtherStreet, Contact__r.OtherCity, Contact__r.OtherState, Contact__r.OtherPostalCode "
								+ "FROM Investor_Contacts__c WHERE Investor_Account__c IN (" + invAccWhereClause + ")");
						//+ "FROM Investor_Contacts__c WHERE Investor_Account__c IN ( select Investor_Account__c from contact_service__c where fund__c = '"+fund.getId()+"')");

						LOGGER.debug("Total Investor Contacts  retreived : " + icqueryResults.getSize());

						done = false;
						if (icqueryResults.getSize() > 0) {

							while (!done) {

								for (SObject s: icqueryResults.getRecords()) {
									Contact con = new Contact();
									con.setAddress1(getParentValue(s, "Contact__r", "OtherStreet", null) != null ? getParentValue(s, "Contact__r", "OtherStreet", null).replaceAll("\n", "") : "");
									con.setCity(checkForNull(getParentValue(s, "Contact__r", "OtherCity", null)));
									con.setCsum("");
									con.setEmail(checkForNull(getParentValue(s, "Contact__r", "Email", null)));
									con.setFirstName(checkForNull(getParentValue(s, "Contact__r", "FirstName", null)));
									con.setFormattedSalutation("Dear " + checkForNull(getParentValue(s, "Contact__r", "Preferred_Name__c", null)));
									con.setId(checkForNull((String)s.getField("Contact__c")));
									con.setLastName(checkForNull(getParentValue(s, "Contact__r", "LastName", null)));
									con.setOrgId(getParentValue(s, "Contact__r", "AccountId", null));
									con.setPostalCode(checkForNull(getParentValue(s, "Contact__r", "OtherPostalCode", null)));
									con.setPrefix(checkForNull(getParentValue(s, "Contact__r", "Salutation", null)));
									con.setSalutation(checkForNull(getParentValue(s, "Contact__r", "Preferred_Name__c", null)));
									con.setState(checkForNull(getParentValue(s, "Contact__r", "OtherState", null)));
									con.setFormattedAddress((con.getPrefix() + " " + getParentValue(s, "Contact__r", "Name", null) + "\\r" + con.getAddress1() + "\\r" +
											con.getCity() + ", " + con.getState() + " " + con.getPostalCode()).replaceAll("\n", ""));

									if(!duplicateContactCheck.contains(con.getId()))
										conList.add(con);

									duplicateContactCheck.add(con.getId());
								}

								if (icqueryResults.isDone()) {
									done = true;
								} else {
									icqueryResults = connection.queryMore(icqueryResults.getQueryLocator());
								}
							}

						}
						invAccWhereClause = "";
					}
					counter++;
				}
			}


			obj.csList = csList;
			obj.orgList = orgList;
			obj.accList = accList;
			obj.conList = conList;

		} catch (Exception e) {

			LOGGER.error("Exception occured while querying funds ." + e.toString());
			throw new Exception(e);
		}    

	}

	private static String getParentValue(SObject s, String firstField, String secondField, String thirdField){

		try{
			if(thirdField == null){
				return (String)((XmlObject) s.getField(firstField)).getChild(secondField).getValue();
			}else{
				return (String)(((XmlObject)((XmlObject) s.getField(firstField)).getField(secondField)).getChild(thirdField).getValue());
			}
		}catch(Exception e){
			//LOGGER.error("Error while getting parent value :" + e.getStackTrace() + e.getMessage() + e.getCause());
			return "";
		}

	}

	private static String checkForNull(String str){
		if(str != null && !str.isEmpty()){
			return str;
		}

		return "";
	}

}
