package cn.edu.bjut.text.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

/**
 * This class contains a set of common IO functions
 * @author tongli
 *
 */
public class IOUtil {

	/**
	 * Export the segmentation results to files
	 * Multiple sentences will be separated by inserting "\n" element 
	 * @param results
	 */
	public static void exportSegmentedSentence(List<String> sentence, String filePath, boolean append) {
		String content = "";
		for(String s: sentence){
			content = content + s + " ";
		}
		
		PrintWriter writer;
		try {
			writer = new PrintWriter(new FileWriter(filePath, append));
			writer.println(content);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Import segmented sentences from files
	 * "a b c d
	 * e f g"
	 * @param filePath
	 * @return
	 */
	public static List<String> importSegmentedSentence(String filePath) {
		List<String> sentence = new LinkedList<String>();
		try {
			File file = new File(filePath);
			BufferedReader bf = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String line = "";
			while ((line = bf.readLine()) != null){
				String[] temp = line.split(" ");
				for(String s:temp){
					sentence.add(s);
				}
			}
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sentence;
	}
	
	
	
	public static LinkedList<String> readSentencesFromPlainFile(String filePath) {
		LinkedList<String> results = new LinkedList<String>();
		
		try {
			File file = new File(filePath);
			BufferedReader bf = new BufferedReader(new FileReader(file.getAbsolutePath()));
			String line = "";
			while ((line = bf.readLine()) != null)
				results.add(line);
			bf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}
	
	
	/**
	 * Export specific content to specific files 
	 */
	public static void exportToFile(String content, String filePath, boolean append) {
		
		PrintWriter writer;
		try {
			writer = new PrintWriter(new FileWriter(filePath, append));
			writer.println(content);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
