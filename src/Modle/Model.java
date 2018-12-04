package Modle;

import ViewModel.View_Model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Model {

    public Indexer indexer;
    public View_Model viewModel;


    public void start(String corpus_path, String destination,boolean wantToStem) {
        indexer=new Indexer(wantToStem,corpus_path,destination);
        indexer.iniateIndex();
    }

    public ArrayList<Term> showDic() {
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

    public void loadDic(boolean wantToStem, String destination) {
        indexer.loadDic(wantToStem,destination);
    }

    public int numOfCities() {
        return indexer.numOfCities();
    }

    public HashSet<String> languages() {
        return indexer.getLanguages();
    }
}
