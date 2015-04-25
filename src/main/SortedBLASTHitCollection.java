/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import classes.BLASTHit;
import classes.BLASTSubhit;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author ralfne
 */
public class SortedBLASTHitCollection extends ArrayList<SortedBLASTHit> {
    
    public void addHits(ArrayList<BLASTHit> hits){
        Iterator<BLASTHit> iter=hits.iterator();
        while(iter.hasNext()){
            BLASTHit hit=iter.next();
            addHit(hit);
        }
    }
    
    public void addHit(BLASTHit hit){
        Iterator<BLASTSubhit> iter=hit.subhits.iterator();
        while(iter.hasNext()){
            BLASTSubhit subhit=iter.next();
            SortedBLASTHit sortedHit=new SortedBLASTHit();
            sortedHit.setHeader(hit.description);
            sortedHit.setBitscore(subhit.bitScore);
            sortedHit.setEValue(subhit.expectValue);
            sortedHit.setIDs(subhit.identities);
            sortedHit.setIDPercentage(subhit.identitiesPercentage);
            this.add(sortedHit);
        }
    }
    
}
