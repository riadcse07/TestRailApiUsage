package Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.gurock.testrail.APIClient;

public class Helper {

	private Settings pSetting;
	APIClient client = null;
	static int m_sectionId = 0;
	static String m_suiteName = "";

	static long custom_device = 0;
	static long custom_os = 0;
    
	public Helper(Settings ps){
		this.pSetting = ps;
		client = getTestrailClient();	
	}
    
	public APIClient getTestrailClient(){
		client = new APIClient(pSetting.getTestrailUrl());
		client.setUser(pSetting.getTestrailUsername());
		client.setPassword(pSetting.getTestrailPassword());
		return client;
	}

	public String getProject(int projectId){
		String projectName = "";
		try {

			JSONObject joProj = (JSONObject) client.sendGet("get_project/"+projectId);
			projectName = joProj.get("name").toString();

		}catch (Exception e) {
			e.printStackTrace();
		}

		return projectName;
	}

	/**
	 * 	project_id = pass your project id from testrail"
	 * 	Pass project_id to get the latest run_id of the partcular TestRun Name { WSAuto }
	 */
	public int getTestRunId(int projectId, String runNamePrefix){
		int runId = 0;
		List<Integer> runIds = new  ArrayList();
		try {
			JSONArray ja = (JSONArray) client.sendGet("get_runs/"+projectId);
			for (int i = 0; i < ja.size(); i++) {
				JSONObject jsonObject = (JSONObject) ja.get(i);
				String tRunName = jsonObject.get("name").toString();
				if(tRunName.contains(runNamePrefix)){
					int value = Integer.parseInt(jsonObject.get("id").toString());
					runIds.add(value);
				}
			}
			runId = Collections.max(runIds);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return runId;
	}
	
	//Returns test cases list with testinfo
	public List<Integer> getTestsInfo(int runId){
		List<Integer> tcIds = new ArrayList<Integer>();
		try {	
			JSONArray jaTests = (JSONArray) client.sendGet("get_tests/"+runId);
			for (int i = 0; i < jaTests.size(); i++) {
				JSONObject joTest = (JSONObject) jaTests.get(i);
				int tcId = Integer.parseInt(joTest.get("case_id").toString());
				tcIds.add(tcId);
			}

		}catch (Exception e) {
			e.printStackTrace();
		}

		return tcIds;
	}

	public Map<String, List<Integer>> getTestCases(int projectId, List<Integer> tcIds){
		String mSuitName = "";
		Map<String, List<Integer>> mapSuiteCase = new HashMap<String, List<Integer>>();
		List<Integer> ids = new ArrayList<Integer>();

		try {
			JSONArray jaCases = (JSONArray) client.sendGet("get_cases/"+projectId);

			for (int i = 0; i < jaCases.size(); i++) {
				JSONObject joCase = (JSONObject) jaCases.get(i);				
				int caseId = Integer.parseInt(joCase.get("id").toString());

				if(!tcIds.contains(caseId)){
					continue;
				} else {
					int sectionId = Integer.parseInt(joCase.get("section_id").toString());

					if(sectionId!=m_sectionId){
						if(ids.size() > 0){
							mapSuiteCase.put(mSuitName, ids);
							//System.out.println("Ok");
						}

						JSONObject joSection = (JSONObject) client.sendGet("get_section/"+sectionId);
						mSuitName = joSection.get("name").toString();
						m_sectionId = sectionId;
						m_suiteName = mSuitName;
						ids = new ArrayList<Integer>();
					}else{
						mSuitName = m_suiteName;
					}

					ids.add(caseId);
				}

			}

			mapSuiteCase.put(mSuitName, ids);
			//System.out.println("Ok");

		}catch (Exception e) {
			e.printStackTrace();
		}

		return mapSuiteCase;
	}

	public Map<String, String> getTestResultsforCase(int runId, int caseId, String mSuiteName){
		int latestResultId = 0;
		Map<String, String> map = new HashMap<String, String>();
		List<Integer> resultIds = new ArrayList<Integer>();
		try {
			JSONArray ja = (JSONArray) client.sendGet("get_results_for_case/"+runId+"/"+caseId);
			if(ja.size() > 0) {
				for (int i = 0; i < ja.size(); i++) {
					JSONObject jsonObject = (JSONObject) ja.get(i);
					String value = jsonObject.get("id").toString();
					int resultId = Integer.parseInt(value);
					resultIds.add(resultId);				
				}

				latestResultId = Collections.max(resultIds);
				//System.out.println(latestResultId);

				for (int i = 0; i < ja.size(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					int jsonObjId = Integer.parseInt(jo.get("id").toString());
					if(jsonObjId == latestResultId){
						//System.out.println(jo);
						map.put("status_id", jo.get("status_id").toString());
						map.put("comment", jo.get("comment").toString());
						map.put("suite_name", mSuiteName);

						break;
					}
				}
			}

		}catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
	
	public List<Map<String, Map<String,String>>> getTestResultsforCaseUpdated(int runId, int caseId, String mSuiteName){

		List<Integer> resultIds = new ArrayList<Integer>();
		//Map<String, Object> MapIds = (Map<String, Object>) new ArrayList<Map<String, Object>>();
		List<Map<String, Map<String,String>>> MapWithBrowser = new ArrayList<Map<String, Map<String,String>>>();
		try {
			JSONArray ja = (JSONArray) client.sendGet("get_results_for_case/"+runId+"/"+caseId);
			if(ja.size() > 0) {
				for (int i = 0; i < ja.size(); i++) {
					JSONObject jsonObject = (JSONObject) ja.get(i);
					String value = jsonObject.get("id").toString();
					int resultId = Integer.parseInt(value);
					resultIds.add(resultId);				
				}
				for (int i = 0; i < ja.size(); i++) {
					JSONObject jo = (JSONObject) ja.get(i);
					Map<String, String> map1 = new HashMap<String, String>();
					Map<String, Map<String,String>> MapIds = (Map<String, Map<String,String>>) new HashMap<String, Map<String,String>>();
					map1.put("status_id", jo.get("status_id").toString());
					map1.put("comment", jo.get("comment").toString());
					map1.put("suite_name", mSuiteName);

					long c_os = (Long) ((JSONArray)jo.get("custom_os")).get(0);
					if(c_os == 6){
						map1.put("custom_os", "safari");
						MapIds.put("safari", map1);
					}
					else if(c_os == 3){
						map1.put("custom_os", "firefox");
						MapIds.put("firefox", map1);
					}
					else if(c_os == 1){
						map1.put("custom_os", "chrome");
						MapIds.put("chrome", map1);
					}
					long c_device = (Long) ((JSONArray)jo.get("custom_device")).get(0);						
					if(c_device != custom_device){
						custom_device = c_device;
					}
					MapWithBrowser.add(MapIds);
				}
			}
		}
		catch(Exception e){
		}
		return MapWithBrowser;
	}
}
