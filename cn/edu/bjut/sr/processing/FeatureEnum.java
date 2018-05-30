package cn.edu.bjut.sr.processing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.ListUtils;

import cn.edu.bjut.text.utility.Log;
import edu.stanford.nlp.util.ArrayUtils;




public class FeatureEnum {
	//Training parameters
	public static final int TRAIN_KEY_NO = 0;
	public static final int TRAIN_KEY_SINGLE = 1;  // make a single feature, which holds true when any keyword is matched
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
	
	public static final int FT_SYNTAX_SLICE= 5;
	public static final int FT_SYNTAX_SLICE_NUMERIC= 6;
	
	public static final int FT_SEMANTIC_DEPENDENCY= 7;
	public static final int FT_SEMANTIC_DEPENDENCY_NUMERIC= 8;
	
//	//Types of syntactic analysis
//	public static final int SIMPLE_ANALYSIS= 0;
//	public static final int COMPLEX_ANALYSIS= 1;
	
	
	/*********************
	 *  SR features enumeration 
	 **********************/
	public static String CATEGORY = "class";//"category";
	public static String SENTENCE = "sentence";

	public static String[] SP = {
			//Security Property
			"security", "identification", "authentication", "authorization", 
			"integrity", "immunity", "anonymity", "confidentiality", "privacy",
			"availability", "non-repudiation", "accountability", "business continuity", "reliability",
	};
	// this set can have repated elements, as we will remove them when using them
	public static String[] SM = {
			// Security Mechanism
			"auditing", "access control", "key", "lock", "detection",
			"validation", "verification", "security education",
			"backup", "clear desk", "clear screen", "clock synchronization",
			"cryptography", "disposal", "logging", "monitoring", "segregation", "separation", //"pin", "awareness", "proof", 
			"password", "screening", "protection",
			"compliance", "restriction", "assurance", //"permission",
			/*new*/
			// from two-word phrases
			"security manangement", "digital signiture", "control", "security gateways", "firewall", 
			"encryption", "security mechanism","safeguard", "dmz", "demilitarized zone", "security check",
			// from three-word phrases
			"security policy", "security guideline", "security setting", "security strategy", 
			"security measure",
			// from four-word phrases
			"security process", 
			// tf-idf
			"troubleshoot", "diagnosis","key recovery", "system recovery", "trust network", "certification",
			"sandbox","antivirus","pki","ssl","puk", "checklist","ipsec", "virs scanning","awareness training",
			"security training",
			
			
			// detailed check based on SM titles
			"adapted segmentation",
			"galvanic separation",
			"sealing",
			"safety doors",
			"automatic drainage",
			"alarm system",
			"uninterruptible power supply", "ups",
			"safeguard",
			"safekeeping",
			"anti-theft",
			"surveillance",

			
	};
	public static String[] TH = {
			// Threat
			"fraud", "error", "bug", "vulnerability", "risk", "fault", "leakage", "misuse",
			"virus",
			/*new*/
			"hacker","loophole","threat","picklock","forgery","spyware","disclosure",

			/*new*/
			// from two-word phrases
			 "unauthorized", //"unauthorized asscess", "unauthorized person",
			 "attacker","danger","error",//"loss",    
			// from three-word phrases
			"trojan horse", 
			// from four-word phrases
			"brute force",  
			// tf-idf
			"damage","defect","burglary","flaw","violation","intrusion","steal",
			"tamper","destruction","malware","hazard", //"weakness",
			"leak","attack","manipulation","fraud","abuse","hijacking","spoof","falsify","detriment","abend",
			
/////			
			"incorrect",//"incorrect use", "incorrect handling", "incorrect entry", "incorrect action", "incorrect configuration", 
			"misjudgement",
			"insecure",//"insecure connection", "insecure channel", "insecure policy",
			"inadequate",//"inadequate administration",
			"improper",
			"maicious",
			"invalid", 
			"divulge"
			
			
	};
	public static String[] EL = {
			// Eliminate
			"eliminate", "get rid of", "stop", "reduce", "avoid", 
			/*new*/
			"annihilate","eradicate","wipe out", "extinguish","obviate","eradicate",
			"decimate","carry off","rule out", "winnow out", "reject",
			"free of",
	};
	public static String[] PR = {		
			// Protect
			"protect", "defend", "save",
	};
	public static String[] AC = {
			// Achieve
			"achieve", "obtain", "satisfy", "fulfill",
			/*new*/
			"accomplish", "attain","reach",
	};
	public static String[] PV = {
			// Provide
			"provide", "have", "implement", "deploy", 
			/*new*/
			"render", "supply","cater","offer", "furnish",
	};
	public static String[] RE = {
			// Restrict
			"restrict", "limit",
			/*new*/
			"curtail", "curb","restrain","bound","confine",
			"can only",
	};
	public static String[] AS = {
			// Assure
			"assure", "ensure",
			/*new*/
			"guarantee", "insure",  
	};
	public static String[] PE = {
			// Permit
			"permit", "allow", "admit",
			/*new*/
			"let", 
	};
	public static String[] OB = {
			// Obey
			"obey", "comply", "subject to", 
	};
			
