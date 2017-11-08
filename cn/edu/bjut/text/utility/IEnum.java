package cn.edu.bjut.text.utility;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enumerate all types and other information used in this project
 * E.g., types of pattern, POS tags, etc.
 * This file is copied from TLSRAF...
 * @author Tong Li
 *  
 */
public class IEnum {
	// private static final String[] REQUIREMENT_ELEMENT_TYPE = { "positive",
	// "negative",
	// "unknown" };
	
	//Object parameters
	public static final int ENGLISH= 0;
	public static final int CHINESE= 1;

	/*********************
	 *  New sets of security related terms according to the 
	 **********************/
	public static String[] SecurityProperty = {
		// property, corresponding verb (stem), corresponding adj, typical synonyms,
		// to be easier, we can only analyze its "stemming form".
		//security property
		"security","secure",
		"access control", 
		"identification","identif",
		"authentication", "authenticity","authenticate","authentic",
		"authorization", "authroize",
		"non-repudiation","repudiation","repudiate",
		"integrity",
		"immunity","immune",
		"auditing","audit",
		"privacy","private",
		"anonymity","anonymous",
		"confidentiality","confidential","secret",
	};
	
	// this should be determined based on security glossary and catalog
	public static String[] SecurityMechanism = {
		// mechanism, key verb (stem), typical synonyms,
		"encryption", "decryption","encrypt","decrypt","cipher",
		"proof","log",
		"key","pin","lock",
		"detection","detect",
		"validation","valid",
		"verification",
	};

	
	// this is determined based on linguistic knowledge and security knowledge as well
	public static String[] Assurance = {
		// imply assurance
		" ensur ", //ensure
		" assur ", //assure
		" verifi", // verify
		" validat ", //validate
	};
	
	// this is determined based on linguistic knowledge and security knowledge as well
	// antonyms are also required
		public static String[] Permission = {
			// imply assurance
			" permiss ", "permit", // permission
			" allow ",
			" admiss ",	" admit ", // admission
			" deni ", //deny
			" revok ", //revoke
			" reject ", 
			" disallow ",
		};

	// a list of verbs that indicate compliance constraints
		// antonyms are also required
		public static String[] Compliance = {
			// imply compliance
			" obei ", " disobei ",//"obey"
			" compli ", // "comply",
			" subject to ",
			" enforce " //stem
	};
	
	
	
	
	
	
	// A set of security qualities, certainly need to further expand 
	public static String[] SecurityTerms = {
		// property, corresponding verb (stem), corresponding adj, typical synonyms,
		//security property
		"security","secure",
		"access control", 
		"identification","identif",
		"authentication", "authenticity","authenticate","authentic",
		"authorization", "authroize",
		"non-repudiation","repudiation","repudiate",
		"integrity",
		"immunity","immune",
		"auditing","audit",
		"privacy","private",
		"anonymity","anonymous",
		"confidentiality","confidential","secret",
		//security concepts
		"attack",
		"harm",
		"protection","protect",
		"recovery","recover",
		"prosecution",
		"vulnerability","vulnerable",
		"risk","threat",
		// attack terms
		"fraud",
		"error",
		"bug",
		//security mechanisms
		"encryption", "decryption","encrypt","decrypt","cipher",
		"proof","log",
		"key","pin","lock",
		"detection","detect",
		"validation","valid",
		"verification",
		};
	
	// a list of verbs that indicate compliance constraints
	public static String[] ComplianceIndicator = {
		// imply compliance
		" obei ", //"obey"
		" compli ", // "comply",
		" subject to ",
	};

	
	/******************************
	*
	* Concrete sentence match patterns for different indicators
	*
	*******************************/
	public static String[] SubjectToPattern = {
		// imply compliance
		"ADJP=adjp <<, (subject . to) << (to .. regul) << regul",
		"ADJP=adjp <<, (subject . to) << (to .. law) << law",
		"ADJP=adjp <<, (subject . to) << (to .. rule) << rule",
	};
	
	public static String[] ComplyPattern = {
		// imply compliance
		"VP=vp <<, (comply . with) << (with .. regul) << regul",
		"VP=vp <<, (comply . with) << (with .. law) << law",
		"VP=vp <<, (comply . with) << (with .. rule) << rule",
	};
	
