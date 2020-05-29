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
public class SimpleHash
{
    
    
    public static void generateHashTable(ArrayList<Object[]> values)
    {
        int arraySize = getNextPrime(values.size() * 2);
        
        System.out.println(arraySize);
        String[] myArray = new String[arraySize];
        
        for (int i = 0; i < values.size(); i++)
        {
            String elementValue = values.get(i)[0].toString();
            int key = Integer.parseInt(values.get(i)[5].toString()) % arraySize;         
            
            myArray[key] = elementValue;
            
            System.out.println("Key Index = " + key + "for element " + elementValue );
            
            while(myArray[key] != null)
            {
                key++;
                System.out.println("Collision detected, try " + key + "instead!" );
                key %= myArray.length;
            }
        }
    }
    
    public static String findHashElement(String key, String[] myArray)
    {
        int arrayIndexHash = Integer.parseInt(key) % myArray.length;
        while(myArray[arrayIndexHash] != null)
        {
            if (myArray[arrayIndexHash].equals(key))
            {
                System.out.println("Key: " + key + "was found at index" + arrayIndexHash);
                
                return myArray[arrayIndexHash];
            }
            ++arrayIndexHash;
            arrayIndexHash %= myArray.length;
        }
        return null;
    }
    
    public static int getNextPrime(int numToCheck)
    {
        for (int i = numToCheck; true; i++)
        {
            if (isPrime(i))
            {
                return i;
            }
        }
       
    }
    
    public static boolean isPrime(int num)
    {
        if (num % 2 == 0)
        {
            return false;
        }
        for (int i = 3; i * i <= num; i++)
        {
            if (num % i == 0)
            {
                return false;
            }
            
        }
        return true;
    }
}
