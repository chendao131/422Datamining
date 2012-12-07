package MRsample;

import java.util.LinkedList;

//import MRsample.point;

public class sample {
	
	public static double distance (point a,point b){
		double d = Math.hypot((a.x-b.x),(a.y-b.y) );
		return d;
		
	}
	
	public static double minDis(point H,LinkedList<point>S){
		double temp = 0;
		double counter = 1000000;
		for (int i = 0;i < S.size();i++){
			temp = distance(H, S.get(i));
			if(counter > temp){
				counter = temp;
			}
		}
		//System.out.println("working~~~");
		return counter;
	}
	
	public static double findV(LinkedList<Double> dis, int idx){
		if(idx > dis.size()){
			System.out.println("error in findV!");
		}
		LinkedList<Double> forSort = new LinkedList<Double>();
		forSort.add(1000000.0);
		for (int i=0;i<dis.size();i++){
			int j = 0;
			while(forSort.get(j)<dis.get(i)){
				j++;
			}
			forSort.add(j,dis.get(i));
		
		}
		forSort.removeLast();
		//System.out.println("working~~~");
		/*for(int k=0;k<forSort.size();k++){
		System.out.println(forSort.get(k));
		}*/
		return forSort.get(idx-1);
		
	}
	
	public static double select(LinkedList<point> H,LinkedList<point> S){
		LinkedList<Double> dis = new LinkedList<Double>();
		for(int i = 0;i < H.size();i++){
			dis.add(minDis(H.get(i), S));
		}
		int idx = (int)(8*Math.log((double)H.size()));
		System.out.println(idx);
		return findV(dis, idx);
		
	}
	
	public static LinkedList<point> sampling(LinkedList<point> V,double k,double e){
		LinkedList<point> S =new LinkedList<point>();
		LinkedList<point> R =new LinkedList<point>();
		LinkedList<point> H =new LinkedList<point>();
		R = V;
		
		System.out.println("rsize = "+R.size());
		
		double ifcon = (4/e)*k*Math.pow(R.size(), e)*Math.log(R.size());
		while(R.size()>ifcon){
		double ifS = (9*k*Math.pow(R.size(), e))/R.size();
		double ifH = (4*Math.pow(R.size(),e))/R.size();
		System.out.println("ifs = "+ifS);
		System.out.println("ifh = "+ifH);
		System.out.println("ifcon = "+ifcon);
		H.clear();
		
			//System.out.println("in while");
			for (int i=0;i<R.size();i++){
				if (Math.random()<ifS){
					//System.out.println("adding to S");
					S.add(R.get(i));
				}
				if (Math.random()<ifH){
					H.add(R.get(i));
				}
			}
			System.out.println("Ssize = "+S.size());
			System.out.println("Hsize = "+H.size());
			double v = select(H, S);
			System.out.println("v = "+v);
			int counter = 0;
			for(int j=0;j<R.size();j++){
				if(minDis(R.get(j), S)<v){
					R.remove(j);
					j--;
					
				}
				counter++;
			}
			System.out.println("R.size = "+R.size());
			System.out.println("counter = "+counter);
			ifcon = (4/e)*k*Math.pow(R.size(), e)*Math.log(R.size());
		}
		LinkedList<point> out = new LinkedList<point>();
		out.addAll(R);
		out.addAll(S);
		System.out.println("final R.size = "+R.size());
		System.out.println("final S.size = "+S.size());
		System.out.println("output size = "+out.size());
		return out;
	}

}
