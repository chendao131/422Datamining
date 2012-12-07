package test;

	
	import java.io.IOException;
	import java.util.*;
	
	import org.apache.hadoop.fs.Path;
	import org.apache.hadoop.conf.*;
	import org.apache.hadoop.io.*;
	import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.*;
	
	public class SecWordCount {
	
	  public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
		  private final static IntWritable one = new IntWritable(1);
		  private Text word = new Text();
		  private IntWritable counter = new IntWritable(0);
	
	      public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable > output, Reporter reporter) throws IOException {
	        String line = value.toString();
	        StringTokenizer tokenizer = new StringTokenizer(line);
	        while (tokenizer.hasMoreTokens()) {
	          word.set(tokenizer.nextToken());
	          output.collect(word, one);
	        }
	      }
	    }
	
	   public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
	      public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
	       int sum = 0;
	        while (values.hasNext()) {
	          sum += values.next().get();
	        }
	        output.collect(key, new IntWritable(sum));
	      }
	    }
	
	    public static void main(String[] args) throws Exception {
	      String input = "/Users/lishuai/input";
	      String output = "/Users/lishuai/output";
	      JobConf conf = new JobConf(WordCount.class);
	      conf.setJobName("wordcount");
	
	      conf.setOutputKeyClass(Text.class);
	      conf.setOutputValueClass(IntWritable.class);
	
	      conf.setMapperClass(Map.class);
	      conf.setCombinerClass(Reduce.class);
	      conf.setReducerClass(Reduce.class);
	
	      conf.setInputFormat(TextInputFormat.class);
	      conf.setOutputFormat(TextOutputFormat.class);
	
	      FileInputFormat.setInputPaths(conf, new Path(input));
	      FileOutputFormat.setOutputPath(conf, new Path(output));
	      JobClient.runJob(conf);
	      
	
	      input = "/Users/lishuai/output";
	      output = "/Users/lishuai/final";
	      JobConf conf0 = new JobConf(WordCount.class);
	      conf0.setJobName("SecWordCount");
	
	      conf0.setOutputKeyClass(Text.class);
	      conf0.setOutputValueClass(IntWritable.class);
	
	      conf0.setMapperClass(Map.class);
	      conf0.setCombinerClass(Reduce.class);
	      conf0.setReducerClass(Reduce.class);
	
	      conf0.setInputFormat(TextInputFormat.class);
	      conf0.setOutputFormat(TextOutputFormat.class);
	
	      FileInputFormat.setInputPaths(conf0, new Path(input));
	      FileOutputFormat.setOutputPath(conf0, new Path(output));
	      JobClient.runJob(conf0);
	    }
 }