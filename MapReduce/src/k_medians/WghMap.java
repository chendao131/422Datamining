package k_medians;

public class WghMap {
	private int index;
	private int weight;
	
	public WghMap(int i, int w){
		index = i;
		weight = w;
	}
	public int weight(){
		return weight;
	}
	public int index(){
		return index;
	}
}
