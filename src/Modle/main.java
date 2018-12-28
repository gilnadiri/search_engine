package Modle;

import java.io.*;
import java.util.*;

public class main {
    public static void main(String[] args) throws IOException {
        int y=0;
        int i=9;



        Searcher searcher=new Searcher("C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\dest");
        ArrayList<Map.Entry<String,Double>> myscores=searcher.Search_query("blood-alcohol fatalities",false,false,new ArrayList<String>(),"C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\corpus");
        ArrayList<String> judjescore=checkfromfile();

        relevant_i_back_from_all_the_relevant(myscores,judjescore);

    }

    private static void relevant_i_back_from_all_the_relevant(ArrayList<Map.Entry<String, Double>> myscores, ArrayList<String> judjescore) {
        ArrayList<String> my=new ArrayList<>();
        for(int i=0;i<myscores.size();i++)
            my.add(myscores.get(i).getKey());
        int ans=0;
        for(int i=0;i<my.size();i++) {
            for (int j = 0; j < judjescore.size(); j++) {
                if (my.get(i).equals(judjescore.get(j)))
                    ans = ans + 1;
            }
        }
        System.out.println("total realy relevant:"+judjescore.size()+"---->"+"relevant i returned:"+ans);
        System.out.println("number of relevant i didnt return:"+(judjescore.size()-ans));

    }

    private static ArrayList<String> checkfromfile() throws IOException {
        ArrayList<String> res=new ArrayList<>();
        BufferedReader bf= new BufferedReader(new FileReader(new File("C:\\Users\\hoday\\Desktop\\destination" + "\\" + "1.txt")));
        for(String line; (line = bf.readLine()) != null; ) {
            if(lineisrelevant(line))
                res.add(line.split(" ")[2]);
        }
        return res;

    }

    private static boolean lineisrelevant(String line) {
        String[] s=line.split(" ");
        if(s[3].equals("1") && s[0].equals("358"))
            return true;
        else return false;
    }


    public static void test1(){
        Indexer test=new Indexer(false,"C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\testCorpus","C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\dest");
        test.iniateIndex();

    }
}