	public static String[] ObeyPattern = {
		// imply compliance
		"VP=vp <<, obey << regul",
		"VP=vp <<, obey << law",
		"VP=vp <<, obey << rule",
	};
	
	
	
	
	
	
	
	
	// A set of security actions, which should be further investigated according to their 
	public static String[] SecurityActions = {
		// imply permission
//		"allow","permit",
		// imply validity
		"validate", 
		"verify", "verif", 
//		"check", 
		// imply a condition
		"ensure", 
		// imply a mechanism,
		"enforce",  
		// imply compliance
		"obey","obeie",
		"comply","compli",
		"subject to",
	};
	
	
	
	public static String[] ModalVerbs = {
		"must",
		"have to","has to","had to",
	};
	
	
	
	
	
	
	
	
	
	
	
		
	// A set of domain keywords that are normally associated with security issues 
	public static String[] DomainWords = {
		"Network",
		"Gateway"
	};
			
		
	
	
	
	
	
	
	
	
	
	
	
		
		
		
		
	
	
	
	// canvas names
	public static final Map<String, String> esg_canvas_mapping = new HashMap<String, String>();
	static {
		esg_canvas_mapping.put(IEnum.Layer.BUSINESS.name(), "Business SG");
		esg_canvas_mapping.put(IEnum.Layer.APPLICATION.name(), "Application SG");
		esg_canvas_mapping.put(IEnum.Layer.PHYSICAL.name(), "Physical SG");
	}
	
	// anti-goal canvas names
	public static final Map<String, String> eag_canvas_mapping = new HashMap<String, String>();
	static {
		eag_canvas_mapping.put(IEnum.Layer.BUSINESS.name(), "Business AG");
		eag_canvas_mapping.put(IEnum.Layer.APPLICATION.name(), "Application AG");
		eag_canvas_mapping.put(IEnum.Layer.PHYSICAL.name(), "Physical AG");
	}

	
	// determine type of scanned shape
	public static final Map<String, String> req_elem_type_map = new HashMap<String, String>();
	static {
		req_elem_type_map.put("Circle", IEnum.RequirementElementType.GOAL.name());
		req_elem_type_map.put("Cloud", IEnum.RequirementElementType.SOFTGOAL.name());
		req_elem_type_map.put("Hexagon", IEnum.RequirementElementType.TASK.name());
		req_elem_type_map.put("Diamond", IEnum.RequirementElementType.QUALITY_CONSTRAINT.name());
		req_elem_type_map.put("Rectangle", IEnum.RequirementElementType.DOMAIN_ASSUMPTION.name());
	}

	// determine type of scanned shape
	public static final Map<String, String> reverse_req_elem_type_map = new HashMap<String, String>();
	static {
		reverse_req_elem_type_map.put(IEnum.RequirementElementType.GOAL.name(), "Circle");
		reverse_req_elem_type_map.put(IEnum.RequirementElementType.ANTI_GOAL.name(), "Circle");
		reverse_req_elem_type_map.put(IEnum.RequirementElementType.ACTOR.name(), "Circle");
		reverse_req_elem_type_map.put(IEnum.RequirementElementType.MIDDLE_POINT.name(), "Circle");
		reverse_req_elem_type_map.put(IEnum.RequirementElementType.SOFTGOAL.name(), "Cloud");
		reverse_req_elem_type_map.put(IEnum.RequirementElementType.SECURITY_GOAL.name(), "Cloud");
		reverse_req_elem_type_map.put(IEnum.RequirementElementType.TASK.name(), "Hexagon");
		reverse_req_elem_type_map.put(IEnum.RequirementElementType.SECURITY_MECHANISM.name(), "Hexagon");
		reverse_req_elem_type_map.put(IEnum.RequirementElementType.QUALITY_CONSTRAINT.name(), "Diamond");
		reverse_req_elem_type_map.put(IEnum.RequirementElementType.DOMAIN_ASSUMPTION.name(), "Rectangle");
	}

	
	
