package cn.edu.bjut.text.processing;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import cn.edu.bjut.text.utility.Fileloader;
import cn.edu.bjut.text.utility.IEnum;
import cn.edu.bjut.text.utility.IOUtil;
import cn.edu.bjut.text.utility.Word;
import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Pair;

/**
 * Exclusively focus on Processing English text May involve English-specific analysis functions For the time being, it is only a test class
 * 
 * @author Tong Li
 * 
 */
public class EnglishProcessing extends TextProcessing{


	/**
	 * This operation should not appear here, it should be placed to domain-specific processing classes, e.g., SRProcessing
	 * Refactor it later
	 */
	public static void runSec() {
		LinkedList<String> all_sec_req = IOUtil.readSentencesFromPlainFile("test files/sec.txt");

		// load lexical parse
		LexicalizedParser lp = null;
//		lp = LexicalizedParser.loadModel("parser/models/lexparser/englishFactored.ser.gz");
		lp = LexicalizedParser.loadModel("parser/models/lexparser/englishPCFG.ser.gz");

		int count =0;
		int term_count=0, action_count=0, modal_count =0;
		boolean s_term,s_compliance,s_modal;
		for(String sec_req: all_sec_req){
			s_term = false;
			s_compliance = false;
			s_modal = false;
			
			if(arrayContainString(IEnum.SecurityTerms, sec_req)){
				s_term = true;
				term_count++;
			}
			
			if(arrayContainString(IEnum.ComplianceIndicator, sec_req)){
				s_compliance = true;
				action_count++;
			}
			
			if(arrayContainString(IEnum.ModalVerbs, sec_req)){
				s_modal = true;
				modal_count++;
			}
			
			
//			if(s_term||s_action){
//				count++;
//				
//			}
//			else{
//				System.out.println(sec_req);
//			}
			
			if(s_compliance){
				// segment words
				String[] words = sec_req.split(" ");
				formatWords(words);
				// parse
				Tree parse = parseSentence(lp, words, IEnum.ENGLISH, false);
				List<Word> pos_tags = obtainPOSTags(parse);
				
				// pattern analysis
				// String pattern = "/VB.?/=vb $+ (NP=np ?$+ PP=pp)";
				// pattern = "clinical : HCN";// ?$+ PP=pp";
				// LinkedList<Pair<String, String>> results = null;
				// results = SentenceProcessing.matchPatterns(parse, pattern);
				// if(results==null){
				// System.out.println("Not matched");
				// }
				// else{
				// for (Pair<String, String> variable : results) {
				// System.out.println(variable.first + ":" + variable.second);
				// }
				// }
				
				boolean found = false;
				List<TypedDependency> tdl = dependencyAnalysis(parse, false);
				// check the dependency relation between must and corresponding verb
				for(TypedDependency td: tdl){
					if(td.reln().toString().equals("dep")&&(td.dep().toString().contains("must"))){
						String dep = td.gov().toString(CoreLabel.OutputFormat.VALUE);
						Word dep_word = findWordByContent(pos_tags, dep);
//						td.dep().toString();
						if(dep_word.type.contains("VB")&&!dep_word.content.equals("be")){
							System.out.println(td);
							if(!found){
								count++;
								found = true;
							}
						}
					}
				}
				
				System.out.println();
				
				
			}
		}
		
		System.out.println(count);
//		System.out.println(count);
		
	}
	
	public static void parseExample(String s) {
		LexicalizedParser lp = null;
//		lp = LexicalizedParser.loadModel("parser/models/lexparser/englishFactored.ser.gz");
		lp = LexicalizedParser.loadModel("parser/models/lexparser/englishPCFG.ser.gz");
		
		String[] words = s.split(" ");
		formatWords(words);
		// parse
		Tree parse = parseSentence(lp, words, IEnum.ENGLISH, true);
		dependencyAnalysis(parse, true);
		
		
		String pattern = "MD=md $++ (VP=vp << VB=vb)";
		pattern = "MD=md ?. RB=rb .. VB=vb";
		pattern = "ADJP=adjp <<, (subject . to) << (to .. regulations) << regulations ";
//		pattern = "VP=vp << (comply . with) << (with .. regulations) << regulations ";
//		pattern = "VP=vp <<, obey << regulations ";
		LinkedList<LinkedList<Pair<String, String>>> results = null;
		results = PatternMatch.matchPatterns(parse, pattern);
		if (results == null) {
			System.out.println("Not matched");
		}
		else {
			for(LinkedList<Pair<String, String>> result: results){
				for (Pair<String, String> variable : result) {
					System.out.println(variable.first + ":" + variable.second);
				}
				System.out.println();
			}
		}
	}
	
	
	/**
	 * Make use of Porter Stemmer to stem a single word
	 * Here we didn't really check whether it is a word ...
	 * @param word
	 * @return stemmed word: s
	 */
	public static String wordStemming(String word){
		word = word.replaceAll("\\W", "");
		
		Stemmer s = new Stemmer();
		char[] w = word.toCharArray();
        s.add(w,w.length);
        s.stem();
        return s.toString(); 
	}
	
