package ViewModel;

import Modle.Documentt;
import Modle.Model;
import Modle.Term;

import javax.swing.text.View;
import java.util.*;

public class View_Model {

    public Model model;
    private View view;

    public View_Model(Model model) {
        this.model = model;
    }

    public void start(String corpus_path, String destination,boolean wantToStem) {
        model.start(corpus_path,destination,wantToStem);
    }
    /////////
    public ArrayList<Term> showDic() {
        return model.showDic();
    }

    public ArrayList<Map.Entry<Documentt,Double>> Search_single_query(String query, boolean stem, boolean semantic_treatment , ArrayList<String> cities_limitation, String corpuspath,String destQuery ){
        return model.Search_single_query(query,stem,semantic_treatment,cities_limitation,corpuspath,destQuery);
    }

    public LinkedHashMap<String, ArrayList<Map.Entry<Documentt,Double>> > Search_files_quries(String query_file_path, boolean stem, boolean semantic, ArrayList<String> cities_limitation, String corpuspath,String destQuery){
        return model.Search_files_quries(query_file_path,stem,semantic,cities_limitation,corpuspath,destQuery);
    }



    public void reset(String destination) {
        model.reset(destination);
    }

    public boolean loadDic(boolean wantToStem, String destination) {
        return model.loadDic(wantToStem,destination);
    }

    public HashSet<String> getLen(String dest){
        return model.getLeng(dest);
    }

    public ArrayList<String> getCitie(){
        return model.getCitie();
    }

    public int numOfDocs(){
        return model.numOfDocs();
    }

    public int numOfTerms(){
        return model.numOfTerms();
    }


    public int numOfCities() {
        return model.numOfCities();
    }

    public HashSet<String> getLanguages() {
        return model.languages();
    }
}