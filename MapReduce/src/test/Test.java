package test;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.hadoop.io.Text;

import makedata.src.mdata.point;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		ArrayList<point> vi = new ArrayList<point>();
		ArrayList<point> c = new ArrayList<point>();
		ArrayList<point> unsampled = new ArrayList<point>();
		Scanner sc1 = new Scanner(new File("/Users/lishuai/422data/V.txt"));
		while(sc1.hasNextLine()){
			String temp = sc1.nextLine().toString();
//			System.out.println(""+key+" "+temp);
			String[] t = temp.split(",");
			vi.add(new point(Double.parseDouble(t[0]),Double.parseDouble(t[1])));
		}

		Scanner sc = new Scanner(new File("/Users/lishuai/422data/C.txt"));
		while(sc.hasNextLine()){
			String temp = sc.nextLine();
//			System.out.println(temp);
			String t[] = temp.split(",");
			c.add(new point(Double.parseDouble(t[0]), Double.parseDouble(t[1])));
		}
		Scanner sc0 = new Scanner(new File("/Users/lishuai/422data/unsampled.txt"));
		while(sc0.hasNextLine()){
			String temp = sc0.nextLine();
//			System.out.println(temp);
			String t[] = temp.split(",");
			unsampled.add(new point(Double.parseDouble(t[0]), Double.parseDouble(t[1])));
		}
		
	//	unsampled = getUnsampled(unsampled, c);
		int[] weight = new int[c.size()];
		int minIndex =0;
		double min = Double.MAX_VALUE;
		for(int i=0;i<unsampled.size();i++){
			for(int j=0;j<c.size();j++){
				Double tempDis = distance(unsampled.get(i), unsampled.get(j));
				if(tempDis<min){
					min = tempDis;
					minIndex = j;
				}
			}
			weight[minIndex]++;
			min = Double.MAX_VALUE;
			minIndex = 0;
		}
		for(int i=0;i<weight.length;i++)
			System.out.println(weight[i]);
		System.out.println(weight.length);
		System.out.println(formText(c, weight));
		
	
	}
	private static String formText(ArrayList<point> c, int[] weight){
		String temp = c.get(0).toString()+",,"+weight[0];
		for(int i=1;i<c.size();i++){
			temp =  temp+"\n" + c.get(i).toString()+",,"+weight[i];
		}
		return temp;
	}
	private static Double distance(point a, point b){
		Double temp1 = a.x-b.x;
		Double temp2 = a.y-b.y;
		return Math.sqrt((temp1*temp1)+(temp2*temp2));
	}

}