	// determine layer of each security mechanism, especially the one that is related to 
	public static final Map<String, String> security_mechanisms = new HashMap<String, String>();
	static {
		security_mechanisms.put("cryptographic_control", IEnum.Layer.BUSINESS.name());
		security_mechanisms.put("access_control", IEnum.Layer.BUSINESS.name());
		security_mechanisms.put("auditing", IEnum.Layer.BUSINESS.name());
		security_mechanisms.put("backup", IEnum.Layer.BUSINESS.name());

		security_mechanisms.put("full_view_with_errors", IEnum.Layer.APPLICATION.name());
		security_mechanisms.put("limited_view", IEnum.Layer.APPLICATION.name());
		security_mechanisms.put("secure_pipe", IEnum.Layer.APPLICATION.name());
		security_mechanisms.put("encrypted_storage", IEnum.Layer.APPLICATION.name());
		security_mechanisms.put("secure_access_layer", IEnum.Layer.APPLICATION.name());
		security_mechanisms.put("data_backup", IEnum.Layer.APPLICATION.name());
		security_mechanisms.put("server_sand_box", IEnum.Layer.APPLICATION.name());
		security_mechanisms.put("input_guard", IEnum.Layer.APPLICATION.name());
		security_mechanisms.put("firewall", IEnum.Layer.APPLICATION.name());
		security_mechanisms.put("replicated_system", IEnum.Layer.APPLICATION.name());
		security_mechanisms.put("load_balancer", IEnum.Layer.APPLICATION.name());

		security_mechanisms.put("secure_office", IEnum.Layer.PHYSICAL.name());
		security_mechanisms.put("physical_entry_control", IEnum.Layer.PHYSICAL.name());
		security_mechanisms.put("monitor", IEnum.Layer.PHYSICAL.name());
		security_mechanisms.put("anti_tamper_protection", IEnum.Layer.PHYSICAL.name());
		security_mechanisms.put("ups", IEnum.Layer.PHYSICAL.name());

	}
	
	// record questions for checking undecidable context
	public static final Map<String, String> pattern_context_question = new HashMap<String, String>();
	static {
		// ids-c1
		pattern_context_question.put("question(ids_c1q1)", "are there nodes communicating with each other using the Internet?");
		pattern_context_question.put("question(ids_c1q1)y", "communicate(system_node, internet).");
		pattern_context_question.put("question(ids_c1q1)n", "dis_communicate(system_node, internet).");
		// ids-c2
		pattern_context_question.put("question(ids_c2q1)", "are requests coming from a non-suspicious address harmful?");
		pattern_context_question.put("question(ids_c2q1)y", "harmful(request_from_non_suspicious_address).");
		pattern_context_question.put("question(ids_c2q1)n", "non_harmful(request_from_non_suspicious_address).");
		// ids-c3
		pattern_context_question.put("question(ids_c3q1)", "is there sufficient and appropriate information?");
		pattern_context_question.put("question(ids_c3q1)y", "sufficient(attack_information).");
		pattern_context_question.put("question(ids_c3q1)n", "not_sufficient(attack_information).");
		// audit-c1
		pattern_context_question.put("question(audit_c1q1)", "does the system handle sensitive data?");
		pattern_context_question.put("question(audit_c1q1)y", "handle(system, sensitive_data).");
		pattern_context_question.put("question(audit_c1q1)n", "not_handle(system, sensitive_data).");
	}
	

	// Types of elements and links
	public enum ModelCategory {
		REQUIREMENT, BUSINESS_PROCESS, SOFTWARE_ARCHITECTURE, DEPLOYMENT,
		ASSET, ACTOR, HOLISTIC_SECURITY_GOAL_MODEL, THREAT_MODEL, RESOURCE_SCHEMA, DATA_FLOW,
		ATTACK_MODEL
	}
	
	// Dimensions of refinements
	public enum RefinementDimension {
		SECURITY_PROPERTY,ASSET,INTERVAL,THREAT,TARGET,PROTECTION
	}

	/*
	 * Types of elements and links, they should cover all types of links.
	 * Additional information of these elements, could be added in the remark
	 * part.
	 */
	public enum RequirementElementType {
		ACTOR, GOAL, TASK, SOFTGOAL, DOMAIN_ASSUMPTION, QUALITY_CONSTRAINT, 
		SECURITY_GOAL, SECURITY_MECHANISM, ACTOR_BOUNDARY, MIDDLE_POINT, LABEL, RESOURCE // syntax
		, ANTI_GOAL, NEW_ANTI_GOAL
	}

