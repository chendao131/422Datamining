package assign2;
import java.util.*;
public class Node {
	private String type;
	private String attribute;
	private int attriPosition;
	private String classification;
	private Vector<String> datas;
	private Vector<Node> children;
	private String prediction;
	private int depth;
	
	public Node(String rootData)
	{
		this.type="root";
		this.attribute = rootData;
		this.attriPosition=0;
		this.classification=null;
		this.datas=null;
		this.children = new Vector<Node>();
		this.prediction = null;
		
		this.depth=0;
	}
	
	public Node(String leaf, String prediction)
	{
		this.type = leaf;
		this.attribute= null;
		this.attriPosition=0;
		this.classification=null;
		this.datas=null;
		this.children = new Vector<Node>();
		this.prediction = prediction;
	}
	public Node(String node, int position,String classifi, Vector<String>datas)
	{
		this.type = node;
		this.attribute=null;
		this.attriPosition=position; //
		this.classification=classifi;
		this.datas=datas;
		this.children = new Vector<Node>();
		this.prediction = null;
	}
	
	public Vector<Node> findChildren()
	{
		return this.children;
	}
	
	public void buildTree(Node thisnode)
	{
		this.children.add(thisnode);
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public Vector<Node> getChildren()
	{
		return this.children;
	}
	
	public String getAttribute()
	{
		return this.attribute;
	}
	
	public int getAttriPosition()
	{
		return this.attriPosition;
	}
	
	public String getClassification()
	{
		return this.classification;
	}
	public String getPrediction()
	{
		return this.prediction;
	}
	
	public int getDepth()
	{
		return this.depth;
	}
	public void setAttribute(String attriName)
	{
		this.attribute= attriName;
	}
	public void setClassification(String classification)
	{
		this.classification=classification;
	}
	public void setPosition(int position)
	{
		this.attriPosition=position;
	}
	public void setDepth(Node parentNode)
	{
		this.depth= parentNode.getDepth()+1;
	}
}
