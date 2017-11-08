package cn.edu.bjut.text.processing;

import java.util.LinkedList;
import java.util.List;

import cn.edu.bjut.text.utility.IEnum;
import cn.edu.bjut.text.utility.Word;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.international.pennchinese.ChineseTreebankLanguagePack;

/**
 * This class contains common text processing functions for both English and Chinese Include structure, POStag, dependency analysis etc.
 * This is an abstract class, and we only use its subclasses when necessary
 * 
 * @author Tong Li
 * 
 */
public abstract class TextProcessing {

	/**
	 * Syntactic structure parser (tree) This also do the POStagging, but need further processing to obtain all such tags
	 * 
	 * @param segmented
	 * @return
	 * @throws Exception
	 */
	public static Tree parseSentence(LexicalizedParser lp, String[] words, int language, boolean tree_print) {
		formatWords(words); // avoid exceptional words
		
		List<CoreLabel> rawWords = Sentence.toCoreLabelList(words);
		Tree parse = lp.apply(rawWords);
		if (tree_print) {
			parse.pennPrint();
			System.out.println();
		}
		return parse;
	}

	/**
	 * Syntactic structure parser (tree) This also do the POStagging, but need further processing to obtain all such tags
	 * 
	 * @param segmented
	 * @return
	 * @throws Exception
	 */
	public static Tree parseSentence(String[] words, int language, boolean tree_print) {
		List<CoreLabel> rawWords = Sentence.toCoreLabelList(words);
		// load lexical parse
		LexicalizedParser lp = null;
		if (language == IEnum.ENGLISH) {
			lp = LexicalizedParser.loadModel("parser/models/lexparser/englishFactored.ser.gz");
		} else if (language == IEnum.CHINESE) {
			lp = LexicalizedParser.loadModel("parser/models/lexparser/chineseFactored.ser.gz");
		} else {
			System.out.println("Lexicalized parser load failed");
			System.exit(1);
		}
		Tree parse = lp.apply(rawWords);
		if (tree_print) {
			parse.pennPrint();
			System.out.println();
		}
		return parse;
	}
	
	
	/**
	 * Instead of using a POStagger, we directly obtain POS tags from grammar tree
	 * 
	 * @param parse
	 * @return
	 */
	public static List<Word> obtainPOSTags(Tree parse) {
		List<Word> words = new LinkedList<Word>();
		List<Tree> leaves = parse.getLeaves();
		for (Tree leave : leaves) {
			Word w = new Word();
			w.content = leave.label().value();
			w.type = leave.parent(parse).label().value();
			words.add(w);
		}
		return words;
	}

	/**
	 * Here is an example that directly uses a pos-tagger, which is surely faster and the result is the same with grammar tree (almost)
	 * So, we will probably not use this function 
	 * @param s
	 */
	@Deprecated
	public static void posTagging(String s) {
		MaxentTagger tagger = new MaxentTagger("/Users/tongli/study/NLP-learning/Standford Parser/past/stanford-postagger-2015-04-20/models/english-bidirectional-distsim.tagger");
		String result = tagger.tagString(s);
		System.out.println(result);
	}

	/**
	 * Dependency parser
	 * 
	 * @param parse
	 */
	public static List<TypedDependency> dependencyAnalysis(Tree parse, Boolean depend_print) {
		TreebankLanguagePack tlp = new ChineseTreebankLanguagePack();
		// TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();

		if (depend_print) {
			// System.out.println(tdl);
			for (TypedDependency td : tdl) {
				System.out.println(td);
			}
			System.out.println();
		}
		return tdl;
	}

	/**
	 * In case the input contains some unexpected characters
	 * task1: remove additional "newline"
	 * @param words
	 */
	public static void formatWords(String[] words) {
		for (int i = 0; i < words.length; i++) {
			if (words[i].endsWith("\n")) {
				words[i] = words[i].substring(0, words[i].length() - 1);
			}
		}
	}
	
	/**
	 * Check whether a string array contain certain string
	 * @param sec_req
	 * @return
	 */
	public static Boolean arrayContainString(String[] array, String sec_req){
		for(String sec_word: array){
			if (sec_req.toLowerCase().contains(sec_word)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * retrieve the a particular word based on its content 
	 * @param sec_req
	 * @return
	 */
	public static Word findWordByContent(List<Word> words, String target){
		for(Word word: words){
			if (target.equals(word.content)){
				return word;
			}
		}
		return null;
	}

}
