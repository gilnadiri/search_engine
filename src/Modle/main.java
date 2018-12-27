package Modle;

import java.io.*;
import java.util.*;

public class main {
    public static void main(String[] args) throws IOException {
       // test1();
        int a=2;
        double d=3.0;
        System.out.println(a*d);




      Searcher searcher=new Searcher("C:\\Users\\gil nadiri\\Desktop\\dest");
      ArrayList<Map.Entry<String,Double>> myscores=searcher.Search_query("piracy",false,false,new ArrayList<String>(),"C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה\\corpus");
      ArrayList<String> judjescore=checkfromfile();

      relevant_i_back_from_all_the_relevant(myscores,judjescore);

    }

    private static void relevant_i_back_from_all_the_relevant(ArrayList<Map.Entry<String, Double>> myscores, ArrayList<String> judjescore) {
        ArrayList<String> my=new ArrayList<>();
        for(int i=0;i<myscores.size();i++)
            my.add(myscores.get(i).getKey());
        int ans=0;
        for(int i=0;i<myscores.size();i++)
            if(judjescore.contains(my.get(i)));
                ans=ans+1;
        System.out.println("total realy relevant:"+judjescore.size()+"---->"+"relevant i returned:"+ans);
        System.out.println("number of relevant i didnt return:"+(judjescore.size()-ans));

    }

    private static ArrayList<String> checkfromfile() throws IOException {
      ArrayList<String> res=new ArrayList<>();
       BufferedReader bf= new BufferedReader(new FileReader(new File("C:\\Users\\gil nadiri\\Desktop\\אחזור עבודה" + "\\" + "1.txt")));
        for(String line; (line = bf.readLine()) != null; ) {
            if(lineisrelevant(line))
                res.add(line.split(" ")[3]);
        }
        return res;

    }

    private static boolean lineisrelevant(String line) {
        String[] s=line.split(" ");
        if(s[3].equals("1") && s[0].equals("367"))
            return true;
        else return false;
    }


    public static void test1(){
        Indexer test=new Indexer(false,"C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\testCorpus","C:\\Users\\hoday\\hodaya\\שנה ג\\אחזור\\dest");
        test.iniateIndex();

    }
}
