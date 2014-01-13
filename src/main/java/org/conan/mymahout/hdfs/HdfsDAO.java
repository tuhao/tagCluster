package org.conan.mymahout.hdfs;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.util.ReflectionUtils;
import org.apache.mahout.clustering.iterator.ClusterWritable;
import org.apache.mahout.math.VectorWritable;
import org.conan.mymahout.recommendation.ItemCFHadoop;

public class HdfsDAO {

    private static final String HDFS = "hdfs://10.65.110.31:9000";

    public HdfsDAO(Configuration conf) {
        this(HDFS, conf);
    }

    public HdfsDAO(String hdfs, Configuration conf) {
        this.hdfsPath = hdfs;
        this.conf = conf;
    }

    private String hdfsPath;
    private Configuration conf;

    public static void main(String[] args) throws IOException {
        JobConf conf = config();
        HdfsDAO hdfs = new HdfsDAO(conf);
//        hdfs.copyFile("datafile/item.csv", "/tmp/new");
//        hdfs.ls("/tmp/new");
        String remote = HDFS + "/user/hdfs/mix_data/seeds/part-randomSeed";
        hdfs.sequenceRead(remote);
    }        
    
    public static JobConf config(){
        JobConf conf = new JobConf(HdfsDAO.class);
        conf.setJobName("HdfsDAO");
        conf.addResource("classpath:/hadoop/core-site.xml");
        conf.addResource("classpath:/hadoop/hdfs-site.xml");
        conf.addResource("classpath:/hadoop/mapred-site.xml");
        return conf;
    }
    
    public void mkdirs(String folder) throws IOException {
        Path path = new Path(folder);
        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
        if (!fs.exists(path)) {
            fs.mkdirs(path);
            System.out.println("Create: " + folder);
        }
        fs.close();
    }
    

    public void rmr(String folder) throws IOException {
        Path path = new Path(folder);
        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
        fs.deleteOnExit(path);
        System.out.println("Delete: " + folder);
        fs.close();
    }

    public void ls(String folder) throws IOException {
        Path path = new Path(folder);
        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
        FileStatus[] list = fs.listStatus(path);
        System.out.println("ls: " + folder);
        System.out.println("==========================================================");
        for (FileStatus f : list) {
            System.out.printf("name: %s, folder: %s, size: %d\n", f.getPath(), f.isDir(), f.getLen());
        }
        System.out.println("==========================================================");
        fs.close();
    }

    public void createFile(String file, String content) throws IOException {
        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
        byte[] buff = content.getBytes();
        FSDataOutputStream os = null;
        try {
            os = fs.create(new Path(file));
            os.write(buff, 0, buff.length);
            System.out.println("Create: " + file);
        } finally {
            if (os != null)
                os.close();
        }
        fs.close();
    }

    public void copyFile(String local, String remote) throws IOException {
        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
        fs.copyFromLocalFile(new Path(local), new Path(remote));
        System.out.println("copy from: " + local + " to " + remote);
        fs.close();
    }

    public void download(String remote, String local) throws IOException {
        Path path = new Path(remote);
        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
        fs.copyToLocalFile(path, new Path(local));
        System.out.println("download: from" + remote + " to " + local);
        fs.close();
    }
    
    public void cat(String remoteFile) throws IOException {
        Path path = new Path(remoteFile);
        FileSystem fs = FileSystem.get(URI.create(hdfsPath), conf);
        FSDataInputStream fsdis = null;
        System.out.println("cat: " + remoteFile);
        try {  
            fsdis =fs.open(path);
            IOUtils.copyBytes(fsdis, System.out, 4096, false);  
          } finally {  
            IOUtils.closeStream(fsdis);
            fs.close();
          }
    }
    
    public List<String> sequenceRead(String remote){
    	List<String> result= new LinkedList<String>();
    	FileSystem fs = null;
    	SequenceFile.Reader reader = null;
    	Path path = null;
    	try {
			fs = FileSystem.get(URI.create(hdfsPath),conf);
			path = new Path(remote);
			reader = new SequenceFile.Reader(fs, path, conf);
			Writable key = (Writable)ReflectionUtils.newInstance(reader.getKeyClass(), conf);
			ClusterWritable value = (ClusterWritable)ReflectionUtils.newInstance(reader.getValueClass(), conf);
			long position = reader.getPosition();
			while (reader.next(key, value)){
				
				String syncSeen = reader.syncSeen() ? "*" : ""; 
                System.out.printf("[%s %s]\t%s\t%s\n", position, syncSeen, key, value.getValue());  
                position = reader.getPosition(); // beginning of next record  
			}
	    	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if (reader != null){
				IOUtils.closeStream(reader);
			}
		}
    	
    	return result;
    	
    }

    public void location() throws IOException {
        // String folder = hdfsPath + "create/";
        // String file = "t2.txt";
        // FileSystem fs = FileSystem.get(URI.create(hdfsPath), new
        // Configuration());
        // FileStatus f = fs.getFileStatus(new Path(folder + file));
        // BlockLocation[] list = fs.getFileBlockLocations(f, 0, f.getLen());
        //
        // System.out.println("File Location: " + folder + file);
        // for (BlockLocation bl : list) {
        // String[] hosts = bl.getHosts();
        // for (String host : hosts) {
        // System.out.println("host:" + host);
        // }
        // }
        // fs.close();
    }
    
    
    

}
