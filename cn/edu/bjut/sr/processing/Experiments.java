package cn.edu.bjut.sr.processing;

import java.text.DecimalFormat;
import java.util.LinkedList;

import cn.edu.bjut.text.utility.Log;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;

public class Experiments {

	
	
	
	
	/**
	 * individual evaluation
	 * @throws Exception
	 */
	public static void individualExperiment(String method) throws Exception{
		// all experiments
//		String [] experiments = {"0","1","2","01","02","12","012","3"};
//		String [] experiments = {"0","1","2","01","02","12","012"};
		String [] experiments = {"3"};
		
		LinkedList<EvaluationResult> eval_results = new LinkedList<EvaluationResult>();

		for(String experiment:experiments) {
			EvaluationResult eval_result = new EvaluationResult(experiment);
			
			SRProcessing srp1 = new SRProcessing();
			WekaAnalysisDataset wa1 = new WekaAnalysisDataset();

			// load dataset
			String data_file_path = srp1.processDataAndGenerateArff(experiment, true, false, FeatureEnum.TRAIN_KEY_MULTI, FeatureEnum.TRAIN_RULE_MULTI, FeatureEnum.TRAIN_DEP_NO, wa1);
//			String data_file_path = srp1.processDataAndGenerateArff(experiment, true, false, FeatureEnum.TRAIN_KEY_MULTI, FeatureEnum.TRAIN_RULE_SINGLE, FeatureEnum.TRAIN_DEP_NO, wa1);

//			Log.info("Dataset "+experiment);
			// train classifier
			Classifier temp;
			String model_file_path = "";
//			wa1.dataset.setClassIndex(wa1.dataset.numAttributes()-1);
//			temp = wa1.trainClassifier("NB");
	//		temp = wa1.trainClassifier("BN");
//			temp = wa1.trainClassifier("LMT");
//			temp = wa1.trainClassifier("J48");
//			temp = wa1.trainClassifier("SMO");
	//		temp = wa1.trainClassifier("Logistic");
	//		temp = wa1.trainClassifier("DT");
//			temp = wa1.trainClassifier("PART");
			temp = wa1.trainClassifier(method);
			// evaluate the classifer
			Evaluation eval = wa1.crossEvaluate(temp);
			// record the results in a predefined order
			eval_result.precision = eval.precision(0);
			eval_result.recall = eval.recall(0);
			eval_result.f_measure = eval.fMeasure(0);
			eval_results.add(eval_result);
			
			// save classification model
			model_file_path = data_file_path.replace("arff", "model");
			wa1.saveModel(temp, model_file_path);
		}
		printInTable(eval_results);
//		outputToExcel(eval_results);
	}
	
	
	/**
	 * Output the evaluation results to an excel 
	 * @param eval_results
	 */
	static void outputToExcel(LinkedList<EvaluationResult> eval_results) {
		
	}


	/**
	 * print the evaluation results in console
	 * @param eval_results
	 */
	static void printInTable(LinkedList<EvaluationResult> eval_results) {
		System.out.println("Dataset	Precision	Recall		F-measure");

		double ava_pre=0;
		double ava_recall=0;
		double ava_fm=0;
		
		DecimalFormat df = new DecimalFormat("#.##");
		for (EvaluationResult er: eval_results) {
			System.out.println(er.dataset+"	"+df.format(er.precision)+"		"+df.format(er.recall)+"		"+df.format(er.f_measure));
			ava_pre+=Double.valueOf(df.format(er.precision));
			ava_recall+=Double.valueOf(df.format(er.recall));
			ava_fm+=Double.valueOf(df.format(er.f_measure));
		}
		System.out.println("average"+"	"+df.format(ava_pre/eval_results.size())+"		"+df.format(ava_recall/eval_results.size())+"		"+df.format(ava_fm/eval_results.size()));
	}



