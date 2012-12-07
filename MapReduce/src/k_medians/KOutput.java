package k_medians;
import makedata.src.mdata.*;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;
import java.io.*;
public class KOutput {
	
	public static void createMatFile(LinkedList<point> d, point[] cl, point[] ce, String path) throws IOException{
//		PrintWriter pw = new PrintWriter(new FileWriter("test.txt"));
//		pw.write("test");
		FileWriter ryt[] = new FileWriter[ce.length];
		BufferedWriter out[] = new BufferedWriter[ce.length];
		int counter[] = new int[ce.length];
		for(int i=0;i<ce.length;i++){		
			ryt[i]=new FileWriter(path+"cluster"+i+".txt");
			out[i]=new BufferedWriter(ryt[i]);
			for(int j=0;j<d.size();j++){
				if(ce[i].x==cl[j].x&&ce[i].y==cl[j].y){
					counter[i]++;
					Double x = d.get(j).x;
					Double y = d.get(j).y;
					String temp = ""+x.intValue()+","+y.intValue();
					out[i].write(temp+"\n");
				}
			}
			Double x = ce[i].x;
			Double y = ce[i].y;
			out[i].write(""+x.intValue()+","+y.intValue());
			out[i].close();
			System.out.println(counter[i]);
		} /*		FileWriter ryt = new FileWriter("fuck.txt"); BufferedWriter out = new BufferedWriter(ryt);
		out.write("fuck");
		out.close();*/
	}
	public static void createFile(LinkedList<point> dataset, String path) throws IOException{
		FileWriter fyt = new FileWriter(path);
		BufferedWriter out = new BufferedWriter(fyt);
		for(int i=0;i<dataset.size();i++){
			Double x= dataset.get(i).x;
			Double y = dataset.get(i).y;
			String temp = ""+x.intValue()+","+y.intValue();
			out.write(temp+"\n");
		}
		out.close();
		System.out.println("File created!");
	}
	public static void main(String args[]) throws IOException{
		//LinkedList<point> temp = getdata.getdataset();
		//sample ss = new sample();
		//LinkedList<point> output = ss.sampling(temp, 3, 0.35);
    	/*int weight[] = new int[20000];
    	for(int i=0; i<20000; i++){
    		//Double t = Math.random()*temp.size();
    		weight[i] =1;
    	}
    	
		KMediansLloyd kld0 = new KMediansLloyd(3,temp, weight);
		KMediansLloyd kld1 = new KMediansLloyd(3,output, weight);
		point[] cl = kld0.getClusters();
		point[] ce = kld0.getCenters();
		KOutput.createMatFile(temp, cl, ce,"/Users/lishuai/Documents/workspace/MapReduce/data/");
		cl = kld1.getClusters();
		ce = kld1.getCenters();
		KOutput.createMatFile(output, cl, ce,"/Users/lishuai/Documents/workspace/MapReduce/dataS/");
		for(int i=0;i<ce.length;i++)
			System.out.println(""+ce[i].x+","+ce[i].y);
		*/
		//KOutput.createFile(temp, "/Users/lishuai/V.txt");
		//KOutput.createFile(output, "/Users/lishuai/C.txt");
		//LinkedList<point> c = new LinkedList<point>();
		/*LinkedList<point> v = temp;
		LinkedList<point> c = output;
		KOutput.createFile(v, "/Users/lishuai/V.txt");
		KOutput.createFile(c, "/Users/lishuai/C.txt");
		LinkedList<point> unsampled = new LinkedList<point>(v);
//		Scanner sc = new Scanner(new File("/Users/lishuai/C.txt"));
//		while(sc.hasNextLine()){
//			String t[] = sc.nextLine().split(",");
//			c.add(new point(Double.parseDouble(t[0]), Double.parseDouble(t[1])));
//		}
		
	//	Scanner sc0 = new Scanner(new File("/Users/lishuai/V.txt"));
	//	while(sc0.hasNextLine()){
	//		String temp = sc0.nextLine();
		//	System.out.println(temp);
	//		String t[] = temp.split(",");
	//		v.add(new point(Double.parseDouble(t[0]), Double.parseDouble(t[1])));
	//	}
		Iterator<point>itr = unsampled.iterator();
		System.out.println(unsampled.size());
		while(itr.hasNext()){
			point tempPoint = itr.next();
			for(int i=0;i<v.size();i++){
				System.out.println(tempPoint.x+","+tempPoint.y);
				if(tempPoint.equal(v.get(i)))
					itr.remove();
			}
		}
		KOutput.createFile(unsampled, "Users/lishuai/unsampled.txt");
		System.out.println(v.size());
		System.out.println(c.size());
		System.out.println(unsampled.size());
		*/
		long start = System.currentTimeMillis();
    	LinkedList<point> temp = getdata.getdataset();
    	ArrayList<Integer> weight = new ArrayList<Integer>();
    	for(int i=0; i<500; i++){
    		//Double t = Math.random()*temp.size();
    		weight.add(1); 
    	}
		KMediansLloyd kld = new KMediansLloyd(3,temp, weight,0.0,20);
		KOutput.createMatFile(temp,kld.getClusters(),kld.getCenters(),"/Users/lishuai/testKM/");
		System.out.println("Time:" + (System.currentTimeMillis()-start));
	}
}
