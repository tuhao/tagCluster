package org.conan.mymahout.recommendation;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.conan.mymahout.common.Constant;
import org.conan.mymahout.common.ReadFile;
import org.conan.mymahout.common.WriteFile;


public class CFReadResult {
	
	private static Map<Integer,String> userTagMap = new HashMap<Integer, String>();
	private static Map<Integer,String> tagHashCodeMap = new HashMap<Integer, String>();
	
	
	static{
		tagHashCodeMap = ReadFile.readHashcodeMap(Constant.TAGS_CSV,"gbk");
		userTagMap = ReadFile.readMapIntegerString(Constant.USER_TAGS_CSV, "gbk");
	}
	
	
	public static void main(String[] args){
		File deleteFile = new File(Constant.USER_TAGS_RECOMMEND_RESULT);
		deleteFile.delete();
			List<String> result = ReadFile.readByLine(Constant.USER_TAGS_RECOMMEND_HASHCODE_RESULT, "utf-8");
			int count = 0;
			for (String item : result){
				String[] temp = item.split("\\s+");
				try{
					int weibo_id = Integer.parseInt(temp[0]);
					String user_tags = userTagMap.get(weibo_id);
					System.out.println(weibo_id + " " + user_tags);
					WriteFile.writeFile(Constant.USER_TAGS_RECOMMEND_RESULT, weibo_id + " " + user_tags + "\n");
					String[] recommend_tags = temp[1].replace("[", "").replace("]","").split(",");
					for(String recommendEle : recommend_tags){
						if (count >= 3) break;
						String[] recommendTemp = recommendEle.split(":");
						int tagHashcode = Integer.parseInt(recommendTemp[0]);
						String str_tag = tagHashCodeMap.get(tagHashcode);
						System.out.println(str_tag + ":" + recommendTemp[1]);
						WriteFile.writeFile(Constant.USER_TAGS_RECOMMEND_RESULT, str_tag + ":" + recommendTemp[1] + "\n");
					}
					System.out.println();
					WriteFile.writeFile(Constant.USER_TAGS_RECOMMEND_RESULT, "\n");
				}catch(NumberFormatException e){
					
				}
			}
			System.out.println(count);  //10:27453
	}

}
