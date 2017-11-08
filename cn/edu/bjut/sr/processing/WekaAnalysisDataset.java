package cn.edu.bjut.sr.processing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Random;

import cn.edu.bjut.text.processing.EnglishProcessing;
import cn.edu.bjut.text.utility.Log;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.classifiers.functions.supportVector.RBFKernel;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.j48.ModelSelection;
import weka.classifiers.trees.lmt.LMTNode;
import weka.classifiers.trees.LMT;
import weka.classifiers.trees.lmt.ResidualModelSelection;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.stemmers.LovinsStemmer;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stopwords.MultiStopwords;
import weka.core.stopwords.StopwordsHandler;
import weka.core.stopwords.WordsFromFile;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.StringToWordVector;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;

public class WekaAnalysisDataset {
	
	private static final String positive = "1";
	private static final String negative = "0";

	Instances dataset = null; // main dataset

	LinkedList<FeaturedSentence> sentences = null; // original dataset from SRProcessing 
	
	// programming sugar
	LinkedList<Attribute> word_features = null;
	
	
	
	/**
	 * Generate the list of features that will be used for learning
	 */
	private void defineDataStructure() {
		// define data structure
		ArrayList<Attribute> attributes = new ArrayList<Attribute>();
		// prepare attributes
		for(SRFeature srf: sentences.getFirst().features) {
			Attribute temp_att = null;
			if(srf.getFeature_type()==FeatureEnum.FT_SENTENCE) {
				// textual 
				temp_att = new Attribute(srf.getFeature_name(), (ArrayList<String>)null);
			}
			else if (srf.getFeature_type()==FeatureEnum.FT_CATEGORY) {
				ArrayList<String> labels = new ArrayList<String>();
				labels.add(positive);
				labels.add(negative);
				temp_att = new Attribute(srf.getFeature_name(), labels);
			}
			else if (srf.getFeature_type()==FeatureEnum.FT_KEYWORD||srf.getFeature_type()==FeatureEnum.FT_RULE) {
				ArrayList<String> labels = new ArrayList<String>();
				labels.add(positive);
				labels.add(negative);
				temp_att = new Attribute(srf.getFeature_name(), labels);
			}
			else {
				Log.error("wrong feature types");
			}
			attributes.add(temp_att);
		}
		 dataset = new Instances("Security requirements", attributes, 10000); //temporally set the size to 1000
	}
	
	
	/**
	 * Fill data to the defined data attributes
	 * This method has to be done after defining data structure
	 */
	private void addData() {
		for(FeaturedSentence fs: sentences) {
			if(dataset.numAttributes()!=fs.features.size()) { // pre-check
				Log.error("loaded sentence has unexpected number of attributes");
				return;
			}
			double[] values = new double[dataset.numAttributes()];
			for(int i=0; i<fs.features.size();i++) {
				SRFeature srf_temp = fs.features.get(i);
				if(srf_temp.getFeature_type()==FeatureEnum.FT_SENTENCE) {
					// textual 
					values[i] = dataset.attribute(i).addStringValue(srf_temp.getFeature_value());
				}
				else if (srf_temp.getFeature_type()==FeatureEnum.FT_CATEGORY) {
					// nominal
					values[i] = dataset.attribute(i).indexOfValue(srf_temp.getFeature_value().toString());
				}
				else if (srf_temp.getFeature_type()==FeatureEnum.FT_KEYWORD||srf_temp.getFeature_type()==FeatureEnum.FT_RULE) {
					// nominal
					values[i] = dataset.attribute(i).indexOfValue(srf_temp.getFeature_value().toString());
				}
				else {
					Log.error("wrong feature types");
				}
			}
			// add this instance
			Instance inst = new DenseInstance(1.0, values);
			dataset.add(inst);
		}
	}
	
