/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package View;

import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author khaled
 */
public class FileChooser {
    
    
    public static String saveFile(String name){
        String path=null;
        JFileChooser c = new JFileChooser();
        c.setSelectedFile(new File(name));
        int rVal = c.showSaveDialog(c);
        if (rVal == JFileChooser.APPROVE_OPTION) {
            String fileName =c.getSelectedFile().getName();        
            String directory = c.getCurrentDirectory().toString();
            path = directory +"\\"+fileName ;
        }
        if (rVal == JFileChooser.CANCEL_OPTION) {
          
        }
        return path;
    }
    
    public static String openFile(){
        String path=null;
        JFileChooser c = new JFileChooser();
        // Demonstrate "Open" dialog:
        int rVal = c.showOpenDialog(c);      
        if (rVal == JFileChooser.APPROVE_OPTION) {
           String fileName =c.getSelectedFile().getName();        
           String directory =c.getCurrentDirectory().toString();
           path = directory +"\\"+fileName;
        }
        if (rVal == JFileChooser.CANCEL_OPTION) {

        }
        return path;
    
    }
}
