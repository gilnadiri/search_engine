package Modle;

public class TokenInfo {

    public int frequentInDoc=0;
    public boolean atStart=false;

    public TokenInfo(int frequentInDoc, boolean atStart) {
        this.frequentInDoc = frequentInDoc;
        this.atStart = atStart;
    }


    public void setFrequentInDoc(int frequentInDoc) {
        this.frequentInDoc = frequentInDoc;
    }

    public void setAtStart(boolean atStart) {
        this.atStart = atStart;
    }

}
