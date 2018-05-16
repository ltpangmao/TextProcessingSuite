package cn.edu.bjut.sr.processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;

import cn.edu.bjut.text.processing.EnglishProcessing;
import cn.edu.bjut.text.utility.IEnum;
import cn.edu.bjut.text.utility.Log;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;




public class SRProcessing {
	LinkedList<FeaturedSentence> sentences = new LinkedList<FeaturedSentence>();
	
	private int rule_temp_count = 0;
	private int keyword_temp_count = 0;
	
	private String[] data_files = {"original data//ePurse-selective.csv", "original data//CPN.csv", "original data//GPS.csv", };
	final String EPUSE = "0";
	final String CPN = "1";
	final String GPS = "2";
	
	
	/**
	 * This is a customized function for generating various dataset, in which parameters represent conditions
	 * @param ori_data
	 * @param include_class
	 * @param include_sen
	 * @param single_keyword
	 * @param single_rule
	 */
	public String processDataAndGenerateArff(String ori_data, boolean include_class, boolean include_sen,  int keyword, int rule, int dep, WekaAnalysisDataset wa) {
		String output_file_path = "output data/"; // this will be incrementally modified during the procedure
	
		// import files
		for (Character c : ori_data.toCharArray()) {
			importSentencesFromCSV(data_files[Integer.valueOf(c.toString())], include_class, include_sen);
			// generate file name
			output_file_path += c + "_";
		}
	
		// whether contain the original sentence as a feature
		if (include_sen) {
			output_file_path += "sen_";
		} else {
			// output_file_path+="nsen_";
		}
	
		// match keywords
	
		if (keyword == FeatureEnum.TRAIN_KEY_SINGLE) {
			keywordMatchSingle();
			// generate file name
			output_file_path += "skey_";
		} else if (keyword == FeatureEnum.TRAIN_KEY_MULTI) {
			keywordMatch();
			output_file_path += "mkey_";
		} else if (keyword == FeatureEnum.TRAIN_KEY_NO) {
	
		} else {
			Log.error("unexpected keyword parameter: " + keyword);
		}
	
		// match linguistic rules based on particular syntactic rules
		if (rule == FeatureEnum.TRAIN_RULE_SINGLE) {
			linguisticAnalysis(false, true);
			output_file_path += "srule_";
		} else if (rule == FeatureEnum.TRAIN_RULE_MULTI) {
			linguisticAnalysis(false, false);
			output_file_path += "mrule_";
		} else if (keyword == FeatureEnum.TRAIN_RULE_NO) {
	
		} else {
			Log.error("unexpected rule parameter: " + rule);
		}
	
		// match dependency rules based on dependency analysis
		if (dep == FeatureEnum.TRAIN_DEP_NO) {
			output_file_path = output_file_path.substring(0, output_file_path.length() - 1);
			output_file_path += ".arff";
		} else if (dep == FeatureEnum.TRAIN_DEP_SINGLE) {
			dependencyAnalysis(true);
			output_file_path += "sdep.arff";
		} else if (dep == FeatureEnum.TRAIN_DEP_MULTI) {
			dependencyAnalysis(false);
			output_file_path += "mdep.arff";
		} else {
			Log.error("unexpected dependency parameter: " + dep);
		}
	
		wa.generateWekaDataFromSRP(sentences, include_class, include_sen, output_file_path);
	
		return output_file_path;
	}


	/**
	 * print current information of the featured dataset
	 */
	public void printData(){
		boolean title =false;
		for (FeaturedSentence fs : sentences){
			//generate titles
			if (!title) {
				String first_row = ""; //FeatureEnumeration.SENTENCE+" ";
				for (SRFeature srf: fs.features) {
					first_row += srf.getFeature_name() + " ";
				}
				title = true;
				Log.debug(first_row);
			}
			// generate content
			String content = ""; //fs.sentence;
			for (SRFeature srf: fs.features) {
				content += srf.getFeature_value() + " ";
			}
			Log.debug(content);
		}
	}


