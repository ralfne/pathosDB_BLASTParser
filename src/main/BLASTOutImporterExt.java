/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import classes.BLASTOutImporter_xml;
import classes.BLASTQuery;
import classes.BLASTQueryCollection;
import org.xml.sax.SAXException;


/**
 *
 * @author ralfne
 */
public class BLASTOutImporterExt extends BLASTOutImporter_xml {
    private Parser m_Parent=null;
    
    public BLASTOutImporterExt(int expZeroValue, int hitsPrProgressTick, 
        boolean writeWarningsForMissingGiNumbers, boolean writeWarningsForMultipleGiNumbers, 
        boolean writeWarningsForEmptyQueries, boolean importQueriesWithoutHits){
        super(expZeroValue,hitsPrProgressTick,
                writeWarningsForMissingGiNumbers, 
                writeWarningsForMultipleGiNumbers, 
                writeWarningsForEmptyQueries, 
                importQueriesWithoutHits);
        super.setBLASTqueries(new BLASTQueryCollection(null));
    }
    
    public void setParent(Parser p){
        m_Parent=p;
    }
        
    @Override
    public void addBLASTquery(BLASTQuery query){
        //super.addBLASTquery(query);
        m_Parent.processBLASTquery(query);
    }
    
    
    
  
}
