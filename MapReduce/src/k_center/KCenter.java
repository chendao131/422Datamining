package k_center;
import java.util.LinkedList;
import java.util.ArrayList;
import makedata.src.mdata.*;
import java.io.*;

public class KCenter {
	protected LinkedList<point> dataset;
	protected int[] cluster;
	protected double[] dist;
	protected int k;
    protected	int[] centers; 
	
	public KCenter(int newK, LinkedList<point> newDataset){
		k = newK;
		dataset = newDataset;
		cluster = new int[dataset.size()];
		dist = new double[dataset.size()];
		centers = new int[k];
	}
	public int[] getClusters(){
		// pick the first point in list as the first center
		for(int i=0;i<dataset.size();i++){
			cluster[i] = 0;
			centers[cluster[i]] = 0; //set the cluster center as 0 for all points
			dist[i] = length(centers[cluster[i]], i);
		}
		for(int i=1;i<k;i++){
			int currentCenter = maxDisIndex();
			centers[i] = currentCenter;
			for(int j=0;j<dataset.size();j++){
				double tempLen = length(centers[i], j);
				if(tempLen<=dist[j]){
					dist[j] = tempLen;
					cluster[j] = currentCenter;
				}
			}
		}
		return cluster;
	}
	protected int maxDisIndex(){
		double max = Double.MIN_VALUE;
		int maxIndex = -1;
		for(int i=0;i<dist.length;i++){
			if(!isCenter(i)){
				if(dist[i]>max){
					max = dist[i];
					maxIndex = i;
				}
			}
		}
		return maxIndex;
	}
	protected boolean isCenter(int i){
		boolean center = false;
		for(int j=0;j<centers.length;j++){
			if(i==centers[j])
				center = true;
		}
		return center;
	}
	
	protected double length(int centerIndex, int pointIndex){
		point center = dataset.get(centerIndex);
		point currentPoint = dataset.get(pointIndex);
		double temp1 = currentPoint.x-center.x;
		double temp2 = currentPoint.y-center.y;
		return Math.sqrt((temp1*temp1)+(temp2*temp2));
	}
	
	public static void main(String args[]) throws IOException{
		LinkedList<point> temp = getdata.getdataset();
		KCenter kc = new KCenter(2,temp);
		int[] cluster = kc.getClusters();
		for(int i=0;i<cluster.length;i++)
			System.out.println("point " + temp.get(i).x+","+temp.get(i).y + ": " + temp.get(cluster[i]).x+","+temp.get(cluster[i]).y);
	}
}
