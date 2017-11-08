package cn.edu.bjut.text.processing;

import java.io.File;
import java.io.StringReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import cn.edu.bjut.text.utility.IOUtil;
import cn.edu.bjut.text.utility.IEnum;
import cn.edu.bjut.text.utility.Word;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.parser.lexparser.ChineseMarkovWordSegmenter;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreePrint;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.international.pennchinese.ChineseTreebankLanguagePack;
import edu.stanford.nlp.util.Pair;

/**
 * Exclusively focus on Processing Chinese, including segmentation and POStagging
 * 
 * @author tongli
 * 
 */
public class ChineseProcessing {

	public static void main(String[] args) {

		String sample = "闂囷拷鐟曚礁鐨㈤崠鑽ゆ灍閺佺増宓侀崣鎴︼拷浣稿煂娑擃厼绺�";
		// segment
//		 List<String> segmented = segment(sample);
		// export
		// IOUtil.exportSegmentedSentence(segmented, "temp.txt", false);
		// import words
		List<String> segmented = IOUtil.importSegmentedSentence("test files/temp.txt");
		
		String[] words = convertToArray(segmented);
		TextProcessing.formatWords(words);

		
		// parse
		Tree parse = TextProcessing.parseSentence(words, IEnum.CHINESE, true);

		
		// // obtain POStag
		// List<Word> pos_tags = TextProcessing.obtainPOSTags(parse);
		// for(Word w: pos_tags){
		// w.wordPrint();
		// }

		
		
		// Dependence analysis
//		TextProcessing.dependencyAnalysis(parse);

		
		
		// pattern analysis
		String pattern = "/VB.?/=vb $+ (NP=np ?$+ PP=pp)";
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
	 * Sentence segmentation
	 * 
	 * @param sentence
	 * @return
	 */
	private static List<String> segment(String sentence) {
		// Get this project root path
		String rootPath = System.getProperty("user.dir");
		// Get the folder of segmenter models
		String basedir = rootPath + "/parser/models/segmenter"; // avoid uploading large files to Github...
		basedir = "/Users/tongli/study/NLP-learning/Standford Parser/new/stanford-chinese-corenlp-2016-01-19-models/edu/stanford/nlp/models/segmenter/chinese";
		// Set segmentation properties, which is used later for creating classifer
		Properties props = new Properties();
		props.setProperty("sighanCorporaDict", basedir);
		props.setProperty("serDictionary", basedir + "/dict-chris6.ser.gz");
		props.setProperty("inputEncoding", "UTF-8");
		props.setProperty("sighanPostProcessing", "true");
		// Create classifier
		CRFClassifier<CoreLabel> segmenter = new CRFClassifier<CoreLabel>(props);
		// Configure classifier with particular corpus
		segmenter.loadClassifierNoExceptions(basedir + "/ctb.gz", props);
		// segmenter.loadClassifierNoExceptions(basedir + "/pku.gz", props);
		List<String> segmented = segmenter.segmentString(sentence);
		return segmented;
	}

	/**
	 * As the force conversion has certain unknown problem... Here is a particular function for doing this conversion.
	 * 
	 * @param segmented
	 * @return
	 */
	private static String[] convertToArray(List<String> segmented) {
		// turn the list into an array
		// String[] words = (String[]) segmented.toArray(); // this causes an confusing error...
		String[] words = new String[segmented.size()];
		for (int i = 0; i < segmented.size(); i++) {
			words[i] = segmented.get(i);
		}
		return words;
	}

}
