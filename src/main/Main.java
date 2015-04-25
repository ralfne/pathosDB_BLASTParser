/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.HashMap;

/**
 *
 * @author ralfne
 */
public class Main {
    public final static String PATH_SEPARATOR=System.getProperty("file.separator");
    public final static String NEWLINE=System.getProperty("line.separator");
    private final static String ARG_IN="-i";
    private final static String ARG_OUT="-o";
    private final static String ARG_HELP="-h";
    private final static String ARG_PARASITE_FILE="-p";
    private final static String ARG_IDENTITY_CUTOFF="-x";
    private final static String ARG_IDENTITY_PERCENTAGE_CUTOFF="-y";
    
    private final static double DEFAULT_IDENTITY_PERCENTAGE_CUTOFF=98;
    private final static int DEFAULT_IDENTITY_CUTOFF=200;
    

    public static void main(String[] args) {
        
        try{
    
            if(args.length==1 && args[0].equals("-h")){
                printHelp();
                return;
            }

           HashMap<String, String> arguments=getCMDarguments(args);
            if(arguments.isEmpty()){
                printHelp();
                return;
            }

            String inFile=arguments.get(ARG_IN);
            String outFolder=arguments.get(ARG_OUT);
            String parasiteFile=arguments.get(ARG_PARASITE_FILE);
            int idsCutoff=DEFAULT_IDENTITY_CUTOFF;
            double idPercentageCutoff=DEFAULT_IDENTITY_PERCENTAGE_CUTOFF;

            if(arguments.get(ARG_IDENTITY_CUTOFF)!=null){
                String s=arguments.get(ARG_IDENTITY_CUTOFF);
                idsCutoff=Integer.parseInt(s);
            }

            if(arguments.get(ARG_IDENTITY_PERCENTAGE_CUTOFF)!=null){
                String s=arguments.get(ARG_IDENTITY_PERCENTAGE_CUTOFF);
                idPercentageCutoff=Double.parseDouble(s);
            }

            System.out.println("Loading parasites file...");
            Parasites parasites=new Parasites();
            parasites.load(parasiteFile);
            System.out.println("Done!");

            System.out.println("Parsing BLAST output file...");
            Parser p=new Parser(idsCutoff,idPercentageCutoff, parasites);
            p.parse(inFile, outFolder);
            System.out.println("Done!");
        }catch(Exception ex){
            ex.printStackTrace();
        }

    } 
   
    private static void printHelp(){
        String message="ParasiteDB_BLASTParser" + Main.NEWLINE;
        message+="Usage:" + Main.NEWLINE;
        message+="-h: print help" + Main.NEWLINE;
        message+="-i: input file (BLAST output file in XML format)" + Main.NEWLINE;
        message+="-o: output folder" + Main.NEWLINE;
        message+="-p: ParasiteDB file holding all parasite species names" + Main.NEWLINE;
        message+="-x: Identity cutoff, default=" + DEFAULT_IDENTITY_CUTOFF + Main.NEWLINE;
        message+="-y: Identity percentage cutoff, default=" + DEFAULT_IDENTITY_PERCENTAGE_CUTOFF;
        System.out.println(message);
    }
        
    private static HashMap<String,String> getCMDarguments(String[] args){
        HashMap<String,String> parsedArgs=new HashMap<String, String>();
        int i=0;
        while(i<args.length){
            String id=args[i];i++;
            String value=args[i];i++;
            parsedArgs.put(id,value);            
        }
        return parsedArgs;
    }
    
}
