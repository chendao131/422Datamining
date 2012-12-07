package test;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

import test.MapReduce_KMedian_phase1;
import test.MapReduce_KMedian_phase1;

public class MapReduce_KMedians {
	public static void main(String args[]) throws Exception{
		long start = System.currentTimeMillis();
		JobConf confPhase1 = setConPhase1("MapReduce_KMedian_phase1");
        FileInputFormat.setInputPaths(confPhase1, new Path("/Users/lishuai/input"));
        FileOutputFormat.setOutputPath(confPhase1, new Path("/Users/lishuai/phase1"));
	    JobClient.runJob(confPhase1);
	    
		JobConf confPhase2 = setConPhase2("MapReduce_KMedian_phase2");
        FileInputFormat.setInputPaths(confPhase2, new Path("/Users/lishuai/phase1"));
        FileOutputFormat.setOutputPath(confPhase2, new Path("/Users/lishuai/phase2"));
	    JobClient.runJob(confPhase2);
	    
		JobConf confPhase3 = setConPhase3("MapReduce_KMedian_phase3");
        FileInputFormat.setInputPaths(confPhase3, new Path("/Users/lishuai/phase2"));
        FileOutputFormat.setOutputPath(confPhase3, new Path("/Users/lishuai/phase3"));
	    JobClient.runJob(confPhase3);
	    long end = System.currentTimeMillis();
	    System.out.println(end-start);
	}
	private static JobConf setConPhase1(String jobName){
		JobConf conf = new JobConf(MapReduce_KMedian_phase1.class);
		conf.setJobName(jobName);
	    conf.setOutputKeyClass(Text.class);
	    conf.setOutputValueClass(Text.class);
	    conf.setMapperClass(MapReduce_KMedian_phase1.MapPhase1.class);
	    conf.setReducerClass(MapReduce_KMedian_phase1.ReducePhase1.class);
	    conf.setInputFormat(TextInputFormat.class);
	    conf.setOutputFormat(TextOutputFormat.class);
	    return conf;
	}
	private static JobConf setConPhase2(String jobName){
		JobConf conf = new JobConf(MapReduce_KMedian_phase2.class);
		conf.setJobName(jobName);
	    conf.setOutputKeyClass(Text.class);
	    conf.setOutputValueClass(Text.class);
	    conf.setMapperClass(MapReduce_KMedian_phase2.MapPhase2.class);
	    conf.setReducerClass(MapReduce_KMedian_phase2.ReducePhase2.class);
	    conf.setInputFormat(TextInputFormat.class);
	    conf.setOutputFormat(TextOutputFormat.class);
	    return conf;
	}
	private static JobConf setConPhase3(String jobName){
		JobConf conf = new JobConf(MapReduce_KMedian_phase3.class);
		conf.setJobName(jobName);
	    conf.setOutputKeyClass(Text.class);
	    conf.setOutputValueClass(Text.class);
   	    conf.setMapperClass(MapReduce_KMedian_phase3.MapPhase3.class);
	    conf.setReducerClass(MapReduce_KMedian_phase3.ReducePhase3.class);
	    conf.setInputFormat(TextInputFormat.class);
	    conf.setOutputFormat(TextOutputFormat.class);
	    return conf;
	}
}
