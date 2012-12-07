package Project.project1;
import org.apache.mahout.classifier.bayes.*;
public class App 
{

	public static void main(String args[]) throws Exception
	{
	/*	StructuredNaiveBayes test = new StructuredNaiveBayes();
		test.ReadFile("/home/cd/422/diabetes.arff");*/
		MyNaiveBayes test = new MyNaiveBayes();
		test.readfile("/home/cd/422/diabetes.arff");
	}
	
}