	public enum RequirementLinkType {
		REFINE, AND_REFINE, OPERATIONALIZE, PREFERRED_TO, DEPEND, TRUST, SUPPORT, MAKE, HELP, HURT, BREAK,
		USE, MAINTAIN, OWN, // used here as 
		AND_REFINE_ARROW//REDUNDANT_LINK
	}

	public enum ResourceElementType {
		RESOURCE
	}

	// Other enumerations
	public enum Layer {
		BUSINESS, APPLICATION, PHYSICAL, ALL
	}

	public enum AssetType {
		SERVICE, DATA, APPLICATION, HARDWARE
	}

	public enum SGImportance {
		Low, Medium, High
	}

	/*
	 * public enum SGSecurityAttribute { Security, Confidentiality,
	 * ServiceConfidentiality, DataConfidentiality, ApplicationConfidentiality,
	 * HardwareConfidentiality, Integrity, ServiceIntegrity, DataIntegrity,
	 * ApplicationIntegrity, HardwareIntegrity, Availability,
	 * ServiceAvailability, ApplicationAvailability, HardwareAvailability }
	 */

	//Except for normal, all other remarks deplete the element
	public enum ElementRemark {
		NORMAL, TRUSTUM, DEPENDUM, REFINEUM, BOUNDARY, SUPPORTUM, TOPSG, BESTPATH
	}

	public enum LinkRemark {
		NORMAL, BESTPATH, REDUNDANT//DEPLETED TRUST, DEPEND, REFINE ARROW, MAKE, HELP,
	}

	// Additional remark
	public enum RefineType {
		ATTRIBUTE, ASSET, INTERVAL
	}
	
	// Security Property
	public enum SecurityProperty {
		Confidentiality, Integrity, Availability, All
	}
	
	// Attack Domain (according to CAPEC)
	public enum AttackDomain {
		SOCIAL, SUPPLY, COMMU, SOFTWARE, PHYSICAL, HARDWARE, ALL
	}
	
	/*
	 * enumerate all commands that can be done by the tool
	 */
	public enum Commands {
		IMP_SELECTION, IMP_FILE, REF_ALL_ONE_STEP, REF_ALL_EXHAUSTIVE
	}
	
	public static String[] threats = {"spoofing","tampering","repudiation","information disclosure","denial of service", "elevation of privilege" };
	
	
	//Current directory
	public static String current_directory = System.getProperty("user.dir");
//	public static String current_directory = "/Users/litong30/research/Trento/Working stuff/Case study/revised smart grid";
	public static String drawing_method_file = System.getProperty("user.dir")+"/applescript/drawing_methods.applescript";

	
	
	
	// determine type of scanned shape
	public static final Map<String, String> security_mechanism_cost = new HashMap<String, String>();
	static {
		// business layer
		security_mechanism_cost.put("alternative_service", "1151");
		security_mechanism_cost.put("client_checking", "1222");
		security_mechanism_cost.put("separation_of_duty", "2173");
		security_mechanism_cost.put("certification_authority", "1234");
		security_mechanism_cost.put("supervision_relation", "2165");
		security_mechanism_cost.put("access_control", "1486");
		security_mechanism_cost.put("auditing", "1597");
		// application layer
		security_mechanism_cost.put("input_guard", "6028");
		security_mechanism_cost.put("firewall", "4049");
		security_mechanism_cost.put("server_sandbox", "2160");
		security_mechanism_cost.put("replicated_system", "1201");
		security_mechanism_cost.put("load_balancer", "3042");
		security_mechanism_cost.put("limited_view", "2083");
		security_mechanism_cost.put("full_view_with_errors", "2184");
		security_mechanism_cost.put("secure_access_layer", "1265");
		security_mechanism_cost.put("secure_pipe", "3076");
		security_mechanism_cost.put("storage_encryption", "2047");
		// physical layer
		security_mechanism_cost.put("equipment_siting_and_protection", "3038");
		security_mechanism_cost.put("supporting_utility", "2059");
		security_mechanism_cost.put("physical_entry_control", "5400");
		security_mechanism_cost.put("cabling_security", "1251");
	}
}
