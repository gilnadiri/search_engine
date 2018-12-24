package Modle;

public class TokenInfo {

    public int frequentInDoc=0;
    public String atStart;//0-not at start 1-at start

    public TokenInfo(int frequentInDoc, String atStart) {
        this.frequentInDoc = frequentInDoc;
        this.atStart = atStart;
    }


    public void setFrequentInDoc(int frequentInDoc) {
        this.frequentInDoc = frequentInDoc;
    }

    public void setAtStart(String atStart) {
        this.atStart = atStart;
    }

}
