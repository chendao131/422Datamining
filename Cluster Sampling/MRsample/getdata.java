package MRsample;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;



public class getdata {
	public static point getpoint(){
		point tmp = new point();
		tmp.x = (int)(Math.random()*10000);
		tmp.y = (int)(Math.random()*10000);
		return tmp;
	}
	
	public static LinkedList<point> getdataset() throws IOException{
		int size = 10000;													//<<<<<<<<<----------Here to change the size of dataset
		LinkedList<point>dataset = new LinkedList<point>();
		FileWriter fw = new FileWriter("/home/nits/422P/data/data.txt");    //<<<<<<<<<----------Here to change the path of the general dataset
		BufferedWriter bw = new BufferedWriter(fw);
		for(int i=0;i<size;i++){
			point tmp = getpoint();
			dataset.add(tmp);
			String px = Double.toString(tmp.x);
			String py = Double.toString(tmp.y);
			String pp = px+","+py;
			bw.write(pp);
			bw.newLine();
		}
		bw.flush();
		bw.close();
		return dataset;
	}
	

	public static void main(String[] args) throws Exception{
		LinkedList<point>dataset = getdataset();
		/*for(int j=0;j<dataset.size();j++){
			System.out.println(dataset.get(j).x + " , " + dataset.get(j).y );
		}
		point a = new point(),b = new point();
		LinkedList<Double> test = new LinkedList<Double>();
		sample ss = new sample();
		LinkedList<point>output = ss.sampling(dataset, 3, 0.35);
		FileWriter fw = new FileWriter("/home/nits/422P/data/data.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		for(int i = 0;i<output.size();i++){
			point tmp = output.get(i);
		String px = Double.toString(tmp.x);
		String py = Double.toString(tmp.y);
		String pp = px+","+py;
		bw.write(pp);
		bw.newLine();
		}
		bw.flush();
		bw.close();*/
		//for(int k=0;k<dataset.size();k++){
		//	test.add(dataset.get(k).x);
		//}
		//LinkedList<point>test1 = getdataset();
		//sample ss = new sample();
		//System.out.println(ss.select(dataset, test1));
		
		
		/*a.x = 3;
		a.y = 4;
		b.x = 0;
		b.y = 0;
		sample ss = new sample();
		double d = ss.distance(a,b);
		System.out.println(d);
		dataset.add(2,a);*/
		/*for(int j=0;j<dataset.size();j++){
			System.out.println("("+ dataset.get(j).x + " , " + dataset.get(j).y +")");
		}*/
		
		//dataset.remove(1);
		//{for(int k=0;k<dataset.size();k++){
			//System.out.println("--("+ dataset.get(k).x + " , " + dataset.get(k).y +")");
		//}}
		
	}

}
