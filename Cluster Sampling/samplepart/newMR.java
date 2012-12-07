package samplepart;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
//import org.apache.mahout.math.Vector;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.VLongWritable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

//import MRsample.point;

public class newMR {
	
	static LinkedList<point> H =new LinkedList<point>();
	static LinkedList<point> S =new LinkedList<point>();
	static LinkedList<point> R =new LinkedList<point>();
	static double v;

	public class map1 extends MapReduceBase implements
	Mapper<LongWritable,Text,IntWritable,Text> {
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
	}
	
	/*public class reduce1 extends MapReduceBase implements
	Reducer<IntWritable,Text,Text,VectorWritable> {
	public void reduce(IntWritable grp,
			Iterator<Text> points,
			OutputCollector<Text,Text> output,
			Reporter reporter) throws IOException {
			Vector userVector = new RandomAccessSparseVector(Integer.MAX_VALUE, 100); B
			while (itemPrefs.hasNext()) {A
			VLongWritable itemPref = itemPrefs.next();
			userVector.set(itemPref.get(), 1.0f); C
			}
			output.collect(userID, new VectorWritable(userVector));
			}
			}*/


}