	public static Object[] KEYWORD;
	
	// initialize KEYWORD by combining all keyword sets
	static {
		// remove duplicates 
		// Arrays.asList(SM).stream().distinct().collect(Collectors.toList());
		List list = new ArrayList(Arrays.asList(SP).stream().distinct().collect(Collectors.toList()));
	    list.addAll(Arrays.asList(SM).stream().distinct().collect(Collectors.toList()));
	    list.addAll(Arrays.asList(TH).stream().distinct().collect(Collectors.toList()));
//	    list.addAll(Arrays.asList(EL));
//	    list.addAll(Arrays.asList(PR));
//	    list.addAll(Arrays.asList(AC));
//	    list.addAll(Arrays.asList(PV));
//	    list.addAll(Arrays.asList(RE));
//	    list.addAll(Arrays.asList(AS));
//	    list.addAll(Arrays.asList(PE));
//	    list.addAll(Arrays.asList(OB));
	    
	    KEYWORD = list.toArray();
	}
	
	
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
		for (String s: SP) {
			KEYWORD_CLASS.put(s, SECURITY_PROPERTY);
		}
		
		//Security Mechanism
		for (String s: SM) {
			KEYWORD_CLASS.put(s, SECURITY_MECHANISM);
		}
		
		//Threat
		for (String s: TH) {
			KEYWORD_CLASS.put(s, THREAT);
		}
		
		// Eliminate
		for (String s: EL) {
			KEYWORD_CLASS.put(s, ELIMINATE);
		}
		
		// Protect
		for (String s: PR) {
			KEYWORD_CLASS.put(s, PROTECT);
		}
		
		// Achieve
		for (String s: AC) {
			KEYWORD_CLASS.put(s, ACHIEVE);
		}
		
		// Provide
		for (String s: PV) {
			KEYWORD_CLASS.put(s, PROVIDE);
		}
		
		// Restrict
		for (String s: RE) {
			KEYWORD_CLASS.put(s, RESTRICT);
		}
		
		// Assure
		for (String s: AS) {
			KEYWORD_CLASS.put(s, ASSURE);
		}
		
		// Permit
		for (String s: PE) {
			KEYWORD_CLASS.put(s, PERMIT);
		}
		
		// Obey
		for (String s: OB) {
			KEYWORD_CLASS.put(s, OBEY);
		}
		
		/*
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
		KEYWORD_CLASS.put("backup", SECURITY_MECHANISM);
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

		// Obey
		KEYWORD_CLASS.put("obey", OBEY);
		KEYWORD_CLASS.put("comply", OBEY);
		KEYWORD_CLASS.put("subject to", OBEY);
		*/
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
