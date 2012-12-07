package k_medians;
import k_center.KCenter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.*;

import makedata.src.mdata.getdata;
import makedata.src.mdata.point;

public class KMediansLloyd{
	private ArrayList<Integer> weight;
	private LinkedList<point> dataset;
	private point[] cluster;
	private point[] centers;
	private int k;
	private double[] dist;
	private int[] clusterCate;
	private boolean clustered;
	private double threshold;
	private int runTimes;
	
	private class CompareW implements Comparator<WghMap>{
		public int compare(WghMap a, WghMap b){
			if(a.weight()>b.weight())
				return 1;
			else if(a.weight()<b.weight())
				return -1;
			else 
				return 0;
		}
		public boolean equals(Object c){
			if(c instanceof CompareW)
				return true;
			else
				return false;
		}
	}
	private class CompareX implements Comparator<WghMap>{
		public int compare(WghMap a, WghMap b) {
			if(dataset.get(a.index()).x*a.weight()>dataset.get(b.index()).x*b.weight())
				return 1;
			else if(dataset.get(a.index()).x*a.weight()<dataset.get(b.index()).x*b.weight())
				return -1;
			else 
				return 0;
		}
		public boolean equals(Object c){
			if(c instanceof CompareW)
				return true;
			else
				return false;
		}	
	}
	private class CompareY implements Comparator<WghMap>{
		public int compare(WghMap a, WghMap b) {
			if(dataset.get(a.index()).y*a.weight()>dataset.get(b.index()).y*b.weight())
				return 1;
			else if(dataset.get(a.index()).y*a.weight()<dataset.get(b.index()).y*b.weight())
				return -1;
			else 
				return 0;
		}
		public boolean equals(Object c){
			if(c instanceof CompareY)
				return true;
			else
				return false;
		}	
	}
	
    public KMediansLloyd( int newK, 
    					  LinkedList<point> newSet,
    					  ArrayList<Integer> newWeight, 
    					  double newThreshold,
    					  int newRunTimes)
    {
    	dataset = newSet;
    	k = newK;
    	cluster = new point[dataset.size()];
    	clusterCate = new int[dataset.size()];
    	centers = new point[k];
    	dist = new double[dataset.size()];
    	weight = newWeight;
    	clustered = false;
    	threshold = newThreshold;
    	runTimes = newRunTimes;
    }
    public point[] getClusters(){
    	boolean notDone = false;
   		pickKCenters();
    	do{
    		clustering();
    		notDone = reCenter();
    		runTimes--;
    		if(runTimes==0)
    			notDone = false;
    	}while(notDone);
    	clustered = true;
    	return cluster;
    }
    public point[] getCenters(){
    	if(clustered)
    		return centers;
    	else
    		return null;
    }
    private void pickKCenters(){
    	for(int i=0;i<k;i++){
    		Double temp = Math.random()*dataset.size();
    	//	System.out.println(temp);
   			centers[i] = dataset.get(temp.intValue());
    	//	System.out.println(centers[i]);
    	}
    }
    private boolean clustering(){
    	boolean notDone = false;
    	for(int i=0; i<dataset.size(); i++){
    		int minIndex = 0;
    		double minDist = Double.MAX_VALUE;
    		for(int j=0; j<k; j++){
    			double tempDist = length(centers[j],dataset.get(i));
    			//System.out.println(tempDist + " "+i+", "+centers[j]);
    			if(minDist>tempDist){
    				minDist = tempDist;
    				minIndex = j;
    			}
    		}
    		dist[i] = minDist;
    		cluster[i] = centers[minIndex];
    		//if(minIndex!=clusterCate[i])
    		//	notDone = true;
    		clusterCate[i] = minIndex;
//    		System.out.println();
    	}
    	return notDone;
    }
    private boolean reCenter(){
    	boolean b = true;
    	//boolean tempb = true;
    	boolean flag = true;
    	double tempX;
    	double tempY;
    	for(int i=0; i<k; i++){
    		ArrayList<WghMap> tempCluster = new ArrayList<WghMap>(dataset.size());
    		for(int j=0; j<dataset.size(); j++){
    			if(cluster[j] == centers[i]){
    				tempCluster.add(new WghMap(j, weight.get(j)));
    //				System.out.println(wm.index()+", " + wm.weight());
    			}
    		}
    		if(tempCluster.size()>2){
    			Collections.sort(tempCluster, new CompareX());
    			tempX = dataset.get(tempCluster.get(tempCluster.size()/2+1).index()).x;
    			Collections.sort(tempCluster, new CompareY());
    			tempY= dataset.get(tempCluster.get(tempCluster.size()/2+1).index()).y;
    		}
    		else{
    			tempX = dataset.get(tempCluster.get(0).index()).x;
    			tempY = dataset.get(tempCluster.get(0).index()).y;
    		}
    		System.out.println("old: "+ centers[i].x+","+centers[i].y);
    		//System.out.println( Math.abs(centers[i].x-tempX));
    		//System.out.println( Math.abs(centers[i].y-tempY));
    		//tempb = centers[i].x==tempX&&centers[i].y==tempY;
//    		flag = centers[i].x==tempX&&centers[i].y==tempY;
 //   		b= flag&&b;
    		flag = Math.abs(centers[i].x-tempX)<=threshold&&Math.abs(centers[i].y-tempY)<=threshold;
    		b= flag&&b;
   			centers[i] = new point(tempX, tempY);
    		System.out.println("new: "+ centers[i].x+","+centers[i].y);
    	}
    	System.out.println();
    	return !b;
    }
    private double length(point center, point p){
    	double temp1 = p.x-center.x;
		double temp2 = p.y-center.y;
		return Math.sqrt((temp1*temp1)+(temp2*temp2));
    }
    public static void main(String args[]) throws IOException{
    	long start = System.currentTimeMillis();
    	LinkedList<point> temp = getdata.getdataset();
    	ArrayList<Integer> weight = new ArrayList<Integer>();
    	for(int i=0; i<500; i++){
    		//Double t = Math.random()*temp.size();
    		weight.add(1); 
    	}
		KMediansLloyd kld = new KMediansLloyd(3,temp, weight,0.0,20);
		kld.getClusters();
		System.out.println("Time:" + (System.currentTimeMillis()-start));
	//	kld.pickKCenters();
	//	kld.clustering();
		//for(int i=0; i<temp.size(); i++)
	//		System.out.println(kld.cluster[i]);
	//	System.out.println();
	//	for(int i=0; i<2; i++)
	//		System.out.println(kld.centers[i]);
	//	for(int i=0;i<kld.cluster.length;i++)
	//		System.out.println("point"+i +" " + temp.get(i).x+","+temp.get(i).y + ": " + (kld.cluster[i]).x+","+kld.cluster[i].y);
    }
}
