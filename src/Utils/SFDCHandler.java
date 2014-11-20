package Utils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import bean.Fund;

import com.config.AppConfig;
import com.sforce.async.JobInfo;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectorConfig;
import com.sforce.async.*;


public class SFDCHandler {

	private PartnerConnection connection;
	private BulkConnection bulkConnection;
	private static final Logger LOGGER = Logger.getLogger(SFDCHandler.class);

	public SFDCHandler(){

	}

	public SFDCHandler(AppConfig appConfig) throws Exception{
		ConnectorConfig config = new ConnectorConfig();
		config.setUsername(appConfig.getSfdcUserName());
		config.setPassword(appConfig.getSfdcPassword());
		config.setAuthEndpoint(appConfig.getEndPoint());
		config.setCompression(true);
		config.setTraceFile("traceLogs.txt");
		config.setTraceMessage(true);
		config.setPrettyPrintXml(true);

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

	public void populateData(XMLUtils obj) throws Exception {

		LOGGER.debug("Querying Funds.....");
		ArrayList<Fund> fundList = new ArrayList<Fund>();
		try {

			/*JobInfo job = createJob("Fund__c");
			String query = "SELECT Id FROM Fund__c";
			ByteArrayInputStream byteArrayInputStream =getData(query,job);
			//TODO: closeJob(bulkconnection, job.getId());
			System.out.println("Data: "+byteArrayInputStream);
		        int c;
		        while(( c= byteArrayInputStream.read())!= -1) {
		            LOGGER.debug("Val = " + (char)c);
		         }
			 */
			// query for the 5 newest contacts  

			LOGGER.debug("Quering lastExecutedDate...");
			String lastExecutedDate = "";
			QueryResult custSettingResult = connection.query("Select Last_Exported_Time__c FROM XML_Export__c limit 1");
			if (custSettingResult.getSize() > 0) {
				lastExecutedDate = (String)custSettingResult.getRecords()[0].getField("Last_Exported_Time__c");
				lastExecutedDate = lastExecutedDate.split("-")[0] + "-" + lastExecutedDate.split("-")[2].split("T")[0] + "-" +
						lastExecutedDate.split("-")[1] + "T" + lastExecutedDate.split("-")[2].split("T")[1];
				LOGGER.debug("lastExecutedDate : " + lastExecutedDate);
			}

			QueryResult queryResults = connection.query("SELECT Id,Fund_Code__c,"
					+ "	(SELECT Id, Service_Text__c, Fund__c, Fund__r.Fund_Code__c, Contact__c, Investor_Account__r.Account_Code__c, Contact__r.AccountId "
									+ "	FROM Contact_Services__r) FROM Fund__c WHERE LastModifiedDate > " + lastExecutedDate  + " limit 5");
			if (queryResults.getSize() > 0) {
				ArrayList<String> fundCodeIds = new ArrayList<String>();
				LOGGER.debug("Total Funds retreived : " + queryResults.getSize());
				for (SObject s: queryResults.getRecords()) {
					Fund f = new Fund();
					f.setId(s.getId());
					f.setCode((String)s.getField("Fund_Code__c"));
					fundList.add(f);
					fundCodeIds.add(s.getId());
					//sb.append("Id: " + s.getId() + "\n");
				}

				obj.fundList = fundList;
				
				/*LOGGER.debug("Quering Contact Service records ....");
				
				String[] fundCodeIdsArr = fundCodeIds.toArray(new String[fundCodeIds.size()]);
				SObject[] sObjectsCS = connection.retrieve("Id, Service_Text__c, Fund__c, Fund__r.Fund_Code__c, Contact__c,Investor_Account__r.Account_Code__c, Contact__r.AccountId",
			            "Account", fundCodeIdsArr);
				
				LOGGER.debug("Queried " + sObjectsCS.length + " contact service records" );*/
			}

		} catch (Exception e) {
			LOGGER.error("Exception occured while querying funds ." + e.getCause());
			throw new Exception(e);
		}    

	}


	/*public JobInfo createJob(String sobjectType)
			throws AsyncApiException {
		JobInfo job = new JobInfo();
		job.setObject(sobjectType);
		job.setOperation(OperationEnum.query);
		job.setConcurrencyMode(ConcurrencyMode.Parallel);
		job.setContentType(ContentType.CSV);
		job = bulkConnection.createJob(job);
		assert job.getId() != null;
		job = bulkConnection.getJobStatus(job.getId());
		System.out.println("\nJob: "+job);
		return job;
	}

	public ByteArrayInputStream getData(String query, JobInfo job) {
		ByteArrayInputStream inputStream = null;
		try{    
			System.out.println("querying...:"+query);
			BatchInfo info = null;
			ByteArrayInputStream bout = new ByteArrayInputStream(
					query.getBytes());// convert query into ByteArrayInputStream
			info = bulkConnection.createBatchFromStream(job, bout);// creates batch from ByteArrayInputStream
			String[] queryResults = null;// to store QueryResultList
			QueryResultList list = null;
			int count=0;
			for (int i = 0; i < 10000; i++) // 10000=maxRowsPerBatch
			{
				count++;
				Thread.sleep(i == 0 ? 5 * 1000 : 5 * 1000); // 30 sec

				info = bulkConnection.getBatchInfo(job.getId(), info.getId());
				// If Batch Status is Completed,get QueryResultList and store in
				// queryResults.

				System.out.println("BatchStateEnum.Completed:"+BatchStateEnum.Completed);

				if (info.getState() == BatchStateEnum.Completed) {

					list = bulkConnection.getQueryResultList(job.getId(),
							info.getId());
					queryResults = list.getResult();

					break;
				} else if (info.getState() == BatchStateEnum.Failed) {
					System.out.println("-------------- failed ----------"
							+ info);
					break;
				} else {
					System.out.println("-------------- waiting ----------"
							+ info);
				}
			}
			System.out.println("count::--"+count);
			System.out.println("QueryResultList::" + list);
			if (queryResults != null) {
				//for each resultid retrieve data and store in ByteArrayInputstream provided job id,batch id and result id 
				//as returnType for bulkConnection.getQueryResultStream() method is ByteArrayInputstream
				for (String resultId : queryResults) {
					inputStream=(ByteArrayInputStream) bulkConnection.getQueryResultStream(job.getId(),
							info.getId(), resultId);
				}
			}

		} catch (AsyncApiException | InterruptedException aae) {
			aae.printStackTrace();
		}
		return inputStream;
	}*/

}
