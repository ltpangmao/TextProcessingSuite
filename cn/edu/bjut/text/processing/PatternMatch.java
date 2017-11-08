package cn.edu.bjut.text.processing;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.util.Pair;

/**
 * This class focus on sentence pattern matching, and other syntactic analysis.
 * @author Tong Li
 *
 */
public class PatternMatch {

	/**
	 * This function matches a sentence with a particular pattern
	 * If matched, return corresponding components of the sentence
	 * If not, return empty results.
	 * @param sentence, this should be a single sentence (assumption)
	 * @param pattern, a pattern for match
	 * @param tree, a parsed tree
	 * @return
	 */
	public static LinkedList<LinkedList<Pair<String, String>>> matchPatterns(Tree tree, String pattern) {
		// tree pattern matcher
		TregexPattern tregex = TregexPattern.compile(pattern);
		TregexMatcher matcher = tregex.matcher(tree);

		LinkedList<LinkedList<Pair<String, String>>> all_match = new LinkedList<LinkedList<Pair<String, String>>>();
		boolean matched = false;
		while (matcher.find()) {
//			System.out.println("matched\n");
			matched = true;
			// individual match
			LinkedList<Pair<String, String>> individual_match = new LinkedList<Pair<String, String>>();

			// detect variables of the pattern
			LinkedList<String> variables = obtainVariables(pattern);
			
			// automatically fill all parameters
			for (String variable : variables) {
				// individual variable
				Pair<String, String> individual_variable_pair = new Pair<String, String>();
				// first check whether the variable is found in the string, as it can be an optional variable
				if (matcher.getNode(variable) != null) {
					individual_variable_pair.first = variable;
					individual_variable_pair.second = getLeafWords(matcher.getNode(variable));
					// add the variable of the matched phrase
					individual_match.add(individual_variable_pair);
				}
			}
			all_match.add(individual_match);
		}
		if(matched){
			return all_match;
		}
		else{
			return null;
		}
	}

	/**
	 * separate variables from specific RegEx patterns
	 * @param patternString
	 * @return
	 */
	private static LinkedList<String> obtainVariables(String patternString) {
		LinkedList<String> variables = new LinkedList<String>();

		Pattern pattern = Pattern.compile("=[\\w]*");
		Matcher matcher = pattern.matcher(patternString);
		while (matcher.find()) {
			// remove the "=" of the matched variable (e.g., "=vp")
			String s = matcher.group().substring(1);
			variables.add(s);
		}

		return variables;
	}



    /**
     * complement the Tree class to get corresponding words of a (sub-)tree
     * @param tree
     * @return
     */
    private static String getLeafWords(Tree tree){
    	String sentence ="";
//    	if(tree.isLeaf()){
//    		if(tree.toString().matches("[a-zA-Z0-9]+")){
//    			sentence = " "+tree.toString();
//    		}
//    		else{
//    			sentence = tree.toString();
//    		}
//    	}
//    	else{
//    		for (Tree child: tree.children()){
//    			sentence += getLeafWords(child);
//    		}
//    	}
    	List<Tree> leaves = tree.getLeaves();
    	for (Tree leaf : leaves) { 
            if(!sentence.equals("") && leaf.label().value().matches("[a-zA-Z0-9]+")){
            	sentence += " "+leaf.label().value();
            }
            else{
            	sentence += leaf.label().value();
            }
    	}
    	return sentence;
    }
	
}
