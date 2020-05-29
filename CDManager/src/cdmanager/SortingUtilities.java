/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdmanager;

import java.util.ArrayList;

/**
 *
 * @author Troy
 */
public class SortingUtilities
{ 
    
    //                                  
    //Bubble Sort on ArrayList of objects based around column specified by parameter.
    //
      public static ArrayList<Object[]> bubbleSort(ArrayList<Object[]> arr, int parameter) 
    {
        
        for(int j=0; j<arr.size(); j++) 
        {  
            for(int i=j+1; i<arr.size(); i++)
            {  
                if((arr.get(i)[parameter]).toString().compareToIgnoreCase(arr.get(j)[parameter].toString()) < 0)
                {  
                   Object[] words = arr.get(j); 
                   arr.set(j, arr.get(i));
                   arr.set(i, words);
                }  
            }  
            System.out.println(arr.get(j)[0] + " - " + arr.get(j)[1]);  
        }  
        return arr;
    }  
      
    //  
    //Perform selection sort on ArrayList based upon column specified by parameter
    //
    public static ArrayList<Object[]> selectionSort(ArrayList<Object[]> arr, int parameter)
    {
        for (int i = 0 ; i < arr.size() -1; ++i)
        {
            int first = i;
            for (int j = i +1 ; j < arr.size(); ++j)
            {
                if ((arr.get(j)[parameter]).toString().compareToIgnoreCase(arr.get(first)[parameter].toString()) < 0)
                {
                    first = j;
                }
                
                Object[] temp = arr.get(i); 
                arr.set(i, arr.get(first));
                arr.set(first, temp);
            } 
        }  
        return arr;
    }
    
    public static ArrayList<Object[]> InsertionSort (ArrayList<Object[]> arr, int param)
    {
        
        
        return arr;
    }
    
    public static int[] InsertionSort(int[] num)
    {
        int key;
        
        for (int i = 1; i < num.length; i++)
        {
            key = num[i];
            
            for (int j = i; j >= 0; j--)
            {
                num[j + 1] = num[j];
            }
            num[i + 1] = key;
        }
            return num;    
    }
}
