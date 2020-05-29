/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cdmanager;

/**
 *
 * @author Troy
 */
public class CDDataValues
{
//    String Title;
//    String Author;
//    String Section;
//    String XPos;
//    String YPos;
//    String Barcode;
//    String Description;
//    
    String[] values;
    
    public CDDataValues(String title, String author, String section, String xpos, 
                        String ypos, String barcode, String description)
    {
        values = new String[]{title, author, section, xpos, ypos, barcode, description};
//        Title = title;
//        Author = author;
//        Section = section;
//        XPos = xpos;
//        YPos = ypos;
//        Barcode = barcode;
//        Description = description;
    }
}
