package Modle;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public ArrayList<String> Rank(ArrayList<String> query){//returns the docNo sorted by rank
        this.query=query;
        Map<String,Double> BM25=new HashMap();
        Map<String,Double> Wij_Wiq=new HashMap();
        Map<String,Double> Wij_2=new HashMap();
        Map<String,Double> Wiq_2=new HashMap();
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
                    //bm25
                    double bm25=BM25(query.get(i),docsWithWord[j],documents.get(docNo).getDocLength());
                    if(BM25.containsKey(docNo))
                        BM25.put(docNo,BM25.get(docNo)+bm25);
                    else
                        BM25.put(docNo,bm25);
                    int df=dictionary.get(query.get(i)).getDf();
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
                    //at start
                    double atStart=AtStart()

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    private String [] spliteDocNo(String postiongForWord) {
        String docInfo=postiongForWord.substring(postiongForWord.indexOf(":")+1,postiongForWord.length());
        return docInfo.split(",");
    }


    public double BM25(String word,String docNo,int docLen){
        double k=0.75;
        double b=0.75;
        int CWQ=calculateWordFreqQuery(word);
        int CWD=Integer.valueOf(docNo.substring(docNo.indexOf("+"),docNo.indexOf("~")));
        double mone=(k+1)*CWD;
        double mechne=CWD+(k*(1-b+(b*(docLen/avdl))));
        double calculat=CWQ*(mone/mechne);
        return calculat;
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


    public double HeadLine(String word, Documentt d){

    }

    public double AtStart(String ){

    }





}