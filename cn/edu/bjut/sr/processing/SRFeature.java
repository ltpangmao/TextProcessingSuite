package cn.edu.bjut.sr.processing;

import cn.edu.bjut.text.utility.Log;

public class SRFeature {

	private String feature_name;
	private int feature_type;
	private String feature_value;
	
	
	
	public SRFeature(String feature_name, int feature_type) {
		super();
		this.feature_name = feature_name;
		this.feature_type = feature_type;
		this.feature_value = "";
	}


	public SRFeature(String feature_name, int feature_type, String feature_value) {
		super();
		this.feature_name = feature_name;
		this.feature_type = feature_type;
		this.feature_value = feature_value;
	}
	

	public String getFeature_name() {
		return feature_name;
	}

	public void setFeature_name(String feature_name) {
		this.feature_name = feature_name;
	}

	public int getFeature_type() {
		return feature_type;
	}

	public void setFeature_type(int feature_type) {
		this.feature_type = feature_type;
	}

	public String getFeature_value() {
		return feature_value;
	}

	public void setFeature_value(String feature_value) {
		this.feature_value = feature_value;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Log.info(FeatureEnumeration.KEYWORD.getClass().getName());
		
		String s = "I like this game very much because there is no reason...";
		
//		System.out.println(s.matches("\\S+\\slike\\s\\S+\\sgame"));
		s.replaceAll("like", "hate");
		System.out.println(s);
	}

}
