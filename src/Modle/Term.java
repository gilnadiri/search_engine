package Modle;
import java.io.Serializable;

public class Term implements Serializable {
    private String term;
    private long PointerToPostings;
    private int df;             //number of docs it exist in
    private int total_frequncy_in_the_corpus;


    public Term(String term,long pointerToPostings,int df,int total_frequncy_in_the_corpus) {
        PointerToPostings = pointerToPostings;
        this.term = term;
        this.df=df;
        this.total_frequncy_in_the_corpus=total_frequncy_in_the_corpus;
    }

    public int getTotal_frequncy_in_the_corpus() {
        return total_frequncy_in_the_corpus;
    }

    public void setTotal_frequncy_in_the_corpus(int total_frequncy_in_the_corpus) {
        this.total_frequncy_in_the_corpus = total_frequncy_in_the_corpus;
    }

    public int getDf() {
        return df;
    }

    public void setDf(int df) {
        this.df = df;
    }
    public long getPointerToPostings() {
        return PointerToPostings;
    }

    public void setPointerToPostings(long pointerToPostings) {
        PointerToPostings = pointerToPostings;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
