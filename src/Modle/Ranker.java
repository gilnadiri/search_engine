package Modle;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ranker {
    public Map<String,Documentt> documents; //information on every doc
    public Map<String,Term> dictionary;     //inverted index of terms
    private boolean semantic;
    public ArrayList<String> query;
    private Parse parser;
    private boolean wantToStemm;
    private String Posting_And_dictionary_path_in_disk;




    public Ranker(ArrayList<String> query,boolean semantic,boolean wantToStemm,String corpusPath, String Posting_And_dictionary_path_in_disk) {
        this.parser=new Parse(corpusPath);
        this.documents = new HashMap<>();
        this.dictionary=new HashMap<>();
        this.query=query;
        this.semantic=semantic;
        this.wantToStemm = wantToStemm;
        this.Posting_And_dictionary_path_in_disk=Posting_And_dictionary_path_in_disk;
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

    public ArrayList<String> Rank(String queryFull){//returns the docNo sorted by rank
        Map<String,Double> BM25=new HashMap();


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
                    double bm25=BM25(query.get(i),docsWithWord[j]);
                    if(BM25.containsKey(docsWithWord[j]))
                        BM25.put(docsWithWord[j],BM25.get(docsWithWord[j])+bm25);
                    else
                        BM25.put(docsWithWord[j],bm25);
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


    public double BM25(String word,String docNo){

        int CWQ=calculateWordFreqQuery(word);
        int CWD=calculateWordFreqDoc(word);

    }

    private int calculateWordFreqDoc(String word) {


    }

    private int calculateWordFreqQuery(String word) {
        int ans=0;
        for(int i=0;i<query.size();i++)
            if(query.get(i).equals(word))
                ans++;
        return ans;
    }

    public double tf_idf(){
    }

    public double HeadLine(){
    }

    public double AtStart(){
    }





}