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

public class MapReduce_KMedian_phase1{
	private final static int E = 4;
	private final int SIZE = 20000;
	public static class MapPhase1 extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException{
			Double tempD = Math.random()*(double)E;
			Integer k = tempD.intValue();
			Text t = new Text(k.toString());
			output.collect(t, value);
		}
	}
	public static class ReducePhase1 extends MapReduceBase implements Reducer<Text, Text, Text, Text>{
		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException{
			//System.out.println("?");
			System.out.println(key.toString());
			ArrayList<point> vi = new ArrayList<point>();
			ArrayList<point> c = new ArrayList<point>();
			ArrayList<point> unsampled = new ArrayList<point>();
			while(values.hasNext()){
				String temp = values.next().toString();
//				System.out.println(""+key+" "+temp);
				String[] t = temp.split(",");
				vi.add(new point(Double.parseDouble(t[0]),Double.parseDouble(t[1])));
			}
	
			Scanner sc = new Scanner(new File("/Users/lishuai/422data/C.txt"));
			while(sc.hasNextLine()){
				String temp = sc.nextLine();
//				System.out.println(temp);
				String t[] = temp.split(",");
				c.add(new point(Double.parseDouble(t[0]), Double.parseDouble(t[1])));
			}
			Scanner sc0 = new Scanner(new File("/Users/lishuai/422data/unsampled.txt"));
			while(sc0.hasNextLine()){
				String temp = sc0.nextLine();
//				System.out.println(temp);
				String t[] = temp.split(",");
				unsampled.add(new point(Double.parseDouble(t[0]), Double.parseDouble(t[1])));
			}
			
		//	unsampled = getUnsampled(unsampled, c);
			int[] weight = new int[c.size()];
			int minIndex =0;
			double min = Double.MAX_VALUE;
			for(int i=0;i<unsampled.size();i++){
				for(int j=0;j<c.size();j++){
					Double tempDis = distance(unsampled.get(i), c.get(j));
					if(tempDis<min){
						min = tempDis;
						minIndex = j;
					}
				}
			//	System.out.println(minIndex);
				weight[minIndex]++;
				min = Double.MAX_VALUE;
				minIndex = 0;
			}
			/*Double[][] dis = new Double[unsampled.size()][c.size()];
			Integer[] weight = new Integer[c.size()];
			for(int i=0;i<unsampled.size();i++)
				for(int j=0;j<c.size();j++)
					dis[i][j] = distance(unsampled.get(i),c.get(j));
			int minIndex = 0;
			double min = Double.MAX_VALUE;
			for(int i=0;i<unsampled.size();i++){
				for(int j=0;j<c.size();j++){
					if(dis[i][j]<min){
						min = dis[i][j];
						minIndex = j;
					}
				}
				weight[minIndex]++;
				minIndex = 0;
			}*/
			Text forValue = new Text();
			Text temp = formText(c, weight);
			System.out.println("test");
			output.collect(temp, forValue);
		}
		
		private Text formText(ArrayList<point> c, int[] weight){
			String temp = c.get(0).toString()+",,"+weight[0];
			for(int i=1;i<c.size();i++)
				temp = temp+"\n"+ c.get(i).toString()+",,"+weight[i];
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
	      String input = "/Users/lishuai/input";
	      String output = "/Users/lishuai/phase1";
	      JobConf conf = new JobConf(MapReduce_KMedian_phase1.class);
	      conf.setJobName("MR_KMedian");
	
	      conf.setOutputKeyClass(Text.class);
	      conf.setOutputValueClass(Text.class);
	
	      conf.setMapperClass(MapReduce_KMedian_phase1.MapPhase1.class);
	      //conf.setCombinerClass(Reduce.class);
	      conf.setReducerClass(MapReduce_KMedian_phase1.ReducePhase1.class);
	
	      conf.setInputFormat(TextInputFormat.class);
	      conf.setOutputFormat(TextOutputFormat.class);
	
	      FileInputFormat.setInputPaths(conf, new Path(input));
	      FileOutputFormat.setOutputPath(conf, new Path(output));
	
	      JobClient.runJob(conf);
	    }
}
