package Modle;

import ViewModel.View_Model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Model {

    public Indexer indexer;
    public Searcher searcher;
    public View_Model viewModel;


    public void start(String corpus_path, String destination,boolean wantToStem) {
        indexer=new Indexer(wantToStem,corpus_path,destination);
        indexer.iniateIndex();
    }

    public ArrayList<Term> showDic() {
        if(searcher==null)
            return null;
        return indexer.showDic();
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
}
