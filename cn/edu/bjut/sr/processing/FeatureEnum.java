package cn.edu.bjut.sr.processing;

import java.util.HashMap;
import java.util.Map;

import cn.edu.bjut.text.utility.Log;




public class FeatureEnum {
	//Training parameters
	public static final int TRAIN_KEY_NO = 0;
	public static final int TRAIN_KEY_SINGLE = 1;
	public static final int TRAIN_KEY_MULTI = 2;
	public static final int TRAIN_RULE_NO = 0;
	public static final int TRAIN_RULE_SINGLE = 1;
	public static final int TRAIN_RULE_MULTI = 2;
	public static final int TRAIN_DEP_NO = 0;
	public static final int TRAIN_DEP_SINGLE = 1;
	public static final int TRAIN_DEP_MULTI = 2;
	
	
	
	//Keyword class enumeration
	public static final String SECURITY_PROPERTY = "SP";
	public static final String SECURITY_MECHANISM = "SM";
	public static final String THREAT = "TH";
	public static final String ELIMINATE = "EL";
	public static final String PROTECT = "PR";
	public static final String ACHIEVE = "AC";
	public static final String PROVIDE = "PV";
	public static final String RESTRICT = "RE";
	public static final String ASSURE = "AS";
	public static final String PERMIT = "PE";
	public static final String OBEY = "OB";
	
	
	
	//Feature type enumeration
	public static final int FT_CATEGORY= 0;
	public static final int FT_SENTENCE= 1;
	public static final int FT_KEYWORD= 2;
	public static final int FT_RULE= 3;
	public static final int FT_DEPENDENCY= 4;
	
//	//Types of syntactic analysis
//	public static final int SIMPLE_ANALYSIS= 0;
//	public static final int COMPLEX_ANALYSIS= 1;
	
	
	/*********************
	 *  SR features enumeration 
	 **********************/
	public static String CATEGORY = "category";
	public static String SENTENCE = "sentence";

	public static String[] KEYWORD = {
			//Security Property
			"security", "identification", "authentication", "authorization", 
			"integrity", "immunity", "anonymity", "confidentiality", "privacy",
			"availability", "non-repudiation", "accountability", "business continuity", "reliability",
			// Security Mechanism
			"auditing", "access control", "proof", "key", "lock", "pin", "detection",
			"validation", "verification", "awareness", "education", "training",
			"back-up", "clear desk", "clear screen", "clock synchronization",
			"cryptography", "disposal", "log", "monitoring", "segregation", "separation", 
			"password", "screening", "protection",
			"compliance", "restriction", "assurance", "permission",
			// Threat
			"fraud", "error", "bug", "vulnerability", "risk", "fault", "leakage", "misuse",
			"virus",
			// Eliminate
			"eliminate", "get rid of", "stop", "reduce", "avoid",
			// Protect
			"protect", "defend", "save",
			// Achieve
			"achieve", "obtain", "satisfy", "fulfill",
			// Provide
			"provide", "have", "implement", "deploy", 
			// Restrict
			"restrict", "limit", 
			// Assure
			"assure", "ensure",
			// Permit
			"permit", "allow", "admit",
			// Permit
			"obey", "comply", "subject to", 
	};
	
	public static String[] RULE = {
			"T", "A", "S", "C1", "C2", "C3", "C4", "C5",
			"C1A", "C2A", "C3A", "C4A", "C5A",
			"C1T", "C2T", "C3T", "C4T", "C5T",
			"C1S", "C2S", "C3S", "C4S", "C5S", 
			"AT", "SA", "TS", "C1AT", "C2AT", "C3AT", "C4AT", "C5AT",
			"ATS", "C1SA", "C2SA", "C3SA", "C4SA", "C5SA", 
			"C1TS", "C2TS", "C3TS", "C4TS", "C5TS", 
			"C1ATS", "C2ATS", "C3ATS", "C4ATS", "C5ATS",
		};
	
