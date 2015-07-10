package business.dataaccess;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class DataAccessFacade {
	private static DataAccess dao;
	
	private static final String propFileName = "./config.properties";
	
	private DataAccessFacade() {
		
	}

	public static DataAccess getDAO() {
		if (getDataSource() == 0) {
			if (dao == null) {
				dao = new DataAccessFile();
			}
			return dao; 
		} else {
			if (dao == null) {
				dao = new DataAccessDB();
			}
			return dao;
		}
	}

	private static int getDataSource() {
		int sourceType = 0;
		Properties properties = new Properties();
		InputStream input = null;
		 
		try {	 
			input = new FileInputStream(propFileName);
	 
			// load a properties file
			properties.load(input);
			
			String source = properties.getProperty("Datasource");
			if (source.equals("DB")) {
				sourceType = 1;
				// get the property value and print it out
				//System.out.println(properties.getProperty("database"));
				//System.out.println(properties.getProperty("dbuser"));
				//System.out.println(properties.getProperty("dbpassword"));
			} 
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sourceType;
	}
	
//	public static void main(String args[]) {
//		getDataSource();
//	}
}
