/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import classes.BLASTSubhit;

/**
 *
 * @author ralfne
 */
public class SortedBLASTHit implements Comparable<SortedBLASTHit> {
    private String m_Header="";
    private Double m_Bitscore=null;
    private String m_EValue=null;
    private double m_IDPercentage=-1;
    private int m_IDs=-1;
    
    public SortedBLASTHit(){
        
    }
    
    public int getIDs(){ return m_IDs; }
    public double getIDPercentage(){ return m_IDPercentage; }
    public String getHeader(){ return m_Header; }
    public double getBitscore(){ return m_Bitscore; }
    
    public void setHeader(String desc){ m_Header=desc; }
    public void setBitscore(double score){ m_Bitscore=score; }
    public void setEValue(String value){ m_EValue=value; }
    public void setIDs(int value){ m_IDs=value; }
    public void setIDPercentage(double value){ m_IDPercentage=value; }
    
    @Override
    public int compareTo(SortedBLASTHit hit){
        int out=hit.m_Bitscore.compareTo(m_Bitscore);
        return out;
    }
}
