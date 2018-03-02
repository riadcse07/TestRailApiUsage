package TestrailMain;
import java.util.Map;

import Util.Helper;
import Util.Settings;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Settings setting = new Settings();

		int projectID = 11 ;
		String getSpecificRunIDName = "Automation";
		
		System.out.println(args.length);
		
		if (args.length >=2){
			String username = args[1];
			String password = args[2];
			String url = args[3];
			
			setting.setTestrailUsername(username);
			setting.setTestrailUrl(url);
			//setting.setTestrailPassword(password);
		}
		
		Helper helper = new Helper(setting);

		int runID = helper.getTestRunId(projectID, getSpecificRunIDName);
		String projectName = helper.getProject(projectID);

		System.out.println("latest Run ID is : " + runID );

		System.out.println("Project Name is : " + projectName);

		System.out.println("Testcase info from Run ID :" + helper.getTestsInfo(runID));
		
	}
	
}