	/**
	 * Make use of Porter Stemmer to stem a single word
	 * @param word
	 * @return a new stemmed sentence: result
	 */
	public static String sentenceStemming(String sentence){
        String[] words = sentence.split(" ");
		String result = "";
        for (String word: words){ 
        	result += wordStemming(word)+" "; 
        }
        result = result.trim(); // remove the last space
        return result;
	}
	
	/**
	 * Make use of Porter Stemmer to stem all leave notes in a penn tree
	 * @param parse
	 * @return nothing, modify original object
	 */
	public static void parseTreeStemming(Tree parse){
//		LinkedList<Tree> leaves = (LinkedList<Tree>) parse.getLeaves();
		String word = "";
		for(Tree leave: (List<Tree>) parse.getLeaves()){
			word = leave.label().value();
			String stemmed_leave = wordStemming(word);
			((Label)leave.label()).setValue(stemmed_leave);
		}
//		return parse;
	}
	
	
	/**
	 * Make use of Porter Stemmer to stem all leave notes in a penn tree
	 * @param parse
	 * @return a new parse tree: stemmed_parse
	 */
	public static Tree parseTreeStemmingNew(Tree parse){
		Tree stemmed_parse = parse.deepCopy();
		String word = "";
		for(Tree leave: (List<Tree>) stemmed_parse.getLeaves()){
			word = leave.label().value();
			String stemmed_leave = wordStemming(word);
			((Label)leave.label()).setValue(stemmed_leave);
		}
		return stemmed_parse;
	}
	
	
	/**
	 * Create dependency graph
	 * @param text
	 * @return
	 */
	public static LinkedList<SemanticGraph> dependencyAnalysis(String text) {
		LinkedList<SemanticGraph> sgs = new LinkedList<SemanticGraph>();
		
		// Add in sentiment
		Properties props = new Properties();
		// props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
		props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");

		StanfordCoreNLP analyzer = new StanfordCoreNLP(props);
		Annotation annotation = new Annotation(text);

		analyzer.annotate(annotation);

		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		for(CoreMap sentence: sentences) {
			SemanticGraph enhanced_graph = sentence.get(SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation.class);
			sgs.add(enhanced_graph);
		}
		
		return sgs;
	}
	
	
	/**
	 * Create dependency graphs using a particular analyzer
	 * @param analyzer
	 * @param text
	 * @return
	 */
	public static LinkedList<SemanticGraph> dependencyAnalysisGivenAnalyzer(StanfordCoreNLP analyzer, String text) {
		LinkedList<SemanticGraph> sgs = new LinkedList<SemanticGraph>();
		
		Annotation annotation = new Annotation(text);
		analyzer.annotate(annotation);

		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		for(CoreMap sentence: sentences) {
			SemanticGraph enhanced_graph = sentence.get(SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation.class);
			sgs.add(enhanced_graph);
		}
		
		return sgs;
	}
	
