package makedata.src.mdata;

public class point {
	public double x;
	public double y;
	
	public point(double xv, double yv){
		x = xv;
		y = yv;
	}
	public point(){
		
	}
	public boolean equal(point p){
		if(p.x==this.x&&p.y==this.y)
			return true;
		else
			return false;
	}
	public String toString(){
		return ""+x+","+y;
	}
}
