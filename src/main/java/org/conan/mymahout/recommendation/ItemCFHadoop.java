package org.conan.mymahout.recommendation;

import java.io.File;

import org.apache.hadoop.mapred.JobConf;
import org.apache.mahout.cf.taste.hadoop.item.RecommenderJob;
import org.conan.mymahout.common.Constant;
import org.conan.mymahout.hdfs.HdfsDAO;

public class ItemCFHadoop {

    private static final String HDFS = "hdfs://10.65.110.31:9000";
    
    public static void main(String[] args) throws Exception {
       itemCF(args);
    }
    
    public static void itemCF(String[] args) throws Exception{
//    	 String localFile = Constant.USER_TAGS_SCORE;
         String inPath = HDFS + "/user/hdfs/userCF";
//         String inFile = inPath + "/user_tags_score.txt";
         String outPath = HDFS + "/user/hdfs/userCF_result/";
//         String outFile = outPath + "/part-r-00000";
         String tmpPath = HDFS + "/tmp/" + System.currentTimeMillis();
         
//         String localItemsFile = Constant.ITEM_FILE;
//         String itemsFile = HDFS +  "/user/hdfs/itemsFile.txt";

         JobConf conf = config();
         HdfsDAO hdfs = new HdfsDAO(HDFS, conf);
//         hdfs.rmr(inPath);
//         hdfs.mkdirs(inPath);
//         hdfs.copyFile(localFile, inPath);
//         hdfs.copyFile(localItemsFile, itemsFile);
         hdfs.ls(inPath);
//         hdfs.cat(inFile);

         StringBuilder sb = new StringBuilder();
         sb.append("--input ").append(inPath);
         sb.append(" --output ").append(outPath);
//         sb.append(" --itemsFile ").append(itemsFile);
//         sb.append(" --booleanData true");
         sb.append(" --numRecommendations ").append(20);  //maximum number of recommendations per user (default: 10)
         sb.append(" --similarityClassname org.apache.mahout.math.hadoop.similarity.cooccurrence.measures.TanimotoCoefficientSimilarity");
         sb.append(" --tempDir ").append(tmpPath);
         args = sb.toString().split(" ");

         RecommenderJob job = new RecommenderJob();
         job.setConf(conf);
         job.run(args);
//         File file = new File(Constant.USER_TAGS_RECOMMEND_HASHCODE_RESULT);
//         file.delete();
//         hdfs.download(outFile, Constant.USER_TAGS_RECOMMEND_HASHCODE_RESULT);
    }

    public static JobConf config() {
        JobConf conf = new JobConf(ItemCFHadoop.class);
        conf.setJobName("ItemCFHadoop");
//        conf.set("mapred.job.tracker", HDFS);
        conf.addResource("classpath:/hadoop/core-site.xml");
        conf.addResource("classpath:/hadoop/hdfs-site.xml");
        conf.addResource("classpath:/hadoop/mapred-site.xml");
        return conf;
    }

}
