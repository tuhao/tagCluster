package org.conan.mymahout.recommendation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;
import org.conan.mymahout.common.Constant;
import org.conan.mymahout.common.OJDBC;
import org.conan.mymahout.common.OJDBC.TagScore;
import org.conan.mymahout.common.ReadFile;
import org.conan.mymahout.common.WriteFile;


public class CFReadSample {
	
//	private static Map<Integer,String> userTagMap = new HashMap<Integer, String>();
	private static Map<Integer,String> tagHashCodeMap = new HashMap<Integer, String>();
	public static List<String> itemWordList = new LinkedList<String>();
	
	static{
		itemWordList = ReadFile.readByLine(Constant.ITEM_WORDS, "utf-8");
		tagHashCodeMap = ReadFile.clusteSimilarTagHashcodeMap(Constant.TAGS_CSV,"gbk",itemWordList);
		
//		userTagMap = ReadFile.readMapIntegerString(Constant.USER_TAGS_CSV, "gbk");
	}
	
	public static void readFromLocal(){
		OJDBC factory = new OJDBC();
//		File deleteFile = new File(Constant.USER_TAGS_RECOMMEND_RESULT);
//		deleteFile.delete();
			List<String> result = ReadFile.readByLine(Constant.USER_TAGS_RECOMMEND_HASHCODE_RESULT, "utf-8");
//			int count = 0;
			for (String item : result){
				String[] temp = item.split("\\s+");
				try{
//					int weibo_id = Integer.parseInt(temp[0]);
//					String user_tags = userTagMap.get(weibo_id);
//					System.out.println(weibo_id + " " + user_tags);
//					WriteFile.writeFile(Constant.USER_TAGS_RECOMMEND_RESULT, weibo_id + " " + user_tags + "\n");
					String[] recommend_tags = temp[1].replace("[", "").replace("]","").split(",");
					for(String recommendEle : recommend_tags){
//						if (count++ >= 3) break;
						String[] recommendTemp = recommendEle.split(":");
						int tagHashcode = Integer.parseInt(recommendTemp[0]);
						String str_tag = tagHashCodeMap.get(tagHashcode);
						System.out.println(str_tag + ":" + recommendTemp[1]);
						TagScore tagScore = new TagScore(temp[0],str_tag,recommendTemp[1]);
						factory.insert(tagScore);
//						WriteFile.writeFile(Constant.USER_TAGS_RECOMMEND_RESULT, str_tag + ":" + recommendTemp[1] + "\n");
					}
//					System.out.println();
//					WriteFile.writeFile(Constant.USER_TAGS_RECOMMEND_RESULT, "\n");
				}catch(NumberFormatException e){
					
				}
			}
			factory.close();
//			System.out.println(count);  //10:27453
	}
	
	public static void main(String[] args){
		
	}
	
    public static JobConf config() {
	    JobConf conf = new JobConf(ItemCFHadoop.class);
	    conf.setJobName("ItemCFHadoop");
//	    conf.set("mapred.job.tracker", HDFS);
	    conf.addResource("classpath:/hadoop/core-site.xml");
	    conf.addResource("classpath:/hadoop/hdfs-site.xml");
	    conf.addResource("classpath:/hadoop/mapred-site.xml");
	    return conf;
	}
    
	public static void readFromHDFS(){
		OJDBC factory = new OJDBC();
		String remoteFile = Constant.HDFS + "/user/hdfs/userCF/result/part-r-00000";
		Path path = new Path(remoteFile);
	    FileSystem fs = null;
		try {
			 JobConf conf = config();
			 fs = FileSystem.get(URI.create(Constant.HDFS), conf);
			 BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(path)));
		     String line = null;
		     while((line = br.readLine()) != null){
		    	 String[] temp = line.split("\\s+");
		    	 String[] recommend_tags = temp[1].replace("[", "").replace("]","").split(",");
				 for(String recommendEle : recommend_tags){
					String[] recommendTemp = recommendEle.split(":");
					int tagHashcode = Integer.parseInt(recommendTemp[0]);
					String str_tag = tagHashCodeMap.get(tagHashcode);
					System.out.println(str_tag + ":" + recommendTemp[1]);
					TagScore tagScore = new TagScore(temp[0],str_tag,recommendTemp[1]);
					factory.insert(tagScore);
				}
		        System.out.println(line);
		     }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				if(fs != null){
					fs.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		factory.close();
	}

}
