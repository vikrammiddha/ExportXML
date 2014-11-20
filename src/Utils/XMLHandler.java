package Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.log4j.Logger;

import bean.Fund;

public class XMLHandler {

	private static final Logger LOGGER = Logger.getLogger(XMLHandler.class);

	public static String getXMLHeader(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return	"<?xml version=\"1.0\" encoding=\"UTF-8\"  standalone=\"no\" ?>\n"+
		"<metadata >\n"+
		"\t<context>\n"+
		"\t\t<item name=\"generatedBy\">Salesforce Export</item>\n"+
		"\t\t<item name=\"generatedBy2\">sp_web_exportservice</item>\n"+
		"\t\t<item name=\"generatedByVersion\">1.0</item>\n"+
		"\t\t<item name=\"sequenceNumber\">XXXXXXXX</item>\n"+
		"\t\t<item name=\"serverMachine\">AGSQL-SQL\\AGSQL</item>\n"+
		"\t\t<item name=\"executeDateTime\">"+dateFormat.format(cal.getTime()) + "</item>\n"+
		"\t\t<item name=\"executeUser\">NYC\\MGroves</item>\n"+
		"\t\t<item name=\"executeHost\">MGROVES-VM01</item>\n"+
		"\t\t<item name=\"localOutputFileName\">xxxxxxxx-sp_web_exportservice-YYYYMMDDXXXXXXX.xml</item>\n"+
		"\t\t<item name=\"localOutputFilePath\">\\\\data1\\Apps\\ITG\\TransferDEV\\Out</item>\n"+
		"\t\t<item name=\"localOutputFileSize\">00000000000</item>\n"+
		"\t</context>\n"+
		"\t<parameters>"+
		"\t\t<parameter name=\"funds\" value=\"SUPR\"/>\n"+
		"\t\t<parameter name=\"writefile\" value=\"1\"/>\n"+
		"\t\t<parameter name=\"autocommit\" value=\"1\"/>\n"+
		"\t\t<parameter name=\"outputPath\" value=\"\\\\data1\\Apps\\ITG\\TransferDEV\\Out\"/>\n"+
		"\t</parameters>\n"+
		"</metadata>\n"+
		"<data>\n";
	}

	public static String getFundsXML(ArrayList<Fund> fundList){
		String retString = "";
		retString = "<funds count=\""+fundList.size()+"\">\n";
		for(Fund f : fundList){
			retString += "\t<fund seq=\"X\" id=\"" +f.getId() +"\" code=\""+f.getCode()+"\" csum=\"\"/>\n";
		}
		retString += "</funds>";
		return retString;
	}

	public static String getXMLFooter(){
		return "\n</data>";
	}

}
