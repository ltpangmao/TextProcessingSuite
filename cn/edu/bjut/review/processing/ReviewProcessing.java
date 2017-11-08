package cn.edu.bjut.review.processing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import cn.edu.bjut.text.utility.Log;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;

public class ReviewProcessing {

	public static void main(String[] args) throws Exception {
//		String path = "/Users/tongli/OneDrive/reseach/Working stuff/ML-learning/Data/SecReq/data/three sets/";
//		processOriCVS(path);
		
//		addQuote();
//		prepareWeightedFile();
				
		String path = "/Users/tongli/OneDrive/temp/data/new_reviews.csv";
		LinkedList<Review> reviews = importCSVFile(path, "utf8", 100);
		
		reviewSentimentAnalysis(reviews);
		
	}
	
	
	public static LinkedList<Review> importCSVFile(String path, String encode, int num) {
		LinkedList<Review> reviews = new LinkedList<Review>();
		int count = 0;
		try {
			File file = new File(path);
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), encode);
			BufferedReader bf = new BufferedReader(isr);
			String line = "";
			while ((line = bf.readLine()) != null && count < num) {
				// remove additional space, if necessary
				// line =line.replace(" ", "");
				Review review = new Review();
				String[] temp = line.split(",");
				if (temp.length == 5) {
					if (count != 0) {
						if(temp[0].equals("2")){
							review.category = 1;
						} else {
							review.category = Integer.valueOf(temp[0]);
						}
						review.keyword = temp[2];
						review.rate = Integer.valueOf(temp[3]);
						review.review = temp[4];
						review.printReview();
						//
						reviews.add(review);
					}
				} else {
					Log.error("Errors in processing input data! " + line);
				}
				count++;
			}
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reviews;
	}


	public static void reviewSentimentAnalysis(LinkedList<Review> reviews) {
		// English analyzer
//		Properties props = new Properties();
//		props.setProperty("annotators", "tokenize, ssplit, pos, parse, sentiment");
//		StanfordCoreNLP analyzer = new StanfordCoreNLP(props);
		
		// Chinese analyzer
		// there isn't a trained Chinese sentiment classifier in stanford nlp
		StanfordCoreNLP analyzer = new StanfordCoreNLP("StanfordCoreNLP-chinese.properties");

		for (Review review : reviews) {
			Annotation annotation = new Annotation(review.review);

			analyzer.annotate(annotation);

			List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
			if (sentences != null && !sentences.isEmpty()) {
				System.out.print(review.category + " ** ");
				for (CoreMap sentence: sentences) {
					String result = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
					System.out.print(result+" ");
					
//					SemanticGraph basic_graph = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
//				    System.out.println(basic_graph.toString(SemanticGraph.OutputFormat.READABLE));
				}
				System.out.println();
				
//				CoreMap sentence = sentences.get(0);
			}
		}
	}
}



class Review{
	int category; // this is identified manually
	int rate;
	String keyword="";
	String review="";
	String sentiment=""; // this is identified using CoreNLP
	
	void printReview() {
		System.out.println(category+" "+ rate + " " + keyword + " " + review);
	}
}
