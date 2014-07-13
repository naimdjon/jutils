package no.ntnu.utils;
import java.util.Comparator;

public class StrLenComparator implements Comparator<String>{
	
	public int compare(String o1, String o2) {
		int length1 = o1.length();
		int length2 = o2.length();
		return (length1<length2 ? -1 : (length1==length2 ? 0 : 1));
	}

}
