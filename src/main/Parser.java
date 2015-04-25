/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import classes.BLASTQuery;
import classes.BLASTimportXMLHandler;
import classes.ImportEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import org.xml.sax.InputSource;

/**
 *
 * @author ralfne
 */
public class Parser implements ImportEvent.ImportEventListener{
    private BLASTOutImporterExt m_Importer=null;
    private int m_IDsCutoff=-1;
    private double m_IDPercentagesCutoff=-1;
    private Parasites m_Parasites=null;
    private HashMap<String,String> m_UniqueParasites=null;
    private BufferedWriter m_FastaHeaderWriter=null;
    private final String PARASITE_OUT_FILENAME="parasites.txt";
    private final String FASTAHEADER_OUT_FILENAME="fastaheaders.txt";
    private ArrayList<String> m_Errors=null;
    
    public Parser(int ids, double idPercentages, Parasites parasites){
        m_IDsCutoff=ids;
        m_IDPercentagesCutoff=idPercentages;
        m_Parasites=parasites;
    }
    
    public void parse(String inputFile, String outFolder)throws Exception{
        if(!outFolder.endsWith("/")){
            outFolder+="/";
        }
        String parasiteFile=outFolder +  PARASITE_OUT_FILENAME;
        String fastaHeaderFile=outFolder +  FASTAHEADER_OUT_FILENAME;
        m_UniqueParasites=new HashMap<String, String>();
        m_Errors=new ArrayList<String>();
        m_Importer=new BLASTOutImporterExt(-999999,1000,false, false, false,false);
        m_Importer.addImportEventListener(this);
        m_Importer.setParent(this);
        
        openFastaHeaderWriter(fastaHeaderFile);
        try{
            m_Importer.createXMLReader(new BLASTimportXMLHandler(m_Importer)).parse(new InputSource(inputFile));
            if(m_Errors.isEmpty()){ writeParasiteRecords(parasiteFile); }
        }catch(Exception e){
            throw e;
        }finally{
            closeFastaHeaderWriter();
        }
        
        if(!m_Errors.isEmpty()){
            throw new Exception("Errors occured!");
        }
        
    }
    
    public void openFastaHeaderWriter(String filename)throws Exception{
        m_FastaHeaderWriter=new BufferedWriter(new FileWriter(filename));
    }
    
    private void closeFastaHeaderWriter()throws Exception{
        m_FastaHeaderWriter.flush();
        m_FastaHeaderWriter.close();
    }
    
    private void writeFastaHeaderRecord(BLASTQuery query, ParasiteAndHitHeader parasiteAndHeader)throws Exception{
        String rec=query.id + "\t" + parasiteAndHeader.HitHeader;
        rec+="\t" + parasiteAndHeader.Parasite;
        m_FastaHeaderWriter.write(rec);
        m_FastaHeaderWriter.newLine();
    }
    
    @Override
    public void HandleImportEvent(String message, boolean append, Integer progress){
        
    }
    
    public void processBLASTquery(BLASTQuery query){
        try {
           if(query.hits.isEmpty()){ return; }
            
           SortedBLASTHitCollection hits=new SortedBLASTHitCollection();
           hits.addHits(query.hits);
           Collections.sort(hits);
           ParasiteAndHitHeader ph=getParasiteSpecies(hits);
           if(ph!=null){
               String s=m_UniqueParasites.get(ph.Parasite);
               if(s==null){ m_UniqueParasites.put(ph.Parasite, ph.Parasite); }
               writeFastaHeaderRecord(query, ph);
           }           
        } catch (Exception e) {
            m_Errors.add("Query:" + query.id);
        }
    }
    
    private void writeParasiteRecords(String filename)throws Exception{
        BufferedWriter bw=new BufferedWriter(new FileWriter(filename));
        try {
            Iterator<String> iter=m_UniqueParasites.keySet().iterator();
            while(iter.hasNext()){
                String parasite=iter.next();
                Integer taxID=m_Parasites.get(parasite);
                String line=String.valueOf(taxID) + "\t" + parasite;
                bw.write(line);
                bw.newLine();
            }            
        } catch (Exception e) {
            throw e;
        } finally{
            bw.flush();
            bw.close();
        }
    }
    
    private boolean passesFilter(SortedBLASTHit hit){
        boolean out=true;
        if (hit.getIDs()<m_IDsCutoff){
            out=false;
            return out;
        }
        if(hit.getIDPercentage()<m_IDPercentagesCutoff){
            out=false;
            return out;
        }
        return out;
    }
    
    private ParasiteAndHitHeader getParasiteSpecies(SortedBLASTHitCollection hits){
        ParasiteAndHitHeader out=null;
        Double bestBisScore=null;
        Iterator<SortedBLASTHit> iter=hits.iterator();
        while(iter.hasNext()){
            SortedBLASTHit hit=iter.next();
            if(bestBisScore==null){
                bestBisScore=hit.getBitscore();
                String parasite=getParasiteSpecies(hit);
                if(!parasite.isEmpty()){
                    out=new ParasiteAndHitHeader(parasite, hit.getHeader());
                    return out;
                }
            }else{
                if(hit.getBitscore()<bestBisScore){
                    return null;
                }else{
                    String parasite=getParasiteSpecies(hit);
                    if(!parasite.isEmpty()){
                        out=new ParasiteAndHitHeader(parasite, hit.getHeader());
                        return out;
                    }   
                }
            }
            
        }
        return null;
    }

    private String getParasiteSpecies(SortedBLASTHit hit){
        //>CP000286|Cryptococcus gattii WM276
        String[] s=hit.getHeader().split("\\|");
        String speciesName=s[1];
        if(m_Parasites.get(speciesName)!=null){
            if(passesFilter(hit)){
                return speciesName;
            }
        }
        return "";
    }
    
    private class ParasiteAndHitHeader{
        public String Parasite="";
        public String HitHeader="";
        public ParasiteAndHitHeader(String parasite, String header){
            this.Parasite=parasite;
            this.HitHeader=header;
        }
    }
}



