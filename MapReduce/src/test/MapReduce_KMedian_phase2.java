package test;

import java.io.IOException;
import java.util.*;
import java.io.File;
import makedata.src.mdata.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;

import test.WordCount.Map;
import test.WordCount.Reduce;

public class MapReduce_KMedian_phase2 {
	private final static int E = 4;
	private final int SIZE = 20000;
	public static class MapPhase2 extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException{
			String temp = value.toString();
			String[] array = temp.split(",,");
			Text k = new Text(array[0]);
 	        StringTokenizer tokenizer = new StringTokenizer(array[1]);
 	        String vString = tokenizer.nextToken();
 	        Text v = new Text(vString);
			output.collect(k, v);
		}
	}
	public static class ReducePhase2 extends MapReduceBase implements Reducer<Text, Text, Text, Text>{
		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException{
			Integer sum = 0;
			while(values.hasNext()){
				 sum = sum+Integer.parseInt(values.next().toString());
			}
			Text v = new Text(sum.toString());
			output.collect(key, v);
		}
		
		private Text formText(ArrayList<point> c, int[] weight){
			String temp = "";
			for(int i=0;i<c.size();i++){
				Double tempx = c.get(i).x;
				Double tempy = c.get(i).y;
				temp = temp + tempx.toString()+","+tempy.toString()+" "+weight[i]+"\n";
			}
			return new Text(temp);
		}
		private ArrayList<point> getUnsampled(ArrayList<point> vi , ArrayList<point> c){
			Iterator<point> itr = vi.iterator();
			while(itr.hasNext()){
				point temp = itr.next();
				for(int i=0;i<c.size();i++){
					if(temp.equals(c.get(i)))
						itr.remove();
				}
			}
			return vi;
		}
		private Double distance(point a, point b){
			Double temp1 = a.x-b.x;
			Double temp2 = a.y-b.y;
			return Math.sqrt((temp1*temp1)+(temp2*temp2));
		}
	}
	
    public static void main(String[] args) throws Exception {
	      String input = "/Users/lishuai/phase1";
	      String output = "/Users/lishuai/phase2";
	      JobConf conf = new JobConf(WordCount.class);
	      conf.setJobName("MR_KMedian_pharse2");
	
	      conf.setOutputKeyClass(Text.class);
	      conf.setOutputValueClass(Text.class);
	
	      conf.setMapperClass(MapReduce_KMedian_phase2.MapPhase2.class);
	      //conf.setCombinerClass(Reduce.class);
	      conf.setReducerClass(MapReduce_KMedian_phase2.ReducePhase2.class);
	
	      conf.setInputFormat(TextInputFormat.class);
	      conf.setOutputFormat(TextOutputFormat.class);
	
	      FileInputFormat.setInputPaths(conf, new Path(input));
	      FileOutputFormat.setOutputPath(conf, new Path(output));
	
	      JobClient.runJob(conf);
	    }
}