	/*
	 * This set map rule name to its content (the simple form), facilitating automation in code
	 */
	public static final Map<String, String> LINGUISTIC_RULES = new HashMap<String, String>();
	static {
		LINGUISTIC_RULES.put("T", ".*"+FeatureEnum.ELIMINATE+".*"+FeatureEnum.THREAT+".*");
		LINGUISTIC_RULES.put("A", ".*"+FeatureEnum.PROTECT+".*");// didn't consider "asset", which should be NP
		LINGUISTIC_RULES.put("S", ".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C1", ".*"+FeatureEnum.PROVIDE+".*"+FeatureEnum.SECURITY_MECHANISM+".*");
		LINGUISTIC_RULES.put("C2", ".*"+FeatureEnum.RESTRICT+".*");
		LINGUISTIC_RULES.put("C3", ".*"+FeatureEnum.ASSURE+".*");
		LINGUISTIC_RULES.put("C4", ".*"+FeatureEnum.PERMIT+".*");
		LINGUISTIC_RULES.put("C5", ".*"+FeatureEnum.OBEY+".*");

		LINGUISTIC_RULES.put("C1A", ".*"+FeatureEnum.PROVIDE+".*"+FeatureEnum.SECURITY_MECHANISM+".*"+FeatureEnum.PROTECT+".*");
		LINGUISTIC_RULES.put("C2A", ".*"+FeatureEnum.RESTRICT+".*"+FeatureEnum.PROTECT+".*");
		LINGUISTIC_RULES.put("C3A", ".*"+FeatureEnum.ASSURE+".*"+FeatureEnum.PROTECT+".*");
		LINGUISTIC_RULES.put("C4A", ".*"+FeatureEnum.PERMIT+".*"+FeatureEnum.PROTECT+".*");
		LINGUISTIC_RULES.put("C5A", ".*"+FeatureEnum.OBEY+".*"+FeatureEnum.PROTECT+".*");
		//
		LINGUISTIC_RULES.put("C1T", ".*"+FeatureEnum.PROVIDE+".*"+FeatureEnum.SECURITY_MECHANISM+".*"+FeatureEnum.ELIMINATE+".*"+FeatureEnum.THREAT+".*");
		LINGUISTIC_RULES.put("C2T", ".*"+FeatureEnum.RESTRICT+".*"+FeatureEnum.ELIMINATE+".*"+FeatureEnum.THREAT+".*");
		LINGUISTIC_RULES.put("C3T", ".*"+FeatureEnum.ASSURE+".*"+FeatureEnum.ELIMINATE+".*"+FeatureEnum.THREAT+".*");
		LINGUISTIC_RULES.put("C4T", ".*"+FeatureEnum.PERMIT+".*"+FeatureEnum.ELIMINATE+".*"+FeatureEnum.THREAT+".*");
		LINGUISTIC_RULES.put("C5T", ".*"+FeatureEnum.OBEY+".*"+FeatureEnum.ELIMINATE+".*"+FeatureEnum.THREAT+".*");
		//
		LINGUISTIC_RULES.put("C1S", ".*"+FeatureEnum.PROVIDE+".*"+FeatureEnum.SECURITY_MECHANISM+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C2S", ".*"+FeatureEnum.RESTRICT+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C3S", ".*"+FeatureEnum.ASSURE+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C4S", ".*"+FeatureEnum.PERMIT+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C5S", ".*"+FeatureEnum.OBEY+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		//
		LINGUISTIC_RULES.put("AT", ".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.ELIMINATE+".*"+FeatureEnum.THREAT+".*");
		LINGUISTIC_RULES.put("SA", ".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("TS", ".*"+FeatureEnum.ELIMINATE+".*"+FeatureEnum.THREAT+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		//
		LINGUISTIC_RULES.put("C1AT", ".*"+FeatureEnum.PROVIDE+".*"+FeatureEnum.SECURITY_MECHANISM+".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.THREAT+".*");
		LINGUISTIC_RULES.put("C2AT", ".*"+FeatureEnum.RESTRICT+".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.THREAT+".*");
		LINGUISTIC_RULES.put("C3AT", ".*"+FeatureEnum.ASSURE+".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.THREAT+".*");
		LINGUISTIC_RULES.put("C4AT", ".*"+FeatureEnum.PERMIT+".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.THREAT+".*");
		LINGUISTIC_RULES.put("C5AT", ".*"+FeatureEnum.OBEY+".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.THREAT+".*");
		//
		LINGUISTIC_RULES.put("ATS", ".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.THREAT+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C1SA", ".*"+FeatureEnum.PROVIDE+".*"+FeatureEnum.SECURITY_MECHANISM+".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C2SA", ".*"+FeatureEnum.RESTRICT+".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C3SA", ".*"+FeatureEnum.ASSURE+".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C4SA", ".*"+FeatureEnum.PERMIT+".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C5SA", ".*"+FeatureEnum.OBEY+".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		//
		LINGUISTIC_RULES.put("C1TS", ".*"+FeatureEnum.PROVIDE+".*"+FeatureEnum.SECURITY_MECHANISM+".*"+FeatureEnum.ELIMINATE+".*"+FeatureEnum.THREAT+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C2TS", ".*"+FeatureEnum.RESTRICT+".*"+FeatureEnum.ELIMINATE+".*"+FeatureEnum.THREAT+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C3TS", ".*"+FeatureEnum.ASSURE+".*"+FeatureEnum.ELIMINATE+".*"+FeatureEnum.THREAT+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C4TS", ".*"+FeatureEnum.PERMIT+".*"+FeatureEnum.ELIMINATE+".*"+FeatureEnum.THREAT+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C5TS", ".*"+FeatureEnum.OBEY+".*"+FeatureEnum.ELIMINATE+".*"+FeatureEnum.THREAT+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		//
		LINGUISTIC_RULES.put("C1ATS", ".*"+FeatureEnum.PROVIDE+".*"+FeatureEnum.SECURITY_MECHANISM+".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.THREAT+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C2ATS", ".*"+FeatureEnum.RESTRICT+".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.THREAT+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C3ATS", ".*"+FeatureEnum.ASSURE+".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.THREAT+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C4ATS", ".*"+FeatureEnum.PERMIT+".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.THREAT+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
		LINGUISTIC_RULES.put("C5ATS", ".*"+FeatureEnum.OBEY+".*"+FeatureEnum.PROTECT+".*"+FeatureEnum.THREAT+".*"+FeatureEnum.ACHIEVE+".*"+FeatureEnum.SECURITY_PROPERTY+".*");
	}
	
	// to be revised
	public static String[] DEPENDENCY = {
			"T", "A", "S", "C1", "C2", "C3", "C4", "C5",
			"C1A", "C2A", "C3A", "C4A", "C5A",
			"C1T", "C2T", "C3T", "C4T", "C5T",
			"C1S", "C2S", "C3S", "C4S", "C5S", 
			"AT", "SA", "TS", "C1AT", "C2AT", "C3AT", "C4AT", "C5AT",
			"ATS", "C1SA", "C2SA", "C3SA", "C4SA", "C5SA", 
			"C1TS", "C2TS", "C3TS", "C4TS", "C5TS", 
			"C1ATS", "C2ATS", "C3ATS", "C4ATS", "C5ATS",
		};
	
	/*
	 * This set of mapping defines associate each keyword with its corresponding security requirements concept
	 */
	public static final Map<String, String> KEYWORD_CLASS = new HashMap<String, String>();
	static {
		//Security Property
		KEYWORD_CLASS.put("security", SECURITY_PROPERTY);
		KEYWORD_CLASS.put("identification", SECURITY_PROPERTY);
		KEYWORD_CLASS.put("authentication", SECURITY_PROPERTY);
		KEYWORD_CLASS.put("authorization", SECURITY_PROPERTY);
		KEYWORD_CLASS.put("integrity", SECURITY_PROPERTY);
		KEYWORD_CLASS.put("immunity", SECURITY_PROPERTY);
		KEYWORD_CLASS.put("anonymity", SECURITY_PROPERTY);
		KEYWORD_CLASS.put("confidentiality", SECURITY_PROPERTY);
		KEYWORD_CLASS.put("privacy", SECURITY_PROPERTY);
		KEYWORD_CLASS.put("availability", SECURITY_PROPERTY);
		KEYWORD_CLASS.put("non-repudiation", SECURITY_PROPERTY);
		KEYWORD_CLASS.put("accountability", SECURITY_PROPERTY);
		KEYWORD_CLASS.put("business continuity", SECURITY_PROPERTY);
		KEYWORD_CLASS.put("reliability", SECURITY_PROPERTY);
		
		// Security Mechanism
		KEYWORD_CLASS.put("auditing", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("access control", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("proof", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("key", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("lock", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("pin", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("validation", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("verification", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("awareness", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("training", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("back-up", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("clear desk", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("clear screen", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("clock synchronization", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("cryptography", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("disposal", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("log", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("monitoring", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("segregation", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("separation", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("password", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("screening", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("protection", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("detection", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("compliance", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("restriction", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("assurance", SECURITY_MECHANISM);
		KEYWORD_CLASS.put("permission", SECURITY_MECHANISM);
		
		// Threat
		KEYWORD_CLASS.put("fraud", THREAT);
		KEYWORD_CLASS.put("error", THREAT);
		KEYWORD_CLASS.put("bug", THREAT);
		KEYWORD_CLASS.put("vulnerability", THREAT);
		KEYWORD_CLASS.put("risk", THREAT);
		KEYWORD_CLASS.put("fault", THREAT);
		KEYWORD_CLASS.put("leakage", THREAT);
		KEYWORD_CLASS.put("misuse", THREAT);
		KEYWORD_CLASS.put("virus", THREAT);
		
		// Eliminate
		KEYWORD_CLASS.put("eliminate", ELIMINATE);
		KEYWORD_CLASS.put("get rid of", ELIMINATE);
		KEYWORD_CLASS.put("stop", ELIMINATE);
		KEYWORD_CLASS.put("reduce", ELIMINATE);
		KEYWORD_CLASS.put("avoid", ELIMINATE);
		
		// Protect
		KEYWORD_CLASS.put("protect", PROTECT);
		KEYWORD_CLASS.put("defend", PROTECT);
		KEYWORD_CLASS.put("save", PROTECT);
		
		// Achieve
		KEYWORD_CLASS.put("achieve", ACHIEVE);
		KEYWORD_CLASS.put("obtain", ACHIEVE);
		KEYWORD_CLASS.put("satisfy", ACHIEVE);
		KEYWORD_CLASS.put("fulfill", ACHIEVE);
		
		// Achieve
		KEYWORD_CLASS.put("provide", PROVIDE);
		KEYWORD_CLASS.put("have", PROVIDE);
		KEYWORD_CLASS.put("implement", PROVIDE);
		KEYWORD_CLASS.put("deploy", PROVIDE);
		
		// Restrict
		KEYWORD_CLASS.put("restrict", RESTRICT);
		KEYWORD_CLASS.put("limit", RESTRICT);

		// Assure
		KEYWORD_CLASS.put("assure", ASSURE);
		KEYWORD_CLASS.put("ensure", ASSURE);

		// Permit
		KEYWORD_CLASS.put("permit", PERMIT);
		KEYWORD_CLASS.put("allow", PERMIT);
		KEYWORD_CLASS.put("admit", PERMIT);

		// Permit
		KEYWORD_CLASS.put("obey", OBEY);
		KEYWORD_CLASS.put("comply", OBEY);
		KEYWORD_CLASS.put("subject to", OBEY);
	}
	
	
	
	
	
	
		
//	
//	public static final Map<Integer, String> revsere_features; //= new HashMap<Integer, String>();
//	static{
//		revsere_features =reverseMap(features);
//	}
//
//	/**
//	 * Create a reverse map for faster query 
//	 * @param map
//	 * @return
//	 */
//	public static Map<Integer, String> reverseMap(Map<String, Integer> map){
//		Map<Integer, String> reverse = new HashMap<Integer,String>();
//		int temp;
//		for(String key: map.keySet()){
//			temp = map.get(key);
//			reverse.put(temp, key);
//		}
//		return reverse;
//	}
	
	
}
