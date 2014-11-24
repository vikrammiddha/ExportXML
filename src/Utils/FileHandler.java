package Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

import com.config.AppConfig;

public class FileHandler {

	private static final Logger LOGGER = Logger.getLogger(FileHandler.class);

	public static void write(String s, Boolean append, AppConfig config, String outputFileName) throws Exception {

		//LOGGER.debug("Writing to File....." + s);
		File file = new File(config.getOutputDirectory() + "/" + outputFileName + ".xml");

		FileWriter writer = new FileWriter(file.getAbsoluteFile(), append);
		PrintWriter out = new PrintWriter(writer);
		try {
			out.print(s);           
			out.flush();
			writer.flush();
			//LOGGER.debug("Writing to File Completed.....");
		} finally {
			writer.close();
			out.close();
		}
	}

	public static void write(String s, Boolean append, AppConfig config, String outputFileName, String outputDirectory) throws Exception {

		//LOGGER.debug("Writing to File....." + s);
		File file = new File(outputDirectory + "/" + outputFileName + ".xml");

		FileWriter writer = new FileWriter(file.getAbsoluteFile(), append);
		PrintWriter out = new PrintWriter(writer);
		try {
			out.print(s);           
			out.flush();
			writer.flush();
			//LOGGER.debug("Writing to File Completed.....");
		} finally {
			writer.close();
			out.close();
		}
	}

}
