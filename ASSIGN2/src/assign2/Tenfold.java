package assign2;

import java.util.*;

public class Tenfold {
	public int brown=0;
	public int right=0;
	public int wrong=0;
	public int unknown=0;
	public int classified[][]= new int [19][19];
	public void start(Vector<String>  attriarray, Vector<String> datarray ,String usrInput1, String usrInput2)
	{
		int range = (int) (datarray.size()/10);
		int start=0,end=71;
		this.training(datarray, start, end,attriarray, usrInput1, usrInput2);
//		System.out.println(range);
		start=71;
		for(int i=0;i<9;i++)
		{
			end=start+range;
			this.training(datarray, start, end, attriarray, usrInput1, usrInput2);
			start=start+range;
		}
//		end=datarray.size();
//		this.training(datarray, start, end, attriarray, usrInput1, usrInput2);
		printMatrix(classified,attriarray, usrInput1, usrInput2);
		System.out.println("TOTAL:");
		System.out.println("right:\t\t"+this.right);	
		System.out.println("wrong:\t\t"+this.wrong);	
		System.out.println("decision:\t"+(this.right+this.wrong)+"/"+datarray.size());
		System.out.println("accuracy:\t"+(double)this.right/(double)(this.right+this.wrong));
	}
	
	public void training(Vector<String> datarray, int start, int end, Vector<String>  attriarray , String usrInput1, String usrInput2)
	{
		System.out.println("ten-fold_"+(start/68+1)+"..");
		Vector<String> training = new Vector<String>();
		for(int i=0; i<datarray.size(); i++)  //calculate average
		{
			if(i<start || i>=end)
			{
				training.add(datarray.get(i));
			}	
		}
		InformationGain infoGain = new InformationGain();
		infoGain.start(attriarray, training , usrInput1, usrInput2);
		this.test(datarray, start, end, attriarray, infoGain);
	}
	
	public void test(Vector<String> datarray, int start, int end, Vector<String>  attriarray, InformationGain infoGain)
	{
//		infoGain.printTree();
		for(int i=start; i<end; i++)  //calculate average
		{
			String temp1=datarray.get(i);
			String temp2="";
			
			for (int k = 0; k < temp1.length(); k++) 
			{
				if (temp1.charAt(k) != ' ') 
			    {
					temp2 += temp1.charAt(k);
			    }
			}
			String currentData[]= temp2.split(",");
			if(currentData[35].compareTo("brown-spot")==0)
				brown++;
			String prediction;
			prediction=infoGain.getPrediction(datarray.get(i));
			if(prediction==null)
				unknown++;
			else if(prediction.compareTo(currentData[35])==0)
			{
				int flag=0;
				flag= infoGain.getClassLocation(currentData[35]);
				classified[flag][flag]++;
				right++;
			}
			else
			{
				int real=0,pred=0;
				real = infoGain.getClassLocation(currentData[35]);
				pred = infoGain.getClassLocation(prediction);
				classified[pred][real]++;
				wrong++;
			}
		}
	}
	public void printMatrix(int matrix[][], Vector<String>  attriarray, String usrInput1, String usrInput2)
	{
		int lastArray= attriarray.size()-1;
		String temp=attriarray.get(lastArray).substring(20, 363);
		String classes[]= temp.split(",");
		
		if(usrInput1.compareTo("infoGain")==0)
		{
			if(usrInput2.compareTo("simple")==0)
				System.out.println("InformationGain__simple majority:");
			else
				System.out.println("InformationGain__real majority:");
		}
		else
		{
			if(usrInput2.compareTo("simple")==0)
				System.out.println("GINI__simple majority:");
			else
				System.out.println("GINI__real majority:");
		}	
		
		System.out.println("=== Confusion Matrix ===");
		char classified='a';
		for(int i=0; i<matrix.length; i++)
		{
			System.out.print(classified++);
			if(i!=matrix.length-1)
				System.out.print("  ");
		}
		System.out.println("  <-- classified as");
		char original='a';
		for(int i=0; i<matrix.length; i++)
		{
			for(int j=0; j<matrix.length; j++)
			{
				
				System.out.print(matrix[i][j]);
				if(j!=matrix.length-1)
				{
					if(matrix[i][j]<10)
						System.out.print("  ");
					else
						System.out.print(" ");
				}
					
			}
			System.out.print("| "+(original++)+"="+ classes[i]);
			System.out.println();
		}
		
		int brownSum=0;
		for(int i=0; i<matrix.length; i++)
		{
			brownSum=brownSum+matrix[i][7];
		}
		System.out.println("BROWN-SPOT:");
		System.out.println("right:\t\t"+matrix[7][7]);
		System.out.println("wrong:\t\t"+(brownSum-matrix[7][7]));
		System.out.println("decision:\t"+brownSum+"/"+brown);
		System.out.println("accuracy:\t"+(double)matrix[7][7]/(double)brownSum);
		
	}

}
