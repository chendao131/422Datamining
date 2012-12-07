package samplepart;

import java.io.*;



public class datatxt {
	public static void main(String[] args) throws IOException{
		int size = 20000;
		FileWriter fw = new FileWriter("/home/nits/422P/demo/data.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		for(int i=0;i<size;i++){
		getdata a = new getdata();
		point tmp = getdata.getpoint();
		String px = Double.toString(tmp.x);
		String py = Double.toString(tmp.y);
		String pp = px+","+py;
		//System.out.println(p);
		
		bw.write(pp);
		bw.newLine();
		}

		bw.flush();
		bw.close();
		fw.close();
		
	
	}
}
	