	/**
	 * Apply Weka "StringToWordVector" filter
	 * @throws Exception 
	 */
	private void stringToVector() throws Exception {
		StringToWordVector filter = new StringToWordVector();

		// config
//		filter.setIDFTransform(true);
		filter.setWordsToKeep(250);
		filter.setMinTermFreq(3);
		// we set stopwords, which should exclude existing keywords that we have included
		WordsFromFile wff = new WordsFromFile();
		wff.setStopwords(new File("original data//stopwords.txt"));
		filter.setStopwordsHandler(wff);
		filter.setStemmer(new LovinsStemmer());
		filter.setLowerCaseTokens(true);
		// apply the filter
		filter.setInputFormat(dataset);
		dataset = Filter.useFilter(dataset, filter);
		
		recordedWordVectors();
	}


	/**
	 * Test for WordToVector-based method 
	 * This method was designed to record word vectors generated from one data set.
	 */
	private void recordedWordVectors() {
		// After applying the filter, identify and return the newly generated set of attributes
		Enumeration<Attribute> atts = dataset.enumerateAttributes();
		LinkedList<Attribute> word_vectors = new LinkedList<Attribute>();
		boolean vector;
		while (atts.hasMoreElements()) {
			vector = true;
			Attribute att = (Attribute) atts.nextElement();
			if(att.name().equals(FeatureEnum.CATEGORY)) {
				vector = false;
			}
			for(String keyword: FeatureEnum.KEYWORD) {
				if (att.name().equals(keyword)) {
					vector = false;
					break;
				}
			}
			for(String rule: FeatureEnum.RULE) {
				if (att.name().equals(rule)) {
					vector = false;
					break;
				}
			}
			if(vector) {
				word_vectors.add(att);
			}
	      }
		this.word_features =  word_vectors;
	}
	
	
	/**
	 * Test for WordToVector-based method 
	 * Implement the string-to-vector function using a predefined word list
	 * This method is used when reclassify requirements in other domains.
	 * @throws Exception 
	 */
	private Instances stringToVectorUsingWordList(Instances modified_dataset, LinkedList<FeaturedSentence> cor_sentences, LinkedList<Attribute> word_vectors) throws Exception {
		Instances newData = new Instances(modified_dataset);
		
		for (Attribute att : word_vectors) {
			// first add that attribute
			Add filter;

			filter = new Add();
			filter.setAttributeIndex("last");
			filter.setAttributeName(att.name());
			filter.setInputFormat(newData);
			newData = Filter.useFilter(newData, filter);

			// assign value of each instance regarding that newly added attribute
			for (int i = 0; i < newData.numInstances(); i++) {
				String stemmed_sentence = EnglishProcessing.sentenceStemming(cor_sentences.get(i).ori_sentence);
				if(stemmed_sentence.contains(att.name())) {
					newData.instance(i).setValue(newData.numAttributes() - 1, 1);
				}
				else {
					newData.instance(i).setValue(newData.numAttributes() - 1, 0);
				}
			}
		}		
		return newData;
	}

