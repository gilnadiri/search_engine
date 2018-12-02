package Modle;

import ViewModel.View_Model;

import java.util.ArrayList;
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

    public void loadDic(boolean wantToStem, String destination) {
        indexer.loadDic(wantToStem,destination);
    }
}
