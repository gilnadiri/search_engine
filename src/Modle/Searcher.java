package Modle;

public class Searcher {

    private Ranker ranker;

    public Searcher (String postingindisk){
        this.ranker=new Ranker(postingindisk);
    }




}
