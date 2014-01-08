package org.conan.mymahout.recommendation;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class TFIDF {
	
	public static double tfidf(int ntag,int tagSum,int userSum,int tagsCnt){
		return new BigDecimal(tf(ntag,tagSum) * idf(userSum,tagsCnt)).setScale(4, RoundingMode.HALF_UP).doubleValue();
	}
	
	private static double tf(int ntag,int tagSum){
		return new BigDecimal(ntag).divide(new BigDecimal(tagSum),4,RoundingMode.HALF_UP).doubleValue();
	}
	
	private static double idf(int userSum,int tagsCnt){
		BigDecimal temp = new BigDecimal(userSum).divide(new BigDecimal(tagsCnt), 4, RoundingMode.HALF_UP);
		return Math.log10(temp.doubleValue());
	}

}
