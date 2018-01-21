package cn.edu.bjut.sr.processing;

import java.util.LinkedList;

import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;


/**
 * A specific class for storing feature information of a sentence
 * @author tongli
 *
 */
public class FeaturedSentence {
	String ori_sentence; // this is the original sentence imported from the file
	String modi_sentence; // if keyword is matched, then we replace the keyword and obtain this modified sentence
	Tree parse;
	Tree stemmed_parse;
//	SemanticGraph dep_tree; 
	LinkedList<SemanticGraph> dep_trees; // in case there are requirements that contains two sentences
	LinkedList<SRFeature> features;

	
	// Syntax sugar, which is used for analyzing syntax clustering 
	LinkedList<String> syntax_slices;
	LinkedList<String> dependencies;
	

	public FeaturedSentence() {
		this.ori_sentence = "";
		this.modi_sentence = "";
		this.parse = null;
		this.stemmed_parse = null;
		this.dep_trees = null;
		this.features = new LinkedList<SRFeature> ();
		//
		this.syntax_slices = new LinkedList<String>();
		this.dependencies = new LinkedList<String>();
	}
	
	public FeaturedSentence(String sentence) {
		this.ori_sentence = sentence;
		this.modi_sentence = "";
		this.parse = null;
		this.stemmed_parse = null;
		this.dep_trees = null;
		this.features = new LinkedList<SRFeature> ();
		//
		this.dependencies = new LinkedList<String>();
	}
}
