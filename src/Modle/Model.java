package Modle;

import ViewModel.View_Model;

import java.io.*;
import java.util.*;

public class Model {

    public Indexer indexer;
    public Searcher searcher;
    public View_Model viewModel;


    public void start(String corpus_path, String destination,boolean wantToStem) {
        indexer=new Indexer(wantToStem,corpus_path,destination);
        indexer.iniateIndex();
        //searcher=new Searcher(destination,wantToStem);
    }

    public HashSet<String> getLeng(String dest){
        HashSet<String> ans=new HashSet<>();
        try{
            BufferedReader br1;
            br1=new BufferedReader(new FileReader(new File(dest + "\\" + "langueges")));

            String line1;
            while ( (line1 = br1.readLine()) != null )
                ans.add(line1);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ans;
    }

    public ArrayList<Term> showDic() {
        return searcher.showDic();
    }

    public void reset(String destination) {
        if(indexer==null){
            indexer=new Indexer(false,"",destination);
        }

        indexer.reset(destination);
    }

    public int numOfDocs(){
        return indexer.numOfDocs();
    }

    public int numOfTerms(){
        return indexer.numOfTerms();
    }


    public boolean loadDic(boolean wantToStem, String destination) {
        if(wantToStem) {
            File docs = new File(destination + "\\" + "docs stem");
            File dictionary = new File(destination + "\\" + "dictionary stem");
            File cities = new File(destination + "\\" + "cities stem");
            if (!docs.exists()||!dictionary.exists()||!cities.exists())
                return false;
        }
        else{
            File docs = new File(destination + "\\" + "docs");
            File dictionary = new File(destination + "\\" + "dictionary");
            File cities = new File(destination + "\\" + "cities");
            if (!docs.exists()||!dictionary.exists()||!cities.exists())
                return false;
        }
        searcher = new Searcher(destination, wantToStem);
        return true;
    }



    public int numOfCities() {
        return indexer.numOfCities();
    }

    public HashSet<String> languages() {
        return indexer.getLanguages();
    }

    public ArrayList<Map.Entry<Documentt,Double>> Search_single_query(String query, boolean stem, boolean semantic_treatment, ArrayList<String> cities_limitation, String corpuspath,String destQuery,boolean saveRes) {
        return searcher.Search_single_query(query,stem,semantic_treatment,cities_limitation,corpuspath,destQuery,true,saveRes);
    }


    public LinkedHashMap<String, ArrayList<Map.Entry<Documentt,Double>> > Search_files_quries(String query_file_path, boolean stem, boolean semantic, ArrayList<String> cities_limitation, String corpuspath,String destQuery,boolean saveRes){
        return searcher.Search_files_quries(query_file_path,stem,semantic,cities_limitation,corpuspath,destQuery,saveRes);
    }

    public ArrayList<String> getCitie(){
        return searcher.getCitie();
    }

}