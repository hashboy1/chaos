package com.chaos.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class testString {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		/*
		Random rand =new Random();
		
	   while(true)
		System.out.println(rand.nextInt(3));
     */
		Integer a = 128;
		Integer b = 128;
		System.out.println(b == a);
}
	
	
    public ArrayList<String> Permutation(String str) {
        ArrayList<String> aa=new ArrayList<String>();
        char[] ar = str.toCharArray();
        if (!str.equals(""))permute(aa,ar,0);    
        return aa;
    }
  

public static void permute(ArrayList<String> returnValue,char[] array,int start){  
	if(start==array.length){  // 输出
			System.out.println(Arrays.toString(array));
            String s = new String(array);
            returnValue.add(s);
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
	

}
