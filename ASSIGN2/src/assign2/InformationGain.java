package assign2;
import java.util.Vector;
public class InformationGain {
	public String classes[];
	public String attributes[][] = new String[36][];  //classifications of attributes
	public String attributeNames[] = new String[36];  // names of attributes
	public Node root = new Node("Desition Tree");
	public void start(Vector<String>  attriarray, Vector<String> datarray, String usrInput1, String usrInput2)
	{
		//get attribute names
		//get classes
		String temp1=attriarray.get(35).substring(20, 363);
		String temp2="";
		for (int i = 0; i < temp1.length(); i ++) 
		{
			if (temp1.charAt(i) != ' ') 
		    {
				temp2 += temp1.charAt(i);
		    }
		}
		classes=temp2.split(",");
		//get attributes
		temp1=""; temp2="";
		for(int i=0; i<attriarray.size()-1; i++)
		{
			temp1=attriarray.get(i);
			temp2="";
			for (int k = 0; k < temp1.length(); k++) 
			{
				if (temp1.charAt(k) == '{') 
			    {
					k++;
					while(temp1.charAt(k) != '}')
					{
						if(temp1.charAt(k)==' ')
							k++;
							temp2 += temp1.charAt(k);
							k++;
					}
			    }
				
			}
			temp2+=",?";
			attributes[i]=temp2.split(",");
		}
		
		//get attribute names
		for(int i=0; i<attriarray.size()-1; i++)
		{
			temp1=attriarray.get(i);
			temp2="";
			String temp3[]=temp1.split("\t");
			String temp4[]=temp3[0].split(" ");
			attributeNames[i]=temp4[1];
		}
		
		int already[] = new int [35]; //already record remaining attributes
		for(int i=0; i<35; i++)
		{
			already[i]=0;
		}
		//begin build tree
		this.recursion(attriarray, datarray, already, root , -1 , usrInput1, usrInput2, null);
	}
	
	public int recursion(Vector<String> attriarray, Vector<String> datarray, int already[], Node currentNode, 
			int currentBit , String usrInput1, String usrInput2, String most)
	{
//******************************************************
		// whether stop building? and then make prediction
		// instance<20  #1
		if(datarray.size()<20)
		{
			String prediction;
			prediction=this.makeSimplePrediciton(datarray,currentNode,1, currentBit ,usrInput1, usrInput2);
			//System.out.println(": "+prediction);
			if(prediction==null)
				prediction=most;
			Node leaf= new Node("leaf",prediction);
			leaf.setDepth(currentNode);
			currentNode.buildTree(leaf);
			return 1;
		}
		// no attribute left  #2
		int left=0;
		for(int i=0; i<35; i++)
		{
			if(already[i]==0)
				left++;
		}
		if(left==0)
		{
			String prediction;
			prediction=this.makeSimplePrediciton(datarray,currentNode, 2 ,currentBit, usrInput1, usrInput2);
			//System.out.println(": "+prediction);
			Node leaf= new Node("leaf",prediction);
			leaf.setDepth(currentNode);
			currentNode.buildTree(leaf);
			return 1;
		}
		
		// all the classes are the same  #3
		left=0;
		for(int i=0; i<datarray.size()-1; i++)
		{
			for(int j=i+1; j<datarray.size(); j++)
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
				String currentData1[]=temp2.split(",");
				
				temp1=datarray.get(j);
				temp2="";
				
				for (int k = 0; k < temp1.length(); k++) 
				{
					if (temp1.charAt(k) != ' ') 
				    {
						temp2 += temp1.charAt(k);
				    }
				}
				String currentData2[]=temp2.split(",");
//				if(datarray.size()==2)
				if(currentData1[35].compareTo(currentData2[35])!=0)
				{
					left = 1;
					break;
				}
			}
			if(left ==1)
				break;
		}
		if(left==0)
		{
			String prediction;
			prediction=this.makeSimplePrediciton(datarray,currentNode,3 , currentBit , usrInput1, usrInput2);
			//System.out.println(": "+prediction);
			Node leaf= new Node("leaf",prediction);
			leaf.setDepth(currentNode);
			currentNode.buildTree(leaf);
			return 1;
		}
		
		double info1=0;
		int doneBit=0;
		info1 = this.info_0(attriarray, datarray);  //all entropy
		
		if(usrInput1.compareTo("infoGain")==0)
			doneBit = this.info_attribute(attriarray, datarray, already);  //get flag
		else
			doneBit = this.GINIsplit(attriarray, datarray, already);
		
		if(doneBit==-1)
		{
			String prediction;
			prediction=this.makeSimplePrediciton(datarray,currentNode,2 , currentBit , usrInput1, usrInput2);
			System.out.println("44leaf:   "+prediction);
			Node leaf= new Node("leaf",prediction);
			currentNode.buildTree(leaf);
			return 1;		
		}
		
