package ViewModel;

import Modle.Model;
import Modle.Term;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;

public class View_Model {

    public Model model;
    private View view;

    public View_Model(Model model) {
        this.model = model;
    }

    public void start(String corpus_path, String destination,boolean wantToStem) {
        model.start(corpus_path,destination,wantToStem);
    }

    public ArrayList<Term> showDic() {
        return model.showDic();
    }

    public void reset(String destination) {
        model.reset(destination);
    }

    public void loadDic(boolean wantToStem, String destination) {
        model.loadDic(wantToStem,destination);
    }
}
