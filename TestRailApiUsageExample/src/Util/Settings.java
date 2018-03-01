package Util;

import java.io.FileInputStream;
import java.util.Properties;

public class Settings {
	
	String testrailUsername = "";
	String testrailPassword = "";
	String testrailUrl = "";
	
	/** 
	 * construct setting information
	 **/
	public Settings()
	{
		Properties prop = null;
		try {
			prop = new Properties();
			prop.load(new FileInputStream(Constant.SETTING_PATH));
			
			this.testrailUsername = prop.getProperty("userName");
            
            this.testrailPassword = prop.getProperty("password");
            this.testrailUrl = prop.getProperty("url");
            
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * @return the testrailUsername
	 */
	public String getTestrailUsername() {
		return testrailUsername;
	}

	/**
	 * @param testrailUsername the testrailUsername to set
	 */
	public void setTestrailUsername(String testrailUsername) {
		this.testrailUsername = testrailUsername;
	}

	/**
	 * @return the testrailPassword
	 */
	public String getTestrailPassword() {
		return testrailPassword;
	}

	/**
	 * @param testrailPassword the testrailPassword to set
	 */
	public void setTestrailPassword(String testrailPassword) {
		this.testrailPassword = testrailPassword;
	}

	/**
	 * @return the testrailUrl
	 */
	public String getTestrailUrl() {
		return testrailUrl;
	}
	
	/**
	 * @param testrailUrl the testrailUrl to set
	 */
	public void setTestrailUrl(String testrailUrl) {
		this.testrailUrl = testrailUrl;
	}
}
