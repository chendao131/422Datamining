package MRsample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

//import javax.swing.text.html.HTMLDocument.Iterator;

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



public class MRS {
	
	static LinkedList<point> H =new LinkedList<point>();
	static LinkedList<point> S =new LinkedList<point>();
	static LinkedList<point> R =new LinkedList<point>();
	public static double v;
	public static int rsize = 20000;  //<<<<<<<<<----------------Here to change the general data size
	static int counter = 0;
	static double e = 0.35;           //<<<<<<<<<----------------Here to change the value of Epsilon
	static double k = 3;			  //<<<<<<<<<----------------Here to change the value of k
	public static class Mapper1 
    extends Mapper<Object, Text, Text, IntWritable>{
 
 //private final static IntWritable one = new IntWritable(int(Math.random()*1024));
 private Text points = new Text();
   
 public void map(Object key, Text value, Context context
                 ) throws IOException, InterruptedException {
   StringTokenizer itr = new StringTokenizer(value.toString());
   while (itr.hasMoreTokens()) {
     points.set(itr.nextToken());
     int ran = (int)(Math.random()*1024);
     if(ran==1024){
    	 ran = 1023;
     }
     context.write( points, new IntWritable(ran));
   }
 }
}
	
	public static class Mapper2 
    extends Mapper<Object, Text, Text, Text>{
 
 //private final static IntWritable one = new IntWritable(int(Math.random()*1024));
 
   
 public void map(Object key, Text value, Context context
                 ) throws IOException, InterruptedException {
	 Text points = new Text();
   StringTokenizer itr = new StringTokenizer(value.toString());
   while (itr.hasMoreTokens()) {
     points.set(itr.nextToken());
     int ran = (int)(Math.random()*20);
     if(ran==20){
    	 ran = 19;
     }
     context.write( new Text(Integer.toString(ran)),points);
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
   double ifS = (9*3*Math.pow(rsize, e))/rsize;
   double ifH = (4*Math.pow(rsize,e))/rsize;
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

public static class reduce2 
extends Reducer<Text,Text,Text,Text> {


public void reduce(Text key, Iterable<Text> values, 
                Context context
                ) throws IOException, InterruptedException {
Text forvalue = new Text();
Iterator<Text> tmp = values.iterator();
String mystr ="";
int count = 0;

while(tmp.hasNext()){
	Text mytext = tmp.next();
	point mypoint = new point();
	String strtmp = mytext.toString();
	String[] xy = strtmp.split(",");
	mypoint.x = Double.parseDouble(xy[0]);
	mypoint.y = Double.parseDouble(xy[1]);
	double dis = sample.minDis(mypoint, S);
	
	if(dis > v){
		if(count ==0){
		mystr = strtmp;
		count++;
		}else{
			mystr = mystr + "\n" + strtmp;
		}
		counter++;
	}
	
}
//System.out.println("ifS = "+ifS);
//System.out.println("ifH = "+ifH);
//for (IntWritable val : values) {
   //System.out.println(key);

 
//}

context.write( new Text(mystr), forvalue);
}
}

public static void main(String[] args) throws Exception {
	v=116;
	int con = 0;
	
	//double k = 3;
	//double e = 0.35;
	
	long start = System.currentTimeMillis();

	
	while(true){
		double ifcon = (4/e)*k*Math.pow(rsize, e)*Math.log(rsize);
	
	//while(v>115){
		String path = "/home/nits/422P/out";						//<<<<<<<<<<<<<<------------Path of the output of Reduce1
		String Path = "/home/nits/422P/output";						//<<<<<<<<<<<<<<------------Path of the output of Reduce2
		con++;
		path = path + Integer.toString(con);
		Path = Path + Integer.toString(con);
 Configuration conf = new Configuration();
 //String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
 //if (otherArgs.length != 2) {
   //System.err.println("Usage: wordcount <in> <out>");
   //System.exit(2);
 //}
 Job job1 = new Job(conf, "mapper1");
 job1.setJarByClass(MRS.class);
 job1.setMapperClass(Mapper1.class);
 //job.setCombinerClass(GoGroup.class);
 job1.setReducerClass(GoGroup.class);
 job1.setOutputKeyClass(Text.class);
 job1.setOutputValueClass(IntWritable.class);
 FileInputFormat.addInputPath(job1, new Path("/home/nits/422P/data"));			//<<<<<<<<<-------Path of the file which contains the general dataset
 FileOutputFormat.setOutputPath(job1, new Path(path));
 //System.exit(job1.waitForCompletion(true) ? 0 : 1);
 job1.waitForCompletion(true);
 R.clear();
 H.clear();
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
	//System.out.println("x = "+tmp.substring(0, fir) + " y = "+tmp.substring(fir+1, sec));
	x = Double.parseDouble(tmp.substring(0, fir));
	y = Double.parseDouble(tmp.substring(fir+1, sec));
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
	FileWriter fw = new FileWriter("/home/nits/422P/data1/sample.txt");			//<<<<<<<<<<<---------Path of the input dataset of Map2
	BufferedWriter bw = new BufferedWriter(fw);
	for(int i = 0;i<R.size();i++){
		point tmp = R.get(i);
	String px = Double.toString(tmp.x);
	String py = Double.toString(tmp.y);
	String pp = px+","+py;
	bw.write(pp);
	bw.newLine();
}
	bw.flush();
	bw.close();
	fw.close();
	counter = 0;
	Configuration conf1 = new Configuration();
	Job job2 = new Job(conf1, "mapper2");
	 job2.setJarByClass(MRS.class);
	 job2.setMapperClass(Mapper2.class);
	 //job.setCombinerClass(GoGroup.class);
	 job2.setReducerClass(reduce2.class);
	 job2.setOutputKeyClass(Text.class);
	 job2.setOutputValueClass(Text.class);
	 FileInputFormat.addInputPath(job2, new Path("/home/nits/422P/data1"));		//<<<<<<<-----------Path of the file which contains the input dataset of Map2
	 FileOutputFormat.setOutputPath(job2, new Path(Path));
	 job2.waitForCompletion(true);
	 rsize =counter;
	 System.out.println("rsize = "+rsize);
	 R.clear();
	 	String Path1 = Path + "/part-r-00000";
	 	FileReader fr1 = new FileReader(Path1);
		BufferedReader br1 = new BufferedReader(fr1);
		while(true){
		String tmp = br1.readLine();
		double x;
		double y;
		int fir=0,sec = 0;
		if(tmp == null){
			break;
		}
		for(int i=0;i<tmp.length();i++){
			//System.out.println(tmp.substring(i,i));
			if(tmp.charAt(i)==','){
				fir = i;
			}if(tmp.charAt(i)=='.'){
				sec = i;
			}
		}
		//System.out.println("x = "+tmp.substring(0, fir) + " y = "+tmp.substring(fir+1, sec));
		x = Double.parseDouble(tmp.substring(0, fir));
		y = Double.parseDouble(tmp.substring(fir+1, sec));
		
		point p = new point();
		p.x = x;
		p.y = y;
		R.add(p);
	}
		br1.close();
		fr1.close();
		System.out.println("r.size = "+R.size());
		FileWriter fw1 = new FileWriter("/home/nits/422P/data/data.txt");	//<<<<<<<<<------------The path of the general dataset
		BufferedWriter bw1 = new BufferedWriter(fw1);
		for(int i=0;i<R.size();i++){
			point tmp = R.get(i);
			String px = Double.toString(tmp.x);
			String py = Double.toString(tmp.y);
			String pp = px+","+py;
			bw1.write(pp);
			bw1.newLine();
		}
		bw1.flush();
		bw1.close();
		fw1.close();
		System.out.println("ifcon = "+ ifcon);
		if(R.size() < ifcon){
			break;
		}
		}
	for(int j = 0;j<S.size();j++){
	point mypoint = S.get(j);
	R.add(mypoint);
	
	}
	System.out.println("final size = "+ R.size());
	FileWriter fw2 = new FileWriter("/home/nits/422P/final/final.txt");//<<<<<<<<<<<-------Path of the final output
	BufferedWriter bw2 = new BufferedWriter(fw2);
	for(int i=0;i<R.size();i++){
		point tmp = R.get(i);
		String px = Double.toString(tmp.x);
		String py = Double.toString(tmp.y);
		String pp = px+","+py;
		bw2.write(pp);
		bw2.newLine();
	}
	bw2.flush();
	bw2.close();
	fw2.close();
	long elapsedTimeMillis = System.currentTimeMillis()-start;
	System.out.println("*************Used Time = "+elapsedTimeMillis);
	FileWriter fw3 = new FileWriter("/home/nits/422P/final/finalS.txt");//<<<<<<<<<<<----------Path of the final list S
	BufferedWriter bw3 = new BufferedWriter(fw3);
	for(int i=0;i<S.size();i++){
		point tmp = S.get(i);
		String px = Double.toString(tmp.x);
		String py = Double.toString(tmp.y);
		String pp = px+","+py;
		bw3.write(pp);
		bw3.newLine();
	}
	bw3.flush();
	bw3.close();
	fw3.close();

}
}

	

/*public class MRS {
	
	static LinkedList<point> H =new LinkedList<point>();
	static LinkedList<point> S =new LinkedList<point>();
	static LinkedList<point> R =new LinkedList<point>();
	static double v;*/

	/*public class map1 extends Mapper<LongWritable,Text,IntWritable,Text> {
	public void map(LongWritable key,
	Text value,
	OutputCollector<IntWritable,Text> output,
	Reporter reporter) throws IOException {
	Matcher m = Pattern.compile("(\\d+(,)\\d+").matcher(value.toString());
	m.find(); 
	IntWritable grp = new IntWritable((int)(Math.random()*1024));
	Text points = new Text();
	while (m.find()) {
	points.set(m.group());
	output.collect(grp, points); 
	}
	}
	}*/
	
	/*public class reduce1 extends MapReduceBase implements
	Reducer<IntWritable,Text,IntWritable,Text> {
	public void reduce(IntWritable grp,
			Iterator<Text> points,
			OutputCollector<IntWritable,Text> output,
			Reporter reporter) throws IOException {
			Text pointList = new Text();
			//Vector userVector = new RandomAccessSparseVector(Integer.MAX_VALUE, 100); B
			while (points.hasNext()) {
			String point = points.next().toString();
			pointList.set(pointList.toString()+"\t"+point);
			}
			output.collect(grp, pointList);
			}
			}*/
	
	/*public static void main(String[] args) throws Exception {
		
		 Configuration conf = new Configuration();
		 //String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		 //if (otherArgs.length != 2) {
		   //System.err.println("Usage: wordcount <in> <out>");
		   //System.exit(2);
		 //}
		 Job job1 = new Job(conf, "mapper1");
		 job1.setJarByClass(MRS.class);
		 job1.setMapperClass(map1.class);
		 //job.setCombinerClass(GoGroup.class);
		 job1.setReducerClass(reduce1.class);
		 job1.setOutputKeyClass(IntWritable.class);
		 job1.setOutputValueClass(Text.class);
		 FileInputFormat.addInputPath(job1, new Path("/home/nits/422P/data"));
		 FileOutputFormat.setOutputPath(job1, new Path("/home/nits/422P/out"));
		 //System.exit(job1.waitForCompletion(true) ? 0 : 1);
		 job1.waitForCompletion(true);
		 	/*FileReader fr = new FileReader("/home/nits/422P/out/part-r-00000");
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
			
			System.out.println("v = "+v);*/
		//}


