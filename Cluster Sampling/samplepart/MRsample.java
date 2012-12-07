package samplepart;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
//import org.apache.mahout.common.HadoopUtil;

//import MRsample.point;



public class MRsample {
	
	static LinkedList<point> H =new LinkedList<point>();
	static LinkedList<point> S =new LinkedList<point>();
	static LinkedList<point> R =new LinkedList<point>();
	static double v;

	public static class Mapper1 
    extends Mapper<Object, Text, Text, IntWritable>{
 
 //private final static IntWritable one = new IntWritable(int(Math.random()*1024));
 private Text word = new Text();
   
 public void map(Object key, Text value, Context context
                 ) throws IOException, InterruptedException {
   StringTokenizer itr = new StringTokenizer(value.toString());
   while (itr.hasMoreTokens()) {
     word.set(itr.nextToken());
     int ran = (int)(Math.random()*1024);
     if(ran==1024){
    	 ran = 1023;
     }
     context.write( word,new IntWritable(ran));
   }
 }
}
	
	public static class Mapper2 
    extends Mapper<Object, Text, IntWritable, Text>{
 
 //private final static IntWritable one = new IntWritable(int(Math.random()*1024));
 private Text word = new Text();
   
 public void map(Object key, Text value, Context context
                 ) throws IOException, InterruptedException {
   StringTokenizer itr = new StringTokenizer(value.toString());
   while (itr.hasMoreTokens()) {
     word.set(itr.nextToken());
     int ran = (int)(Math.random()*20);
     if(ran==20){
    	 ran = 19;
     }
     context.write( new IntWritable(ran),word);
   }
 }
}

public static class GoGroup 
    extends Reducer<Text,IntWritable,Text,IntWritable> {
 private IntWritable result = new IntWritable();

 public void reduce(Text key, Iterable<IntWritable> values, 
                    Context context
                    ) throws IOException, InterruptedException {
   int flag = 0;
   double ifS = (9*3*Math.pow(20000, 0.3))/20000;
   double ifH = (4*Math.pow(20000,0.3))/20000;
   //System.out.println("ifS = "+ifS);
   //System.out.println("ifH = "+ifH);
   //for (IntWritable val : values) {
	   //System.out.println(key);
     if (Math.random()<ifS){
    	 flag = 1;
     }
     if(Math.random()<ifH){
    	 flag = flag + 2;
     }
   //}
   result.set(flag);
   context.write(key, result);
 }
}

public static void main(String[] args) throws Exception {
	v=116;
	int con = 0;
	while(v>115){
		String path = "/home/nits/422P/out";
		con++;
		path = path + Integer.toString(con);
 Configuration conf = new Configuration();
 //String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
 //if (otherArgs.length != 2) {
   //System.err.println("Usage: wordcount <in> <out>");
   //System.exit(2);
 //}
 Job job1 = new Job(conf, "mapper1");
 job1.setJarByClass(MRsample.class);
 job1.setMapperClass(Mapper1.class);
 //job.setCombinerClass(GoGroup.class);
 job1.setReducerClass(GoGroup.class);
 job1.setOutputKeyClass(Text.class);
 job1.setOutputValueClass(IntWritable.class);
 FileInputFormat.addInputPath(job1, new Path("/home/nits/422P/data"));
 FileOutputFormat.setOutputPath(job1, new Path(path));
 //System.exit(job1.waitForCompletion(true) ? 0 : 1);
 job1.waitForCompletion(true);
 	String path1 = path + "/part-r-00000";
 	FileReader fr = new FileReader(path1);
	BufferedReader br = new BufferedReader(fr);
	while(true){
	String tmp = br.readLine();
	double x;
	double y;
	int flag;
	int fir=0,sec = 0;
	if(tmp == null){
		break;
	}
	for(int i=0;i<tmp.length();i++){
		//System.out.println(tmp.substring(i,i));
		if(tmp.charAt(i)==','){
			fir = i;
		}if(tmp.charAt(i)=='\t'){
			sec = i;
		}
	}
	
	//System.out.println("fir = "+fir);
	x = Double.parseDouble(tmp.substring(1, fir-1));
	y = Double.parseDouble(tmp.substring(fir+1, sec-1));
	flag = Integer.parseInt(tmp.substring(sec+1));
	point p = new point();
	p.x = x;
	p.y = y;
	R.add(p);
	//System.out.println("x = "+x+"\ty = "+y+"\tflag = "+flag);
	if(flag == 1||flag == 3){
		
		S.add(p);
	}if(flag == 2||flag == 3){
		
		H.add(p);
	}}
	br.close();
	fr.close();
	//sample ss = new sample();
	v = sample.select(H, S);
	System.out.println("Ssize = "+S.size());
	System.out.println("Hsize = "+H.size());
	
	System.out.println("v = "+v);
}}
	   }
	

