package Modle;

import javax.swing.text.html.ListView;
import java.awt.*;
import java.awt.List;
import java.io.*;
import java.util.*;

public class Ranker {
    public Map<String,Documentt> documents; //information on every doc
    public Map<String,Term> dictionary;     //inverted index of terms
    public ArrayList<String> query;
    private String Posting_And_dictionary_path_in_disk;
    int avdl;




    public Ranker(String Posting_And_dictionary_path_in_disk) {
        this.documents = new HashMap<>();
        this.dictionary=new HashMap<>();
        this.Posting_And_dictionary_path_in_disk=Posting_And_dictionary_path_in_disk;
        this.avdl=avdl();
        FileInputStream fis;
        try {
            fis = new FileInputStream(Posting_And_dictionary_path_in_disk + "\\" + "dictionary");
            ObjectInputStream ois = new ObjectInputStream(fis);
            dictionary=(HashMap) ois.readObject();
            ois.close();

            fis = new FileInputStream(Posting_And_dictionary_path_in_disk + "\\" + "documents");
            ObjectInputStream ois1 = new ObjectInputStream(fis);
            documents=(HashMap) ois1.readObject();
            ois1.close();
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private int avdl() {
        int sum=0;
        for (Map.Entry<String,Documentt> entry : documents.entrySet()) {
            sum+=entry.getValue().getDocLength();
        }
        return (sum/documents.size());
    }

    public ArrayList<String> Rank(ArrayList<String> query,boolean filter,ArrayList<String> docsFilter){//returns the docNo sorted by rank
        this.query=query;
        Map<String,Double> BM25=new HashMap();
        Map<String,Double> Wij_Wiq=new HashMap();
        Map<String,Double> Wij_2=new HashMap();
        Map<String,Double> Wiq_2=new HashMap();
        Map<String,Double> AtStart=new HashMap();
        for(int i=0;i<query.size();i++) {
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(Posting_And_dictionary_path_in_disk, "rw");
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
                    //atStart
                    double atStart=Double.valueOf(docsWithWord[j].substring(docsWithWord[j].indexOf("~")+1,docsWithWord[j].length()));
                    if(AtStart.containsKey(docNo))
                        AtStart.put(docNo,AtStart.get(docNo)+atStart);
                    else
                        AtStart.put(docNo,atStart);
                    //bm25
                    int df=dictionary.get(query.get(i)).getDf();
                    double bm25=BM25(query.get(i),docsWithWord[j],documents.get(docNo).getDocLength(),df);
                    if(BM25.containsKey(docNo))
                        BM25.put(docNo,BM25.get(docNo)+bm25);
                    else
                        BM25.put(docNo,bm25);
                    //1=wij*wiq
                    double wij_wiq=Wij_Wiq(docsWithWord[j],documents.get(docNo).getDocLength(),df,1);
                    if(Wij_Wiq.containsKey(docNo))
                        Wij_Wiq.put(docNo,Wij_Wiq.get(docNo)+wij_wiq);
                    else
                        Wij_Wiq.put(docNo,wij_wiq);
                    //2=wij^2
                    double wij=Wij_Wiq(docsWithWord[j],documents.get(docNo).getDocLength(),df,2);
                    if(Wij_2.containsKey(docNo))
                        Wij_2.put(docNo,Wij_2.get(docNo)+wij);
                    else
                        Wij_2.put(docNo,wij);
                    //3=wiq^2
                    double wiq=Wij_Wiq(docsWithWord[j],documents.get(docNo).getDocLength(),df,3);
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
        return finalCalculate(BM25,finalCosSim,AtStart);

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

    private ArrayList<String> finalCalculate(Map<String, Double> bm25, Map<String, Double>  cosSim, Map<String, Double> atStart) {
        HashMap<String,Double> rank=new HashMap<>();
        for(Map.Entry<String,Double> entry: bm25.entrySet()){
            String docNo=entry.getKey();
            double Rank=entry.getValue()*0.4+cosSim.get(docNo)*0.4+atStart.get(docNo)*0.2;
            rank.put(docNo,Rank);
        }

        LinkedList<Map.Entry<String, Double>> list = new LinkedList<>(rank.entrySet());
        list.sort(new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        ArrayList<String> bestRank=new ArrayList<>();
        for(int i=0;i<50&&i<list.size();i++){
            bestRank.add(list.get(i).getKey());
        }

        return bestRank;
    }




    private String [] spliteDocNo(String postiongForWord) {
        String docInfo=postiongForWord.substring(postiongForWord.indexOf(":")+1,postiongForWord.length());
        return docInfo.split(",");
    }


    public double BM25(String word,String docNo,int docLen,int df){
        double k=0.75;
        double b=0.75;
        double M=0.75;
        int CWQ=calculateWordFreqQuery(word);
        int CWD=Integer.valueOf(docNo.substring(docNo.indexOf("+")+1,docNo.indexOf("~")));
        double mone=(k+1)*CWD;
        double mechne=CWD+(k*(1-b+(b*(docLen/avdl))));
        double logMone=M+1;
        double logCalculat=Math.log(logMone/df);
        double leftSide=CWQ*(mone/mechne);
        return leftSide*logCalculat;
    }


    private int calculateWordFreqQuery(String word) {
        int ans=0;
        for(int i=0;i<query.size();i++)
            if(query.get(i).equals(word))
                ans++;
        return ans;
    }


    public double Wij_Wiq(String docNo,int docLen,int df,int i){
        int fi=Integer.valueOf(docNo.substring(docNo.indexOf("+"),docNo.length()));
        double tfi=fi/docLen;
        if(i==1)
            return tfi*df;
        if(i==2)
            return Math.pow(tfi*df,2);
        else
            return 1;
    }








}