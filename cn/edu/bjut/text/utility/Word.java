
package cn.edu.bjut.text.utility;

/**
 * Word 
 * @author Tong Li
 */

public class Word {
	public int id; // we may also assign a particular id to a word
	public int position; // position in the sentence
	public String content; 
	public String type; //POS tag
	
	public Word() {
		super();
	}
	public Word(String content, String type) {
		super();
		this.content = content;
		this.type = type;
	}
	
	public void wordPrint(String s){
//		System.out.println(content + " " + type);
		System.out.println("we"+ s);
	}
}
