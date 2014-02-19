package org.conan.mymahout.recommendation;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.conan.mymahout.common.Constant;
import org.conan.mymahout.common.ReadFile;
import org.conan.mymahout.common.Simplify;
import org.conan.mymahout.common.WriteFile;


public class CalculateScore {
	
	public static Map<String,Integer> tagMap = new HashMap<String, Integer>();
	public static List<String> userTagsList = new LinkedList<String>();
	public static List<String> itemWordList = new LinkedList<String>();
	
	static{
		itemWordList = ReadFile.readByLine(Constant.ITEM_WORDS, "utf-8");
		tagMap = ReadFile.clusteSimilarTagProcess(Constant.TAGS_CSV, "gbk",itemWordList);
		userTagsList = ReadFile.readByLine(Constant.USER_TAGS_CSV, "gbk");
	}
	
	public static void itemsFile(){
		File file = new File(Constant.ITEM_FILE);
		file.delete();
		for(String item:itemWordList){
			item = item.toLowerCase();
			item = Simplify.toSimple(item);
			WriteFile.writeFile(Constant.ITEM_FILE, item.hashCode() + "\n");
		}
	}
	
	public static void calculate(){
//		File file = new File(Constant.USER_TAGS_SCORE);
//		file.delete();
		for(String item:userTagsList){
			String[] line = item.replace("\"","").split(",");
			for(int i =1;i < line.length;i++){
				String sc = Simplify.toSimple(line[i]);
				sc = sc.toLowerCase();
				for(String itemWord:itemWordList){
					if (sc.contains(itemWord)){
						sc = itemWord;
						break;
					}
				}
				if(tagMap.get(sc) == null) continue;
				double tfidf = TFIDF.tfidf(1, line.length - 1, Constant.USER_SUM, tagMap.get(sc));
				WriteFile.writeFile(Constant.USER_TAGS_SCORE, line[0] + "," + sc.hashCode() + "," + tfidf + "\n");
			}
		}
	}
	
	public static void main(String[] args){
//		calculate();
		itemsFile();
	}

}
