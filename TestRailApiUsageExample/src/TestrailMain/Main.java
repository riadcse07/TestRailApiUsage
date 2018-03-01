package TestrailMain;
import Util.Helper;
import Util.Settings;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Settings setting = new Settings();
		
		int projectID = 11 ;
		String getSpecificRunIDName = "Automation";
		
		Helper helper = new Helper(setting);
		
		int runID = helper.getTestRunId(projectID, getSpecificRunIDName);
		String projectName = helper.getProject(projectID);
		
		System.out.println("latest Run ID is : " + runID );
		
		System.out.println("Project Name is : " + projectName);
		
		System.out.println("Testcase info from Run ID :" + helper.getTestsInfo(runID));
		
	}

}