		if(datarray.size()==2)
		{
			String temp1=datarray.get(0);
			String temp2="";
			for(int k = 0; k <temp1.length(); k++) 
			{
				if (temp1.charAt(k) != ' ') 
			    {
					temp2 += temp1.charAt(k);
			    }
			}
			String currentData1[]=temp2.split(",");
			
			temp1=null; temp2=null;
			temp1=datarray.get(1);
			for(int k = 0; k <temp1.length(); k++) 
			{
				if (temp1.charAt(k) != ' ') 
			    {
					temp2 += temp1.charAt(k);
			    }
			}
			String currentData2[]=temp2.split(",");
			if(currentData1[doneBit].compareTo(currentData2[doneBit])==0)
			{	
				String prediction;
				prediction=this.makeSimplePrediciton(datarray,currentNode,4 ,currentBit, usrInput1, usrInput2);
				//prediction = currentData1[doneBit];
				//System.out.print(": "+prediction);
				Node leaf= new Node("leaf",prediction);
				leaf.setDepth(currentNode);
				currentNode.buildTree(leaf);
				//System.out.println();
				return 1;
			}

		}
		//System.out.println();
		int already_in[] = already;
		already_in[doneBit] =1;
		for(int i=0; i<attributes[doneBit].length; i++)
		{
			
			currentNode.setAttribute(attributeNames[doneBit]);
			Node temp = new Node("node",
					doneBit,attributes[doneBit][i], this.getNewArray(datarray, doneBit, i));
			temp.setDepth(currentNode);
			currentNode.buildTree(temp);
			
// print all training tree
/*			for(int p=0; p<currentNode.getDepth(); p++)
				System.out.print("|"); */
		//	System.out.print(currentNode.getType()+"   "+currentNode.getAttribute()	+" = "+ temp.getClassification()/*+":"+this.getNewArray(datarray, doneBit, i).size()*/);
			//String getMost=this.getMostClass(datarray);
			this.recursion(attriarray, this.getNewArray(datarray, doneBit, i), already, temp , doneBit , usrInput1, usrInput2, null);
		}
		already[doneBit]=0;
		return 1;
	}
	
	public String makeSimplePrediciton(Vector<String> datarray, Node currentNode, int type , int currentBit, String usrInput1, String usrInput2)
	{
		Vector<String> Newdatarray=this.deleteSpace(datarray);
		if(usrInput2.compareTo("simple")==0)  //simple majority
		{
			if(type==1)  // few instances
			{
				int flag=0;
				flag=currentBit;
				int classNum[] = new int [19];
				for(int i=0; i<19; i++)
					classNum[i]=0;
				
				for(int i=0; i<Newdatarray.size(); i++)
				{
					String currentData[]= Newdatarray.get(i).split(",");
					for(int j=0; j<classes.length; j++)
					{
						if(currentData[35].compareTo(classes[j])==0)
						{
							classNum[j]++;
						}
					}
				}
				int max=0;
				flag=-1;
				for(int i=0; i<19; i++)
				{
					if(classNum[i]>max)
					{
						max=classNum[i];
						flag=i;
					}
				}
				if(flag==-1)
					return null;
				else
					return classes[flag];
			}
			if(type==2)  //ran out attributes
			{
				int classNum[] = new int [19];
				for(int i=0; i<19; i++)
					classNum[i]=0;
				for (int i = 0; i < datarray.size(); i++) 
				{
					for(int j=0; j<classes.length; j++)
					{
						classNum[j]++;
					}
				}
				int max=0;
				int flag=-1;
				for(int i=0; i<19; i++)
				{
					if(classNum[i]>max)
					{
						max=classNum[i];
						flag=i;
					}
				}
				if(flag==-1)
					return null;
				else
					return classes[flag];
			}
		}
		else  // real majority 
		{
			if(type==1)  // few instances
			{
				int flag=0;
				flag=currentBit;
				int classNum[] = new int [19];
				for(int i=0; i<19; i++)
					classNum[i]=0;
				
				for(int i=0; i<Newdatarray.size(); i++)
				{
					String currentData[]= Newdatarray.get(i).split(",");
					for(int j=0; j<classes.length; j++)
					{
						if(currentData[35].compareTo(classes[j])==0)
						{
							classNum[j]++;
						}
					}
				}
				int max=0,sum=0;
				flag=0;
				for(int i=0; i<19; i++)
				{
					sum=sum+classNum[i];
					if(classNum[i]>max)
					{
						max=classNum[i];
						flag=i;
					}
				}
				if(max==0 || sum==0)
					return null;
				if((double)max/(double)sum>(double)2/3)
					return classes[flag];
				else
					return null;
			}
			if(type==2)  //ran out attributes
			{
				int classNum[] = new int [19];
				for(int i=0; i<19; i++)
					classNum[i]=0;
				for (int i = 0; i < Newdatarray.size(); i++) 
				{
					String currentData[]= Newdatarray.get(i).split(",");
					for(int j=0; j<classes.length; j++)
					{
						if(currentData[35].compareTo(classes[j])==0)
						{
							classNum[j]++;
						}
					}
				}
				int max=0,sum=0;
				int flag=0;
				for(int i=0; i<19; i++)
				{
					sum=sum+classNum[i];
					if(classNum[i]>max)
					{
						max=classNum[i];
						flag=i;
					}
				}
				if(max==0 || sum==0)
					return null;
				if((double)max/(double)sum>(double)2/3)
					return classes[flag];
				else
					return null;
			}		
		}
		String currentData[]= Newdatarray.get(0).split(",");
		return currentData[35];		
	}
	
