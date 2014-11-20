package com;

import Utils.FileHandler;
import Utils.XMLUtils;

import com.config.AppConfig;
import com.config.Configurator;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.Error;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;




public class Main {

	static final String USERNAME = "angelo@silverlinecrm.com.uat";
	static final String PASSWORD = "gordonforc7";
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
			obj.generateXMLFile();
		} catch (Exception e) {
			LOGGER.error("Exception in initiating the export process :" + e.getCause());
		}
		
	}

	// queries and displays the 5 newest contacts
	
	// create 5 test Accounts
	/*private static void createAccounts() {

    System.out.println("Creating 5 new test Accounts...");
    SObject[] records = new SObject[5];

    try {

      // create 5 test accounts
      for (int i=0;i<5;i++) {
        SObject so = new SObject();
        so.setType("Account");
        so.setField("Name", "Test Account "+i);
        records[i] = so;
      }


      // create the records in Salesforce.com
      SaveResult[] saveResults = connection.create(records);

      // check the returned results for any errors
      for (int i=0; i< saveResults.length; i++) {
        if (saveResults[i].isSuccess()) {
          System.out.println(i+". Successfully created record - Id: " + saveResults[i].getId());
        } else {
          Error[] errors = saveResults[i].getErrors();
          for (int j=0; j< errors.length; j++) {
            System.out.println("ERROR creating record: " + errors[j].getMessage());
          }
        }    
      }

    } catch (Exception e) {
      e.printStackTrace();
    }    

  }

  // updates the 5 newly created Accounts
  private static void updateAccounts() {

    System.out.println("Update the 5 new test Accounts...");
    SObject[] records = new SObject[5];

    try {

      QueryResult queryResults = connection.query("SELECT Id, Name FROM Account ORDER BY " +
      		"CreatedDate DESC LIMIT 5");
      if (queryResults.getSize() > 0) {
    	  for (int i=0;i<queryResults.getRecords().length;i++) {
    	    SObject so = (SObject)queryResults.getRecords()[i];
    	    System.out.println("Updating Id: " + so.getId() + " - Name: "+so.getField("Name"));
    	    // create an sobject and only send fields to update
    	    SObject soUpdate = new SObject();
    	    soUpdate.setType("Account");
    	    soUpdate.setId(so.getId());
    	    soUpdate.setField("Name", so.getField("Name")+" -- UPDATED");
    	    records[i] = soUpdate;
    	  }
    	}


      // update the records in Salesforce.com
      SaveResult[] saveResults = connection.update(records);

      // check the returned results for any errors
      for (int i=0; i< saveResults.length; i++) {
        if (saveResults[i].isSuccess()) {
          System.out.println(i+". Successfully updated record - Id: " + saveResults[i].getId());
        } else {
          Error[] errors = saveResults[i].getErrors();
          for (int j=0; j< errors.length; j++) {
            System.out.println("ERROR updating record: " + errors[j].getMessage());
          }
        }    
      }

    } catch (Exception e) {
      e.printStackTrace();
    }    

  }

  // delete the 5 newly created Account
  private static void deleteAccounts() {

    System.out.println("Deleting the 5 new test Accounts...");
    String[] ids = new String[5];

    try {

      QueryResult queryResults = connection.query("SELECT Id, Name FROM Account ORDER BY " +
      		"CreatedDate DESC LIMIT 5");
      if (queryResults.getSize() > 0) {
    	  for (int i=0;i<queryResults.getRecords().length;i++) {
    	    SObject so = (SObject)queryResults.getRecords()[i];
    	    ids[i] = so.getId();
    	    System.out.println("Deleting Id: " + so.getId() + " - Name: "+so.getField("Name"));
    	  }
    	}


      // delete the records in Salesforce.com by passing an array of Ids
      DeleteResult[] deleteResults = connection.delete(ids);

      // check the results for any errors
      for (int i=0; i< deleteResults.length; i++) {
        if (deleteResults[i].isSuccess()) {
          System.out.println(i+". Successfully deleted record - Id: " + deleteResults[i].getId());
        } else {
          Error[] errors = deleteResults[i].getErrors();
          for (int j=0; j< errors.length; j++) {
            System.out.println("ERROR deleting record: " + errors[j].getMessage());
          }
        }    
      }

    } catch (Exception e) {
      e.printStackTrace();
    }    

  }*/

}