	public static void dependencyTest() {
		String text = "The card issuer and the CEP card share a secret key to generate and verify MACs";
		
//		text = "She was suspected by everyone";
//		text = "She gave me a raise";
		
		// Add in sentiment
	    Properties props = new Properties();
//	    props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
	    props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
	    
	    StanfordCoreNLP analyzer = new StanfordCoreNLP(props);
	    Annotation annotation = new Annotation(text);
	 
	    analyzer.annotate(annotation);
	    
	    List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
	    if (sentences != null && ! sentences.isEmpty()) {
	      CoreMap sentence = sentences.get(0);
	      
	      System.out.println(text);
	      System.out.println("Basic dependencies are:");
	      SemanticGraph basic_graph = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
//	      System.out.println(basic_graph.toString(SemanticGraph.OutputFormat.LIST));
	      System.out.println(basic_graph.toString(SemanticGraph.OutputFormat.READABLE));
	      System.out.println(basic_graph.toString(SemanticGraph.OutputFormat.RECURSIVE));
	      
//	      System.out.println("The first sentence enhanced++ dependencies are:");
	      SemanticGraph enhanced_graph = sentence.get(SemanticGraphCoreAnnotations.EnhancedPlusPlusDependenciesAnnotation.class);
//	      System.out.println(enhanced_graph.toString(SemanticGraph.OutputFormat.LIST));
//	      System.out.println(enhanced_graph.toString(SemanticGraph.OutputFormat.READABLE));
//	      System.out.println(enhanced_graph.toString(SemanticGraph.OutputFormat.RECURSIVE));

	      List<SemanticGraphEdge> basic_list = basic_graph.edgeListSorted();
	      List<SemanticGraphEdge> enhanced_list = enhanced_graph.edgeListSorted();

	      System.out.println("Different dependencies are:");
	      boolean print;
	      for(SemanticGraphEdge enhanced_sge: enhanced_list) {
	    	  	print = true;
	    	  	for(SemanticGraphEdge basic_sge: basic_list) {
	    	  		if(enhanced_sge.equals(basic_sge)) {
	    	  			print = false;
	    	  			break;
	    	  		}
	    	  	}
	    	  	if(print) {
	    	  		System.out.println(enhanced_sge.toString());
//	    	  		IndexedWord gov = enhanced_sge.getGovernor();
//	    	  		String pos = gov.get(CoreAnnotations.PartOfSpeechAnnotation.class); // gov.keySet() to obtain all annotation keys 
//	    	  		System.out.println(gov.originalText()+"/"+gov.pseudoPosition()+"/"+pos);
	    	  	}
	      }
	      
//	      for(SemanticGraphEdge basic_sge: basic_list) {
//	    	  	print = true;
//	    	  	for(SemanticGraphEdge enhanced_sge: enhanced_list) {
//	    	  		if(enhanced_sge.equals(basic_sge)) {
//	    	  			print = false;
//	    	  			break;
//	    	  		}
//	    	  	}
//	    	  	if(print) {
//	    	  		System.out.println(basic_sge.toString());
//	    	  	}
//	      }
	      
	      /*
	      System.out.println("Coreference information");
	      Map<Integer, CorefChain> corefChains =
	          annotation.get(CorefCoreAnnotations.CorefChainAnnotation.class);
	      if (corefChains == null) { return; }
	      for (Map.Entry<Integer,CorefChain> entry: corefChains.entrySet()) {
	    	  System.out.println("Chain " + entry.getKey());
	        for (CorefChain.CorefMention m : entry.getValue().getMentionsInTextualOrder()) {
	          // We need to subtract one since the indices count from 1 but the Lists start from 0
	          List<CoreLabel> tokens = sentences.get(m.sentNum - 1).get(CoreAnnotations.TokensAnnotation.class);
	          // We subtract two for end: one for 0-based indexing, and one because we want last token of mention not one following.
	          System.out.println("  " + m + ", i.e., 0-based character offsets [" + tokens.get(m.startIndex - 1).beginPosition() +
	                  ", " + tokens.get(m.endIndex - 2).endPosition() + ")");
	        }
	      }
	      System.out.println();
	      */

//	      System.out.println("The first sentence overall sentiment rating is " + sentence.get(SentimentCoreAnnotations.SentimentClass.class));
	    }

	}
	

	public static void main(String[] args) {
		dependencyTest();
//		runSec();
//		parseExample("The card issuer and the CEP card share a secret key to generate and verify MACs");
//		parseExample("The system should eliminate information disclosure. The card issuer and the CEP card share a secret key to generate and verify MACs");
//		parseExample("information disclosure should be eliminated");
		
//		System.out.println(stemSentence("regulations laws rules"));
		
		
		// import words
//		String s = Fileloader.readPlainFile("temp_eng.txt");
//		String[] words = s.split(" ");
//		TextProcessing.formatWords(words);

		// parse
//		Tree parse = TextProcessing.structureAnalysis(words, IEnum.ENGLISH, true);

		// obtain POStag
		// List<Word> pos_tags = TextProcessing.obtainPOSTags(parse);
		// for(Word w: pos_tags){
		// w.wordPrint();
		// }

		// TextProcessing.dependencyAnalysis(parse);

		// pattern analysis
		// String pattern = "/VB.?/=vb $+ (NP=np ?$+ PP=pp)";
		// pattern = "clinical : HCN";// ?$+ PP=pp";
		// LinkedList<Pair<String, String>> results = null;
		// results = SentenceProcessing.matchPatterns(parse, pattern);
		// if(results==null){
		// System.out.println("Not matched");
		// }
		// else{
		// for (Pair<String, String> variable : results) {
		// System.out.println(variable.first + ":" + variable.second);
		// }
		// }
	}

}