	private void assignClass() {
		// set class in dataset
		dataset.setClassIndex(0);
	}
	
	
	private void outputDataToFile(String file_path) throws Exception {
		ArffSaver saver = new ArffSaver();
	    saver.setInstances(dataset);
	    saver.setFile(new File(file_path));
	    saver.writeBatch();
	}
	
	
	/**
	 * Generate training dataset that can be used in Weka, based on the processed SR data
	 * @param processed_data
	 * @param file_path
	 * @param include_sen
	 * @param output
	 */
	public void generateWekaDataFromSRP(LinkedList<FeaturedSentence> processed_data, boolean include_sen, String file_path) {
		sentences = processed_data;
		defineDataStructure();
		addData();
		
		// filter data, if necessary
		if(include_sen) {
			try {
				stringToVector();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// assign class index
		assignClass();
		
		//output the data to an arff file if necessary
		if(file_path!=null) {
			try {
				outputDataToFile(file_path);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	
	
	
	/**
	 * This function train a particular type of classifier using the dataset that has been created in analyzer.
	 * @param classifier, the classifier that is supposed to be created
	 * @throws Exception
	 */
	public Classifier trainClassifier(String classifier) throws Exception {
		Classifier cf = null;
		// create classifier
		switch(classifier) {
			case "NB":cf = new NaiveBayes(); 
				break;
			case "BN":cf = new BayesNet(); 
				break;
			case "LMT":cf = new LMT(); 
				break;
			case "J48":cf = new J48(); 
				String[] options = new String[1];
				options[0] = "-U"; // unpruned tree
				((J48)cf).setOptions(options); 
				break;
			case "SMO":cf = new SMO(); 
				break;
			case "Logistic":cf = new Logistic(); 
				break;
			case "DT":cf = new DecisionTable(); 
				break;
			case "PART":cf = new PART(); 
				break;
			default: Log.error("unknown classifier");
				break;
		}
		
		// set class in dataset
		dataset.setClassIndex(0);
		cf.buildClassifier(dataset);
		
		return cf;
	}
	
	

	/**
	 * evaluate a classifier using a particular set of test data 
	 * @param cf, the classifier that is supposed to be evaluate
	 * @param wa_test, this is the test data set,
	 * @param word_vectors, used for a deprecated function, which should be removed later
	 * @throws Exception
	 */
	public Evaluation evaluateWithTestData(Classifier cf, WekaAnalysisDataset wa_test, LinkedList<Attribute> word_vectors) throws Exception {
		Instances test_data = wa_test.dataset;;
		if(word_vectors!=null) { // if want to use these vectors...
			test_data = stringToVectorUsingWordList(wa_test.dataset, wa_test.sentences, word_vectors);
		}
		test_data.setClassIndex(0);
		
		// set training and test data
		Evaluation eval = new Evaluation(dataset);	// set training dataset
		eval.evaluateModel(cf, test_data); 	//set test dataset
		
		// represent evaluation result
		DecimalFormat df = new DecimalFormat("#.##");
		Log.info(cf.getClass().getName()+" Results"+"\n");
		Log.info("Precision: "+df.format(eval.precision(0))+"\n"+
						"Recall: "+df.format(eval.recall(0)) +"\n"+
						"F-Measure: "+df.format(eval.fMeasure(0))+"\n");
		
		return eval;
	}
	
	/**
	 * apply the cross validation for a particular classifier on a particular data set 
	 * @param cf
	 * @return
	 * @throws Exception
	 */
	public Evaluation crossEvaluate(Classifier cf) throws Exception {
		if(this.dataset==null) {
			Log.error("Dataset has not been loaded!");
			return null;
		}
		
		// try different k
		int [] k = {10};
//		int [] k = {2,3,4,5,6,7,8,9,10};
		
		Evaluation eval = null;
		for(int a :k) {
//			//start
//	        long lStartTime = System.nanoTime();
//			//end
//	        long lEndTime = System.nanoTime();
//			//time elapsed
//	        long output = lEndTime - lStartTime;
//	        Log.info("Elapsed time in milliseconds: " + output / 1000000);

			// evaluate (cross validation)
			eval = new Evaluation(dataset);
			eval.crossValidateModel(cf, dataset, a, new Random(1));
			
			DecimalFormat df = new DecimalFormat("#.##");//.format(dblVar);
//			Log.info(eval.toSummaryString("\n\n"+cf.getClass().getName()+ "   "+ a+"-fold "+" Results", false));
			Log.info(cf.getClass().getName()+ "   "+ a+"-fold "+" Results");
			Log.info("Precision: "+df.format(eval.precision(0))+"\n"+
							"Recall: "+df.format(eval.recall(0)) +"\n"+
							"F-Measure: "+df.format(eval.fMeasure(0))+"\n");
		}
		return eval;
	}
	
	
	/**
	 * Output the obtained classifiers
	 * @param cf
	 * @param output_classifier_path
	 * @throws IOException
	 */
	public void saveModel(Classifier cf, String output_classifier_path) throws IOException {
		 // serialize model
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(output_classifier_path));
		 oos.writeObject(cf);
		 oos.flush();
		 oos.close();
	}
	 
	
	
	public static void main(String[] args) {

		WordsFromFile wff = new WordsFromFile();
		wff.setStopwords(new File("original data//stopwords.txt"));
		
		System.out.println(wff.isStopword("authentication"));

	}
	
}
