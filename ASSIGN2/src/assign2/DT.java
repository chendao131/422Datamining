package assign2;
import java.io.*;
import java.util.*;
public class DT {
	public Vector<String> ReadData() throws Exception
	{ 
		FileReader fr = new FileReader("soybean.arff");
		BufferedReader br = new BufferedReader(fr);
		Vector<String> datarray = new Vector<String>();
		String s;
		while((s = br.readLine()) != null){
			if(s.compareTo("@DATA")==0)
			{
				s = br.readLine();
				while((s = br.readLine()) != null && s.charAt(0)!='%')
				{
					String temp="";
					
					for (int k = 0; k < s.length(); k++) 
					{
						if (s.charAt(k) != ' ') 
					    {
							temp += s.charAt(k);
					    }
					}	
					datarray.add(temp);	
				}
				break;
			}
		}
		return datarray;
	}
	
	public Vector<String> ReadAttribute() throws Exception
	{
		FileReader fr = new FileReader("soybean.arff");
		BufferedReader br = new BufferedReader(fr);
		Vector<String> attributearray = new Vector<String>();
		String s;
		while((s = br.readLine()) != null){
			if(s.compareTo("@RELATION soybean")==0)
			{
				s = br.readLine();
				for(int cout=0; cout<36; cout++)
				{
					s = br.readLine();
					attributearray.add(s);
				}
				break;
			}
		}
		return attributearray;
	}
	
	public static void main(String argv[]) throws Exception
	{
		DT dt= new DT();
		Tenfold tenfold = new Tenfold();
		Vector<String> datarray = new Vector<String>();
		Vector<String> attriarray = new Vector<String>();
		datarray = dt.ReadData();
		attriarray = dt.ReadAttribute();
		Scanner scan = new Scanner(System.in);
		String usrInput1,usrInput2;
		
		int inputRight=0;
/*		System.out.println("file directory?");
		String path = scan.next();
		datarray = dt.ReadData(path);
		while(datarray==null)
		{
			System.out.println("file can not find..");
			usrInput1 = scan.next();
			datarray = dt.ReadData(path);
		}
		attriarray = dt.ReadAttribute(path);*/
		
		System.out.println("infoGain or gini?: ");
		usrInput1 = scan.next();
		if(usrInput1.compareTo("infoGain")==0 || usrInput1.compareTo("gini")==0)
			inputRight=1;
		else
			System.out.println("input error!(infoGain or gini)");
		while(inputRight==0)
		{
			usrInput1 = scan.next();
			if(usrInput1.compareTo("infoGain")==0 || usrInput1.compareTo("gini")==0)
				inputRight=1;
			else
				System.out.println("input error!(infoGain or gini)");
		}
		
		inputRight=0;
		System.out.println("simple or real?: ");
		usrInput2 = scan.next();
		inputRight=0;
		if(usrInput2.compareTo("simple")==0 || usrInput2.compareTo("real")==0)
			inputRight=1;
		else
			System.out.println("input error!(simple or real)");
		while(inputRight==0)
		{
			usrInput2 = scan.next();
			if(usrInput2.compareTo("simple")==0 || usrInput2.compareTo("real")==0)
				inputRight=1;
			else
				System.out.println("input error!(simple or real)");
		}
			
			tenfold.start(attriarray,datarray,usrInput1,usrInput2);
	}
}
