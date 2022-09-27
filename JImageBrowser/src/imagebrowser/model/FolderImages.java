/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagebrowser.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sely
 */
public class FolderImages {
     static int currentIndex=-1;
     static List<String> listOfImageFiles = new ArrayList<>();
     
     public static List<String> getImageFiles(){
         return listOfImageFiles;
     }
     
     public static int getIndex(){
         return currentIndex;
     }
     
     public static void setIndex(int index){
         currentIndex=index;
     }
    
}
