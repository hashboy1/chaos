package onlineTemp;
import java.util.Scanner;
public class Main {
   
    public static void main(String[] args) {
        Scanner input= new Scanner(System.in);
        int input_count=input.nextInt();
        int num_arr[]=new int[input_count];
        int i=0;
        while(i<input_count)
        {    
            int temp=input.nextInt();
            int count=0;
            for(int j=0;j<i;j++)
            {   
                if (num_arr[j] == temp) count++;
            }
            if (count==0) num_arr[i]=temp;
            
            i++;
        }
        
        sort(num_arr);
        i=0;
        while (i<input_count)
        {
            System.out.println(num_arr[i]);
            i++;
        }
        
        
    }
    
      public static void sort(int[] targetArr) {
        long t = System.currentTimeMillis();
        int temp = 0;
        for (int i = 0; i < targetArr.length; i++) {
            for (int j = i; j < targetArr.length; j++) {
                if (targetArr[i] > targetArr[j]) {
                    targetArr[i] = targetArr[i] ^ targetArr[j];
                    targetArr[j] = targetArr[i] ^ targetArr[j];
                    targetArr[i] = targetArr[i] ^ targetArr[j];
                }
            }
        }
      }
    
}