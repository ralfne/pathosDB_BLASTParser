/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 *
 * @author ralfne
 */
public class Parasites extends HashMap<String, Integer> {
    
    public void load(String filename)throws Exception{
        BufferedReader br=new BufferedReader(new FileReader(filename));
        String line="";
        while((line=br.readLine())!=null){
            String [] s=line.split("\t");
            Integer taxID=Integer.parseInt(s[0]);
            this.put(s[1], taxID);
        }
        br.close();
    }
    
}
