package cn.edu.bjut.text.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cn.edu.bjut.text.processing.EnglishProcessing;
import cn.edu.bjut.text.processing.PatternMatch;
import cn.edu.bjut.text.processing.Stemmer;
import cn.edu.bjut.text.processing.TextProcessing;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.Pair;
import weka.core.AbstractInstance;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

/**
 * This class handles all the operations related to ARFF file Include import, export, modification etc.
 * 
 * @author Tong Li
 * 
 */
public class ARFF {
	public static Instances data = null;
	

	/**
	 * Load an arff file
	 * @throws IOException
	 */
	public static void loadARFF(String file_path) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file_path));
		data = new Instances(reader);
		reader.close();
		// setting class attribute
		data.setClassIndex(data.numAttributes() - 1);
//		System.out.println(data.get(1).stringValue(1));
	}
	

	/**
	 * Save current Instances to a particular file
	 * @throws IOException
	 */
	public static void saveARFF(String file_path) throws IOException{
		 // save ARFF
	    ArffSaver saver = new ArffSaver();
	    saver.setInstances(data);
	    saver.setFile(new File(file_path));
//	    saver.setDestination(new File(args[1]));
	    saver.writeBatch();
	}
	
	/**
	 * adding attributes to the existing Instances and create a new set of Instances
	 * @throws IOException
	 */
	public static void addAttributesToExistingARFF(ArrayList<Attribute> new_atts, LinkedList<LinkedList<String>> atts_values) throws IOException{
		if(data!=null){
//			ArrayList<Attribute> new_atts = new ArrayList<Attribute>();
			for(int i=0; i<new_atts.size(); i++){
				// insert the new attribute at the second to last position, i.e., leave the "class" attribute to the last 
				data.insertAttributeAt(new_atts.get(i), data.numAttributes()-1);
				// For the new attribute, set new values to all instances.
				for(int j=0; j<data.numInstances(); j++){
					Instance ins = data.get(j);
					if(new_atts.get(i).isString()||new_atts.get(i).isNominal()){
						String value = atts_values.get(i).get(j);
				        ins.setValue(data.numAttributes()-2, value);
					}
					else if(new_atts.get(i).isNumeric()){
						double value = Double.parseDouble(atts_values.get(i).get(j));
				        ins.setValue(data.numAttributes()-2, value);
					}
			      }
			}
		}
		else{
			System.out.println("Data has not been loaded");
		}
	}
	
	
	


	/**
	 * define particular attributes and collect their values
	 * This function serves as a template
	 * @param new_atts
	 * @param atts_values
	 */
	private static void prepareNewAttributes(ArrayList<Attribute> new_atts, LinkedList<LinkedList<String>> atts_values) {
		LinkedList<String> att_values = new LinkedList<String>();
		// create a single nominal attribute 
		List<String> attributeValues = new LinkedList<String>();
		attributeValues.add("Yes");
		attributeValues.add("No");
		Attribute att = new Attribute("VN pattern", attributeValues);
		new_atts.add(att);
		// the number of values should be the same with the number of instances in current arff file
		for(int i=0; i<data.numInstances(); i++){
			att_values.add("Yes");
		}
		atts_values.add(att_values);
		
		// create a single string attribute 
		attributeValues = null;
		att = new Attribute("Security word", attributeValues);
		new_atts.add(att);
		// prepare corresponding values for the new attributes
		att_values = new LinkedList<String>();
		// the number of values should be the same with the number of instances in current arff file
		for(int i=0; i<data.numInstances(); i++){
			att_values.add("confidentiality");
		}
		atts_values.add(att_values);
		
		
		// create a single real attribute 
		att = new Attribute("score");
		new_atts.add(att);
		// prepare corresponding values for the new attributes
		att_values = new LinkedList<String>();
		// the number of values should be the same with the number of instances in current arff file
		for(int i=0; i<data.numInstances(); i++){
			att_values.add("100.001");
		}
		atts_values.add(att_values);
	}
	
	/**
	 * Add a couple of new attributes for better learning
	 * @throws IOException 
	 */
	public static void prepareSRAtrributes() throws IOException{
		// prepare the list of new attributes
		ArrayList<Attribute> new_atts = new ArrayList<Attribute>();
		// prepare corresponding values for the new attributes
		LinkedList<LinkedList<String>> atts_values = new LinkedList<LinkedList<String>>();


		// create specific attributes and their values
		createSecurityTermAttribute(new_atts, atts_values);
		
		createSecurityConstraintsAttribute(new_atts, atts_values);
		
		
		// modify arff files
		addAttributesToExistingARFF(new_atts,atts_values);
	}

	/**
	 * create a particular attribute
	 * @param new_atts
	 * @param atts_values
	 */
	private static void createSecurityTermAttribute(ArrayList<Attribute> new_atts, LinkedList<LinkedList<String>> atts_values) {
		// create a numeric attribute to indicate whether security terms are included
		Attribute att = new Attribute("SecurityTerms");		
		new_atts.add(att);
		// add a corresponding value list 
		LinkedList<String> att_values = new LinkedList<String>();
		atts_values.add(att_values);
		// calculate corresponding values for the new attributes
		for(int i=0; i<data.numInstances(); i++){
			// obtain the first attribute of a data instance, which is SR text
			String sr_text = data.instance(i).stringValue(data.instance(i).attribute(0));
			if(TextProcessing.arrayContainString(IEnum.SecurityTerms, sr_text)){
				att_values.add("1");
			}else{
				att_values.add("0");
			}
		}
	}
	
	/**
	 * create a particular attributes
	 * @param new_atts
	 * @param atts_values
	 */
	private static void createSecurityConstraintsAttribute(ArrayList<Attribute> new_atts, LinkedList<LinkedList<String>> atts_values) {
		// this function needs lexical processing
		LexicalizedParser lp = null;
		lp = LexicalizedParser.loadModel("parser/models/lexparser/englishPCFG.ser.gz");

		// create a numeric attribute to indicate whether security terms are included
		Attribute att = new Attribute("SecurityConstraints");		
		new_atts.add(att);
		// add a corresponding value list 
		LinkedList<String> att_values = new LinkedList<String>();
		atts_values.add(att_values);
		// calculate corresponding values for the new attributes
		for(int i=0; i<data.numInstances(); i++){
			// obtain the first attribute of a data instance, which is SR text
			String sr_text = data.instance(i).stringValue(data.instance(i).attribute(0));
			
			if(TextProcessing.arrayContainString(IEnum.ComplianceIndicator, sr_text)){
				// segment words
				String[] words = sr_text.split(" ");
				TextProcessing.formatWords(words);
				// parse
				Tree parse = TextProcessing.parseSentence(lp, words, IEnum.ENGLISH, false);
				// stem the sr description
				String sr_stem = EnglishProcessing.sentenceStemming(sr_text);
				// stem the obtained penn tree 
				EnglishProcessing.parseTreeStemming(parse);
				
				// apply particular pattern rules to different indicators
				boolean matched = false;
				if(sr_stem.contains("subject to")){
					for (String pattern: IEnum.SubjectToPattern){
						if (PatternMatch.matchPatterns(parse, pattern) != null) {
							matched=true;
							break;
						}
					}
				}
				else if (sr_stem.contains("compli")){
					for (String pattern: IEnum.ComplyPattern){
						if (PatternMatch.matchPatterns(parse, pattern) != null) {
							matched=true;
							break;
						}
					}
				}
				// assign values to the attribute
				if(matched){
					att_values.add("1");
					System.out.println(sr_text);
				}else{
					att_values.add("0");
				}
			}
			else{
				att_values.add("0");
			}
		}
	}
	
	
	public static void main(String Args[]) throws IOException {
		
		loadARFF("test files/three/ePurse.arff");
		
		
		prepareSRAtrributes();
		
		
		saveARFF("test files/three/ePurse_r.arff");
		}

}

