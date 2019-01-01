package Modle;

import javax.swing.text.html.ListView;
import java.awt.*;
import java.awt.List;
import java.io.*;
import java.util.*;

public class Ranker {
    public Map<String,Documentt> documents; //information on every doc
    public Map<String,Term> dictionary;     //inverted index of terms
    public Map<String,City> cities_index;
    public ArrayList<String> query;
    private String Posting_And_dictionary_path_in_disk;
    double avdl;




    public Ranker(String Posting_And_dictionary_path_in_disk,boolean stem,HashMap<String,City> cities_index) {
        this.documents = new HashMap<>();
        this.dictionary=new HashMap<>();
        this.cities_index=new HashMap<>();
        this.cities_index=cities_index;
        this.Posting_And_dictionary_path_in_disk=Posting_And_dictionary_path_in_disk;
        Load_Dictionary_and_documets(stem,Posting_And_dictionary_path_in_disk);
        this.avdl=avdl();
    }

    private void Load_Dictionary_and_documets(boolean stem, String posting_and_dictionary_path_in_disk) {
        FileInputStream fis;
        try {
            BufferedReader br;

            if(stem)
                br = new BufferedReader(new FileReader(new File(Posting_And_dictionary_path_in_disk + "\\" + "dictionary stem")));
            else
                br=new BufferedReader(new FileReader(new File(Posting_And_dictionary_path_in_disk + "\\" + "dictionary")));

            String line;
            while ( (line = br.readLine()) != null )
            {
                String[] s=line.split("#");
                Term term=new Term(s[0],Long.parseLong(s[1]),Integer.valueOf(s[2]),Integer.valueOf(s[3]));
                dictionary.put(term.getTerm(),term);
            }


            BufferedReader br1;
            if(stem)
                br1 = new BufferedReader(new FileReader(new File(Posting_And_dictionary_path_in_disk + "\\" + "docs stem")));
            else
                br1=new BufferedReader(new FileReader(new File(Posting_And_dictionary_path_in_disk + "\\" + "docs")));

            String line1;
            while ( (line1 = br1.readLine()) != null )
            {
                String[] s=line1.split("#");
                ArrayList<String> yeshooyott = new ArrayList<>();
                if(s.length==7) {
                    String yeshooyot = s[6];
                    String[] s1 = yeshooyot.split(",");
                    for (int i = 0; i < s1.length; i++)
                        yeshooyott.add(s1[i]);
                }


                Documentt documett=new Documentt(s[0],Integer.valueOf(s[1]),Integer.valueOf(s[3]),s[4],Integer.valueOf(s[2]));
                documett.setHeader(s[5]);
                documett.setYeshooyot(yeshooyott);
                documents.put(documett.Doc_Name,documett);
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private double avdl() {
        double sum=0;
        for (Map.Entry<String,Documentt> entry : documents.entrySet()) {
            sum+=entry.getValue().getDocLength();
        }
        return (sum/documents.size());
    }


    public ArrayList<Map.Entry<Documentt,Double>> Rank(ArrayList<String> query,boolean filter,HashSet<String> docsFilter){//returns the docNo sorted by rank
        this.query=query;
        HashMap<Documentt,Double> total=new HashMap<>();
        Map<String,Double> BM25=new HashMap();
        Map<String,Double> Wij_Wiq=new HashMap();
        Map<String,Double> Wij_2=new HashMap();
        Map<String,Double> Wiq_2=new HashMap();
        Map<String,Double> AtStart=new HashMap();
        Map<String,Double> AtHeader=new HashMap();

        for(int i=0;i<query.size();i++) {
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(Posting_And_dictionary_path_in_disk+"\\"+"final_posting", "rw");
                if(!dictionary.containsKey(query.get(i))) {
                    //check if the word is at city tags
                    //bring the cities index from searcher
                    //add to finish score with grade 1
                    if(cities_index.containsKey(query.get(i))) {
                        //the word exist in city tag but not at text
                        City toAdd=cities_index.get(i);
                        HashMap<String,String> loc=toAdd.getLocation();
                        for(Map.Entry<String,String> entry: loc.entrySet()){
                            total.put(documents.get(entry.getKey()),1.0);
                        }
                    }
                    continue;
                }
                long pointer=dictionary.get(query.get(i)).getPointerToPostings();
                raf.seek(pointer);
                String postiongForWord=raf.readLine();
                String [] docsWithWord=spliteDocNo(postiongForWord);
                for(int j=0;j<docsWithWord.length;j++){
                    String docNo=docsWithWord[j].substring(0,docsWithWord[j].indexOf("+"));
                    if(filter) {
                        if (!docsFilter.contains(docNo))
                            continue;
                    }

                    //atHeader
                    double atHeader=existInHeader(docNo,query.get(i));
                    if(AtHeader.containsKey(docNo))
                        AtHeader.put(docNo,AtHeader.get(docNo)+atHeader);
                    else
                        AtHeader.put(docNo,atHeader);

                    //atStart
                    double atStart=Double.valueOf(docsWithWord[j].substring(docsWithWord[j].indexOf("~")+1,docsWithWord[j].length()));
                    if(AtStart.containsKey(docNo))
                        AtStart.put(docNo,AtStart.get(docNo)+atStart);
                    else
                        AtStart.put(docNo,atStart);

                    //bm25
                    double df=dictionary.get(query.get(i)).getDf();
                    double bm25=BM25(query.get(i),docsWithWord[j],documents.get(docNo).getDocLength(),df);

                    if(BM25.containsKey(docNo))
                        BM25.put(docNo,BM25.get(docNo)+bm25);
                    else
                        BM25.put(docNo,bm25);

                    //1=wij*wiq
                    double wij_wiq=Wij_Wiq(docsWithWord[j],(double)documents.get(docNo).getDocLength(),df,1);
                    if(Wij_Wiq.containsKey(docNo))
                        Wij_Wiq.put(docNo,Wij_Wiq.get(docNo)+wij_wiq);
                    else
                        Wij_Wiq.put(docNo,wij_wiq);
                    //2=wij^2
                    double wij=Wij_Wiq(docsWithWord[j],(double)documents.get(docNo).getDocLength(),df,2);
                    if(Wij_2.containsKey(docNo))
                        Wij_2.put(docNo,Wij_2.get(docNo)+wij);
                    else
                        Wij_2.put(docNo,wij);
                    //3=wiq^2
                    double wiq=Wij_Wiq(docsWithWord[j],(double)documents.get(docNo).getDocLength(),df,3);
                    if(Wiq_2.containsKey(docNo))
                        Wiq_2.put(docNo,Wiq_2.get(docNo)+wiq);
                    else
                        Wiq_2.put(docNo,wiq);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        HashMap<String,Double> finalCosSim=finalCosSim(Wij_Wiq,Wij_2,Wiq_2);
        double maxbm25 = getMax(BM25);
        nirmul(BM25,maxbm25);
        double maxCos = getMax(finalCosSim);
        nirmul(finalCosSim,maxCos);
        double maxStart = getMax(AtStart);
        nirmul(AtStart,maxStart);
        nirmul(AtHeader,query.size());


        return finalCalculate(BM25,finalCosSim,AtStart,AtHeader,total);

    }

    private void nirmul(Map<String, Double> toNormelize,double max) {
        for(Map.Entry<String,Double> entry: toNormelize.entrySet()) {
            String doc=entry.getKey();
            toNormelize.put(doc,(entry.getValue()/max));
        }



    }
    private double getMax(Map<String, Double> map) {
        double max=0;
        for(Map.Entry<String,Double> entry: map.entrySet()) {
            if(max<entry.getValue())
                max=entry.getValue();
        }
        return max;
    }



    private double existInHeader(String docNo, String queryWord) {
        String header=documents.get(docNo).getHeader();
        if(header.equals("-1"))
            return (double)0;
        String [] headerByWords=header.split(" ");
        String wordFromQuery=queryWord.toUpperCase();
        for (int i=0;i<headerByWords.length;i++){
            String wordFromHeader=headerByWords[i].toUpperCase();
            if(wordFromHeader.equals(wordFromQuery))
                return (double)1;
        }
        return (double)0;


    }

    private HashMap<String,Double> finalCosSim(Map<String, Double> wij_wiq, Map<String, Double> wij_2, Map<String, Double> wiq_2) {
        HashMap<String,Double> ans=new HashMap<>();
        for(Map.Entry<String,Double> entry: wij_wiq.entrySet()){
            String docNo=entry.getKey();
            double mechane=Math.sqrt(wij_2.get(docNo)*wiq_2.get(docNo));
            double cosSim=(entry.getValue()/mechane);
            ans.put(docNo,cosSim);
        }
        return ans;
    }

    private ArrayList<Map.Entry<Documentt,Double>> finalCalculate(Map<String, Double> bm25, Map<String, Double>  cosSim, Map<String, Double> atStart,Map<String,Double> atHeader,Map<Documentt,Double> total) {

        for(Map.Entry<String,Double> entry: bm25.entrySet()) {
            String doc=entry.getKey();
            double newVal=(entry.getValue()*0.97)+(cosSim.get(doc)*0.01)+(atStart.get(doc)*0.01)+(atHeader.get(doc)*0.01);
            total.put(documents.get(doc),newVal);
        }

        ArrayList<Map.Entry<Documentt, Double>> list = new ArrayList<>(total.entrySet());
        list.sort(new Comparator<Map.Entry<Documentt, Double>>() {
            @Override
            public int compare(Map.Entry<Documentt, Double> o1, Map.Entry<Documentt, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        ArrayList<Map.Entry<Documentt, Double>> res = new ArrayList<>();
        for (int i = 0; i < 50 && i < list.size(); i++) {
            res.add(list.get(i));
        }

        return res;

    }

    private String [] spliteDocNo(String postiongForWord) {
        String docInfo=postiongForWord.substring(postiongForWord.indexOf(":")+1,postiongForWord.length());
        return docInfo.split(",");
    }


    public double BM25(String word,String AlldocNo,double docLen,double df){

        double k=1.2;
        double b=0.75;
        double M=documents.size();
        double CWQ=calculateWordFreqQuery(word);
        double CWD=Double.valueOf(AlldocNo.substring(AlldocNo.indexOf("+")+1,AlldocNo.indexOf("~")));
        double mone=(k+1)*CWD;
        double mechne1=CWD;
        double mechne2= k* ( 0.25 + (b*(docLen/avdl)) );
        double mechne=mechne1+mechne2;
        double logMone=M+1;
        double logCalculat=(Math.log10(logMone/df));
        double leftSide=CWQ*(mone/mechne);
        return leftSide*logCalculat;
    }


    private double calculateWordFreqQuery(String word) {
        double ans=0;
        for(int i=0;i<query.size();i++)
            if(query.get(i).equals(word))
                ans++;
        return ans;
    }


    public double Wij_Wiq(String docNo,double docLen,double df,int i){
        double fi=Double.valueOf(docNo.substring(docNo.indexOf("+")+1,docNo.indexOf("~")));
        double tfi=fi/docLen;
        double N=documents.size();
        double idf=Math.log(N/df)/Math.log(2);
        double wij=tfi*idf;
        if(i==1)
            return wij;
        if(i==2)
            return Math.pow(wij,2);
        else
            return 1;
    }

    public ArrayList<Term> showDic() {
        if(dictionary.size()==0)
            return null;

        ArrayList<Term> list = new ArrayList<>(dictionary.values());
        list.sort(new Comparator<Term>() {
            @Override
            public int compare(Term o1, Term o2) {
                String term1 = o1.getTerm();
                String term2 = o2.getTerm();
                return term1.compareToIgnoreCase(term2);
            }
        });

        return list;
    }








}