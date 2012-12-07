package test;

public class TestArraySize {
	public static Double[][] array(){
		Double[][] temp = new Double[5000][6000];
		return temp;
		
			Integer[] weight = new Integer[c.size()];
			int minIndex =0;
			double min = Double.MAX_VALUE;
			for(int i=0;i<unsampled.size();i++){
				for(int j=0;j<c.size();j++){
					Double tempDis = distance(unsampled.get(i), unsampled.get(j))
					if(tempDis<min){
						min = tempDis;
						minIndex = j;
					}
				}
				weight[j]++;
				min = Double.MAX_VALUE;
				minIndex = 0;
			}
					
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
			}
	}
	public static void main(String args[]){
		array();
	}
}
