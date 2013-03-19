/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.*;
import java.util.*;
import java.util.StringTokenizer;
import javaapplication1.UML;


/**
 *
 * @author Aarthi Giridharan
 */
public class JavaApplication1 {
    private static String path;
    public  static ArrayList stopWords = new ArrayList();
    /**
     * @param args the command line arguments
     */
    
    public static boolean stopWordCheck(String s)
    {
        ListIterator swli = stopWords.listIterator();
        int f = 0;
        while(swli.hasNext())
        {
            String swc = (String)swli.next();
            if(s.equals(swc))
            {
                f = 1;
                break;
            }                
        }
        if(f == 1)
            return true;
        else
            return false;
        
    }
    
    public static String Gamma(int a)
    {
        if(a == 1)
            return "0";
        else
        {
        String bs = Integer.toBinaryString(a);
        String subbs = bs.substring(1);
        int subbsl = subbs.length();
        String unar = "";
        for(int g = 0; g<subbsl ; g++)
        {
            unar+="1";
        }
        unar+="0";
        String gamma = unar+subbs;
        return gamma;
        }
    }
    
    public static String Delta(int p)
    {
        if(p == 1)
            return "0";
        else
        {
        String dbs = Integer.toBinaryString(p);
        int dbsl = dbs.length();
        String dsubbs = dbs.substring(1);
        String dgamma = Gamma(dbsl);
        String delta = dgamma + dsubbs;
        return delta;                
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        int c=0, count=0;
        path = "/people/cs/s/sanda/cs6322/Cranfield/";
        File dir = new File(path);
        String[] filenames = dir.list();
        
        ArrayList umlList = new ArrayList();
        
        int docno=0;
        
        FileReader sW = new FileReader("/people/cs/s/sanda/cs6322/resourcesIR/stopwords");
        BufferedReader bufferRead = new BufferedReader(sW);
        String stringVar;
        
             
        
        while((stringVar = bufferRead.readLine())!= null)
        {
            stopWords.add(stringVar);

        }
        
        for(int x=0;x<1400;x++)
        {
        
        FileReader fileReader = new FileReader(path+filenames[x]);
        BufferedReader bufferReader = new BufferedReader(fileReader);
        String stringVariable;
        String textVariable = "";
        
        
        int flag = 0;
        int flag1 = 0;
        while((stringVariable = bufferReader.readLine()) != null)
        {
            if(stringVariable.equals("<TITLE>")||stringVariable.equals("</TITLE>")||stringVariable.equals("<AUTHOR>")||stringVariable.equals("</AUTHOR>")||stringVariable.equals("<TITLE>")||stringVariable.equals("<BIBLIO>")||stringVariable.equals("</BIBLIO>"))
            continue;
            
            if(stringVariable.equals("<DOCNO>"))
            {flag1 = 1;}
            
            if(flag1 == 1)
            {
                String s = bufferReader.readLine();
                docno = Integer.parseInt(s);
                flag1 = 0;
            }
            
            if(stringVariable.equals("</DOCNO>"))
            {flag1 = 0;}
                
            if(stringVariable.equals("<TEXT>"))
            {flag = 1;}
            if(flag == 1)
            {
            textVariable = textVariable +" "+stringVariable.toLowerCase();
            }
         
            if(stringVariable.equals("</TEXT>"))
            {flag = 0;}
        
         }
        
        String replaceAll = textVariable.replaceAll("\\)", " ");
            replaceAll=replaceAll.replaceAll("\n", " ");
            replaceAll=replaceAll.replaceAll("\\.", " ");
            replaceAll=replaceAll.replaceAll(" \\. ", " ");
            replaceAll=replaceAll.replaceAll(",", " ");
            replaceAll=replaceAll.replaceAll("/", " ");
            replaceAll=replaceAll.replaceAll("-", " ");
            replaceAll=replaceAll.replaceAll("\\(", " ");
            replaceAll=replaceAll.replaceAll("'s", " ");
            replaceAll=replaceAll.replaceAll("<text>", " ");
           
 
            
            StringTokenizer st2 = new StringTokenizer(replaceAll);
            
            int dflag = 0;
            int uflag = 0;
            int uindex = 0;
            
            while(st2.hasMoreTokens())
            {
                String token = st2.nextToken();
                
                if(stopWordCheck(token)==false)
                {                    
                ListIterator uli = umlList.listIterator();
                token = stem(token);
                while(uli.hasNext())
                {
                    UML u = (UML)uli.next();
                    
                    if(u.getTerm().equals(token))
                    {
                        uflag = 1;
                        uindex = uli.previousIndex();
                        break;
                    }
                }
                
                
            if(uflag == 1)
            {
                UML u = (UML)umlList.get(uindex);
                
                ListIterator li = u.docidList.listIterator();
                
                
                int tindex = 0;
                while(li.hasNext())
                {
                    Integer tp = Integer.parseInt(li.next().toString());
                    tindex = li.previousIndex();
                    if(tp == docno )
                    {dflag = 1;
                     break;
                    }
                }
                
                if (dflag == 0)
                {
                    u.docidList.add(docno);
                    u.termFrequency.add((int)1);
                }
                
                else
                {
                    Integer temp = Integer.parseInt(u.termFrequency.get(tindex).toString());
                    temp++;
                    u.termFrequency.add(tindex,temp);
                    dflag=0;
                    
                }
                umlList.remove(uindex);
                umlList.add(uindex,u);
                
                        
            uflag=0;            
            }
            else
            {
                UML u = new UML();
                u.setTerm(token);
                u.docidList.add(docno);
                u.termFrequency.add((int)1);
                umlList.add(u);
                
            }
                }
            }
        }
        
 
            
            ListIterator oli = umlList.listIterator();
            
            while(oli.hasNext())
            {
                UML t = (UML)oli.next();
                               
                ListIterator odi = t.docidList.listIterator();
                while(odi.hasNext())
                {
                    Integer d = Integer.parseInt(odi.next().toString());
                    int ind = (int)odi.previousIndex();
                
                }
                
                int docfreq = t.docidList.size();
                t.setDocumentFrequency(docfreq);
                
            }
            
            
            
            String opt1 = "";
            String opt2 = "";
            
            Writer output1;
            Writer output2;
            File file1 = new File("uncompressed.txt");
            File file2 = new File("compressed.txt");
            output1 = new BufferedWriter(new FileWriter(file1));
            output2 = new BufferedWriter(new FileWriter(file2));
            
            ListIterator opli = umlList.listIterator();
            int count1=0;
            while(opli.hasNext())
            {
                count1++;
                UML ul = (UML)opli.next();
                output1.write(ul.getTerm()+" "+(int)ul.getDocumentFrequency()+" {");
                output2.write(ul.getTerm()+" "+Gamma(ul.getDocumentFrequency())+" {");
                ListIterator opdl = ul.docidList.listIterator();
                while(opdl.hasNext())
                {
                    Integer odil = Integer.parseInt(opdl.next().toString());
                    int di = opdl.previousIndex();
                    output1.write(" "+odil+": ");
                    output2.write(" "+Delta(odil)+": ");
                    ListIterator frql = ul.termFrequency.listIterator();
                    
                    while(frql.hasNext())
                    {
                        Integer frl = Integer.parseInt(frql.next().toString());
                        int fi = frql.previousIndex();
                        if(di == fi)
                        {
                            output1.write(frl+" ");
                            output2.write(Gamma(frl)+" ");
                            break;
                        }
                                           
                    }
                    
                }
                output1.write("} [ ");
                output2.write("} [ ");
                
                
                ListIterator opdl1 = ul.docidList.listIterator();
                
                while(opdl1.hasNext())
                {
                    Integer dl = Integer.parseInt(opdl1.next().toString());
                    if(opdl1.hasNext())
                    {
                    output1.write( dl + ", ");
                    output2.write( Delta(dl) + ", ");
                    }
                    else
                    {
                    output1.write( dl + " ");
                    output2.write( Delta(dl) + " ");
                    }
                }
                
                output1.write("]  ");
                output1.write("\n");
                output2.write("]  ");
                output2.write("\n");
                
            }
                        
            output1.close();
            output2.close();
              
                
             }
    public static String stem(String input){
        Stemmer s = new Stemmer();
        s.add(input.toCharArray(),input.length());
        s.stem();
        return s.toString();
    }
            
    }

