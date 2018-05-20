package cn.edu.bjut.text.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class TrivialProcessing {
	public static void main(String[] args) throws Exception {
//		String path = "/Users/tongli/OneDrive/reseach/Working stuff/ML-learning/Data/SecReq/data/three sets/";
//		processOriCVS(path);
		
//		addQuote();
//		prepareWeightedFile();
		
//		String path = "/Users/tongli/OneDrive/temp/data/AppReviews.csv";
//		importCSVFile(path, "gbk", 10);
//		
//		path = "/Users/tongli/OneDrive/temp/data/new_reviews.csv";
//		importCSVFile(path, "utf8", 10);
		
		//String.class,int.class,int.class
		
		String unknown_class_name = "cn.edu.bjut.text.utility.Word";
		String unknown_method_name = "wordPrint";
		String unknown_method_content = "test";
		String parameter_type_string = "java.lang.String";
		
		Class parameter_type = Class.forName(parameter_type_string);
		Class unknown_class = Class.forName(unknown_class_name);
		Object o= unknown_class.newInstance();
		Method unknown_method=unknown_class.getMethod(unknown_method_name,parameter_type);
		unknown_method.invoke(o,unknown_method_content);
		
		
	}
	// course example
	@SuppressWarnings({"rawtypes","unchecked"})
	static void weirdMethod(String unknown_class_name, 
			String unknown_method_name, String parameter_type_name,
			String unknown_para_content) throws Exception {
		Class parameter_type = Class.forName(parameter_type_name);
		Class unknown_class = Class.forName(unknown_class_name);
		Object o = unknown_class.newInstance();
		Method unknown_method = unknown_class.getMethod(unknown_method_name, parameter_type);
		unknown_method.invoke(o, unknown_para_content);
	}

	
	
	public static void importCSVFile(String path, String encode, int num) {
		String result = "";
		int count = 0;
		try {
			File file = new File(path);
//			BufferedReader bf = new BufferedReader(new FileReader(file.getAbsolutePath()));
			// Switch to gbk encoding
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), encode);
			BufferedReader bf = new BufferedReader(isr);
			
			String line = "";
			while ((line = bf.readLine()) != null && count<num) {
				line =line.replace(" ", "");
				result += line + "\n";
				count++;
			}
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		result=result.replaceAll(",-",",");
		System.out.println(result);
	}
	
	/**
	 * Add "\"" to a particular string
	 * this is a string, sec
	 */
	private static void processOriCSV(String path) {
		String result = "";
		try {
			File file = new File(path+"ePurse.csv");
			BufferedReader bf = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String line = "";
			String new_line = "";
			while ((line = bf.readLine()) != null) {
				// clear all quote mark first
				line = line.replaceAll("\"", "");
				// add mark intentionally
				new_line = "\"" + line.substring(0, line.indexOf(";")) + "\"," + line.substring(line.indexOf(";")+1);
				result += new_line + "\n";
			}
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		result=result.replaceAll(",-",",");
		System.out.println(result);
		
		result = "@RELATION sr\n"
				+ "@ATTRIBUTE SecurityRequirements string\n"
				+ "@ATTRIBUTE @@type@@ {sec,nonsec}\n"
				+ "@DATA\n"+result;
		
		PrintWriter writer;
		try {
			writer = new PrintWriter(new FileWriter(path+"ePurse.arff", false));
			writer.println(result);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	/**
	 * Add "\"" to a particular string
	 * this is a string, sec
	 */
	private static void addQuote() {
		String result = "";
		try {
			File file = new File("/Users/tongli/OneDrive/reseach/Working stuff/ML-learning/Data/SecReq/arff2/3-all-ori.csv");
			BufferedReader bf = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String line = "";
			String new_line = "";
			while ((line = bf.readLine()) != null) {
				if (!line.startsWith("\"")) {
					new_line = "\"" + line.substring(0, line.indexOf(",")) + "\"" + line.substring(line.indexOf(","));
					result += new_line + "\n";
				}
				else {
					result += line + "\n";
				}
			}
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		result=result.replaceAll(",-",",");
		System.out.println(result);
		
		
		
		result = "@RELATION sr\n"
				+ "@ATTRIBUTE SecurityRequirements string\n"
				+ "@ATTRIBUTE @@type@@ {sec,nonsec,unknown}\n"
				+ "@DATA\n"+result;
		
		
		PrintWriter writer;
		try {
			writer = new PrintWriter(new FileWriter("/Users/tongli/OneDrive/reseach/Working stuff/ML-learning/Data/SecReq/arff2/current2.arff", false));
			writer.println(result);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * only for a particular use
	 * prepare an arff file with weight
	 * This function follows the previous one
	 * @return
	 */
	private static void prepareWeightedFile(){
		
		String result = "";
		try {
			File file = new File("/Users/tongli/OneDrive/reseach/Working stuff/ML-learning/Data/SecReq/arff/test2.csv");
			BufferedReader bf = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String line = "";
			String new_line = "";
			while ((line = bf.readLine()) != null) {
				// add "{}"
				line=line.replaceAll(",-",",");// first remove negative weight
				new_line = line.substring(0, line.lastIndexOf(",")+1) + "{" + line.substring(line.lastIndexOf(",")+1)+"}";
				result += new_line + "\n";
			}
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(result);
		
		PrintWriter writer;
		try {
			writer = new PrintWriter(new FileWriter("/Users/tongli/OneDrive/reseach/Working stuff/ML-learning/Data/SecReq/arff/test3.csv", false));
			writer.println(result);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
