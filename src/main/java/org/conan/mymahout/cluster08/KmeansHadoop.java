package org.conan.mymahout.cluster08;

import java.io.File;
import java.util.Iterator;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;
import org.apache.mahout.clustering.canopy.CanopyDriver;
import org.apache.mahout.clustering.classify.WeightedVectorWritable;
import org.apache.mahout.clustering.conversion.InputDriver;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.clustering.kmeans.RandomSeedGenerator;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.Vector;
import org.apache.mahout.utils.clustering.ClusterDumper;
import org.conan.mymahout.common.Constant;
import org.conan.mymahout.common.ReadFile;
import org.conan.mymahout.common.WriteFile;
import org.conan.mymahout.hdfs.HdfsDAO;
import org.conan.mymahout.recommendation.ItemCFHadoop;

public class KmeansHadoop {
    private static final String HDFS = "hdfs://10.65.110.31:9000";
    
    private static int k = ReadFile.readByLine(Constant.ITEM_FILE, "utf-8").size();
    
    public static void kmeans(String[] args) throws Exception{
    	String localFile = Constant.KMEANS_INPUT;
        String inPath = HDFS + "/user/hdfs/mix_data";
        String seqFile = inPath + "/seqfile";
        String seeds = inPath + "/seeds";
        String outPath = inPath + "/result/";
        String clusteredPoints = outPath + "/clusteredPoints";
        

        JobConf conf = config();
        HdfsDAO hdfs = new HdfsDAO(HDFS, conf);
        hdfs.rmr(inPath);
        hdfs.mkdirs(inPath);
        hdfs.copyFile(localFile, inPath);
        hdfs.ls(inPath);
        InputDriver.runJob(new Path(inPath), new Path(seqFile), "org.apache.mahout.math.RandomAccessSparseVector");
        Path seqFilePath = new Path(seqFile);
        Path clustersSeeds = new Path(seeds);
        DistanceMeasure measure = new EuclideanDistanceMeasure();
//        clustersSeeds = RandomSeedGenerator.buildRandom(conf, seqFilePath, clustersSeeds, k, measure);
        
        CanopyDriver.run(seqFilePath, clustersSeeds, measure, (float)3.0,(float)2.0, true, 0.01, false);
        clustersSeeds = new Path(seeds + "/clusters-0-final");
        
        KMeansDriver.run(conf, seqFilePath, clustersSeeds, new Path(outPath), measure, 0.01, 20, true, 0.01, false);

        Path outGlobPath = new Path(outPath, "clusters-*-final");
        Path clusteredPointsPath = new Path(clusteredPoints);
        System.out.printf("Dumping out clusters from clusters: %s and clusteredPoints: %s\n", outGlobPath, clusteredPointsPath);

        ClusterDumper clusterDumper = new ClusterDumper(outGlobPath, clusteredPointsPath);
//        clusterDumper.printClusters(null);
        displayCluster(clusterDumper);
    }

    public static void main(String[] args) throws Exception {
        kmeans(args);
    }

    public static JobConf config() {
        JobConf conf = new JobConf(ItemCFHadoop.class);
        conf.setJobName("ItemCFHadoop");
        conf.addResource("classpath:/hadoop/core-site.xml");
        conf.addResource("classpath:/hadoop/hdfs-site.xml");
        conf.addResource("classpath:/hadoop/mapred-site.xml");
        return conf;
    }

    public static void displayCluster(ClusterDumper clusterDumper) {
        Iterator<Integer> keys = clusterDumper.getClusterIdToPoints().keySet().iterator();
        int number = 1;
        while (keys.hasNext()) {
        	String output = Constant.CLUSTER_FILE_HEAD + number + ".csv";
        	File file = new File(output);
        	file.delete();
            Integer center = keys.next();
            System.out.println("Center:" + center);
            for (WeightedVectorWritable point : clusterDumper.getClusterIdToPoints().get(center)) {
            	StringBuffer sb = new StringBuffer();
                Vector v = point.getVector();
                System.out.println(point);
                for (int i =0;i <k;i++){
                	sb.append(v.get(i)).append(",");
                }
                sb = new StringBuffer(sb.substring(0, sb.lastIndexOf(",")));
                sb.append("\n");
                WriteFile.writeFile(output,sb.toString());
            }
            number ++;
        }
    }
}