	/**
	 * import a list of tagged sentences from csv files
	 * modify global variable
	 * @param filePath
	 */
	private void importSentencesFromCSV(String filePath, boolean include_class, boolean include_sen) {
		try {
			File file = new File(filePath);
			BufferedReader bf = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String line = "";
			String[] temp = null;
			while ((line = bf.readLine()) != null) {
				// each sentence + ; + tag
				temp = line.split(";");
				if (temp == null || temp.length != 2) {
					Log.error("Errors in spliting sentences and tags!" + line);
				}
				else {
					FeaturedSentence fs = new FeaturedSentence(temp[0]);
					// include class attribute if required
					if (include_class) {
						// first add the category feature
						if (temp[1].equals("sec")) {
							fs.features.add(new SRFeature(FeatureEnum.CATEGORY, FeatureEnum.FT_CATEGORY, "1"));
						} else if (temp[1].equals(("nonsec"))) {
							fs.features.add(new SRFeature(FeatureEnum.CATEGORY, FeatureEnum.FT_CATEGORY, "0"));
						} else {
							Log.error("Errors of imported classification!\n" + temp[1] + " " + temp[0]);
						}
					}
					if(include_sen) {
						// add the sentence itself as a feature, if required
						fs.features.add(new SRFeature(FeatureEnum.SENTENCE, FeatureEnum.FT_SENTENCE,temp[0]));
					}
					// add the processed sentence to our repository
					sentences.add(fs);
				}
			}
			bf.close();
			Log.debug("imported sentences:"+sentences.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
//		// don't know why it is quite slow...
//		for(int i=0;i<sentences.size()-3;i++) {
//			sentences.removeLast();
//		}
	}
	
	
	/**
	 * Further process imported sentences to see whether they contain predefined keywords
	 */
	private void keywordMatch() {
		// traverse all trees' all nodes against all keywords
		String s1, s2 = "";
		String sr_concept = "";
		for (FeaturedSentence fs : sentences) {
			for (Object temp : FeatureEnum.KEYWORD) {
				String kw = (String)temp;
				s1 = EnglishProcessing.wordStemming(kw);
				s2 = EnglishProcessing.sentenceStemming(fs.ori_sentence);
				if (s2.contains(s1)) {// if keyword is found,
					// add feature value
					fs.features.add(new SRFeature(kw, FeatureEnum.FT_KEYWORD, "1"));
					// update sentence
					sr_concept = FeatureEnum.KEYWORD_CLASS.get(kw);
					fs.modi_sentence = s2.replaceAll(s1, sr_concept);
					// for debug
					keyword_temp_count++;
				} else {
					fs.features.add(new SRFeature(kw, FeatureEnum.FT_KEYWORD, "0"));
				}
			}
		}
		Log.debug("sentences that contain keyword:" + keyword_temp_count);
	}
	
	/**
	 * This method will prepare only one keyword-based feature
	 * Currently, we don't account the multiple appearances of keywords, only record 1 as long as one keyword is found. 
	 */
	private void keywordMatchSingle() {
		// traverse all trees' all nodes against all keywords
		String s1, s2 = "";
		String sr_concept = "";
		boolean keyword_found; // prepared for the single feature processing
		for (FeaturedSentence fs : sentences) {
			keyword_found = false;
			for (Object temp : FeatureEnum.KEYWORD) {
				String kw = (String)temp;
				s1 = EnglishProcessing.wordStemming(kw);
				s2 = EnglishProcessing.sentenceStemming(fs.ori_sentence);
				if (s2.contains(s1)) {// if keyword is found,
					// add feature value, if found, record 1
					fs.features.add(new SRFeature("keyword", FeatureEnum.FT_KEYWORD, "1"));
					// update sentence
					sr_concept = FeatureEnum.KEYWORD_CLASS.get(kw);
					fs.modi_sentence = s2.replaceAll(s1, sr_concept);
					// mark
					keyword_found = true;
					break;
				}
			}
			if (!keyword_found) {
				// add feature value, if found, record 1
				fs.features.add(new SRFeature("keyword", FeatureEnum.FT_KEYWORD, "0"));
			} else {
				// for debug
				keyword_temp_count++;
			}
		}
		Log.debug("sentences that contain keyword:" + keyword_temp_count);
	}
	
	
	
	
	/**
	 * simple linguistic rule matching based on textual sentence, using regular expression
	 * complicated linguistic rule matching based on parse tree, using TregexPattern
	 */
	private void linguisticAnalysis(boolean tregex, boolean single){
		// may need this for complicated syntactic analysis
		LexicalizedParser lp = null;
		lp = LexicalizedParser.loadModel("parser/models/lexparser/englishPCFG.ser.gz");
		
		for (FeaturedSentence fs: sentences){
			// if no keyword was found in this sentence, then all rule-based features are set to 0
			if(fs.modi_sentence.equals("")) {
				if(single) {
					fs.features.add(new SRFeature("rule", FeatureEnum.FT_RULE,"0"));
				}
				else {
					for(String rule: FeatureEnum.RULE) {
						fs.features.add(new SRFeature(rule, FeatureEnum.FT_RULE,"0"));
					}
				}
			}
			else {
				// complex analysis
				if (tregex) {
					// parse sentences and generated a stemmed version, only for candidate match
					String[] words = fs.ori_sentence.split(" ");
					fs.parse = EnglishProcessing.parseSentence(lp, words, IEnum.ENGLISH, false);
					fs.stemmed_parse = EnglishProcessing.parseTreeStemmingNew(fs.parse);
					// match exact linguistic rules
					ruleMatch(fs);
				}
				// simple analysis
				else {
					if(single) {
						simpleRuleMatchSingle(fs);
					}
					else {
						simpleRuleMatch(fs);
					}
				}
			}
		}
		Log.debug("sentences that match rules:"+rule_temp_count);
	}
	
	
	
	
	/**
	 * Syntactically match all rules and record the results
	 * This method considers the simple case, i.e., using regular expressions to match the syntactic rules, and even don't do analyze POS tags.
	 * @param fs
	 */
	private void simpleRuleMatch(FeaturedSentence fs){
		String target = fs.modi_sentence;
		String rule_content = "";
		
		for(String rule_name: FeatureEnum.RULE) {
			rule_content = FeatureEnum.LINGUISTIC_RULES.get(rule_name);
			if(target.matches(rule_content)) {
				fs.features.add(new SRFeature(rule_name, FeatureEnum.FT_RULE, "1"));
				//for debug
				rule_temp_count++;
			} else {
				fs.features.add(new SRFeature(rule_name, FeatureEnum.FT_RULE, "0"));
			}
		}
	}
	
	/**
	 * This method functions similar with the previous one, except that it produces only one rule-related feature
	 * @param fs
	 */
	private void simpleRuleMatchSingle(FeaturedSentence fs) {
		String target = fs.modi_sentence;
		String rule_content = "";
		boolean rule_match = false;

		for (String rule_name : FeatureEnum.RULE) {
			rule_content = FeatureEnum.LINGUISTIC_RULES.get(rule_name);
			if (target.matches(rule_content)) {
				rule_match = true;
				break;
			}
		}
		if (rule_match) {
			fs.features.add(new SRFeature("rule", FeatureEnum.FT_RULE, "1"));
			// for debug
			rule_temp_count++;
		} else {
			fs.features.add(new SRFeature("rule", FeatureEnum.FT_RULE, "0"));
		}
	}
	
	/**
	 * Syntactically match all rules and record the results
	 * This method is supposed to match syntactic rules using TregexPattern, i.e., the complicated case
	 * @param fs
	 */
	private void ruleMatch(FeaturedSentence fs){
		
	}
	
	
	
	
	private void dependencyAnalysis(boolean single) {
		// Create NLP analyzer
		Properties props = new Properties();
		// props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
		props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
		StanfordCoreNLP analyzer = new StanfordCoreNLP(props);
		
		for (FeaturedSentence fs: sentences){
			fs.dep_trees = EnglishProcessing.dependencyAnalysisGivenAnalyzer(analyzer, fs.ori_sentence);
			
			if (single) {
				// fs.features.add(new SRFeature("dep", FeatureEnum.FT_DEPENDENCY,"0"));
			} else {
				// for(String dep: FeatureEnum.DEPENDENCY) {
				// fs.features.add(new SRFeature(dep, FeatureEnum.FT_DEPENDENCY,"0"));
				// }
			}

			if (fs.features.get(0).getFeature_value().equals("1")) {//print secure ones for learning
				System.out.println(fs.ori_sentence);
				
				for (SemanticGraph sg : fs.dep_trees) {
					Collection<TypedDependency> dependencies = sg.typedDependencies();
					for(TypedDependency td: dependencies) {
						if (td.gov() != null && td.dep() != null) {
							// gov
							String gov_pos_tag = td.gov().toString();
							int p1 = gov_pos_tag.indexOf("/");
							if (p1!=-1) {
								gov_pos_tag = gov_pos_tag.substring(p1+1);
							}
							// dep
							String dep_pos_tag = td.dep().toString();
							int p2 = dep_pos_tag.indexOf("/");
							if (p2!=-1) {
								dep_pos_tag = dep_pos_tag.substring(p2+1);
							}
							System.out.println(td.reln() + "(" + gov_pos_tag + ", " + dep_pos_tag + ")");
						}
					}
//					System.out.println(sg.toString(SemanticGraph.OutputFormat.READABLE));
				}
			}
		}
	}
	
	
	/**
	 * import data file and produce a set of syntax slices for clustering 
	 * @param ori_data
	 * @param wa
	 * @param feature_type
	 * @return
	 */
	public String processDataForSyntaxCluster(String ori_data, WekaAnalysisDataset wa, int feature_type) {
		String output_file_path = "output data/"; // this will be incrementally modified during the procedure
		// import files
		for (Character c : ori_data.toCharArray()) {
			// Note that here we include classification
			importSentencesFromCSV(data_files[Integer.valueOf(c.toString())], true, false);
			// generate file name
			output_file_path += c + "_";
		}
		// identify syntax slices
		Set<String> syntax_slice_set = identifySyntaxSlices();
		// generate featured sentences based on syntax slices
		generateSyntaxSliceFeature(syntax_slice_set, feature_type);
		// output file name
		if(feature_type==FeatureEnum.FT_SYNTAX_SLICE) {
			output_file_path += "syn_slice.arff";
		} else if(feature_type==FeatureEnum.FT_SYNTAX_SLICE_NUMERIC) {
			output_file_path += "syn_slice_num.arff";
		} else {
			Log.error("processDataForSyntaxCluster -> wrong feature types: "+ feature_type);
		}
		// generate dataset for weka analysis
		wa.generateWekaDataFromSRP(sentences, true, false, output_file_path);
		return output_file_path;
	}

	
	


	private Set<String> identifySyntaxSlices(){
			//LinkedList<String> syntax_slice_set = new LinkedList();
			Set<String> syntax_slice_set = new HashSet<String>();
			// syntactic analysis
			LexicalizedParser lp = null;
			lp = LexicalizedParser.loadModel("parser/models/lexparser/englishPCFG.ser.gz");
			
			for (FeaturedSentence fs: sentences){
				// parse sentences and generated a stemmed version, only for candidate match
				String[] words = fs.ori_sentence.split(" ");
				fs.parse = EnglishProcessing.parseSentence(lp, words, IEnum.ENGLISH, false);
				// obtain all leaves, and go up to get all slices
				ArrayList<Tree> leaves = (ArrayList<Tree>) fs.parse.getLeaves();
				for(Tree leaf : leaves) {
					// obtain the POSTag nodes, i.e., the parents of leaves   
					Tree parent = leaf.ancestor(1, fs.parse);
					Tree temp;
					// traverse up to the root node, and generate all slices
					while (parent!=null) {
						temp = parent.ancestor(1, fs.parse);
						if(temp!=null) {
							String slice = parent.value()+"->"+temp.value();
							// attach syntax slices to the sentence 
							fs.syntax_slices.add(slice);
							// record syntax slices, which will be used later 
							syntax_slice_set.add(slice);
	//						System.out.println(slice);
						}
						parent = temp;
					}				
				}
				//fs.stemmed_parse = EnglishProcessing.parseTreeStemmingNew(fs.parse);
			}
			return syntax_slice_set;
		}


	/**
	 * generate slice features according to identified syntax slices
	 * @param syntax_slice_set
	 */
	private void generateSyntaxSliceFeature(Set<String> syntax_slice_set, int feature_type) {
		// TODO Auto-generated method stub
		for(FeaturedSentence fs: sentences) {
			
			for (String slice : syntax_slice_set) {
				if (fs.syntax_slices.contains(slice)){// if keyword is found,
					// add feature value
					if(feature_type==FeatureEnum.FT_SYNTAX_SLICE) {
						fs.features.add(new SRFeature(slice, FeatureEnum.FT_SYNTAX_SLICE, "1"));
					}
					else if(feature_type==FeatureEnum.FT_SYNTAX_SLICE_NUMERIC) {
						int count = 0;
						//calculate the number of occurrence  
						for(String temp_slice: fs.syntax_slices) {
							if(temp_slice.equals(slice)) {
								count++;
							}
						}
						fs.features.add(new SRFeature(slice, FeatureEnum.FT_SYNTAX_SLICE_NUMERIC, Integer.toString(count)));
					}
					else {
						Log.error("generateSyntaxSliceFeature -> unexpected feature type: " + feature_type);
					}
				} else {
					if(feature_type==FeatureEnum.FT_SYNTAX_SLICE) {
						fs.features.add(new SRFeature(slice, FeatureEnum.FT_SYNTAX_SLICE, "0"));
					}
					else if(feature_type==FeatureEnum.FT_SYNTAX_SLICE_NUMERIC) {
						fs.features.add(new SRFeature(slice, FeatureEnum.FT_SYNTAX_SLICE_NUMERIC, "0"));
					}
					else {
						Log.error("generateSyntaxSliceFeature -> unexpected feature type: " + feature_type);
					}
				}
			}
			
		}
	}
	
	
	/**
	 * import data file and produce a set of semantic dependencies for clustering 
	 * @param ori_data
	 * @param wa
	 * @return
	 */
	public String processDataForDependencyCluster(String ori_data, WekaAnalysisDataset wa, int feature_type) {
		String output_file_path = "output data/"; // this will be incrementally modified during the procedure
		// import files
		for (Character c : ori_data.toCharArray()) {
			// Note that here we include classification
			importSentencesFromCSV(data_files[Integer.valueOf(c.toString())], true, false);
			// generate file name
			output_file_path += c + "_";
		}
		
//		dependencyAnalysis(false);
		
		
		// identify syntax slices
		Set<String> dependency_set = identifyDependencies();
		// generate featured sentences based on syntax slices
//		generateDependencyFeature(dependency_set, feature_type);
		
		/*
		// output file name
		if(feature_type==FeatureEnum.FT_SYNTAX_SLICE) {
			output_file_path += "syn_slice.arff";
		} else if(feature_type==FeatureEnum.FT_SYNTAX_SLICE_NUMERIC) {
			output_file_path += "syn_slice_num.arff";
		} else {
			Log.error("processDataForSyntaxCluster -> wrong feature types: "+ feature_type);
		}
		
		// generate dataset for weka analysis
		wa.generateWekaDataFromSRP(sentences, true, false, output_file_path);
		
		*/
		return output_file_path;
	}


	private Set<String> identifyDependencies() {
		//LinkedList<String> syntax_slice_set = new LinkedList();
		Set<String> dependency_set = new HashSet<String>();
		
		// Create NLP analyzer
		Properties props = new Properties();
		// props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
		props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
		StanfordCoreNLP analyzer = new StanfordCoreNLP(props);
		
		for (FeaturedSentence fs : sentences) {
			System.out.println(fs.ori_sentence);
			fs.dep_trees = EnglishProcessing.dependencyAnalysisGivenAnalyzer(analyzer, fs.ori_sentence);
			
			
			
			/*
			// dep_trees can go wrong?
			for (SemanticGraph sg : fs.dep_trees) {
				Collection<TypedDependency> dependencies = sg.typedDependencies();
				for (TypedDependency td : dependencies) {
					if (td.gov() != null && td.dep() != null) {
						// gov
						String gov_pos_tag = td.gov().toString();
						int p1 = gov_pos_tag.indexOf("/");
						if (p1 != -1) {
							gov_pos_tag = gov_pos_tag.substring(p1 + 1);
						}
						// dep
						String dep_pos_tag = td.dep().toString();
						int p2 = dep_pos_tag.indexOf("/");
						if (p2 != -1) {
							dep_pos_tag = dep_pos_tag.substring(p2 + 1);
						}
						String dependency = td.reln() + "(" + gov_pos_tag + ", " + dep_pos_tag + ")";
						//
						fs.dependencies.add(dependency);
						dependency_set.add(dependency);
					}
				}
			}
			*/
		}

		return null;
	}


	public static void main(String[] args) throws Exception{
		SRProcessing srp1 = new SRProcessing();
		WekaAnalysisDataset wa1 = new WekaAnalysisDataset();

		// prepare classification dataset
//		String data_file_path = srp1.processDataAndGenerateArff("1", false, false, FeatureEnum.TRAIN_KEY_MULTI, FeatureEnum.TRAIN_RULE_MULTI, FeatureEnum.TRAIN_DEP_NO, wa1);
		
		// prepare syntax clustering dataset
//		String data_file_path = srp1.processDataForSyntaxCluster("1", wa1, FeatureEnum.FT_SYNTAX_SLICE_NUMERIC);
		// prepare semantic dependency clustering dataset
		String data_file_path = srp1.processDataForDependencyCluster("1", wa1, FeatureEnum.FT_SEMANTIC_DEPENDENCY_NUMERIC);
		
		
		// load dataset from file
//		wa1.loadModel("output data/1_syn_slice.arff", true);
//		wa1.loadModel("output data/1_syn_slice_num.arff", true);
		
//		wa1.clustering("K-means");
//		wa1.clustering("EM");

				
		
		
	}
	


}