	/**
	 * cross-domain evaluation
	 * @throws Exception
	 */
	public static void crossDomainExperiment(String evaluated_set) throws Exception{
		
		String[] data_sets = {"0","1","2","01","02","12","012"};
//		String[] data_sets = {"0"};
		
		for(String data_set:data_sets) {
			SRProcessing srp1 = new SRProcessing();
			WekaAnalysisDataset wa1 = new WekaAnalysisDataset();
			if(!data_set.contains(evaluated_set)) {
				System.out.print(data_set +"->"+evaluated_set);
				// load the training dataset to wa1
				srp1.processDataAndGenerateArff(data_set, true, false, FeatureEnum.TRAIN_KEY_MULTI, FeatureEnum.TRAIN_RULE_MULTI, FeatureEnum.TRAIN_DEP_NO, wa1);
				
				// load the test dataset to wa2
				SRProcessing srp2 = new SRProcessing();
				WekaAnalysisDataset wa2 = new WekaAnalysisDataset();
				// load another dataset
				srp2.processDataAndGenerateArff(evaluated_set, true, false, FeatureEnum.TRAIN_KEY_MULTI, FeatureEnum.TRAIN_RULE_MULTI, FeatureEnum.TRAIN_DEP_NO, wa2); // no need to keep the file path
				
		//		wa1.classifyUseTrainTestData(wa2, "J48", null);
				Classifier temp = wa1.trainClassifier("J48");
				wa1.evaluateWithTestData(temp, wa2, null);

			}
		}
	}
	
	
	/**
	 * cross-domain evaluation
	 * @throws Exception
	 */
	public static void ownCrossDomainExperiment(String evaluated_set) throws Exception{
		
//		String[] data_sets = {"0","1","2","01","02","12","012"};
		String[] data_sets = {"012"};
		
		for(String data_set:data_sets) {
			SRProcessing srp1 = new SRProcessing();
			WekaAnalysisDataset wa1 = new WekaAnalysisDataset();
			if(!data_set.contains(evaluated_set)) {
				System.out.print(data_set +"->"+evaluated_set);
				// load the training dataset to wa1
				srp1.processDataAndGenerateArff(data_set, true, false, FeatureEnum.TRAIN_KEY_MULTI, FeatureEnum.TRAIN_RULE_MULTI, FeatureEnum.TRAIN_DEP_NO, wa1);
				
				// load the test dataset to wa2
				SRProcessing srp2 = new SRProcessing();
				WekaAnalysisDataset wa2 = new WekaAnalysisDataset();
				// load another dataset
				srp2.processDataAndGenerateArff(evaluated_set, true, false, FeatureEnum.TRAIN_KEY_MULTI, FeatureEnum.TRAIN_RULE_MULTI, FeatureEnum.TRAIN_DEP_NO, wa2); // no need to keep the file path
				
		//		wa1.classifyUseTrainTestData(wa2, "J48", null);
				Classifier temp = wa1.trainClassifier("LMT");
				wa1.evaluateWithTestData(temp, wa2, null);

			}
		}
	}
	
	
	/**
	 * Test for WordToVector-based method
	 * @throws Exception
	 */
	public static void crossDomainWithAttributeExperiment() throws Exception{
		SRProcessing srp1 = new SRProcessing();
		WekaAnalysisDataset wa1 = new WekaAnalysisDataset();
		// load dataset
		srp1.processDataAndGenerateArff("0", true, true, FeatureEnum.TRAIN_KEY_MULTI, FeatureEnum.TRAIN_RULE_MULTI, FeatureEnum.TRAIN_DEP_NO, wa1);
		
		// try the previous model on another dataset
		SRProcessing srp2 = new SRProcessing();
		WekaAnalysisDataset wa2 = new WekaAnalysisDataset();
		// load another dataset
		srp2.processDataAndGenerateArff("1", true, false, FeatureEnum.TRAIN_KEY_MULTI, FeatureEnum.TRAIN_RULE_MULTI, FeatureEnum.TRAIN_DEP_NO, wa2); // no need to keep the file path
		
		Classifier temp = wa1.trainClassifier("J48");
		wa1.evaluateWithTestData(temp, wa2, wa1.word_features);
	}
	
	
	/**
	 * can only save/load classifiers that have not been "StringToVector"
	 * @throws Exception
	 */
	public static void loadClassifierExperiment() throws Exception {
		// all experiments
		String model_file_path = "";

		SRProcessing srp1 = new SRProcessing();
		WekaAnalysisDataset wa1 = new WekaAnalysisDataset();

		// load dataset
		String data_file_path = srp1.processDataAndGenerateArff("0", true, false, FeatureEnum.TRAIN_KEY_MULTI, FeatureEnum.TRAIN_RULE_MULTI, FeatureEnum.TRAIN_DEP_NO, wa1);
		// classify and evaluate
		Classifier temp;
		temp = wa1.trainClassifier("J48");

		// save classification model
		model_file_path = data_file_path.replace("arff", "model");
		wa1.saveModel(temp, model_file_path);
		
		// new analyzer
		WekaAnalysisDataset wa2 = new WekaAnalysisDataset();
		SRProcessing srp2 = new SRProcessing();
		srp2.processDataAndGenerateArff("1", true, false, FeatureEnum.TRAIN_KEY_MULTI, FeatureEnum.TRAIN_RULE_MULTI, FeatureEnum.TRAIN_DEP_NO,wa2);

		// load classifier
		Classifier cf = (Classifier) weka.core.SerializationHelper.read(model_file_path);
		wa2.evaluateWithTestData(cf, wa2, null);
		
	}

	public static void main(String[] args) throws Exception {
//		String methods[] = {"NB","BN","LMT","J48","SMO","Logistic","DT","PART"};
		String methods[] = {"Logistic"};
		for(String method:methods) {
			System.out.println("\n\n\n**********"+method);
			individualExperiment(method);
		}
		
//		crossDomainExperiment("3");
		
		
		
//		crossDomainWithAttributeExperiment();
//		loadClassifierExperiment();
	}
}

/**
 * A temporal data structure for storing evaluation results.
 * @author tongli
 *
 */
class EvaluationResult {
	String dataset;
	double precision;
	double recall;
	double f_measure;
	
	
	public EvaluationResult() {
		super();
	}

	public EvaluationResult(String dataset) {
		super();
		this.dataset = dataset;
	}
}
