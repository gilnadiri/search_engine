package Modle;

import java.io.RandomAccessFile;
import java.util.*;

public class main {
    public static void main(String[] args) {
        // test1();
        Searcher searcher=new Searcher("C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\dest");
        searcher.Search_query("Falkland petroleum exploration",false,false,new ArrayList<String>(),"C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\corpus");
        int i=0;
        int o=9;
        double t=i+o;



    }






    public static void test1(){
        Indexer test=new Indexer(false,"C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\testCorpus","C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\dest");
        test.iniateIndex();

    }
}