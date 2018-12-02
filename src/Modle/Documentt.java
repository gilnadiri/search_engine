package Modle;
import java.io.Serializable;

public class Documentt implements Serializable {

    public String Doc_Name;
    public int Doc_max_tf;    //the freqency of the most common term in the doc
    private int DocLength;   //num of words  //TODo לבדוק אם זה לפני אחרי הפרסר, והאם כולל חזרות
    public int Doc_uniqe_words;
    private String Doc_City;







    public Documentt(String doc_Name, int doc_max_tf, int doc_uniqe_words, String doc_City, int docLength) {
        Doc_Name = doc_Name;
        Doc_max_tf = doc_max_tf;
        Doc_uniqe_words = doc_uniqe_words;
        Doc_City = doc_City;
        DocLength = docLength;

    }

    public int getDocLength() {
        return DocLength;
    }

    public void setDocLength(int docLength) {
        DocLength = docLength;
    }

    public void setDoc_Name(String doc_Name) {
        Doc_Name = doc_Name;
    }

    public void setDoc_max_tf(int doc_max_tf) {
        Doc_max_tf = doc_max_tf;
    }

    public void setDoc_uniqe_words(int doc_uniqe_words) {
        Doc_uniqe_words = doc_uniqe_words;
    }

    public void setDoc_City(String doc_City) {
        Doc_City = doc_City;
    }

    public String getDoc_Name() {
        return Doc_Name;
    }

    public int getDoc_max_tf() {
        return Doc_max_tf;
    }

    public int getDoc_uniqe_words() {
        return Doc_uniqe_words;
    }

    public String getDoc_City() {
        return Doc_City;
    }



}
