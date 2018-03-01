package Util;

public class Constant {
	private Constant () { } // prevents instantiation
	
	private static String USER_DIR = System.getProperty("user.name");
	public static final String CURRENT_DIR = System.getProperty("user.dir");
	
	public static final String SETTING_PATH = CURRENT_DIR+"/Config/setting.conf";
	public static final String APP_CONFIG = CURRENT_DIR+"/config/";
}
