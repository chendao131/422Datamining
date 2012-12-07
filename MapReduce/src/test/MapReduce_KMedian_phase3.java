package test;

import java.io.IOException;
import java.util.*;
import java.io.File;
import makedata.src.mdata.*;
import k_medians.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

import test.WordCount.Map;
import test.WordCount.Reduce;

public class MapReduce_KMedian_phase3 {
	private final static int E = 4;
	private final int SIZE = 20000;
	private static LinkedList<point> c = new LinkedList<point>();
	private static ArrayList<Integer> weight = new ArrayList<Integer>();
	public static class MapPhase3 extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException{
			String temp = value.toString();
 	        StringTokenizer tokenizer = new StringTokenizer(temp);
 	        String pString = tokenizer.nextToken();
			String[] p = pString.split(",");
			c.add(new point(Double.parseDouble(p[0]),Double.parseDouble(p[1])));
 	        Integer i = Integer.parseInt(tokenizer.nextToken());
 	        weight.add(i);
 	        output.collect(new Text(), new Text());
		}
	}
	public static class ReducePhase3 extends MapReduceBase implements Reducer<Text, Text, Text, Text>{
		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException{
			point cluster[]=null;
			int k = 3;
			int threshold = 0;
			int runTimes = 20;
			KMediansLloyd kld = new KMediansLloyd(k, c, weight,threshold, runTimes);
			cluster = kld.getClusters();
			System.out.println("k="+k+"\n"+"threshold="+threshold+"\n"+runTimes+"\n");
			output.collect(formText(c, cluster), new Text());
		}
		private Text formText(LinkedList<point> c, point[] cluster){
			String temp = "";
			for(int i=0;i<c.size();i++){
				temp = temp+c.get(i).toString()+" "+cluster[i].toString()+"\n";
			}
			return new Text(temp);
		}
	}
	
    public static void main(String[] args) throws Exception {
	      String input = "/Users/lishuai/phase2";
	      String output = "/Users/lishuai/phase3";
	      JobConf conf = new JobConf(MapReduce_KMedian_phase3.class);
	      conf.setJobName("MR_KMedian_pharse3");
	
	      conf.setOutputKeyClass(Text.class);
	      conf.setOutputValueClass(Text.class);
	
	      conf.setMapperClass(MapReduce_KMedian_phase3.MapPhase3.class);
	      //conf.setCombinerClass(Reduce.class);
	      conf.setReducerClass(MapReduce_KMedian_phase3.ReducePhase3.class);
	
	      conf.setInputFormat(TextInputFormat.class);
	      conf.setOutputFormat(TextOutputFormat.class);
	
	      FileInputFormat.setInputPaths(conf, new Path(input));
	      FileOutputFormat.setOutputPath(conf, new Path(output));
	
	      JobClient.runJob(conf);
	    }
}
