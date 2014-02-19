package org.conan.mymahout.recommendation;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.conan.mymahout.common.Constant;
import org.conan.mymahout.common.ReadFile;
import org.conan.mymahout.common.WriteFile;

public class CFGenerate {
	
	private static Map<String,Map<String,String>> userTagMap = new HashMap<String,Map<String,String>>();
	
	private static BigDecimal one = new BigDecimal(1);
	
	private static BigDecimal max = null;
	private static BigDecimal min = null;
	private static BigDecimal range = null;
	
	static{
		preProcess();
	}
	
	private static void preProcess(){
		String strMax = "0.0000";
		String strMin = "1.0000";

		List<String> recHashResults = ReadFile.readByLine(Constant.USER_TAGS_RECOMMEND_HASHCODE_RESULT, "utf-8");
		for (String item:recHashResults){
			try{
				String[] line = item.split("\\s+");
				String[] pairArray = line[1].replace("[","").replace("]","").split(",");
				String strWeiboId = line[0];
				for (String strPair:pairArray){
					String[] codeScore = strPair.split(":");
					String score = codeScore[1];
					if (userTagMap.get(strWeiboId) == null){
						Map<String,String> tempMap = new HashMap<String,String>();
						tempMap.put(codeScore[0],codeScore[1]);
						userTagMap.put(strWeiboId, tempMap);
					}else{
						userTagMap.get(strWeiboId).put(codeScore[0],codeScore[1]);
					}
					switch(score.compareTo(strMax)){
					case 1:
						strMax = score;
						break;
					}
					switch(score.compareTo(strMin)){
					case -1:
						strMin = score;
						break;
					}
				}
			}catch(NumberFormatException e){
				
			}
		}
		min = new BigDecimal(strMin);
		max = new BigDecimal(strMax);
		range = max.subtract(min);
		System.out.println("max:" + max.doubleValue());
		System.out.println("min:" + min.doubleValue());
	}
	
	private static double distance(double sim){
		BigDecimal divide = new BigDecimal(sim + min.doubleValue());
		return (one.divide(divide,3,RoundingMode.HALF_UP)).subtract(one).doubleValue();
	}
	
	private static double specificate(double score){
		BigDecimal para = new BigDecimal(score);
		return distance(para.subtract(min).divide(range, 3, RoundingMode.HALF_UP).doubleValue());
	}
	
	public static void generate(){
		File file = new File(Constant.KMEANS_INPUT);
		file.delete();
		file = new File(Constant.USER_TAG_PAIR);
		file.delete();
		List<String> itemWordsHash = ReadFile.readByLine(Constant.ITEM_FILE, "utf-8");
		String[] itemsHashs = new String[itemWordsHash.size()];
		itemWordsHash.toArray(itemsHashs);
		Arrays.sort(itemsHashs);
		for (Iterator<Entry<String, Map<String, String>>> it = userTagMap.entrySet().iterator();it.hasNext();){
			Entry<String, Map<String, String>> entry = (Entry<String, Map<String, String>>)it.next();
			String strWeiboId = (String)entry.getKey();
			Map<String,String> scoreMap = (Map<String,String>)entry.getValue();
			String strScore = null;
			StringBuffer userValue = new StringBuffer();
			for(String hash:itemsHashs){
				strScore = scoreMap.get(hash) == null ? String.valueOf(min.doubleValue()) : scoreMap.get(hash);
				try{
					double value = specificate(Double.parseDouble(strScore));
					WriteFile.writeFile(Constant.KMEANS_INPUT,value + " ");
					userValue.append(value).append(" ");
				}catch(NumberFormatException e){
					
				}
			}
			WriteFile.writeFile(Constant.KMEANS_INPUT,"\n");
			userValue.append(strWeiboId).append("\n");
			WriteFile.writeFile(Constant.USER_TAG_PAIR, userValue.toString());
		}
	}
	
	public static void main(String[] args){
		generate();
		
//		System.out.println(distance(0.1));
	}

}
