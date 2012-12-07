package samplepart;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

	public static void main(String[] args) throws Exception{
		Pattern p = Pattern.compile("\\d+(,)\\d+");
		Matcher m = p.matcher("187,123");
		boolean b = m.matches();
		System.out.println(b);
	}
}
