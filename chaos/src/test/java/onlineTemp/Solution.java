package onlineTemp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.mysql.fabric.xmlrpc.base.Array;
public class Solution {
    public ArrayList<String> Permutation(String str) {
        ArrayList<String> aa=new ArrayList<String>();
        char[] ar = str.toCharArray();
        if (!str.equals(""))permute(aa,ar,0);    
        Collections.sort(aa);
        return aa;
    }
  

public static void permute(ArrayList<String> returnValue,char[] array,int start){  
	if(start==array.length){  // 输出
			System.out.println(Arrays.toString(array));
			//Arrays.sort(array);
            String s = new String(array);
           if (!arrayListTraversal(returnValue,s))  returnValue.add(s);
		}
	else
	for(int i=start;i<array.length;++i){
		swap(array,start,i);  
		permute(returnValue,array,start+1);  
		swap(array,start,i);  
		}
}
 
private static void swap(char[] array,int s,int i){
	char t=array[s];
	array[s]=array[i];
	array[i]=t;
}
    
    
    public static boolean arrayListTraversal(ArrayList<String> lists,String invalue)
    {
         for (String list : lists) {
            if (list.equals(invalue)) return true;
        }
        return false;
    }
    
    public static void main(String[] args) {
    
    	Solution sl = new Solution();
    	
       System.out.println(sl.Permutation("abcd").toString());
    }
    
    


}