/*decide which attribute is the 
 * current node
 */
	public String getMostClass(Vector<String> datarray)
	{
		Vector<String> NewDataArray = this.deleteSpace(datarray);
		int sum[]= new int[19];
		
		for(int i=0; i<NewDataArray.size(); i++)
		{
			String currentData[]=NewDataArray.get(i).split(",");
			for(int j=0; j<classes.length; j++)
			{
				if(currentData[35].compareTo(classes[j])==0)
					sum[j]++;
			}
		}
		int max=0;
		int flag=-1;
		for(int i=0; i<classes.length; i++)
		{
			if(max<sum[i])
			{
				max=sum[i];
				flag=i;
			}
		}
		return classes[flag];
	}
	public int info_attribute(Vector<String> attriarray, Vector<String> datarray, int already[])
	{
		int sumAttri[][] = new int[36][20];
		int attriClasses[][][] = new int[36][19][19]; // to calculate I(), 
		//get each data row
		Vector<String> Newdatarray=this.deleteSpace(datarray);
		for(int i=0; i<Newdatarray.size(); i++) 
		{	
			//split current data by ","
			String currentData[]=Newdatarray.get(i).split(",");
			// attribute[k][j]
			for(int k=0; k<(currentData.length-1); k++)
			{
				for(int j=0; j<attributes[k].length; j++)
				{
					if(currentData[k].compareTo(attributes[k][j])==0)
					{
						sumAttri[k][j]++;
						for(int t=0; t<19; t++)
						{
							if(currentData[35].compareTo(classes[t])==0)
							{
								attriClasses[k][j][t]++;
								break;
							}
						}
						break;
					}
				}
			}
		}
		
		//calculate Info_a(D)
		double smallest=1000;
		//record which attribute Gain most
		int flag=-1; 
		for(int i=0; i<35; i++)
		{

			
			if(already[i]==0)
			{
				double temp=info_A(sumAttri[i],attriClasses[i],attributes[i].length);
				//if(datarray.size()==246)
				if(smallest>temp)
				{
					smallest=temp;
					flag = i;
				}
			}
		}
		return flag;
	}
	
	public double info_A(int sumAttri[], int attriClasses[][], int bits)
	{
		double result=0;
		int sum=0;
		for(int i=0; i<bits; i++)
		{
			sum=sum+sumAttri[i];
		}
		for(int i=0; i<bits; i++)
		{
			if(sumAttri[i]!=0)
			{
				result= result+ ((double)sumAttri[i]/ (double)sum)*I(attriClasses[i]);
			}
		}		
		return result;
	}
	
	public double I(int attriClasses[])
	{
		double In=0;
		int sum=0;
		double each=0;
		for(int i=0; i<19; i++)
		{
			sum=sum+ attriClasses[i];
		}
		for(int i=0; i<19; i++)
		{
			if(attriClasses[i]!=0)
			{
				double possibility= (double)attriClasses[i]/ (double)sum; 
				each= possibility* (Math.log(possibility)/Math.log(2));
				In =In + each;	
			}
		}
		return -In;
	}
	

	public Vector<String> getNewArray(Vector<String> datarray, int attri, int classification)
	{
		Vector<String> temp = new Vector<String>();
		for(int i=0; i<datarray.size(); i++)
		{
			String temp1=datarray.get(i);
			String temp2="";
			for(int k = 0; k <temp1.length(); k++) 
			{
				if (temp1.charAt(k) != ' ') 
			    {
					temp2 += temp1.charAt(k);
			    }
			}
			String currentData[]=temp2.split(",");
			if(currentData[attri].compareTo(attributes[attri][classification])==0)
			{
				temp.add(datarray.get(i));
			}
		}
		return temp;
	}
	
	public double info_0(Vector<String> attriarray, Vector<String> datarray)
	{
		Vector<String> Newdatarray=this.deleteSpace(datarray);
		int sum[] = new int[19];
		double info=0;
		double each=0;
		//initialization
		for(int i=0; i<19; i++)
		{
			sum[i]=0;
		}
		
		//calculate entropy
		//delete spaces
		for(int i=0; i<Newdatarray.size(); i++) //683
		{	
			String currentData[]=Newdatarray.get(i).split(",");
			for(int j=0; j<19; j++)
			{
				if(currentData[35].compareTo(classes[j])==0)
				{
					sum[j]++;
					break;
				}
			}
		}
		for(int i=0; i<19; i++)
		{
			
			double possibility= (double)sum[i]/ (double)datarray.size();
			if(possibility!=0)
			{
				each= -possibility* (Math.log(possibility)/Math.log(2));
			}
			info=info+ each;
		}
		return info;
	}
	
	public String getPrediction(String data)
	{
		String temp="";
		
		for (int k = 0; k < data.length(); k++) 
		{
			if (data.charAt(k) != ' ') 
		    {
				temp += data.charAt(k);
		    }
		}
		String currentData[]= temp.split(",");
		Node currentNode = root;
		while(currentNode.getType().compareTo("leaf")!=0)
		{
			for(int i=0; i<currentNode.getChildren().size(); i++)
			{
				String nodeClassifi;
				nodeClassifi = currentNode.getChildren().get(i).getClassification();
				String dataclassifi;
				int attriPosition= currentNode.getChildren().get(i).getAttriPosition();
				dataclassifi= currentData[attriPosition];
				if(nodeClassifi==null)
				{
					currentNode=currentNode.getChildren().get(0);
					break;
				}
				if(dataclassifi.compareTo(nodeClassifi)==0)
				{
					currentNode=currentNode.getChildren().get(i);
					break;
				}
			}
		//	currentNode=currentNode.getChildren().get(0);
		}
		return currentNode.getPrediction();
	}
	
	public int getClassLocation(String currentClass)
	{
		int flag=0;
		for(int i=0; i<19; i++)
		{
			if(currentClass.compareTo(classes[i])==0)
			{
				flag=i;
				break;
			}
		}
		return flag;
	}
	
	public Vector<String> deleteSpace(Vector<String> datarray)
	{
		Vector<String> NewArray= new Vector<String>();
		for(int i=0; i<datarray.size(); i++)
		{
			String temp1=datarray.get(i);
			String temp2="";
			for(int k = 0; k <temp1.length(); k++) 
			{
				if (temp1.charAt(k) != ' ') 
			    {
					temp2 += temp1.charAt(k);
			    }
			}
			NewArray.add(temp2);
		}
		return NewArray;
		
	}
	
	public int GINIsplit(Vector<String> attriarray, Vector<String> datarray, int already[])
	{
		int sumAttri[][] = new int[36][20];
		int attriClasses[][][] = new int[36][19][19]; // to calculate I(), 
		double giniSplits[] = new double[35];
		//get each data row
		Vector<String> Newdatarray=this.deleteSpace(datarray);
		Vector<String> Newattriarray=this.deleteSpace(attriarray);
		
		
		
		double smallest=1000;
		int flag=-1;
		for(int i=0; i<35; i++)
		{
			giniSplits[i]=-1;
			if(already[i]==0)
			{	
				for(int j=0; j<attributes[i].length; j++)
				{
					Vector<String> giniData= new Vector<String>();
					for(int k=0; k<Newdatarray.size(); k++)
					{
						String currentData[]= Newdatarray.get(k).split(",");
						if(currentData[i].compareTo(attributes[i][j])==0)
						{
							giniData.add(Newdatarray.get(k));
						}
					}
					if(giniData.size()!=0)
					{
						double p=(double)giniData.size()/(double)Newdatarray.size();
						giniSplits[i]=giniSplits[i]+p*this.gini(giniData);
					}
				}
			}
		}
		
		for(int i=0; i<35; i++)
		{
			if(giniSplits[i]!=-1)
			{
				if(giniSplits[i]<smallest)
				{
					smallest=giniSplits[i];
					flag=i;
				}
			}
		}
		return flag;
	}
	public double gini(Vector<String> giniData)
	{
		double result=0;
		int number[]= new int[19];
		double sum=0;
		for(int i=0; i<19; i++)
		{
			number[i]=0;
			for(int j=0; j<giniData.size(); j++)
			{
				String currentData[]=giniData.get(j).split(",");
				if(currentData[35].compareTo(classes[i])==0)
					number[i]++;
			}
		}
		for(int i=0; i<19; i++)
		{
			if(number[i]!=0)
			{
				int temp1=number[i]*number[i];
				int temp2=giniData.size()*giniData.size();
				
				sum+=(double)temp1/(double)temp2;
			}
		}
		result= 1-sum;
		return result;
	}
}
